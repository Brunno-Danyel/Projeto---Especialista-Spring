package application.api.controller;

import application.domain.entities.Client;
import application.domain.repositories.ClientRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClientController {

    @Autowired
    private ClientRepository repository;

    @GetMapping("/id/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente Encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado com o ID informado")
    })
    public Client findByClientId(@PathVariable @ApiParam("Id do cliente") Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    /*@GetMapping
    public ResponseEntity<List<Client>> findAllClient() {
        List listClient = repository.findAll();
        return ResponseEntity.ok().body(listClient);
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Client addClient(@RequestBody @Valid Client client) {
        return repository.save(client);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClient(@PathVariable Integer id) {
        repository.findById(id)
                .map(client -> {
                    repository.delete(client);
                    return client;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Client client, @PathVariable Integer id) {

        repository.findById(id).map(clientExist -> {
            client.setId(id);
            repository.save(client);
            return clientExist;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

    }

    @GetMapping
    public List<Client> find(Client client) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(client, matcher);
        return repository.findAll(example);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Client> findByCpf(@PathVariable String cpf) {
        Client client = repository.findByCpf(cpf);
        return ResponseEntity.ok().body(client);
    }


}

