package application.service;

import application.api.dto.PedidoDTO;
import application.domain.entities.Pedido;

import java.util.Optional;

public interface PedidoService {

    Pedido save(PedidoDTO dto);

    Optional<Pedido> findPedido(Integer id);



}
