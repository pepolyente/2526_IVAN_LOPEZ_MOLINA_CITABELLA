# Frontend Web – CitaBella (Angular 21)

---

# 1. Visión general

El frontend es una SPA construida con **Angular 21**, utilizando el sistema de módulos tradicional (`NgModule`) y **lazy loading** para todas las áreas funcionales.

La aplicación se divide en tres zonas con layouts distintos:

- **Zona pública (`PublicLayout`)** – accesible sin autenticación.
- **Autenticación (`/login`, `/register`)** – páginas independientes fuera de cualquier layout contenedor.
- **Panel privado (`MainLayout`)** – bajo la ruta `/panel`, protegido por guards de autenticación y rol.

## Tecnologías principales

- Angular 21
- RxJS 7.8
- Signals
- TypeScript 5.9
- FullCalendar 6.1.20
- Flatpickr 4.6.13
- Fuente Inter
- Material Symbols Outlined
- CSS puro con design tokens

---

# 2. Estructura de directorios

```plaintext
frontend/citabella-web/
├── angular.json
├── package.json
├── proxy.conf.json
├── tsconfig.json
├── tsconfig.app.json
├── tsconfig.spec.json
└── src/
    ├── index.html
    ├── main.ts
    ├── styles.css
    ├── styles/
    │   └── tokens.css
    └── app/
        ├── app.ts
        ├── app-module.ts
        ├── app-routing-module.ts
        ├── core/
        │   ├── core-module.ts
        │   ├── guards/
        │   │   ├── auth.guard.ts
        │   │   └── role.guard.ts
        │   ├── interceptors/
        │   │   └── auth.interceptor.ts
        │   ├── utils/
        │   │   └── http-params.util.ts
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
        │   ├── directives/
        │   │   └── flatpickr.directive.ts
        │   ├── models/
        │   └── components/
        ├── layout/
        │   ├── layout-module.ts
        │   ├── header/
        │   ├── public-header/
        │   ├── main-layout/
        │   └── public-layout/
        └── features/
            ├── public/
            ├── auth/
            ├── appointments/
            ├── clients/
            ├── employees/
            ├── treatments/
            ├── product/
            └── admin/
```

---

# 3. Módulos y Lazy Loading

Todos los módulos funcionales utilizan:

```typescript
loadChildren
```

## Tabla de módulos

| Módulo | Ruta base | Componentes principales | Roles |
|---|---|---|---|
| `PublicModule` | `/` | Home, ServicesPage, ProductsPage | Todos |
| `AuthModule` | `/login` | Login | Todos |
| `RegisterModule` | `/register` | Register | Todos |
| `AppointmentsModule` | `/panel/appointments` | AppointmentCalendar, AppointmentForm, AppointmentModal | ADMIN, EMPLOYEE |
| `ClientsModule` | `/panel/clients` | ClientList, ClientForm | ADMIN, EMPLOYEE |
| `ProductModule` | `/panel/products` | ProductList, ProductForm | ADMIN, EMPLOYEE |
| `EmployeesModule` | `/panel/employees` | EmployeeList, EmployeeForm | ADMIN |
| `TreatmentsModule` | `/panel/treatments` | TreatmentList, TreatmentForm | ADMIN |
| `AdminModule` | `/panel/admin` | UserList, UserForm | ADMIN |
| `MyAppointmentsModule` | `/panel/my-appointments` | MyAppointmentsCalendar | CLIENT |

---

# 4. Sistema de Enrutamiento y Seguridad

## Árbol de rutas

```plaintext
/ → PublicLayout → PublicModule

/servicios → ServicesPage
/productos → ProductsPage

/login → Login
/register → Register

/panel → MainLayout [AuthGuard]

/appointments → AppointmentsModule
/clients → ClientsModule
/products → ProductModule
/employees → EmployeesModule
/treatments → TreatmentsModule
/admin → AdminModule
/my-appointments → MyAppointmentsModule

/unauthorized → /
** → /
```

---

## Guards

### AuthGuard

Verifica si existe token en `localStorage`.

Si no existe:

```text
redirect → /login
```

---

### RoleGuard

Compara:

```typescript
AuthService.getRole()
```

con:

```typescript
data.roles
```

Si el rol no coincide:

```text
redirect → /unauthorized
```

---

# 5. Flujo de autenticación

## Login

```typescript
POST /api/auth/login
```

Respuesta:

```typescript
{
  token,
  username,
  role
}
```

---

## Flujo completo

1. Login
2. Guardar token
3. Ejecutar:

```typescript
GET /api/auth/me
```

4. Guardar:
    - token
    - role
    - username
    - userId

5. Redirección:

| Rol | Ruta |
|---|---|
| ADMIN | `/panel/appointments` |
| EMPLOYEE | `/panel/appointments` |
| CLIENT | `/panel/my-appointments` |

---

## AuthInterceptor

Añade automáticamente:

