"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.formatCOP = formatCOP;
exports.safeNumber = safeNumber;
/* archivo de código format */
function formatCOP(value) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        maximumFractionDigits: 0
    }).format(value);
}
function safeNumber(value) {
    const n = Number(value);
    return Number.isFinite(n) ? n : 0;
}
