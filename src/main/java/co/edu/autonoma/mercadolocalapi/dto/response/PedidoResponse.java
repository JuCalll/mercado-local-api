package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        EstadoPedido estado,
        CompradorResumenResponse comprador,
        List<ItemPedidoResponse> items,
        BigDecimal total,
        OffsetDateTime creadoEn,
        OffsetDateTime confirmadoEn
) {
}
