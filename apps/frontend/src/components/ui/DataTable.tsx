/* archivo de componente datatable */
import { useMemo, useState } from "react";
import type { ReactNode } from "react";
import { ArrowDown, ArrowUp } from "lucide-react";
import styles from "./DataTable.module.css";
import { EmptyState } from "./EmptyState";
import { SkeletonTable } from "./SkeletonTable";

export type Column<T> = {
  key: string;
  header: string;
  render: (row: T) => ReactNode;
  sortValue?: (row: T) => string | number;
  hideOnMobile?: boolean;
};

type SortDir = "asc" | "desc";

type Props<T> = {
  rows: T[];
  columns: Column<T>[];
  isLoading?: boolean;
  emptyTitle?: string;
  emptyDescription?: string;
};

export function DataTable<T>({ rows, columns, isLoading = false, emptyTitle = "Sin datos", emptyDescription }: Props<T>) {
  const [sortKey, setSortKey] = useState<string | null>(null);
  const [sortDir, setSortDir] = useState<SortDir>("asc");

  const sorted = useMemo(() => {
    if (!sortKey) return rows;
    const col = columns.find((c) => c.key === sortKey);
    if (!col?.sortValue) return rows;

    const copy = [...rows];
    copy.sort((a, b) => {
      const av = col.sortValue!(a);
      const bv = col.sortValue!(b);
      if (av < bv) return sortDir === "asc" ? -1 : 1;
      if (av > bv) return sortDir === "asc" ? 1 : -1;
      return 0;
    });
    return copy;
  }, [rows, columns, sortKey, sortDir]);

  const onToggleSort = (key: string) => {
    if (sortKey !== key) {
      setSortKey(key);
      setSortDir("asc");
      return;
    }
    setSortDir((d) => (d === "asc" ? "desc" : "asc"));
  };

  if (isLoading) return <SkeletonTable />;

  if (!sorted.length) {
    return <EmptyState title={emptyTitle} description={emptyDescription} />;
  }

  return (
    <div className={styles.wrap}>
      {}
      <div className={styles.desktopTable}>
        <table className={styles.table}>
          <thead>
            <tr>
              {columns.map((c) => (
                <th key={c.key}>
                  <button
                    className={`${styles.thBtn} ${c.sortValue ? styles.sortable : ""}`}
                    onClick={() => (c.sortValue ? onToggleSort(c.key) : undefined)}
                    aria-label={c.sortValue ? `Ordenar por ${c.header}` : c.header}
                    type="button"
                  >
                    <span>{c.header}</span>
                    {sortKey === c.key ? (
                      <span className={styles.sortIcon} aria-hidden="true">
                        {sortDir === "asc" ? <ArrowUp size={14} /> : <ArrowDown size={14} />}
                      </span>
                    ) : null}
                  </button>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {sorted.map((r, idx) => (
              <tr key={idx}>
                {columns.map((c) => (
                  <td key={c.key}>{c.render(r)}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {}
      <div className={styles.mobileCards}>
        {sorted.map((r, idx) => (
          <div key={idx} className={styles.card} role="group">
            {columns
              .filter((c) => !c.hideOnMobile)
              .map((c) => (
                <div key={c.key} className={styles.cardRow}>
                  <div className={styles.cardLabel}>{c.header}</div>
                  <div className={styles.cardValue}>{c.render(r)}</div>
                </div>
              ))}
          </div>
        ))}
      </div>
    </div>
  );
}
