/* archivo de página productoslistpage */
import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Pencil, Trash2, Plus } from "lucide-react";
import styles from "./ProductosListPage.module.css";
import { useProductos } from "../../hooks/useProductos";
import { DataTable } from "../../components/ui/DataTable";
import type { Column } from "../../components/ui/DataTable";
import { Button } from "../../components/ui/Button";
import { ConfirmDialog } from "../../components/ui/ConfirmDialog";
import { StockBadge } from "../../components/ui/StockBadge";
import type { Producto } from "../../types/producto";
import { formatCOP } from "../../utils/format";
import { useAuth } from "../../app/auth";

export function ProductosListPage() {
  const navigate = useNavigate();
  const { isAdmin } = useAuth();
  const { data, isLoading, error, remove } = useProductos();

  const [query, setQuery] = useState("");
  const [confirmId, setConfirmId] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return data;
    return data.filter((p) => p.nombre.toLowerCase().includes(q));
  }, [data, query]);

  const columns: Column<Producto>[] = [
    { key: "id", header: "ID", render: (p) => p.id, sortValue: (p) => p.id, hideOnMobile: true },
    { key: "nombre", header: "Nombre", render: (p) => <span className={styles.strong}>{p.nombre}</span>, sortValue: (p) => p.nombre },
    { key: "precio", header: "Precio", render: (p) => formatCOP(p.precio), sortValue: (p) => p.precio },
    { key: "stock", header: "Stock", render: (p) => <StockBadge stock={p.stock} />, sortValue: (p) => p.stock },
    { key: "descripcion", header: "Descripción", render: (p) => <span className={styles.desc}>{p.descripcion ?? "—"}</span>, sortValue: (p) => p.descripcion ?? "", hideOnMobile: true },
  ];

  if (isAdmin) {
    columns.push({
      key: "acciones",
      header: "Acciones",
      render: (p) => (
        <div className={styles.actions}>
          <Button
            variant="secondary"
            size="sm"
            className={styles.iconBtn}
            onClick={() => navigate(`/productos/${p.id}/editar`)}
            aria-label={`Editar producto ${p.nombre}`}
            type="button"
          >
            <Pencil size={16} />
          </Button>
          <Button
            variant="secondary"
            size="sm"
            className={styles.iconBtnDanger}
            onClick={() => setConfirmId(p.id)}
            aria-label={`Eliminar producto ${p.nombre}`}
            type="button"
          >
            <Trash2 size={16} />
          </Button>
        </div>
      ),
      hideOnMobile: false
    });
  }

  const onConfirmDelete = async () => {
    if (confirmId == null) return;
    setIsDeleting(true);
    try {
      await remove(confirmId);
      setConfirmId(null);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <div className={styles.title}>Productos</div>
          <div className={styles.subtitle}>Inventario de perfiles y productos de aluminio</div>
        </div>

        {isAdmin ? (
          <Button
            variant="primary"
            leftIcon={<Plus size={16} />}
            onClick={() => navigate("/productos/nuevo")}
          >
            Nuevo producto
          </Button>
        ) : null}
      </div>

      <div className={styles.toolbar}>
        <input
          className={styles.search}
          placeholder="Buscar por nombre…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          aria-label="Buscar producto por nombre"
        />
        {error ? <div className={styles.error} role="alert">{error}</div> : null}
      </div>

      <DataTable
        rows={filtered}
        columns={columns}
        isLoading={isLoading}
        emptyTitle="No hay productos"
        emptyDescription="Crea un nuevo producto para comenzar."
      />

      {isAdmin ? (
        <ConfirmDialog
          isOpen={confirmId != null}
          title="Eliminar producto"
          message="Esta acción no se puede deshacer. ¿Deseas eliminar el producto?"
          confirmText="Eliminar"
          cancelText="Cancelar"
          isDanger
          isLoading={isDeleting}
          onClose={() => setConfirmId(null)}
          onConfirm={onConfirmDelete}
        />
      ) : null}
    </div>
  );
}
