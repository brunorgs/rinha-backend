package com.backend.rinha;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;

@Entity(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue
    private Integer id;

    private Long valor;

    private String tipo;

    private String descricao;

    @Column(name = "realizada_em")
    private ZonedDateTime realizadaEm;

    @Column(name = "id_cliente")
    private Integer idCliente;

    public Transacao() {
    }

    public Transacao(Long valor, String tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = ZonedDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ZonedDateTime getRealizadaEm() {
        return realizadaEm;
    }

    public void setRealizadaEm(ZonedDateTime realizadaEm) {
        this.realizadaEm = realizadaEm;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}
