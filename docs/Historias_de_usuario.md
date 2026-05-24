# Historias de Usuario — CitaBella

> Proyecto Intermodular DAM · Iván López Molina · Curso 2025–2026

---

## Leyenda de roles

- **Visitante** — cualquier persona sin autenticación
- **Cliente** — usuario registrado con perfil de cliente vinculado
- **Empleado** — trabajador con acceso operativo
- **Admin** — propietaria/administradora con acceso completo

---

## FASE 1 — MVP (Implementadas)

---

### HISTORIA DE USUARIO 1: FRENTE

**ID:** HU-01-LOGIN
**TÍTULO:** Como usuario registrado quiero iniciar sesión con mi nombre de usuario y contraseña para acceder al panel
correspondiente a mi rol.

**REGLAS DE NEGOCIO:**

- El nombre de usuario es único en el sistema.
- La contraseña se almacena cifrada con BCrypt; nunca en texto plano.
- El sistema genera un token JWT firmado (HS256) con el nombre de usuario y rol.
- La redirección tras login depende del rol: ADMIN y EMPLOYEE van a `/panel/appointments`; CLIENT va a
  `/panel/my-appointments`.
- El token tiene tiempo de expiración configurable.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Inicio de sesión exitoso como administrador**
Dado que el usuario "Admin" está registrado con contraseña válida y rol ADMIN.
Cuando introduce su nombre de usuario y contraseña correctos en el formulario de login.
Entonces el sistema genera el token JWT, lo almacena en `localStorage` y redirige a `/panel/appointments`.

**Escenario 2: Inicio de sesión exitoso como cliente**
Dado que el usuario "Cliente" está registrado con rol CLIENT y perfil de cliente vinculado.
Cuando introduce sus credenciales correctas.
Entonces el sistema redirige a `/panel/my-appointments`.

**Escenario 3: Fallo por contraseña incorrecta**
Dado que el usuario "Admin" existe en el sistema.
Cuando introduce el nombre de usuario correcto pero una contraseña errónea.
Entonces el sistema muestra un mensaje de error y no genera token ni redirige.

**Escenario 4: Fallo por usuario inexistente**
Dado que no existe ningún usuario con el nombre "pepe_inexistente".
Cuando intenta iniciar sesión con ese nombre.
Entonces el sistema devuelve un error y no permite el acceso.

**Escenario 5: Acceso a ruta protegida sin sesión**
Dado que no hay token en `localStorage`.
Cuando el usuario intenta acceder directamente a `/panel/appointments`.
Entonces el `AuthGuard` redirige a `/login`.

---

### HISTORIA DE USUARIO 2: FRENTE

**ID:** HU-02-REGISTER
**TÍTULO:** Como visitante quiero registrarme con email y contraseña para obtener una cuenta de usuario pendiente de
activación.

**REGLAS DE NEGOCIO:**

- El email debe ser único en el sistema.
- El nombre de usuario debe ser único en el sistema.
- La contraseña debe tener un mínimo de 6 caracteres.
- Al registrarse, el usuario recibe el rol `USER` y `accountStatus = PENDING`.
- Un usuario `USER/PENDING` puede iniciar sesión pero no accede a ningún módulo de gestión hasta que un ADMIN o EMPLOYEE
  vincule su cuenta a un perfil de cliente o empleado.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Registro exitoso**
Dado que no existe ningún usuario con email "nuevo@cliente.com" ni username "NuevoCliente".
Cuando el visitante completa el formulario de registro con datos válidos.
Entonces el sistema crea la cuenta con rol USER y accountStatus PENDING, y permite el inicio de sesión.

**Escenario 2: Fallo por email duplicado**
Dado que ya existe un usuario con email "admin@citabella.com".
Cuando otro visitante intenta registrarse con ese mismo email.
Entonces el sistema muestra un mensaje de error indicando que el email ya está en uso.

**Escenario 3: Fallo por nombre de usuario duplicado**
Dado que ya existe el usuario "Admin".
Cuando un visitante intenta registrarse con username "Admin".
Entonces el sistema rechaza el registro e indica que el nombre de usuario no está disponible.

**Escenario 4: Fallo por contraseña demasiado corta**
Dado que el visitante intenta registrarse con contraseña "abc".
Cuando envía el formulario.
Entonces el sistema muestra error de validación antes de llamar a la API.

---

### HISTORIA DE USUARIO 3: FRENTE

**ID:** HU-03-PUBLIC-HOME
**TÍTULO:** Como visitante quiero ver la página principal del negocio con los tratamientos y productos destacados para
conocer los servicios que ofrece la peluquería.

**REGLAS DE NEGOCIO:**

- La zona pública es accesible sin autenticación.
- Se muestran como máximo 4 tratamientos destacados y 4 productos destacados en la home.
- Solo se muestran tratamientos y productos con `active = true`.
- Desde la cabecera se puede navegar a la página de servicios completa, productos y al login.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Carga de la página principal**
Dado que existen tratamientos y productos activos en el sistema.
Cuando un visitante accede a la URL raíz `/`.
Entonces se muestra el hero con el nombre del negocio, hasta 4 tratamientos activos con nombre y precio, y hasta 4
productos activos con imagen y precio de venta.

**Escenario 2: Navegación al catálogo completo**
Dado que el visitante está en la página principal.
Cuando hace clic en "Ver todos" debajo de la sección de tratamientos.
Entonces el sistema redirige a `/servicios` con el catálogo completo.

