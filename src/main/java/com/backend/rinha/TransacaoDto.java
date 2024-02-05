package com.backend.rinha;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class TransacaoDto implements Serializable {

    @NotNull
    private Long valor;

    @NotNull
    private String tipo;

    @NotNull
    @Size(max = 10)
    private String descricao;

    public TransacaoDto(Long valor, String tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
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
}
