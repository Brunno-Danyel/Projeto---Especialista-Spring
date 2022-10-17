package application.api.dto;

import application.validation.NotEmptyList;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "Informe o código do cliente" )
    private Integer cliente;

    @NotNull(message = "Campo TOTAL do pedido obrigatório")
    private BigDecimal total;

    @NotEmptyList(message = "Pedido não pode ser realizado sem itens. Adicione alguns itens para prosseguir")
    private List<ItensPedidoDTO> items;

}
