import test from "node:test";
import assert from "node:assert/strict";
import { isEmail, isPhone } from "../../src/utils/validators";

test("isEmail acepta vacío y correos válidos", () => {
  assert.equal(isEmail(""), true);
  assert.equal(isEmail("admin@alumipro.com"), true);
});

test("isEmail rechaza formatos inválidos", () => {
  assert.equal(isEmail("admin"), false);
  assert.equal(isEmail("admin@"), false);
  assert.equal(isEmail("admin@alumipro"), false);
});

test("isPhone acepta vacío y teléfonos de 7 o más dígitos", () => {
  assert.equal(isPhone(""), true);
  assert.equal(isPhone("3001234567"), true);
});

test("isPhone rechaza teléfonos cortos o con letras", () => {
  assert.equal(isPhone("12345"), false);
  assert.equal(isPhone("300ABC123"), false);
});
