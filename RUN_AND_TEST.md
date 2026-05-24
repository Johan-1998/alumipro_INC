# Cómo ejecutar y probar ALUMIPRO Mobile

1. Levante MySQL en XAMPP.
2. Ejecute el backend Spring Boot desde `apps/backend`.
3. Abra `apps/mobile` en Android Studio y permita la sincronización Gradle.
4. Instale la app en emulador o celular.
5. En la pantalla de login toque **Configurar API**.
   - Emulador Android: `http://10.0.2.2:8080/`
   - Celular físico: `http://IP_DE_TU_PC:8080/`
6. Inicie sesión con:
   - `admin@alumipro.com / admin123`
   - `vendedor@alumipro.com / vendedor123`

## Pruebas recomendadas
- Clientes: listar, crear, editar y eliminar (eliminar solo admin).
- Productos: listar para todos; crear/editar/eliminar solo admin.
- Ventas: registrar venta, validar stock insuficiente, revisar histórico y detalle.
- Ayuda: verificar SQLite, audio, video y notificaciones.
- Tiempo real: deje abierta la pestaña Ayuda en móvil y cree un cliente/producto/venta desde web o móvil; debe aparecer una notificación operativa nueva.
