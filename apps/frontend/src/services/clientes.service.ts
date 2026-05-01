/* archivo que maneja el servicio clientes */
import { http } from "./http";
import type { Cliente, ClienteCreate } from "../types/cliente";

export const clientesService = {
  list: () => http<Cliente[]>("/api/clientes"),
  get: (id: number) => http<Cliente>(`/api/clientes/${id}`),
  create: (data: ClienteCreate) =>
    http<Cliente>("/api/clientes", { method: "POST", body: JSON.stringify(data) }),
  update: (id: number, data: ClienteCreate) =>
    http<Cliente>(`/api/clientes/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  remove: (id: number) => http<void>(`/api/clientes/${id}`, { method: "DELETE" })
};
