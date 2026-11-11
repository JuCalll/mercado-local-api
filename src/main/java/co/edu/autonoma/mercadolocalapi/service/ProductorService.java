package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.dto.response.ProductorResponse;
import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.repository.ProductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductorService {

    private final ProductorRepository repository;

    public List<ProductorResponse> listar() {
        return repository.findAllByOrderByIdAsc().stream()
                .map(ProductorResponse::desde)
                .toList();
    }

    public ProductorResponse buscar(Long id) {
        return ProductorResponse.desde(obtener(id));
    }

    private ProductorEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el productor " + id));
    }
}