/* archivo de página login */
import { useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import toast from "react-hot-toast";
import { LockKeyhole } from "lucide-react";
import styles from "./LoginPage.module.css";
import { Button } from "../../components/ui/Button";
import { FormField } from "../../components/ui/FormField";
import { isEmail } from "../../utils/validators";
import type { FormEvent } from "react";
import { useAuth } from "../../app/auth";

type Form = { correo: string; password: string };
type Errors = Partial<Record<keyof Form, string>>;

export function LoginPage() {
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const redirect = useMemo(() => params.get("redirect") ?? "/", [params]);
  const { login } = useAuth();

  const [values, setValues] = useState<Form>({ correo: "", password: "" });
  const [errors, setErrors] = useState<Errors>({});
  const [isLoading, setIsLoading] = useState(false);

  const validate = (v: Form) => {
    const next: Errors = {};
    if (!v.correo.trim()) next.correo = "El correo es obligatorio.";
    else if (!isEmail(v.correo)) next.correo = "Formato de correo inválido.";
    if (!v.password.trim()) next.password = "La contraseña es obligatoria.";
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!validate(values)) return;

    setIsLoading(true);
    try {
      const u = await login({ correo: values.correo.trim(), password: values.password });
      toast.success(`Bienvenido, ${u.nombre}.`);
      navigate(redirect, { replace: true });
    } catch (err) {
      const msg = err instanceof Error ? err.message : "No se pudo iniciar sesión.";
      toast.error(msg);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      {/* ── Panel izquierdo: imagen de fondo de trabajadores ── */}
      <aside className={styles.imageSide} aria-hidden="true">
        <div className={styles.imageContent}>
          <div className={styles.imageBadge}>
            <span className={styles.imageBadgeDot} />
            <span className={styles.imageBadgeText}>Sistema activo</span>
          </div>
          <h1 className={styles.imageHeadline}>
            Precisión
            <span className={styles.imageHeadlineAccent}>Industrial</span>
          </h1>
          <p className={styles.imageDesc}>
            Fabricación y comercialización de aluminio estructural, ventanería
            arquitectónica y fachadas de alto desempeño para Colombia.
          </p>
          <div className={styles.imageStats}>
            <div className={styles.imageStat}>
              <div className={styles.imageStatValue}>+12</div>
              <div className={styles.imageStatLabel}>Años</div>
            </div>
            <div className={styles.imageStat}>
              <div className={styles.imageStatValue}>+380</div>
              <div className={styles.imageStatLabel}>Proyectos</div>
            </div>
            <div className={styles.imageStat}>
              <div className={styles.imageStatValue}>ISO</div>
              <div className={styles.imageStatLabel}>Certificado</div>
            </div>
          </div>
        </div>
      </aside>

      {/* ── Panel derecho: formulario ── */}
      <main className={styles.formSide}>
        <div className={styles.formInner}>
          <div className={styles.brand}>
            <div className={styles.logoMark} aria-hidden="true">
              {/* Logo ALUMIPRO SVG */}
              <svg className={styles.logoGlyph} viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="3" y="18" width="22" height="3" rx="1.5" fill="#E05C1A"/>
                <polygon points="14,4 3,18 7,18 14,8 21,18 25,18" fill="#E8ECF2"/>
                <rect x="11" y="12" width="6" height="6" rx="1" fill="#E05C1A" opacity="0.7"/>
              </svg>
            </div>
            <div className={styles.brandText}>
              <div className={styles.brandName}>ALUMIPRO</div>
              <div className={styles.brandSub}>Gestión interna · Colombia</div>
            </div>
          </div>

          <div className={styles.formHeader}>
            <div className={styles.divider} />
            <h2 className={styles.formTitle}>Ingresa al sistema</h2>
            <p className={styles.formSub}>Acceso restringido a personal autorizado</p>
          </div>

          <form className={styles.form} onSubmit={onSubmit}>
            <FormField label="Correo electrónico" required error={errors.correo}>
              <input
                value={values.correo}
                onChange={(e) => setValues((s) => ({ ...s, correo: e.target.value }))}
                placeholder="usuario@alumipro.com"
                aria-label="Correo"
                disabled={isLoading}
                autoComplete="email"
              />
            </FormField>

            <FormField label="Contraseña" required error={errors.password}>
              <input
                value={values.password}
                onChange={(e) => setValues((s) => ({ ...s, password: e.target.value }))}
                placeholder="••••••••"
                aria-label="Contraseña"
                type="password"
                disabled={isLoading}
                autoComplete="current-password"
              />
            </FormField>

            <div className={styles.actions}>
              <Button variant="primary" type="submit" isLoading={isLoading} leftIcon={<LockKeyhole size={16} />}>
                Ingresar al sistema
              </Button>
            </div>

            <div className={styles.helper}>
              Credenciales demo: <strong>admin@alumipro.com</strong> / admin123 &nbsp;·&nbsp; <strong>vendedor@alumipro.com</strong> / vendedor123
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}
