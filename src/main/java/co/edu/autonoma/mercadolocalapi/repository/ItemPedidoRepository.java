package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {

            boolean existsByOfertaId(Long ofertaId);
}
