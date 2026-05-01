/* archivo que contiene el hook useclientes */
import { useCallback, useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { clientesService } from "../services/clientes.service";
import { HttpError } from "../services/http";
import type { Cliente, ClienteCreate } from "../types/cliente";

type State = {
  data: Cliente[];
  isLoading: boolean;
  error: string | null;
};

export function useClientes() {
  const [state, setState] = useState<State>({ data: [], isLoading: true, error: null });

  const load = useCallback(async () => {
    setState((s) => ({ ...s, isLoading: true, error: null }));
    try {
      const data = await clientesService.list();
      setState({ data, isLoading: false, error: null });
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo cargar clientes.";
      setState((s) => ({ ...s, isLoading: false, error: msg }));
      toast.error(msg);
    }
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  const create = useCallback(async (payload: ClienteCreate) => {
    try {
      const created = await clientesService.create(payload);
      toast.success("Cliente creado.");
      setState((s) => ({ ...s, data: [created, ...s.data] }));
      return created;
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo crear el cliente.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const update = useCallback(async (id: number, payload: ClienteCreate) => {
    try {
      const updated = await clientesService.update(id, payload);
      toast.success("Cliente actualizado.");
      setState((s) => ({ ...s, data: s.data.map((c) => (c.id === id ? updated : c)) }));
      return updated;
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo actualizar el cliente.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const remove = useCallback(async (id: number) => {
    try {
      await clientesService.remove(id);
      toast.success("Cliente eliminado.");
      setState((s) => ({ ...s, data: s.data.filter((c) => c.id !== id) }));
    } catch (e) {
      const msg = e instanceof HttpError ? e.message : "No se pudo eliminar el cliente.";
      toast.error(msg);
      throw e;
    }
  }, []);

  const byId = useMemo(() => {
    const map = new Map<number, Cliente>();
    for (const c of state.data) map.set(c.id, c);
    return map;
  }, [state.data]);

  return { ...state, load, create, update, remove, byId };
}
