/* archivo de página registerpage */
import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { UserPlus, ArrowLeft } from "lucide-react";
import styles from "./RegisterPage.module.css";
import { Button } from "../../components/ui/Button";
import { FormField } from "../../components/ui/FormField";
import { isEmail } from "../../utils/validators";
import type { FormEvent } from "react";

type Form = {
  nombres: string;
  apellidos: string;
  cedula: string;
  fechaNacimiento: string;
  email: string;
  password: string;
};

type Errors = Partial<Record<keyof Form, string>>;

const USERS_KEY = "alumipro_users";


export function RegisterPage() {
  const navigate = useNavigate();
  const [values, setValues] = useState<Form>({
    nombres: "",
    apellidos: "",
    cedula: "",
    fechaNacimiento: "",
    email: "",
    password: ""
  });
  const [errors, setErrors] = useState<Errors>({});
  const [isLoading, setIsLoading] = useState(false);

  const fullName = useMemo(() => `${values.nombres} ${values.apellidos}`.trim(), [values.nombres, values.apellidos]);

  const validate = (v: Form) => {
    const next: Errors = {};
    if (!v.nombres.trim()) next.nombres = "Los nombres son obligatorios.";
    if (!v.apellidos.trim()) next.apellidos = "Los apellidos son obligatorios.";
    if (!/^[0-9]{6,}$/.test(v.cedula.trim())) next.cedula = "La cédula debe ser numérica (mínimo 6 dígitos).";
    if (!v.fechaNacimiento) next.fechaNacimiento = "La fecha de nacimiento es obligatoria.";
    if (!v.email.trim()) next.email = "El email es obligatorio.";
    else if (!isEmail(v.email)) next.email = "Formato de email inválido.";
    if (v.password.trim().length < 6) next.password = "La contraseña debe tener mínimo 6 caracteres.";
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const onSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!validate(values)) return;

    setIsLoading(true);
    try {
      const existing = (() => {
        try {
          const raw = localStorage.getItem(USERS_KEY);
          if (!raw) return [];
          const parsed = JSON.parse(raw) as unknown;
          return Array.isArray(parsed) ? parsed : [];
        } catch {
          return [];
        }
      })();

      const user = {
        nombres: values.nombres.trim(),
        apellidos: values.apellidos.trim(),
        cedula: values.cedula.trim(),
        fechaNacimiento: values.fechaNacimiento,
        email: values.email.trim(),
        createdAt: new Date().toISOString()
      };

      localStorage.setItem(USERS_KEY, JSON.stringify([user, ...existing].slice(0, 50)));
      toast.success(`Registro creado: ${fullName || "usuario"}.`);
      navigate("/auth/login");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.head}>
          <div>
            <div className={styles.title}>Registro de usuario</div>
            <div className={styles.subtitle}>Formulario requerido por guía (modo local).</div>
          </div>
        </div>

        <form className={styles.form} onSubmit={onSubmit}>
          <div className={styles.grid}>
            <FormField label="Nombres" required error={errors.nombres}>
              <input
                value={values.nombres}
                onChange={(e) => setValues((s) => ({ ...s, nombres: e.target.value }))}
                placeholder="Ej: Juan David"
                aria-label="Nombres"
                disabled={isLoading}
              />
            </FormField>

            <FormField label="Apellidos" required error={errors.apellidos}>
              <input
                value={values.apellidos}
                onChange={(e) => setValues((s) => ({ ...s, apellidos: e.target.value }))}
                placeholder="Ej: Sánchez Gaviria"
                aria-label="Apellidos"
                disabled={isLoading}
              />
            </FormField>

            <FormField label="Cédula" required error={errors.cedula} hint="Solo números">
              <input
                value={values.cedula}
                onChange={(e) => setValues((s) => ({ ...s, cedula: e.target.value }))}
                placeholder="Ej: 1020304050"
                aria-label="Cédula"
                disabled={isLoading}
                inputMode="numeric"
              />
            </FormField>

            <FormField label="Fecha de nacimiento" required error={errors.fechaNacimiento}>
              <input
                value={values.fechaNacimiento}
                onChange={(e) => setValues((s) => ({ ...s, fechaNacimiento: e.target.value }))}
                aria-label="Fecha de nacimiento"
                disabled={isLoading}
                type="date"
              />
            </FormField>

            <FormField label="Email" required error={errors.email}>
              <input
                value={values.email}
                onChange={(e) => setValues((s) => ({ ...s, email: e.target.value }))}
                placeholder="usuario@alumipro.com"
                aria-label="Email"
                disabled={isLoading}
              />
            </FormField>

            <FormField label="Contraseña" required error={errors.password} hint="Mínimo 6 caracteres">
              <input
                value={values.password}
                onChange={(e) => setValues((s) => ({ ...s, password: e.target.value }))}
                placeholder="••••••••"
                aria-label="Contraseña"
                disabled={isLoading}
                type="password"
              />
            </FormField>
          </div>

          <div className={styles.actions}>
            <Button variant="secondary" type="button" leftIcon={<ArrowLeft size={16} />} onClick={() => navigate("/auth/login")} disabled={isLoading}>
              Volver
            </Button>
            <Button variant="primary" type="submit" isLoading={isLoading} leftIcon={<UserPlus size={16} />}>
              Crear registro
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
