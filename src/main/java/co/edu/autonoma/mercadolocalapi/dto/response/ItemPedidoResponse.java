package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.ItemPedidoEntity;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long id,
        OfertaResumenResponse oferta,
        ParticipanteResponse productor,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
        public static ItemPedidoResponse desde(ItemPedidoEntity item, boolean mostrarContacto){
                OfertaEntity oferta = item.getOferta();
                return new ItemPedidoResponse(
                        item.getId(),
                        new OfertaResumenResponse(oferta.getId(), oferta.getProducto()),
                        ParticipanteResponse.desde(oferta.getProductor(), mostrarContacto),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.subtotal()
                );
        }
}