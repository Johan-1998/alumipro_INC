/* archivo de código producto */
export type Producto = {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  descripcion?: string | null;
};

export type ProductoCreate = Omit<Producto, "id">;
