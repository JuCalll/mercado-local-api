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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public OfertaEntity(String producto, BigDecimal precioUnitario,
                        Integer stock, LocalDate vigenteHasta) {
        this.producto = producto;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.vigenteHasta = vigenteHasta;
        this.estado = EstadoOferta.ACTIVA;
    }
}