# CitaBella

> **CitaBella** es un proyecto desarrollado como parte del módulo de **Proyecto** del  
> **Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.  
> Su objetivo principal es **digitalizar la gestión de citas, clientes y servicios** de la peluquería local *Ruth Molina*, actualmente gestionada de forma manual.

---

## Descripción

El sistema estará compuesto por:
- Una **aplicación web**
- Una **aplicación móvil Android**
- Un **backend con API REST**

Además, incluirá un **módulo ERP-lite** para visualizar estadísticas y facilitar la toma de decisiones dentro del negocio.

---

## Objetivos del Proyecto

-  Desarrollar una **aplicación web y móvil** para la gestión de citas, clientes y servicios.  
-  Implementar un **sistema de autenticación** con distintos roles (cliente y administrador).  
-  Permitir la **visualización de estadísticas** y control básico de productos y servicios.  
-  Diseñar una **arquitectura modular** que facilite el mantenimiento y la escalabilidad.  
-  Documentar adecuadamente el **proceso de desarrollo y despliegue**.  

---

## Tecnologías Utilizadas

| **Componente** | **Tecnología / Herramienta** |
|----------------|------------------------------|
| Lenguaje backend | Java |
| Framework backend | Spring Boot (APIs REST) |
| Base de datos | MySQL con JPA / Hibernate |
| Frontend web | HTML, CSS, Bootstrap, JavaScript (AJAX) |
| Aplicación móvil | Android Studio (Java / Kotlin) |
| Seguridad | Spring Security |
| Despliegue | Docker, XAMPP o máquina virtual |
| Diagramas | Draw.io |
| IDEs | IntelliJ IDEA, Android Studio, Visual Studio Code |
| Control de versiones | Git / GitHub |

---

## Arquitectura General

El proyecto se compone de **tres módulos principales**:

### 🔹 Backend (Spring Boot)
API REST que gestiona la comunicación entre la base de datos y las aplicaciones cliente.

### 🔹 Frontend Web
Interfaz accesible para clientes y administradores, con gestión de citas y visualización de datos.

### 🔹 Aplicación Android
Aplicación nativa para la reserva y gestión de citas desde dispositivos móviles.

 El **módulo ERP-lite** se integrará en el backend y permitirá generar estadísticas como:
- Citas mensuales  
- Servicios más demandados  
- Clientes recurrentes  
- Control básico de productos  

---

## 📁 Estructura del Repositorio
```bash
2526_IVAN_LOPEZ_MOLINA_CITABELLA/
│
├── backend/ # API REST con Spring Boot 
├── frontend-web/ # Aplicación web (HTML, CSS, JS) 
├── app-android/ # Aplicación Android nativa 
├── database/ # Diseño y scripts SQL 
├── docs/ # Documentación, diagramas y memoria 
├── docker/ # Configuración de contenedores 
├── README.md # Este documento 
└── LICENSE # Licencia del proyecto 
```

---

## 🗓️ Planificación del Proyecto

| **Fase** | **Periodo** | **Objetivo principal** |
|-----------|--------------|------------------------|
| Fase 1 | Oct – Nov 2025 | Diseño, planificación y aprendizaje base |
| Fase 2 | Dic 2025 – Ene 2026 | Implementación del backend y base de datos |
| Fase 3 | Feb – Mar 2026 | Desarrollo del frontend web |
| Fase 4 | Abr 2026 | Desarrollo de la aplicación Android |
| Fase 5 | May 2026 | Integración, pruebas y ERP-lite |
| Fase 6 | Jun 2026 | Documentación final y presentación |

---

## Ramas del Proyecto

| **Rama** | **Propósito** |
|-----------|---------------|
| `main` | Versión estable para revisión o entrega |
| `dev` | Rama de desarrollo principal |
| `dev-backend` | Implementación del backend (Spring Boot) |
| `dev-frontend` | Desarrollo de la interfaz web |
| `dev-android` | Desarrollo de la app Android |
| `docs` | Documentación, diagramas y memoria técnica |

---

## Instalación y Ejecución *(pendiente de completar)*

### Backend
```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/citabella-dam.git

# Entrar al directorio backend
cd backend

# Configurar la base de datos en application.properties
# Ejecutar con Maven o desde IntelliJ IDEA
```

### Frontend
```bash
# Abrir la carpeta del frontend
cd frontend-web

# Ejecutar en un entorno local o servidor Apache
```
### Android
```bash
# Abrir el proyecto en Android Studio
# Configurar la URL base de la API
# Compilar y ejecutar en emulador o dispositivo físico
```
## 📊 Estado del Proyecto

> Estado actual de desarrollo del sistema CitaBella

- [x] 🧩 **Diseño y planificación**
- [x] ⚙️ **Backend funcional**
- [ ] 💻 **Frontend web en desarrollo**
- [ ] 📱 **Aplicación Android en desarrollo**
- [ ] 📈 **ERP-lite integrado**
- [ ] 📝 **Documentación finalizada**

---
## 👤 Autor

**Iván López Molina**  
🎓 *Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)*  
📅 *Curso 2025–2026*  
📍 *La Pobla de Vallbona*  

🔗 [LinkedIn](https://www.linkedin.com/in/ivan-lopez-molina) · [GitHub](https://github.com/pepolyente)

---
## 🪪 Licencia

Copyright (c) 2025 Iván López Molina
All rights reserved.

Este código fuente y sus archivos asociados están protegidos por derechos de autor.
No se permite su uso, reproducción, modificación, distribución o publicación total o parcial sin el consentimiento expreso por escrito del autor.

> ⚠️ **Nota:** Este repositorio es privado y el contenido aún está en desarrollo.

