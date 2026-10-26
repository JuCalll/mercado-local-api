package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PedidoResumenResponse(
        Long id,
        EstadoPedido estado,
        BigDecimal total,
        int cantidadItems,
        OffsetDateTime creadoEn
) {
}
