"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_test_1 = __importDefault(require("node:test"));
const strict_1 = __importDefault(require("node:assert/strict"));
const format_1 = require("../../src/utils/format");
(0, node_test_1.default)("formatCOP devuelve moneda en pesos colombianos", () => {
    const value = (0, format_1.formatCOP)(98000);
    strict_1.default.match(value, /98\.000|98,000/);
    strict_1.default.match(value, /\$/);
});
(0, node_test_1.default)("safeNumber devuelve 0 cuando el valor no es numérico", () => {
    strict_1.default.equal((0, format_1.safeNumber)("abc"), 0);
    strict_1.default.equal((0, format_1.safeNumber)(""), 0);
});
(0, node_test_1.default)("safeNumber convierte correctamente números válidos", () => {
    strict_1.default.equal((0, format_1.safeNumber)("123"), 123);
    strict_1.default.equal((0, format_1.safeNumber)("45.5"), 45.5);
});
