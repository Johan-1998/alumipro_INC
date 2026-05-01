/* archivo de página ventacreatepage */
import { useMemo, useState } from "react";
import { Plus, Trash2, CheckCircle2 } from "lucide-react";
import styles from "./VentaCreatePage.module.css";
import { useClientes } from "../../hooks/useClientes";
import { useProductos } from "../../hooks/useProductos";
import { useVentas } from "../../hooks/useVentas";
import { SearchSelect } from "../../components/ui/SearchSelect";
import { FormField } from "../../components/ui/FormField";
import { Button } from "../../components/ui/Button";
import { DataTable } from "../../components/ui/DataTable";
import type { Column } from "../../components/ui/DataTable";
import { Modal } from "../../components/ui/Modal";
import { formatCOP, safeNumber } from "../../utils/format";
import type { VentaCreateResponse } from "../../types/venta";
import type { Producto } from "../../types/producto";

type SaleItem = {
  productoId: number;
  nombre: string;
  precio: number;
  cantidad: number;
};

export function VentaCreatePage() {
  const clientes = useClientes();
  const productos = useProductos();
  const ventas = useVentas();

  const [clienteId, setClienteId] = useState<number | null>(null);
  const [productoId, setProductoId] = useState<number | null>(null);
  const [cantidad, setCantidad] = useState<number>(1);

  const [items, setItems] = useState<SaleItem[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [success, setSuccess] = useState<VentaCreateResponse | null>(null);

  const clienteItems = useMemo(
    () =>
      clientes.data.map((c) => ({
        id: c.id,
        label: c.nombre,
        meta: [c.telefono ?? "", c.email ?? ""].filter(Boolean).join(" • ")
      })),
    [clientes.data]
  );

  const productoItems = useMemo(
    () =>
      productos.data.map((p) => ({
        id: p.id,
        label: p.nombre,
        meta: `${formatCOP(p.precio)} • Stock: ${p.stock}`
      })),
    [productos.data]
  );

  const selectedProducto: Producto | null = useMemo(() => {
    if (!productoId) return null;
    return productos.byId.get(productoId) ?? null;
  }, [productoId, productos.byId]);

  const alreadyInCartQty = useMemo(() => {
    if (!productoId) return 0;
    const it = items.find((i) => i.productoId === productoId);
    return it ? it.cantidad : 0;
  }, [items, productoId]);

  const stockDisponible = useMemo(() => {
    if (!selectedProducto) return 0;
    const remain = selectedProducto.stock - alreadyInCartQty;
    return Math.max(0, remain);
  }, [selectedProducto, alreadyInCartQty]);

  const canAdd = useMemo(() => {
    if (!selectedProducto || !productoId) return false;
    if (!Number.isFinite(cantidad) || cantidad <= 0) return false;
    return cantidad <= stockDisponible;
  }, [selectedProducto, productoId, cantidad, stockDisponible]);

  const total = useMemo(() => {
    return items.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
  }, [items]);

  const addItem = () => {
    setError(null);
    if (!selectedProducto || !productoId) {
      setError("Selecciona un producto.");
      return;
    }
    if (!clienteId) {
      setError("Selecciona un cliente antes de agregar ítems.");
      return;
    }
    if (!Number.isFinite(cantidad) || cantidad <= 0) {
      setError("La cantidad debe ser mayor a 0.");
      return;
    }
    if (cantidad > stockDisponible) {
      setError(`Cantidad inválida. Stock disponible: ${stockDisponible}.`);
      return;
    }

    setItems((prev) => {
      const existing = prev.find((i) => i.productoId === productoId);
      if (!existing) {
        return [
          ...prev,
          { productoId: selectedProducto.id, nombre: selectedProducto.nombre, precio: selectedProducto.precio, cantidad }
        ];
      }
      return prev.map((i) =>
        i.productoId === productoId ? { ...i, cantidad: i.cantidad + cantidad } : i
      );
    });

    setCantidad(1);
  };

  const removeItem = (id: number) => {
    setItems((prev) => prev.filter((i) => i.productoId !== id));
  };

  const columns: Column<SaleItem>[] = [
    { key: "producto", header: "Producto", render: (it) => <span className={styles.strong}>{it.nombre}</span>, sortValue: (it) => it.nombre },
    { key: "precio", header: "Precio unitario", render: (it) => formatCOP(it.precio), sortValue: (it) => it.precio },
    { key: "cantidad", header: "Cantidad", render: (it) => it.cantidad, sortValue: (it) => it.cantidad },
    { key: "subtotal", header: "Subtotal", render: (it) => formatCOP(it.precio * it.cantidad), sortValue: (it) => it.precio * it.cantidad },
    {
      key: "quitar",
      header: "Quitar",
      render: (it) => (
        <Button
          variant="secondary"
          size="sm"
          className={styles.iconBtnDanger}
          onClick={() => removeItem(it.productoId)}
          aria-label={`Quitar ${it.nombre}`}
          type="button"
        >
          <Trash2 size={16} />
        </Button>
      )
    }
  ];

  const submit = async () => {
    setError(null);
    if (!clienteId) {
      setError("Selecciona un cliente.");
      return;
    }
    if (!items.length) {
      setError("Agrega al menos un ítem a la venta.");
      return;
    }

    setIsSubmitting(true);
    try {
      const res = await ventas.create({
        clienteId,
        items: items.map((i) => ({ productoId: i.productoId, cantidad: i.cantidad }))
      });
      setSuccess(res);
    } catch (e) {
      // El hook ya muestra toast, pero aquí mostramos un mensaje visible.
      const msg = e instanceof Error ? e.message : "Error al registrar la venta.";
      setError(msg);
    } finally {
      setIsSubmitting(false);
    }
  };

  const reset = () => {
    setClienteId(null);
    setProductoId(null);
    setCantidad(1);
    setItems([]);
    setError(null);
    setSuccess(null);
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.title}>Registrar venta</div>
        <div className={styles.subtitle}>Selecciona un cliente, agrega ítems y confirma el registro.</div>
      </div>

      <div className={styles.formCard}>
        <div className={styles.grid}>
          <FormField label="Cliente" required>
            <SearchSelect
              label="Cliente"
              placeholder="Selecciona un cliente"
              items={clienteItems}
              selectedId={clienteId}
              onSelect={setClienteId}
              disabled={clientes.isLoading}
              error={!clienteId && !!error}
            />
          </FormField>

          <FormField label="Producto" required>
            <SearchSelect
              label="Producto"
              placeholder="Selecciona un producto"
              items={productoItems}
              selectedId={productoId}
              onSelect={setProductoId}
              disabled={productos.isLoading}
              error={!productoId && !!error}
            />
          </FormField>

          <FormField
            label="Cantidad"
            required
            hint={selectedProducto ? `Stock disponible: ${stockDisponible}` : "Selecciona un producto"}
          >
            <input
              value={String(cantidad)}
              onChange={(e) => setCantidad(safeNumber(e.target.value))}
              aria-label="Cantidad"
              inputMode="numeric"
              disabled={!productoId}
            />
          </FormField>

          <div className={styles.addWrap}>
            <Button
              variant="primary"
              leftIcon={<Plus size={16} />}
              onClick={addItem}
              disabled={!canAdd}
              type="button"
            >
              Agregar ítem
            </Button>
            {!canAdd && productoId ? (
              <div className={styles.addHint}>Revisa cantidad vs stock.</div>
            ) : null}
          </div>
        </div>

        {error ? <div className={styles.errorBox} role="alert">{error}</div> : null}
      </div>

      <div className={styles.itemsSection}>
        <div className={styles.itemsHead}>
          <div className={styles.itemsTitle}>Ítems de la venta</div>
          <div className={styles.total}>
            Total: <span className={styles.totalValue}>{formatCOP(total)}</span>
          </div>
        </div>

        <DataTable
          rows={items}
          columns={columns}
          isLoading={false}
          emptyTitle="Sin ítems"
          emptyDescription="Agrega productos para armar la venta."
        />

        <div className={styles.actionsBar}>
          <Button variant="secondary" type="button" onClick={reset} disabled={isSubmitting}>
            Limpiar
          </Button>
          <Button variant="primary" type="button" onClick={submit} isLoading={isSubmitting}>
            Registrar venta
          </Button>
        </div>
      </div>

      <Modal
        isOpen={success != null}
        title="Venta registrada"
        onClose={reset}
        footer={
          <>
            <Button variant="secondary" onClick={reset}>Nueva venta</Button>
          </>
        }
      >
        {success ? (
          <div className={styles.success}>
            <div className={styles.successIcon} aria-hidden="true"><CheckCircle2 size={20} /></div>
            <div className={styles.successText}>
              <div><strong>ID venta:</strong> {success.ventaId}</div>
              <div><strong>Total:</strong> {formatCOP(success.total)}</div>
            </div>
          </div>
        ) : null}
      </Modal>
    </div>
  );
}
