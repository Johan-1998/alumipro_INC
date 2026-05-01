/* archivo de hook useventas */
import { useCallback, useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { ventasService } from "../services/ventas.service";
import { HttpError } from "../services/http";
import type { VentaCreate, VentaDetalle, VentaSummary } from "../types/venta";

export function useVentas() {
  const [data, setData] = useState<VentaSummary[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await ventasService.list();
      setData(res);
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo cargar ventas.";
      setError(msg);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const create = useCallback(async (payload: VentaCreate) => {
    try {
      const res = await ventasService.create(payload);
      toast.success("Venta registrada.");
      await refresh();
      return res;
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo registrar la venta.";
      toast.error(msg);
      throw e;
    }
  }, [refresh]);

  const getDetalle = useCallback(async (id: number): Promise<VentaDetalle> => {
    return await ventasService.get(id);
  }, []);

  const stats = useMemo(() => {
    const count = data.length;
    const total = data.reduce((acc, it) => acc + (Number(it.total) || 0), 0);
    return { count, total };
  }, [data]);

  return { data, isLoading, error, refresh, create, getDetalle, stats };
}
