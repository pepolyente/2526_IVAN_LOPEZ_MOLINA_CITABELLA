# Contenerización y Despliegue con Docker Compose

# 1. Contenerización con Docker Compose

El sistema se despliega mediante **Docker Compose**, que define y orquesta tres servicios principales:

```yaml
services:
  citabella-db:       # MySQL 8
  citabella-api:      # Backend Spring Boot
  citabella-web:      # Nginx + Angular
```

---

## Servicios incluidos

### citabella-db

- Base de datos MySQL 8

### citabella-api

- Backend Spring Boot
- Imagen construida desde Dockerfile multistage

### citabella-web

- Nginx
- Servidor estático Angular
- Proxy inverso hacia la API

---

## Orden de arranque

1. Base de datos (`citabella-db`)
2. Backend (`citabella-api`)
3. Frontend / Nginx (`citabella-web`)

La API espera a que MySQL esté disponible antes de inicializarse.

---

# 2. Descripción de Contenedores

| Servicio        | Imagen base    | Función principal        | Puerto interno | Puerto expuesto     |
|-----------------|----------------|--------------------------|----------------|---------------------|
| `citabella-db`  | `mysql:8`      | Base de datos relacional | `3306`         | No confirmado       |
| `citabella-api` | `openjdk:21`   | API REST Spring Boot     | `8080`         | `8080` (desarrollo) |
| `citabella-web` | `nginx:alpine` | Angular + proxy inverso  | `80`           | `4200` o `80`       |

---

> **Nota:**  
> Los puertos exactos no aparecen definidos explícitamente en la memoria.
>
> Se deduce:
>
> - Desarrollo:
    >   - `4200:80`
    >
- `8080:8080`
>
> - Producción:
    >   - Nginx expone `80/443`

---

# 3. Persistencia de Datos

El contenedor MySQL utiliza un volumen Docker persistente para conservar la información entre reinicios.

---

## Variables de entorno

La configuración se inyecta mediante:

```yaml
environment:
```

Desde `docker-compose.yml`.

Esto permite sobrescribir propiedades del backend sin recompilar.

---

## Ejemplos de variables

```env
MYSQL_HOST=citabella-db
MYSQL_DATABASE=citabella
JWT_SECRET=secret
```

---

## Ventajas

- configuración desacoplada
- distintos entornos
- seguridad mejorada
- despliegue flexible

---

# 4. Nginx como Proxy Inverso

La configuración de Nginx integra dos funciones principales:

- servir Angular
- redirigir peticiones API

---

# Servir Frontend Angular

Los archivos compilados se sirven desde:

```plaintext
/usr/share/nginx/html
```

---

## Soporte SPA Angular

```nginx
location / {
    try_files $uri /index.html;
}
```

Esto permite:

- recargar rutas Angular
- navegación cliente-side
- soporte SPA completo

---

# Proxy hacia Backend

```nginx
location /api/ {
    proxy_pass http://citabella-api:8080;
}
```

---

## Funcionalidades

- redirección automática a Spring Boot
- mantenimiento de cabeceras
- punto de entrada único

---

## Beneficios

- simplificación de acceso
- separación frontend/backend
- mayor seguridad
- despliegue centralizado

---

# 5. Flujo de Despliegue

# Requisitos previos

- Docker Desktop
- Docker Compose

Opcionalmente:

- Java 21
- Node.js
- Angular CLI

---

# Construcción

## Frontend

```bash
ng build
```

Genera archivos estáticos para Nginx.

---

## Backend

```bash
mvn package
```

Genera el archivo `.jar`.

Posteriormente:

- Docker construye la imagen
- usando Dockerfile multistage

---

# Despliegue

Desde la raíz del proyecto:

```bash
docker-compose up -d
```

---

## Comunicación interna

Los contenedores se comunican mediante:

- red Docker interna
- nombres de servicio Compose

---

# Verificación

Acceder al frontend desde:

```plaintext
http://localhost
```

O el puerto configurado.

---

## Comprobaciones

- navegación pública
- login
- acceso al panel privado
- comunicación API

---

# 6. Pipeline Inferido

Aunque no se documenta un pipeline CI/CD explícito, el uso de:

- Git
- GitHub
- separación frontend/backend

Sugiere compatibilidad sencilla con:

- GitHub Actions
- CI/CD automatizado
- despliegues automáticos

---

> **Nota:**  
> No existe confirmación explícita en la memoria técnica.

---

# 7. Recomendaciones Técnicas

## Variables sensibles

Mantener toda configuración sensible mediante variables de entorno:

- JWT
- MySQL
- secretos

---

## Archivo recomendado

```plaintext
.env.example
```

Para documentar variables necesarias.

---

# Seguridad en Producción

## Exponer únicamente:

- `80`
- `443`

Desde Nginx.

---

## Evitar

- exponer MySQL al host
- abrir puertos internos innecesarios

---

# Healthchecks

Añadir:

```yaml
healthcheck:
```

En los contenedores para:

- detección de fallos
- reinicio automático
- mayor resiliencia

---

# HTTPS

Configurar:

- Let's Encrypt
- certificados SSL/TLS

Cuando el sistema pase a producción real.

---