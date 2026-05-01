# GA8-220501096-AA1-EV02 – Guía de ejecución del sistema

## 1. Requisitos del ambiente
- Windows 11
- Java 17.0.12
- Node.js 24.12.0
- XAMPP con MySQL
- IntelliJ IDEA
- Git

## 2. Preparación de la base de datos
1. Abrir XAMPP Control Panel.
2. Iniciar **MySQL**.
3. Ingresar a phpMyAdmin.
4. Crear la base `alumipro_db` si no existe.
5. Importar:
   - `db/schema.sql`
   - `db/seed.sql`

> Configuración base usada por el proyecto: host `localhost`, puerto `3306`, usuario `root`, contraseña vacía.

## 3. Ejecución del backend
En IntelliJ:
1. Abrir `apps/backend` como proyecto Maven.
2. Esperar la sincronización.
3. Ejecutar `spring-boot:run` desde el panel Maven o la clase `AlumiproAa3Application`.

Por consola:
```bash
cd apps/backend
mvn spring-boot:run
```

Pruebas rápidas:
- `POST /api/auth/login`
- `GET /api/productos`
- `GET /api/clientes`
- `GET /api/ventas`

## 4. Ejecución del frontend
```bash
cd apps/frontend
npm install
npm run dev
```

## 5. Validación funcional mínima
- Iniciar sesión como administrador.
- Ver dashboard con métricas.
- Crear, editar y eliminar clientes.
- Crear, editar y eliminar productos.
- Crear venta y validar descuento de stock.
- Consultar histórico y detalle de ventas.
- Gestionar usuarios desde el rol administrador.

## 6. Artefactos generados
- Frontend: `apps/frontend/dist/`
- Backend: `apps/backend/target/` (generado al ejecutar `mvn package`)
- Evidencias: `docs/evidencias/GA8-220501096-AA1-EV02/`
