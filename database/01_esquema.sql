SET client_encoding = 'UTF8';

BEGIN;

-- ---------------------------------------------------------------------
-- 1. Esquemas: un esquema por área del negocio
-- ---------------------------------------------------------------------

CREATE SCHEMA personas;
CREATE SCHEMA catalogo;
CREATE SCHEMA ventas;
CREATE SCHEMA auditoria;

COMMENT ON SCHEMA personas  IS 'Quienes participan en el mercado: productores y compradores';
COMMENT ON SCHEMA catalogo  IS 'Ofertas publicadas por los productores';
COMMENT ON SCHEMA ventas    IS 'Pedidos de los compradores y sus ítems';
COMMENT ON SCHEMA auditoria IS 'Trazabilidad de los cambios de stock';

-- ---------------------------------------------------------------------
-- 2. personas.productores
-- ---------------------------------------------------------------------

CREATE TABLE personas.productores (
                                      id          BIGINT       GENERATED ALWAYS AS IDENTITY,
                                      nombre      VARCHAR(120) NOT NULL,
                                      correo      VARCHAR(150) NOT NULL,
                                      telefono    VARCHAR(20)  NOT NULL,
                                      municipio   VARCHAR(80)  NOT NULL,
                                      activo      BOOLEAN      NOT NULL DEFAULT TRUE,
                                      creado_en   TIMESTAMPTZ  NOT NULL DEFAULT now(),

                                      CONSTRAINT pk_productores        PRIMARY KEY (id),
                                      CONSTRAINT uq_productores_correo UNIQUE (correo)
);

COMMENT ON TABLE personas.productores IS 'Productores que publican ofertas. Su contacto solo se revela al confirmar un pedido';

-- ---------------------------------------------------------------------
-- 3. personas.compradores
-- ---------------------------------------------------------------------

CREATE TABLE personas.compradores (
                                      id          BIGINT       GENERATED ALWAYS AS IDENTITY,
                                      nombre      VARCHAR(120) NOT NULL,
                                      correo      VARCHAR(150) NOT NULL,
                                      telefono    VARCHAR(20)  NOT NULL,
                                      creado_en   TIMESTAMPTZ  NOT NULL DEFAULT now(),

                                      CONSTRAINT pk_compradores        PRIMARY KEY (id),
                                      CONSTRAINT uq_compradores_correo UNIQUE (correo)
);

COMMENT ON TABLE personas.compradores IS 'Compradores que consultan ofertas y crean pedidos';

-- ---------------------------------------------------------------------
-- 4. catalogo.ofertas            (RN-01, RN-02, RN-04)
-- ---------------------------------------------------------------------

CREATE TABLE catalogo.ofertas (
                                  id               BIGINT        GENERATED ALWAYS AS IDENTITY,
                                  productor_id     BIGINT        NOT NULL,
                                  producto         VARCHAR(120)  NOT NULL,
                                  precio_unitario  NUMERIC(12,2) NOT NULL,
                                  stock            INTEGER       NOT NULL,
                                  vigente_hasta    DATE          NOT NULL,
                                  estado           VARCHAR(20)   NOT NULL DEFAULT 'ACTIVA',
                                  creado_en        TIMESTAMPTZ   NOT NULL DEFAULT now(),

                                  CONSTRAINT pk_ofertas                   PRIMARY KEY (id),
                                  CONSTRAINT fk_ofertas_productor         FOREIGN KEY (productor_id)
                                      REFERENCES personas.productores (id),
                                  CONSTRAINT ck_ofertas_precio_positivo   CHECK (precio_unitario > 0),
                                  CONSTRAINT ck_ofertas_stock_no_negativo CHECK (stock >= 0),
                                  CONSTRAINT ck_ofertas_estado            CHECK (estado IN ('ACTIVA', 'CERRADA'))
);

COMMENT ON TABLE catalogo.ofertas IS 'Ofertas con precio, stock y vigencia. El stock nunca puede quedar negativo';

-- ---------------------------------------------------------------------
-- 5. ventas.pedidos              (RN-03, RN-06)
-- ---------------------------------------------------------------------

CREATE TABLE ventas.pedidos (
                                id             BIGINT        GENERATED ALWAYS AS IDENTITY,
                                comprador_id   BIGINT        NOT NULL,
                                estado         VARCHAR(20)   NOT NULL DEFAULT 'PENDIENTE',
                                total          NUMERIC(14,2) NOT NULL DEFAULT 0,
                                creado_en      TIMESTAMPTZ   NOT NULL DEFAULT now(),
                                confirmado_en  TIMESTAMPTZ,

                                CONSTRAINT pk_pedidos                  PRIMARY KEY (id),
                                CONSTRAINT fk_pedidos_comprador        FOREIGN KEY (comprador_id)
                                    REFERENCES personas.compradores (id),
                                CONSTRAINT ck_pedidos_estado           CHECK (estado IN ('PENDIENTE', 'CONFIRMADO', 'CANCELADO')),
                                CONSTRAINT ck_pedidos_total_no_negativo CHECK (total >= 0),
                                CONSTRAINT ck_pedidos_fecha_confirmacion CHECK (estado <> 'CONFIRMADO' OR confirmado_en IS NOT NULL)
);

