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
import java.util.Arrays;
import java.util.List;

@Path("clientes")
public class ClienteControlador {

    @PersistenceContext
    private EntityManager entityManager;

    private static final List<String> tipos = Arrays.asList("c", "d");

    @POST
    @Path("{id}/transacoes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<ClienteDto> criaTransacao(@PathParam("id") Integer idCliente, @Valid TransacaoDto transacaoDto){

        if(!tipos.contains(transacaoDto.getTipo())) return RestResponse.status(422);

        Query nativeQuery = entityManager.createNativeQuery("select * from cria_transacao(:id, :valor, :tipo, :descricao)");
        nativeQuery.setParameter("id", idCliente);
        nativeQuery.setParameter("valor", transacaoDto.getValor());
        nativeQuery.setParameter("tipo", transacaoDto.getTipo());
        nativeQuery.setParameter("descricao", transacaoDto.getDescricao());

        List<Object[]> singleResult = nativeQuery.getResultList();

        ClienteDto clienteDto = new ClienteDto((Integer) singleResult.getFirst()[0], (Integer) singleResult.getFirst()[1]);

        if(clienteDto.limite() == -1) return RestResponse.notFound();
        if(clienteDto.limite() == -2) return RestResponse.status(422);

        return RestResponse.ok(clienteDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/extrato")
    public RestResponse<ExtratoDto> obtemExtrato(@PathParam("id") Integer idCliente) {

        if(idCliente == null) return RestResponse.notFound();

        Cliente clienteBD = entityManager.find(Cliente.class, idCliente);

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
            list.add(new TransacaoDto((Integer) transacao[0], (String) transacao[1],
                    (String) transacao[2], (ZonedDateTime) transacao[3]));
        }

        return list;
    }
}
