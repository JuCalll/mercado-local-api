package co.edu.autonoma.mercadolocalapi.service;

import co.edu.autonoma.mercadolocalapi.dto.request.CrearCompradorRequest;
import co.edu.autonoma.mercadolocalapi.dto.response.CompradorResponse;
import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;
import co.edu.autonoma.mercadolocalapi.repository.CompradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
@RequiredArgsConstructor 

public class CompradorService {

    private final CompradorRepository repository;

    @Transactional
    public CompradorResponse registrar(CrearCompradorRequest request){
        if(repository.existsByCorreo(request.correo())){
            throw new ReglaNegocioException("Ya existe un comprador con el correo: " + request.correo());
        }
        CompradorEntity comprador = new CompradorEntity(request.nombre(), request.correo(), request.telefono());
        return CompradorResponse.desde(repository.save(comprador));
    }

}