**Escenario 3: Sin productos activos**
Dado que no hay ningún producto con `active = true` en el sistema.
Cuando el visitante accede a la home.
Entonces la sección de productos no se muestra o aparece vacía, sin errores.

---

### HISTORIA DE USUARIO 4: FRENTE

**ID:** HU-04-PUBLIC-SERVICES
**TÍTULO:** Como visitante quiero consultar el catálogo completo de tratamientos con su duración y precio para decidir
qué servicio me interesa antes de pedir cita.

**REGLAS DE NEGOCIO:**

- Solo se muestran tratamientos con `active = true`.
- Se muestra nombre, duración mínima y precio de cada tratamiento.
- No se requiere autenticación.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Visualización del catálogo de servicios**
Dado que existen tratamientos activos como "Lavar cabeza" y "Cortar puntas".
Cuando un visitante accede a `/servicios`.
Entonces el sistema muestra todos los tratamientos activos con nombre, duración mínima y precio en formato de tarjetas o
tabla.

**Escenario 2: Tratamiento desactivado no visible**
Dado que el tratamiento "Servicio antiguo" tiene `active = false`.
Cuando cualquier usuario accede a `/servicios`.
Entonces ese tratamiento no aparece en el listado.

---

### HISTORIA DE USUARIO 5: FRENTE

**ID:** HU-05-PUBLIC-PRODUCTS
**TÍTULO:** Como visitante quiero ver el catálogo de productos disponibles con su imagen, nombre y precio para conocer
qué artículos vende la peluquería.

**REGLAS DE NEGOCIO:**

- Solo se muestran productos con `active = true` y `usageType` de tipo `SALE` o `BOTH`.
- La respuesta pública (`ProductPublicResponse`) expone únicamente `id`, `name`, `category`, `salePrice` e `imageKey`.
  No se expone el precio de compra ni el proveedor.
- Si un producto no tiene imagen (`imageKey` vacío), se muestra imagen de fallback.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Visualización del catálogo de productos**
Dado que existe el producto "SuavePelo" con imagen y `usageType = BOTH`.
Cuando un visitante accede a `/productos`.
Entonces se muestra la tarjeta del producto con imagen, nombre, categoría y precio de venta.

**Escenario 2: Producto sin imagen**
Dado que el producto "CortaPelo" no tiene `imageKey` definido.
Cuando un visitante accede a `/productos`.
Entonces se muestra la tarjeta con imagen de fallback en lugar de imagen vacía.

**Escenario 3: Producto de uso interno no visible**
Dado que "CortaPelo" tiene `usageType = INTERNAL`.
Cuando cualquier usuario accede al catálogo público.
Entonces ese producto no aparece en el listado.

---

### HISTORIA DE USUARIO 6: FRENTE

**ID:** HU-06-CREATE-APPOINTMENT
**TÍTULO:** Como empleado o administrador quiero crear una cita nueva asignando cliente, empleado, tratamientos y franja
horaria para organizar la agenda del negocio.

**REGLAS DE NEGOCIO:**

- Una cita debe tener al menos un tratamiento asociado.
- Una cita debe tener un cliente, un empleado, una fecha de inicio y una fecha de fin.
- Al crearse, el estado inicial de la cita es `PENDING`.
- Si la franja horaria se solapa con otra cita del mismo empleado, el campo `hasOverlap` se marca como `true`. El
  sistema avisa pero no bloquea la creación.
- Solo ADMIN y EMPLOYEE pueden crear citas.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Creación exitosa de cita sin solapamiento**
Dado que el empleado "Ruth Molina" no tiene citas en el horario 10:00–11:00 del próximo lunes.
Cuando un empleado crea una cita para el cliente "Ivan" con tratamiento "Cortar puntas" en esa franja.
Entonces el sistema crea la cita con estado PENDING y `hasOverlap = false`, y aparece en el calendario.

**Escenario 2: Creación con solapamiento detectado**
Dado que "Ruth Molina" ya tiene una cita de 10:00 a 11:00 el próximo lunes.
Cuando se intenta crear otra cita para ese mismo empleado de 10:30 a 11:30.
Entonces el sistema crea la cita pero marca `hasOverlap = true` y muestra advertencia visual en el calendario.

**Escenario 3: Fallo por falta de tratamiento**
Dado que se intenta crear una cita sin seleccionar ningún tratamiento.
Cuando se envía el formulario.
Entonces el sistema devuelve error 400 y no crea la cita.

**Escenario 4: Creación mediante wizard en el calendario**
Dado que el empleado hace clic en una franja horaria vacía del calendario.
Cuando completa los 3 pasos del wizard (cliente+horario → tratamientos+empleado → confirmación).
Entonces la cita se crea y aparece inmediatamente en el calendario sin recargar la página.

---

### HISTORIA DE USUARIO 7: FRENTE

**ID:** HU-07-VIEW-CALENDAR
**TÍTULO:** Como empleado o administrador quiero ver el calendario de citas en vista mensual, semanal y diaria para
consultar la agenda del negocio de forma visual.

**REGLAS DE NEGOCIO:**

