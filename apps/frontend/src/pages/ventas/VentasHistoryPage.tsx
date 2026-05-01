/* archivo de página ventashistory */
import { useMemo, useState } from "react";
import styles from "./VentasHistoryPage.module.css";
import { DataTable } from "../../components/ui/DataTable";
import type { Column } from "../../components/ui/DataTable";
import { Button } from "../../components/ui/Button";
import { Modal } from "../../components/ui/Modal";
import { useVentas } from "../../hooks/useVentas";
import { formatCOP } from "../../utils/format";
import type { VentaDetalle, VentaSummary } from "../../types/venta";
import { Eye } from "lucide-react";

function formatDate(iso: string) {
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return iso;
  return d.toLocaleDateString("es-CO");
}

export function VentasHistoryPage() {
  const ventas = useVentas();
  const [open, setOpen] = useState(false);
  const [detalle, setDetalle] = useState<VentaDetalle | null>(null);
  const [loadingDetalle, setLoadingDetalle] = useState(false);

  const columns = useMemo<Column<VentaSummary>[]>(() => {
    return [
      { key: "id", header: "ID", render: (v) => v.id, sortValue: (v) => v.id },
      { key: "fecha", header: "Fecha", render: (v) => formatDate(v.fecha), sortValue: (v) => v.fecha },
      { key: "cliente", header: "Cliente", render: (v) => v.clienteNombre, sortValue: (v) => v.clienteNombre },
      { key: "vendedor", header: "Vendedor", render: (v) => v.vendedorNombre || "—", sortValue: (v) => v.vendedorNombre || "" },
      { key: "total", header: "Total", render: (v) => formatCOP(v.total), sortValue: (v) => v.total },
      {
        key: "ver",
        header: "Detalle",
        render: (v) => (
          <Button
            variant="secondary"
            size="sm"
            leftIcon={<Eye size={16} />}
            type="button"
            onClick={async () => {
              setOpen(true);
              setDetalle(null);
              setLoadingDetalle(true);
              try {
                const d = await ventas.getDetalle(v.id);
                setDetalle(d);
              } finally {
                setLoadingDetalle(false);
              }
            }}
            aria-label={`Ver venta ${v.id}`}
          >
            Ver
          </Button>
        ),
        hideOnMobile: true
      }
    ];
  }, [ventas]);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Histórico de ventas</h1>
          <p className={styles.subtitle}>Registro consolidado de ventas con detalle por transacción.</p>
        </div>
        <Button variant="secondary" type="button" onClick={ventas.refresh} disabled={ventas.isLoading}>
          Actualizar
        </Button>
      </div>

      {ventas.error ? <div className={styles.error}>{ventas.error}</div> : null}

      <DataTable<VentaSummary>
        rows={ventas.data}
        columns={columns}
        isLoading={ventas.isLoading}
        emptyTitle="Sin ventas"
        emptyDescription="Aún no hay ventas registradas."
      />

      <Modal
        isOpen={open}
        title={detalle ? `Venta #${detalle.id}` : "Detalle de venta"}
        onClose={() => setOpen(false)}
        footer={<Button variant="secondary" onClick={() => setOpen(false)}>Cerrar</Button>}
      >
        {loadingDetalle ? (
          <div className={styles.loading}>Cargando detalle...</div>
        ) : detalle ? (
          <div className={styles.detail}>
            <div className={styles.detailGrid}>
              <div>
                <div className={styles.k}>Fecha</div>
                <div className={styles.v}>{formatDate(detalle.fecha)}</div>
              </div>
              <div>
                <div className={styles.k}>Cliente</div>
                <div className={styles.v}>{detalle.clienteNombre}</div>
              </div>
              <div>
                <div className={styles.k}>Vendedor</div>
                <div className={styles.v}>{detalle.vendedorNombre || "—"}</div>
              </div>
              <div>
                <div className={styles.k}>Total</div>
                <div className={styles.vStrong}>{formatCOP(detalle.total)}</div>
              </div>
            </div>

            <div className={styles.itemsTitle}>Ítems</div>
            <div className={styles.items}>
              {detalle.items.map((it) => (
                <div key={it.productoId} className={styles.itemRow}>
                  <div className={styles.itemName}>{it.productoNombre}</div>
                  <div className={styles.itemMeta}>
                    <span>{formatCOP(it.precioUnitario)}</span>
                    <span className={styles.dot}>•</span>
                    <span>Cant: {it.cantidad}</span>
                    <span className={styles.dot}>•</span>
                    <span className={styles.itemSub}>{formatCOP(it.subtotal)}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ) : (
          <div className={styles.loading}>No hay información.</div>
        )}
      </Modal>
    </div>
  );
}
