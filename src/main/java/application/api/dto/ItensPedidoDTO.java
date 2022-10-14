package application.api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItensPedidoDTO {

    private Integer produto;
    private Integer quantidade;
}
