package co.edu.autonoma.mercadolocalapi.service;

import org.springframework.stereotype.Service;
import co.edu.autonoma.mercadolocalapi.dto.EstadoResponse;

@Service
public class EstadoService {

    public EstadoResponse consultar() {
        return new EstadoResponse(
                "mercado-local-api",
                "disponible"
        );
    }
}