```http
Authorization: Bearer <token>
```

---

# 6. Comunicación con la API

## Servicios principales

| Servicio | Base URL | Funciones destacadas |
|---|---|---|
| `AuthService` | `/api/auth` | login, register, me, logout |
| `AppointmentService` | `/api/appointments` | create, update, cancel |
| `ClientService` | `/api/clients` | linkUser, unlinkUser |
| `EmployeeService` | `/api/employees` | activate, deactivate |
| `TreatmentService` | `/api/treatments` | activate, deactivate |
| `ProductService` | `/api/products` | getAllActive, getAdmin |
| `UserService` | `/api/users` | activate, deactivate, swapRole |

---

## Parámetros comunes

### Paginación

```typescript
page
size
sort[]
```

### Filtros

```typescript
active?: boolean
search?: string
accountStatus?: string
```

---

## buildHttpParams

Archivo:

```plaintext
core/utils/http-params.util.ts
```

Convierte objetos en:

```typescript
HttpParams
```

---

# 7. Manejo de errores HTTP

## 401

Acciones:

- limpiar sesión
- redirect `/login`

---

## 403

Acciones:

- mantener sesión
- redirect `/`

---

# 8. Componentes de la zona pública

---

## Home (`/`)

### Características

- Hero principal
- Glow effects
- Glassmorphism
- CTA dinámicos
- Servicios destacados
- Productos destacados

### Métodos usados

```typescript
AuthService.isLogged()
AuthService.getRole()
```

---

## ServicesPage (`/servicios`)

### Características

- Grid de tratamientos
- Skeleton loading
- Sin paginación visible

---

## ProductsPage (`/productos`)

### Características

- Grid de productos
- Imagen fallback:

```plaintext
/images/citabella.jpg
```

- Skeleton loading

---

## PublicHeader

### Funciones

- navegación sticky
- selector de tema
- menú hamburguesa
- login/logout

---

## PublicLayout

### Contiene

- PublicHeader
- router-outlet
- footer
- features strip

---

# 9. Autenticación

---

## Login (`/login`)

### Funciones

- usuario/contraseña
- mostrar/ocultar password
- ToastService
- redirect automático

---

## Register (`/register`)

### Validaciones

- campos requeridos
- email válido
- password ≥ 6

---

# 10. Componentes compartidos del panel

---

## Header

### Características

- navegación por rol
- avatar
- logout
- responsive

---

## BreadcrumbsComponent

Usa:

```typescript
BreadcrumbService
```

Escucha:

```typescript
NavigationEnd
```

---

## ConfirmDialogComponent

Activación:

```typescript
ConfirmService.confirm(message)
```

Devuelve:

```typescript
Promise<boolean>
```

---

## ToastContainerComponent

### Tipos

- success
- error
- warning

### Duración

```text
4 segundos
```

---

# 11. Gestión de citas

---

## AppointmentCalendar

### Integraciones

- FullCalendar
- dayGridMonth
- timeGridWeek
- timeGridDay

### Funciones

- selección de celdas
- colores por estado
- indicador ⚠️
- responsive
- filtros

---

## AppointmentCreateModal

### Wizard de 3 pasos

---

### Paso 1

- cliente
- fecha
- hora

---

### Paso 2

- tratamientos
- empleados

---

### Paso 3

- resumen
- confirmar cita

---

## AppointmentForm

Formulario completo alternativo al wizard.

---

## AppointmentModal

### Acciones

- cambiar estado
- reagendar
- cancelar

### Estados válidos

```text
PENDING → CONFIRMED | CANCELLED
CONFIRMED → IN_PROGRESS | CANCELLED | NO_SHOW
IN_PROGRESS → COMPLETED | CANCELLED
```

---

## MyAppointmentsCalendar

### Características

- solo lectura
- citas del cliente
- modal de detalle

---

# 12. Gestión de clientes

---

## ClientList

### Funciones

- tabla paginada
- búsqueda
- edición inline
- vincular usuario
- desvincular usuario
- desactivar

---

## ClientForm

### Campos

- nombre
- teléfono
- cumpleaños
- género

---

# 13. Gestión de empleados

---

## EmployeeList

### Funciones

- búsqueda
- edición inline
- activar/desactivar

---

## EmployeeForm

### Campos

- nombre
- puesto

---

# 14. Gestión de tratamientos

---

## TreatmentList

### Funciones

- tabla paginada
- búsqueda
- edición inline
- activar/desactivar

---

## TreatmentForm

### Campos

- nombre
- descripción
- duración mínima
- duración máxima
- precio

---

# 15. Gestión de productos

---

## ProductList

### Funciones

- tabla paginada
- búsqueda
- edición inline
- formulario colapsable
- activar/desactivar

---

## ProductForm

Formulario completo de productos.

---

# 16. Administración de usuarios

---

## UserList

### Funciones

- tabla paginada
- búsqueda
- cambio de rol inline
- bloquear/activar
- formulario colapsable

