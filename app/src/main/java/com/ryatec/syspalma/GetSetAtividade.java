package com.ryatec.syspalma;

public class GetSetAtividade {

    String id_ficha;

    // r_realizado_apontamento
    private Integer id_parcela;
    private String atividade;
    private Integer linha_inicial;
    private Integer linha_final;
    private Integer plantas;
    private Double area_realizada;
    private Integer id_realizado_mdo;
    private String id_mdo;
    private String gerar_apontamento;

    // r_realizado_colheita
    private Integer cachos;
    private Double producao;
    private Double peso;
    private String hora;
    private Integer caixa;
    private String id_apontamento;

    // r_realizado_mdo
    private Integer id_colaborador;
    private Integer id_atividade_tipo;

    // r_realizado_patrimonio
    private Integer id_patrimonio;
    private Integer id_implemento;
    private Double marcador_inicial;
    private Double marcador_final;
    private String hora_inicial;
    private String hora_final;

    // r_realizado_insumo
    private Integer id_insumo;
    private String unidade;

    private String data_realizado;
    private String ativiade_patrimonio, origem, destino, obs;

    public Double getProducao() {
        return producao;
    }

    public void setProducao(Double producao) {
        this.producao = producao;
    }

    public String getData_realizado() {
        return data_realizado;
    }

    public void setData_realizado(String data_realizado) {
        this.data_realizado = data_realizado;
    }

    public String getGerar_apontamento() {
        return gerar_apontamento;
    }

    public void setGerar_apontamento(String gerar_apontamento) {
        this.gerar_apontamento = gerar_apontamento;
    }

    public String getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(String id_ficha) {
        this.id_ficha = id_ficha;
    }

    public Integer getId_parcela() {
        return id_parcela;
    }

    public void setId_parcela(Integer id_parcela) {
        this.id_parcela = id_parcela;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public Integer getLinha_inicial() {
        return linha_inicial;
    }

    public void setLinha_inicial(Integer linha_inicial) {
        this.linha_inicial = linha_inicial;
    }

    public Integer getLinha_final() {
        return linha_final;
    }

    public void setLinha_final(Integer linha_final) {
        this.linha_final = linha_final;
    }

    public Integer getPlantas() {
        return plantas;
    }

    public void setPlantas(Integer plantas) {
        this.plantas = plantas;
    }

    public Double getArea_realizada() {
        return area_realizada;
    }

    public void setArea_realizada(Double area_realizada) {
        this.area_realizada = area_realizada;
    }

    public Integer getId_realizado_mdo() {
        return id_realizado_mdo;
    }

    public void setId_realizado_mdo(Integer id_realizado_mdo) {
        this.id_realizado_mdo = id_realizado_mdo;
    }

    public String getId_mdo() {
        return id_mdo;
    }

    public void setId_mdo(String id_mdo) {
        this.id_mdo = id_mdo;
    }

    public Integer getCachos() {
        return cachos;
    }

    public void setCachos(Integer cachos) {
        this.cachos = cachos;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getCaixa() {
        return caixa;
    }

    public void setCaixa(Integer caixa) {
        this.caixa = caixa;
    }

    public String getId_apontamento() {
        return id_apontamento;
    }

    public void setId_apontamento(String id_apontamento) {
        this.id_apontamento = id_apontamento;
    }

    public Integer getId_colaborador() {
        return id_colaborador;
    }

    public void setId_colaborador(Integer id_colaborador) {
        this.id_colaborador = id_colaborador;
    }

    public Integer getId_atividade_tipo() {
        return id_atividade_tipo;
    }

    public void setId_atividade_tipo(Integer id_atividade_tipo) {
        this.id_atividade_tipo = id_atividade_tipo;
    }

    public Integer getId_patrimonio() {
        return id_patrimonio;
    }

    public void setId_patrimonio(Integer id_patrimonio) {
        this.id_patrimonio = id_patrimonio;
    }

    public Integer getId_implemento() {
        return id_implemento;
    }

    public void setId_implemento(Integer id_implemento) {
        this.id_implemento = id_implemento;
    }

    public Double getMarcador_inicial() {
        return marcador_inicial;
    }

    public void setMarcador_inicial(Double marcador_inicial) {
        this.marcador_inicial = marcador_inicial;
    }

    public Double getMarcador_final() {
        return marcador_final;
    }

    public void setMarcador_final(Double marcador_final) {
        this.marcador_final = marcador_final;
    }

    public String getHora_inicial() {
        return hora_inicial;
    }

    public void setHora_inicial(String hora_inicial) {
        this.hora_inicial = hora_inicial;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public Integer getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(Integer id_insumo) {
        this.id_insumo = id_insumo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    private Double quantidade;

    public String getAtiviade_patrimonio() {
        return ativiade_patrimonio;
    }

    public void setAtiviade_patrimonio(String ativiade_patrimonio) {
        this.ativiade_patrimonio = ativiade_patrimonio;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
