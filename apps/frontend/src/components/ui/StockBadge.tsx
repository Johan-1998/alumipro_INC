/* archivo de componente stockbadge */
import styles from "./StockBadge.module.css";

type Props = {
  stock: number;
};

export function StockBadge({ stock }: Props) {
  const tone = stock === 0 ? "zero" : stock < 10 ? "low" : "ok";
  const label = stock === 0 ? "Sin stock" : stock < 10 ? "Bajo" : "Disponible";

  return (
    <span className={`${styles.badge} ${styles[tone]}`} aria-label={`Stock: ${stock}`}>
      <span className={styles.dot} aria-hidden="true" />
      <span className={styles.text}>{label}: {stock}</span>
    </span>
  );
}
