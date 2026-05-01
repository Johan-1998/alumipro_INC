/* archivo de tipos usuario */
export type RolUsuario = "ADMIN" | "VENDEDOR";

export type UsuarioAuth = {
  id: number;
  nombre: string;
  correo: string;
  rol: RolUsuario;
};

export type LoginRequest = {
  correo: string;
  password: string;
};

export type LoginResponse = {
  token: string;
  user: UsuarioAuth;
};

export type UsuarioUpsert = {
  nombre: string;
  correo: string;
  password?: string;
  rol: RolUsuario;
};

export type UsuarioView = {
  id: number;
  nombre: string;
  correo: string;
  rol: RolUsuario;
};
