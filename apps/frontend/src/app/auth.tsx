/* archivo de contexto auth */
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { authService } from "../services/auth.service";
import { AUTH_EVENT, clearAuth, readAuth, writeAuth } from "../services/auth.storage";
import type { LoginRequest, UsuarioAuth } from "../types/usuario";

type AuthValue = {
  user: UsuarioAuth | null;
  token: string | null;
  isReady: boolean;
  isAdmin: boolean;
  login: (payload: LoginRequest) => Promise<UsuarioAuth>;
  logout: () => void;
};

const Ctx = createContext<AuthValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UsuarioAuth | null>(() => readAuth()?.user ?? null);
  const [token, setToken] = useState<string | null>(() => readAuth()?.token ?? null);
  const [isReady, setIsReady] = useState(false);

  const syncFromStorage = useCallback(() => {
    const a = readAuth();
    setUser(a?.user ?? null);
    setToken(a?.token ?? null);
  }, []);

  useEffect(() => {
    window.addEventListener(AUTH_EVENT, syncFromStorage);
    return () => window.removeEventListener(AUTH_EVENT, syncFromStorage);
  }, [syncFromStorage]);

  useEffect(() => {
    const a = readAuth();
    if (!a?.token) {
      setIsReady(true);
      return;
    }

    authService.me()
      .then((u) => {
        writeAuth({ token: a.token, user: u });
        setUser(u);
        setToken(a.token);
      })
      .catch(() => {
        clearAuth();
        setUser(null);
        setToken(null);
      })
      .finally(() => setIsReady(true));
  }, []);

  const login = useCallback(async (payload: LoginRequest) => {
    const res = await authService.login(payload);
    writeAuth({ token: res.token, user: res.user });
    setUser(res.user);
    setToken(res.token);
    return res.user;
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    setUser(null);
    setToken(null);
  }, []);

  const isAdmin = useMemo(() => user?.rol === "ADMIN", [user]);

  const value: AuthValue = useMemo(
    () => ({ user, token, isReady, isAdmin, login, logout }),
    [user, token, isReady, isAdmin, login, logout]
  );

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useAuth() {
  const v = useContext(Ctx);
  if (!v) throw new Error("AuthProvider no configurado");
  return v;
}
