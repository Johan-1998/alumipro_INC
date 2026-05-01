/* archivo de componente skeletontable */
import styles from "./SkeletonTable.module.css";

type Props = {
  rows?: number;
};

export function SkeletonTable({ rows = 6 }: Props) {
  return (
    <div className={styles.wrap} aria-label="Cargando tabla">
      {Array.from({ length: rows }).map((_, idx) => (
        <div key={idx} className={styles.row}>
          <div className={styles.cell} />
          <div className={styles.cell} />
          <div className={styles.cell} />
          <div className={styles.cell} />
        </div>
      ))}
    </div>
  );
}