- El calendario muestra todas las citas del sistema (ADMIN y EMPLOYEE ven todas).
- Cada cita se colorea según su estado (PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW).
- Las citas con `hasOverlap = true` muestran un indicador visual de advertencia.
- Se muestra indicador de hora actual en la vista semanal/diaria.
- Solo ADMIN y EMPLOYEE tienen acceso a esta vista.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Visualización mensual**
Dado que existen citas en el mes actual con distintos estados.
Cuando el empleado accede a `/panel/appointments`.
Entonces ve el calendario mensual con las citas representadas por bloques de color según estado.

**Escenario 2: Cambio de vista**
Dado que el empleado está en la vista mensual.
Cuando selecciona la vista semanal.
Entonces el calendario muestra las citas del mismo rango en formato de columnas por día con franjas horarias.

**Escenario 3: Cita con solapamiento**
Dado que existe una cita con `hasOverlap = true`.
Cuando aparece en el calendario.
Entonces muestra el icono de advertencia junto al título de la cita en cualquier vista.

---

### HISTORIA DE USUARIO 8: FRENTE

**ID:** HU-08-CHANGE-STATUS
**TÍTULO:** Como empleado o administrador quiero cambiar el estado de una cita existente para reflejar su progreso en el
ciclo de vida del servicio.

**REGLAS DE NEGOCIO:**

- La máquina de estados de citas es: `PENDING → CONFIRMED | CANCELLED`, `CONFIRMED → IN_PROGRESS | CANCELLED | NO_SHOW`,
  `IN_PROGRESS → COMPLETED | CANCELLED`.
- Solo se pueden aplicar transiciones válidas; una cita COMPLETED no puede volver a PENDING.
- ADMIN y EMPLOYEE pueden cambiar estados.
- Un cliente no puede cambiar el estado de sus citas.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Confirmación de cita pendiente**
Dado que existe una cita con estado PENDING.
Cuando el empleado abre el modal de la cita y selecciona "Confirmar".
Entonces el estado cambia a CONFIRMED y el cambio se refleja visualmente en el calendario.

**Escenario 2: Marcar cita como completada**
Dado que una cita está en estado IN_PROGRESS.
Cuando el empleado selecciona "Completar".
Entonces el estado cambia a COMPLETED y el badge de color se actualiza.

**Escenario 3: Transición inválida bloqueada**
Dado que una cita ya está en estado COMPLETED.
Cuando se intenta cambiar a CONFIRMED desde la API.
Entonces el backend devuelve error 400 indicando que la transición no está permitida.

**Escenario 4: Registrar no-show**
Dado que un cliente no se ha presentado a una cita CONFIRMED.
Cuando el empleado selecciona "No presentado".
Entonces el estado cambia a NO_SHOW para dejar constancia en el historial.

---

### HISTORIA DE USUARIO 9: FRENTE

**ID:** HU-09-CANCEL-APPOINTMENT
**TÍTULO:** Como empleado o administrador quiero cancelar una cita para liberar la franja horaria y actualizar el estado
en el historial.

**REGLAS DE NEGOCIO:**

- Solo se pueden cancelar citas en estado PENDING, CONFIRMED o IN_PROGRESS.
- Una cita COMPLETED o NO_SHOW no puede cancelarse.
- La cancelación no elimina físicamente la cita; el estado pasa a CANCELLED.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Cancelación exitosa desde el modal**
Dado que existe una cita con estado CONFIRMED.
Cuando el empleado abre el modal de detalle y selecciona "Cancelar".
Entonces el estado cambia a CANCELLED y la cita desaparece del color activo en el calendario.

**Escenario 2: Intento de cancelar cita completada**
Dado que una cita tiene estado COMPLETED.
Cuando se intenta cancelarla.
Entonces el sistema devuelve error y no cambia el estado.

---

### HISTORIA DE USUARIO 10: FRENTE

**ID:** HU-10-MANAGE-CLIENTS
**TÍTULO:** Como empleado o administrador quiero registrar y gestionar clientes para mantener actualizada la ficha de
cada persona que acude al negocio.

**REGLAS DE NEGOCIO:**

- El número de teléfono del cliente es único en el sistema.
- Los campos obligatorios son: nombre, teléfono, fecha de nacimiento y género.
- La desactivación es lógica (`active = false`); el cliente no se elimina de la base de datos para preservar el
  historial.
- Los clientes inactivos no aparecen en los listados por defecto pero pueden filtrarse.
- Solo ADMIN y EMPLOYEE pueden gestionar clientes.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Alta de nuevo cliente**
Dado que no existe ningún cliente con teléfono "666123456".
Cuando el empleado completa el formulario de alta con nombre, teléfono, fecha de nacimiento y género.
Entonces el sistema crea el cliente con `active = true` y aparece en el listado.

**Escenario 2: Fallo por teléfono duplicado**
Dado que ya existe la clienta "Angela" con teléfono "666123456".
Cuando se intenta registrar otro cliente con el mismo teléfono.
Entonces el sistema devuelve error 400 indicando que el teléfono ya está registrado.

**Escenario 3: Edición inline de datos**
Dado que el empleado está en el listado de clientes.
Cuando edita directamente el nombre de un cliente en la fila de la tabla.
Entonces el cambio se guarda al confirmar sin necesidad de abrir un formulario aparte.

**Escenario 4: Desactivación de cliente**
Dado que el cliente "Ivan" está activo y tiene citas pasadas asociadas.
Cuando el administrador lo desactiva.
Entonces el cliente pasa a `active = false`, deja de aparecer en el listado activo, pero sus citas históricas
permanecen.

