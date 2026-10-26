package co.edu.autonoma.mercadolocalapi.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearOfertaRequest(
        @NotNull Long productorId,
        @NotBlank @Size(max = 120) String producto,
        @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal precioUnitario,
        @NotNull @Positive Integer stock,
        @NotNull @FutureOrPresent LocalDate vigenteHasta
) {
}
