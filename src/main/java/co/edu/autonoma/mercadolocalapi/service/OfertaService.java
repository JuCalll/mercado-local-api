package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.dto.response.OfertaResponse;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OfertaService {

    private final OfertaRepository repository;

    public OfertaService(OfertaRepository repository) {
        this.repository = repository;
    }

    public List<OfertaResponse> listarVigentes() {
        return repository
                .findByEstadoAndVigenteHastaGreaterThanEqual(EstadoOferta.ACTIVA, LocalDate.now())
                .stream()
                .map(this::aResponse)
                .toList();
    }

    private OfertaResponse aResponse(OfertaEntity oferta) {
        return new OfertaResponse(
                oferta.getId(),
                oferta.getProducto(),
                oferta.getPrecioUnitario(),
                oferta.getStock(),
                oferta.getVigenteHasta(),
                oferta.getEstado()
        );
    }
    
}