---

### HISTORIA DE USUARIO 11: FRENTE

**ID:** HU-11-LINK-USER-CLIENT
**TÍTULO:** Como administrador quiero vincular un cliente existente a una cuenta de usuario para que ese usuario pueda
acceder al panel con rol CLIENT y consultar sus citas.

**REGLAS DE NEGOCIO:**

- Un cliente solo puede estar vinculado a un usuario a la vez.
- Un usuario no puede tener vinculado a la vez un perfil de cliente y uno de empleado.
- Al vincular, el rol del usuario cambia automáticamente a CLIENT y su `accountStatus` a ACTIVE.
- Al desvincular, el rol vuelve a USER y el estado a PENDING.
- Solo el ADMIN puede vincular y desvincular perfiles.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Vinculación exitosa**
Dado que el usuario "NuevoCliente" tiene rol USER y el cliente "Angela" no tiene usuario vinculado.
Cuando el administrador vincula "NuevoCliente" al cliente "Angela" desde el modal de gestión.
Entonces el usuario pasa a rol CLIENT y accountStatus ACTIVE; "Angela" aparece como cliente vinculado.

**Escenario 2: Intento de vincular usuario ya vinculado a empleado**
Dado que el usuario "RuthUser" ya está vinculado al empleado "Ruth Molina".
Cuando se intenta vincular ese mismo usuario a un cliente.
Entonces el sistema devuelve error indicando que el usuario ya tiene un perfil asignado.

**Escenario 3: Desvinculación**
Dado que "NuevoCliente" está vinculado a "Angela".
Cuando el administrador desvincula el perfil.
Entonces el usuario vuelve a rol USER y accountStatus PENDING; el cliente "Angela" queda sin usuario asociado.

---

### HISTORIA DE USUARIO 12: FRENTE

**ID:** HU-12-MANAGE-EMPLOYEES
**TÍTULO:** Como administrador quiero registrar y gestionar empleados para controlar qué trabajadores están activos y
pueden ser asignados a citas.

**REGLAS DE NEGOCIO:**

- El nombre del empleado es único en el sistema.
- La desactivación es lógica (`active = false`).
- Solo ADMIN puede crear, editar y desactivar empleados.
- Un empleado desactivado no aparece como opción al crear nuevas citas.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Alta de empleado**
Dado que no existe ningún empleado con nombre "Laura García".
Cuando el administrador completa el formulario de alta con nombre y puesto.
Entonces el empleado aparece en el listado con `active = true` y puede asignarse a citas.

**Escenario 2: Fallo por nombre duplicado**
Dado que ya existe el empleado "Ruth Molina".
Cuando se intenta crear otro empleado con ese mismo nombre.
Entonces el sistema devuelve error 400 indicando que el nombre ya está registrado.

**Escenario 3: Desactivación de empleado**
Dado que el empleado "Ivan L" tiene citas futuras asignadas.
Cuando el administrador lo desactiva.
Entonces pasa a `active = false` y no aparece en el selector de empleados al crear nuevas citas, pero sus citas
existentes no se modifican.

---

### HISTORIA DE USUARIO 13: FRENTE

**ID:** HU-13-MANAGE-TREATMENTS
**TÍTULO:** Como administrador quiero gestionar el catálogo de tratamientos para mantener actualizada la oferta de
servicios del negocio.

**REGLAS DE NEGOCIO:**

- El nombre del tratamiento es único en el sistema.
- Los campos obligatorios son: nombre, precio y duración mínima.
- La duración máxima es opcional.
- El precio debe ser mayor que 0.
- La desactivación es lógica. Los tratamientos inactivos no aparecen en la zona pública.
- Solo ADMIN puede crear, editar y desactivar tratamientos.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Creación de tratamiento**
Dado que no existe ningún tratamiento llamado "Tinte completo".
Cuando el administrador completa el formulario con nombre, descripción, duración mínima 60 min y precio 45 €.
Entonces el tratamiento se crea activo y aparece en la zona pública de servicios.

**Escenario 2: Fallo por nombre duplicado**
Dado que ya existe el tratamiento "Lavar cabeza".
Cuando se intenta crear otro con el mismo nombre.
Entonces el sistema devuelve error 400 indicando nombre duplicado.

**Escenario 3: Edición inline**
Dado que el administrador está en el listado de tratamientos.
Cuando modifica el precio de "Cortar puntas" directamente en la tabla.
Entonces el cambio se persiste y se refleja en la zona pública inmediatamente.

---

### HISTORIA DE USUARIO 14: FRENTE

**ID:** HU-14-MANAGE-PRODUCTS-ADMIN
**TÍTULO:** Como administrador quiero gestionar el catálogo de productos con precio de compra, precio de venta, tipo de
uso y proveedor para controlar el inventario del negocio.

**REGLAS DE NEGOCIO:**

- El nombre del producto es único en el sistema.
- `usageType` puede ser INTERNAL, SALE o BOTH. Solo los de tipo SALE o BOTH aparecen en la zona pública.
- `isCritical = true` marca el producto como prioritario para alertas futuras de stock.
- La vista pública (`ProductPublicResponse`) no expone precio de compra, proveedor ni `isCritical`.
- Solo ADMIN puede gestionar productos desde el panel privado.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Creación de producto**
Dado que no existe ningún producto llamado "Mascarilla Reparadora".
Cuando el administrador crea el producto con precio de compra 8 €, precio de venta 15 €, tipo BOTH y proveedor "
Schwarzkopf".
Entonces aparece en el panel de administración y también en la zona pública de productos.

