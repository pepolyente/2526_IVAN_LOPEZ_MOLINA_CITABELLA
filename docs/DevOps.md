# DevOps — Contenerización y Despliegue con Docker Compose

## Índice

1. [Arquitectura de contenedores](#1-arquitectura-de-contenedores)
2. [Descripción de servicios](#2-descripción-de-servicios)
3. [Variables de entorno](#3-variables-de-entorno)
4. [Persistencia de datos](#4-persistencia-de-datos)
5. [Dockerfiles](#5-dockerfiles)
6. [Nginx como proxy inverso](#6-nginx-como-proxy-inverso)
7. [Arranque y despliegue](#7-arranque-y-despliegue)
8. [Healthchecks y disponibilidad](#8-healthchecks-y-disponibilidad)
9. [Pipeline inferido (CI/CD)](#9-pipeline-inferido-cicd)
10. [Recomendaciones técnicas](#10-recomendaciones-técnicas)

---

## 1. Arquitectura de contenedores

El sistema CitaBella se despliega mediante **Docker Compose**, orquestando tres servicios que se comunican a través de
una red interna compartida (`app-net`):

```yaml
services:
  mysql      # MySQL 8 — base de datos relacional
  backend    # Spring Boot — API REST
  nginx      # Nginx + Angular — frontend y proxy inverso
```

### Orden de arranque

```
mysql  →  backend  →  nginx
```

El backend espera a MySQL usando el script `wait-for-it.sh` antes de iniciar la JVM. Nginx, a su vez, solo arranca
cuando el backend supera su healthcheck HTTP.

---

## 2. Descripción de servicios

| Servicio  | Imagen base                      | Contenedor          | Función                  | Puerto interno | Puerto expuesto |
|-----------|----------------------------------|---------------------|--------------------------|----------------|-----------------|
| `mysql`   | `mysql:8`                        | `citabella-mysql`   | Base de datos relacional | `3306`         | No expuesto     |
| `backend` | `maven:3.9.9-eclipse-temurin-17` | `citabella-backend` | API REST Spring Boot     | `8080`         | No expuesto     |
| `nginx`   | `nginx:alpine`                   | `citabella-nginx`   | Angular + proxy inverso  | `80`           | `80:80`         |

> Solo Nginx expone un puerto al host. MySQL y el backend son exclusivamente internos, accesibles únicamente dentro de
> la red `app-net`.

---

## 3. Variables de entorno

La configuración sensible se inyecta mediante un fichero `.env` en la raíz del proyecto. Se incluye `.env.example` como
plantilla:

```env
# Base de datos
DB_NAME=your_db_name
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000
```

Para empezar, copia el fichero de ejemplo y rellena tus valores:

```bash
cp .env.example .env
```

Estas variables se consumen en `docker-compose.yml` de la siguiente forma:

```yaml
# Servicio mysql
environment:
  MYSQL_DATABASE: ${DB_NAME}
  MYSQL_USER: ${DB_USER}
  MYSQL_PASSWORD: ${DB_PASSWORD}
  MYSQL_ROOT_PASSWORD: root   # ⚠️ Cambiar en producción

# Servicio backend
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
  SPRING_DATASOURCE_USERNAME: ${DB_USER}
  SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
  SPRING_PROFILES_ACTIVE: docker
  JWT_SECRET: ${JWT_SECRET}
  JWT_EXPIRATION: ${JWT_EXPIRATION}
```

---

## 4. Persistencia de datos

MySQL monta un volumen Docker nombrado para conservar los datos entre reinicios y recreaciones del contenedor:

```yaml
volumes:
  - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

El volumen `mysql_data` es gestionado por Docker y persiste hasta que se elimine explícitamente con `docker volume rm`.

---

## 5. Dockerfiles

### Backend — Multistage build (Java 17)

```
backend/
├── Dockerfile
├── wait-for-it.sh
└── CitaBellaAPI/
    ├── pom.xml
    └── src/
```

**`backend/Dockerfile`**

```dockerfile
# Etapa 1 — compilación
FROM maven:3.9.9-eclipse-temurin-17 AS build
LABEL authors="shdwd"

WORKDIR /app

COPY CitaBellaAPI/pom.xml .
RUN mvn dependency:go-offline

COPY CitaBellaAPI/src ./src
RUN mvn clean package -DskipTests

# Etapa 2 — imagen de ejecución
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN apt-get update && apt-get install -y netcat-openbsd curl

COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]
```

La imagen de producción es ligera: solo contiene el `.jar` y las herramientas necesarias para los healthchecks. La
imagen de build (Maven + JDK completo) no se incluye en el artefacto final.

**`backend/wait-for-it.sh`**

Script de espera activa: sondea el puerto TCP de MySQL con `nc` antes de lanzar la JVM. Esto complementa el `depends_on`
de Compose, que solo garantiza que el contenedor MySQL está en ejecución, no que el servidor de base de datos está listo
para aceptar conexiones.

```sh
#!/bin/sh
host="$1"
shift

if [ "$1" = "--" ]; then
  shift
fi

until nc -z ${host%:*} ${host#*:}; do
  echo "⏳ Esperando a MySQL en $host..."
  sleep 2
done

echo "✅ MySQL disponible - arrancando backend"
exec "$@"
```

---

### Frontend — Multistage build (Node 22 + Nginx)

```
frontend/
├── Dockerfile
└── citabella-web/
    ├── package.json
    └── src/
```

**`frontend/Dockerfile`**

```dockerfile
# Etapa 1 — compilación Angular
FROM node:22-alpine AS build
LABEL authors="shdwd"

WORKDIR /app

COPY citabella-web/package*.json ./
RUN npm install

COPY citabella-web/ .
RUN npm run build -- --configuration production

# Etapa 2 — servidor estático
FROM nginx:alpine

COPY --from=build /app/dist/*/browser /usr/share/nginx/html

EXPOSE 80
```

Los artefactos compilados se copian desde `dist/*/browser` (salida estándar de Angular 17+) directamente a la raíz de
Nginx.

---

## 6. Nginx como proxy inverso

**`nginx/nginx.conf`**

```nginx
events {}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    resolver 127.0.0.11 valid=10s;   # DNS interno de Docker

    server {
        listen 80;

        # Servir Angular (SPA)
        location / {
            root  /usr/share/nginx/html;
            index index.html;
            try_files $uri $uri/ /index.html;
        }

        # Proxy hacia el backend
        location /api/ {
            set $upstream http://backend:8080;
            proxy_pass $upstream;
            proxy_http_version 1.1;
            proxy_set_header Host             $host;
            proxy_set_header X-Real-IP        $remote_addr;
        }
    }
}
```

### Decisiones de diseño

| Elemento                           | Motivo                                                                                                                        |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| `resolver 127.0.0.11`              | Resuelve nombres de servicio Compose en tiempo de petición, no en el arranque. Evita errores si el backend aún no está listo. |
| `set $upstream`                    | Combina con el resolver para que Nginx no falle al iniciar si `backend` todavía no tiene IP asignada.                         |
| `try_files $uri $uri/ /index.html` | Soporte completo de SPA: las rutas Angular del lado del cliente se redirigen al `index.html`.                                 |
| Puerto único (`80`)                | El cliente solo necesita un punto de entrada; backend y base de datos permanecen internos.                                    |

---

## 7. Arranque y despliegue

### Requisitos previos

- Docker Desktop (o Docker Engine + Docker Compose v2)
- Fichero `.env` configurado (ver [sección 3](#3-variables-de-entorno))

> Para desarrollo local sin Docker: Java 17, Maven 3.9+, Node.js 22, Angular CLI.

### Levantar el entorno

```bash
# Desde la raíz del proyecto
docker compose up -d
```

Docker construirá automáticamente las imágenes de `backend` y `nginx` en el primer arranque.

### Reconstruir tras cambios en el código

```bash
docker compose up -d --build
```

### Detener el entorno

```bash
docker compose down          # Detiene y elimina contenedores (los datos persisten)
docker compose down -v       # ⚠️ Elimina también el volumen mysql_data
```

### Verificación

```bash
# Ver logs de todos los servicios
docker compose logs -f

# Ver logs de un servicio concreto
docker compose logs -f backend

# Comprobar estado de los contenedores
docker compose ps
```

Acceder a la aplicación en: `http://localhost`

Comprobaciones manuales recomendadas tras el arranque:

- Navegación pública (home, rutas sin autenticación)
- Login y generación de token JWT
- Acceso al panel privado (rutas protegidas)
- Llamadas a la API visibles en las DevTools del navegador

---

## 8. Healthchecks y disponibilidad

El backend expone el endpoint de Spring Actuator para que Compose verifique su estado antes de arrancar Nginx:

```yaml
# En docker-compose.yml — servicio backend
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 10s
  timeout: 5s
  retries: 10
  start_period: 40s
```

```yaml
# En docker-compose.yml — servicio nginx
depends_on:
  backend:
    condition: service_healthy
```

El flujo de disponibilidad completo es:

```
mysql arranca
    → wait-for-it.sh confirma TCP 3306
        → JVM inicia
            → /actuator/health devuelve 200
                → nginx arranca
```

---

## 9. Pipeline inferido (CI/CD)

El proyecto no documenta un pipeline CI/CD explícito actualmente, pero la estructura (Git, GitHub, separación
frontend/backend, builds reproducibles con Docker) es directamente compatible con **GitHub Actions**:

```yaml
# Ejemplo orientativo — .github/workflows/deploy.yml
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build and push images
        run: docker compose build
      - name: Deploy
        run: docker compose up -d
```


---

## 10. Cambios para producción

### Seguridad en producción

- Cambiar `MYSQL_ROOT_PASSWORD: root` por una contraseña robusta gestionada desde el `.env`.
- Exponer únicamente los puertos `80` y `443` desde Nginx. MySQL y el backend deben permanecer internos.
- Configurar HTTPS con Let's Encrypt (Certbot + Nginx) antes del despliegue público.
- Rotar `JWT_SECRET` y usar un valor de al menos 256 bits de entropía.

### HTTPS con Let's Encrypt (producción)

```nginx
server {
    listen 443 ssl;
    ssl_certificate     /etc/letsencrypt/live/tudominio.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/tudominio.com/privkey.pem;
    # ... resto de la configuración
}

server {
    listen 80;
    return 301 https://$host$request_uri;
}
```

### Checklist antes de producción

- [ ] `.env` con secretos reales fuera del repositorio
- [ ] `MYSQL_ROOT_PASSWORD` no es `root`
- [ ] HTTPS configurado
- [ ] Healthchecks activos en todos los servicios
- [ ] `docker compose logs` sin errores tras el primer arranque
- [ ] Volumen `mysql_data` con política de backup