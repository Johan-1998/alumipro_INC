/* archivo de servicio auth */
import { http } from "./http";
import type { LoginRequest, LoginResponse, UsuarioUpsert, UsuarioView } from "../types/usuario";

export const authService = {
  login: (payload: LoginRequest) =>
    http<LoginResponse>("/api/auth/login", { method: "POST", body: JSON.stringify(payload) }),

  me: () => http<LoginResponse["user"]>("/api/auth/me"),

  listarUsuarios: () => http<UsuarioView[]>("/api/usuarios"),

  crearUsuario: (payload: UsuarioUpsert) =>
    http<UsuarioView>("/api/usuarios", { method: "POST", body: JSON.stringify(payload) }),

  actualizarUsuario: (id: number, payload: Partial<UsuarioUpsert>) =>
    http<UsuarioView>(`/api/usuarios/${id}`, { method: "PUT", body: JSON.stringify(payload) }),

  eliminarUsuario: (id: number) => http<void>(`/api/usuarios/${id}`, { method: "DELETE" })
};