**Escenario 2: Producto interno no visible en zona pública**
Dado que el producto "CortaPelo" tiene `usageType = INTERNAL`.
Cuando un visitante accede a `/productos`.
Entonces "CortaPelo" no aparece en el catálogo público.

**Escenario 3: Marcado como crítico**
Dado que el administrador marca "SuavePelo" como `isCritical = true`.
Cuando se implemente el módulo de stock en Fase 2, este producto generará alertas prioritarias.
Entonces el campo queda persistido para uso futuro.

---

### HISTORIA DE USUARIO 15: FRENTE

**ID:** HU-15-VIEW-OWN-APPOINTMENTS
**TÍTULO:** Como cliente quiero ver mis propias citas desde el panel privado para saber cuándo tengo próximas visitas y
el estado de mis reservas.

**REGLAS DE NEGOCIO:**

- Un usuario con rol CLIENT solo puede ver sus propias citas, no las de otros clientes.
- Se muestran citas activas e históricas del cliente vinculado a ese usuario.
- El acceso a `/panel/my-appointments` está protegido por `AuthGuard` + `RoleGuard(CLIENT)`.
- Un empleado no puede acceder a esta vista; tiene su propio módulo de citas.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Visualización de citas propias**
Dado que el usuario "Cliente" está vinculado al cliente "Ivan" y tiene citas registradas.
Cuando accede a `/panel/my-appointments`.
Entonces ve únicamente las citas de "Ivan" con su estado, fecha y tratamientos asociados.

**Escenario 2: Cliente sin citas**
Dado que el cliente vinculado no tiene ninguna cita registrada.
Cuando accede a la vista de mis citas.
Entonces se muestra un mensaje de estado vacío indicando que no hay citas.

**Escenario 3: Intento de acceso de un empleado**
Dado que el usuario autenticado tiene rol EMPLOYEE.
Cuando intenta acceder a `/panel/my-appointments`.
Entonces el `RoleGuard` redirige a `/unauthorized`.

---

### HISTORIA DE USUARIO 16: FRENTE

**ID:** HU-16-MANAGE-USERS
**TÍTULO:** Como administrador quiero ver y gestionar todos los usuarios del sistema para controlar el acceso y los
roles de cada cuenta.

**REGLAS DE NEGOCIO:**

- Solo ADMIN puede acceder al módulo de usuarios en `/panel/admin`.
- El administrador puede cambiar el rol de cualquier usuario mediante la operación `swapRole`.
- Un usuario con rol CLIENT o EMPLOYEE no puede ser promovido a ADMIN desde esta interfaz directamente (requiere
  desvinculación de perfil primero).
- La desactivación de un usuario establece `accountStatus = LOCKED`.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Listado de usuarios**
Dado que el administrador accede a `/panel/admin`.
Cuando se carga la vista.
Entonces ve la tabla con todos los usuarios: username, email y rol actual.

**Escenario 2: Cambio de rol**
Dado que el usuario "Desconocido" tiene rol USER.
Cuando el administrador selecciona "Cambiar rol" y asigna EMPLOYEE.
Entonces el sistema actualiza el rol y lo refleja en la tabla sin recargar la página.

**Escenario 3: Intento de acceso de no administrador**
Dado que el usuario autenticado tiene rol EMPLOYEE.
Cuando intenta acceder a `/panel/admin`.
Entonces el `RoleGuard` redirige a `/unauthorized`.

---

### HISTORIA DE USUARIO 17: FRENTE

**ID:** HU-17-THEME-TOGGLE
**TÍTULO:** Como cualquier usuario quiero cambiar entre modo claro y oscuro para adaptar la interfaz a mi preferencia
visual o a las condiciones de iluminación.

**REGLAS DE NEGOCIO:**

- Los modos disponibles son: `light`, `dark` y `auto` (sigue la preferencia del sistema operativo).
- La preferencia se persiste en `localStorage` bajo la clave `citabella-theme`.
- En modo `auto`, si el sistema operativo cambia su preferencia, la interfaz se actualiza en tiempo real sin recargar.
- El toggle está disponible tanto en la cabecera pública como en la del panel privado.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Activar modo oscuro manualmente**
Dado que la interfaz está en modo claro.
Cuando el usuario hace clic en el botón de tema en la cabecera.
Entonces la interfaz cambia inmediatamente al tema oscuro (fondo `#0D0D0D`, acento dorado `#C8A96E`) y la preferencia se
guarda en `localStorage`.

**Escenario 2: Persistencia entre sesiones**
Dado que el usuario guardó la preferencia "dark".
Cuando cierra el navegador y vuelve a abrir la aplicación.
Entonces la interfaz carga directamente en modo oscuro sin parpadeo.

**Escenario 3: Modo auto con cambio del sistema operativo**
Dado que el usuario tiene seleccionado el modo `auto` y el SO está en modo claro.
Cuando el SO cambia a modo oscuro.
Entonces la interfaz se actualiza automáticamente al tema oscuro.

---

