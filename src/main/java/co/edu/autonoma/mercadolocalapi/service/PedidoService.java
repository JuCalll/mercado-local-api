package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.dto.request.CrearPedidoRequest;
import co.edu.autonoma.mercadolocalapi.dto.request.ItemPedidoRequest;
import co.edu.autonoma.mercadolocalapi.dto.response.PedidoResponse;
import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import co.edu.autonoma.mercadolocalapi.entity.ItemPedidoEntity;
import co.edu.autonoma.mercadolocalapi.entity.MovimientoStockEntity;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.repository.CompradorRepository;
import co.edu.autonoma.mercadolocalapi.repository.MovimientoStockRepository;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import co.edu.autonoma.mercadolocalapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import co.edu.autonoma.mercadolocalapi.dto.request.CambiarEstadoPedidoRequest;
import co.edu.autonoma.mercadolocalapi.repository.ItemPedidoRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final CompradorRepository compradorRepository;
    private final OfertaRepository ofertaRepository;
    private final MovimientoStockRepository movimientoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Transactional
    public PedidoResponse crear(CrearPedidoRequest request) {
        CompradorEntity comprador = compradorRepository.findById(request.compradorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el comprador " + request.compradorId()));

        PedidoEntity pedido = new PedidoEntity(comprador);
        LocalDate hoy = LocalDate.now();

        List<ItemPedidoRequest> itemsOrdenados = request.items().stream()
                .sorted(Comparator.comparing(ItemPedidoRequest::ofertaId))
                .toList();

        for (ItemPedidoRequest item : itemsOrdenados) {
            OfertaEntity oferta = bloquearOferta(item.ofertaId());
            oferta.descontar(item.cantidad(), hoy);
            pedido.agregarItem(oferta, item.cantidad());
        }

        PedidoEntity guardado = repository.save(pedido);
        for (ItemPedidoEntity item : guardado.getItems()) {
            movimientoRepository.save(MovimientoStockEntity.descuento(item.getOferta(), guardado.getId(), item.getCantidad()));
        }
        return PedidoResponse.desde(guardado);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) {
        return PedidoResponse.desde(obtener(id));
    }

    @Transactional
    public PedidoResponse cambiarEstado(Long id, CambiarEstadoPedidoRequest request){
        PedidoEntity pedido = obtener(id);
        if(request.estado() == EstadoPedido.CONFIRMADO){
            pedido.confirmar();
        } else{
            pedido.cancelar();
            pedido.getItems().forEach(this::devolverStock);
        }
        return PedidoResponse.desde(pedido);
    }

    @Transactional
    public void retirarItem(Long itemId){
        ItemPedidoEntity item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el item " + itemId));
        item.getPedido().retirarItem(item);
        devolverStock(item);
    }

    private PedidoEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pedido " + id));
    }

    private void devolverStock(ItemPedidoEntity item){
        OfertaEntity oferta = bloquearOferta(item.getOferta().getId());
        oferta.devolver(item.getCantidad());
        movimientoRepository.save(MovimientoStockEntity.devolucion(oferta, item.getPedido().getId(), item.getCantidad()));
    }

    private OfertaEntity bloquearOferta(Long ofertaId) {
        return ofertaRepository.buscarParaActualizar(ofertaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la oferta " + ofertaId));
    }
}
