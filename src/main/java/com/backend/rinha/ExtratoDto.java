package com.backend.rinha;

import java.io.Serializable;
import java.util.List;

public class ExtratoDto implements Serializable {

    private SaldoDto saldo;

    private List<TransacaoDto> ultimas_transacoes;

    public ExtratoDto(SaldoDto saldo, List<TransacaoDto> ultimas_transacoes) {
        this.saldo = saldo;
        this.ultimas_transacoes = ultimas_transacoes;
    }

    public SaldoDto getSaldo() {
        return saldo;
    }

    public List<TransacaoDto> getUltimas_transacoes() {
        return ultimas_transacoes;
    }
}
