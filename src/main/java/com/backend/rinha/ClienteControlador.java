package com.backend.rinha;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @GetMapping("{id}/extrato")
    public ResponseEntity<ExtratoDto> obtemExtrato(@PathVariable("id") Integer idCliente) {

        Cliente clienteBD = obtemCliente(idCliente);

        if(clienteBD == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        SaldoDto saldoDto = new SaldoDto(clienteBD.getSaldo(), clienteBD.getLimite());

        ExtratoDto extratoDto = new ExtratoDto(saldoDto, obtemTransacoes(clienteBD.getId()));
        return new ResponseEntity<>(extratoDto, HttpStatus.OK);
    }

    private List<TransacaoDto> obtemTransacoes(Integer idCliente) {


        Query query = entityManager.createQuery("select (valor, tipo, descricao, realizadaEm) from transacao where idCliente=:valor order by realizadaEm DESC limit 10");
        query.setParameter("valor", idCliente);

        List<TransacaoDto> list = new ArrayList<>();

        List<Object[]> resultList = query.getResultList();

        for(Object transacao[] : resultList) {
            list.add(new TransacaoDto((Long) transacao[0], (String) transacao[1],
                    (String) transacao[2], (ZonedDateTime) transacao[3]));
        }

        return list;
    }


    private Cliente obtemCliente(Integer id) {

        if(id == null) return null;

        Query query = entityManager.createQuery("select (id, limite, saldo) from cliente where id=:value");
        query.setParameter("value", id);

        List<Object[]> resultList = query.getResultList();

        if(resultList.isEmpty()) return null;

        return new Cliente((Integer)resultList.getFirst()[0], (Long) resultList.getFirst()[1], (Long) resultList.getFirst()[2]);
    }
}
