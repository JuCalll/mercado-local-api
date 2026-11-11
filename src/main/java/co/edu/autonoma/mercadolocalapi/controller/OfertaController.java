package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.dto.response.OfertaResponse;
import co.edu.autonoma.mercadolocalapi.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.autonoma.mercadolocalapi.dto.response.MovimientoStockResponse;
import co.edu.autonoma.mercadolocalapi.dto.request.CrearOfertaRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;
import java.util.List;
import co.edu.autonoma.mercadolocalapi.dto.request.CambiarEstadoOfertaRequest;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/ofertas")
@RequiredArgsConstructor
public class OfertaController {

    private final OfertaService service;

    @GetMapping
    public List<OfertaResponse> listar() {
        return service.listarVigentes();
    }

    @GetMapping("/{id}")
    public OfertaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoStockResponse> movimientos(@PathVariable Long id) {
        return service.listarMovimientos(id);
    }

    @PostMapping
    public ResponseEntity<OfertaResponse> publicar(@Valid @RequestBody CrearOfertaRequest request) {
        OfertaResponse creada = service.publicar(request);
        return ResponseEntity.created(URI.create("/api/ofertas/" + creada.id())).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public OfertaResponse cambiarEstado(@PathVariable Long id,
            @Valid @RequestBody CambiarEstadoOfertaRequest request) {
        return service.cambiarEstado(id, request);
    }
}