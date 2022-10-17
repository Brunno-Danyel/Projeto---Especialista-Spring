package application.api.controller;

import application.domain.entities.Product;
import application.domain.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @PostMapping
    @ResponseStatus(CREATED)
    public Product saveProduct(@RequestBody @Valid Product product) {
        return repository.save(product);
    }

    /*@GetMapping
    public ResponseEntity<List<Product>> findAllProduct() {
        List products = repository.findAll();
        return ResponseEntity.ok().body(products);
    }*/

    @GetMapping("id/{id}")
    public Product findByProductId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping("descricao/{descricao}")
    public ResponseEntity<List<Product>> findByDescricao(@PathVariable String descricao) {
        List products = repository.findByDescricao(descricao);
        return ResponseEntity.ok().body(products);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void removeProduct(@PathVariable Integer id) {
        repository.findById(id)
                .map(product -> {
                    repository.delete(product);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateProduct(@RequestBody @Valid Product product, @PathVariable Integer id){
        repository.findById(id).map(productExist ->{
            product.setId(id);
            repository.save(product);
            return product;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping
    public List<Product> find(Product filtro ){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING );

        Example example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }

}

