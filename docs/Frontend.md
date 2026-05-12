# Frontend Web – CitaBella (Angular 18)

---

## 1. Arquitectura y Organización

El frontend se construye con **Angular 18** usando el modelo clásico **NgModule** (no standalone) y **lazy loading**
para todos los módulos de funcionalidad. Esto minimiza el bundle inicial y divide la aplicación en tres zonas claramente
separadas:

- Zona pública (`PublicLayout`) — visible sin autenticación
- Zona de autenticación (`/login`, `/register`) — fuera de cualquier layout contenedor
- Panel privado (`MainLayout`) bajo `/panel`, protegido por guards

**Tipografía e iconos:** Inter (Google Fonts, 400/500/600) + Material Symbols Outlined.

---

### Estructura de directorios

```
frontend/citabella-web/src/
├── styles/
│   └── tokens.css              # Design tokens globales (colores, sombras, radios…)
├── styles.css                  # Reset CSS y clases utilitarias globales
└── app/
    ├── app.ts / app-module.ts / app-routing-module.ts
    ├── core/
    │   ├── guards/
    │   │   ├── auth.guard.ts
    │   │   └── role.guard.ts
    │   ├── interceptors/
    │   │   └── auth.interceptor.ts
    │   ├── core-module.ts
    │   └── services/
    │       ├── auth.service.ts
    │       ├── appointment.service.ts
    │       ├── client.service.ts
    │       ├── employee.service.ts
    │       ├── treatment.service.ts
    │       ├── product.service.ts
    │       ├── user.service.ts
    │       ├── theme.service.ts
    │       ├── toast.service.ts
    │       ├── confirm.service.ts
    │       └── breadcrumb.service.ts
    ├── shared/
    │   ├── shared-module.ts
    │   ├── models/                  # Interfaces TypeScript por dominio
    │   │   ├── appointment.model.ts
    │   │   ├── auth.model.ts
    │   │   ├── client.model.ts
    │   │   ├── employee.model.ts
    │   │   ├── treatment.model.ts
    │   │   ├── product.model.ts
    │   │   ├── user.model.ts
    │   │   └── page-response.model.ts
    │   └── components/
    │       ├── confirm-dialog/      # Diálogo de confirmación global
    │       └── toast-container/     # Stack de notificaciones toast
    ├── layout/
    │   ├── layout-module.ts
    │   ├── header/                  # Cabecera del panel privado
    │   ├── public-header/           # Cabecera de la zona pública
    │   ├── main-layout/             # Wrapper del panel privado
    │   └── public-layout/           # Wrapper de la zona pública
    └── features/
        ├── public/                  # PublicModule (Home, ServicesPage, ProductsPage)
        ├── auth/                    # AuthModule (Login, Register)
        ├── appointments/            # AppointmentsModule – lazy
        ├── clients/                 # ClientsModule – lazy
        ├── employees/               # EmployeesModule – lazy
        ├── treatments/              # TreatmentsModule – lazy
        ├── product/                 # ProductModule – lazy
        └── admin/                   # AdminModule – lazy
```

---

## 2. Módulos y Lazy Loading

Todos los módulos de funcionalidad se cargan con `loadChildren` mediante imports dinámicos.

| Módulo                       | Ruta base                | Componentes                                                                    | Roles permitidos |
|------------------------------|--------------------------|--------------------------------------------------------------------------------|------------------|
| PublicModule                 | `/`                      | Home, ServicesPage, ProductsPage                                               | Todos            |
| AuthModule                   | `/login`, `/register`    | Login, Register                                                                | Todos            |
| AppointmentsModule           | `/panel/appointments`    | AppointmentCalendar, AppointmentForm, AppointmentModal, AppointmentCreateModal | ADMIN, EMPLOYEE  |
| ClientsModule                | `/panel/clients`         | ClientList, ClientForm                                                         | ADMIN, EMPLOYEE  |
| ProductModule                | `/panel/products`        | ProductList                                                                    | ADMIN, EMPLOYEE  |
| EmployeesModule              | `/panel/employees`       | EmployeeList, EmployeeForm                                                     | ADMIN            |
| TreatmentsModule             | `/panel/treatments`      | TreatmentList, TreatmentForm                                                   | ADMIN            |
| AdminModule                  | `/panel/admin`           | UserList                                                                       | ADMIN            |
| AppointmentsModule _(alias)_ | `/panel/my-appointments` | Vista de citas propias                                                         | CLIENT           |

