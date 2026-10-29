package co.edu.autonoma.mercadolocalapi.dto.response;

import java.time.OffsetDateTime;

public record CompradorResponse(
        Long id,
        String nombre,
        OffsetDateTime creadoEn
) {
}