### HISTORIA DE USUARIO 18: FRENTE

**ID:** HU-18-LOGOUT
**TÍTULO:** Como usuario autenticado quiero cerrar sesión para que mi cuenta quede protegida al terminar de usar la
aplicación.

**REGLAS DE NEGOCIO:**

- Al cerrar sesión, el token JWT se elimina de `localStorage`.
- El usuario es redirigido a la página pública `/`.
- No se realiza ninguna llamada al backend (el token simplemente se descarta en el cliente).
- Tras el logout, cualquier intento de acceder a rutas protegidas redirige a `/login`.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Cierre de sesión exitoso**
Dado que el usuario está autenticado y en el panel privado.
Cuando hace clic en "Salir" en la cabecera.
Entonces el token se elimina de `localStorage`, se redirige a `/` y los enlaces de sesión cambian a "Registrarse /
Entrar".

**Escenario 2: Acceso a ruta protegida tras logout**
Dado que el usuario acaba de cerrar sesión.
Cuando intenta navegar directamente a `/panel/appointments` en la barra del navegador.
Entonces el `AuthGuard` detecta la ausencia de token y redirige a `/login`.

---

### HISTORIA DE USUARIO 19: FRENTE

**ID:** HU-19-MOBILE-APP
**TÍTULO:** Como empleado o cliente quiero acceder al sistema desde mi móvil Android mediante la aplicación instalada
para gestionar citas o consultar mi agenda sin necesidad de abrir el navegador.

**REGLAS DE NEGOCIO:**

- La app Flutter actúa como contenedor WebView de la SPA Angular; no reimplementa lógica propia.
- El JWT se almacena en el `localStorage` del WebView, que persiste entre sesiones.
- Si no hay conexión a internet, se muestra una pantalla de error con botón de reintento.
- Si hay red pero el servidor no responde (timeout 8 s), se muestra error diferenciado ("Servidor en mantenimiento").
- El botón Atrás de Android navega por el historial de Angular; si no hay historial, muestra diálogo de confirmación
  para salir.
- `usesCleartextTraffic = true` en desarrollo (HTTP); desactivar en producción con HTTPS.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Arranque con conexión disponible**
Dado que el dispositivo tiene conexión y el servidor responde.
Cuando el usuario abre la app.
Entonces se muestra el splash nativo, la animación de carga, y la barra de progreso hasta que Angular esté listo.

**Escenario 2: Sin conexión a internet**
Dado que el dispositivo no tiene WiFi ni datos móviles.
Cuando el usuario abre la app.
Entonces aparece la pantalla de error con icono `wifi_off` y el mensaje "Sin conexión a internet" con botón "
Reintentar".

**Escenario 3: Servidor inaccesible con red activa**
Dado que hay conexión pero el servidor no responde en 8 segundos.
Cuando el WebView intenta cargar la URL.
Entonces aparece la pantalla de error con icono `dns_outlined` y el mensaje "Servidor en mantenimiento".

**Escenario 4: Botón Atrás con historial**
Dado que el usuario navegó de la home al login y luego al panel.
Cuando pulsa el botón Atrás de Android.
Entonces la app navega a la pantalla anterior dentro de Angular (no cierra la app).

**Escenario 5: Botón Atrás sin historial**
Dado que el usuario está en la página inicial y no hay historial de navegación anterior.
Cuando pulsa el botón Atrás de Android.
Entonces aparece un diálogo de confirmación preguntando si desea salir de la aplicación.

---

### HISTORIA DE USUARIO 20: FRENTE

**ID:** HU-20-SWAGGER-API
**TÍTULO:** Como desarrollador o administrador técnico quiero acceder a la documentación interactiva de la API en
Swagger UI para explorar y probar los endpoints disponibles.

**REGLAS DE NEGOCIO:**

- La documentación está disponible en `/swagger-ui.html` cuando el backend está en ejecución.
- Los endpoints protegidos requieren introducir el token JWT en el campo `bearerAuth` de Swagger para poder ejecutarse.
- Los endpoints públicos (login, register, tratamientos activos, productos públicos) pueden probarse sin token.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Acceso a Swagger UI**
Dado que el backend está en ejecución.
Cuando se accede a `http://localhost/swagger-ui.html`.
Entonces se carga la interfaz Swagger con todos los grupos de endpoints organizados por recurso.

**Escenario 2: Prueba de endpoint protegido con token**
Dado que el desarrollador ha obtenido un token JWT mediante POST /api/auth/login.
Cuando introduce el token en el campo "Authorize" de Swagger y ejecuta GET /api/appointments.
Entonces recibe la lista paginada de citas con código 200.

**Escenario 3: Prueba de endpoint protegido sin token**
Dado que el desarrollador no ha introducido token.
Cuando ejecuta GET /api/appointments.
Entonces recibe respuesta 403 Forbidden.

---

## FASE 2 — Inventario y Ventas (Planificadas)

---

### HISTORIA DE USUARIO 21: FRENTE

**ID:** HU-21-REGISTER-SALE
**TÍTULO:** Como empleado o administrador quiero registrar una venta de productos y/o servicios al cerrar una sesión con
un cliente para llevar el control de ingresos del negocio.

**REGLAS DE NEGOCIO:**

