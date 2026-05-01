/* archivo de componente projectthumb */
import styles from "./ProjectThumb.module.css";

type Kind = "Fachadas" | "Ventanería" | "Estructuras" | "Residencial" | "Industrial";

export function ProjectThumb({ kind }: { kind: Kind }) {
  return (
    <div className={styles.wrap} aria-hidden="true" data-kind={kind}>
      {kind === "Fachadas" ? <Fachada /> : null}
      {kind === "Ventanería" ? <Ventana /> : null}
      {kind === "Estructuras" ? <Estructura /> : null}
      {kind === "Residencial" ? <Residencial /> : null}
      {kind === "Industrial" ? <Industrial /> : null}
    </div>
  );
}

function Fachada() {
  return (
    <svg viewBox="0 0 400 250" className={styles.svg}>
      <rect x="0" y="0" width="400" height="250" className={styles.bg} />
      <rect x="52" y="34" width="296" height="182" className={styles.frame} />
      <g className={styles.grid}>
        {Array.from({ length: 6 }).map((_, r) => (
          <g key={r}>
            {Array.from({ length: 10 }).map((_, c) => (
              <rect key={c} x={70 + c * 26} y={52 + r * 26} width="18" height="18" rx="3" />
            ))}
          </g>
        ))}
      </g>
      <rect x="40" y="210" width="320" height="10" className={styles.base} />
    </svg>
  );
}

function Ventana() {
  return (
    <svg viewBox="0 0 400 250" className={styles.svg}>
      <rect x="0" y="0" width="400" height="250" className={styles.bg} />
      <rect x="90" y="40" width="220" height="170" className={styles.frame} />
      <rect x="100" y="50" width="100" height="150" className={styles.glass} />
      <rect x="210" y="50" width="90" height="150" className={styles.glass} />
      <rect x="200" y="40" width="10" height="170" className={styles.mullion} />
      <rect x="90" y="120" width="220" height="10" className={styles.mullion} />
      <circle cx="285" cy="125" r="4" className={styles.handle} />
    </svg>
  );
}

function Estructura() {
  return (
    <svg viewBox="0 0 400 250" className={styles.svg}>
      <rect x="0" y="0" width="400" height="250" className={styles.bg} />
      <g className={styles.steel}>
        <rect x="70" y="40" width="18" height="170" rx="6" />
        <rect x="312" y="40" width="18" height="170" rx="6" />
        <rect x="70" y="60" width="260" height="14" rx="6" />
        <rect x="70" y="120" width="260" height="14" rx="6" />
        <rect x="70" y="180" width="260" height="14" rx="6" />
      </g>
      <g className={styles.brace}>
        <path d="M88 74 L312 180" />
        <path d="M88 180 L312 74" />
      </g>
    </svg>
  );
}

function Residencial() {
  return (
    <svg viewBox="0 0 400 250" className={styles.svg}>
      <rect x="0" y="0" width="400" height="250" className={styles.bg} />
      <path d="M110 120 L200 60 L290 120" className={styles.roof} />
      <rect x="120" y="120" width="160" height="90" className={styles.wall} />
      <rect x="145" y="140" width="60" height="55" className={styles.glass} />
      <rect x="220" y="140" width="45" height="70" className={styles.door} />
      <rect x="175" y="140" width="6" height="55" className={styles.mullion} />
    </svg>
  );
}

function Industrial() {
  return (
    <svg viewBox="0 0 400 250" className={styles.svg}>
      <rect x="0" y="0" width="400" height="250" className={styles.bg} />
      <rect x="70" y="90" width="260" height="120" className={styles.wall} />
      <rect x="90" y="110" width="220" height="40" className={styles.bay} />
      <rect x="90" y="160" width="60" height="50" className={styles.glass} />
      <rect x="160" y="160" width="60" height="50" className={styles.glass} />
      <rect x="230" y="160" width="80" height="50" className={styles.glass} />
      <rect x="310" y="60" width="16" height="60" className={styles.stack} />
      <path d="M318 55 C335 50 350 55 358 66" className={styles.smoke} />
    </svg>
  );
}
