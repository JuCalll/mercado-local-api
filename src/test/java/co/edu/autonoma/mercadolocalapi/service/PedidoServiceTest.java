package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import co.edu.autonoma.mercadolocalapi.dto.request.CrearPedidoRequest;
import co.edu.autonoma.mercadolocalapi.dto.request.ItemPedidoRequest;
import co.edu.autonoma.mercadolocalapi.dto.response.PedidoResponse;
import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import co.edu.autonoma.mercadolocalapi.entity.ItemPedidoEntity;
import co.edu.autonoma.mercadolocalapi.entity.MovimientoStockEntity;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;
import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;
import co.edu.autonoma.mercadolocalapi.repository.CompradorRepository;
import co.edu.autonoma.mercadolocalapi.repository.ItemPedidoRepository;
import co.edu.autonoma.mercadolocalapi.repository.MovimientoStockRepository;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import co.edu.autonoma.mercadolocalapi.repository.PedidoRepository;
import co.edu.autonoma.mercadolocalapi.repository.ProductorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private CompradorRepository compradorRepository;

    @Mock
    private OfertaRepository ofertaRepository;

    @Mock
    private MovimientoStockRepository movimientoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ProductorRepository productorRepository;

    @InjectMocks
    private PedidoService service;

    private final ProductorEntity productor = new ProductorEntity("Finca La Esperanza",
            "finca@correo.com", "3001234567", "Manizales");

    private final CompradorEntity comprador = new CompradorEntity("Ana Gómez",
            "ana@correo.com", "3109876543");

    private OfertaEntity ofertaDeTomate(int stock) {
        return new OfertaEntity(productor, "Tomate chonto",
                new BigDecimal("3500"), stock, LocalDate.now().plusDays(5));
    }

    @Test
    void buscar_existente_devuelveElPedido() {
        // Arrange
        PedidoEntity pedido = new PedidoEntity(comprador);
        when(repository.findById(3L)).thenReturn(Optional.of(pedido));

        // Act
        PedidoResponse respuesta = service.buscar(3L);

        // Assert
        assertThat(respuesta.estado()).isEqualTo(EstadoPedido.PENDIENTE);
        assertThat(respuesta.comprador().nombre()).isEqualTo("Ana Gómez");
    }

    @Test
    void buscar_inexistente_lanzaRecursoNoEncontrado() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,
                () -> service.buscar(99L));

        // Assert
        assertThat(error.getMessage()).isEqualTo("No existe el pedido 99");
    }

    @Test
    void crear_conStockSuficiente_quedaPendienteYDescuentaElStock() {
        // Arrange
        OfertaEntity tomate = ofertaDeTomate(10);
        when(compradorRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(ofertaRepository.buscarParaActualizar(2L)).thenReturn(Optional.of(tomate));
        when(repository.save(any(PedidoEntity.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));
        CrearPedidoRequest request = new CrearPedidoRequest(1L,
                List.of(new ItemPedidoRequest(2L, 3)));

        // Act
        PedidoResponse respuesta = service.crear(request);

        // Assert
        assertThat(respuesta.estado()).isEqualTo(EstadoPedido.PENDIENTE);
        assertThat(respuesta.total()).isEqualByComparingTo("10500");
        assertThat(tomate.getStock()).isEqualTo(7);
    }

    @Test
    void crear_sinStockSuficiente_lanzaReglaNegocioYNoGuarda() {
        // Arrange
        OfertaEntity tomate = ofertaDeTomate(2);
        when(compradorRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(ofertaRepository.buscarParaActualizar(2L)).thenReturn(Optional.of(tomate));
        CrearPedidoRequest request = new CrearPedidoRequest(1L,
                List.of(new ItemPedidoRequest(2L, 5)));

        // Act
        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> service.crear(request));

        // Assert
        assertThat(error.getMessage()).contains("Stock insuficiente");
        assertThat(tomate.getStock()).isEqualTo(2);
        verify(repository, never()).save(any());
    }

    @Test
    void retirarItem_existente_devuelveElStockYRegistraElMovimiento() {
        // Arrange
        OfertaEntity tomate = ofertaDeTomate(8);
        PedidoEntity pedido = new PedidoEntity(comprador);
        pedido.agregarItem(tomate, 2);
        ItemPedidoEntity item = pedido.getItems().get(0);
        when(itemPedidoRepository.findById(5L)).thenReturn(Optional.of(item));
        when(ofertaRepository.buscarParaActualizar(any())).thenReturn(Optional.of(tomate));

        // Act
        service.retirarItem(5L);

        // Assert
        assertThat(pedido.getItems()).isEmpty();
        assertThat(tomate.getStock()).isEqualTo(10);
        verify(movimientoRepository).save(any(MovimientoStockEntity.class));
    }

    @Test
    void retirarItem_inexistente_noRegistraMovimientos() {
        // Arrange
        when(itemPedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        assertThrows(RecursoNoEncontradoException.class, () -> service.retirarItem(99L));

        // Assert
        verify(movimientoRepository, never()).save(any());
    }
}