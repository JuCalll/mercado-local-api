package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {

    List<OfertaEntity> findByEstadoAndVigenteHastaGreaterThanEqual(EstadoOferta estado, LocalDate fecha);
}