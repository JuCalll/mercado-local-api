package co.edu.autonoma.mercadolocalapi.entity;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ofertas", schema = "catalogo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productor_id", nullable = false)
    private ProductorEntity productor;

    @Column(nullable = false, length = 120)
    private String producto;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "vigente_hasta", nullable = false)
    private LocalDate vigenteHasta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOferta estado;

    public OfertaEntity(ProductorEntity productor, String producto, BigDecimal precioUnitario,
            Integer stock, LocalDate vigenteHasta) {
        this.productor = productor;
        this.producto = producto;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.vigenteHasta = vigenteHasta;
        this.estado = EstadoOferta.ACTIVA;
    }

    public void cambiarEstado(EstadoOferta nuevoEstado, LocalDate hoy) {
        if (nuevoEstado == EstadoOferta.ACTIVA && vigenteHasta.isBefore(hoy)) {
            throw new ReglaNegocioException("La oferta " + id + " está vencida y no se puede activar");
        }
        this.estado = nuevoEstado;
    }
}
