# Pruebas manuales documentadas – ALUMIPRO

## Datos de prueba base
### Usuarios
- Administrador: `admin@alumipro.com` / `admin123`
- Vendedor: `vendedor@alumipro.com` / `vendedor123`

### Cliente sugerido
- Nombre: `Cliente QA EV02`
- Teléfono: `3005557788`
- Dirección: `Calle 80 # 15-20`
- Email: `cliente.qa@alumipro.com`

### Producto sugerido
- Nombre: `Perfil QA 3m`
- Precio: `125000`
- Stock: `12`
- Descripción: `Producto de pruebas manuales EV02`

## Casos de prueba manual
| ID | Módulo | Rol | Datos | Resultado esperado |
|---|---|---|---|---|
| PM-01 | Login administrador | Admin | admin@alumipro.com / admin123 | Acceso exitoso al dashboard |
| PM-02 | Login vendedor | Vendedor | vendedor@alumipro.com / vendedor123 | Acceso exitoso sin menú de usuarios |
| PM-03 | Restricción por rol | Vendedor | Intentar abrir `/usuarios` | Redirección o denegación |
| PM-04 | Crear cliente | Admin/Vendedor | Datos de cliente sugerido | Registro exitoso |
| PM-05 | Editar cliente | Admin/Vendedor | Cambio de teléfono | Actualización visible en listado |
| PM-06 | Eliminar cliente | Admin | Cliente QA EV02 | Registro eliminado |
| PM-07 | Crear producto | Admin | Datos de producto sugerido | Registro exitoso |
| PM-08 | Editar producto | Admin | Cambio de precio o stock | Actualización visible |
| PM-09 | Eliminar producto | Admin | Perfil QA 3m | Registro eliminado |
| PM-10 | Registrar venta | Vendedor/Admin | Cliente existente + producto con stock | Venta creada y total correcto |
| PM-11 | Validar stock insuficiente | Vendedor/Admin | Cantidad mayor al stock | Mensaje de error y sin descuento |
| PM-12 | Consultar histórico | Admin/Vendedor | Abrir `/ventas/historico` | Se listan ventas registradas |
| PM-13 | Ver detalle venta | Admin/Vendedor | Seleccionar venta | Se muestran ítems y total |
| PM-14 | Crear usuario | Admin | Nuevo usuario vendedor | Usuario visible en módulo usuarios |
| PM-15 | Eliminar usuario | Admin | Usuario creado en PM-14 | Usuario eliminado |

## Dónde registrar los resultados
Usa la siguiente matriz dentro del acta o en este mismo archivo al momento de la sustentación:

| ID | Ejecutado por | Fecha | Resultado real | Estado | Evidencia |
|---|---|---|---|---|---|
| PM-01 | Aprendiz |  |  | Aprobado / Fallido | Captura login |
| PM-02 | Aprendiz |  |  | Aprobado / Fallido | Captura dashboard vendedor |
| PM-03 | Aprendiz |  |  | Aprobado / Fallido | Captura denegación |
| PM-04 | Aprendiz |  |  | Aprobado / Fallido | Captura clientes |
| PM-05 | Aprendiz |  |  | Aprobado / Fallido | Captura edición cliente |
| PM-06 | Aprendiz |  |  | Aprobado / Fallido | Captura eliminación cliente |
| PM-07 | Aprendiz |  |  | Aprobado / Fallido | Captura productos |
| PM-08 | Aprendiz |  |  | Aprobado / Fallido | Captura edición producto |
| PM-09 | Aprendiz |  |  | Aprobado / Fallido | Captura eliminación producto |
| PM-10 | Aprendiz |  |  | Aprobado / Fallido | Captura nueva venta |
| PM-11 | Aprendiz |  |  | Aprobado / Fallido | Captura mensaje stock |
| PM-12 | Aprendiz |  |  | Aprobado / Fallido | Captura histórico |
| PM-13 | Aprendiz |  |  | Aprobado / Fallido | Captura detalle |
| PM-14 | Aprendiz |  |  | Aprobado / Fallido | Captura usuarios |
| PM-15 | Aprendiz |  |  | Aprobado / Fallido | Captura eliminación usuario |
