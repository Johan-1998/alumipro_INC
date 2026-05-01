# Cumplimiento de guías (enfoque FRONTEND)

> Nota: Varias guías solicitan evidencias documentales (PDF/Word) que se entregan aparte como documentos.  
> En el código se reflejan las **pantallas, flujos y componentes** que dichas guías exigen para el frontend.

## Guía 1 (Fase: análisis)
- **Requisitos / prototipos / interfaz**: La guía menciona actividades de análisis y validación con artefactos y prototipos.
- **Implementación en este frontend**:
  - Se construyó un layout completo (Sidebar + Topbar + breadcrumbs) y páginas funcionales para el sistema.
  - Se incluye manejo de errores: `ErrorPage`, `NotFoundPage` y pantalla informativa `ErrorInfoPage`.

## Guía 2 (Fase: análisis)
- Principalmente evidencias de modelado/validación (documentales).  
- **Impacto en frontend**: no exige pantallas adicionales específicas.

## Guía 3 (Fase: planeación — algoritmia)
- Evidencias centradas en lógica/algoritmos y JavaScript (documentales y talleres).
- **Impacto en frontend**: no exige pantallas adicionales específicas para el proyecto Alumipro.

## Guía 4 (Fase: planeación — diseño)
- Evidencias centradas en base de datos, POO, validación de artefactos (documentales).
- **Impacto en frontend**: no exige pantallas adicionales específicas.

## Guía 5 (Fase: planeación — prototipado e interfaz gráfica)
Evidencias relevantes para frontend:
- **GA5-220501095-AA1-EV01** (Taller prototipo): incluye pantallas:
  - *Validar autenticidad de un usuario*: `src/pages/auth/LoginPage.tsx`
  - *Ingresar nombres, apellidos, cédula, fecha nacimiento*: `src/pages/auth/RegisterPage.tsx`
  - *Pantalla de errores*: `src/pages/ErrorInfoPage.tsx` + `src/pages/ErrorPage.tsx`
- **GA5-220501095-AA1-EV03 / EV05** (Interfaz + mapa de navegación): el frontend está organizado por rutas y navegación (Sidebar) que equivale al mapa de navegación del sistema.
- **GA5-220501095-AA1-EV04** (Maquetación HTML): el resultado final está implementado con React + Vite; los componentes están estructurados y maquetados con CSS propio.
- **EV06/EV07/EV08** (Móvil / Android XML): corresponden al desarrollo móvil. Se dejan listos los colores, tipografías y la arquitectura de UI para mantener consistencia al migrar a APK.

## Guía 6 (Fase: ejecución — interfaz de usuario)
Evidencias relevantes para frontend:
- **GA6-220501096-AA3-EV02 / EV03**: diseño web/móvil e interfaces gráficas.
  - Se implementa el diseño web completo y funcional con React + CSS Modules.
- **GA6-220501096-AA4-EV03**: diseño frontend cumpliendo requerimientos.
  - Páginas implementadas: Dashboard, Clientes (CRUD), Productos (CRUD), Ventas (registro).

## Guía 7 (Fase: ejecución — codificación del software)
Evidencias relevantes para frontend:
- **GA7-220501096-AA4-EV03**: “Componente frontend del proyecto formativo”.
  - El módulo frontend está codificado con React, con separación de responsabilidades (services/hooks/components/pages), validaciones y manejo de errores.
- **Estándares / validación / pruebas**:
  - Los formularios incluyen validaciones y mensajes de error.
  - `ventas.service.ts` incluye TODO sobre endpoints faltantes para consulta de ventas (para métricas reales).

## Endpoints faltantes (marcados con TODO)
- **GET /api/ventas** (y/o métricas) no existe en el backend actual; por eso el dashboard calcula métricas locales en `useVentas.ts`.
