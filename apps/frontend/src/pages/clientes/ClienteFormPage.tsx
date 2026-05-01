/* archivo de página clienteformpage */
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Save } from "lucide-react";
import styles from "./ClienteFormPage.module.css";
import { FormField } from "../../components/ui/FormField";
import { Button } from "../../components/ui/Button";
import { clientesService } from "../../services/clientes.service";
import { useClientes } from "../../hooks/useClientes";
import { HttpError } from "../../services/http";
import { isEmail, isPhone } from "../../utils/validators";
import type { ClienteCreate } from "../../types/cliente";
import type { FormEvent } from "react";

type Mode = "create" | "edit";

type Props = {
  mode: Mode;
};

type FormErrors = Partial<Record<keyof ClienteCreate, string>>;

export function ClienteFormPage({ mode }: Props) {
  const navigate = useNavigate();
  const params = useParams();
  const { create, update } = useClientes();

  const id = useMemo(() => (params.id ? Number(params.id) : null), [params.id]);

  const [values, setValues] = useState<ClienteCreate>({ nombre: "", email: "", telefono: "", direccion: "" });
  const [errors, setErrors] = useState<FormErrors>({});
  const [isLoading, setIsLoading] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);

  useEffect(() => {
    const run = async () => {
      if (mode !== "edit" || !id) return;
      setIsLoading(true);
      setLoadError(null);
      try {
        const c = await clientesService.get(id);
        setValues({
          nombre: c.nombre ?? "",
          email: c.email ?? "",
          telefono: c.telefono ?? "",
          direccion: c.direccion ?? ""
        });
      } catch (e) {
        const msg = e instanceof HttpError ? e.message : "No se pudo cargar el cliente.";
        setLoadError(msg);
      } finally {
        setIsLoading(false);
      }
    };
    void run();
  }, [mode, id]);

  const validate = (v: ClienteCreate) => {
    const next: FormErrors = {};
    if (!v.nombre.trim()) next.nombre = "El nombre es obligatorio.";
    if (!isEmail(v.email ?? "")) next.email = "El email no tiene un formato válido.";
    if (!isPhone(v.telefono ?? "")) next.telefono = "El teléfono debe ser numérico y mínimo 7 dígitos.";
    if ((v.direccion ?? "").length > 160) next.direccion = "La dirección supera el límite permitido.";
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const payload: ClienteCreate = {
      nombre: values.nombre.trim(),
      email: values.email?.trim() ?? "",
      telefono: values.telefono?.trim() ?? "",
      direccion: values.direccion?.trim() ?? ""
    };

    if (!validate(payload)) return;

    setIsLoading(true);
    try {
      if (mode === "create") {
        await create(payload);
      } else if (mode === "edit" && id) {
        await update(id, payload);
      }
      navigate("/clientes");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <Button variant="secondary" leftIcon={<ArrowLeft size={16} />} onClick={() => navigate("/clientes")}>
          Volver
        </Button>

        <div className={styles.headText}>
          <div className={styles.title}>{mode === "create" ? "Nuevo cliente" : "Editar cliente"}</div>
          <div className={styles.subtitle}>Completa la información y guarda los cambios.</div>
        </div>
      </div>

      {loadError ? <div className={styles.loadError} role="alert">{loadError}</div> : null}

      <form className={styles.form} onSubmit={onSubmit}>
        <div className={styles.grid}>
          <FormField label="Nombre" required error={errors.nombre}>
            <input
              value={values.nombre}
              onChange={(e) => setValues((s) => ({ ...s, nombre: e.target.value }))}
              placeholder="Ej: Constructora Andina"
              aria-label="Nombre"
              disabled={isLoading}
            />
          </FormField>

          <FormField label="Teléfono" error={errors.telefono} hint="Solo números">
            <input
              value={values.telefono ?? ""}
              onChange={(e) => setValues((s) => ({ ...s, telefono: e.target.value }))}
              placeholder="Ej: 3000000000"
              aria-label="Teléfono"
              disabled={isLoading}
              inputMode="numeric"
            />
          </FormField>

          <FormField label="Email" error={errors.email}>
            <input
              value={values.email ?? ""}
              onChange={(e) => setValues((s) => ({ ...s, email: e.target.value }))}
              placeholder="Ej: compras@empresa.com"
              aria-label="Email"
              disabled={isLoading}
            />
          </FormField>

          <FormField label="Dirección" error={errors.direccion}>
            <input
              value={values.direccion ?? ""}
              onChange={(e) => setValues((s) => ({ ...s, direccion: e.target.value }))}
              placeholder="Ej: Calle 10 #20-30"
              aria-label="Dirección"
              disabled={isLoading}
            />
          </FormField>
        </div>

        <div className={styles.footer}>
          <Button variant="secondary" type="button" onClick={() => navigate("/clientes")} disabled={isLoading}>
            Cancelar
          </Button>
          <Button variant="primary" type="submit" isLoading={isLoading} leftIcon={<Save size={16} />}>
            Guardar
          </Button>
        </div>
      </form>
    </div>
  );
}
