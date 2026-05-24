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
10. [Recomendaciones técnicas y checklist para producción](#10-recomendaciones-técnicas-y-checklist-para-producción)

---

## 1. Arquitectura de contenedores

El sistema CitaBella se despliega mediante **Docker Compose**, orquestando tres servicios que se comunican a través de una red interna aislada y compartida (`app-net`):

```yaml
services:
  mysql      # MySQL 8 — Base de datos relacional
  backend    # Spring Boot — API REST
  nginx      # Nginx + Angular — Frontend estático y proxy inverso

```

### Orden de arranque lógico

```
mysql  →  backend  →  nginx

```

El backend mitiga problemas de sincronización esperando a que el puerto TCP de MySQL esté completamente abierto usando el script `wait-for-it.sh` antes de iniciar la Máquina Virtual de Java (JVM). Nginx, a su vez, solo arranca cuando el backend supera con éxito su healthcheck HTTP (`depends_on` con condición `service_healthy`).

---

## 2. Descripción de servicios

| Servicio | Imagen base | Contenedor | Función | Puerto interno | Puerto expuesto | Notas |
| --- | --- | --- | --- | --- | --- | --- |
| `mysql` | `mysql:8` | `citabella-mysql` | Base de datos relacional | `3306` | No expuesto | Volumen persistente `mysql_data` |
| `backend` | `maven:3.9.9-eclipse-temurin-17` | `citabella-backend` | API REST Spring Boot | `8080` | No expuesto | Construido con Dockerfile multietapa, incluye healthcheck |
| `nginx` | `nginx:alpine` | `citabella-nginx` | Angular + Proxy Inverso | `80` | `80:80` | Monta configuración personalizada `./nginx/nginx.conf` |

> ⚠️ **Aislamiento de red:** Solo Nginx expone un puerto al host externo. MySQL y el backend se ejecutan exclusivamente de manera interna, permaneciendo invisibles fuera de la red virtual de Docker (`app-net`).

El servicio `nginx` utiliza un montaje de tipo bind de solo lectura para sobrescribir el archivo de configuración por defecto con `./nginx/nginx.conf`. Esto permite ajustar las reglas del proxy inverso en caliente sin necesidad de reconstruir la imagen.

---

## 3. Variables de entorno

La configuración sensible y las credenciales operativas se inyectan mediante un archivo `.env` ubicado en la raíz del proyecto. Se incluye un archivo `.env.example` a modo de plantilla.

### Plantilla `.env.example`

```env
# Base de datos
DB_NAME=your_db_name
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# Admin inicial
ADMIN_USERNAME=citabella
ADMIN_EMAIL=admin@citabella.com
ADMIN_PASSWORD=citabella123

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000

```

### Mapeo e Inyección en Docker Compose

```yaml
# Servicio mysql
environment:
  MYSQL_DATABASE: ${DB_NAME}
  MYSQL_USER: ${DB_USER}
  MYSQL_PASSWORD: ${DB_PASSWORD}
  MYSQL_ROOT_PASSWORD: root            # ⚠️ Cambiar inmediatamente en producción

# Servicio backend
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
  SPRING_DATASOURCE_USERNAME: ${DB_USER}
  SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
  SPRING_PROFILES_ACTIVE: docker
  JWT_SECRET: ${JWT_SECRET}
  JWT_EXPIRATION: ${JWT_EXPIRATION}
  ADMIN_USERNAME: ${ADMIN_USERNAME}
  ADMIN_EMAIL: ${ADMIN_EMAIL}
  ADMIN_PASSWORD: ${ADMIN_PASSWORD}

```

### Gestión de Datos Demo y Perfil de Spring Boot

A diferencia de las credenciales, la activación de los datos de prueba está vinculada de forma estática al perfil de ejecución de Docker. Dentro de tu archivo `application-docker.properties`, la propiedad está configurada de la siguiente manera:

```properties
app.demo.enabled=true

```

| Propiedad | Estado por Defecto | Comportamiento del Inicializador |
| --- | --- | --- |
| `app.demo.enabled` | `true` | Si al levantar el contenedor la base de datos está vacía (`userRepository.count() == 0`), el componente `DemoDataInitializer` poblará de forma automática la base de datos con: 4 roles, 1 administrador, 10 empleados, 6 usuarios, 45 clientes, 16 tratamientos y 20 productos para agilizar los tests del MVP. |

---

## 4. Persistencia de datos

MySQL monta un volumen Docker nombrado para conservar el estado de la información ante reinicios, paradas o recreaciones de los contenedores:

```yaml
volumes:
  - mysql_data:/var/lib/mysql

volumes:
  mysql_data:

```

El volumen `mysql_data` es gestionado directamente por el demonio de Docker y persiste en el disco físico del host de manera indefinida hasta que se elimine explícitamente mediante el comando `docker volume rm`. El backend y Nginx no requieren volúmenes de estado (son stateless).

---

## 5. Dockerfiles

### Backend — Multistage build (Java 17)

```text
backend/
├── Dockerfile
├── wait-for-it.sh
└── CitaBellaAPI/
    ├── pom.xml
    └── src/

```

**`backend/Dockerfile`**

```dockerfile
# Etapa 1 — Compilación y construcción
FROM maven:3.9.9-eclipse-temurin-17 AS build
LABEL authors="shdwd"

WORKDIR /app

COPY CitaBellaAPI/pom.xml .
RUN mvn dependency:go-offline

COPY CitaBellaAPI/src ./src
RUN mvn clean package -DskipTests

# Etapa 2 — Imagen ligera de ejecución
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN apt-get update && apt-get install -y netcat-openbsd curl

COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]

```

### Frontend — Multistage build (Node 22 + Nginx)

```text
frontend/
├── Dockerfile
└── citabella-web/
    ├── package.json
    └── src/

```

**`frontend/Dockerfile`**

```dockerfile
# Etapa 1 — Compilación de la SPA en Angular
FROM node:22-alpine AS build
LABEL authors="shdwd"

WORKDIR /app

COPY citabella-web/package*.json ./
RUN npm install

COPY citabella-web/ .
RUN npm run build -- --configuration production

# Etapa 2 — Servidor web estático
FROM nginx:alpine

COPY --from=build /app/dist/*/browser /usr/share/nginx/html

EXPOSE 80

```

---

## 6. Nginx como proxy inverso

El archivo `./nginx/nginx.conf` local se inyecta en el contenedor en la ruta `/etc/nginx/nginx.conf`, actuando como la pasarela de entrada única del sistema.

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

        # Proxy hacia el backend de Spring Boot
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

### Decisiones de diseño de infraestructura

* **`resolver 127.0.0.11` junto con `set $upstream`:** Obliga a Nginx a resolver la dirección IP del contenedor `backend` en tiempo de ejecución (bajo demanda) y no durante el arranque del servidor web. Esto previene que Nginx sufra un *crash* si el backend tarda unos segundos de más en inicializarse.
* **`try_files $uri $uri/ /index.html`:** Esencial para soportar el enrutamiento interno de Angular (SPA). Cualquier ruta no física en el servidor se redirige al `index.html` para que el enrutador de Angular tome el control.

---

## 7. Arranque y despliegue

### Requisitos previos

* Docker Desktop o Docker Engine + Docker Compose v2.
* Archivo `.env` configurado en la raíz del proyecto (basado en `.env.example`).

### Levantar el entorno completo

```bash
docker compose up -d

```

> En la primera ejecución, Docker descargará las imágenes base y compilará tanto el código Java (Maven) como el de Angular (Node). El proceso puede tomar unos minutos.

### Recompilar la aplicación tras cambios de código

```bash
docker compose up -d --build

```

### Apagar el entorno de forma segura

```bash
docker compose down        # Detiene contenedores y redes (los datos de la BD persisten)
docker compose down -v     # ⚠️ ¡Peligro! Detiene el entorno y destruye el volumen de datos

```

---

## 8. Healthchecks y disponibilidad

El backend implementa un mecanismo de verificación utilizando Spring Boot Actuator para asegurar que el sistema no reciba tráfico antes de estar completamente operativo.

```yaml
# Configuración en docker-compose.yml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 10s
  timeout: 5s
  retries: 10
  start_period: 40s

```

Nginx se mantiene en pausa y solo arranca una vez que la verificación anterior devuelve un estado saludable (`service_healthy`):

```yaml
depends_on:
  backend:
    condition: service_healthy

```

---

## 9. Pipeline inferido (CI/CD)

Aunque la Fase 1 / MVP se enfoca en un despliegue local autocontenido mediante Docker Compose, la modularización del proyecto facilita su migración inmediata hacia un pipeline automatizado en **GitHub Actions**:

```yaml
# .github/workflows/deploy.yml (Esquema de integración futura)
on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Docker Images
        run: docker compose build
      - name: Production Run
        run: docker compose up -d

```

---

## 10. Recomendaciones técnicas y checklist para producción

### Seguridad general

* Cambiar la clave de administración de la base de datos (`MYSQL_ROOT_PASSWORD`) por un hash complejo dentro de tu archivo `.env`.
* Configurar un certificado SSL (HTTPS) mapeando puertos `443` utilizando Let's Encrypt o Nginx para securizar los tokens JWT en tránsito.

### Desactivación de Datos Demo en Producción (Prioridad Crítica)

Como en tu archivo `application-docker.properties` la propiedad viene configurada en `true` (`app.demo.enabled=true`), dejar este comportamiento en producción poblaría el entorno real con datos de test obsoletos y credenciales de prueba inseguras.

Para anular esta propiedad de forma limpia y transparente **sin modificar el código fuente empaquetado**, se debe aprovechar el orden de precedencia de propiedades de Spring Boot. Las variables de entorno inyectadas por el sistema operativo tienen prioridad absoluta sobre los archivos `.properties` internos.

Basta con añadir la variable directamente en el bloque `environment` del servicio `backend` en tu archivo `docker-compose.yml` para producción:

```yaml
backend:
  environment:
    - APP_DEMO_ENABLED=false

```

> **Nota de arquitectura:** Spring Boot hace uso de *relaxed binding* (enlace relajado), convirtiendo de forma automática la variable de entorno `APP_DEMO_ENABLED=false` en la propiedad normalizada `app.demo.enabled=false` durante la carga del contexto, anulando el valor original de forma segura.

### Checklist definitivo para despliegue productivo

* [ ] `.env` creado con secretos reales y fuera del control de versiones (añadido a `.gitignore`).
* [ ] `MYSQL_ROOT_PASSWORD` modificado con un valor seguro.
* [ ] Variable `APP_DEMO_ENABLED=false` inyectada en el entorno del backend.
* [ ] Healthchecks de Spring Actuator respondiendo correctamente en el despliegue.
* [ ] Certificación SSL/HTTPS activa a nivel de proxy inverso.

```

```