/* archivo de componente formfield */
import type { ReactNode } from "react";
import styles from "./FormField.module.css";

type Props = {
  label: string;
  required?: boolean;
  error?: string;
  hint?: string;
  children: ReactNode;
};

export function FormField({ label, required = false, error, hint, children }: Props) {
  return (
    <div className={styles.field}>
      <div className={styles.labelRow}>
        <label className={styles.label}>
          {label} {required ? <span className={styles.req} aria-hidden="true">*</span> : null}
        </label>
        {hint ? <span className={styles.hint}>{hint}</span> : null}
      </div>

      <div className={`${styles.control} ${error ? styles.controlError : ""}`}>{children}</div>

      {error ? <div className={styles.error} role="alert">{error}</div> : null}
    </div>
  );
}