> **Nota sobre rutas públicas:** Las páginas de servicios y productos de la zona pública se sirven bajo `/servicios` y
`/productos` respectivamente, **no** bajo `/services` ni `/products`.

---

## 3. Sistema de Enrutamiento y Seguridad

### Árbol de rutas (`app-routing-module.ts`)

```
/                       → PublicLayout → PublicModule
  /servicios            → ServicesPage
  /productos            → ProductsPage

/login                  → AuthModule → Login
/register               → AuthModule → Register

/panel                  → MainLayout [AuthGuard]
  /appointments         → AppointmentsModule [RoleGuard: ADMIN, EMPLOYEE]
  /clients              → ClientsModule      [RoleGuard: ADMIN, EMPLOYEE]
  /products             → ProductModule      [RoleGuard: ADMIN, EMPLOYEE]
  /employees            → EmployeesModule    [RoleGuard: ADMIN]
  /treatments           → TreatmentsModule   [RoleGuard: ADMIN]
  /admin                → AdminModule        [RoleGuard: ADMIN]
  /my-appointments      → AppointmentsModule [RoleGuard: CLIENT]

/unauthorized           → redirect /
**                      → redirect /
```

### AuthGuard

Comprueba `AuthService.isLogged()` (existencia del token en `localStorage`). Si no existe, redirige a `/login`.

```typescript
canActivate()
:
boolean
{
    if (this.auth.isLogged()) return true;
    this.router.navigate(['/login']);
    return false;
}
```

### RoleGuard

Lee el rol del token y lo compara con `route.data['roles']`. Si no coincide, redirige a `/unauthorized`.

```typescript
canActivate(route
:
ActivatedRouteSnapshot
):
boolean
{
    const allowed: string[] = route.data['roles'] ?? [];
    if (allowed.length === 0 || allowed.includes(this.auth.getRole())) return true;
    this.router.navigate(['/unauthorized']);
    return false;
}
```

### Flujo de autenticación

1. `LoginComponent` envía credenciales a `POST /api/auth/login`.
2. El backend devuelve `{ token, username, role }`. El token se guarda en `localStorage`.
3. Se lanza automáticamente `GET /api/auth/me` para obtener `{ id, username, role }` completo.
4. `localStorage` almacena `token`, `role`, `username`, `userId`.
5. Los guards permiten el acceso al panel.
6. `AuthInterceptor` añade `Authorization: Bearer <token>` a todas las peticiones salientes.
7. Tras el login, la redirección depende del rol:
    - `ADMIN` / `EMPLOYEE` → `/panel/appointments`
    - `CLIENT` → `/panel/my-appointments`

---

## 4. Comunicación con la API

### Servicios por dominio

Cada dominio tiene su propio servicio Angular bajo `core/services/`:

| Servicio             | Base URL            | Operaciones principales                                           |
|----------------------|---------------------|-------------------------------------------------------------------|
| `AuthService`        | `/api/auth`         | `login`, `register`, `me`, `logout`                               |
| `AppointmentService` | `/api/appointments` | `getAll`, `getById`, `create`, `update`, `changeStatus`, `cancel` |
| `ClientService`      | `/api/clients`      | CRUD + `linkUser`, `unlinkUser`, `deactivate`                     |
| `EmployeeService`    | `/api/employees`    | CRUD + `activate`, `linkUser`                                     |
| `TreatmentService`   | `/api/treatments`   | CRUD + `getDetailed`, `deactivate`                                |
| `ProductService`     | `/api/products`     | CRUD + `getAllActive`, `getAdmin`, `deactivate`                   |
| `UserService`        | `/api/users`        | CRUD + `swapRole`, `deactivate`                                   |

Todos los servicios utilizan `HttpClient` y devuelven `Observable<T>`, con paginación mediante el modelo genérico
`PageResponse<T>`.

### AuthInterceptor (`core/interceptors/auth.interceptor.ts`)

