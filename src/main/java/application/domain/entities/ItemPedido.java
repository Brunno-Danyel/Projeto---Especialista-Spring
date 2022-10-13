package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Product product;

    private Integer quantidade;
}
