"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AUTH_EVENT = void 0;
exports.readAuth = readAuth;
exports.writeAuth = writeAuth;
exports.clearAuth = clearAuth;
const KEY = "alumipro:auth";
exports.AUTH_EVENT = "alumipro:auth";
function readAuth() {
    try {
        const raw = localStorage.getItem(KEY);
        if (!raw)
            return null;
        const parsed = JSON.parse(raw);
        if (!parsed?.token || !parsed?.user)
            return null;
        return parsed;
    }
    catch {
        return null;
    }
}
function writeAuth(state) {
    localStorage.setItem(KEY, JSON.stringify(state));
    window.dispatchEvent(new Event(exports.AUTH_EVENT));
}
function clearAuth() {
    localStorage.removeItem(KEY);
    window.dispatchEvent(new Event(exports.AUTH_EVENT));
}
