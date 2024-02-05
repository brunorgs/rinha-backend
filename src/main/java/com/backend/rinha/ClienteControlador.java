package com.backend.rinha;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
public class ClienteControlador {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("{id}/transacoes")
    @Transactional
    public ResponseEntity<ClienteDto> criaTransacao(@PathVariable("id") Integer idCliente,
                                                    @RequestBody @Valid TransacaoDto transacaoDto){

        Cliente clienteBD = obtemCliente(idCliente);

        if(clienteBD == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Transacao transacao = transacaoDto.paraEntidade();
        transacao.setIdCliente(clienteBD.getId());

        long novoSaldo = clienteBD.getSaldo() - transacao.getValor();

        if(Math.abs(novoSaldo) - clienteBD.getLimite() > 0 &&
                transacaoDto.getTipo().equalsIgnoreCase("d")) return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        entityManager.persist(transacao);

        Query query = entityManager.createQuery("UPDATE cliente SET saldo=:valor WHERE id=:idCliente");
        query.setParameter("valor", novoSaldo);
        query.setParameter("idCliente", clienteBD.getId());

        int executed = query.executeUpdate();

        return new ResponseEntity<>(new ClienteDto(clienteBD.getLimite(), novoSaldo), HttpStatus.OK);
    }

    private Cliente obtemCliente(Integer id) {

        if(id == null) return null;

        Query query = entityManager.createQuery("select (id, limite, saldo) from cliente where id=:value");
        query.setParameter("value", id);

        List<Object[]> resultList = query.getResultList();
        return new Cliente((Integer)resultList.getFirst()[0], (Long) resultList.getFirst()[1], (Long) resultList.getFirst()[2]);
    }
}
