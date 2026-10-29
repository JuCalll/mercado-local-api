# Contrato de la API · mercado-local-api

Catálogo de todas las peticiones del aplicativo, con la estructura exacta de cada request y de cada respuesta.

**Fuentes:** enunciado del proyecto (usuarios, reglas RN-01 a RN-06 y contrato HTTP mínimo), modelo de datos `database/01_esquema.sql` y clases de la asignatura (verbos HTTP, códigos de estado, DTO).

**Estado de implementación al 29 de octubre:** 3 de 20 (E01, E02 y E03) · 17 pendientes. Todos los DTO del contrato existen en el paquete `dto` desde la rama `feature/iteracion-get`.

---

## 1. Convenciones

| Aspecto | Regla |
|---|---|
| URL base | `http://localhost:8080` |
| Prefijo | Todas las rutas empiezan por `/api` |
| Recursos | Sustantivos en plural y minúscula: `/api/ofertas`, `/api/pedidos` |
| Formato | JSON en UTF-8. Las peticiones con cuerpo envían `Content-Type: application/json` |
| Nombres de propiedades | camelCase, igual que en Java: `precioUnitario`, `vigenteHasta` |
| Fechas | `"2026-10-31"` (año-mes-día) |
| Fecha y hora | `"2026-10-19T20:56:00-05:00"`, con zona horaria |
| Dinero | Número con dos decimales: `3500.00` |
| Estados | Texto en mayúsculas, igual que en la base: `"ACTIVA"`, `"CONFIRMADO"` |
| Identidad | Sin inicio de sesión por ahora: quien actúa se indica en la ruta o en el cuerpo (`compradorId`). Cuando llegue la seguridad, saldrá de la sesión |

### Códigos de estado

| Código | Cuándo |
|---|---|
| `200 OK` | Consulta o modificación exitosa |
| `201 Created` | Recurso creado. Incluye el encabezado `Location` con la ruta del recurso nuevo |
| `204 No Content` | Eliminación exitosa, sin cuerpo |
| `400 Bad Request` | **La forma** de la petición es inválida: falta un campo, un número es negativo, el id no es un número |
| `404 Not Found` | El recurso de la ruta, o uno referenciado en el cuerpo, no existe |
| `409 Conflict` | La petición está bien formada, pero **choca con una regla del negocio**: no hay stock, la oferta está cerrada, el pedido ya se confirmó |
| `500 Internal Server Error` | Falla inesperada del servidor |

Regla para decidir entre `400` y `409`: si el error se detecta mirando solo la petición, es `400`. Si hace falta consultar la base de datos, es `409`.

### Formato único de error

Toda respuesta de error, sin importar el endpoint, tiene esta forma:

```json
{
  "status": 409,
  "error": "Conflict",
  "mensaje": "Stock insuficiente para la oferta 7: disponible 3, solicitado 5",
  "ruta": "/api/pedidos",
  "fecha": "2026-10-19T20:56:00-05:00",
  "detalles": []
}
```

`detalles` siempre está presente. Es una lista vacía, salvo en los errores `400`, donde trae cada campo inválido:

```json
{
  "status": 400,
  "error": "Bad Request",
  "mensaje": "La petición tiene campos inválidos",
  "ruta": "/api/pedidos",
  "fecha": "2026-10-19T20:56:00-05:00",
  "detalles": [
    { "campo": "items[0].cantidad", "mensaje": "debe ser mayor que 0" }
  ]
}
```

### Los ejemplos cuentan una misma historia

Todos los ejemplos usan los mismos datos, en este orden: la *Finca La Esperanza* (productor 1) publica *Tomate chonto* (oferta 7, E04). *Ana Gómez* (compradora 3) crea el pedido 12 con tomate y con panela de otro productor (E17, total `29500.00`). Luego retira la panela (E20) y confirma (E19). Por eso, en E07, E13 y E16 el pedido 12 aparece con un solo ítem y total `17500.00`.

### Privacidad del contacto

El enunciado exige no exponer el contacto antes de confirmar el pedido. Por eso **ninguna respuesta incluye teléfono ni correo**, salvo en las vistas de un pedido `CONFIRMADO` (E13 y E18). Allí aparece el objeto `contacto`; mientras el pedido no esté confirmado, `contacto` es `null`.

