package application.api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsPedidoDTO {

    private Integer produto;
    private Integer quantidade;
}
