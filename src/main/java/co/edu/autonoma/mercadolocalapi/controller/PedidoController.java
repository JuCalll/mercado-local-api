package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.dto.request.CrearPedidoRequest;
import co.edu.autonoma.mercadolocalapi.dto.response.PedidoResponse;
import co.edu.autonoma.mercadolocalapi.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import co.edu.autonoma.mercadolocalapi.dto.request.CambiarEstadoPedidoRequest;
import org.springframework.web.bind.annotation.PatchMapping;

import java.net.URI;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody CrearPedidoRequest request) {
        PedidoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.id())).body(creado);
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoPedidoRequest request) {
        return service.cambiarEstado(id, request);
    }
}
