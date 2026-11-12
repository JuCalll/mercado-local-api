package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;

import java.time.OffsetDateTime;

public record CompradorResponse(
        Long id,
        String nombre,
        OffsetDateTime creadoEn
) {
        public static CompradorResponse desde(CompradorEntity comprador){
                return new CompradorResponse(
                        comprador.getId(),
                        comprador.getNombre(),
                        comprador.getCreadoEn()
                );
        }
}
