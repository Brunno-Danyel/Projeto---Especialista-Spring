package application.service;

import application.api.dto.PedidoDTO;
import application.domain.entities.Pedido;

public interface PedidoService {

    Pedido save(PedidoDTO dto);

}
