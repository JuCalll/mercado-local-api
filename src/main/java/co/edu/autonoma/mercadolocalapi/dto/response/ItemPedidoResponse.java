package co.edu.autonoma.mercadolocalapi.dto.response;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long id,
        OfertaResumenResponse oferta,
        ParticipanteResponse productor,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