---

## 2. Resumen de las 20 peticiones

**Origen:** **Enunciado** = contrato HTTP mínimo obligatorio · **Docente** = anunciado en clase · **Necesario** = sin él, un flujo del enunciado no se puede completar · **Complementario** = mejora la trazabilidad o la supervisión.

| # | Método | Ruta | Qué hace | Origen | Iteración |
|---|---|---|---|---|---|
| E01 | GET | `/api/estado` | Estado del servicio | Guía de inicio | Hecho |
| E02 | GET | `/api/ofertas` | Lista las ofertas vigentes | **Enunciado** | GET · Hecho |
| E03 | GET | `/api/ofertas/{id}` | Consulta una oferta | **Docente** | GET · Hecho |
| E04 | POST | `/api/ofertas` | Publica una oferta | Necesario | POST |
| E05 | PUT | `/api/ofertas/{id}` | Reemplaza una oferta antes de recibir pedidos | **Enunciado** | PUT/PATCH |
| E06 | PATCH | `/api/ofertas/{id}/estado` | Activa o cierra una oferta | **Enunciado** | PUT/PATCH |
| E07 | GET | `/api/ofertas/{id}/movimientos` | Historial de stock de una oferta | Complementario | GET |
| E08 | GET | `/api/productores` | Lista los productores | Complementario | GET |
| E09 | GET | `/api/productores/{id}` | Consulta un productor | Necesario | GET |
| E10 | POST | `/api/productores` | Registra un productor | Necesario | POST |
| E11 | PATCH | `/api/productores/{id}/estado` | Activa o desactiva un productor | Complementario | PUT/PATCH |
| E12 | GET | `/api/productores/{id}/ofertas` | Ofertas de un productor, en cualquier estado | Complementario | GET |
| E13 | GET | `/api/productores/{id}/pedidos` | Vista del productor: pedidos con sus ofertas | **Enunciado** | GET |
| E14 | POST | `/api/compradores` | Registra un comprador | Necesario | POST |
| E15 | GET | `/api/compradores/{id}` | Consulta un comprador | Necesario | GET |
| E16 | GET | `/api/compradores/{id}/pedidos` | Pedidos de un comprador | Necesario | GET |
| E17 | POST | `/api/pedidos` | Crea un pedido y descuenta stock | **Enunciado** | POST |
| E18 | GET | `/api/pedidos/{id}` | Consulta un pedido | Necesario | GET |
| E19 | PATCH | `/api/pedidos/{id}/estado` | Confirma o cancela un pedido | Necesario | PUT/PATCH |
| E20 | DELETE | `/api/items-pedido/{id}` | Retira un ítem antes de confirmar | **Enunciado** | DELETE |

**Por iteración:**

| Iteración | Peticiones | Cantidad |
|---|---|---|
| GET | E02, E03, E07, E08, E09, E12, E13, E15, E16, E18 | 10 |
| POST | E04, E10, E14, E17 | 4 |
| PUT / PATCH | E05, E06, E11, E19 | 4 |
| DELETE | E20 | 1 |

**Por qué existen las peticiones "Necesarias"** que no están en el enunciado:

- **E04, E10 y E14:** sin registrar productores, compradores y ofertas, no hay datos con los que crear pedidos.
- **E19:** el enunciado habla de "retirar un ítem **antes de confirmar**" y de "no exponer contacto **antes de confirmar**". Ambas reglas suponen que existe la acción de confirmar.
- **E09, E15, E16 y E18:** el enunciado dice que el comprador "consulta pedidos". Toda colección necesita también su consulta individual, con `200` o `404`.

---

## 3. Ofertas

### E02 · Listar ofertas vigentes

```http
GET http://localhost:8080/api/ofertas
Accept: application/json
```

**Qué cuenta como vigente:** estado `ACTIVA` y `vigenteHasta` igual o posterior a hoy. Una oferta agotada (`stock` en 0) sigue siendo vigente y se lista: el comprador ve que no hay unidades, y si intenta pedirla, E17 lo rechaza con `409` (RN-02).

