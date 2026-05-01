/* archivo de código validators */
export function isEmail(value: string) {
  const v = value.trim();
  if (!v) return true;

  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
}

export function isPhone(value: string) {
  const v = value.trim();
  if (!v) return true;
  return /^[0-9]{7,}$/.test(v);
}
