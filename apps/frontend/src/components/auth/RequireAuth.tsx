/* archivo de componente requireauth */
import type { ReactElement } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../../app/auth";

export function RequireAuth({ children }: { children: ReactElement }) {
  const { user, isReady } = useAuth();
  const loc = useLocation();

  if (!isReady) return null;

  if (!user) {
    const redirect = encodeURIComponent(loc.pathname + loc.search);
    return <Navigate to={`/auth/login?redirect=${redirect}`} replace />;
  }

  return children;
}
