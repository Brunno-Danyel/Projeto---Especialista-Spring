package application.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoItemPedidoDTo {

    private String descricao;
    private BigDecimal precoUnitario;
    private Integer quantidade;
}
