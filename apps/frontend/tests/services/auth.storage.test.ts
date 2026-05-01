import test from "node:test";
import assert from "node:assert/strict";
import { AUTH_EVENT, clearAuth, readAuth, writeAuth } from "../../src/services/auth.storage";

class MemoryStorage {
  private store = new Map<string, string>();

  getItem(key: string) {
    return this.store.has(key) ? this.store.get(key)! : null;
  }

  setItem(key: string, value: string) {
    this.store.set(key, value);
  }

  removeItem(key: string) {
    this.store.delete(key);
  }
}

function createWindowMock() {
  const listeners = new Map<string, Array<(event: Event) => void>>();
  return {
    addEventListener(type: string, handler: (event: Event) => void) {
      const list = listeners.get(type) ?? [];
      list.push(handler);
      listeners.set(type, list);
    },
    removeEventListener(type: string, handler: (event: Event) => void) {
      const list = listeners.get(type) ?? [];
      listeners.set(type, list.filter((item) => item !== handler));
    },
    dispatchEvent(event: Event) {
      const list = listeners.get(event.type) ?? [];
      for (const handler of list) handler(event);
      return true;
    }
  };
}

test.beforeEach(() => {
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

test("writeAuth guarda el estado y readAuth lo recupera", () => {
  const state = {
    token: "token-demo",
    user: { id: 1, nombre: "Administrador", correo: "admin@alumipro.com", rol: "ADMIN" as const }
  };

  writeAuth(state);
  assert.deepEqual(readAuth(), state);
});

test("clearAuth elimina el estado almacenado", () => {
  writeAuth({
    token: "token-demo",
    user: { id: 2, nombre: "Vendedor", correo: "vendedor@alumipro.com", rol: "VENDEDOR" }
  });

  clearAuth();
  assert.equal(readAuth(), null);
});

test("AUTH_EVENT se dispara al escribir y limpiar la sesión", () => {
  let count = 0;
  window.addEventListener(AUTH_EVENT, () => {
    count += 1;
  });

  writeAuth({
    token: "token-demo",
    user: { id: 1, nombre: "Administrador", correo: "admin@alumipro.com", rol: "ADMIN" }
  });
  clearAuth();

  assert.equal(count, 2);
});
