package co.edu.autonoma.mercadolocalapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequest(
        @NotNull Long ofertaId,
        @NotNull @Positive Integer cantidad
) {
}
