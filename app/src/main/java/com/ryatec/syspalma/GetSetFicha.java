package com.ryatec.syspalma;

public class GetSetFicha {

    public String getFichaRealizado() {
        return fichaRealizado;
    }

    private String fichaRealizado;
    private String data_realizado;
    private String matricula_responsavel_tecnico;
    private String matricula_responsavel_operacional;
    private Integer id_planejado;

    public Integer getId_planejado() {
        return id_planejado;
    }

    public void setId_planejado(Integer id_planejado) {
        this.id_planejado = id_planejado;
    }

    public String getData_realizado() {
        return data_realizado;
    }

    public void setData_realizado(String data_realizado) {
        this.data_realizado = data_realizado;
    }

    public void setFichaRealizado(String fichaRealizado) {
        this.fichaRealizado = fichaRealizado;
    }

    public String getMatricula_responsavel_tecnico() {
        return matricula_responsavel_tecnico;
    }

    public void setMatricula_responsavel_tecnico(String matricula_responsavel_tecnico) {
        this.matricula_responsavel_tecnico = matricula_responsavel_tecnico;
    }

    public String getMatricula_responsavel_operacional() {
        return matricula_responsavel_operacional;
    }

    public void setMatricula_responsavel_operacional(String matricula_responsavel_operacional) {
        this.matricula_responsavel_operacional = matricula_responsavel_operacional;
    }
}
