/* archivo de almacenamiento auth */
import type { UsuarioAuth } from "../types/usuario";

export type AuthState = {
  token: string;
  user: UsuarioAuth;
};

const KEY = "alumipro:auth";
export const AUTH_EVENT = "alumipro:auth";

export function readAuth(): AuthState | null {
  try {
    const raw = localStorage.getItem(KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw) as AuthState;
    if (!parsed?.token || !parsed?.user) return null;
    return parsed;
  } catch {
    return null;
  }
}

export function writeAuth(state: AuthState) {
  localStorage.setItem(KEY, JSON.stringify(state));
  window.dispatchEvent(new Event(AUTH_EVENT));
}

export function clearAuth() {
  localStorage.removeItem(KEY);
  window.dispatchEvent(new Event(AUTH_EVENT));
}
