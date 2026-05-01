/* archivo de componente searchselect */
import { useEffect, useMemo, useRef, useState } from "react";
import { ChevronDown, Search } from "lucide-react";
import styles from "./SearchSelect.module.css";

export type SearchSelectItem = {
  id: number;
  label: string;
  meta?: string;
};

type Props = {
  label: string;
  placeholder: string;
  items: SearchSelectItem[];
  selectedId: number | null;
  onSelect: (id: number) => void;
  disabled?: boolean;
  error?: boolean;
};

export function SearchSelect({ label, placeholder, items, selectedId, onSelect, disabled = false, error = false }: Props) {
  const [query, setQuery] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const wrapRef = useRef<HTMLDivElement | null>(null);

  const selected = useMemo(() => items.find((i) => i.id === selectedId) ?? null, [items, selectedId]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items.slice(0, 12);
    return items
      .filter((i) => i.label.toLowerCase().includes(q) || (i.meta ?? "").toLowerCase().includes(q))
      .slice(0, 12);
  }, [items, query]);

  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      const t = e.target as Node;
      if (!wrapRef.current?.contains(t)) setIsOpen(false);
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, []);

  return (
    <div ref={wrapRef} className={`${styles.wrap} ${disabled ? styles.disabled : ""}`} aria-label={label}>
      <button
        type="button"
        className={`${styles.trigger} ${error ? styles.error : ""}`}
        onClick={() => !disabled && setIsOpen((v) => !v)}
        aria-haspopup="listbox"
        aria-expanded={isOpen}
        disabled={disabled}
      >
        <span className={styles.triggerLeft}>
          <Search size={16} aria-hidden="true" />
          <span className={styles.triggerText}>
            {selected ? selected.label : <span className={styles.placeholder}>{placeholder}</span>}
          </span>
        </span>
        <ChevronDown size={16} aria-hidden="true" className={`${styles.chev} ${isOpen ? styles.chevUp : ""}`} />
      </button>

      {isOpen ? (
        <div className={styles.popover} role="listbox" aria-label={`${label} opciones`}>
          <input
            className={styles.search}
            placeholder="Buscar…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            autoFocus
            aria-label={`Buscar en ${label}`}
            disabled={disabled}
          />
          <div className={styles.list}>
            {filtered.length ? (
              filtered.map((i) => (
                <button
                  key={i.id}
                  type="button"
                  className={`${styles.item} ${selectedId === i.id ? styles.itemActive : ""}`}
                  onClick={() => {
                    onSelect(i.id);
                    setIsOpen(false);
                    setQuery("");
                  }}
                >
                  <div className={styles.itemLabel}>{i.label}</div>
                  {i.meta ? <div className={styles.itemMeta}>{i.meta}</div> : null}
                </button>
              ))
            ) : (
              <div className={styles.nores} role="status">
                No hay resultados
              </div>
            )}
          </div>
        </div>
      ) : null}
    </div>
  );
}
