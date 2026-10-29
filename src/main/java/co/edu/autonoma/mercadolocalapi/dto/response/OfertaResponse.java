package co.edu.autonoma.mercadolocalapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;

public record OfertaResponse(
        Long id,
        String producto,
        BigDecimal precioUnitario,
        Integer stock,
        LocalDate vigenteHasta,
        EstadoOferta estado
) {
}
