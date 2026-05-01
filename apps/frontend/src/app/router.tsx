/* archivo de código router */
import { createBrowserRouter } from "react-router-dom";
import { AppLayout } from "../components/layout/AppLayout";
import { RequireAuth } from "../components/auth/RequireAuth";
import { RequireAdmin } from "../components/auth/RequireAdmin";
import { ErrorPage } from "../pages/ErrorPage";
import { NotFoundPage } from "../pages/NotFoundPage";

import { DashboardPage } from "../pages/DashboardPage";

import { ClientesListPage } from "../pages/clientes/ClientesListPage";
import { ClienteFormPage } from "../pages/clientes/ClienteFormPage";

import { ProductosListPage } from "../pages/productos/ProductosListPage";
import { ProductoFormPage } from "../pages/productos/ProductoFormPage";

import { VentaCreatePage } from "../pages/ventas/VentaCreatePage";
import { VentasHistoryPage } from "../pages/ventas/VentasHistoryPage";

import { LoginPage } from "../pages/auth/LoginPage";
import { RegisterPage } from "../pages/auth/RegisterPage";
import { ErrorInfoPage } from "../pages/ErrorInfoPage";
import { UsuariosPage } from "../pages/usuarios/UsuariosPage";

export const router = createBrowserRouter([
  {
    path: "/auth/login",
    element: <LoginPage />,
    handle: { title: "Acceso", crumb: "Acceso" }
  },
  {
    path: "/auth/registro",
    element: <RegisterPage />,
    handle: { title: "Registro", crumb: "Registro" }
  },
  {
    path: "/",
    element: (
      <RequireAuth>
        <AppLayout />
      </RequireAuth>
    ),
    errorElement: <ErrorPage />,
    children: [
      {
        index: true,
        element: <DashboardPage />,
        handle: { title: "Dashboard", crumb: "Dashboard" }
      },
      {
        path: "clientes",
        element: <ClientesListPage />,
        handle: { title: "Clientes", crumb: "Clientes" }
      },
      {
        path: "clientes/nuevo",
        element: <ClienteFormPage mode="create" />,
        handle: { title: "Nuevo cliente", crumb: "Nuevo" }
      },
      {
        path: "clientes/:id/editar",
        element: <ClienteFormPage mode="edit" />,
        handle: { title: "Editar cliente", crumb: "Editar" }
      },
      {
        path: "productos",
        element: <ProductosListPage />,
        handle: { title: "Productos", crumb: "Productos" }
      },
      {
        path: "productos/nuevo",
        element: (
          <RequireAdmin>
            <ProductoFormPage mode="create" />
          </RequireAdmin>
        ),
        handle: { title: "Nuevo producto", crumb: "Nuevo" }
      },
      {
        path: "productos/:id/editar",
        element: (
          <RequireAdmin>
            <ProductoFormPage mode="edit" />
          </RequireAdmin>
        ),
        handle: { title: "Editar producto", crumb: "Editar" }
      },
      {
        path: "ventas/nueva",
        element: <VentaCreatePage />,
        handle: { title: "Registrar venta", crumb: "Nueva venta" }
      },
      {
        path: "ventas/historico",
        element: <VentasHistoryPage />,
        handle: { title: "Histórico de ventas", crumb: "Histórico" }
      },
      {
        path: "usuarios",
        element: (
          <RequireAdmin>
            <UsuariosPage />
          </RequireAdmin>
        ),
        handle: { title: "Usuarios", crumb: "Usuarios" }
      },
      {
        path: "errores",
        element: <ErrorInfoPage />,
        handle: { title: "Errores", crumb: "Errores" }
      },
      {
        path: "*",
        element: <NotFoundPage />,
        handle: { title: "No encontrado", crumb: "404" }
      }
    ]
  }
]);
