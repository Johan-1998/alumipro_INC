"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.isEmail = isEmail;
exports.isPhone = isPhone;
/* archivo de código validators */
function isEmail(value) {
    const v = value.trim();
    if (!v)
        return true;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
}
function isPhone(value) {
    const v = value.trim();
    if (!v)
        return true;
    return /^[0-9]{7,}$/.test(v);
}
