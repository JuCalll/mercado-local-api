package co.edu.autonoma.mercadolocalapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public record CrearPedidoRequest(
        @NotNull Long compradorId,
        @NotEmpty List<@Valid @NotNull ItemPedidoRequest> items
) {

    @AssertTrue(message = "no se puede repetir una oferta dentro del mismo pedido")
    public boolean isSinOfertasRepetidas() {
        if (items == null) {
            return true;
        }
        List<Long> ofertas = items.stream()
                .filter(Objects::nonNull)
                .map(ItemPedidoRequest::ofertaId)
                .filter(Objects::nonNull)
                .toList();
        return ofertas.size() == ofertas.stream().distinct().count();
    }
}
