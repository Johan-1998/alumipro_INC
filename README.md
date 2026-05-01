# ALUMIPRO – Sistema integrado de gestión interna

ALUMIPRO es una solución web para la gestión interna de una empresa colombiana del sector aluminio. El proyecto integra autenticación con JWT, control de roles, administración de clientes, productos, ventas e histórico de operaciones en un solo repositorio.

## Tecnologías principales
- **Backend:** Java 17, Spring Boot 2.7, Spring Security, JPA
- **Frontend:** React 19, TypeScript, Vite, CSS Modules
- **Base de datos:** MySQL 8 / MariaDB (XAMPP)
- **Pruebas automatizadas:**
  - Backend: JUnit 5 + Mockito
  - Frontend: Node Test Runner + TypeScript compilado

## Estructura del repositorio
- `apps/backend`: API REST y capa de negocio.
- `apps/frontend`: interfaz web.
- `db`: scripts de base de datos.
- `docs`: documentación técnica y evidencias.
- `legacy`: entregables previos conservados.

## Requisitos previos
- Windows 11
- Java 17
- IntelliJ IDEA o Android Studio/VS Code para apoyo
- Node.js 24
- XAMPP con MySQL
- Git

## Configuración de base de datos
1. Iniciar **MySQL** desde XAMPP.
2. Crear la base `alumipro_db` si no existe.
3. Importar en este orden:
   - `db/schema.sql`
   - `db/seed.sql`
4. Verificar que existan las tablas `usuarios`, `clientes`, `productos`, `ventas` y `detalle_venta`.

## Ejecución del backend
En IntelliJ:
1. Abrir `apps/backend` como módulo Maven.
2. Ejecutar el ciclo `spring-boot:run` o la clase `AlumiproAa3Application`.

Por consola, cuando Maven esté disponible:
```bash
cd apps/backend
mvn spring-boot:run
```

La API queda publicada en:
- `http://localhost:8080/api`

## Ejecución del frontend
```bash
cd apps/frontend
npm install
npm run dev
```

La aplicación queda publicada en:
- `http://localhost:5173`

## Credenciales de prueba
- **Administrador:** `admin@alumipro.com` / `admin123`
- **Vendedor:** `vendedor@alumipro.com` / `vendedor123`

## Pruebas automatizadas
### Frontend
```bash
cd apps/frontend
npm run lint
npm run test
npm run build
```

### Backend
```bash
cd apps/backend
mvn test
mvn package
```

## Evidencias EV02
Las evidencias finales de la entrega están en:

- `docs/evidencias/GA8-220501096-AA1-EV02/GA8-220501096-AA1-EV02_Documento_Entrega_ALUMIPRO.docx`
- `docs/evidencias/GA8-220501096-AA1-EV02/docs/03-pruebas-automatizadas.md`
- `docs/evidencias/GA8-220501096-AA1-EV02/docs/04-pruebas-manuales.md`
- `docs/evidencias/GA8-220501096-AA1-EV02/reportes/`

## Repositorio
- URL: `https://github.com/Johan-1998/alumipro_INC.git`
- Rama principal: `main`
