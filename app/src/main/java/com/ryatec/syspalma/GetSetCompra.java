package com.ryatec.syspalma;

public class GetSetCompra {

    // r_realizado_apontamento
    private String data;
    private String hora;
    private String token;
    private Integer id_fornecedor;
    private Integer id_patrimonio;
    private String km;
    private Double area_realizada;
    private String cupom_fiscal;
    private String id_mdo;
    private String mdo_motorista;
    private Integer id_insumo;
    private Double quantidade;
    private Double valor_unit;
    private String cod;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(Integer id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public Integer getId_patrimonio() {
        return id_patrimonio;
    }

    public void setId_patrimonio(Integer id_patrimonio) {
        this.id_patrimonio = id_patrimonio;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public Double getArea_realizada() {
        return area_realizada;
    }

    public void setArea_realizada(Double area_realizada) {
        this.area_realizada = area_realizada;
    }

    public String getCupom_fiscal() {
        return cupom_fiscal;
    }

    public void setCupom_fiscal(String cupom_fiscal) {
        this.cupom_fiscal = cupom_fiscal;
    }

    public String getId_mdo() {
        return id_mdo;
    }

    public void setId_mdo(String id_mdo) {
        this.id_mdo = id_mdo;
    }

    public Integer getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(Integer id_insumo) {
        this.id_insumo = id_insumo;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(Double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public String getMdo_motorista() {
        return mdo_motorista;
    }

    public void setMdo_motorista(String mdo_motorista) {
        this.mdo_motorista = mdo_motorista;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
}
