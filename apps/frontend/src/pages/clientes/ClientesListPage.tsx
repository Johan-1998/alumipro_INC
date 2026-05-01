/* archivo de página clienteslistpage */
import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Pencil, Trash2, Plus } from "lucide-react";
import styles from "./ClientesListPage.module.css";
import { useClientes } from "../../hooks/useClientes";
import { DataTable } from "../../components/ui/DataTable";
import type { Column } from "../../components/ui/DataTable";
import { Button } from "../../components/ui/Button";
import { ConfirmDialog } from "../../components/ui/ConfirmDialog";
import type { Cliente } from "../../types/cliente";
import { useAuth } from "../../app/auth";

export function ClientesListPage() {
  const navigate = useNavigate();
  const { isAdmin } = useAuth();
  const { data, isLoading, error, remove } = useClientes();

  const [query, setQuery] = useState("");
  const [confirmId, setConfirmId] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return data;
    return data.filter((c) => c.nombre.toLowerCase().includes(q));
  }, [data, query]);

  const columns: Column<Cliente>[] = [
    { key: "id", header: "ID", render: (c) => c.id, sortValue: (c) => c.id, hideOnMobile: true },
    { key: "nombre", header: "Nombre", render: (c) => <span className={styles.strong}>{c.nombre}</span>, sortValue: (c) => c.nombre },
    { key: "telefono", header: "Teléfono", render: (c) => c.telefono ?? "—", sortValue: (c) => c.telefono ?? "" },
    { key: "email", header: "Email", render: (c) => c.email ?? "—", sortValue: (c) => c.email ?? "" },
    {
      key: "acciones",
      header: "Acciones",
      render: (c) => (
        <div className={styles.actions}>
          <Button
            variant="secondary"
            size="sm"
            className={styles.iconBtn}
            onClick={() => navigate(`/clientes/${c.id}/editar`)}
            aria-label={`Editar cliente ${c.nombre}`}
            type="button"
          >
            <Pencil size={16} />
          </Button>
          {isAdmin ? (
            <Button
              variant="secondary"
              size="sm"
              className={styles.iconBtnDanger}
              onClick={() => setConfirmId(c.id)}
              aria-label={`Eliminar cliente ${c.nombre}`}
              type="button"
            >
              <Trash2 size={16} />
            </Button>
          ) : null}
        </div>
      ),
      hideOnMobile: false
    }
  ];

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
          <div className={styles.title}>Clientes</div>
          <div className={styles.subtitle}>Gestión de clientes registrados</div>
        </div>

        <Button
          variant="primary"
          leftIcon={<Plus size={16} />}
          onClick={() => navigate("/clientes/nuevo")}
        >
          Nuevo cliente
        </Button>
      </div>

      <div className={styles.toolbar}>
        <input
          className={styles.search}
          placeholder="Buscar por nombre…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          aria-label="Buscar cliente por nombre"
        />
        {error ? <div className={styles.error} role="alert">{error}</div> : null}
      </div>

      <DataTable
        rows={filtered}
        columns={columns}
        isLoading={isLoading}
        emptyTitle="No hay clientes"
        emptyDescription="Crea un nuevo cliente para comenzar."
      />

      {isAdmin ? (
        <ConfirmDialog
          isOpen={confirmId != null}
          title="Eliminar cliente"
          message="Esta acción no se puede deshacer. ¿Deseas eliminar el cliente?"
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
