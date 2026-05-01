/* archivo que maneja el servicio productos */
import { http } from "./http";
import type { Producto, ProductoCreate } from "../types/producto";

export const productosService = {
  list: () => http<Producto[]>("/api/productos"),
  get: (id: number) => http<Producto>(`/api/productos/${id}`),
  create: (data: ProductoCreate) =>
    http<Producto>("/api/productos", { method: "POST", body: JSON.stringify(data) }),
  update: (id: number, data: ProductoCreate) =>
    http<Producto>(`/api/productos/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  remove: (id: number) => http<void>(`/api/productos/${id}`, { method: "DELETE" })
};
