# cURL para importar en Postman

Pega un cURL por request en Postman → Import → Raw text.

## Login

```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
  "correo": "admin@alumipro.com",
  "password": "admin123"
}'
```

## Clientes

```bash
curl --location 'http://localhost:8080/api/clientes' \
--header 'Authorization: Bearer {{TOKEN}}'
```

```bash
curl --location 'http://localhost:8080/api/clientes' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Cliente Nuevo",
  "telefono": "3000000000",
  "direccion": "Calle 10 #20-30",
  "email": "cliente@correo.com"
}'
```

```bash
curl --location --request PUT 'http://localhost:8080/api/clientes/1' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Cliente Editado",
  "telefono": "3111111111",
  "direccion": "Carrera 12 #34-56",
  "email": "cliente.edit@correo.com"
}'
```

```bash
curl --location --request DELETE 'http://localhost:8080/api/clientes/1' \
--header 'Authorization: Bearer {{TOKEN}}'
```

## Productos (POST/PUT/DELETE solo ADMIN)

```bash
curl --location 'http://localhost:8080/api/productos' \
--header 'Authorization: Bearer {{TOKEN}}'
```

```bash
curl --location 'http://localhost:8080/api/productos' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Perfil Aluminio 2m",
  "precio": 98000,
  "stock": 25,
  "descripcion": "Perfil industrial para ventanería"
}'
```

```bash
curl --location --request PUT 'http://localhost:8080/api/productos/1' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Perfil Aluminio 2m Reforzado",
  "precio": 105000,
  "stock": 20,
  "descripcion": "Perfil reforzado"
}'
```

```bash
curl --location --request DELETE 'http://localhost:8080/api/productos/1' \
--header 'Authorization: Bearer {{TOKEN}}'
```

## Ventas

```bash
curl --location 'http://localhost:8080/api/ventas' \
--header 'Authorization: Bearer {{TOKEN}}'
```

```bash
curl --location 'http://localhost:8080/api/ventas' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "clienteId": 1,
  "items": [
    { "productoId": 2, "cantidad": 1 },
    { "productoId": 3, "cantidad": 2 }
  ]
}'
```

```bash
curl --location 'http://localhost:8080/api/ventas/1' \
--header 'Authorization: Bearer {{TOKEN}}'
```

## Usuarios (solo ADMIN)

```bash
curl --location 'http://localhost:8080/api/usuarios' \
--header 'Authorization: Bearer {{TOKEN}}'
```

```bash
curl --location 'http://localhost:8080/api/usuarios' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Vendedor 2",
  "correo": "vend2@alumipro.com",
  "password": "123456",
  "rol": "VENDEDOR"
}'
```

```bash
curl --location --request PUT 'http://localhost:8080/api/usuarios/2' \
--header 'Authorization: Bearer {{TOKEN}}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombre": "Vendedor 2 Editado",
  "rol": "VENDEDOR"
}'
```

```bash
curl --location --request DELETE 'http://localhost:8080/api/usuarios/2' \
--header 'Authorization: Bearer {{TOKEN}}'
```
