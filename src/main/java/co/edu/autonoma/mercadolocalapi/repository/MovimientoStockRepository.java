package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.entity.MovimientoStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStockEntity, Long> {

    List<MovimientoStockEntity> findByOfertaIdOrderByIdAsc(Long ofertaId);
}