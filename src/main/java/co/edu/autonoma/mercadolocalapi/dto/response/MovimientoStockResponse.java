package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.TipoMovimiento;

import java.time.OffsetDateTime;

public record MovimientoStockResponse(
        Long id,
        TipoMovimiento tipo,
        Integer variacion,
        Integer stockResultante,
        Long pedidoId,
        OffsetDateTime registradoEn
) {
}
