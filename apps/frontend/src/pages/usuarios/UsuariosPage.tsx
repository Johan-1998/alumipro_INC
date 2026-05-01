/* archivo de página usuarios */
import { useEffect, useMemo, useState } from "react";
import styles from "./UsuariosPage.module.css";
import { useAuth } from "../../app/auth";
import { authService } from "../../services/auth.service";
import type { UsuarioUpsert, UsuarioView } from "../../types/usuario";
import { DataTable } from "../../components/ui/DataTable";
import type { Column } from "../../components/ui/DataTable";
import { Button } from "../../components/ui/Button";
import { Modal } from "../../components/ui/Modal";
import { FormField } from "../../components/ui/FormField";
import { ConfirmDialog } from "../../components/ui/ConfirmDialog";
import toast from "react-hot-toast";
import { Plus, Trash2 } from "lucide-react";

const emptyForm: UsuarioUpsert = { nombre: "", correo: "", password: "", rol: "VENDEDOR" };

export function UsuariosPage() {
  const { isAdmin } = useAuth();
  const [data, setData] = useState<UsuarioView[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<UsuarioUpsert>(emptyForm);
  const [saving, setSaving] = useState(false);

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [toDelete, setToDelete] = useState<UsuarioView | null>(null);
  const [deleting, setDeleting] = useState(false);

  const load = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await authService.listarUsuarios();
      setData(res);
    } catch (e) {
      const msg = e instanceof Error ? e.message : "No se pudieron cargar usuarios.";
      setError(msg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (!isAdmin) return;
    load();
  }, [isAdmin]);

  const columns = useMemo<Column<UsuarioView>[]>(() => {
    return [
      { key: "id", header: "ID", render: (u) => u.id, sortValue: (u) => u.id },
      { key: "nombre", header: "Nombre", render: (u) => u.nombre, sortValue: (u) => u.nombre },
      { key: "correo", header: "Correo", render: (u) => u.correo, sortValue: (u) => u.correo },
      { key: "rol", header: "Rol", render: (u) => u.rol, sortValue: (u) => u.rol },
      {
        key: "acciones",
        header: "Acciones",
        render: (u) => (
          <Button
            variant="danger"
            size="sm"
            leftIcon={<Trash2 size={16} />}
            type="button"
            onClick={() => {
              setToDelete(u);
              setConfirmOpen(true);
            }}
            aria-label={`Eliminar ${u.correo}`}
          >
            Eliminar
          </Button>
        ),
        hideOnMobile: true
      }
    ];
  }, []);

  if (!isAdmin) {
    return (
      <div className={styles.page}>
        <div className={styles.denied}>No tienes permisos para ver esta sección.</div>
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Usuarios</h1>
          <p className={styles.subtitle}>Administración de accesos (admin y vendedor).</p>
        </div>

        <div className={styles.headerActions}>
          <Button variant="secondary" type="button" onClick={load} disabled={isLoading}>Actualizar</Button>
          <Button
            variant="primary"
            type="button"
            leftIcon={<Plus size={18} />}
            onClick={() => {
              setForm(emptyForm);
              setOpen(true);
            }}
          >
            Nuevo usuario
          </Button>
        </div>
      </div>

      {error ? <div className={styles.error}>{error}</div> : null}

      <DataTable<UsuarioView>
        rows={data}
        columns={columns}
        isLoading={isLoading}
        emptyTitle="Sin usuarios"
        emptyDescription="Crea un usuario para comenzar."
      />

      <Modal
        isOpen={open}
        title="Crear usuario"
        onClose={() => setOpen(false)}
        footer={
          <div className={styles.modalFooter}>
            <Button variant="secondary" onClick={() => setOpen(false)} disabled={saving}>Cancelar</Button>
            <Button
              variant="primary"
              onClick={async () => {
                setSaving(true);
                try {
                  await authService.crearUsuario({
                    nombre: form.nombre,
                    correo: form.correo,
                    password: form.password,
                    rol: form.rol
                  });
                  toast.success("Usuario creado.");
                  setOpen(false);
                  await load();
                } catch (e) {
                  toast.error(e instanceof Error ? e.message : "No se pudo crear.");
                } finally {
                  setSaving(false);
                }
              }}
              disabled={!form.nombre.trim() || !form.correo.trim() || !form.password?.trim()}
              isLoading={saving}
            >
              Guardar
            </Button>
          </div>
        }
      >
        <div className={styles.formGrid}>
          <FormField label="Nombre" required>
            <input value={form.nombre} onChange={(e) => setForm((s) => ({ ...s, nombre: e.target.value }))} />
          </FormField>

          <FormField label="Correo" required>
            <input value={form.correo} onChange={(e) => setForm((s) => ({ ...s, correo: e.target.value }))} />
          </FormField>

          <FormField label="Contraseña" required>
            <input type="password" value={form.password ?? ""} onChange={(e) => setForm((s) => ({ ...s, password: e.target.value }))} />
          </FormField>

          <FormField label="Rol" required>
            <select value={form.rol} onChange={(e) => setForm((s) => ({ ...s, rol: e.target.value as UsuarioUpsert["rol"] }))}>
              <option value="VENDEDOR">VENDEDOR</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </FormField>
        </div>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        title="Confirmar eliminación"
        message={toDelete ? `¿Eliminar el usuario ${toDelete.correo}?` : ""}
        confirmText="Eliminar"
        cancelText="Cancelar"
        isDanger
        isLoading={deleting}
        onClose={() => {
          setConfirmOpen(false);
          setToDelete(null);
        }}
        onConfirm={async () => {
          if (!toDelete) return;
          setDeleting(true);
          try {
            await authService.eliminarUsuario(toDelete.id);
            toast.success("Usuario eliminado.");
            setConfirmOpen(false);
            setToDelete(null);
            await load();
          } catch (e) {
            toast.error(e instanceof Error ? e.message : "No se pudo eliminar.");
          } finally {
            setDeleting(false);
          }
        }}
      />
    </div>
  );
}
