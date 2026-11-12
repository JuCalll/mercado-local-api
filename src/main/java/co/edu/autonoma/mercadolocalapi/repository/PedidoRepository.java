package co.edu.autonoma.mercadolocalapi.repository;

import co.edu.autonoma.mercadolocalapi.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("""
        select distinct p from PedidoEntity p
        join p.items i
        where i.oferta.productor.id = :productorId
        order by p.creadoEn desc
    """)
    List<PedidoEntity> buscarConOfertasDelProductor(@Param("productorId") Long productorId);
    List<PedidoEntity> findByCompradorIdOrderByCreadoEnDesc(Long compradoId);
}
