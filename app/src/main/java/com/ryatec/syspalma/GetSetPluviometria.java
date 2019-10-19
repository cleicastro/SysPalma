package com.ryatec.syspalma;

public class GetSetPluviometria {

    // r_realizado_apontamento
    private String data;
    private Double quantidade;
    private String fazenda;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getFazenda() {
        return fazenda;
    }

    public void setFazenda(String fazenda) {
        this.fazenda = fazenda;
    }
}
