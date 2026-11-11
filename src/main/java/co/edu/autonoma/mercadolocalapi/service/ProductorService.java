package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.dto.response.ProductorResponse;
import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;
import co.edu.autonoma.mercadolocalapi.exception.RecursoNoEncontradoException;
import co.edu.autonoma.mercadolocalapi.repository.ProductorRepository;
import co.edu.autonoma.mercadolocalapi.dto.request.CrearProductorRequest;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public ProductorResponse registrar(CrearProductorRequest request) {
        if (repository.existsByCorreo(request.correo())) {
            throw new ReglaNegocioException("Ya existe un productor con el correo " + request.correo());
        }
        ProductorEntity productor = new ProductorEntity(request.nombre(), request.correo(),
                request.telefono(), request.municipio());
        return ProductorResponse.desde(repository.save(productor));
    }

    private ProductorEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el productor " + id));
    }
}