# ALUMIPRO Mobile

Aplicación Android nativa en Java + XML integrada al monorepo de ALUMIPRO.

## Alcance implementado
- Inicio de sesión con JWT contra el backend Spring Boot.
- Dashboard móvil.
- Gestión de clientes.
- Consulta y gestión de productos para rol admin.
- Registro de ventas.
- Histórico y detalle de ventas.
- Persistencia local con SQLite para sesión, configuración API, caché y notificaciones.
- Canal de datos en tiempo real por SSE respaldado por MySQL para notificaciones operativas.
- Multimedia con MediaPlayer y VideoView.

## Importante
La URL del backend se configura desde la misma app en **Configurar API**. Para emulador Android use `http://10.0.2.2:8080/`. Para dispositivo físico use la IP local de su equipo, por ejemplo `http://192.168.1.10:8080/`.

## Estructura
- `app/src/main/java`: código Java.
- `app/src/main/res`: layouts XML, colores, temas y recursos.
- `app/src/test`: pruebas unitarias base.

## Sincronización recomendada
- Abra `apps/mobile` directamente en Android Studio.
- Permita que Android Studio haga la primera sincronización Gradle y descargue dependencias.
- Si Android Studio solicita regenerar wrapper o archivos Gradle locales, acepte la acción.
