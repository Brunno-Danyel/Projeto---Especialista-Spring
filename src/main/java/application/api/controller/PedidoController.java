package application.api.controller;

import application.api.dto.InformacaoItemPedidoDTo;
import application.api.dto.InformacaoPedidoDTO;
import application.api.dto.PedidoDTO;
import application.domain.entities.ItemPedido;
import application.domain.entities.Pedido;
import application.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer addPedido(@RequestBody PedidoDTO dto) {
        Pedido pedido = service.save(dto);
        return pedido.getId();
    }

    @GetMapping("id/{id}")
    public InformacaoPedidoDTO informacaoPedidoDTO(@PathVariable Integer id) {
        return service.findPedido(id).map(pedido -> converter(pedido))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado"));
    }

    public InformacaoPedidoDTO converter(Pedido pedido) {
     return InformacaoPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getClient().getCpf())
                .nomeCliente(pedido.getClient().getNome())
                .total(pedido.getTotal())
                .itens(converter(pedido.getItens()))
                .build();

    }

    public List<InformacaoItemPedidoDTo> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacaoItemPedidoDTo
                        .builder()
                        .descricao(item.getProduct().getDescricao())
                        .precoUnitario(item.getProduct().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
