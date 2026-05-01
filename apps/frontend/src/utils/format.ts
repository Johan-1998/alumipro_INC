/* archivo de código format */
export function formatCOP(value: number) {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0
  }).format(value);
}

export function safeNumber(value: string) {
  const n = Number(value);
  return Number.isFinite(n) ? n : 0;
}
