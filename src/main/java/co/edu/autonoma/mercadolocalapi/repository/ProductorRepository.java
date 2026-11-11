package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductorRepository extends JpaRepository<ProductorEntity, Long> {
    List<ProductorEntity> findAllByOrderByIdAsc();
}