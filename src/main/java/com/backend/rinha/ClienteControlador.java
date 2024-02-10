package com.backend.rinha;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("clientes")
public class ClienteControlador {

    @PersistenceContext
    private EntityManager entityManager;

    @POST
    @Path("{id}/transacoes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<ClienteDto> criaTransacao(@PathParam("id") Integer idCliente, @Valid TransacaoDto transacaoDto){

        Cliente clienteBD = obtemCliente(idCliente);

        if(clienteBD == null) return RestResponse.notFound();

        Transacao transacao = transacaoDto.paraEntidade();
        transacao.setIdCliente(clienteBD.getId());

        Long valor;

        if(transacaoDto.getTipo().equalsIgnoreCase("d")) {
            valor = transacao.getValor() * -1;
        } else {
            valor = transacao.getValor();
        }

        long novoSaldo = clienteBD.getSaldo() + valor;

        if(Math.abs(novoSaldo) - clienteBD.getLimite() > 0 &&
                transacaoDto.getTipo().equalsIgnoreCase("d")) return RestResponse.status(422);

        entityManager.persist(transacao);

        Query query = entityManager.createQuery("UPDATE cliente SET saldo=:valor WHERE id=:idCliente");
        query.setParameter("valor", novoSaldo);
        query.setParameter("idCliente", clienteBD.getId());

        int executed = query.executeUpdate();

        return RestResponse.ok(new ClienteDto(clienteBD.getLimite(), novoSaldo));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/extrato")
    public RestResponse<ExtratoDto> obtemExtrato(@PathParam("id") Integer idCliente) {

        Cliente clienteBD = obtemCliente(idCliente);

        if(clienteBD == null) return RestResponse.notFound();

        SaldoDto saldoDto = new SaldoDto(clienteBD.getSaldo(), clienteBD.getLimite());

        ExtratoDto extratoDto = new ExtratoDto(saldoDto, obtemTransacoes(clienteBD.getId()));
        return RestResponse.ok(extratoDto);
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
