package co.edu.autonoma.mercadolocalapi.dto.request;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoOfertaRequest(
        @NotNull EstadoOferta estado
) {
}