Registrado globalmente en `CoreModule` vía `HTTP_INTERCEPTORS`. Intercepta todas las peticiones salientes y añade la
cabecera:

```
Authorization: Bearer <token>
```

No afecta peticiones sin token (p.ej., rutas públicas de la API).

---

## 5. Componentes por Zona

### Zona Pública

**`Home`** — Hero con imagen de fondo + glassmorphism, sección de tratamientos destacados (máx. 4) y productos
destacados (máx. 4), con enlaces "Ver todos".

**`ServicesPage`** (`/servicios`) — Grid de tarjetas con todos los tratamientos activos: nombre, duración mínima y
precio.

**`ProductsPage`** (`/productos`) — Grid de tarjetas con imagen, nombre, categoría y precio de venta. Imagen de fallback
si no hay `imageKey`.

**`PublicHeader`** — Cabecera responsiva con menú hamburguesa en móvil. Incluye enlaces Inicio / Servicios / Productos,
botón de tema (claro/oscuro) y acciones de sesión (Registrarse / Entrar / Mi panel / Salir).

### Autenticación

**`Login`** (`/login`) — Tarjeta centrada sobre gradiente oscuro. Toggle para mostrar/ocultar contraseña.

**`Register`** (`/register`) — Tarjeta centrada. Validación de email, longitud de contraseña (mín. 6 caracteres) y
campos obligatorios antes de llamar a la API.

### Panel Privado

**`Header`** — Cabecera del panel con navegación dinámica filtrada por rol, botón de tema y menú hamburguesa en móvil.

**`AppointmentCalendar`** — Vista principal de citas integrada con **FullCalendar** (`@fullcalendar/angular`). Soporta
vistas mes/semana/día, selección de hueco vacío para crear cita, indicador de hora actual y eventos coloreados por
estado. Muestra advertencia visual (`⚠️`) en citas solapadas.

**`AppointmentCreateModal`** — Wizard de 3 pasos para crear citas desde el calendario:

1. Selección de cliente + franja horaria
2. Selección de tratamientos (chips) + empleado
3. Resumen de confirmación

**`AppointmentForm`** (`/panel/appointments/new`) — Formulario clásico de página completa para crear citas: cliente,
empleado, tratamientos con checkboxes, inicio/fin (`datetime-local`) y notas.

**`AppointmentModal`** — Modal de detalle de cita con acciones: cambiar estado, reagendar y cancelar. Máquina de estados
con transiciones válidas:

```
PENDING     → CONFIRMED, CANCELLED
CONFIRMED   → IN_PROGRESS, CANCELLED, NO_SHOW
IN_PROGRESS → COMPLETED, CANCELLED
```

**`ClientList`** — Tabla paginada con filtro por estado (activo/inactivo). Edición inline por fila. Modal para
vincular/desvincular usuario. Skeleton de carga.

**`ClientForm`** — Formulario de alta de cliente: nombre, teléfono, fecha de nacimiento y género.

**`EmployeeList`** — Tabla con edición inline. Botones de activar/desactivar empleado con confirmación.

**`EmployeeForm`** — Formulario de alta de empleado: nombre y puesto.

**`TreatmentList`** — Tabla con edición inline y filtro por estado. Campos: nombre, descripción, duración mín/máx y
precio.

**`TreatmentForm`** — Formulario de alta de tratamiento con validación de duración y precio.

**`ProductList`** — Tabla paginada con filtro por estado, edición inline y formulario de creación colapsable. Campos:
nombre, categoría, precios (compra/venta), tipo de uso (`INTERNAL` / `SALE` / `BOTH`), proveedor y bandera "crítico".

**`UserList`** (AdminModule) — Gestión de usuarios con cambio de rol (`swapRole`).

---

## 6. Gestión de Estado y Reactividad

La arquitectura evita stores externos (no usa NgRx, Akita ni Redux). El estado se gestiona con:

**RxJS Observables** — para todas las peticiones HTTP y propagación de datos entre componentes.

**Angular Signals** — para estado local reactivo de alta frecuencia:

