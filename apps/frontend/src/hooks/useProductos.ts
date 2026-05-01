/* archivo que contiene el hook useproductos */
import { useCallback, useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { productosService } from "../services/productos.service";
import { HttpError } from "../services/http";
import type { Producto, ProductoCreate } from "../types/producto";

type State = {
  data: Producto[];
  isLoading: boolean;
  error: string | null;
};

export function useProductos() {
  const [state, setState] = useState<State>({ data: [], isLoading: true, error: null });

  const load = useCallback(async () => {
    setState((s) => ({ ...s, isLoading: true, error: null }));
    try {
      const data = await productosService.list();
      setState({ data, isLoading: false, error: null });
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo cargar productos.";
      setState((s) => ({ ...s, isLoading: false, error: msg }));
      toast.error(msg);
    }
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  const create = useCallback(async (payload: ProductoCreate) => {
    try {
      const created = await productosService.create(payload);
      toast.success("Producto creado.");
      setState((s) => ({ ...s, data: [created, ...s.data] }));
      return created;
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo crear el producto.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const update = useCallback(async (id: number, payload: ProductoCreate) => {
    try {
      const updated = await productosService.update(id, payload);
      toast.success("Producto actualizado.");
      setState((s) => ({ ...s, data: s.data.map((p) => (p.id === id ? updated : p)) }));
      return updated;
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo actualizar el producto.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const remove = useCallback(async (id: number) => {
    try {
      await productosService.remove(id);
      toast.success("Producto eliminado.");
      setState((s) => ({ ...s, data: s.data.filter((p) => p.id !== id) }));
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo eliminar el producto.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const byId = useMemo(() => {
    const map = new Map<number, Producto>();
    for (const p of state.data) map.set(p.id, p);
    return map;
  }, [state.data]);

  return { ...state, load, create, update, remove, byId };
}
