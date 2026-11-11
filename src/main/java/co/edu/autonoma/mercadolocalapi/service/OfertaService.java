package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.dto.response.OfertaResponse;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfertaService {

    private final OfertaRepository repository;

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

    private OfertaEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la oferta " + id));
    }
}