**Implementación:** en `develop` desde el pull request #5. El filtro lo hace `OfertaRepository.findByEstadoAndVigenteHastaGreaterThanEqual(ACTIVA, hoy)`, una consulta por nombre del método (diapositiva 18).

**Respuesta `200 OK`** · `List<OfertaResponse>`

```json
[
  {
    "id": 7,
    "producto": "Tomate chonto",
    "precioUnitario": 3500.00,
    "stock": 40,
    "vigenteHasta": "2026-10-31",
    "estado": "ACTIVA"
  }
]
```

Si no hay ofertas vigentes, la respuesta es `200` con `[]`. **Una colección vacía no es un `404`:** la ruta existe; simplemente no hay elementos.

| Código | Cuándo |
|---|---|
| `200` | Siempre, con la lista o con `[]` |

### E03 · Consultar una oferta

```http
GET http://localhost:8080/api/ofertas/7
Accept: application/json
```

Devuelve la oferta en cualquier estado, también si está cerrada o vencida. El campo `estado` permite saberlo.

**Respuesta `200 OK`** · `OfertaResponse`: misma estructura de un elemento de E02.

| Código | Cuándo |
|---|---|
| `200` | La oferta existe |
| `400` | El id no es un número: `/api/ofertas/abc` |
| `404` | No existe una oferta con ese id |

**Implementación actual:** el `404` responde sin cuerpo, con `ResponseEntity.notFound()`. El cuerpo con `ErrorResponse` llegará cuando se construya el manejo global de errores en el paquete `exception`.

### E04 · Publicar una oferta

```http
POST http://localhost:8080/api/ofertas
Content-Type: application/json

{
  "productorId": 1,
  "producto": "Tomate chonto",
  "precioUnitario": 3500.00,
  "stock": 40,
  "vigenteHasta": "2026-10-31"
}
```

**Request** · `CrearOfertaRequest`

| Campo | Tipo | Reglas |
|---|---|---|
| `productorId` | número | Obligatorio |
| `producto` | texto | Obligatorio, máximo 120 caracteres |
| `precioUnitario` | número | Obligatorio, mayor que 0 |
| `stock` | entero | Obligatorio, mayor que 0 |
| `vigenteHasta` | fecha | Obligatoria, hoy o posterior |

No se envían `id` ni `estado`: el servidor los asigna. Toda oferta nueva queda `ACTIVA`.

**Efecto:** registra un movimiento `PUBLICACION` con el stock inicial.

**Respuesta `201 Created`** · `OfertaResponse` · encabezado `Location: /api/ofertas/7`

| Código | Cuándo |
|---|---|
| `201` | Oferta publicada |
| `400` | Falta un campo, el precio o el stock no son positivos, la fecha ya pasó |
| `404` | El productor no existe |
| `409` | El productor está inactivo |

### E05 · Reemplazar una oferta

```http
PUT http://localhost:8080/api/ofertas/7
Content-Type: application/json

{
  "producto": "Tomate chonto de primera",
  "precioUnitario": 3800.00,
  "stock": 35,
  "vigenteHasta": "2026-11-05"
}
```

**Request** · `ReemplazarOfertaRequest`: mismos campos y reglas que E04, sin `productorId`, porque una oferta no cambia de dueño. Como es `PUT`, **todos los campos son obligatorios**: se reemplaza la oferta completa.

**Regla del enunciado:** solo se puede reemplazar **antes de recibir pedidos**, es decir, si ningún ítem de pedido apunta a ella.

**Efecto:** si el stock cambia, registra un movimiento `AJUSTE` con la diferencia.

**Respuesta `200 OK`** · `OfertaResponse`

| Código | Cuándo |
|---|---|
| `200` | Oferta reemplazada |
| `400` | Campos inválidos, igual que en E04 |
| `404` | La oferta no existe |
| `409` | La oferta ya recibió pedidos |

### E06 · Activar o cerrar una oferta

```http
PATCH http://localhost:8080/api/ofertas/7/estado
Content-Type: application/json

{
  "estado": "CERRADA"
}
```

**Request** · `CambiarEstadoOfertaRequest`: `estado` obligatorio, `"ACTIVA"` o `"CERRADA"`.

Es `PATCH` porque cambia **un solo campo**, no la oferta completa. Pedir el mismo estado que ya tiene no es un error: responde `200` sin cambios.

