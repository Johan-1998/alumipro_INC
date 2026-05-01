"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_test_1 = __importDefault(require("node:test"));
const strict_1 = __importDefault(require("node:assert/strict"));
const auth_storage_1 = require("../../src/services/auth.storage");
class MemoryStorage {
    store = new Map();
    getItem(key) {
        return this.store.has(key) ? this.store.get(key) : null;
    }
    setItem(key, value) {
        this.store.set(key, value);
    }
    removeItem(key) {
        this.store.delete(key);
    }
}
function createWindowMock() {
    const listeners = new Map();
    return {
        addEventListener(type, handler) {
            const list = listeners.get(type) ?? [];
            list.push(handler);
            listeners.set(type, list);
        },
        removeEventListener(type, handler) {
            const list = listeners.get(type) ?? [];
            listeners.set(type, list.filter((item) => item !== handler));
        },
        dispatchEvent(event) {
            const list = listeners.get(event.type) ?? [];
            for (const handler of list)
                handler(event);
            return true;
        }
    };
}
node_test_1.default.beforeEach(() => {
    Object.defineProperty(globalThis, "localStorage", {
        value: new MemoryStorage(),
        configurable: true,
        writable: true
    });
    Object.defineProperty(globalThis, "window", {
        value: createWindowMock(),
        configurable: true,
        writable: true
    });
});
(0, node_test_1.default)("writeAuth guarda el estado y readAuth lo recupera", () => {
    const state = {
        token: "token-demo",
        user: { id: 1, nombre: "Administrador", correo: "admin@alumipro.com", rol: "ADMIN" }
    };
    (0, auth_storage_1.writeAuth)(state);
    strict_1.default.deepEqual((0, auth_storage_1.readAuth)(), state);
});
(0, node_test_1.default)("clearAuth elimina el estado almacenado", () => {
    (0, auth_storage_1.writeAuth)({
        token: "token-demo",
        user: { id: 2, nombre: "Vendedor", correo: "vendedor@alumipro.com", rol: "VENDEDOR" }
    });
    (0, auth_storage_1.clearAuth)();
    strict_1.default.equal((0, auth_storage_1.readAuth)(), null);
});
(0, node_test_1.default)("AUTH_EVENT se dispara al escribir y limpiar la sesión", () => {
    let count = 0;
    window.addEventListener(auth_storage_1.AUTH_EVENT, () => {
        count += 1;
    });
    (0, auth_storage_1.writeAuth)({
        token: "token-demo",
        user: { id: 1, nombre: "Administrador", correo: "admin@alumipro.com", rol: "ADMIN" }
    });
    (0, auth_storage_1.clearAuth)();
    strict_1.default.equal(count, 2);
});
