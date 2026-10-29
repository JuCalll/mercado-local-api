package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record PedidoProductorResponse(
        Long pedidoId,
        EstadoPedido estado,
        OffsetDateTime creadoEn,
        OffsetDateTime confirmadoEn,
        ParticipanteResponse comprador,
        List<ItemProductorResponse> items,
        BigDecimal subtotalProductor
) {
}
