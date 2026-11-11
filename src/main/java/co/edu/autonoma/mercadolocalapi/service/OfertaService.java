package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.dto.response.OfertaResponse;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.edu.autonoma.mercadolocalapi.repository.ProductorRepository;
import co.edu.autonoma.mercadolocalapi.dto.response.MovimientoStockResponse;
import co.edu.autonoma.mercadolocalapi.repository.MovimientoStockRepository;
import java.time.LocalDate;
import java.util.List;
import co.edu.autonoma.mercadolocalapi.dto.request.CrearOfertaRequest;
import co.edu.autonoma.mercadolocalapi.entity.MovimientoStockEntity;
import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OfertaService {

    private final OfertaRepository repository;
    private final ProductorRepository productorRepository;
    private final MovimientoStockRepository movimientoRepository;

    public List<OfertaResponse> listarVigentes() {
        return repository
                .findByEstadoAndVigenteHastaGreaterThanEqual(EstadoOferta.ACTIVA, LocalDate.now())
                .stream()
                .map(OfertaResponse::desde)
                .toList();
    }

    public OfertaResponse buscar(Long id) {
        return OfertaResponse.desde(obtener(id));
    }

    public List<OfertaResponse> listarPorProductor(Long productorId) {
        if (!productorRepository.existsById(productorId)) {
            throw new RecursoNoEncontradoException("No existe el productor " + productorId);
        }
        return repository.findByProductorIdOrderByIdAsc(productorId).stream()
                .map(OfertaResponse::desde)
                .toList();
    }

    public List<MovimientoStockResponse> listarMovimientos(Long id) {
        obtener(id);
        return movimientoRepository.findByOfertaIdOrderByIdAsc(id).stream()
                .map(MovimientoStockResponse::desde)
                .toList();
    }

    @Transactional
    public OfertaResponse publicar(CrearOfertaRequest request) {
        ProductorEntity productor = productorRepository.findById(request.productorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el productor " + request.productorId()));
        if (!productor.isActivo()) {
            throw new ReglaNegocioException(
                    "El productor " + productor.getId() + " está inactivo y no puede publicar ofertas");
        }
        OfertaEntity oferta = new OfertaEntity(productor, request.producto(), request.precioUnitario(),
                request.stock(), request.vigenteHasta());
        OfertaEntity guardada = repository.save(oferta);
        movimientoRepository.save(MovimientoStockEntity.publicacion(guardada));
        return OfertaResponse.desde(guardada);
    }

    private OfertaEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la oferta " + id));
    }
}