package co.edu.autonoma.mercadolocalapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoEntityTest {

    @Test
    void agregarItem_conUnItemPrevio_sumaAmbosSubtotalesEnElTotal() {
        // Arrange
        ProductorEntity productor = new ProductorEntity("Finca La Esperanza",
                "finca@correo.com", "3001234567", "Manizales");
        OfertaEntity tomate = new OfertaEntity(productor, "Tomate chonto",
                new BigDecimal("3500"), 20, LocalDate.now().plusDays(5));
        OfertaEntity mora = new OfertaEntity(productor, "Mora de Castilla",
                new BigDecimal("8000"), 10, LocalDate.now().plusDays(5));
        PedidoEntity pedido = new PedidoEntity(
                new CompradorEntity("Ana Gómez", "ana@correo.com", "3109876543"));
        pedido.agregarItem(tomate, 2);

        // Act
        pedido.agregarItem(mora, 1);

        // Assert
        assertThat(pedido.getItems()).hasSize(2);
        assertThat(pedido.getTotal()).isEqualByComparingTo("15000");
    }
}