**Respuesta `200 OK`** · `OfertaResponse`

| Código | Cuándo |
|---|---|
| `200` | Estado actualizado |
| `400` | El estado no es `ACTIVA` ni `CERRADA` |
| `404` | La oferta no existe |
| `409` | Se intenta activar una oferta vencida |

### E07 · Historial de stock de una oferta

```http
GET http://localhost:8080/api/ofertas/7/movimientos
Accept: application/json
```

**Respuesta `200 OK`** · `List<MovimientoStockResponse>`, del más antiguo al más reciente

```json
[
  { "id": 1, "tipo": "PUBLICACION", "variacion": 40, "stockResultante": 40, "pedidoId": null, "registradoEn": "2026-10-19T09:00:00-05:00" },
  { "id": 2, "tipo": "DESCUENTO",   "variacion": -5, "stockResultante": 35, "pedidoId": 12,   "registradoEn": "2026-10-19T10:15:00-05:00" }
]
```

| Código | Cuándo |
|---|---|
| `200` | La oferta existe, con su historial |
| `404` | La oferta no existe |

---

## 4. Productores

### E08 · Listar productores

```http
GET http://localhost:8080/api/productores
Accept: application/json
```

**Respuesta `200 OK`** · `List<ProductorResponse>`

```json
[
  { "id": 1, "nombre": "Finca La Esperanza", "municipio": "Manizales", "activo": true }
]
```

Sin teléfono ni correo, por la regla de privacidad.

### E09 · Consultar un productor

```http
GET http://localhost:8080/api/productores/1
Accept: application/json
```

**Respuesta `200 OK`** · `ProductorResponse`

| Código | Cuándo |
|---|---|
| `200` | El productor existe |
| `404` | No existe |

### E10 · Registrar un productor

```http
POST http://localhost:8080/api/productores
Content-Type: application/json

{
  "nombre": "Finca La Esperanza",
  "correo": "esperanza@correo.com",
  "telefono": "3001234567",
  "municipio": "Manizales"
}
```

**Request** · `CrearProductorRequest`

| Campo | Reglas |
|---|---|
| `nombre` | Obligatorio, máximo 120 |
| `correo` | Obligatorio, formato de correo, máximo 150 |
| `telefono` | Obligatorio, máximo 20 |
| `municipio` | Obligatorio, máximo 80 |

**Respuesta `201 Created`** · `ProductorResponse` · `Location: /api/productores/1`. Un productor nuevo queda `activo: true`.

| Código | Cuándo |
|---|---|
| `201` | Registrado |
| `400` | Falta un campo o el correo no tiene formato válido |
| `409` | Ya existe un productor con ese correo |

### E11 · Activar o desactivar un productor

```http
PATCH http://localhost:8080/api/productores/1/estado
Content-Type: application/json

{
  "activo": false
}
```

Uso del administrador. Un productor inactivo no puede publicar ofertas nuevas (E04 responde `409`). Sus ofertas existentes no cambian; si hace falta, se cierran con E06. Un productor nunca se elimina, porque se perdería la trazabilidad de sus ofertas y pedidos.

**Respuesta `200 OK`** · `ProductorResponse`

| Código | Cuándo |
|---|---|
| `200` | Actualizado |
| `400` | Falta `activo` o no es `true` / `false` |
| `404` | El productor no existe |

### E12 · Ofertas de un productor

```http
GET http://localhost:8080/api/productores/1/ofertas
Accept: application/json
```

A diferencia de E02, incluye las ofertas **en cualquier estado**: activas, cerradas, vencidas y agotadas. Es la vista de gestión del propio productor.

**Respuesta `200 OK`** · `List<OfertaResponse>`

| Código | Cuándo |
|---|---|
| `200` | El productor existe, con sus ofertas o `[]` |
| `404` | El productor no existe |

### E13 · Vista del productor: sus pedidos

```http
GET http://localhost:8080/api/productores/1/pedidos
Accept: application/json
```

Devuelve los pedidos que incluyen al menos una oferta de este productor. Como un pedido puede reunir productos de varios productores, **cada productor ve solo sus propios ítems**, no los de los demás.

