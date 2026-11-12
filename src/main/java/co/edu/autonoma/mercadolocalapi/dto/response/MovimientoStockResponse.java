package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.domain.TipoMovimiento;
import co.edu.autonoma.mercadolocalapi.entity.MovimientoStockEntity;
import java.time.OffsetDateTime;

public record MovimientoStockResponse(
                Long id,
                TipoMovimiento tipo,
                Integer variacion,
                Integer stockResultante,
                Long pedidoId,
                OffsetDateTime registradoEn) {

        public static MovimientoStockResponse desde(MovimientoStockEntity movimiento) {
                return new MovimientoStockResponse(
                                movimiento.getId(),
                                movimiento.getTipo(),
                                movimiento.getVariacion(),
                                movimiento.getStockResultante(),
                                movimiento.getPedidoId(),
                                movimiento.getRegistradoEn());
        }
}
