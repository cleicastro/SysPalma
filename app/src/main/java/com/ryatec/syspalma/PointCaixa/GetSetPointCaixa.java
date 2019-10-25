package com.ryatec.syspalma.PointCaixa;

public class GetSetPointCaixa {

    private String data;
    private String placa;
    private Integer caixa;
    private String local;
    private String matricula;
    private Double latitude;
    private Double longitude;
    private String Id_apontamento;
    private Integer Id_patrimonio;

    public Integer getId_patrimonio() {
        return Id_patrimonio;
    }

    public void setId_patrimonio(Integer id_patrimonio) {
        Id_patrimonio = id_patrimonio;
    }

    public String getId_apontamento() {
        return Id_apontamento;
    }

    public void setId_apontamento(String id_apontamento) {
        Id_apontamento = id_apontamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getCaixa() {
        return caixa;
    }

    public void setCaixa(Integer caixa) {
        this.caixa = caixa;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
