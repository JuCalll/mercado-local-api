package co.edu.autonoma.mercadolocalapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoProductorRequest(
        @NotNull Boolean activo
) {
}
