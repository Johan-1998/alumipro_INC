/* archivo de servicio ventas */
import { http } from "./http";
import type { VentaCreate, VentaCreateResponse, VentaDetalle, VentaSummary } from "../types/venta";

export const ventasService = {
  create: (data: VentaCreate) =>
    http<VentaCreateResponse>("/api/ventas", { method: "POST", body: JSON.stringify(data) }),

  list: () => http<VentaSummary[]>("/api/ventas"),

  get: (id: number) => http<VentaDetalle>(`/api/ventas/${id}`)
};
