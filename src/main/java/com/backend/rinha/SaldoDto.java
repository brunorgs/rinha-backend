package com.backend.rinha;

import java.time.ZonedDateTime;

public class SaldoDto {
    private Long total;
    private ZonedDateTime data_extrato = ZonedDateTime.now();
    private Long limite;

    public SaldoDto(Long total, Long limite) {
        this.total = total;
        this.limite = limite;
    }

    public SaldoDto() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public ZonedDateTime getData_extrato() {
        return data_extrato;
    }

    public void setData_extrato(ZonedDateTime data_extrato) {
        this.data_extrato = data_extrato;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }
}
