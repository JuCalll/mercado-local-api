package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaResponse(
        Long id,
        String producto,
        BigDecimal precioUnitario,
        Integer stock,
        LocalDate vigenteHasta,
        EstadoOferta estado
) {

    public static OfertaResponse desde(OfertaEntity oferta) {
        return new OfertaResponse(
                oferta.getId(),
                oferta.getProducto(),
                oferta.getPrecioUnitario(),
                oferta.getStock(),
                oferta.getVigenteHasta(),
                oferta.getEstado()
        );
    }
}