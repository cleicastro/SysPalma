package com.ryatec.syspalma.AGFIndustria;

public class GetSetProgCaixaAGF {

    // r_realizado_apontamento
    private String data;
    private String cod;
    private String clifor;
    private Integer caixa_i;
    private Double tonelada;
    private Integer cachos;
    private String romaneio;
    private String caixa_cheia;
    private String responsavel;

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getClifor() {
        return clifor;
    }

    public void setClifor(String clifor) {
        this.clifor = clifor;
    }

    public Integer getCaixa_i() {
        return caixa_i;
    }

    public void setCaixa_i(Integer caixa_i) {
        this.caixa_i = caixa_i;
    }

    public Double getTonelada() {
        return tonelada;
    }

    public void setTonelada(Double tonelada) {
        this.tonelada = tonelada;
    }

    public Integer getCachos() {
        return cachos;
    }

    public void setCachos(Integer cachos) {
        this.cachos = cachos;
    }

    public String getRomaneio() {
        return romaneio;
    }

    public void setRomaneio(String romaneio) {
        this.romaneio = romaneio;
    }

    public String getCaixa_cheia() {
        return caixa_cheia;
    }

    public void setCaixa_cheia(String caixa_cheia) {
        this.caixa_cheia = caixa_cheia;
    }
}