- Una venta puede estar asociada a una cita existente (OneToOne opcional) o ser una venta independiente en mostrador.
- Los métodos de pago disponibles son: CASH, CARD, TRANSFER, OTHER (inicialmente solo CASH y Bizum).
- Una venta incluye detalle de los productos (`SaleProductDetail`) y/o tratamientos (`SaleTreatmentDetail`) con el
  precio en el momento de la venta.
- Al registrar productos en la venta, el stock disponible disminuye automáticamente.
- Se permite registrar propina y descuento.
- Solo ADMIN y EMPLOYEE pueden registrar ventas.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Venta asociada a cita**
Dado que la cita de "Ivan" para "Cortar puntas" está en estado COMPLETED.
Cuando el empleado registra la venta desde el panel indicando método de pago CASH.
Entonces el sistema crea la venta con el detalle del tratamiento al precio vigente y la asocia a la cita.

**Escenario 2: Venta en mostrador sin cita**
Dado que una clienta compra "SuavePelo" sin tener cita ese día.
Cuando el empleado registra la venta con producto, cantidad y método de pago.
Entonces la venta se crea sin `appointment` asociada y el stock de "SuavePelo" disminuye en la cantidad vendida.

**Escenario 3: Stock insuficiente**
Dado que el stock actual de "SuavePelo" es 0 unidades.
Cuando se intenta registrar una venta de ese producto.
Entonces el sistema advierte de stock insuficiente (sin bloquear si se decide permitir stock negativo como decisión de
diseño).

---

### HISTORIA DE USUARIO 22: FRENTE

**ID:** HU-22-STOCK-CONTROL
**TÍTULO:** Como administrador quiero consultar el stock actual de cada producto y registrar entradas de mercancía para
mantener actualizado el inventario del negocio.

**REGLAS DE NEGOCIO:**

- Cada producto tiene un registro de stock con `stock_actual` y `stock_minimo`.
- Los movimientos de stock pueden ser de tipo: INBOUND (entrada), OUTBOUND (salida) o ADJUSTMENT (ajuste manual).
- Las ventas generan movimientos OUTBOUND automáticamente.
- Las entradas de mercancía se registran manualmente por el administrador como movimientos INBOUND.
- Solo ADMIN puede registrar entradas y ajustes.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Consulta de stock**
Dado que el producto "SuavePelo" tiene 5 unidades en stock y stock mínimo de 2.
Cuando el administrador accede al panel de inventario.
Entonces ve el stock actual de cada producto junto a su nivel mínimo.

**Escenario 2: Registro de entrada de mercancía**
Dado que llegan 20 unidades de "SuavePelo" del proveedor "Schwarzkopf".
Cuando el administrador registra una entrada INBOUND de 20 unidades.
Entonces el stock actual pasa de 5 a 25 y se registra el movimiento en el historial.

**Escenario 3: Ajuste manual de stock**
Dado que tras un recuento físico el stock real de "CortaPelo" es 3 pero el sistema indica 5.
Cuando el administrador registra un ajuste con valor -2.
Entonces el stock pasa a 3 y se registra el movimiento como ADJUSTMENT con nota de motivo.

---

### HISTORIA DE USUARIO 23: FRENTE

**ID:** HU-23-STOCK-ALERT
**TÍTULO:** Como administrador quiero recibir una alerta visual cuando el stock de un producto crítico esté por debajo
del mínimo para poder hacer el pedido antes de que se agote.

**REGLAS DE NEGOCIO:**

- Un producto se considera en nivel crítico cuando `stock_actual < stock_minimo`.
- Los productos marcados con `isCritical = true` generan alertas prioritarias visibles en el panel principal.
- La alerta es visual en el dashboard; no implica envío de email en esta fase.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Alerta de stock bajo**
Dado que "SuavePelo" tiene `stock_actual = 1` y `stock_minimo = 3`, y está marcado como `isCritical`.
Cuando el administrador accede al panel principal.
Entonces aparece una tarjeta de alerta destacada indicando "SuavePelo — stock bajo (1 unidad)".

**Escenario 2: Sin alertas activas**
Dado que todos los productos tienen stock superior al mínimo.
Cuando el administrador accede al panel.
Entonces la sección de alertas de stock no muestra ningún aviso.

---

### HISTORIA DE USUARIO 24: FRENTE

**ID:** HU-24-SALES-REPORT
**TÍTULO:** Como administrador quiero consultar un resumen de ventas por período para conocer los ingresos del negocio y
los servicios más demandados.

**REGLAS DE NEGOCIO:**

- Los informes pueden filtrarse por día, semana y mes.
- Se muestran KPIs básicos: total de ingresos, número de citas completadas, top 5 tratamientos y top 5 productos
  vendidos.
- Solo ADMIN puede acceder al módulo de informes.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Resumen mensual**
Dado que en abril 2026 se registraron 45 citas completadas y ventas por 1.200 €.
Cuando el administrador selecciona "Abril 2026" en el filtro de informes.
Entonces ve el total de ingresos, número de citas, y el tratamiento más demandado del mes.

**Escenario 2: Sin ventas en el período**
Dado que no se registraron ventas en una semana concreta.
Cuando se filtra por esa semana.
Entonces se muestran todos los KPIs a cero sin errores.

---

## FASE 3 — Notificaciones Automáticas (Planificadas)

---

### HISTORIA DE USUARIO 25: FRENTE

