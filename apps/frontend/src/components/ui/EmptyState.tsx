/* archivo de componente emptystate */
import type { ReactNode } from "react";
import styles from "./EmptyState.module.css";

type Props = {
  title: string;
  description?: string;
  icon?: ReactNode;
  action?: ReactNode;
};

export function EmptyState({ title, description, icon, action }: Props) {
  return (
    <div className={styles.empty} role="status">
      <div className={styles.icon} aria-hidden="true">
        {icon ?? (
          <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="7" width="40" height="28" rx="10" stroke="currentColor" strokeWidth="2"/>
            <path d="M13 18h20M13 24h14" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
            <path d="M16 39h14" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
          </svg>
        )}
      </div>
      <div className={styles.title}>{title}</div>
      {description ? <div className={styles.desc}>{description}</div> : null}
      {action ? <div className={styles.action}>{action}</div> : null}
    </div>
  );
}
