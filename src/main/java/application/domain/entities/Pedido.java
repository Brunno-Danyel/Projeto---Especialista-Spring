package application.domain.entities;

import application.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Client client;

    private LocalDate dataPedido;

    @Column(scale = 2, precision = 20)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;


    public void cancelar() {
        if (podeSerfinalizada()) {
            setStatus(StatusPedido.CANCELADO);
        }
    }
    public boolean podeSerfinalizada() {
        return StatusPedido.REALIZADO.equals(getStatus());
    }
}
