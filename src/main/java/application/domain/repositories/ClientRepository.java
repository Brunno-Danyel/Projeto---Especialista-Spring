package application.domain.repositories;

import application.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository <Client, Integer> {


    List<Client> findByNomeLike(String nome);

    List<Client> findByNomeOrIdOrderById(String nome, Integer id);

    Client findByCpf(String cpf);

    @Query("select c from Client c left join fetch c.pedidos where c.id =:id ")
    Client findClientFetchPedidos(@Param("id") Integer id);

}
