# GA8-220501096-AA1-EV02 – Documentación de servicios web

Base URL API: `http://localhost:8080/api`

## 1. Autenticación
### POST `/auth/login`
Permite iniciar sesión con correo y contraseña.

Body de ejemplo:
```json
{
  "correo": "admin@alumipro.com",
  "password": "admin123"
}
```

Respuesta:
```json
{
  "token": "jwt",
  "user": {
    "id": 1,
    "nombre": "Administrador ALUMIPRO",
    "correo": "admin@alumipro.com",
    "rol": "ADMIN"
  }
}
```

### GET `/auth/me`
Retorna el usuario autenticado a partir del JWT.

## 2. Clientes
- `GET /clientes`
- `GET /clientes/{id}`
- `POST /clientes`
- `PUT /clientes/{id}`
- `DELETE /clientes/{id}`

Reglas:
- Nombre obligatorio.
- Correo válido si se informa.
- Teléfono solo numérico y mínimo 7 dígitos.

## 3. Productos
- `GET /productos`
- `GET /productos/{id}`
- `POST /productos`
- `PUT /productos/{id}`
- `DELETE /productos/{id}`

Reglas:
- Nombre obligatorio.
- Precio mayor o igual a 0.
- Stock mayor o igual a 0.

## 4. Ventas
- `POST /ventas`
- `GET /ventas`
- `GET /ventas/{id}`

Reglas:
- Debe existir cliente.
- Debe existir al menos un ítem.
- Cada ítem debe tener producto y cantidad mayor a 0.
- No se permite vender con stock insuficiente.

## 5. Usuarios
- `GET /usuarios`
- `POST /usuarios`
- `PUT /usuarios/{id}`
- `DELETE /usuarios/{id}`

Reglas:
- Solo rol `ADMIN`.
- Correo único.
- Contraseña obligatoria en creación.
