/* archivo de tipos venta */
export type VentaItemCreate = {
  productoId: number;
  cantidad: number;
};

export type VentaCreate = {
  clienteId: number;
  items: VentaItemCreate[];
};

export type VentaCreateResponse = {
  ventaId: number;
  total: number;
};

export type VentaSummary = {
  id: number;
  fecha: string;
  clienteId: number;
  clienteNombre: string;
  vendedorId: number | null;
  vendedorNombre: string;
  total: number;
};

export type VentaDetalleItem = {
  productoId: number;
  productoNombre: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
};

export type VentaDetalle = {
  id: number;
  fecha: string;
  clienteId: number;
  clienteNombre: string;
  vendedorId: number | null;
  vendedorNombre: string;
  total: number;
  items: VentaDetalleItem[];
};
