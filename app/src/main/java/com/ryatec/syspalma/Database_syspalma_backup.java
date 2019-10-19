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

import java.util.ArrayList;
import java.util.HashMap;

public class Database_syspalma_backup extends SQLiteOpenHelper {

    /**
     * O nome do arquivo de base de dados
     */
    private static final String NOME_BD = "syspalma_backup.db";
    /**
     * A versão da base de dados
     */
    private static final int VERSAO_BD = 5;
    /**
     * Diretório do banco de dados
     */
    private static String DB_PATH = "/mnt/sdcard/SysPalma/database/";
    /**
     * Mantém rastreamento do contexto da aplicação
     */
    private final Context contexto;

    public Database_syspalma_backup(Context context) {
        super(context, DB_PATH + NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //AQUI TEM QUE CRIAR AS TABELAS
        super.onOpen(db);
        String planejado  = "CREATE TABLE planejado (\n" +
                "    idplanejado                       INTEGER         PRIMARY KEY AUTOINCREMENT,\n" +
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
                "    id_implemento          INTEGER (11)    DEFAULT NULL,\n" +
                "    marcador_inicial       DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    marcador_final         DECIMAL (10, 2) DEFAULT NULL,\n" +
                "    hora_inicial           TIME            DEFAULT NULL,\n" +
                "    hora_final             TIME            DEFAULT NULL,\n" +
                "  obs VARCHAR(255)                 DEFAULT NULL,\n" +
                "  apontamento VARCHAR(50)          UNIQUE\n" +
                ");\n";

        db.execSQL(planejado);
        db.execSQL(realizado);
        db.execSQL(apontamento);
        db.execSQL(colheita);
        db.execSQL(insumo);
        db.execSQL(patrimonio_apont_realizado);
        db.execSQL(patrimonio_apont);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS planejado");
        db.execSQL("DROP TABLE IF EXISTS realizado");
        db.execSQL("DROP TABLE IF EXISTS realizado_apontamento");
        db.execSQL("DROP TABLE IF EXISTS realizado_colheita");
        db.execSQL("DROP TABLE IF EXISTS realizado_patrimonio");
        db.execSQL("DROP TABLE IF EXISTS realizado_insumo");
        db.execSQL("DROP TABLE IF EXISTS realizado_apontamento_patrimonio");
        onCreate(db);
    }

    /*MÉTODO QUE VAMOS USAR NA CLASSE QUE VAI EXECUTAR AS ROTINAS NO
    BANCO DE DADOS*/
    public SQLiteDatabase GetConexaoDataBase() {
        return this.getWritableDatabase();
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
    public void deletarRegistro(String apontamento){
        String strSQL1 = "DELETE FROM realizado_apontamento WHERE apontamento = '"+ apontamento+ "'";
        String strSQL2 = "DELETE FROM realizado_colheita WHERE apontamento = '"+ apontamento+ "'";
        String strSQL3 = "DELETE FROM realizado_patrimonio WHERE apontamento = '"+ apontamento+ "'";
        String strSQL4 = "DELETE FROM realizado_insumo WHERE apontamento = '"+ apontamento+ "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL1);
        db.execSQL(strSQL2);
        db.execSQL(strSQL3);
        db.execSQL(strSQL4);
        db.close();
    }
    public void deletarRegistroDetalhe(String ficha, String matricula){
        String strSQL1 = "DELETE FROM realizado_apontamento WHERE ficha = '"+ficha+ "' AND matricula_mdo = '"+matricula+"'";

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
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQLficha);
        db.execSQL(strSQL1);
        db.execSQL(strSQL2);
        db.execSQL(strSQL3);
        db.execSQL(strSQL4);
        db.close();
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

}