---

## UserForm

Formulario completo de usuarios.

---

# 17. Estado y Reactividad

## RxJS

Todas las peticiones usan:

```typescript
Observable<T>
```

---

## Signals

### Utilizados en

| Servicio | Signal |
|---|---|
| ThemeService | isDark |
| ToastService | toasts |
| ConfirmService | state |

---

## BreadcrumbService

Usa:

```typescript
BehaviorSubject
```

---

# 18. Diseño Visual y Temas

---

## tokens.css

Más de 60 variables CSS.

### Incluye

- colores
- sombras
- bordes
- radios
- gradientes
- breakpoints

---

## Temas

### Light Navy

```text
#0A0F1E
```

---

### Dark Gold

```text
#C8A96E
```

---

## ThemeService

### Modos

- dark
- light
- auto

Persistencia:

```plaintext
localStorage → citabella-theme
```

---

# 19. Estilos globales

## styles.css

### Clases

#### Layout

```css
.page-wrapper
.form-card
.panel-section
.cards-grid
```

#### Formularios

```css
.form-group
.form-row
.input-large
.btn-primary
```

#### Tablas

```css
.simple-table
```

#### Componentes

```css
.badge
.empty-state
.skeleton-table
.modal-overlay
```

---

# 20. FlatpickrDirective

Selector:

```html
[appFlatpickr]
```

---

## Configuración

```typescript
enableTime: true
time_24hr: true
allowInput: true
locale: Spanish
dateFormat: 'Y-m-d\\TH:i:S'
altInput: true
altFormat: 'd-m-Y H:i'
disableMobile: true
```

---

## Usos

- citas
- cumpleaños
- reagendado

---

# 21. Dependencias externas

| Paquete | Uso |
|---|---|
| `@angular/core` | Framework |
| `@fullcalendar/angular` | Calendario |
| `@fullcalendar/daygrid` | Vista mensual |
| `@fullcalendar/timegrid` | Vista semanal |
| `@fullcalendar/interaction` | Eventos |
| `flatpickr` | Fecha/hora |
| `rxjs` | Reactividad |

---

# 22. Desarrollo y Producción

---

## Desarrollo

### Proxy

```plaintext
proxy.conf.json
```

### Backend

```plaintext
http://localhost:8080
```

### Puerto frontend

```plaintext
4200
```

---

## Producción

### Build

```bash
ng build
```

Salida:

```plaintext
dist/citabella-web/
```

---

## Nginx

### Funciones

- SPA fallback
- reverse proxy `/api`

---

## Optimización

- minificación
- hash assets
- tree shaking

---

# 23. Angular Configuration

## angular.json

### Configuración

```json
"standalone": false
```

Builder:

```json
@angular/build:application
```

---

# 24. Buenas prácticas

- lazy loading
- guards
- interceptor JWT
- separación de responsabilidades
- edición inline
- skeleton loading
- responsive design
- Signals
- strict mode
- accesibilidad

---

# 25. Modelos de datos

---

## AppointmentResponse

```typescript
{
  id: number;
  startAt: string;
  endAt: string;
  status: AppointmentStatus;
  notes?: string;
  hasOverlap: boolean;
  client: ClientResponse;
  employee: EmployeeResponse;
  treatments: TreatmentResponse[];
}
```

---

## CreateAppointmentRequest

```typescript
{
  clientId?: number;
  employeeId?: number;
  treatmentsIds: number[];
  startAt: string;
  endAt: string;
  notes?: string;
}
```

---

## ProductRequest

```typescript
{
  name: string;
  category?: string;
  purchasePrice?: number;
  salePrice?: number;
  usageType?: 'INTERNAL' | 'SALE' | 'BOTH';
  supplier?: string;
  isCritical?: boolean;
  imageKey?: string;
}
```

---

## UserResponse

```typescript
{
  id: number;
  username: string;
  email: string;
  role: string;
  accountStatus: 'PENDING' | 'ACTIVE' | 'LOCKED';
  profileType: 'NONE' | 'CLIENT' | 'EMPLOYEE';
}
```

---

# 26. Query Params

```typescript
export interface SearchableQueryParams extends ActiveQueryParams {
  search?: string;
}

export interface ActiveQueryParams extends BaseQueryParams {
  active?: boolean;
}

export interface BaseQueryParams {
  page?: number;
  size?: number;
  sort?: string[];
}
```

---

# 27. Notas adicionales

## Imagen por defecto

```plaintext
/images/citabella.jpg
```

---

## Logout

- limpia `localStorage`
- redirect `/`

---

## Errores de sesión

- 401 → warning toast
- 403 → warning toast

---

## Transiciones de citas

Solo se muestran estados válidos.

---

## Botón "Mi panel"

Visible para:

- CLIENT
- EMPLOYEE
- ADMIN

---

## Registro

Los usuarios nuevos quedan:

```text
PENDING
```

hasta activación manual.