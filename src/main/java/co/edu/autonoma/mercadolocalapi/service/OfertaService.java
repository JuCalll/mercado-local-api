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

    private OfertaEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la oferta " + id));
    }
}