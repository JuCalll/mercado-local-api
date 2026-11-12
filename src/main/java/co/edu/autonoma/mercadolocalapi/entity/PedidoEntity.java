package co.edu.autonoma.mercadolocalapi.entity;

import co.edu.autonoma.mercadolocalapi.domain.EstadoPedido;
import co.edu.autonoma.mercadolocalapi.exception.ReglaNegocioException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity 
@Table(name = "pedidos", schema = "ventas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    private CompradorEntity comprador;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    private EstadoPedido estado;

    @Column (nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Column (name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    @Column (name = "confirmado_en")
    private OffsetDateTime confirmadoEn;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<ItemPedidoEntity> items = new ArrayList<>();

    public PedidoEntity(CompradorEntity comprador){
        this.comprador = comprador;
        this.estado = EstadoPedido.PENDIENTE;
        this.total = BigDecimal.ZERO;
        this.creadoEn = OffsetDateTime.now();
    }

    public void agregarItem(OfertaEntity oferta, int cantidad){
        items.add(new ItemPedidoEntity(this, oferta, cantidad));
        recalcularTotal();
    }

    public void retirarItem(ItemPedidoEntity item){
        exigirPendiente();
        items.remove(item);
        recalcularTotal();
    }

    public void confirmar(){
        exigirPendiente();
        if(items.isEmpty()){
            throw new ReglaNegocioException("El pedido " + id + " no tiene ítems y no se puede confirmar");
        }
        this.estado = EstadoPedido.CONFIRMADO;
        this.confirmadoEn = OffsetDateTime.now();
    }

    public void cancelar(){
        exigirPendiente();
        this.estado = EstadoPedido.CANCELADO;
    }

    public boolean estaConfirmado(){
        return estado == EstadoPedido.CONFIRMADO;
    }

    private void exigirPendiente(){
        if(estado != EstadoPedido.PENDIENTE){
            throw new ReglaNegocioException("El pedido " + id + " está " + estado + " y ya no se puede modificar");
        }
    }

    private void recalcularTotal(){
        this.total = items.stream()
                .map(ItemPedidoEntity::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
