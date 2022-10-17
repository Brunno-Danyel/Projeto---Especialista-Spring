package application.api.controller;

import application.api.dto.InformacaoItemPedidoDTo;
import application.api.dto.InformacaoPedidoDTO;
import application.api.dto.PedidoDTO;
import application.domain.entities.ItemPedido;
import application.domain.entities.Pedido;
import application.service.PedidoService;
import application.service.impl.PedidoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {



    @Autowired
    private PedidoService service;

    @Autowired
    PedidoServiceImpl service2;

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer addPedido(@RequestBody @Valid PedidoDTO dto) {
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
                .status(pedido.getStatus().name())
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

    @PutMapping("id/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id){
        service2.cancelarPedido(id);

    }
}
