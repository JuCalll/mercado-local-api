package co.edu.autonoma.mercadolocalapi.dto.response;

public record DetalleErrorResponse(
        String campo,
        String mensaje
) {
}
