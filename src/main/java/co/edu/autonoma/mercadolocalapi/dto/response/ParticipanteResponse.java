package co.edu.autonoma.mercadolocalapi.dto.response;

public record ParticipanteResponse(
        Long id,
        String nombre,
        ContactoResponse contacto
) {
}
