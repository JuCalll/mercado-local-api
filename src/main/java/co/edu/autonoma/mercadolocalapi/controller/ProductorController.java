package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.dto.response.ProductorResponse;
import co.edu.autonoma.mercadolocalapi.service.ProductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.autonoma.mercadolocalapi.dto.request.CrearProductorRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/api/productores")
@RequiredArgsConstructor
public class ProductorController {

    private final ProductorService service;

    @GetMapping
    public List<ProductorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProductorResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
    
    @PostMapping
    public ResponseEntity<ProductorResponse> registrar(@Valid @RequestBody CrearProductorRequest request) {
        ProductorResponse creado = service.registrar(request);
        return ResponseEntity.created(URI.create("/api/productores/" + creado.id())).body(creado);
    }
}