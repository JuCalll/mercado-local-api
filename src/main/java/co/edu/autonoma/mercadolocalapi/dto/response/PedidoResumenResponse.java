package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PedidoResumenResponse(
        Long id,
        EstadoPedido estado,
        BigDecimal total,
        int cantidadItems,
        OffsetDateTime creadoEn
) {

        public static PedidoResumenResponse desde(PedidoEntity pedido) {
                return new PedidoResumenResponse(
                        pedido.getId(),
                        pedido.getEstado(),
                        pedido.getTotal(),
                        pedido.getItems().size(),
                        pedido.getCreadoEn()
                );
        }
}
