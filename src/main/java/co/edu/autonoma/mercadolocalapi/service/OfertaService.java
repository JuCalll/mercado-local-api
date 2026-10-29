package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.dto.response.OfertaResponse;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Optional<OfertaResponse> buscar(Long id) {
        return repository.findById(id)
                .map(OfertaResponse::desde);
    }
}