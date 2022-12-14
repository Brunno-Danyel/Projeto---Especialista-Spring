package application.domain.entities.service.impl;

import application.api.dto.ItensPedidoDTO;
import application.api.dto.PedidoDTO;
import application.domain.entities.Client;
import application.domain.entities.ItemPedido;
import application.domain.entities.Pedido;
import application.domain.entities.Product;
import application.domain.enums.StatusPedido;
import application.domain.repositories.ClientRepository;
import application.domain.repositories.ItemPedidoRepository;
import application.domain.repositories.PedidoRepository;
import application.domain.repositories.ProductRepository;
import application.exeption.PedidoNaoEncontradoException;
import application.exeption.RegraNegocioException;
import application.domain.entities.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido save(PedidoDTO dto) {
        Integer idClient = dto.getCliente();
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new RegraNegocioException("Codigo de cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setClient(client);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedidos = convertItens(pedido, dto.getItems());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> findPedido(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    private List<ItemPedido> convertItens(Pedido pedido, List<ItensPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("N??o ?? possivel realizar um pedido sem itens.");
        }
        return itens.stream().map(dto -> {
            Integer idProduto = dto.getProduto();
            Product product = productRepository.findById(idProduto).orElseThrow(() ->
                    new RegraNegocioException("Codigo de produto invalido: " + idProduto));
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduct(product);
            return itemPedido;
        }).collect(Collectors.toList());

    }


    public Pedido buscarPedido(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException());

    }

    @Transactional
    public void cancelarPedido(Integer id) {
        Pedido pedido = buscarPedido(id);

        pedido.cancelar();

        pedidoRepository.save(pedido);
    }


}
