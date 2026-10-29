package co.edu.autonoma.mercadolocalapi.dto.response;

public record ProductorResponse(
        Long id,
        String nombre,
        String municipio,
        boolean activo
) {
}