**Respuesta `200 OK`** · `List<PedidoProductorResponse>`

```json
[
  {
    "pedidoId": 12,
    "estado": "CONFIRMADO",
    "creadoEn": "2026-10-19T10:15:00-05:00",
    "confirmadoEn": "2026-10-19T10:40:00-05:00",
    "comprador": {
      "id": 3,
      "nombre": "Ana Gómez",
      "contacto": { "telefono": "3110000000", "correo": "ana@correo.com" }
    },
    "items": [
      { "itemId": 21, "ofertaId": 7, "producto": "Tomate chonto", "cantidad": 5, "precioUnitario": 3500.00, "subtotal": 17500.00 }
    ],
    "subtotalProductor": 17500.00
  }
]
```

`contacto` es `null` mientras el pedido no esté `CONFIRMADO`.

| Código | Cuándo |
|---|---|
| `200` | El productor existe, con sus pedidos o `[]` |
| `404` | El productor no existe |

---

## 5. Compradores

### E14 · Registrar un comprador

```http
POST http://localhost:8080/api/compradores
Content-Type: application/json

{
  "nombre": "Ana Gómez",
  "correo": "ana@correo.com",
  "telefono": "3110000000"
}
```

**Request** · `CrearCompradorRequest`: `nombre` obligatorio (máximo 120), `correo` obligatorio con formato válido (máximo 150), `telefono` obligatorio (máximo 20).

**Respuesta `201 Created`** · `CompradorResponse` · `Location: /api/compradores/3`

| Código | Cuándo |
|---|---|
| `201` | Registrado |
| `400` | Falta un campo o el correo es inválido |
| `409` | Ya existe un comprador con ese correo |

### E15 · Consultar un comprador

```http
GET http://localhost:8080/api/compradores/3
Accept: application/json
```

**Respuesta `200 OK`** · `CompradorResponse`

```json
{ "id": 3, "nombre": "Ana Gómez", "creadoEn": "2026-10-19T08:30:00-05:00" }
```

| Código | Cuándo |
|---|---|
| `200` | Existe |
| `404` | No existe |

### E16 · Pedidos de un comprador

```http
GET http://localhost:8080/api/compradores/3/pedidos
Accept: application/json
```

**Respuesta `200 OK`** · `List<PedidoResumenResponse>`, del más reciente al más antiguo

```json
[
  { "id": 12, "estado": "CONFIRMADO", "total": 17500.00, "cantidadItems": 1, "creadoEn": "2026-10-19T10:15:00-05:00" }
]
```

Es un resumen: el detalle completo de cada pedido se consulta con E18.

| Código | Cuándo |
|---|---|
| `200` | El comprador existe, con sus pedidos o `[]` |
| `404` | El comprador no existe |

---

## 6. Pedidos

### E17 · Crear un pedido

```http
POST http://localhost:8080/api/pedidos
Content-Type: application/json

{
  "compradorId": 3,
  "items": [
    { "ofertaId": 7, "cantidad": 5 },
    { "ofertaId": 9, "cantidad": 2 }
  ]
}
```

**Request** · `CrearPedidoRequest` con una lista de `ItemPedidoRequest`

| Campo | Reglas |
|---|---|
| `compradorId` | Obligatorio |
| `items` | Obligatorio, al menos un ítem |
| `items[].ofertaId` | Obligatorio, sin repetir dentro del mismo pedido |
| `items[].cantidad` | Obligatoria, mayor que 0 |

**El request no tiene precio ni total (RN-03).** Si el cliente los envía, se ignoran, porque el DTO no tiene dónde recibirlos. El precio de cada ítem se toma de la oferta en el momento del pedido, y el total lo calcula el servidor.

**Efecto, todo o nada (RN-05):** valida cada oferta, descuenta el stock, registra un movimiento `DESCUENTO` por ítem y deja el pedido `PENDIENTE`. Si un solo ítem falla, no se descuenta nada.

**Respuesta `201 Created`** · `PedidoResponse` · `Location: /api/pedidos/12`

