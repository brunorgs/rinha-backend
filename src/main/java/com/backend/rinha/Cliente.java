package com.backend.rinha;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "cliente")
public class Cliente {

    @Id
    private Integer id;

    private Long limite;

    private Long saldo;

    public Cliente() {}

    public Cliente(Integer id, Long limite, Long saldo) {
        this.id = id;
        this.limite = limite;
        this.saldo = saldo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }
}
