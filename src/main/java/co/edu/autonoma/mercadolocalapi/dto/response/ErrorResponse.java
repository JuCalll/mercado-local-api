package co.edu.autonoma.mercadolocalapi.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String mensaje,
        String ruta,
        OffsetDateTime fecha,
        List<DetalleErrorResponse> detalles
) {
}
