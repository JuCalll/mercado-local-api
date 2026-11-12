package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;
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

        public static PedidoProductorResponse desde(PedidoEntity pedido, Long productorId){
                List<ItemProductorResponse> items = pedido.getItems().stream()
                        .filter(item -> item.getOferta().getProductor().getId().equals(productorId))
                        .map(ItemProductorResponse::desde)
                        .toList();
                BigDecimal subtotal = items.stream()
                        .map(ItemProductorResponse::subtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                return new PedidoProductorResponse(
                        pedido.getId(),
                        pedido.getEstado(),
                        pedido.getCreadoEn(),
                        pedido.getConfirmadoEn(),
                        ParticipanteResponse.desde(pedido.getComprador(), pedido.estaConfirmado()),
                        items,
                        subtotal
                );
        }
}
