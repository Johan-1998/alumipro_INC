# Pruebas automatizadas – ALUMIPRO

## Herramientas utilizadas
### Backend
- JUnit 5
- Mockito
- Spring Boot Test (dependencia existente en el `pom.xml`)

### Frontend
- Node Test Runner (`node:test`)
- TypeScript compilado con `tsc`
- ESLint
- Vite build

## Casos automatizados agregados

### Backend (`apps/backend/src/test/java`)
1. `ClienteServiceTest`
   - crea cliente válido;
   - rechaza email inválido;
   - rechaza teléfono inválido.

2. `ProductoServiceTest`
   - crea producto válido;
   - rechaza precio negativo;
   - rechaza stock negativo.

3. `VentaServiceTest`
   - registra venta y calcula total;
   - impide venta con stock insuficiente.

4. `ApiAuthControllerTest`
   - login exitoso;
   - validación de campos obligatorios.

5. `ApiUsuarioControllerTest`
   - crea usuario con contraseña encriptada;
   - rechaza correo duplicado;
   - actualiza rol y contraseña.

### Frontend (`apps/frontend/tests`)
1. `format.test.ts`
   - formatea valores en COP;
   - convierte números seguros.

2. `validators.test.ts`
   - valida correos;
   - valida teléfonos.

3. `auth.storage.test.ts`
   - guarda y recupera sesión;
   - limpia sesión;
   - dispara evento de autenticación.

## Comandos de ejecución

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

## Reportes
Los resultados del frontend ejecutados en este paquete quedaron guardados en:
- `reportes/frontend-lint.txt`
- `reportes/frontend-test.txt`
- `reportes/frontend-build.txt`

Para backend se dejan las pruebas listas en la estructura Maven estándar para ejecutar desde IntelliJ o con Maven local del aprendiz.
