# mercado-local-api

Backend para gestionar productores, ofertas, stock y pedidos de un mercado local.

## Integrantes
- Juan Camilo Orozco Londoño
- Miguel Ángel López Betancur

## Tecnologías
Java 17 · Spring Boot · Maven · PostgreSQL 18

## Base de datos

1. Como superusuario (`psql -U postgres`), crear el usuario y la base:

   CREATE USER mercado_app WITH PASSWORD 'tu-contraseña';
   CREATE DATABASE mercado_local OWNER mercado_app;

2. Desde la carpeta del proyecto, crear esquemas y tablas:

   psql -U mercado_app -d mercado_local -v ON_ERROR_STOP=1 -f database/01_esquema.sql

3. Definir la variable de entorno `DB_PASSWORD` con la contraseña de `mercado_app`.

## Cómo ejecutar

Windows: `.\mvnw.cmd spring-boot:run`
Linux / macOS: `./mvnw spring-boot:run`

La API queda disponible en `http://localhost:8080`.

## Estructura del proyecto

```
co.edu.autonoma.mercadolocalapi
├── controller      recibe las peticiones HTTP y delega
├── service         coordina los casos de uso y aplica las reglas del negocio
├── repository      acceso a datos con Spring Data JPA
├── entity          clases mapeadas a las tablas de la base
├── domain          estados del negocio (EstadoOferta, EstadoPedido, TipoMovimiento)
├── dto
│   ├── request     lo que entra a la API, con sus validaciones
│   └── response    lo que sale de la API
├── config          configuración y comprobaciones de arranque
└── exception       errores del negocio y su traducción a HTTP
```

## Endpoints

Contrato completo, con la estructura de cada request y respuesta: [`docs/contrato-api.md`](docs/contrato-api.md).


| # | Método | Ruta | Qué hace | Request | Response | Códigos | Estado |
|---|---|---|---|---|---|---|---|
| E01 | GET | `/api/estado` | Estado del servicio | — | `EstadoResponse` | 200 | Implementado |
| E02 | GET | `/api/ofertas` | Lista las ofertas vigentes | — | `List<OfertaResponse>` | 200 | Implementado |
| E03 | GET | `/api/ofertas/{id}` | Consulta una oferta | — | `OfertaResponse` | 200 · 400 · 404 | Implementado |
| E04 | POST | `/api/ofertas` | Publica una oferta | `CrearOfertaRequest` | `OfertaResponse` | 201 · 400 · 404 · 409 | Pendiente |
| E05 | PUT | `/api/ofertas/{id}` | Reemplaza una oferta antes de recibir pedidos | `ReemplazarOfertaRequest` | `OfertaResponse` | 200 · 400 · 404 · 409 | Pendiente |
| E06 | PATCH | `/api/ofertas/{id}/estado` | Activa o cierra una oferta | `CambiarEstadoOfertaRequest` | `OfertaResponse` | 200 · 400 · 404 · 409 | Pendiente |
| E07 | GET | `/api/ofertas/{id}/movimientos` | Historial de stock de una oferta | — | `List<MovimientoStockResponse>` | 200 · 404 | Pendiente |
| E08 | GET | `/api/productores` | Lista los productores | — | `List<ProductorResponse>` | 200 | Pendiente |
| E09 | GET | `/api/productores/{id}` | Consulta un productor | — | `ProductorResponse` | 200 · 404 | Pendiente |
| E10 | POST | `/api/productores` | Registra un productor | `CrearProductorRequest` | `ProductorResponse` | 201 · 400 · 409 | Pendiente |
| E11 | PATCH | `/api/productores/{id}/estado` | Activa o desactiva un productor | `CambiarEstadoProductorRequest` | `ProductorResponse` | 200 · 400 · 404 | Pendiente |
| E12 | GET | `/api/productores/{id}/ofertas` | Ofertas de un productor, en cualquier estado | — | `List<OfertaResponse>` | 200 · 404 | Pendiente |
| E13 | GET | `/api/productores/{id}/pedidos` | Vista del productor: pedidos con sus ofertas | — | `List<PedidoProductorResponse>` | 200 · 404 | Pendiente |
| E14 | POST | `/api/compradores` | Registra un comprador | `CrearCompradorRequest` | `CompradorResponse` | 201 · 400 · 409 | Pendiente |
| E15 | GET | `/api/compradores/{id}` | Consulta un comprador | — | `CompradorResponse` | 200 · 404 | Pendiente |
| E16 | GET | `/api/compradores/{id}/pedidos` | Pedidos de un comprador | — | `List<PedidoResumenResponse>` | 200 · 404 | Pendiente |
| E17 | POST | `/api/pedidos` | Crea un pedido y descuenta stock | `CrearPedidoRequest` | `PedidoResponse` | 201 · 400 · 404 · 409 | Pendiente |
| E18 | GET | `/api/pedidos/{id}` | Consulta un pedido | — | `PedidoResponse` | 200 · 404 | Pendiente |
| E19 | PATCH | `/api/pedidos/{id}/estado` | Confirma o cancela un pedido | `CambiarEstadoPedidoRequest` | `PedidoResponse` | 200 · 400 · 404 · 409 | Pendiente |
| E20 | DELETE | `/api/items-pedido/{id}` | Retira un ítem antes de confirmar | — | — | 204 · 404 · 409 | Pendiente |

Todos los errores responden con `ErrorResponse`.

### Reglas del negocio por endpoint

| Regla | Endpoints |
|---|---|
| RN-01 · Una oferta tiene vigencia y stock | E02, E04 |
| RN-02 · El pedido no puede superar el stock | E17 |
| RN-03 · Precio y total se calculan en el servidor | E17, E20 |
| RN-04 · Una oferta vencida o inactiva no se incluye | E06, E17 |
| RN-05 · El stock se descuenta de forma consistente | E17, E19, E20 |
| RN-06 · Sin pagos reales | E19 |

## Flujo de trabajo

- `main`: versiones entregables.
- `develop`: integración. Todo entra por pull request.
- `feature/...`, `fix/...`, `refactor/...`, `docs/...`: una rama por tarea.

Cada pull request pide la revisión de la docente (`.github/CODEOWNERS`).
