package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;

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

        public static PedidoResponse desde(PedidoEntity pedido) {
                boolean confirmado = pedido.estaConfirmado();
                CompradorEntity comprador = pedido.getComprador();
                return new PedidoResponse(
                        pedido.getId(),
                        pedido.getEstado(),
                        new CompradorResumenResponse(comprador.getId(), comprador.getNombre()),
                        pedido.getItems().stream()
                        .map(item -> ItemPedidoResponse.desde(item, confirmado))
                        .toList(),
                        pedido.getTotal(),
                        pedido.getCreadoEn(),
                        pedido.getConfirmadoEn()
                );
        }
}
