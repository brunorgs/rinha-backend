package com.backend.rinha;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TransacaoDto implements Serializable {

    @NotNull
    @Positive
    private Long valor;

    @NotEmpty
    private String tipo;

    @NotEmpty
    @Size(max = 10)
    private String descricao;

    private ZonedDateTime realizada_em;

    public TransacaoDto(Long valor, String tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public TransacaoDto(Long valor, String tipo, String descricao, ZonedDateTime realizadaEm) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizada_em = realizadaEm;
    }

    public TransacaoDto() {
    }

    public Transacao paraEntidade() {
        return new Transacao(valor, tipo, descricao);
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ZonedDateTime getRealizada_em() {
        return realizada_em;
    }

    public void setRealizada_em(ZonedDateTime realizada_em) {
        this.realizada_em = realizada_em;
    }
}
