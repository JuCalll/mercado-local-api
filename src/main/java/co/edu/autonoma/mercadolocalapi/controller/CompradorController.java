package co.edu.autonoma.mercadolocalapi.controller;

import co.edu.autonoma.mercadolocalapi.dto.request.CrearCompradorRequest;
import co.edu.autonoma.mercadolocalapi.dto.response.CompradorResponse;
import co.edu.autonoma.mercadolocalapi.service.CompradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@RestController
@RequestMapping ("/api/compradores")
@RequiredArgsConstructor

public class CompradorController {

    private final CompradorService service;

    @PostMapping
    public ResponseEntity<CompradorResponse> registrar(@RequestBody @Valid CrearCompradorRequest request){
        CompradorResponse creado = service.registrar(request);
        return ResponseEntity.created(URI.create("/api/compradores/" + creado.id())).body(creado);
    }

    @GetMapping("/{id}")
    public CompradorResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }
    

}