| Servicio         | Signal   | Uso                                        |
|------------------|----------|--------------------------------------------|
| `ThemeService`   | `isDark` | Modo oscuro/claro activo                   |
| `ToastService`   | `toasts` | Lista de notificaciones activas            |
| `ConfirmService` | `state`  | Config del diálogo de confirmación abierto |

**`BreadcrumbService`** — construye las migas de pan leyendo los segmentos de URL en cada `NavigationEnd`, con un mapa
de etiquetas legibles (`ROUTE_LABELS`).

---

## 7. Diseño Visual – Tokens CSS y Modo Oscuro

El sistema de diseño se define en `src/styles/tokens.css` con variables CSS para colores, sombras, radios y breakpoints.

### Temas

Existen dos temas completos activados mediante clases en `<html>`:

| Clase                                                            | Tema           | Acento principal |
|------------------------------------------------------------------|----------------|------------------|
| `html.theme-light` (o sin clase + `prefers-color-scheme: light`) | **Light Navy** | `#0A0F1E`        |
| `html.theme-dark` (o sin clase + `prefers-color-scheme: dark`)   | **Dark Gold**  | `#C8A96E`        |

**`ThemeService`** gestiona los tres modos posibles: `dark`, `light` y `auto`. La preferencia se persiste en
`localStorage` bajo la clave `citabella-theme`. En modo `auto` se respeta `prefers-color-scheme` del sistema operativo y
se actualiza en tiempo real al cambiar la preferencia del SO.

### Badges de estado de citas

| Estado        | Clase CSS            |
|---------------|----------------------|
| `PENDING`     | `.badge-pending`     |
| `CONFIRMED`   | `.badge-confirmed`   |
| `IN_PROGRESS` | `.badge-in_progress` |
| `CANCELLED`   | `.badge-cancelled`   |
| `COMPLETED`   | `.badge-completed`   |
| `NO_SHOW`     | `.badge-no_show`     |

### Clases utilitarias globales (`styles.css`)

Destacan: `.simple-table`, `.form-card`, `.form-group`, `.form-row`, `.cards-grid`, `.card`, `.btn-primary`,
`.btn-outline`, `.btn-xs`, `.badge`, `.empty-state`, `.skeleton-table / .skeleton-row / .skeleton-cell`, `.paginator`,
`.modal-overlay / .modal-card`, `.breadcrumbs`, `.page-header`, `.filters-bar`.

Las tablas `.simple-table` se transforman en tarjetas apiladas en pantallas `< 768px` (usando `data-label` en las celdas
para el patrón responsive).

---

## 8. Patrones y Buenas Prácticas

**Arquitectura** — Lazy loading modular, separación estricta por dominios, CoreModule para singletons, SharedModule para
reutilizables.

**Seguridad** — Doble guardia (`AuthGuard` + `RoleGuard`), interceptor JWT automático, control de acceso por rol en
navegación y en la API.

**UX** — Skeleton loading en todas las listas, edición inline en tablas, wizard multi-paso para citas, toast de
confirmación/error, diálogo de confirmación para acciones destructivas.

**Reactividad** — RxJS para flujos HTTP, Angular Signals para estado UI local.

**Responsive** — Breakpoint `768px` (CSS variable `--breakpoint-md`). Menú hamburguesa con drawer lateral animado en
móvil. Tablas responsive y grid de tarjetas con `auto-fill`.

**Tipado** — Modelos TypeScript por dominio en `shared/models/`, respuesta paginada genérica `PageResponse<T>`.

---

## 9. Dependencias Externas Notables

- **`@fullcalendar/angular`** + plugins `daygrid`, `timegrid`, `interaction` — Calendario de citas con localización
  española.
- **Google Fonts: Inter** — Tipografía principal.
- **Google Fonts: Material Symbols Outlined** — Iconografía.

---

## 10. Consideraciones de Despliegue

### Desarrollo

Angular CLI redirige el prefijo `/api` hacia el backend local mediante el proxy de desarrollo:

```
/api  →  http://localhost:8080
```

### Producción

```bash
ng build
```

Los archivos compilados se copian a un volumen Docker o imagen Nginx. Nginx:

- Sirve la SPA Angular con fallback a `index.html` para la navegación cliente.
- Proxifica las peticiones `/api` hacia el backend Spring Boot.