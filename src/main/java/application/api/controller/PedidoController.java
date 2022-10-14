package application.api.controller;

import application.api.dto.PedidoDTO;
import application.domain.entities.Pedido;
import application.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer addPedido(@RequestBody PedidoDTO dto){
       Pedido pedido = service.save(dto);
       return pedido.getId();
    }

}
