/* archivo de arranque del frontend */
import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { router } from "./app/router";
import { AuthProvider } from "./app/auth";
import "./styles/global.css";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3500,
          className: "alumiproToast"
        }}
      />
    </AuthProvider>
  </React.StrictMode>
);
