package co.edu.autonoma.mercadolocalapi.dto.request;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoPedidoRequest(
        @NotNull EstadoPedido estado
) {

    @AssertTrue(message = "solo se puede pasar a CONFIRMADO o CANCELADO")
    public boolean isEstadoDestinoValido() {
        return estado != EstadoPedido.PENDIENTE;
    }
}
