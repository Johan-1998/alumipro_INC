/* archivo de código cliente */
export type Cliente = {
  id: number;
  nombre: string;
  telefono?: string | null;
  direccion?: string | null;
  email?: string | null;
};

export type ClienteCreate = Omit<Cliente, "id">;
