package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.ItemPedidoEntity;

import java.math.BigDecimal;

public record ItemProductorResponse(
        Long itemId,
        Long ofertaId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {

        public static ItemProductorResponse desde(ItemPedidoEntity item){
                return new ItemProductorResponse(
                        item.getId(),
                        item.getOferta().getId(),
                        item.getOferta().getProducto(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.subtotal()
                );
        }
}
