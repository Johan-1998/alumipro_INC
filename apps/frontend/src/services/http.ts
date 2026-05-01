/* archivo de servicio http */
import { clearAuth, readAuth } from "./auth.storage";

export const API_BASE_URL: string = (import.meta.env.VITE_API_BASE_URL as string | undefined) ?? "";

export class HttpError extends Error {
  readonly status: number;
  readonly payload?: unknown;

  constructor(status: number, message: string, payload?: unknown) {
    super(message);
    this.status = status;
    this.payload = payload;
  }
}

function joinUrl(base: string, path: string) {
  if (!base) return path;
  if (base.endsWith("/") && path.startsWith("/")) return base.slice(0, -1) + path;
  if (!base.endsWith("/") && !path.startsWith("/")) return base + "/" + path;
  return base + path;
}

export async function http<T>(path: string, init?: RequestInit): Promise<T> {
  const auth = readAuth();
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(init?.headers as Record<string, string> | undefined)
  };

  if (auth?.token) {
    headers.Authorization = `Bearer ${auth.token}`;
  }

  const res = await fetch(joinUrl(API_BASE_URL, path), {
    ...init,
    headers
  });

  if (res.status === 204) return null as T;

  const contentType = res.headers.get("content-type") ?? "";
  const isJson = contentType.includes("application/json");

  if (!res.ok) {
    let payload: unknown = undefined;
    let msg = `Error HTTP ${res.status}`;

    try {
      if (isJson) {
        payload = await res.json();
        const maybeMsg = (payload as { message?: string })?.message;
        if (maybeMsg) msg = String(maybeMsg);
      } else {
        const txt = await res.text();
        payload = txt;
        if (txt) msg = txt;
      }
    } catch {
      // sin cuerpo a propósito
    }

    if (res.status === 401) {
      clearAuth();
      throw new HttpError(401, "Sesión no válida. Inicia sesión.", payload);
    }

    const friendly =
      res.status === 400 ? msg :
      res.status === 404 ? "No se encontró el recurso solicitado." :
      res.status === 500 ? "Error interno del servidor. Intenta de nuevo." :
      msg;

    throw new HttpError(res.status, friendly, payload);
  }

  if (isJson) return (await res.json()) as T;
  const text = await res.text();
  return (text ? (JSON.parse(text) as T) : (null as T));
}