COMMENT ON TABLE ventas.pedidos IS 'Pedidos de un comprador. Pueden incluir ofertas de varios productores. Sin pagos reales';

-- ---------------------------------------------------------------------
-- 6. ventas.items_pedido         (RN-02, RN-03)
-- ---------------------------------------------------------------------

CREATE TABLE ventas.items_pedido (
                                     id               BIGINT        GENERATED ALWAYS AS IDENTITY,
                                     pedido_id        BIGINT        NOT NULL,
                                     oferta_id        BIGINT        NOT NULL,
                                     cantidad         INTEGER       NOT NULL,
                                     precio_unitario  NUMERIC(12,2) NOT NULL,

                                     CONSTRAINT pk_items_pedido                PRIMARY KEY (id),
                                     CONSTRAINT fk_items_pedido_pedido         FOREIGN KEY (pedido_id)
                                         REFERENCES ventas.pedidos (id) ON DELETE CASCADE,
                                     CONSTRAINT fk_items_pedido_oferta         FOREIGN KEY (oferta_id)
                                         REFERENCES catalogo.ofertas (id),
                                     CONSTRAINT uq_items_pedido_pedido_oferta  UNIQUE (pedido_id, oferta_id),
                                     CONSTRAINT ck_items_pedido_cantidad       CHECK (cantidad > 0),
                                     CONSTRAINT ck_items_pedido_precio         CHECK (precio_unitario > 0)
);

COMMENT ON TABLE ventas.items_pedido IS 'Cada ítem guarda el precio vigente al momento del pedido, calculado por el servidor';

-- ---------------------------------------------------------------------
-- 7. auditoria.movimientos_stock (RN-05 y trazabilidad)
-- ---------------------------------------------------------------------

CREATE TABLE auditoria.movimientos_stock (
                                             id                BIGINT      GENERATED ALWAYS AS IDENTITY,
                                             oferta_id         BIGINT      NOT NULL,
                                             pedido_id         BIGINT,
                                             tipo              VARCHAR(20) NOT NULL,
                                             variacion         INTEGER     NOT NULL,
                                             stock_resultante  INTEGER     NOT NULL,
                                             registrado_en     TIMESTAMPTZ NOT NULL DEFAULT now(),

                                             CONSTRAINT pk_movimientos_stock            PRIMARY KEY (id),
                                             CONSTRAINT fk_movimientos_stock_oferta     FOREIGN KEY (oferta_id)
                                                 REFERENCES catalogo.ofertas (id),
                                             CONSTRAINT fk_movimientos_stock_pedido     FOREIGN KEY (pedido_id)
                                                 REFERENCES ventas.pedidos (id),
                                             CONSTRAINT ck_movimientos_stock_tipo       CHECK (tipo IN ('PUBLICACION', 'DESCUENTO', 'DEVOLUCION', 'AJUSTE')),
                                             CONSTRAINT ck_movimientos_stock_signo      CHECK (
                                                 (tipo = 'PUBLICACION' AND variacion > 0)
                                                     OR (tipo = 'DESCUENTO'   AND variacion < 0)
                                                     OR (tipo = 'DEVOLUCION'  AND variacion > 0)
                                                     OR (tipo = 'AJUSTE'      AND variacion <> 0)
                                                 ),
                                             CONSTRAINT ck_movimientos_stock_pedido     CHECK ((tipo IN ('DESCUENTO', 'DEVOLUCION')) = (pedido_id IS NOT NULL)),
                                             CONSTRAINT ck_movimientos_stock_resultante CHECK (stock_resultante >= 0)
);

COMMENT ON TABLE auditoria.movimientos_stock IS 'Cada cambio de stock deja un registro. La suma de las variaciones de una oferta es su stock actual';

-- ---------------------------------------------------------------------
-- 8. Índices para las consultas del contrato HTTP
-- ---------------------------------------------------------------------

-- GET /api/ofertas: solo ofertas activas, filtradas por vigencia
CREATE INDEX ix_ofertas_vigentes        ON catalogo.ofertas (vigente_hasta) WHERE estado = 'ACTIVA';

-- GET /api/productores/{id}/pedidos: ofertas de un productor y sus ítems
CREATE INDEX ix_ofertas_productor       ON catalogo.ofertas (productor_id);
CREATE INDEX ix_items_pedido_oferta     ON ventas.items_pedido (oferta_id);

-- Pedidos de un comprador
CREATE INDEX ix_pedidos_comprador       ON ventas.pedidos (comprador_id);

-- Historial de stock de una oferta
CREATE INDEX ix_movimientos_stock_oferta ON auditoria.movimientos_stock (oferta_id);

COMMIT;