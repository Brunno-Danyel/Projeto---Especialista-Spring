package application.domain.repositories;


import application.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByDescricao(String descricao);

    List<Product> findByPreco(BigDecimal preco);
}
