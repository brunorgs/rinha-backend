package com.backend.rinha;

import java.time.ZonedDateTime;

public class SaldoDto {
    private Integer total;
    private ZonedDateTime data_extrato = ZonedDateTime.now();
    private Integer limite;

    public SaldoDto(Integer total, Integer limite) {
        this.total = total;
        this.limite = limite;
    }

    public SaldoDto() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ZonedDateTime getData_extrato() {
        return data_extrato;
    }

    public void setData_extrato(ZonedDateTime data_extrato) {
        this.data_extrato = data_extrato;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }
}
