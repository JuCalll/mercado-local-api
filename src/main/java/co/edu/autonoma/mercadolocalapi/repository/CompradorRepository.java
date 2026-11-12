package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompradorRepository extends JpaRepository<CompradorEntity, Long> {

    boolean existsByCorreo(String correo);

}