```json
{
  "id": 12,
  "estado": "PENDIENTE",
  "comprador": { "id": 3, "nombre": "Ana Gómez" },
  "items": [
    {
      "id": 21,
      "oferta": { "id": 7, "producto": "Tomate chonto" },
      "productor": { "id": 1, "nombre": "Finca La Esperanza", "contacto": null },
      "cantidad": 5,
      "precioUnitario": 3500.00,
      "subtotal": 17500.00
    },
    {
      "id": 22,
      "oferta": { "id": 9, "producto": "Panela orgánica" },
      "productor": { "id": 2, "nombre": "Trapiche El Roble", "contacto": null },
      "cantidad": 2,
      "precioUnitario": 6000.00,
      "subtotal": 12000.00
    }
  ],
  "total": 29500.00,
  "creadoEn": "2026-10-19T10:15:00-05:00",
  "confirmadoEn": null
}
```

Este ejemplo muestra el caso del enunciado: **un pedido con productos de dos productores distintos**.

| Código | Cuándo | Regla |
|---|---|---|
| `201` | Pedido creado | — |
| `400` | Sin ítems, cantidad no positiva u oferta repetida | Forma |
| `404` | El comprador o alguna oferta no existe | — |
| `409` | Alguna oferta está cerrada o vencida | RN-04 |
| `409` | La cantidad supera el stock disponible | RN-02 |

### E18 · Consultar un pedido

```http
GET http://localhost:8080/api/pedidos/12
Accept: application/json
```

**Respuesta `200 OK`** · `PedidoResponse`: misma estructura de E17. Si el pedido está `CONFIRMADO`, el objeto `productor` de cada ítem trae su `contacto`:

```json
{
  "id": 1,
  "nombre": "Finca La Esperanza",
  "contacto": { "telefono": "3001234567", "correo": "esperanza@correo.com" }
}
```

| Código | Cuándo |
|---|---|
| `200` | El pedido existe |
| `404` | No existe |

### E19 · Confirmar o cancelar un pedido

```http
PATCH http://localhost:8080/api/pedidos/12/estado
Content-Type: application/json

{
  "estado": "CONFIRMADO"
}
```

**Request** · `CambiarEstadoPedidoRequest`: `estado` obligatorio, `"CONFIRMADO"` o `"CANCELADO"`. No se acepta `"PENDIENTE"`: un pedido no vuelve atrás.

**Efecto según el estado:**

| Nuevo estado | Qué pasa |
|---|---|
| `CONFIRMADO` | Se registra `confirmadoEn` y, desde ese momento, el contacto de las dos partes queda visible en E13 y E18. Sin pagos: la confirmación es un acuerdo, no una transacción (RN-06) |
| `CANCELADO` | Se devuelve el stock de cada ítem, con un movimiento `DEVOLUCION` por ítem |

**Respuesta `200 OK`** · `PedidoResponse`

| Código | Cuándo |
|---|---|
| `200` | Estado actualizado |
| `400` | El estado no es `CONFIRMADO` ni `CANCELADO` |
| `404` | El pedido no existe |
| `409` | El pedido no está `PENDIENTE` |
| `409` | Se intenta confirmar un pedido sin ítems |

---

## 7. Ítems de pedido

### E20 · Retirar un ítem antes de confirmar

```http
DELETE http://localhost:8080/api/items-pedido/22
```

Sin cuerpo en la petición ni en la respuesta.

**Efecto:** devuelve al stock la cantidad del ítem con un movimiento `DEVOLUCION` y recalcula el total del pedido. Si se retira el último ítem, el pedido queda vacío con total `0` y no se podrá confirmar.

**Respuesta `204 No Content`**

| Código | Cuándo |
|---|---|
| `204` | Ítem retirado |
| `404` | El ítem no existe |
| `409` | El pedido ya no está `PENDIENTE` |

---

## 8. DTO del contrato

Todos los records del paquete `dto`, separados en dos subpaquetes: `dto.request` para lo que entra a la API y `dto.response` para lo que sale.

### 8.1 Entrada · `dto.request`

Llevan anotaciones de validación (`jakarta.validation`) con las reglas de forma de cada petición. Se aplican cuando el Controller recibe el cuerpo con `@Valid`.

