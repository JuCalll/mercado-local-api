package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.dto.response.ProductorResponse;
import co.edu.autonoma.mercadolocalapi.service.ProductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}