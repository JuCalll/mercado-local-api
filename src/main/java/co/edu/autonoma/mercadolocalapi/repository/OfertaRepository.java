package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.domain.EstadoOferta;
import co.edu.autonoma.mercadolocalapi.entity.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {

    List<OfertaEntity> findByEstadoAndVigenteHastaGreaterThanEqual(EstadoOferta estado, LocalDate fecha);

    List<OfertaEntity> findByProductorIdOrderByIdAsc(Long productorId);

    @Lock (LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OfertaEntity o WHERE o.id = :id")
    Optional<OfertaEntity> buscarParaActualizar(@Param("id") Long id);
}