| Record | Usado en | Validaciones |
|---|---|---|
| `CrearOfertaRequest` | E04 | Todos obligatorios · `producto` hasta 120 · precio y stock positivos · precio con máximo 2 decimales · `vigenteHasta` hoy o posterior |
| `ReemplazarOfertaRequest` | E05 | Las de E04, sin `productorId` |
| `CambiarEstadoOfertaRequest` | E06 | `estado` obligatorio |
| `CrearProductorRequest` | E10 | Todos obligatorios · correo con formato válido · largos máximos de la tabla |
| `CambiarEstadoProductorRequest` | E11 | `activo` obligatorio |
| `CrearCompradorRequest` | E14 | Todos obligatorios · correo con formato válido |
| `CrearPedidoRequest` | E17 | `compradorId` obligatorio · al menos un ítem · sin ofertas repetidas |
| `ItemPedidoRequest` | E17 | `ofertaId` obligatorio · `cantidad` positiva |
| `CambiarEstadoPedidoRequest` | E19 | `estado` obligatorio · no se acepta `PENDIENTE` |

### 8.2 Salida · `dto.response`

| Record | Usado en |
|---|---|
| `EstadoResponse` | E01 |
| `OfertaResponse` | E02 a E06, E12 |
| `MovimientoStockResponse` | E07 |
| `ProductorResponse` | E08 a E11 |
| `PedidoProductorResponse` | E13 |
| `CompradorResponse` | E14, E15 |
| `PedidoResumenResponse` | E16 |
| `PedidoResponse` | E17, E18, E19 |
| `ErrorResponse` | Todos los errores |

**Objetos anidados**, que aparecen dentro de otras respuestas:

| Record | Campos | Dónde aparece |
|---|---|---|
| `ItemPedidoResponse` | `id`, `oferta`, `productor`, `cantidad`, `precioUnitario`, `subtotal` | Ítems dentro de `PedidoResponse` |
| `CompradorResumenResponse` | `id`, `nombre` | `comprador` dentro de `PedidoResponse` |
| `OfertaResumenResponse` | `id`, `producto` | `oferta` dentro de cada ítem de `PedidoResponse` |
| `ParticipanteResponse` | `id`, `nombre`, `contacto` | `productor` en cada ítem de `PedidoResponse` (E17, E18) y `comprador` en `PedidoProductorResponse` (E13) |
| `ContactoResponse` | `telefono`, `correo` | Dentro de `ParticipanteResponse`. Es `null` mientras el pedido no esté confirmado |
| `ItemProductorResponse` | `itemId`, `ofertaId`, `producto`, `cantidad`, `precioUnitario`, `subtotal` | Ítems dentro de `PedidoProductorResponse` (E13) |
| `DetalleErrorResponse` | `campo`, `mensaje` | Cada elemento de `detalles` dentro de `ErrorResponse` |

### 8.3 Estados del dominio que usan los DTO

| Enum | Valores | Usado en |
|---|---|---|
| `EstadoOferta` | `ACTIVA`, `CERRADA` | `OfertaResponse`, `CambiarEstadoOfertaRequest` |
| `EstadoPedido` | `PENDIENTE`, `CONFIRMADO`, `CANCELADO` | `PedidoResponse`, `PedidoResumenResponse`, `PedidoProductorResponse`, `CambiarEstadoPedidoRequest` |
| `TipoMovimiento` | `PUBLICACION`, `DESCUENTO`, `DEVOLUCION`, `AJUSTE` | `MovimientoStockResponse` |

Los tres viven en el paquete `domain` y sus valores son los mismos que admite la base de datos.

---

## 9. Trazabilidad con las reglas del negocio

| Regla | Peticiones donde se aplica |
|---|---|
| RN-01 · Vigencia y stock | E02 (filtro de vigentes), E04 (stock y fecha obligatorios) |
| RN-02 · No superar el stock | E17 |
| RN-03 · Precio y total en el servidor | E17 (el request no los trae), E20 (recalcula el total) |
| RN-04 · Oferta vencida o inactiva no se incluye | E17, E06 (no se activa una vencida) |
| RN-05 · Stock consistente | E17, E19 y E20 (todo o nada, con su movimiento de stock) |
| RN-06 · Sin pagos | E19 (confirmar no cobra) · ninguna petición recibe datos de pago |
| Contacto solo tras confirmar | E13, E18 |
| Trazabilidad | E07 |
