package com.ryatec.syspalma;

public class GetSetEPI {

    // r_realizado_apontamento
    private String data;
    private String requisicao;
    private String matricula;
    private Integer id_insumo;
    private Integer quantidade;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(Integer id_insumo) {
        this.id_insumo = id_insumo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
