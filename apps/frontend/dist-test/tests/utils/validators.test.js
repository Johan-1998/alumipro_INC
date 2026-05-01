"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_test_1 = __importDefault(require("node:test"));
const strict_1 = __importDefault(require("node:assert/strict"));
const validators_1 = require("../../src/utils/validators");
(0, node_test_1.default)("isEmail acepta vacío y correos válidos", () => {
    strict_1.default.equal((0, validators_1.isEmail)(""), true);
    strict_1.default.equal((0, validators_1.isEmail)("admin@alumipro.com"), true);
});
(0, node_test_1.default)("isEmail rechaza formatos inválidos", () => {
    strict_1.default.equal((0, validators_1.isEmail)("admin"), false);
    strict_1.default.equal((0, validators_1.isEmail)("admin@"), false);
    strict_1.default.equal((0, validators_1.isEmail)("admin@alumipro"), false);
});
(0, node_test_1.default)("isPhone acepta vacío y teléfonos de 7 o más dígitos", () => {
    strict_1.default.equal((0, validators_1.isPhone)(""), true);
    strict_1.default.equal((0, validators_1.isPhone)("3001234567"), true);
});
(0, node_test_1.default)("isPhone rechaza teléfonos cortos o con letras", () => {
    strict_1.default.equal((0, validators_1.isPhone)("12345"), false);
    strict_1.default.equal((0, validators_1.isPhone)("300ABC123"), false);
});
