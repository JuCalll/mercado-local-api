package co.edu.autonoma.mercadolocalapi.dto.response;

import java.math.BigDecimal;

public record ItemProductorResponse(
        Long itemId,
        Long ofertaId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
