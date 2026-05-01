/* archivo de componente statcard */
import type { ReactNode } from "react";
import styles from "./StatCard.module.css";

type Props = {
  label: string;
  value: string;
  icon?: ReactNode;
  tone?: "primary" | "accent" | "neutral";
  helper?: string;
};

export function StatCard({ label, value, icon, tone = "neutral", helper }: Props) {
  return (
    <div className={`${styles.card} ${styles[tone]}`} role="group" aria-label={label}>
      <div className={styles.top}>
        <div className={styles.label}>{label}</div>
        {icon ? <div className={styles.icon} aria-hidden="true">{icon}</div> : null}
      </div>
      <div className={styles.value}>{value}</div>
      {helper ? <div className={styles.helper}>{helper}</div> : null}
    </div>
  );
}
