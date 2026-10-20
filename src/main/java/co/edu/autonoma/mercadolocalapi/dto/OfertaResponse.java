package co.edu.autonoma.mercadolocalapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaResponse(
        Long id,
        String producto,
        BigDecimal precioUnitario,
        Integer stock,
        LocalDate vigenteHasta
) {
}
