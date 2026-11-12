package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items-pedido")
@RequiredArgsConstructor

public class ItemPedidoController {

    private final PedidoService service;

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> retirar(@PathVariable Long id) {
        service.retirarItem(id);
        return ResponseEntity.noContent().build();
    }
}
