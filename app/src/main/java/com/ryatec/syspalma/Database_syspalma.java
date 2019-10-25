package com.ryatec.syspalma;

/**
 * Created by Ryatec-Not on 07/06/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ryatec.syspalma.AGFIndustria.GetSetProgCaixaAGF;
import com.ryatec.syspalma.PointCaixa.GetSetPointCaixa;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database_syspalma extends SQLiteOpenHelper {

    /**
     * O nome do arquivo de base de dados
     */
    private static final String NOME_BD = "syspalma.db";
    /**
     * A versão da base de dados
     */
    private static final int VERSAO_BD = 27;
    /**
     * Diretório do banco de dados
     */
    private static String DB_PATH = "/mnt/sdcard/SysPalma/database/";
    /**
     * Mantém rastreamento do contexto da aplicação
     */
    private final Context contexto;

    public Database_syspalma(Context context) {
        super(context, DB_PATH + NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //AQUI TEM QUE CRIAR AS TABELAS
        super.onOpen(db);
        String c_fornecedor = "CREATE TABLE c_fornecedor (\n" +
                "    idfornecedor INTEGER         NOT NULL,\n" +
                "    fornecedor   VARCHAR (100) NOT NULL" +
                "                               UNIQUE," +
                "    descricao    VARCHAR (255) NOT NULL," +
                "    departamento   VARCHAR (255) DEFAULT NULL," +
                "    tipo       VARCHAR (100) DEFAULT NULL" +
                ");\n";
        String c_insumo = "CREATE TABLE c_insumo (\n" +
                "    idinsumo INTEGER         NOT NULL,\n" +
                "    insumo   VARCHAR (100) NOT NULL" +
                "                               UNIQUE," +
                "    descricao    VARCHAR (254) NOT NULL," +
                "    info_extra   VARCHAR (254) DEFAULT NULL," +
                "    tipo       VARCHAR (100) DEFAULT NULL" +
                ");\n";
        String atividade = "CREATE TABLE c_atividade (" +
                "    idatividade INTEGER     PRIMARY KEY AUTOINCREMENT," +
                "    operacao    VARCHAR (45) NOT NULL," +
                "    atividade   VARCHAR (45) NOT NULL" +
                ");";
        String patrimonio = "CREATE TABLE c_patrimonio (" +
                "    idpatrimonio INTEGER       NOT NULL," +
                "    patrimonio   VARCHAR (100) NOT NULL" +
                "                               UNIQUE," +
                "    tipo         VARCHAR (100) DEFAULT NULL," +
                "    descricao    VARCHAR (255) NOT NULL," +
                "    fabricante   VARCHAR (255) DEFAULT NULL," +
                "    modelo       VARCHAR (100) DEFAULT NULL" +
                ");";
        String fazenda = "CREATE TABLE i_fazenda (" +
                "    idfazenda    INTEGER         UNIQUE," +
                "    fazenda      VARCHAR (45)    UNIQUE," +
                "    descricao    VARCHAR (45)    DEFAULT NULL," +
                "    area_fazenda DECIMAL (10, 2) DEFAULT NULL," +
                "    ano_plantio  VARCHAR (4)     NOT NULL" +
                ");";
        String linha = "CREATE TABLE i_linha (" +
                "    idlinha        INTEGER   UNIQUE," +
                "    linha          INT (11) NOT NULL," +
                "    planta_inicial INT (11) NOT NULL," +
                "    planta_final   INT (11) NOT NULL," +
                "    quant_planta   INT (11) NOT NULL," +
                "    id_parcela     INT (11) NOT NULL" +
                "    CONSTRAINT fk_linha_parcela REFERENCES i_parcela (idparcela) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED\n" +
                ");";
        String parcela = "CREATE TABLE i_parcela (" +
                "    idparcela    INTEGER      UNIQUE," +
                "    parcela      VARCHAR (45)    NOT NULL," +
                "    area_parcela DECIMAL (10, 2) NOT NULL," +
                "    cultivar     VARCHAR (255)   DEFAULT NULL," +
                "    id_fazenda   INT (11)        NOT NULL" +
                ");";
        String colaborador = "CREATE TABLE p_colaborador (" +
                "    idcolaborador INTEGER       PRIMARY KEY AUTOINCREMENT," +
                "    matricula     VARCHAR (11)  NOT NULL" +
                "                                UNIQUE," +
                "    nome          VARCHAR (150) NOT NULL," +
                "    funcao        VARCHAR (45)  NOT NULL," +
                "    empresa       VARCHAR (45)  NOT NULL," +
                "    departamento  VARCHAR (45)  DEFAULT NULL," +
                "    situacao      BOOLEAN (1)   NOT NULL," +
                "    data_cadastro DATETIME      NOT NULL," +
                "    gestor        VARCHAR (45)," +
                "    usuario       VARCHAR (45)," +
                "    email         VARCHAR (45)," +
                "    tipo          VARCHAR (45) " +
                ");";
        String usuario = "CREATE TABLE p_usuario (\n" +
                "    idusuario             INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    usuario               VARCHAR (45) NOT NULL\n" +
                "                                       UNIQUE,\n" +
                "    senha                 VARCHAR (45) NOT NULL,\n" +
                "    email                 VARCHAR (45) NOT NULL,\n" +
                "    tipo                  VARCHAR (45) NOT NULL,\n" +
                "    matricula_colaborador INT (11)     NOT NULL\n" +
                "                                       UNIQUE,\n" +
                "    situacao              BOOLEAN (1)  NOT NULL,\n" +
                "    data_cadastro         DATETIME     DEFAULT CURRENT_TIMESTAMP\n" +
                ");\n";
        String planejado  = "CREATE TABLE planejado (\n" +
                "    idplanejado                       INTEGER         NOT NULL,\n" +
                "    ordem_servico                     VARCHAR (15)    NOT NULL\n" +
                "                                                      UNIQUE,\n" +
                "    descricao                         VARCHAR (45)    DEFAULT NULL,\n" +
                "    fazenda                           VARCHAR (45)    NOT NULL,\n" +
                "    atividade                         VARCHAR (45)    NOT NULL,\n" +
                "    operacao                          VARCHAR (45)    NOT NULL,\n" +
                "    matricula_colaborador_responsavel VARCHAR (45)    NOT NULL,\n" +
                "    data_inicial                      DATE            NOT NULL,\n" +
                "    data_final                        DATE            NOT NULL,\n" +
                "    ha                                DECIMAL (10, 2),\n" +
                "    status                            CHAR (1)        NOT NULL\n" +
                ");\n";
        String realizado = "CREATE TABLE realizado (\n" +
                "    idrealizado                       INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    ficha                             TEXT (20)    UNIQUE,\n" +
                "    id_planejado                      INTEGER (11) NOT NULL,\n" +
                "    data_realizado                    DATE         NOT NULL,\n" +
                "    matricula_responsavel_tecnico     VARCHAR (45) NOT NULL,\n" +
                "    matricula_responsavel_operacional VARCHAR (45) NOT NULL\n" +
                ");\n";
        String apontamento = "CREATE TABLE realizado_apontamento (\n" +
                "    idrealizado_apontamento INTEGER         PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_parcela              INTEGER (11)    NOT NULL,\n" +
                "    atividade               VARCHAR (45)    NOT NULL,\n" +
                "    linha_inicial           INTEGER (11)    NOT NULL,\n" +
                "    linha_final             INTEGER (11)    NOT NULL,\n" +
                "    plantas                 INTEGER (11)    NOT NULL,\n" +
                "    area_realizada          DECIMAL (10, 2) NOT NULL,\n" +
                "    ficha                   VARCHAR (45)    NOT NULL\n" +
                "    CONSTRAINT fk_ficha_realizado REFERENCES realizado (ficha) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED,\n" +
                "    matricula_mdo           VARCHAR (45)    NOT NULL,\n" +
                "    apontamento             VARCHAR (45)    UNIQUE\n" +
                ");\n";
        String colheita = "CREATE TABLE realizado_colheita (\n" +
                "    idrealizado_colheita INTEGER         NOT NULL\n" +
                "                                         PRIMARY KEY AUTOINCREMENT,\n" +
                "    cachos               INTEGER (11)    NOT NULL,\n" +
                "    peso                 DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    hora                 TIME            DEFAULT NULL,\n" +
                "    caixa                INTEGER (11)    NOT NULL,\n" +
                "    apontamento          VARCHAR (45)    NOT NULL\n" +
                "    CONSTRAINT fk_colheita_apontamento REFERENCES realizado_apontamento (apontamento) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED\n" +
                ");\n";
        String fitossanidade = "CREATE TABLE realizado_fitossanidade (\n" +
                "    idrealizado_fitossanidade INTEGER         NOT NULL\n" +
                "                                         PRIMARY KEY AUTOINCREMENT,\n" +
                "    producao               INTEGER (11)    NOT NULL,\n" +
                "    apontamento          VARCHAR (45)    NOT NULL\n" +
                "    CONSTRAINT fk_fitossanidade_apontamento REFERENCES realizado_apontamento (apontamento) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED\n" +
                ");\n";
        String insumo = "CREATE TABLE realizado_insumo (\n" +
                "    idrealizado_insumo INTEGER         PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_insumo          INTEGER (11)        NOT NULL,\n" +
                "    unidade            VARCHAR (20)    NOT NULL,\n" +
                "    quantidade         DECIMAL (10, 3) NOT NULL,\n" +
                "    apontamento        VARCHAR (45)    NOT NULL\n" +
                "    CONSTRAINT fk_insumo_realizado REFERENCES realizado_apontamento (apontamento) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED\n" +
                ");\n";
        String patrimonio_apont_realizado = "CREATE TABLE realizado_patrimonio (\n" +
                "    idrealizado_patrimonio INTEGER         PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_patrimonio          INTEGER (11)    NOT NULL,\n" +
                "    id_implemento          INTEGER (11)    NOT NULL,\n" +
                "    marcador_inicial       DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    marcador_final         DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    hora_inicial           TIME            DEFAULT NULL,\n" +
                "    hora_final             TIME            DEFAULT NULL,\n" +
                "    apontamento            VARCHAR (45)    NOT NULL\n" +
                "    CONSTRAINT k_patrimonio_apontamento REFERENCES realizado_apontamento (apontamento) ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE DEFERRABLE INITIALLY DEFERRED\n" +
                ");\n";
        String patrimonio_apont = "CREATE TABLE realizado_apontamento_patrimonio (\n" +
                "  idrealizado_patrimonio INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  data_realizado DATE              NOT NULL,\n" +
                "  atividade VARCHAR(45)            NOT NULL,\n" +
                "  origem VARCHAR(45)               NOT NULL,\n" +
                "  destino VARCHAR(45)              NOT NULL,\n" +
                "  matricula_mdo VARCHAR(45)        NOT NULL,\n" +
                "    id_patrimonio          INTEGER (11)    NOT NULL,\n" +
                "    id_implemento          INTEGER (11)    NOT NULL,\n" +
                "    marcador_inicial       DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    marcador_final         DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    hora_inicial           TIME            DEFAULT NULL,\n" +
                "    hora_final             TIME            DEFAULT NULL,\n" +
                "  obs VARCHAR(255)                 DEFAULT NULL,\n" +
                "  apontamento VARCHAR(50)          UNIQUE\n" +
                ")";
        String rc_compra = "CREATE TABLE rc_compra (\n" +
                "  idcompra INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  data DATE              NOT NULL,\n" +
                "  token VARCHAR(45)            NOT NULL,\n" +
                "  id_fornecedor          INTEGER (11)    NOT NULL,\n" +
                "  id_patrimonio          INTEGER (11)    NOT NULL,\n" +
                "  km       DECIMAL (10, 2) DEFAULT NULL,\n" +
                "  cupom_fiscal VARCHAR(45)              NOT NULL,\n" +
                "  matricula_responsavel VARCHAR(45)        NOT NULL,\n" +
                "  id_insumo          INTEGER (11)    NOT NULL,\n" +
                "  quantidade       DECIMAL (10, 4) NOT NULL,\n" +
                "  valor_unit         DECIMAL (10, 4) NOT NULL\n" +
                ")";
        String tanque_interno = "CREATE TABLE tanque_interno (\n" +
                "  idtanque INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  token VARCHAR(45)            NOT NULL,\n" +
                "  id_patrimonio          INTEGER (11)    NOT NULL,\n" +
                "  cupom_fiscal VARCHAR(45)              NOT NULL,\n" +
                "  id_insumo          INTEGER (11)    NOT NULL,\n" +
                "  quantidade       DECIMAL (10, 4) NOT NULL,\n" +
                "  valor_unit         DECIMAL (10, 4) NOT NULL,\n" +
                "  tanque       DECIMAL (10, 4) NOT NULL\n" +
                ")";
        String abastecimento_interno = "CREATE TABLE abastecimento_interno (\n" +
                "  idabastecimento INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  data DATETIME              NOT NULL,\n" +
                "  id_patrimonio          INTEGER (11)    NOT NULL,\n" +
                "  id_patrimonio_posto          INTEGER (11)    NOT NULL,\n" +
                "  matricula_responsavel VARCHAR(45)              NOT NULL,\n" +
                "  matricula_mdo VARCHAR(45)              NOT NULL,\n" +
                "  horimetro_km       DECIMAL (10, 2) NOT NULL,\n" +
                "  cupom_fiscal VARCHAR(45)              NOT NULL,\n" +
                "  id_insumo          INTEGER (11)    NOT NULL,\n" +
                "  quantidade       DECIMAL (10, 4) NOT NULL,\n" +
                "  cod_abastecimento      VARCHAR (45)    UNIQUE\n" +
                ")";
        String industria_programacao = "CREATE TABLE industria_programacao (\n" +
                "  idprogramacao INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  cod_programacao      VARCHAR (45)    UNIQUE,\n" +
                "  data_progamacao DATE              NOT NULL,\n" +
                "  caixa_i          INTEGER (11)     NOT NULL,\n" +
                "  caixa_ii          INTEGER (11)    DEFAULT NULL,\n" +
                "  fazenda VARCHAR(100)              NOT NULL,\n" +
                "  matricula_mdo VARCHAR(45)         NOT NULL,\n" +
                "  area_ref_cheia VARCHAR(100)       NOT NULL,\n" +
                "  area_ref_vazia VARCHAR(100)       DEFAULT NULL,\n" +
                "  placa_caminhao VARCHAR(100)       DEFAULT NULL,\n" +
                "  status       CHAR(1) NOT NULL\n" +
                ")";
        String pluviometria = "CREATE TABLE pluviometria (\n" +
                "  idpluviometria INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  data DATE              NOT NULL,\n" +
                "  fazenda VARCHAR(100)              NOT NULL,\n" +
                "  quantidade       DECIMAL (10, 2) NOT NULL\n" +
                ")";
        String ficha_producao = "CREATE TABLE ficha_producao (\n" +
                "  idficha_producao INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  data VARCHAR(100)              NOT NULL,\n" +
                "  matricula VARCHAR(100)              NOT NULL,\n" +
                "  funcao VARCHAR(100)              NOT NULL,\n" +
                "  fazenda VARCHAR(100)              NOT NULL,\n" +
                "  ordem_servico VARCHAR(100)              NOT NULL,\n" +
                "  operacao VARCHAR(100)              NOT NULL,\n" +
                "  atividade VARCHAR(100)              NOT NULL,\n" +
                "  fiscal VARCHAR(100)              NOT NULL,\n" +
                "  dias_trabalhados INTEGER              NOT NULL,\n" +
                "  plantas INTEGER              NOT NULL,\n" +
                "  hectar       DECIMAL (10, 2) NOT NULL,\n" +
                "  cachos INTEGER              DEFAULT NULL,\n" +
                "  producao       DECIMAL (10, 2) NOT NULL,\n" +
                "  faixa VARCHAR(100)              NOT NULL,\n" +
                "  valor       DECIMAL (10, 2) NOT NULL,\n" +
                "  pagamento       DECIMAL (10, 2) NOT NULL\n" +
                ")";
        String filtro_producao = "CREATE TABLE filtro_producao (\n" +
                "  data_inicial DATE              NOT NULL,\n" +
                "  data_final DATE              NOT NULL,\n" +
                "  matricula       VARCHAR(45) NOT NULL\n" +
                ")";
        String epi_mapa = "CREATE TABLE epi_mapa (\n" +
                "  idepi_mapa INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  id_epi_requisicao UNIQUE,\n" +
                "  data_mapa date NOT NULL,\n" +
                "  matricula VARCHAR(45) NOT NULL\n" +
                ")";
        String epi_requisicao = "CREATE TABLE epi_requisicao (\n" +
                "  idepi_requisicao INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  data_requisicao date NOT NULL,\n" +
                "  token VARCHAR(45) NOT NULL,\n" +
                "  matricula_responsavel VARCHAR(45) NOT NULL,\n" +
                "  matricula_requisitante VARCHAR(45) NOT NULL,\n" +
                "  epi INTEGER NOT NULL,\n" +
                "  quantidade INTEGER NOT NULL,\n" +
                "  obs VARCHAR(255),\n" +
                "  status boolean NOT NULL default 0\n" +
                ")";

        String industria_programacao_agf = "CREATE TABLE agf_industria_programacao (\n" +
                "  idprogramacao INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                "  cod_programacao      VARCHAR (45)    UNIQUE,\n" +
                "  data_progamacao DATE              NOT NULL,\n" +
                "  caixa_i          INTEGER (11)     NOT NULL,\n" +
                "  clifor_agricultor    VARCHAR(45)  NOT NULL,\n" +
                "  matricula_mdo VARCHAR(45)         NOT NULL,\n" +
                "  area_ref_cheia VARCHAR(100)       NOT NULL,\n" +
                "  area_ref_vazia VARCHAR(100)       DEFAULT NULL,\n" +
                "  placa_caminhao VARCHAR(100)       DEFAULT NULL,\n" +
                "  tonelada      DECIMAL (10, 2)     NOT NULL,\n" +
                "  cachos           INTEGER (11)     NOT NULL,\n" +
                "  romaneio         VARCHAR(100)     NOT NULL,\n" +
                "  status       CHAR(1) NOT NULL\n" +
                ")";

        String agf_agricultor = "CREATE TABLE agf_agricultor (\n" +
                "  idagf_agricultor INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "  clifor      VARCHAR (45)     UNIQUE,\n" +
                "  agricultor  VARCHAR (250)    NOT NULL,\n" +
                "  matricula_responsavel VARCHAR (45)  DEFAULT NULL,\n" +
                "  ano_plantio    VARCHAR(5)    NOT NULL,\n" +
                "  municipio VARCHAR(100)       NOT NULL,\n" +
                "  comunidade VARCHAR(150)      NOT NULL,\n" +
                "  area      DECIMAL (10, 2)    NOT NULL,\n" +
                "  agencia_bancaria   VARCHAR(100)  NOT NULL,\n" +
                "  cdb         VARCHAR(100)     NOT NULL,\n" +
                "  cpf         VARCHAR(15)      NOT NULL,\n" +
                "  inscricao_estadual VARCHAR(12)     NOT NULL,\n" +
                "  vencimento_bloco_nf DATE     NOT NULL,\n" +
                "  dap         VARCHAR(100)     NOT NULL,\n" +
                "  vencimento_dap       DATE    NOT NULL\n" +
                ")";

        String apontamento_caixa = "CREATE TABLE apontamento_caixa (\n" +
                "  idapontamento_caixa INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "  data      DATETIME,\n" +
                "  id_patrimonio      INTEGER,\n" +
                "  caixa  INTEGER,\n" +
                "  matricula VARCHAR (45)  NOT NULL,\n" +
                "  local    VARCHAR(45)    NOT NULL,\n" +
                "  tatitude REAL       NOT NULL,\n" +
                "  longitude REAL      NOT NULL,\n" +
                "  apontamento VARCHAR (45)      NOT NULL\n" +
                ")";

        db.execSQL(c_fornecedor);
        db.execSQL(c_insumo);
        db.execSQL(atividade);
        db.execSQL(patrimonio);
        db.execSQL(fazenda);
        db.execSQL(parcela);
        db.execSQL(linha);
        db.execSQL(colaborador);
        db.execSQL(usuario);
        db.execSQL(planejado);
        db.execSQL(realizado);
        db.execSQL(apontamento);
        db.execSQL(colheita);
        db.execSQL(fitossanidade);
        db.execSQL(insumo);
        db.execSQL(patrimonio_apont_realizado);
        db.execSQL(patrimonio_apont);
        db.execSQL(rc_compra);
        db.execSQL(tanque_interno);
        db.execSQL(abastecimento_interno);
        db.execSQL(industria_programacao);
        db.execSQL(epi_mapa);
        db.execSQL(epi_requisicao);

        db.execSQL(pluviometria);
        db.execSQL(ficha_producao);
        db.execSQL(filtro_producao);
        db.execSQL(industria_programacao_agf);
        db.execSQL(agf_agricultor);
        db.execSQL(apontamento_caixa);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS c_fornecedor");
        db.execSQL("DROP TABLE IF EXISTS c_insumo");
        db.execSQL("DROP TABLE IF EXISTS c_atividade");
        db.execSQL("DROP TABLE IF EXISTS c_patrimonio");
        db.execSQL("DROP TABLE IF EXISTS i_fazenda");
        db.execSQL("DROP TABLE IF EXISTS i_linha");
        db.execSQL("DROP TABLE IF EXISTS i_parcela");
        db.execSQL("DROP TABLE IF EXISTS p_colaborador");
        db.execSQL("DROP TABLE IF EXISTS p_usuario");
        db.execSQL("DROP TABLE IF EXISTS planejado");
        db.execSQL("DROP TABLE IF EXISTS realizado");
        db.execSQL("DROP TABLE IF EXISTS realizado");
        db.execSQL("DROP TABLE IF EXISTS realizado_apontamento");
        db.execSQL("DROP TABLE IF EXISTS realizado_colheita");
        db.execSQL("DROP TABLE IF EXISTS realizado_fitossanidade");
        db.execSQL("DROP TABLE IF EXISTS realizado_patrimonio");
        db.execSQL("DROP TABLE IF EXISTS realizado_insumo");
        db.execSQL("DROP TABLE IF EXISTS realizado_apontamento_patrimonio");
        db.execSQL("DROP TABLE IF EXISTS rc_compra");
        db.execSQL("DROP TABLE IF EXISTS tanque_interno");
        db.execSQL("DROP TABLE IF EXISTS abastecimento_interno");
        db.execSQL("DROP TABLE IF EXISTS industria_programacao");
        db.execSQL("DROP TABLE IF EXISTS epi_mapa");
        db.execSQL("DROP TABLE IF EXISTS epi_requisicao");

        db.execSQL("DROP TABLE IF EXISTS pluviometria");
        db.execSQL("DROP TABLE IF EXISTS ficha_producao");
        db.execSQL("DROP TABLE IF EXISTS filtro_producao");
        db.execSQL("DROP TABLE IF EXISTS agf_industria_programacao");
        db.execSQL("DROP TABLE IF EXISTS agf_agricultor");
        db.execSQL("DROP TABLE IF EXISTS apontamento_caixa");
        onCreate(db);
    }

    /*MÉTODO QUE VAMOS USAR NA CLASSE QUE VAI EXECUTAR AS ROTINAS NO
    BANCO DE DADOS*/
    public SQLiteDatabase GetConexaoDataBase() {
        return this.getWritableDatabase();
    }

    public void deletarRegistro(String apontamento){
        String strSQL1 = "DELETE FROM realizado_apontamento WHERE apontamento = '"+ apontamento+ "'";
        String strSQL2 = "DELETE FROM realizado_colheita WHERE apontamento = '"+ apontamento+ "'";
        String strSQL7 = "DELETE FROM realizado_fitossanidade WHERE apontamento = '"+ apontamento+ "'";
        String strSQL3 = "DELETE FROM realizado_patrimonio WHERE apontamento = '"+ apontamento+ "'";
        String strSQL4 = "DELETE FROM realizado_insumo WHERE apontamento = '"+ apontamento+ "'";
        String strSQL5 = "DELETE FROM realizado_apontamento_patrimonio WHERE apontamento = '"+ apontamento+ "'";
        String strSQL6 = "DELETE FROM realizado_fitossanidade WHERE apontamento = '"+ apontamento+ "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL1);
        db.execSQL(strSQL2);
        db.execSQL(strSQL6);
        db.execSQL(strSQL7);
        db.execSQL(strSQL3);
        db.execSQL(strSQL4);
        db.execSQL(strSQL5);
        db.close();
    }
    public void deletarRegistroDetalhe(String ficha, String matricula){
        String strSQL1 = "DELETE FROM realizado_apontamento WHERE ficha = '"+ficha+ "' AND matricula_mdo = '"+matricula+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL1);
        db.close();
    }
    public void deletarApontamentoMaquinario(String apontamento){
        String strSQL1 = "DELETE FROM realizado_patrimonio WHERE apontamento = '"+apontamento+ "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL1);
        db.close();
    }
    public void deletarFicha(String ficha){
        String strSQLficha = "DELETE FROM realizado WHERE ficha = '"+ ficha+ "'";
        String strSQL1 = "DELETE FROM realizado_apontamento WHERE ficha = '"+ ficha+ "'";
        String strSQL2 = "DELETE FROM realizado_colheita WHERE apontamento LIKE '%"+ ficha+ "'";
        String strSQL3 = "DELETE FROM realizado_patrimonio WHERE apontamento LIKE '%"+ ficha+ "'";
        String strSQL4 = "DELETE FROM realizado_insumo WHERE apontamento LIKE '%"+ ficha+ "'";
        String strSQL5 = "DELETE FROM realizado_apontamento_patrimonio WHERE apontamento LIKE '%"+ ficha+ "'";
        String strSQL6 = "DELETE FROM realizado_fitossanidade WHERE apontamento LIKE '%"+ ficha+ "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQLficha);
        db.execSQL(strSQL1);
        db.execSQL(strSQL2);
        db.execSQL(strSQL6);
        db.execSQL(strSQL3);
        db.execSQL(strSQL4);
        db.execSQL(strSQL5);
        db.close();
    }
    public void deletarAbastecimento(String cod, String cupom){
        String strSQLabastecimento = "DELETE FROM abastecimento_interno WHERE cod_abastecimento = '"+ cod+ "'";
        String upd = "UPDATE tanque_interno\n" +
                "SET tanque = tanque  + (SELECT quantidade\n" +
                "                  FROM abastecimento_interno\n" +
                "                  WHERE cod_abastecimento = '"+ cod+ "')\n"+
                "WHERE cupom_fiscal = '"+ cupom+ "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(upd);
        db.execSQL(strSQLabastecimento);
        db.close();
    }
    public void deletarApontamentoCaixa(int id){
        String upd = "DELETE FROM apontamento_caixa WHERE idapontamento_caixa = "+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(upd);
        db.close();
    }
    public void deletarAbastecimentoTanque(String id){
        String strSQLabastecimento = "DELETE FROM tanque_interno WHERE idtanque = "+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQLabastecimento);
        db.close();
    }

    public void deletarRegistroFuncao(String tabela, String coluna, String id){
        String strSQLabastecimento = "DELETE FROM "+tabela+" WHERE "+coluna+" IN ("+id+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQLabastecimento);
        db.close();
    }

    public ArrayList<HashMap<String, String>> ListaColaboradores() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT matricula, nome, funcao, departamento, gestor, empresa FROM p_colaborador ORDER BY funcao DESC, matricula ASC";
        //String selectQuery = "SELECT matricula, nome, funcao, departamento, gestor, empresa FROM view_pessoas WHERE gestor != 'NULL'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("matricula", cursor.getString(0));
                map.put("nome", cursor.getString(1));
                map.put("funcao", cursor.getString(2));
                map.put("departamento", cursor.getString(3));
                map.put("gestor", cursor.getString(4));
                map.put("empresa", cursor.getString(5));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
    public String parcela (String ficha, String matricula){
        String parcelas="";
        String selectQuery = "SELECT p.parcela FROM realizado_apontamento " +
                "INNER JOIN p_colaborador c " +
                "ON c.matricula = matricula_mdo " +
                "INNER JOIN i_parcela p " +
                "ON p.idparcela = id_parcela " +
                "WHERE ficha = '"+ficha+"' AND c.matricula = '"+matricula+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                parcelas = parcelas +" | "+ cursor.getString(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return parcelas;
    }
    public Integer contarFicha (){
        Integer fichas = 0;
        String selectQuery = "SELECT COUNT(ficha) as fichas FROM realizado";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                fichas = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return fichas;
    }

    public void InserirFicha(GetSetFicha Ficha) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR FICHA DIÁRIA
        try{
            ContentValues realizado = new ContentValues();
            realizado.put("ficha", Ficha.getFichaRealizado());
            realizado.put("id_planejado", Ficha.getId_planejado());
            realizado.put("data_realizado", Ficha.getData_realizado());
            realizado.put("matricula_responsavel_tecnico", Ficha.getMatricula_responsavel_tecnico());
            realizado.put("matricula_responsavel_operacional", Ficha.getMatricula_responsavel_operacional());

            db.insert("realizado", null, realizado);
            db.close();
            Toast.makeText(contexto, "Ficha Cadastrada: "+realizado.toString(), Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(contexto, "Erro no envio: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Long InserirRealizadoApontamento(GetSetAtividade Atividade) {
        Long returnId = null;
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O APONTAMENTO
        ContentValues apontamento = new ContentValues();
        apontamento.put("id_parcela", Atividade.getId_parcela());
        apontamento.put("atividade", Atividade.getAtividade());
        apontamento.put("linha_inicial", Atividade.getLinha_inicial());
        apontamento.put("linha_final", Atividade.getLinha_final());
        apontamento.put("plantas", Atividade.getPlantas());
        apontamento.put("area_realizada", Atividade.getArea_realizada());
        apontamento.put("ficha", Atividade.getId_ficha());
        apontamento.put("matricula_mdo", Atividade.getId_mdo());
        apontamento.put("apontamento", Atividade.getGerar_apontamento());

        returnId = db.insert("realizado_apontamento", null, apontamento);

        db.close();
        return returnId;
    }

    public void InserirRealizadoPatrimonio(GetSetAtividade Atividade) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues patrimonio = new ContentValues();
        patrimonio.put("id_patrimonio", Atividade.getId_patrimonio());
        patrimonio.put("id_implemento", Atividade.getId_implemento());
        patrimonio.put("marcador_inicial", Atividade.getMarcador_inicial());
        patrimonio.put("marcador_final", Atividade.getMarcador_final());
        patrimonio.put("hora_inicial",Atividade.getHora_inicial());
        patrimonio.put("hora_final",Atividade.getHora_final());
        patrimonio.put("apontamento", Atividade.getId_apontamento());

        db.insert("realizado_patrimonio", null, patrimonio);
        db.close();
    }
    public void InserirRealizadoPatrimonioAtividade(GetSetAtividade Atividade) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues patrimonio = new ContentValues();
        patrimonio.put("atividade", Atividade.getAtiviade_patrimonio());
        patrimonio.put("origem", Atividade.getOrigem());
        patrimonio.put("destino", Atividade.getDestino());
        patrimonio.put("matricula_mdo",Atividade.getId_mdo());
        patrimonio.put("apontamento",Atividade.getId_apontamento());
        patrimonio.put("obs", Atividade.getObs());
        patrimonio.put("data_realizado", Atividade.getData_realizado());
        patrimonio.put("id_patrimonio", Atividade.getId_patrimonio());
        patrimonio.put("id_implemento", Atividade.getId_implemento());
        patrimonio.put("marcador_inicial", Atividade.getMarcador_inicial());
        patrimonio.put("marcador_final", Atividade.getMarcador_final());
        patrimonio.put("hora_inicial",Atividade.getHora_inicial());
        patrimonio.put("hora_final",Atividade.getHora_final());

        db.insert("realizado_apontamento_patrimonio", null, patrimonio);
        db.close();
    }

    public void InserirRealizadoColheita(GetSetAtividade Atividade) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues colheita = new ContentValues();
        colheita.put("cachos", Atividade.getCachos());
        colheita.put("peso", Atividade.getPeso());
        colheita.put("caixa", Atividade.getCaixa());
        colheita.put("apontamento", Atividade.getId_apontamento());
        //Toast.makeText(contexto, "Inserindo Colheita: "+colheita.toString(), Toast.LENGTH_LONG).show();
        db.insert("realizado_colheita", null, colheita);
        db.close();
    }

    public void InserirRealizadoProducaoFito(GetSetAtividade Atividade) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERIR O PATIMONIO
        ContentValues colheita = new ContentValues();
        colheita.put("producao", Atividade.getProducao());
        colheita.put("apontamento", Atividade.getId_apontamento());
        //Toast.makeText(contexto, "Inserindo Colheita: "+colheita.toString(), Toast.LENGTH_LONG).show();
        db.insert("realizado_fitossanidade", null, colheita);
        db.close();
    }

    public void InserirCompra(GetSetCompra Compra) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data", Compra.getData());
        rc.put("token", Compra.getToken());
        rc.put("id_fornecedor", Compra.getId_fornecedor());
        rc.put("id_patrimonio", Compra.getId_patrimonio());
        rc.put("km",Compra.getKm());
        rc.put("cupom_fiscal", Compra.getCupom_fiscal());
        rc.put("matricula_responsavel", Compra.getId_mdo());
        rc.put("id_insumo", Compra.getId_insumo());
        rc.put("quantidade", Compra.getQuantidade());
        rc.put("valor_unit", Compra.getValor_unit());

        db.insert("rc_compra", null, rc);
        db.close();
    }
    public void InserirEPI(GetSetEPI EPI) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data_requisicao", EPI.getData());
        rc.put("token", EPI.getRequisicao());
        rc.put("matricula_responsavel", GetSetUsuario.getMatricula());
        rc.put("matricula_requisitante", EPI.getMatricula());
        rc.put("epi",EPI.getId_insumo());
        rc.put("quantidade", EPI.getQuantidade());

        db.insert("epi_requisicao", null, rc);
        db.close();
    }
    public void InserirProgCaixa(GetSetProgCaixa programacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data_progamacao", programacao.getData());
        rc.put("cod_programacao", programacao.getCod());
        rc.put("fazenda", programacao.getFazenda());
        rc.put("caixa_i", programacao.getCaixa_i());

        //VERIFICA SE A CAIXA II ESTÁ VAZIA PARA SALVAR COMO NULL NO SQLITE
        if(programacao.getCaixa_ii() > 0){
            rc.put("caixa_ii", programacao.getCaixa_ii());
        }

        rc.put("matricula_mdo",programacao.getResponsavel());
        rc.put("area_ref_cheia", programacao.getCaixa_cheia());
        rc.put("area_ref_vazia", programacao.getCaixa_vazia());
        rc.put("status", "A");

        db.insert("industria_programacao", null, rc);
        db.close();
    }


    public void InserirProgCaixaAGF(GetSetProgCaixaAGF programacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data_progamacao", programacao.getData());
        rc.put("cod_programacao", programacao.getCod());
        rc.put("clifor_agricultor", programacao.getClifor());
        rc.put("caixa_i", programacao.getCaixa_i());

        rc.put("matricula_mdo",programacao.getResponsavel());
        rc.put("area_ref_cheia", programacao.getCaixa_cheia());
        rc.put("tonelada", programacao.getTonelada());
        rc.put("cachos", programacao.getCachos());
        rc.put("romaneio", programacao.getRomaneio());
        rc.put("status", "A");

        db.insert("agf_industria_programacao", null, rc);
        db.close();
    }
    public void InserirPluviometria(GetSetPluviometria pluviometria) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data", pluviometria.getData());
        rc.put("fazenda", pluviometria.getFazenda());
        rc.put("quantidade", pluviometria.getQuantidade());
        db.insert("pluviometria", null, rc);
        db.close();
    }

    public void InserirAbastecimentoInterno(GetSetCompra Compra) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("data", Compra.getData());
        rc.put("id_patrimonio", Compra.getId_patrimonio());
        rc.put("id_patrimonio_posto", Compra.getId_fornecedor());
        rc.put("matricula_responsavel", Compra.getId_mdo());
        rc.put("matricula_mdo", Compra.getMdo_motorista());
        rc.put("horimetro_km",Compra.getKm());
        rc.put("cupom_fiscal", Compra.getCupom_fiscal());
        rc.put("id_insumo", Compra.getId_insumo());
        rc.put("quantidade", Compra.getQuantidade());
        rc.put("cod_abastecimento", Compra.getCod());

        db.insert("abastecimento_interno", null, rc);
        db.close();
    }

    public void InserirTanque(GetSetCompra Compra) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues rc = new ContentValues();
        rc.put("token", Compra.getToken());
        rc.put("id_patrimonio", Compra.getId_patrimonio());
        rc.put("cupom_fiscal", Compra.getCupom_fiscal());
        rc.put("id_insumo", Compra.getId_insumo());
        rc.put("quantidade", Compra.getQuantidade());
        rc.put("valor_unit", Compra.getValor_unit());
        rc.put("tanque", Compra.getQuantidade());

        db.insert("tanque_interno", null, rc);
        db.close();
    }

    public ArrayList<HashMap<String, String>> ListaComprasToken(String token) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT id_insumo, quantidade, valor_unit, idcompra FROM rc_compra WHERE token LIKE '%"+token+"%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Integer n = 0;
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                n++;
                Double total = cursor.getDouble(2) * cursor.getDouble(1);

                DecimalFormat df = new DecimalFormat("0.000");
                String dx = df.format(total);

                String item = RetoronoIdSelectString(cursor.getInt(0), "c_insumo", "descricao", "idinsumo");
                map.put("item", item);
                map.put("quantidade", cursor.getString(1));
                map.put("valor", cursor.getString(2));
                map.put("id", cursor.getString(3));
                map.put("total", "R$ "+dx);
                map.put("n", n.toString());
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaRequisicaoEPI(String requisicao) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT epi, quantidade, matricula_requisitante, idepi_requisicao FROM epi_requisicao WHERE token = '"+requisicao+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Integer n = 0;
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                n++;
                String item = RetoronoIdSelectString(cursor.getInt(0), "c_insumo", "descricao", "idinsumo");
                map.put("item", item);
                map.put("quantidade", cursor.getString(2));
                map.put("valor", cursor.getString(1));
                map.put("id", cursor.getString(3));
                map.put("total", "");
                map.put("n", n.toString());
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }
    public List<String> ListaRequisicaoEPIHistorico() {
        List<String> usersList = new ArrayList<String>();
        String selectQuery = "SELECT token, data_requisicao FROM epi_requisicao " +
                "WHERE matricula_responsavel = '"+GetSetUsuario.getMatricula()+"' " +
                "GROUP BY token ORDER BY data_requisicao DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Integer n = 0;
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(1).replaceAll("-", "/");
                String[] s = data.split("/");
                String novaData = s[2]+"/"+s[1]+"/"+s[0];
                usersList.add(cursor.getString(0)+" - "+novaData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaComboio() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery =
                "SELECT ins.descricao, quantidade, valor_unit, idcompra, p.descricao FROM rc_compra rc \n"+
                "INNER JOIN c_insumo ins \n"+
                "ON ins.idinsumo = rc.id_insumo \n"+
                "INNER JOIN c_patrimonio p \n"+
                "ON p.idpatrimonio = rc.id_patrimonio \n"+
                "WHERE p.tipo = 'COMBOIO'\n";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Integer n = 0;
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                Double total = cursor.getDouble(2) * cursor.getDouble(1);

                DecimalFormat df = new DecimalFormat("0.000");
                String dx = df.format(total);

                map.put("item", cursor.getString(4)+ " | " +cursor.getString(0));
                map.put("quantidade", cursor.getString(1));
                map.put("valor", cursor.getString(2));
                map.put("id", cursor.getString(3));
                map.put("total", "R$ "+dx);
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaColaboradoresPesquisa(String q) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT matricula, nome, funcao, departamento, gestor, empresa FROM p_colaborador WHERE nome LIKE '%"+q+"%' OR matricula LIKE '%"+q+"%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("matricula", cursor.getString(0));
                map.put("nome", cursor.getString(1));
                map.put("funcao", cursor.getString(2));
                map.put("departamento", cursor.getString(3));
                map.put("gestor", cursor.getString(4));
                map.put("empresa", cursor.getString(5));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public List<String> SelectFazendas() {
        List<String> campo = new ArrayList<String>();
        String selectQuery = "SELECT fazenda FROM i_fazenda";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        campo.add("Selecione");
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public List<String> SelectParcela(String fazenda) {
        List<String> campo = new ArrayList<String>();
        String selectQuery = "SELECT parcela FROM i_parcela p INNER JOIN i_fazenda f ON f.idfazenda = p.id_fazenda WHERE fazenda='"+fazenda+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        campo.add("Parc.");
        campo.add("Desloc. Maq.");
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public List<String> SelectFornecedores(String tipo) {
        List<String> campo = new ArrayList<String>();
        campo.add("SELECIONE");
        String selectQuery = "SELECT descricao FROM c_fornecedor WHERE tipo IN("+tipo+") ORDER BY descricao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public List<String> SelectInsumo(String tipo) {
        List<String> campo = new ArrayList<String>();
        campo.add("SELECIONE");
        String selectQuery = "SELECT descricao FROM c_insumo WHERE tipo IN("+tipo+") ORDER BY descricao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public List<String> SelectPatrimonio(String tipo) {
        List<String> campo = new ArrayList<String>();
        String selectQuery = "SELECT descricao FROM c_patrimonio WHERE tipo IN("+tipo+") ORDER BY descricao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(tipo=="'TRATOR'") {
            campo.add("MAQUINÁRIO");
        }else if(tipo=="'IMPLEMENTO'"){
            campo.add("IMPLEMENTO");
        }else{
            campo.add("SELECIONE");
        }
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public List<String> SelectAtividade(String operacao) {
        List<String> campo = new ArrayList<String>();
        String selectQuery = "SELECT atividade FROM c_atividade WHERE operacao = '"+operacao.toUpperCase()+"' OR operacao = 'EXT'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        campo.add("SELECIONE UMA ATIVIDADE");
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }

    public List<String> SelectRT() {
        List<String> campo = new ArrayList<String>();
        String selectQuery = "SELECT nome FROM p_colaborador WHERE funcao LIKE '%TECNICO AGR%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        campo.add("SELECIONE UM RESPONSÁVEL");
        if (cursor.moveToFirst()) {
            do {
                campo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }

    public ArrayList<HashMap<String, String>> ListaPlanejado(String atividade) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT pl.idplanejado, pl.fazenda, pl.ordem_servico, pl.descricao, pl.data_inicial, pl.data_final, pl.ha, "+
                "(pl.idplanejado) AS idfazenda, pl.atividade FROM planejado pl "+
                "INNER JOIN i_fazenda f "+
                "ON f.fazenda =  pl.fazenda "+
                "INNER JOIN i_parcela p "+
                "ON p.id_fazenda =  f.idfazenda "+
                "INNER JOIN i_linha l "+
                "ON l.id_parcela =  p.idparcela "+
                "WHERE pl.atividade = '"+atividade+"' GROUP BY pl.fazenda ORDER BY pl.fazenda, pl.data_inicial");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                String data_inicio = cursor.getString(4).replaceAll("-", "/");
                String[] s = data_inicio.split("/");
                String novaDataInicio = s[2]+"/"+s[1]+"/"+s[0];

                String data_fim = cursor.getString(5).replaceAll("-", "/");
                String[] si = data_fim.split("/");
                String novaDataFim = si[2]+"/"+si[1]+"/"+si[0];

                map.put("id", cursor.getString(0));
                map.put("fazenda", cursor.getString(1));
                map.put("ordem_servico", cursor.getString(2));
                map.put("descricao", "Descrição: " + cursor.getString(3));
                map.put("data_inicio", "Período: "+ novaDataInicio);
                map.put("data_fim", novaDataFim);
                map.put("ha", cursor.getString(6) + " ha Planejado");
                map.put("idfazenda", cursor.getString(7));
                map.put("atividade", cursor.getString(8));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("idplanejado", "Nenhuma ordem de serviço aberta, faça o download das configurações");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaFicha(Integer idplanejado) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT r.ficha, r.data_realizado, c.nome as tecnico, cc.nome as operacional FROM realizado r");
        selectQuery.append(" INNER JOIN p_colaborador c");
        selectQuery.append(" ON matricula_responsavel_tecnico = c.matricula");
        selectQuery.append(" INNER JOIN p_colaborador cc");
        selectQuery.append(" ON matricula_responsavel_operacional = cc.matricula");
        selectQuery.append(" WHERE r.id_planejado = "+idplanejado);

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                String data = cursor.getString(1).replaceAll("-", "/");
                String[] s = data.split("/");
                String novaData = s[2]+"/"+s[1]+"/"+s[0];

                map.put("idrealizado", cursor.getString(0));
                map.put("data_realizado", novaData);
                map.put("id_responsavel_tecnico", "Responsável Técnico: "+ cursor.getString(2));
                map.put("id_responsavel_operacional","Responsável Operacional: " + cursor.getString(3));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id_responsavel_tecnico", "Nenhuma ficha cadastrada! Favor, abrir uma nova (clicar no botão abaixo)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaFazenda() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT fazenda FROM i_fazenda");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idrealizado", cursor.getString(0));
                //map.put("data_realizado", novaData);
                //map.put("id_responsavel_tecnico", "Responsável Técnico: "+ cursor.getString(2));
                //map.put("id_responsavel_operacional","Responsável Operacional: " + cursor.getString(3));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("idrealizado", "Atualizar fazendas");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaProgamacaoCaixa() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT rap.cod_programacao, strftime('%d/%m/%Y', data_progamacao) as data, c.nome, fazenda, caixa_i, caixa_ii, area_ref_cheia, area_ref_vazia, rap.idprogramacao FROM industria_programacao rap");
        selectQuery.append(" INNER JOIN p_colaborador c");
        selectQuery.append(" ON c.matricula = rap.matricula_mdo");
        selectQuery.append(" ORDER BY data_progamacao");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("cod_programacao", cursor.getString(0));
                map.put("data", cursor.getString(1));
                map.put("nome",cursor.getString(2));
                map.put("fazenda", cursor.getString(3));
                map.put("caixa_i",cursor.getString(4));
                map.put("caixa_ii",cursor.getString(5));
                map.put("caixa_cheia", cursor.getString(6));
                map.put("caixa_vazia", cursor.getString(7));
                map.put("id", cursor.getString(8));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("fazenda", "Nenhuma progamação armazenada)");
            map.put("data", "");
            map.put("nome","");
            map.put("caixa_i","");
            map.put("caixa_ii","");
            map.put("caixa_cheia", "");
            map.put("caixa_vazia", "");
            map.put("id","");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaProgamacaoCaixaAGF() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT rap.cod_programacao, strftime('%d/%m/%Y', data_progamacao) as data, " +
                "c.nome, clifor_agricultor, caixa_i, romaneio, cachos, rap.idprogramacao, agf.agricultor, agf.cpf, agf.comunidade " +
                "FROM agf_industria_programacao rap");
        selectQuery.append(" INNER JOIN p_colaborador c");
        selectQuery.append(" ON c.matricula = rap.matricula_mdo");
        selectQuery.append(" INNER JOIN agf_agricultor agf");
        selectQuery.append(" ON agf.clifor = rap.clifor_agricultor");
        selectQuery.append(" ORDER BY data_progamacao");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("cod_programacao", cursor.getString(0));
                map.put("data", cursor.getString(1));
                map.put("nome",cursor.getString(2));
                map.put("clifor_agricultor", cursor.getString(3));
                map.put("caixa_i",cursor.getString(4));
                map.put("romaneio", cursor.getString(5));
                map.put("cachos", cursor.getString(6));
                map.put("id", cursor.getString(7));
                map.put("agricultor", cursor.getString(8));
                map.put("cpf", cursor.getString(9));
                map.put("comunidade", cursor.getString(10));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("clifor_agricultor", "Nenhuma progamação armazenada)");
            map.put("data", "");
            map.put("nome","");
            map.put("caixa_i","");
            map.put("romaneio","");
            map.put("cachos", "");
            map.put("id","");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaPluviometria() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT rap.idpluviometria, data, fazenda, quantidade FROM pluviometria rap");
        selectQuery.append(" ORDER BY data, fazenda");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                String data = cursor.getString(1).replaceAll("-", "/");
                String[] s = data.split("/");
                String novaData = s[2]+"/"+s[1]+"/"+s[0];

                map.put("idpluviometria", cursor.getString(0));
                map.put("data", novaData);
                map.put("fazenda",cursor.getString(2));
                map.put("quantidade", cursor.getString(3));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("fazenda", "Nenhum dado informado!)");
            map.put("data", "");
            map.put("idpluviometria","");
            map.put("quantidade","0.00");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaAbastecimentosFrota() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT rap.cod_abastecimento, strftime('%d/%m/%Y', data) as data, c.nome, p.descricao, quantidade, i.descricao, horimetro_km, p.tipo, matricula_mdo, cupom_fiscal FROM abastecimento_interno rap");
        selectQuery.append(" INNER JOIN p_colaborador c");
        selectQuery.append(" ON c.matricula = rap.matricula_mdo");

        selectQuery.append(" INNER JOIN c_patrimonio p");
        selectQuery.append(" ON p.idpatrimonio = rap.id_patrimonio");

        selectQuery.append(" INNER JOIN c_insumo i");
        selectQuery.append(" ON i.idinsumo = rap.id_insumo");
        selectQuery.append(" ORDER BY data");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("cod_abastecimento", cursor.getString(0)); //código do abastecimento
                map.put("data", cursor.getString(1)); //data formatada
                map.put("km_horimetro", "horimetro/km: "+cursor.getString(6)); //km
                map.put("nome",cursor.getString(8)+" "+cursor.getString(2)); //nome do motorista
                map.put("quantidade",cursor.getString(4)); //quatidade de litros
                map.put("insumo",cursor.getString(5)); //tipo de insumo
                map.put("patrimonio", cursor.getString(3)); //veiculo ou maquinário
                map.put("tipo", cursor.getString(7)); //veiculo ou maquinário
                map.put("cupom", cursor.getString(9)); //cupom
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("km_horimetro", "Nenhuma abastecimento armazenado)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaPointCaixa() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ac.idapontamento_caixa, strftime('%d/%m/%Y', data) as data,  p.descricao, caixa, matricula, local, apontamento  FROM apontamento_caixa ac");
        selectQuery.append(" INNER JOIN c_patrimonio p");
        selectQuery.append(" ON p.idpatrimonio = ac.id_patrimonio");
        selectQuery.append(" ORDER BY data");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("idapontamento_caixa", cursor.getString(0)); //código do abastecimento
                map.put("data", cursor.getString(1)); //data formatada
                map.put("descricao", cursor.getString(2));
                map.put("caixa",cursor.getString(3));
                map.put("matricula",cursor.getString(4));
                map.put("local",cursor.getString(5));
                map.put("apontamento", cursor.getString(6));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("descricao", "Nenhuma registro armazenado)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaAbastecimentosTanque() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT rap.idtanque, p.descricao, quantidade, tanque, i.descricao, p.tipo,  cupom_fiscal FROM tanque_interno rap");

        selectQuery.append(" INNER JOIN c_patrimonio p");
        selectQuery.append(" ON p.idpatrimonio = rap.id_patrimonio");

        selectQuery.append(" INNER JOIN c_insumo i");
        selectQuery.append(" ON i.idinsumo = rap.id_insumo");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("idtanque", cursor.getString(0)); //código do abastecimento
                map.put("patrimonio",cursor.getString(1)+"               "+cursor.getString(3)+" Litros disponível"); //nome do motorista
                map.put("tanque", cursor.getString(3)); //veiculo ou maquinário
                map.put("quantidade",cursor.getString(2)); //quatidade de litros
                map.put("insumo",cursor.getString(4)); //tipo de insumo
                map.put("tipo", cursor.getString(5)); //veiculo ou maquinário
                map.put("cupom", cursor.getString(6)); //cupom
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("km_horimetro", "Nenhuma abastecimento armazenado)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaFichaFrota(String responsavel) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT atividade, apontamento, nome, data_realizado, origem, destino, marcador_inicial, marcador_final, p.descricao FROM realizado_apontamento_patrimonio rap");
        selectQuery.append(" INNER JOIN p_colaborador c");
        selectQuery.append(" ON c.matricula = rap.matricula_mdo");

        selectQuery.append(" LEFT JOIN c_patrimonio p");
        selectQuery.append(" ON p.idpatrimonio = rap.id_patrimonio");

        selectQuery.append(" WHERE c.matricula = '"+responsavel+"'");
        selectQuery.append(" ORDER BY data_realizado");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                String data = cursor.getString(3).replaceAll("-", "/");
                String[] s = data.split("/");
                String novaData = s[2]+"/"+s[1]+"/"+s[0];
                Double rodados = cursor.getDouble(7) - cursor.getDouble(6);
                String fechado = "não";
                if(cursor.getDouble(7) > 0){
                    fechado = "sim";
                }

                map.put("idrealizado", cursor.getString(1));
                map.put("data_realizado", novaData);
                map.put("id_responsavel_tecnico", cursor.getString(2)+" | km Inicial: "+cursor.getString(6)+" - km Final: "+cursor.getString(7));
                map.put("id_responsavel_operacional"," Atividade: "+cursor.getString(0)+" | Placa: "+cursor.getString(8)+" | "+String.format("%.2f", rodados)+" km rodados");
                map.put("resumo","Origem: "+cursor.getString(4)+" Destino: "+cursor.getString(5));
                map.put("operacao", "km/hor Inicial: "+cursor.getString(6));
                map.put("os", "Placa: "+cursor.getString(8));
                map.put("status", fechado);
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id_responsavel_tecnico", "Nenhuma ficha cadastrada! Favor, abrir uma nova (clicar no botão abaixo)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaFichaGeral(String responsavel) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT r.idrealizado, pl.operacao, r.ficha, r.data_realizado, pl.descricao, pl.fazenda, COUNT(DISTINCT(ra.matricula_mdo)) as quant_pessoas,  SUM(ra.area_realizada) as area, SUM(ra.plantas) as plantas, SUM(rc.cachos) as cachos, pl.atividade, pl.ordem_servico FROM realizado r");
        selectQuery.append(" INNER JOIN planejado pl");
        selectQuery.append(" ON pl.idplanejado = r.id_planejado");
        selectQuery.append(" LEFT JOIN realizado_apontamento ra");
        selectQuery.append(" ON ra.ficha = r.ficha");
        selectQuery.append(" LEFT JOIN realizado_colheita rc");
        selectQuery.append(" ON rc.apontamento = ra.apontamento");
        selectQuery.append(" WHERE matricula_responsavel_operacional = '"+responsavel+"'");
        selectQuery.append(" GROUP BY r.ficha");
        selectQuery.append(" ORDER BY r.data_realizado");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                String data = cursor.getString(3).replaceAll("-", "/");
                String[] s = data.split("/");
                String novaData = s[2]+"/"+s[1]+"/"+s[0];
                String cachos = "0";
                if(cursor.getString(9) != null){
                    cachos = cursor.getString(9);
                }

                map.put("idrealizado", cursor.getString(2));
                map.put("data_realizado", novaData);
                map.put("id_responsavel_tecnico", cursor.getString(5));
                map.put("id_responsavel_operacional"," Atividade: "+cursor.getString(4) + " | Mão de Obra: "+cursor.getString(6));
                map.put("resumo"," Área: "+cursor.getString(7) + " | Plantas: "+cursor.getString(8)+" | Cachos: "+cachos);
                map.put("operacao", cursor.getString(10));
                map.put("os", cursor.getString(11));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id_responsavel_tecnico", "Nenhuma ficha cadastrada! Favor, abrir uma nova (clicar no botão abaixo)");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaApontamento(String idFicha, String matricula) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT ap.apontamento, linha_inicial, linha_final, parcela, atividade, ap.plantas, cachos, rf.producao, ap.area_realizada, p.matricula, p.nome  FROM realizado_apontamento ap");
        selectQuery.append(" LEFT JOIN realizado_colheita rc");
        selectQuery.append(" ON rc.apontamento = ap.apontamento");
        selectQuery.append(" LEFT JOIN realizado_fitossanidade rf");
        selectQuery.append(" ON rf.apontamento = ap.apontamento");
        selectQuery.append(" INNER JOIN i_parcela pa");
        selectQuery.append(" ON ap.id_parcela = pa.idparcela");
        selectQuery.append(" INNER JOIN p_colaborador p");
        selectQuery.append(" ON ap.matricula_mdo = p.matricula");
        selectQuery.append(" WHERE ap.ficha = '"+idFicha+"' AND matricula_mdo= '"+matricula+"'");
        selectQuery.append(" GROUP BY ap.apontamento");
        selectQuery.append(" ORDER BY p.matricula, atividade");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("apontamento", cursor.getString(0));
                map.put("linhas", cursor.getString(1) + " - "+cursor.getString(2));
                map.put("parcela", cursor.getString(3));
                map.put("atividade", cursor.getString(4));
                map.put("planta", cursor.getString(5));
                map.put("cachos", cursor.getString(6));
                map.put("producao", cursor.getString(7));
                map.put("ha", cursor.getString(8));
                map.put("matricula", cursor.getString(9));
                map.put("nome", cursor.getString(10));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matricula", "Nenhum apontamento cadastrado!");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaMaquinarioResumo(String idFicha) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT atividade, p.matricula, p.nome, cp.descricao, cip.descricao, rp.marcador_inicial, rp.marcador_final, (rp.marcador_final - rp.marcador_inicial) as horimetro, rp.apontamento  FROM realizado_apontamento ap");

        selectQuery.append(" INNER JOIN i_parcela pa");
        selectQuery.append(" ON ap.id_parcela = pa.idparcela");

        selectQuery.append(" INNER JOIN p_colaborador p");
        selectQuery.append(" ON ap.matricula_mdo = p.matricula");

        selectQuery.append(" INNER JOIN realizado_patrimonio rp");
        selectQuery.append(" ON ap.apontamento = rp.apontamento");

        selectQuery.append(" INNER JOIN c_patrimonio cp");
        selectQuery.append(" ON cp.idpatrimonio = rp.id_patrimonio");

        selectQuery.append(" INNER JOIN c_patrimonio cip");
        selectQuery.append(" ON cip.idpatrimonio = rp.id_implemento");

        selectQuery.append(" WHERE ap.ficha = '"+idFicha+"'");
        selectQuery.append(" GROUP BY ap.apontamento");
        selectQuery.append(" ORDER BY cp.idpatrimonio, rp.marcador_inicial desc");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        int cont = 1;
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("atividade", cursor.getString(0));
                map.put("matricula", cursor.getString(1));
                map.put("nome", cursor.getString(2));
                map.put("maquinario", cursor.getString(3));
                map.put("implemento", cursor.getString(4));
                map.put("hrm_ini", cursor.getString(5));
                map.put("hrm_final", cursor.getString(6));
                map.put("hrm_total", cursor.getString(7));
                map.put("apontamento", cursor.getString(8));
                //map.put("cont", "N°: "+cont);
                //map.put("fazenda", GetSetCache.getFazendaGet());
                usersList.add(map);
                cont++;
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matricula", "Nenhum apontamento cadastrado!");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> ListaApontamentoResumo(String idFicha) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT atividade, p.matricula, p.nome, p.funcao  FROM realizado_apontamento ap");

        selectQuery.append(" INNER JOIN i_parcela pa");
        selectQuery.append(" ON ap.id_parcela = pa.idparcela");

        selectQuery.append(" INNER JOIN p_colaborador p");
        selectQuery.append(" ON ap.matricula_mdo = p.matricula");

        selectQuery.append(" WHERE ap.ficha = '"+idFicha+"'");
        selectQuery.append(" GROUP BY ap.matricula_mdo");
        selectQuery.append(" ORDER BY p.matricula");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        int cont = 1;
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("atividade", cursor.getString(0));
                map.put("matricula", cursor.getString(1));
                map.put("nome", cursor.getString(2));
                map.put("funcao", cursor.getString(3));
                //map.put("cont", "N°: "+cont);
                //map.put("fazenda", GetSetCache.getFazendaGet());
                usersList.add(map);
                cont++;
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matricula", "Nenhum apontamento cadastrado!");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> ListaApontamentoErro(String idFicha) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" SELECT apontamento, linha_inicial, linha_final, parcela, atividade, ap.plantas, (SELECT cachos FROM realizado_colheita WHERE apontamento = ap.apontamento) as cachos, ap.area_realizada, ap.matricula_mdo, p.nome FROM realizado_apontamento ap");
        selectQuery.append(" LEFT OUTER JOIN i_parcela pa");
        selectQuery.append(" ON ap.id_parcela = pa.idparcela");
        selectQuery.append(" LEFT OUTER JOIN p_colaborador p");
        selectQuery.append(" ON ap.matricula_mdo = p.matricula");
        selectQuery.append(" WHERE p.nome IS null OR parcela IS null");
        selectQuery.append(" ORDER BY p.matricula, atividade");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("apontamento", cursor.getString(0));
                map.put("linhas", cursor.getString(1) + " - "+cursor.getString(2));
                map.put("parcela", cursor.getString(3));
                map.put("atividade", cursor.getString(4));
                map.put("planta", cursor.getString(5));
                map.put("cachos", cursor.getString(6));
                map.put("ha", cursor.getString(7));
                map.put("matricula", cursor.getString(8));
                map.put("nome", cursor.getString(9));
                usersList.add(map);
            } while (cursor.moveToNext());
        }else{
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matricula", "Nenhum apontamento cadastrado!");
            usersList.add(map);
        }
        database.close();
        return usersList;
    }

    public String RetoronoIdResTec(String nome) {
        String matricula = null;
        String selectQuery = "SELECT matricula FROM p_colaborador WHERE nome='"+nome+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                matricula = cursor.getString(0);
                GetSetCache.setIdResTec(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return matricula;
    }
    public Integer RetoronoCaixeiro(String ficha) {
        Integer media_equipe = null;
        String selectQuery = "SELECT COUNT(DISTINCT(ra.matricula_mdo)) as pessoas, SUM(cachos) as cachos  \n" +
                "FROM realizado r \n" +
                "INNER JOIN realizado_apontamento ra ON ra.ficha = r.ficha \n" +
                "INNER JOIN realizado_colheita rc ON rc.apontamento = ra.apontamento \n" +
                "WHERE ra.atividade LIKE '%CARREAMENTO MANUAL%' AND ra.ficha = '"+ficha+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Integer calc_media = ();
                if(cursor.getInt(1)>0) {
                    media_equipe = cursor.getInt(1) / cursor.getInt(0);
                }else{
                    media_equipe = 0;
                }
                Toast.makeText(contexto, "Pessoas Carreando: "+cursor.getString(0)+" | total de cachos: "+cursor.getString(1), Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return media_equipe;
    }
    public String RetoronoIdMat(String matricula) {
        String returMatricula = null;
        String selectQuery = "SELECT matricula FROM p_colaborador WHERE matricula='"+matricula+"' AND funcao LIKE '%RURICOLA' OR '%OPERADOR%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                returMatricula = cursor.getString(0);
                //GetSetCache.setMdo(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returMatricula;
    }
    public void RetoronoIdParcela(String parcela, Integer fazenda) {
        String selectQuery = "SELECT idparcela FROM i_parcela WHERE id_fazenda="+fazenda+" AND parcela='"+parcela+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GetSetCache.setIdParcela(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }
    public int RetoronoIdSelect(String descricao, String tabela, String campo) {
        int returnId = 0;
        String selectQuery = "SELECT "+campo+" FROM "+tabela+" WHERE descricao='"+descricao+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //GetSetCache.setIdPatrimonio(cursor.getInt(0));
                returnId = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnId;
    }
    public String RetoronoIdSelectString(int id, String tabela, String campo, String where) {
        String returnId = null;
        String selectQuery = "SELECT "+campo+" FROM "+tabela+" WHERE "+where+ "="+id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //GetSetCache.setIdPatrimonio(cursor.getInt(0));
                returnId = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnId;
    }
    public int RetoronoIdPatrimonio(String descricao) {
        int returnPat = 0;
        String selectQuery = "SELECT idpatrimonio FROM c_patrimonio WHERE descricao='"+descricao+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //GetSetCache.setIdPatrimonio(cursor.getInt(0));
                returnPat = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnPat;
    }
    public int RetoronoIdImplemento(String descricao) {
        int returnPat = 1;
        String selectQuery = "SELECT idpatrimonio FROM c_patrimonio WHERE descricao='"+descricao+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //GetSetCache.setIdImplemento(cursor.getInt(0));
                returnPat = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnPat;
    }

    public int RetoronoQuantTanque(int patrimonio) {
        int returnId = 0;
        String selectQuery = "SELECT COUNT(idtanque) FROM tanque_interno WHERE id_patrimonio = "+patrimonio;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //GetSetCache.setIdPatrimonio(cursor.getInt(0));
                returnId = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnId;
    }

    public ArrayList<HashMap<String, String>> RetornoDifLinhas(String fazenda, String parcela, String atividade){
        ArrayList<HashMap<String, String>> campo;
        campo = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.parcela, (p.area_parcela - SUM(r.area_realizada)) as area_restando, COUNT(DISTINCT(r.matricula_mdo)) as mdo FROM realizado_apontamento r");
        sql.append(" INNER JOIN i_parcela p");
        sql.append(" ON r.id_parcela = p.idparcela");
        sql.append(" INNER JOIN i_fazenda f");
        sql.append(" ON p.id_fazenda = f.idfazenda");
        sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "' AND r.atividade = '"+atividade+"'");
        sql.append(" GROUP BY p.idparcela");

        Cursor cursor = database.rawQuery(sql.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("parcela", cursor.getString(0));
                map.put("area_restando", cursor.getString(1));
                map.put("mdo", cursor.getString(2));
                campo.add(map);
                //detApont = cursor.getString(0) + " | restando: " + cursor.getString(1) + " ha | Pessoas: " +cursor.getString(2);
                //GetSetCache.setSetPessoasParcela(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return campo;
    }
    public Long RetoronoIdRealizado(String ficha) {
        long id = 0;
        String selectQuery = "SELECT MAX(idrealizado_apontamento) FROM realizado_apontamento WHERE ficha='"+ficha+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
                GetSetCache.setMdo(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return id;
    }

    public void alteraApontamentoMaquinario(GetSetAtividade Atividade, String id){
        String where_patrimonio;
        SQLiteDatabase database = this.getWritableDatabase();

        where_patrimonio = "apontamento = '" + id +"'";

        ContentValues patrimonio = new ContentValues();
        patrimonio.put("id_patrimonio", Atividade.getId_patrimonio());
        patrimonio.put("id_implemento", Atividade.getId_implemento());
        patrimonio.put("marcador_inicial", Atividade.getMarcador_inicial());
        patrimonio.put("marcador_final", Atividade.getMarcador_final());
        database.update("realizado_patrimonio",patrimonio,where_patrimonio,null);
        database.close();
    }
    public void alteraApontamento(GetSetAtividade Atividade, String id){
        String where_apontamento;
        String where_colheita;
        String where_patrimonio;
        SQLiteDatabase database = this.getWritableDatabase();

        where_apontamento = "apontamento = '" + id +"'";
        where_colheita = "apontamento = '" + id +"'";
        where_patrimonio = "apontamento = '" + id +"'";

        ContentValues apontamento = new ContentValues();
        apontamento.put("matricula_mdo", Atividade.getId_mdo());
        apontamento.put("id_parcela", Atividade.getId_parcela());
        apontamento.put("atividade", Atividade.getAtividade());
        apontamento.put("linha_inicial", Atividade.getLinha_inicial());
        apontamento.put("linha_final", Atividade.getLinha_final());
        apontamento.put("plantas", Atividade.getPlantas());
        apontamento.put("area_realizada", Atividade.getArea_realizada());
        database.update("realizado_apontamento",apontamento,where_apontamento,null);

        ContentValues patrimonio = new ContentValues();
        patrimonio.put("id_patrimonio", Atividade.getId_patrimonio());
        patrimonio.put("id_implemento", Atividade.getId_implemento());
        patrimonio.put("marcador_inicial", Atividade.getMarcador_inicial());
        patrimonio.put("marcador_final", Atividade.getMarcador_final());
        database.update("realizado_patrimonio",patrimonio,where_patrimonio,null);

        ContentValues colheita = new ContentValues();
        colheita.put("cachos", Atividade.getCachos());
        colheita.put("peso", Atividade.getPeso());
        colheita.put("caixa", Atividade.getCaixa());
        database.update("realizado_colheita",colheita,where_colheita,null);

        database.close();
    }

    public Long insertPlanejamento(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idplanejado", queryValues.get("idplanejado"));
        values.put("ordem_servico", queryValues.get("ordem_servico"));
        values.put("descricao", queryValues.get("descricao"));
        values.put("fazenda", queryValues.get("fazenda"));
        values.put("atividade", queryValues.get("atividade"));
        values.put("operacao", queryValues.get("operacao"));
        values.put("matricula_colaborador_responsavel", queryValues.get("matricula_colaborador_responsavel"));
        values.put("data_inicial", queryValues.get("data_inicial"));
        values.put("data_final", queryValues.get("data_final"));
        values.put("ha", queryValues.get("ha"));
        values.put("status", queryValues.get("status"));

        returnId = database.insert("planejado", null, values);

        database.close();
        return returnId;
    }
    public Long insertAtividade(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("operacao", queryValues.get("operacao"));
        values.put("atividade", queryValues.get("atividade"));

        returnId = database.insert("c_atividade", null, values);

        database.close();
        return returnId;
    }
    public Long insertInsumo(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idinsumo", queryValues.get("idinsumo"));
        values.put("insumo", queryValues.get("insumo"));
        values.put("descricao", queryValues.get("descricao"));
        values.put("info_extra", queryValues.get("info_extra"));
        values.put("tipo", queryValues.get("tipo"));

        returnId = database.insert("c_insumo", null, values);

        database.close();
        return returnId;
    }
    public Long insertFornecedor(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idfornecedor", queryValues.get("idfornecedor"));
        values.put("fornecedor", queryValues.get("fornecedor"));
        values.put("descricao", queryValues.get("descricao"));
        values.put("departamento", queryValues.get("departamento"));
        values.put("tipo", queryValues.get("tipo"));

        returnId = database.insert("c_fornecedor", null, values);

        database.close();
        return returnId;
    }
    public Long insertPatrimonio(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idpatrimonio", queryValues.get("idpatrimonio"));
        values.put("patrimonio", queryValues.get("patrimonio"));
        values.put("tipo", queryValues.get("tipo"));
        values.put("descricao", queryValues.get("descricao"));
        values.put("fabricante", queryValues.get("fabricante"));
        values.put("modelo", queryValues.get("modelo"));

        returnId = database.insert("c_patrimonio", null, values);

        database.close();
        return returnId;
    }
    public Long insertPessoas(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idcolaborador", queryValues.get("idcolaborador").toString());
        values.put("matricula", queryValues.get("matricula").toString());
        values.put("nome", queryValues.get("nome").toString());
        values.put("funcao", queryValues.get("funcao").toString());
        values.put("empresa", queryValues.get("empresa").toString());
        values.put("departamento", queryValues.get("departamento").toString());
        values.put("situacao", queryValues.get("situacao").toString());
        values.put("data_cadastro", queryValues.get("data_cadastro").toString());
        values.put("gestor", queryValues.get("gestor").toString());
        values.put("usuario", queryValues.get("usuario").toString());
        values.put("email", queryValues.get("email").toString());
        values.put("tipo", queryValues.get("tipo").toString());

        returnId = database.insert("p_colaborador", null, values);

        database.close();
        return returnId;
    }
    public Long insertUsuario(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idusuario", queryValues.get("idusuario").toString());
        values.put("usuario", queryValues.get("usuario").toString());
        values.put("senha", queryValues.get("senha").toString());
        values.put("email", queryValues.get("email").toString());
        values.put("tipo", queryValues.get("tipo").toString());
        values.put("matricula_colaborador", queryValues.get("matricula_colaborador").toString());
        values.put("situacao", queryValues.get("situacao").toString());
        values.put("data_cadastro", queryValues.get("data_cadastro").toString());

        returnId = database.insert("p_usuario", null, values);

        database.close();
        return returnId;
    }
    public Long insertFazenda(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idfazenda", queryValues.get("idfazenda").toString());
        values.put("fazenda", queryValues.get("fazenda").toString());
        values.put("descricao", queryValues.get("descricao").toString());
        values.put("area_fazenda", queryValues.get("area_fazenda").toString());
        values.put("ano_plantio", queryValues.get("ano_plantio").toString());

        returnId = database.insert("i_fazenda", null, values);

        database.close();
        return returnId;
    }
    public Long insertParcela(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idparcela", queryValues.get("idparcela").toString());
        values.put("parcela", queryValues.get("parcela").toString());
        values.put("area_parcela", queryValues.get("area_parcela").toString());
        values.put("cultivar", queryValues.get("cultivar").toString());
        values.put("id_fazenda", queryValues.get("id_fazenda").toString());

        returnId = database.insert("i_parcela", null, values);

        database.close();
        return returnId;
    }
    public Long insertLinha(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idlinha", queryValues.get("idlinha").toString());
        values.put("linha", queryValues.get("linha").toString());
        values.put("planta_inicial", queryValues.get("planta_inicial").toString());
        values.put("planta_final", queryValues.get("planta_final").toString());
        values.put("quant_planta", queryValues.get("quant_planta").toString());
        values.put("id_parcela", queryValues.get("id_parcela").toString());

        returnId = database.insert("i_linha", null, values);

        database.close();
        return returnId;
    }

    public Long insertAgricultores(HashMap<String, String> queryValues) {
        Long returnId = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idagf_agricultor", queryValues.get("idagf_agricultor").toString());
        values.put("clifor", queryValues.get("clifor").toString());
        values.put("agricultor", queryValues.get("agricultor").toString());
        values.put("matricula_responsavel", queryValues.get("id_colaborador").toString());
        values.put("ano_plantio", queryValues.get("ano_plantio").toString());
        values.put("municipio", queryValues.get("municipio").toString());
        values.put("comunidade", queryValues.get("comunidade").toString());
        values.put("area", queryValues.get("area").toString());
        values.put("agencia_bancaria", queryValues.get("agencia_bancaria").toString());
        values.put("cdb", queryValues.get("cdb").toString());
        values.put("cpf", queryValues.get("cpf").toString());
        values.put("inscricao_estadual", queryValues.get("inscricao_estadual").toString());
        values.put("vencimento_bloco_nf", queryValues.get("vencimento_bloco_nf").toString());
        values.put("dap", queryValues.get("dap").toString());
        values.put("vencimento_dap", queryValues.get("vencimento_dap").toString());

        returnId = database.insert("agf_agricultor", null, values);

        database.close();
        return returnId;
    }
    public void LimparTabela(String tabela) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(tabela, null, null);
    }
    public void LimparTabelaInventario(String tabela, String fazenda) {
        String where_set;
        where_set = "  WHERE fazenda ='"+fazenda+ "'";

        StringBuilder deleteParcela = new StringBuilder();
        deleteParcela.append(" delete from i_parcela where id_fazenda =( ");
        deleteParcela.append(" SELECT f.idfazenda FROM i_parcela p");
        deleteParcela.append(" INNER JOIN i_fazenda f");
        deleteParcela.append(" ON f.idfazenda = p.id_fazenda");
        deleteParcela.append(where_set);
        deleteParcela.append(" )");

        StringBuilder deleteLinhas = new StringBuilder();
        deleteLinhas.append(" delete from i_linha where id_parcela IN( ");
        deleteLinhas.append(" SELECT p.idparcela FROM i_parcela p");
        deleteLinhas.append(" INNER JOIN i_fazenda f");
        deleteLinhas.append(" ON f.idfazenda = p.id_fazenda");
        deleteLinhas.append(where_set);
        deleteLinhas.append(" )");

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteLinhas.toString());
        db.execSQL(deleteParcela.toString());
        db.close();
    }
    public void deletarCompra_item(Integer id){
        String strSQL = "DELETE FROM rc_compra WHERE idcompra = "+ id;

        String del = "DELETE FROM tanque_interno\n" +
                "WHERE cupom_fiscal = (SELECT cupom_fiscal\n" +
                "                  FROM rc_compra\n" +
                "                  WHERE idcompra = "+ id+ ")\n"+
                "AND tanque = (SELECT quantidade\n" +
                "                  FROM rc_compra\n" +
                "                  WHERE idcompra = "+ id+ ")\n";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(del);
        db.execSQL(strSQL);
        db.close();
    }

    public void deletarEPIRequisicao_item(Integer id){
        String strSQL = "DELETE FROM epi_requisicao WHERE idepi_requisicao = "+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
        db.close();
    }
    public ArrayList<JSONObject> getParcelaApontamento(String fazenda, String parcela,Integer linha_inicial, Integer linha_final) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
        sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
        sql.append(" INNER JOIN i_parcela p");
        sql.append(" ON l.id_parcela = p.idparcela");
        sql.append(" INNER JOIN i_fazenda f");
        sql.append(" ON p.id_fazenda = f.idfazenda");
        sql.append(" WHERE f.fazenda = '"+fazenda+"' AND p.parcela = '"+parcela+"' AND l.linha BETWEEN 1 AND 10");
        sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");

        ArrayList<JSONObject> usersList;
        usersList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject map = new JSONObject();
                try {
                    map.put("idparcela", cursor.getString(0));
                    map.put("parcela", cursor.getString(1));
                    map.put("linha_inicial", cursor.getString(2));
                    map.put("linha_final", cursor.getString(3));
                    map.put("quant_planta", cursor.getString(4));
                    map.put("area_parcela", cursor.getString(5));
                    usersList.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public ArrayList<JSONObject> SelectProgramacaoSync() {

        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM industria_programacao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cod_prog_caixa",cursor.getString(1));
                    jsonObject.put("data_programacao",cursor.getString(2));
                    jsonObject.put("caixa_i",cursor.getString(3));
                    jsonObject.put("caixa_ii", cursor.getString(4));
                    jsonObject.put("fazenda", cursor.getString(5));
                    jsonObject.put("matricula_mdo",cursor.getString(6));
                    jsonObject.put("area_ref_cheia",cursor.getString(7));
                    jsonObject.put("area_ref_vazia",cursor.getString(8));
                    jsonObject.put("status", cursor.getString(10));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }


    public ArrayList<JSONObject> SelectProgramacaoSyncAGF() {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM agf_industria_programacao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cod_prog_caixa",cursor.getString(1));
                    jsonObject.put("data_programacao",cursor.getString(2));
                    jsonObject.put("caixa_i",cursor.getString(3));
                    jsonObject.put("clifor_agricultor", cursor.getString(4));
                    jsonObject.put("matricula_mdo", cursor.getString(5));
                    jsonObject.put("area_ref_cheia",cursor.getString(6));
                    jsonObject.put("toneladas",cursor.getString(9));
                    jsonObject.put("cachos",cursor.getString(10));
                    jsonObject.put("romaneio",cursor.getString(11));
                    jsonObject.put("status", cursor.getString(12));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectPluviometriaSync() {

        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM pluviometria";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data",cursor.getString(1));
                    jsonObject.put("fazenda",cursor.getString(2));
                    jsonObject.put("quantidade",cursor.getString(3));
                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }

    public ArrayList<JSONObject> SelectCompraSync() {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM rc_compra";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data",cursor.getString(1));
                    jsonObject.put("token",cursor.getString(2));
                    jsonObject.put("id_fornecedor",cursor.getString(3));
                    jsonObject.put("id_patrimonio", cursor.getString(4));
                    jsonObject.put("km", cursor.getString(5));
                    jsonObject.put("cupom_fiscal",cursor.getString(6));
                    jsonObject.put("matricula_responsavel",cursor.getString(7));
                    jsonObject.put("id_insumo",cursor.getString(8));
                    jsonObject.put("quantidade", cursor.getString(9));
                    jsonObject.put("valor_unit", cursor.getString(10));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectEPIRequisicaoSync() {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM epi_requisicao";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data_requisicao",cursor.getString(1));
                    jsonObject.put("token",cursor.getString(2));
                    jsonObject.put("matricula_responsavel", cursor.getString(3));
                    jsonObject.put("matricula_requisitante", cursor.getString(4));
                    jsonObject.put("epi",cursor.getString(5));
                    jsonObject.put("quantidade",cursor.getString(6));
                    jsonObject.put("status",cursor.getString(7));
                    jsonObject.put("obs", cursor.getString(8));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectAbastecimentoSync() {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT  * FROM abastecimento_interno";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data",cursor.getString(1));
                    jsonObject.put("id_patrimonio",cursor.getString(2));
                    jsonObject.put("id_patrimonio_posto", cursor.getString(3));
                    jsonObject.put("matricula_responsavel", cursor.getString(4));
                    jsonObject.put("matricula_mdo",cursor.getString(5));
                    jsonObject.put("horimetro_km",cursor.getString(6));
                    jsonObject.put("cupom_fiscal",cursor.getString(7));
                    jsonObject.put("id_insumo", cursor.getString(8));
                    jsonObject.put("quantidade", cursor.getString(9));
                    jsonObject.put("cod_abastecimento", cursor.getString(10));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectRealizadoSync(Integer idPlanejado) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;
        if(idPlanejado>0) {
            selectQuery = "SELECT  * FROM realizado WHERE id_planejado = " + idPlanejado;
        }else{
            selectQuery = "SELECT  * FROM realizado";
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ficha",cursor.getString(1));
                    jsonObject.put("id_planejado",cursor.getString(2));
                    jsonObject.put("data_realizado",cursor.getString(3));
                    jsonObject.put("matricula_responsavel_tecnico", cursor.getString(4));
                    jsonObject.put("matricula_responsavel_operacional", cursor.getString(5));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectRealizadoSyncGeral(String responsavel) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        String selectQuery;

        selectQuery = "SELECT  * FROM realizado WHERE matricula_responsavel_operacional = '" + responsavel +"'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ficha",cursor.getString(1));
                    jsonObject.put("id_planejado",cursor.getString(2));
                    jsonObject.put("data_realizado",cursor.getString(3));
                    jsonObject.put("matricula_responsavel_tecnico", cursor.getString(4));
                    jsonObject.put("matricula_responsavel_operacional", cursor.getString(5));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectidRealizadoApontamentoSync(Integer idPlanejado) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        if(idPlanejado>0) {
            selectQuery.append(" SELECT ra.idrealizado_apontamento, ra.id_parcela, ra.atividade, ra.linha_inicial, ra.linha_final," +
                    " ra.plantas, ra.area_realizada, ra.ficha, ra.matricula_mdo, ra.apontamento FROM realizado_apontamento ra");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.id_planejado = " + idPlanejado);
        }else{
            selectQuery.append(" SELECT ra.idrealizado_apontamento, ra.id_parcela, ra.atividade, ra.linha_inicial, ra.linha_final," +
                    " ra.plantas, ra.area_realizada, ra.ficha, ra.matricula_mdo, ra.apontamento FROM realizado_apontamento ra");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'");
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_parcela",cursor.getString(1));
                    jsonObject.put("atividade",cursor.getString(2));
                    jsonObject.put("linha_inicial",cursor.getString(3));
                    jsonObject.put("linha_final", cursor.getString(4));
                    jsonObject.put("plantas", cursor.getString(5));
                    jsonObject.put("area_realizada", cursor.getString(6));
                    jsonObject.put("ficha", cursor.getString(7));
                    jsonObject.put("matricula_mdo", cursor.getString(8));
                    jsonObject.put("apontamento", cursor.getString(9));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectRealizadoColheitaSync(Integer idPlanejado) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        if(idPlanejado>0) {
            selectQuery.append(" SELECT rc.cachos, rc.peso, rc.hora, caixa, rc.apontamento FROM realizado_colheita rc");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rc.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.id_planejado = " + idPlanejado);
        }else{
            selectQuery.append(" SELECT rc.cachos, rc.peso, rc.hora, caixa, rc.apontamento FROM realizado_colheita rc");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rc.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'");
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cachos",cursor.getString(0));
                    jsonObject.put("peso",cursor.getString(1));
                    jsonObject.put("hora",cursor.getString(2));
                    jsonObject.put("caixa", cursor.getString(3));
                    jsonObject.put("apontamento", cursor.getString(4));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectRealizadoFitoSync(Integer idPlanejado) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        if(idPlanejado>0) {
            selectQuery.append(" SELECT rf.producao, rf.apontamento FROM realizado_fitossanidade rf");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rf.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.id_planejado = " + idPlanejado);
        }else{
            selectQuery.append(" SELECT rf.producao, rf.apontamento FROM realizado_fitossanidade rf");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rf.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'");
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("producao",cursor.getString(0));
                    jsonObject.put("apontamento", cursor.getString(1));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    public ArrayList<JSONObject> SelectRealizadoPatrimonioSync(Integer idPlanejado) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        if(idPlanejado>0) {
            selectQuery.append(" SELECT rp.id_patrimonio, rp.id_implemento, rp.marcador_inicial, rp.marcador_final, rp.hora_inicial, rp.hora_final, rp.apontamento FROM realizado_patrimonio rp");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rp.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.id_planejado = " + idPlanejado);
        }else{
            selectQuery.append(" SELECT rp.id_patrimonio, rp.id_implemento, rp.marcador_inicial, rp.marcador_final, rp.hora_inicial, rp.hora_final, rp.apontamento FROM realizado_patrimonio rp");
            selectQuery.append(" INNER JOIN realizado_apontamento ra");
            selectQuery.append(" ON ra.apontamento = rp.apontamento");
            selectQuery.append(" INNER JOIN realizado r");
            selectQuery.append(" ON ra.ficha = r.ficha");
            selectQuery.append(" WHERE r.matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'");
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_patrimonio",cursor.getString(0));
                    jsonObject.put("id_implemento",cursor.getString(1));
                    jsonObject.put("marcador_inicial",cursor.getString(2));
                    jsonObject.put("marcador_final", cursor.getString(3));
                    jsonObject.put("hora_inicial", cursor.getString(4));
                    jsonObject.put("hora_final", cursor.getString(5));
                    jsonObject.put("apontamento", cursor.getString(6));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }

    public ArrayList<JSONObject> SelectRealizadoPatrimonioAtividadeSync(String responsavel) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  * FROM realizado_apontamento_patrimonio ");
        selectQuery.append("WHERE (marcador_final - marcador_inicial) > 0 AND matricula_mdo = ");
        selectQuery.append(responsavel);

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data_realizado",cursor.getString(1));
                    jsonObject.put("atividade",cursor.getString(2));
                    jsonObject.put("origem",cursor.getString(3));
                    jsonObject.put("destino", cursor.getString(4));
                    jsonObject.put("matricula_mdo", cursor.getString(5));
                    jsonObject.put("id_patrimonio",cursor.getString(6));
                    jsonObject.put("id_implemento",cursor.getString(7));
                    jsonObject.put("marcador_inicial",cursor.getString(8));
                    jsonObject.put("marcador_final", cursor.getString(9));
                    jsonObject.put("hora_inicial", cursor.getString(10));
                    jsonObject.put("hora_final", cursor.getString(11));
                    jsonObject.put("obs", cursor.getString(12));
                    jsonObject.put("apontamento", cursor.getString(13));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }

    public ArrayList<JSONObject> SelectApontamentoCaixaSync() {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  * FROM apontamento_caixa");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data",cursor.getString(1));
                    jsonObject.put("id_patrimonio",cursor.getString(2));
                    jsonObject.put("caixa",cursor.getString(3));
                    jsonObject.put("matricula", cursor.getString(4));
                    jsonObject.put("local",cursor.getString(5));
                    jsonObject.put("tatitude",cursor.getString(6));
                    jsonObject.put("longitude",cursor.getString(7));
                    jsonObject.put("apontamento", cursor.getString(8));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }

    public ArrayList<JSONObject> SelectRealizadoPatrimonioFrotaSync(String responsavel) {
        ArrayList<JSONObject> dataArrays = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  * FROM realizado_apontamento_patrimonio rap ");
        selectQuery.append("INNER JOIN realizado_patrimonio rp ");
        selectQuery.append("ON rp.apontamento = rap.apontamento ");
        selectQuery.append("WHERE (rp.marcador_final - rp.marcador_inicial) > 0 AND matricula_mdo = ");
        selectQuery.append(responsavel);

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_patrimonio",cursor.getString(0));
                    jsonObject.put("id_implemento",cursor.getString(1));
                    jsonObject.put("marcador_inicial",cursor.getString(2));
                    jsonObject.put("marcador_final", cursor.getString(3));
                    jsonObject.put("hora_inicial", cursor.getString(4));
                    jsonObject.put("hora_final", cursor.getString(5));
                    jsonObject.put("apontamento", cursor.getString(6));

                    dataArrays.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }

    public String[] BuscaBackupPlanejado(Integer idPlanejado) {
        String[] dataArrays = new String[0];
        StringBuilder selectQuery = new StringBuilder();
        if(idPlanejado>0) {
            selectQuery.append(" SELECT fazenda, operacao, ordem_servico FROM planejado pl");
            selectQuery.append(" WHERE pl.idplanejado = " + idPlanejado);
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                //JSONObject jsonObject = new JSONObject();
                dataArrays = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2)};
            } while (cursor.moveToNext());
        }
        database.close();
        return dataArrays;
    }
    //FUNÇÃO PARA DELETAR AS MDO's DESATIVDADAS
    public void deletarApontamentoErro(){
        SQLiteDatabase db = this.getWritableDatabase();
        //DELETAR O REALIZADO
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(" DELETE FROM realizado_apontamento WHERE apontamento IN (");
        selectQuery.append(" 'SELECT apontamento FROM realizado_apontamento ap");
        selectQuery.append(" LEFT OUTER JOIN p_colaborador p");
        selectQuery.append(" ON ap.matricula_mdo = p.matricula");
        selectQuery.append(" WHERE p.nome IS null ')");

        db.execSQL(selectQuery.toString());
        db.close();
    }
    public void deletarSync(Integer idPlanejado){
        SQLiteDatabase db = this.getWritableDatabase();
        String where_set;
        String selectRealizado;
        if(idPlanejado>0){
            selectRealizado = "DELETE FROM realizado WHERE id_planejado = "+idPlanejado;
            where_set = " WHERE r.id_planejado = "+idPlanejado;
        }else{
            selectRealizado = "DELETE FROM realizado WHERE matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'";
            where_set = " WHERE r.matricula_responsavel_operacional = '" + GetSetUsuario.getMatricula()+"'";
        }
        //DELETAR O REALIZADO

        //DELETAR O APONTAMENTO
        StringBuilder deleteApontamento = new StringBuilder();
        deleteApontamento.append(" DELETE FROM realizado_apontamento");
        deleteApontamento.append(" WHERE ficha IN (");
        deleteApontamento.append(" SELECT r.ficha FROM realizado r");
        deleteApontamento.append(where_set);
        deleteApontamento.append(" );");

        //DELETAR
        StringBuilder deletePatrimonio = new StringBuilder();
        deletePatrimonio.append(" DELETE FROM realizado_patrimonio");
        deletePatrimonio.append(" WHERE apontamento IN (");
        deletePatrimonio.append(" SELECT apontamento FROM realizado_apontamento ra");
        deletePatrimonio.append(" INNER JOIN realizado r");
        deletePatrimonio.append(" ON r.ficha = ra.ficha");
        deletePatrimonio.append(where_set);
        deletePatrimonio.append(" );");

        StringBuilder deleteColheita = new StringBuilder();
        deleteColheita.append(" DELETE FROM realizado_colheita");
        deleteColheita.append(" WHERE apontamento IN (");
        deleteColheita.append(" SELECT apontamento FROM realizado_apontamento ra");
        deleteColheita.append(" INNER JOIN realizado r");
        deleteColheita.append(" ON r.ficha = ra.ficha");
        deleteColheita.append(where_set);
        deleteColheita.append(" );");

        db.execSQL(deleteColheita.toString());
        db.execSQL(deletePatrimonio.toString());
        db.execSQL(deleteApontamento.toString());
        db.execSQL(selectRealizado);
        db.close();
    }
    public void deletarSyncApontamentoCaixa(){
        SQLiteDatabase db = this.getWritableDatabase();
        //DELETAR O APONTAMENTO
        StringBuilder deleteApontamento = new StringBuilder();
        deleteApontamento.append(" DELETE FROM apontamento_caixa");
        db.execSQL(deleteApontamento.toString());
        db.close();
    }

    public void deletarSyncFrota(String responsavel){
        SQLiteDatabase db = this.getWritableDatabase();
        String where_set = " WHERE (marcador_final - marcador_inicial) > 0 AND matricula_mdo = '" + responsavel+"'";
        String selectRealizado;
        //DELETAR O REALIZADO

        //DELETAR O APONTAMENTO
        StringBuilder deleteApontamento = new StringBuilder();
        deleteApontamento.append(" DELETE FROM realizado_apontamento_patrimonio");
        deleteApontamento.append(where_set);

        db.execSQL(deleteApontamento.toString());

        db.close();
    }

    public void alteraApontamentoFrota(GetSetAtividade Atividade, String id){
        String where_apontamento;
        SQLiteDatabase database = this.getWritableDatabase();

        where_apontamento = "apontamento = '" + id +"'";

        ContentValues apontamento = new ContentValues();
        apontamento.put("atividade", Atividade.getAtiviade_patrimonio());
        apontamento.put("origem", Atividade.getOrigem());
        apontamento.put("destino", Atividade.getDestino());
        apontamento.put("id_patrimonio", Atividade.getId_patrimonio());
        apontamento.put("id_implemento", Atividade.getId_implemento());
        apontamento.put("marcador_inicial", Atividade.getMarcador_inicial());
        apontamento.put("marcador_final", Atividade.getMarcador_final());
        apontamento.put("hora_final", Atividade.getHora_final());
        database.update("realizado_apontamento_patrimonio",apontamento,where_apontamento,null);

        database.close();
    }
    public void UpdTanque(Double litros, String cupom_fiscal, String cupom_transferido){
        String where_apontamento;
        SQLiteDatabase database = this.getWritableDatabase();

        where_apontamento = "cupom_fiscal = '" + cupom_fiscal +"'";

        ContentValues apontamento = new ContentValues();
        apontamento.put("tanque", litros);
        database.update("tanque_interno",apontamento,where_apontamento,null);

        if(cupom_transferido != null){
            String strSQL = "DELETE FROM tanque_interno WHERE cupom_fiscal = '"+ cupom_transferido+"'";
            database.execSQL(strSQL);
        }

        database.close();
    }
    public String BuscaMatricula(String busca) {
        String returnMatricula = null;
        String selectQuery = "SELECT matricula FROM colaborador WHERE matricula = '"+busca+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                returnMatricula = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnMatricula;
    }

    public ArrayList<String> SelectEPI() {
        ArrayList<String> queryValues;
        queryValues = new ArrayList<String>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  idinsumo, descricao FROM c_insumo ");
        selectQuery.append("WHERE tipo = 'EPI' ");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("idinsumo",cursor.getString(0));
                    jsonObject.put("descricao",cursor.getString(1));

                    queryValues.add(jsonObject.get("descricao").toString()+"; "+jsonObject.get("idinsumo").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return queryValues;
    }

    public ArrayList<String> selecAGF() {
        ArrayList<String> queryValues = new ArrayList<String>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT clifor,agricultor,municipio,comunidade,cpf,inscricao_estadual FROM agf_agricultor");
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("clifor",cursor.getString(0));
                    jsonObject.put("agricultor",cursor.getString(1));
                    jsonObject.put("municipio",cursor.getString(2));
                    jsonObject.put("comunidade",cursor.getString(3));
                    jsonObject.put("cpf",cursor.getString(4));
                    jsonObject.put("inscricao_estadual",cursor.getString(5));

                    queryValues.add(jsonObject.get("agricultor").toString()+", "+jsonObject.get("inscricao_estadual").toString()+", "+jsonObject.get("clifor").toString()+", "+jsonObject.get("cpf").toString()+", "+jsonObject.get("comunidade").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return queryValues;
    }

    public void InserirApontamentoCaixa(GetSetPointCaixa pointCaixa) {
        SQLiteDatabase db = this.getWritableDatabase();

        //INSERIR O PATIMONIO
        ContentValues pontoCaixa = new ContentValues();
        pontoCaixa.put("data", pointCaixa.getData());
        pontoCaixa.put("id_patrimonio", pointCaixa.getId_patrimonio());
        pontoCaixa.put("caixa", pointCaixa.getCaixa());
        pontoCaixa.put("matricula",pointCaixa.getMatricula());
        pontoCaixa.put("local",pointCaixa.getLocal());
        pontoCaixa.put("tatitude", pointCaixa.getLatitude());
        pontoCaixa.put("longitude", pointCaixa.getLongitude());
        pontoCaixa.put("apontamento", pointCaixa.getId_apontamento());

        db.insert("apontamento_caixa", null, pontoCaixa);
        db.close();
    }
}
