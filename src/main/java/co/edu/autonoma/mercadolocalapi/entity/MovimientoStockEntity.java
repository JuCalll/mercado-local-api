package co.edu.autonoma.mercadolocalapi.entity;

import co.edu.autonoma.mercadolocalapi.domain.TipoMovimiento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "movimientos_stock", schema = "auditoria")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovimientoStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oferta_id", nullable = false)
    private OfertaEntity oferta;

    @Column(name = "pedido_id")
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer variacion;

    @Column(name = "stock_resultante", nullable = false)
    private Integer stockResultante;

    @Column(name = "registrado_en", nullable = false)
    private OffsetDateTime registradoEn;

    private MovimientoStockEntity(OfertaEntity oferta, Long pedidoId, TipoMovimiento tipo, int variacion) {
        this.oferta = oferta;
        this.pedidoId = pedidoId;
        this.tipo = tipo;
        this.variacion = variacion;
        this.stockResultante = oferta.getStock();
        this.registradoEn = OffsetDateTime.now();
    }

    public static MovimientoStockEntity publicacion(OfertaEntity oferta) {
        return new MovimientoStockEntity(oferta, null, TipoMovimiento.PUBLICACION, oferta.getStock());
    }

    public static MovimientoStockEntity ajuste(OfertaEntity oferta, int diferencia) {
        return new MovimientoStockEntity(oferta, null, TipoMovimiento.AJUSTE, diferencia);
    }

    public static MovimientoStockEntity descuento(OfertaEntity oferta, Long pedidoId, int cantidad) {
        return new MovimientoStockEntity(oferta, pedidoId, TipoMovimiento.DESCUENTO, -cantidad);
    }

    public static MovimientoStockEntity devolucion(OfertaEntity oferta, Long pedidoId, int cantidad) {
        return new MovimientoStockEntity(oferta, pedidoId, TipoMovimiento.DEVOLUCION, cantidad);
    }
}