import test from "node:test";
import assert from "node:assert/strict";
import { formatCOP, safeNumber } from "../../src/utils/format";

test("formatCOP devuelve moneda en pesos colombianos", () => {
  const value = formatCOP(98000);
  assert.match(value, /98\.000|98,000/);
  assert.match(value, /\$/);
});

test("safeNumber devuelve 0 cuando el valor no es numérico", () => {
  assert.equal(safeNumber("abc"), 0);
  assert.equal(safeNumber(""), 0);
});

test("safeNumber convierte correctamente números válidos", () => {
  assert.equal(safeNumber("123"), 123);
  assert.equal(safeNumber("45.5"), 45.5);
});
