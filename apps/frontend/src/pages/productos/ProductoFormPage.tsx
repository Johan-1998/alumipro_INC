/* archivo de página productoformpage */
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Save } from "lucide-react";
import styles from "./ProductoFormPage.module.css";
import { FormField } from "../../components/ui/FormField";
import { Button } from "../../components/ui/Button";
import { productosService } from "../../services/productos.service";
import { useProductos } from "../../hooks/useProductos";
import { HttpError } from "../../services/http";
import { safeNumber } from "../../utils/format";
import type { ProductoCreate } from "../../types/producto";
import type { FormEvent } from "react";

type Mode = "create" | "edit";

type Props = {
  mode: Mode;
};

type FormErrors = Partial<Record<keyof ProductoCreate, string>>;

export function ProductoFormPage({ mode }: Props) {
  const navigate = useNavigate();
  const params = useParams();
  const { create, update } = useProductos();

  const id = useMemo(() => (params.id ? Number(params.id) : null), [params.id]);

  const [values, setValues] = useState<ProductoCreate>({
    nombre: "",
    precio: 0,
    stock: 0,
    descripcion: ""
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [isLoading, setIsLoading] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);

  useEffect(() => {
    const run = async () => {
      if (mode !== "edit" || !id) return;
      setIsLoading(true);
      setLoadError(null);
      try {
        const p = await productosService.get(id);
        setValues({
          nombre: p.nombre ?? "",
          precio: Number(p.precio ?? 0),
          stock: Number(p.stock ?? 0),
          descripcion: p.descripcion ?? ""
        });
      } catch (e) {
        const msg = e instanceof HttpError ? e.message : "No se pudo cargar el producto.";
        setLoadError(msg);
      } finally {
        setIsLoading(false);
      }
    };
    void run();
  }, [mode, id]);

  const validate = (v: ProductoCreate) => {
    const next: FormErrors = {};
    if (!v.nombre.trim()) next.nombre = "El nombre es obligatorio.";
    if (!(Number(v.precio) > 0)) next.precio = "El precio debe ser mayor a 0.";
    if (!(Number(v.stock) >= 0)) next.stock = "El stock debe ser 0 o mayor.";
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const payload: ProductoCreate = {
      nombre: values.nombre.trim(),
      precio: Number(values.precio),
      stock: Number(values.stock),
      descripcion: values.descripcion?.trim() ?? ""
    };

    if (!validate(payload)) return;

    setIsLoading(true);
    try {
      if (mode === "create") {
        await create(payload);
      } else if (mode === "edit" && id) {
        await update(id, payload);
      }
      navigate("/productos");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <Button variant="secondary" leftIcon={<ArrowLeft size={16} />} onClick={() => navigate("/productos")}>
          Volver
        </Button>

        <div className={styles.headText}>
          <div className={styles.title}>{mode === "create" ? "Nuevo producto" : "Editar producto"}</div>
          <div className={styles.subtitle}>Define nombre, precio y stock del producto.</div>
        </div>
      </div>

      {loadError ? <div className={styles.loadError} role="alert">{loadError}</div> : null}

      <form className={styles.form} onSubmit={onSubmit}>
        <div className={styles.grid}>
          <FormField label="Nombre" required error={errors.nombre}>
            <input
              value={values.nombre}
              onChange={(e) => setValues((s) => ({ ...s, nombre: e.target.value }))}
              placeholder="Ej: Perfil aluminio 2m"
              aria-label="Nombre"
              disabled={isLoading}
            />
          </FormField>

          <FormField label="Precio (COP)" required error={errors.precio}>
            <input
              value={String(values.precio)}
              onChange={(e) => setValues((s) => ({ ...s, precio: safeNumber(e.target.value) }))}
              placeholder="Ej: 98000"
              aria-label="Precio"
              disabled={isLoading}
              inputMode="numeric"
            />
          </FormField>

          <FormField label="Stock" required error={errors.stock} hint="0 = agotado">
            <input
              value={String(values.stock)}
              onChange={(e) => setValues((s) => ({ ...s, stock: safeNumber(e.target.value) }))}
              placeholder="Ej: 10"
              aria-label="Stock"
              disabled={isLoading}
              inputMode="numeric"
            />
          </FormField>

          <FormField label="Descripción" hint="Opcional">
            <textarea
              value={values.descripcion ?? ""}
              onChange={(e) => setValues((s) => ({ ...s, descripcion: e.target.value }))}
              placeholder="Detalles del producto (material, uso, etc.)"
              aria-label="Descripción"
              disabled={isLoading}
            />
          </FormField>
        </div>

        <div className={styles.footer}>
          <Button variant="secondary" type="button" onClick={() => navigate("/productos")} disabled={isLoading}>
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
