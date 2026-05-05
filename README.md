# CitaBella

> **CitaBella** es un proyecto desarrollado como parte del módulo de **Proyecto Intermodular** del  
> **Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.  
>  
> Su objetivo es **digitalizar la gestión integral de una peluquería real**, sustituyendo los procesos manuales por una solución moderna, segura y escalable, basada en una **arquitectura contenerizada**.

---

## Descripción

CitaBella es una plataforma de gestión compuesta por:
- Una **aplicación web**
- Una **aplicación móvil Android**
- Un **backend con API REST**
- Un **sistema de despliegue basado en Docker**

El sistema está diseñado para funcionar como un **SaaS**, donde todos los servicios se levantan de forma conjunta mediante **Docker Compose**, comunicándose a través de redes internas de Docker y exponiendo únicamente un **proxy inverso (Nginx)** al exterior.

---

## Objetivos del Proyecto

- Desarrollar una **aplicación web y móvil** para la gestión de citas, clientes y servicios.  
- Implementar un **sistema de autenticación y autorización** con distintos roles.  
- Gestionar **ventas, productos, stock y servicios**.  
- Integrar un **ERP-lite** para estadísticas y control del negocio.  
- Diseñar una **arquitectura modular, segura y escalable**.  
- Facilitar el **despliegue completo del sistema con un solo comando**.  

---

## Tecnologías Utilizadas

| **Componente** | **Tecnología / Herramienta** |
|----------------|------------------------------|
| Lenguaje backend | Java |
| Framework backend | Spring Boot (API REST) |
| Base de datos | MySQL |
| ORM | JPA / Hibernate |
| Frontend web | Angular |
| Aplicación móvil | Android Studio (Java / Kotlin) |
| Seguridad | Spring Security + JWT |
| Proxy inverso | Nginx |
| Contenerización | Docker / Docker Compose |
| Diagramas | Draw.io |
| IDEs | IntelliJ IDEA, Android Studio, Visual Studio Code |
| Control de versiones | Git / GitHub |

---

## Arquitectura General

El sistema se basa en una **arquitectura contenerizada y desacoplada**, con un único punto de entrada:

- El **backend y la base de datos no exponen puertos al exterior**.
- Toda la comunicación externa se realiza a través de **Nginx** como **proxy inverso**.
- Los servicios se comunican entre sí mediante **redes internas de Docker**.

---

## 📁 Estructura del Repositorio

```bash
2526_IVAN_LOPEZ_MOLINA_CITABELLA/
│
├── backend/
│   └── CitaBellaAPI/          # API REST Spring Boot
│       └── Dockerfile
│
├── frontend/                  # Aplicación web
│
├── db/
│   └── init/                  # Scripts SQL de inicialización
│
├── nginx/
│   └── nginx.conf             # Proxy inverso
│
├── docker-compose.yml         # Orquestación completa del sistema
├── docs/                      # Documentación y diagramas
├── README.md                  # Este documento
└── LICENSE                    # Licencia del proyecto
```
---

## 🐳 Despliegue con Docker

El sistema completo puede levantarse mediante un único comando:
```bash
docker compose up
```
Este comando inicia:

- Base de datos MySQL
- Backend Spring Boot
- Proxy inverso Nginx
- (En el futuro) Frontend servido desde Nginx

---

## Ramas del Proyecto

| **Rama**       | **Propósito**                           |
| -------------- | --------------------------------------- |
| `main`         | Versión estable para revisión o entrega |
| `dev`          | Rama de desarrollo principal            |
| `dev-backend`  | Desarrollo del backend Spring Boot      |
| `dev-frontend` | Desarrollo del frontend web             |
| `dev-android`  | Desarrollo de la app Android            |
| `docs`         | Documentación y memoria técnica         |

---

## Instalación y Ejecución
Backend (Dockerizado)
```bash
# Clonar el repositorio
git clone https://github.com/pepolyente/2526_IVAN_LOPEZ_MOLINA_CITABELLA.git

# Entrar al proyecto
cd 2526_IVAN_LOPEZ_MOLINA_CITABELLA

# Levantar todos los servicios
docker compose up
```
---

Frontend
```bash
# Entrar en la carpeta del frontend
cd frontend

# Instalar dependencias
npm install

# Ejecutar en entorno de desarrollo
ng serve
```
---

Android
```bash
# Abrir el proyecto en Android Studio
# Configurar la URL base de la API (Nginx)
# Compilar y ejecutar en emulador o dispositivo físico
```

---

## 📊 Estado del Proyecto

> Estado actual de desarrollo del sistema CitaBella

- [x] 🧩 Diseño y planificación

- [x] ⚙️ Backend funcional

- [ ] 🐳 Arquitectura Docker definida

- [ ] 💻 Frontend web en desarrollo

- [ ] 📱 Aplicación Android en desarrollo

- [ ] 📈 ERP-lite avanzado

- [ ] 📝 Documentación finalizada

---

## 🗓️ Planificación del Proyecto
| **Fase** | **Periodo**    | **Objetivo principal**              |
| -------- | -------------- | ----------------------------------- |
| Fase 1   | Oct – Nov 2025 | Diseño y modelado del sistema       |
| Fase 2   | Dic 2025       | Arquitectura Docker y base de datos |
| Fase 3   | Ene – Feb 2026 | Backend Spring Boot                 |
| Fase 4   | Mar 2026       | Frontend web                        |
| Fase 5   | Abr 2026       | Aplicación Android                  |
| Fase 6   | May – Jun 2026 | Integración y documentación         |

## 👤 Autor

**Iván López Molina**
- 🎓 *Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)*
- 📅 *Curso 2025–2026*
- 📍 *La Pobla de Vallbona*

🔗 [LinkedIn](https://www.linkedin.com/in/ivan-lopez-molina) · [GitHub](https://github.com/pepolyente)

---
## 🪪 Licencia

Copyright (c) 2025 Iván López Molina
All rights reserved.

Este código fuente y sus archivos asociados están protegidos por derechos de autor.
No se permite su uso, reproducción, modificación, distribución o publicación total o parcial sin el consentimiento expreso del autor.

> ⚠️ **Nota:** Este repositorio es privado y el contenido aún está en desarrollo.
