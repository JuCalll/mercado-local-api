package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;

public record ProductorResponse(
        Long id,
        String nombre,
        String municipio,
        boolean activo
) {

    public static ProductorResponse desde(ProductorEntity productor) {
        return new ProductorResponse(
                productor.getId(),
                productor.getNombre(),
                productor.getMunicipio(),
                productor.isActivo()
        );
    }
}