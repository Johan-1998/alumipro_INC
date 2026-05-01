/* archivo de componente topbar */
import { AlignJustify, ChevronRight } from "lucide-react";
import styles from "./Topbar.module.css";
import { Button } from "../ui/Button";

export type BreadcrumbItem = {
  label: string;
  href?: string;
};

type Props = {
  title: string;
  breadcrumbs: BreadcrumbItem[];
  onToggleSidebar: () => void;
};

export function Topbar({ title, breadcrumbs, onToggleSidebar }: Props) {
  return (
    <header className={styles.topbar} role="banner">
      <div className={styles.left}>
        <Button
          variant="ghost"
          size="md"
          className={styles.hamburger}
          onClick={onToggleSidebar}
          aria-label="Abrir/cerrar menú"
          leftIcon={<AlignJustify size={18} />}
          type="button"
        >
          Menú
        </Button>

        <div className={styles.breadcrumbs} aria-label="Breadcrumbs">
          {breadcrumbs.map((b, idx) => (
            <div key={`${b.label}-${idx}`} className={styles.crumb}>
              <span className={styles.crumbLabel}>{b.label}</span>
              {idx !== breadcrumbs.length - 1 ? (
                <span className={styles.sep} aria-hidden="true">
                  <ChevronRight size={14} />
                </span>
              ) : null}
            </div>
          ))}
        </div>
      </div>

      <div className={styles.right}>
        <div className={styles.title} aria-label="Título de sección">
          {title}
        </div>
      </div>
    </header>
  );
}