**ID:** HU-25-REMINDER-NOTIFICATION
**TÍTULO:** Como cliente quiero recibir un recordatorio automático por email 24 horas antes de mi cita para no olvidar
la visita a la peluquería.

**REGLAS DE NEGOCIO:**

- El recordatorio se envía únicamente si la cita está en estado CONFIRMED o PENDING.
- No se envía recordatorio si la cita fue cancelada o ya completada.
- El cliente puede configurar si desea recibir recordatorios (opt-out posible).
- El sistema usa la entidad `Notification` con canal EMAIL y tipo REMINDER.
- El estado del envío queda registrado: PENDING → SENT o FAILED.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Envío exitoso de recordatorio**
Dado que la clienta "Angela" tiene una cita CONFIRMED mañana a las 10:00 y tiene email registrado.
Cuando el sistema ejecuta el proceso de recordatorios con 24 h de antelación.
Entonces se envía un email a "Angela" con el detalle de la cita y el estado de la notificación pasa a SENT.

**Escenario 2: Cita cancelada antes del recordatorio**
Dado que una cita fue cancelada hoy y habría generado un recordatorio mañana.
Cuando el proceso de recordatorios se ejecuta.
Entonces no se envía ningún email para esa cita.

**Escenario 3: Fallo en el envío**
Dado que el servidor de email no está disponible al intentar enviar el recordatorio.
Cuando el sistema intenta el envío.
Entonces el estado de la notificación pasa a FAILED y se registra el error para reintento o revisión manual.

---

### HISTORIA DE USUARIO 26: FRENTE

**ID:** HU-26-CONFIRMATION-NOTIFICATION
**TÍTULO:** Como cliente quiero recibir una confirmación automática por email cuando el empleado confirme mi cita para
tener constancia de que está aceptada.

**REGLAS DE NEGOCIO:**

- La notificación de tipo CONFIRMATION se envía cuando una cita pasa de PENDING a CONFIRMED.
- Solo se envía si el cliente tiene email registrado.
- Canal: EMAIL. Tipo: CONFIRMATION.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Notificación de confirmación enviada**
Dado que la cita de "Ivan" pasa de PENDING a CONFIRMED.
Cuando el empleado actualiza el estado.
Entonces el sistema envía automáticamente un email a "Ivan" confirmando la fecha, hora y tratamientos de la cita.

**Escenario 2: Cliente sin email**
Dado que el cliente "Angela" no tiene email registrado en su ficha.
Cuando su cita es confirmada.
Entonces no se intenta el envío y la notificación queda registrada con estado FAILED con motivo "Sin email".

---

### HISTORIA DE USUARIO 27: FRENTE

**ID:** HU-27-CANCELLATION-NOTIFICATION
**TÍTULO:** Como cliente quiero recibir una notificación por email si mi cita es cancelada por el negocio para poder
buscar otra fecha alternativa.

**REGLAS DE NEGOCIO:**

- La notificación de tipo CANCELLATION se envía cuando una cita pasa a estado CANCELLED y la acción la realizó un
  EMPLOYEE o ADMIN (no el propio cliente).
- Canal: EMAIL. Tipo: CANCELLATION.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Notificación de cancelación por el negocio**
Dado que el empleado cancela la cita de "Angela" por indisponibilidad.
Cuando el estado cambia a CANCELLED desde el panel del empleado.
Entonces el sistema envía un email a "Angela" indicando que su cita fue cancelada e invitándola a contactar para
reprogramar.

**Escenario 2: Cancelación por el propio cliente**
Dado que el cliente "Ivan" cancela su propia cita desde el panel.
Cuando el estado cambia a CANCELLED.
Entonces no se envía notificación al cliente (él mismo realizó la acción).

---

### HISTORIA DE USUARIO 28: FRENTE

**ID:** HU-28-BIRTHDAY-NOTIFICATION
**TÍTULO:** Como administradora quiero que el sistema envíe automáticamente un mensaje de felicitación a los clientes en
su cumpleaños para fidelizarlos y reforzar la relación con el negocio.

**REGLAS DE NEGOCIO:**

- El sistema revisa diariamente qué clientes cumplen años ese día.
- Solo se envía si el cliente tiene email registrado y la opción de notificaciones activada.
- Canal: EMAIL. Tipo: BIRTHDAY.
- El mensaje puede incluir un mensaje personalizable configurado por la administradora.

---

**DORSO**

**CRITERIOS DE ACEPTACIÓN:**

**Escenario 1: Envío de felicitación de cumpleaños**
Dado que hoy es el cumpleaños de "Angela" (campo `birthday` coincide con la fecha de hoy).
Cuando el proceso diario de notificaciones se ejecuta.
Entonces el sistema envía un email de felicitación a "Angela" con la plantilla configurada.

**Escenario 2: Cliente sin fecha de nacimiento registrada**
Dado que el cliente "Cliente sin BD" no tiene `birthday` registrado.
Cuando el proceso diario se ejecuta.
Entonces no se intenta ningún envío para ese cliente.

**Escenario 3: Varios clientes de cumpleaños el mismo día**
Dado que "Ivan" y "Angela" cumplen años el mismo día.
Cuando el proceso se ejecuta.
Entonces ambos reciben su email de felicitación de forma independiente.

---

*Documento con las historias de usuario para el Proyecto Intermodular DAM — CitaBella · Iván López Molina · Curso
2025–2026*