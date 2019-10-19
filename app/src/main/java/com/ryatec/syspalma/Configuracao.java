package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static java.lang.Boolean.FALSE;

public class Configuracao extends AppCompatActivity {
    private CardView funcinario, atividade, patrimonio, inventario, os, insumo, fornecedor, agf;
    private static String urlFuncionario = "http://adm.syspalma.com/index.php/webservice/mdo";
    private static String urlAtividade = "http://adm.syspalma.com/index.php/webservice/atividade";
    private static String urlPatrimonio = "http://adm.syspalma.com/index.php/webservice/patrimonio";
    private static String urlOS = "http://adm.syspalma.com/index.php/webservice/planejamento";
    private static String urlFazenda = "http://adm.syspalma.com/index.php/webservice/fazenda";
    private static String urlParcela = "http://adm.syspalma.com/index.php/webservice/parcela";
    private static String urlInsumo = "http://adm.syspalma.com/index.php/webservice/insumo";
    private static String urlFornecedor = "http://adm.syspalma.com/index.php/webservice/fornecedor";
    private static String urlAgricultores = "http://adm.syspalma.com/index.php/webservice/agricultores";
    private static String urlLinha;
    HashMap<String, String> queryValues;
    private Database_syspalma database = new Database_syspalma(this);
    private Database_syspalma_backup database_backup = new Database_syspalma_backup(this);
    private AlertDialog alerta;//atributo da classe.
    public Long retorno = null;
    private ProgressDialog barProgressDialogGeral;
    private TextView txtmdo, txtatividade, txtpatrimonio, txtplanejado, txtinventario, txtagf;
    private JSONArray arr_fazenda;
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        txtmdo = findViewById(R.id.contFunc);
        txtatividade = findViewById(R.id.contAtiv);
        txtpatrimonio = findViewById(R.id.contPat);
        txtplanejado = findViewById(R.id.contOS);
        txtinventario = findViewById(R.id.contInv);
        txtagf = findViewById(R.id.contAGF);

        resumo();

        funcinario = (CardView) findViewById(R.id.cadFuncionario);
        atividade = (CardView) findViewById(R.id.cadAtividade);
        patrimonio = (CardView) findViewById(R.id.cadPatrimonio);
        inventario = (CardView) findViewById(R.id.cadInventario);
        insumo = (CardView) findViewById(R.id.cadInsumo);
        fornecedor = (CardView) findViewById(R.id.cadFornecedor);
        agf = (CardView) findViewById(R.id.cadAGF);
        os = (CardView) findViewById(R.id.cadOS);

        funcinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlFuncionario,"funcionario");
            }
        });
        atividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlAtividade,"atividade");
                syncSQLite(urlFazenda,"fazenda");
            }
        });
        patrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlPatrimonio,"patrimonio");
            }
        });
        insumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlInsumo,"insumo");
            }
        });
        fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlFornecedor,"fornecedor");
            }
        });
        inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DownloadDados valor = (DownloadDados) new DownloadDados(Configuracao.this).execute();
                //syncInventario(urlFazenda,"fazenda");
            }
        });
        agf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlAgricultores,"agf");
            }
        });
        os.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSQLite(urlOS,"os");
            }
        });

        myListView = (ListView) findViewById(R.id.listFazenda);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView fazenda = (TextView) view.findViewById(R.id.fazenda);
                if(fazenda.getText().toString().equals("Atualizar fazendas")){
                    syncInventario(urlFazenda,"fazenda");
                    chamarLista();
                }else{
                    exemplo_alerta_fazenda("PARCELAS","Deseja realmente atualizar?",fazenda.getText().toString());
                    chamarLista();
                }

            }
        });
        chamarLista();
    }
    public void syncSQLite(String urlParse, final String campo) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Buscando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setTimeout(3000);
        RequestParams params = new RequestParams();
        // Janela de Progresso
        client.get(urlParse, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                //controller.LimparTabela("funcionarios");
                //Toast.makeText(getApplicationContext(), "Atualizado com sucesso ", Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
                switch (campo){
                    case "os" :
                        updateSQLiteOS(response);
                    break;
                    case "patrimonio" :
                        updateSQLitePatrimonio(response);
                        break;
                    case "atividade" :
                        updateSQLiteAtividade(response);
                        break;
                    case "funcionario" :
                        updateSQLiteFuncionario(response);
                        break;
                    case "fazenda" :
                        updateSQLiteFazenda(response);
                        break;
                    case "insumo" :
                        updateSQLiteInsumo(response);
                        break;
                    case "fornecedor" :
                        updateSQLiteFornecedor(response);
                        break;
                    case "agf" :
                        updateSQLiteAGF(response);
                        break;
                }
                resumo();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Falha " + statusCode, Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
            }
            @Override
            public void onFinish() {
                barProgressDialog.hide();
            }

        });
    }
    public void updateSQLiteOS(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Long id = null;

            if (arr.length() != 0) {
                database.LimparTabela("planejado");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idplanejado", obj.get("idplanejado").toString());
                    queryValues.put("ordem_servico", obj.get("ordem_servico").toString());
                    queryValues.put("descricao", obj.get("descricao").toString());
                    queryValues.put("fazenda", obj.get("fazenda").toString());
                    queryValues.put("atividade", obj.get("atividade").toString());
                    queryValues.put("operacao", obj.get("operacao").toString());
                    queryValues.put("matricula_colaborador_responsavel", obj.get("matricula_colaborador_responsavel").toString());
                    queryValues.put("data_inicial", obj.get("data_inicial").toString());
                    queryValues.put("data_final", obj.get("data_final").toString());
                    queryValues.put("ha", obj.get("ha").toString());
                    queryValues.put("status", obj.get("status").toString());
                    id =  database.insertPlanejamento(queryValues);
                    //BANCO DO BACKUP PARA INSERIR O APONTAMENTO
                    database_backup.insertPlanejamento(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Valores armazenados!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLitePatrimonio(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("c_patrimonio");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idpatrimonio", obj.get("idpatrimonio").toString());
                    queryValues.put("patrimonio", obj.get("patrimonio").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());
                    queryValues.put("descricao", obj.get("descricao").toString());
                    queryValues.put("modelo", obj.get("modelo").toString());
                    id =  database.insertPatrimonio(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Patrimônio","Valores armazenados! ");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteAtividade(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("c_atividade");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("operacao", obj.get("operacao").toString());
                    queryValues.put("atividade", obj.get("atividade").toString());
                    id =  database.insertAtividade(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Valores armazenados!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteFuncionario(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("p_colaborador");
                Long id = null;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idcolaborador", obj.get("idcolaborador").toString());
                    queryValues.put("matricula", obj.get("matricula").toString());
                    queryValues.put("nome", obj.get("nome").toString());
                    queryValues.put("funcao", obj.get("funcao").toString());
                    queryValues.put("empresa", obj.get("empresa").toString());
                    queryValues.put("departamento", obj.get("departamento").toString());
                    queryValues.put("situacao", obj.get("situacao").toString());
                    queryValues.put("data_cadastro", obj.get("data_cadastro").toString());
                    queryValues.put("gestor", obj.get("gestor").toString());
                    queryValues.put("usuario", obj.get("usuario").toString());
                    queryValues.put("email", obj.get("email").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());

                    id =  database.insertPessoas(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Valores armazenados!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteInsumo(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());

            if (arr.length() != 0) {
                database.LimparTabela("c_insumo");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idinsumo", obj.get("idinsumo").toString());
                    queryValues.put("insumo", obj.get("insumo").toString());
                    queryValues.put("descricao", obj.get("descricao").toString());
                    queryValues.put("info_extra", obj.get("info_extra").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());
                    id =  database.insertInsumo(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Insumo","Valores armazenados! ");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteFornecedor(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("c_fornecedor");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idfornecedor", obj.get("idfornecedor").toString());
                    queryValues.put("fornecedor", obj.get("fornecedor").toString());
                    queryValues.put("descricao", obj.get("descricao").toString());
                    queryValues.put("departamento", obj.get("departamento").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());
                    id =  database.insertFornecedor(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Fornecedor","Valores armazenados! ");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteAGF(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Salvando internamente, aguarde...");
        barProgressDialog.setMessage("Salvando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("agf_agricultor");
                Long id = null;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idagf_agricultor", obj.get("idagf_agricultor").toString());
                    queryValues.put("clifor", obj.get("clifor").toString());
                    queryValues.put("agricultor", obj.get("agricultor").toString());
                    queryValues.put("id_colaborador", obj.get("id_colaborador").toString());
                    queryValues.put("ano_plantio", obj.get("ano_plantio").toString());
                    queryValues.put("municipio", obj.get("municipio").toString());
                    queryValues.put("comunidade", obj.get("comunidade").toString());
                    queryValues.put("area", obj.get("area").toString());
                    queryValues.put("agencia_bancaria", obj.get("agencia_bancaria").toString());
                    queryValues.put("cdb", obj.get("cdb").toString());
                    queryValues.put("cpf", obj.get("cpf").toString());
                    queryValues.put("inscricao_estadual", obj.get("inscricao_estadual").toString());
                    queryValues.put("vencimento_bloco_nf", obj.get("vencimento_bloco_nf").toString());
                    queryValues.put("dap", obj.get("dap").toString());
                    queryValues.put("vencimento_dap", obj.get("vencimento_dap").toString());

                    id =  database.insertAgricultores(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Valores armazenados!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void syncInventario(String urlParse, final String campo) {
        barProgressDialogGeral = new ProgressDialog(this);
        barProgressDialogGeral.setTitle("Sincronizando, aguarde...");
        barProgressDialogGeral.setMessage("Baixando Informações ...");
        barProgressDialogGeral.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialogGeral.setCancelable(FALSE);
        barProgressDialogGeral.show();
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setTimeout(3000);
        RequestParams params = new RequestParams();
        // Janela de Progresso
        client.get(urlParse, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                switch (campo){
                    case "fazenda" :
                        updateSQLiteFazenda(response);
                        break;
                }
                retorno = Long.valueOf(response.length());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Falha " + statusCode, Toast.LENGTH_LONG).show();
                barProgressDialogGeral.hide();
            }
            @Override
            public void onFinish() {
                barProgressDialogGeral.hide();
                //exemplo_alerta("PARCELAS","Inventário Atualizado com sucesso! ");
            }

        });
    }
    public void updateSQLiteFazenda(String response) {
        try {
            JSONArray arr = new JSONArray(response);
            arr_fazenda = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("i_fazenda");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idfazenda", obj.get("idfazenda").toString());
                    queryValues.put("fazenda", obj.get("fazenda").toString());
                    queryValues.put("descricao", obj.get("descricao").toString());
                    queryValues.put("area_fazenda", obj.get("area_fazenda").toString());
                    queryValues.put("ano_plantio", obj.get("ano_plantio").toString());
                    id =  database.insertFazenda(queryValues);
                }
                if(id != null){
                    //exemplo_alerta_fazenda("FAZENDA","FAZENDAS ATUALIZADAS!");
                    exemplo_alerta("ATUALIZANDO FAZENDAS...","FAZENDAS ATUALIZADAS COM SUCESSO!");
                    chamarLista();
                }else {
                    exemplo_alerta("FAZENDA ERRO!","FALHA AO SALVAR OS DADOS DAS FAZENDAS, TENTE NOVAMENTE!");
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteParcela(String response, String fazenda) {
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabelaInventario("i_parcela",fazenda);
                //database.LimparTabela("i_parcela");
                Long id = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idparcela", obj.get("idparcela").toString());
                    queryValues.put("parcela", obj.get("parcela").toString());
                    queryValues.put("area_parcela", obj.get("area_parcela").toString());
                    queryValues.put("cultivar", obj.get("cultivar").toString());
                    queryValues.put("id_fazenda", obj.get("id_fazenda").toString());
                    id =  database.insertParcela(queryValues);
                }
                if(id != null){
                    barProgressDialogGeral.hide();
                    exemplo_alerta_parcela("PARCELAS",fazenda+", PARCELAS ATUALIZADAS! ", fazenda);
                }else {
                    exemplo_alerta("ERRO...","Falha ao salvar os dados!");
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteLinha(String response, String fazenda) {
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Toast.makeText(this, "Preparando "+arr.length()+" Linhas", Toast.LENGTH_SHORT).show();
            if (arr.length() != 0) {
                //database.LimparTabela("i_linha");
                Long id = null;
                int countPlantas = 0;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idlinha", obj.get("idlinha").toString());
                    queryValues.put("linha", obj.get("linha").toString());
                    queryValues.put("planta_inicial", obj.get("planta_inicial").toString());
                    queryValues.put("planta_final", obj.get("planta_final").toString());
                    queryValues.put("quant_planta", obj.get("quant_planta").toString());
                    queryValues.put("id_parcela", obj.get("id_parcela").toString());
                    countPlantas = countPlantas + new Integer(obj.get("quant_planta").toString());
                    id =  database.insertLinha(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Inventário","Plantas: "+countPlantas+" e Linhas:  "+ arr.length()+" da fazenda: "+fazenda+" Armazenadas");
                    //barProgressDialog.dismiss();
                    resumo();
                }else {
                    //barProgressDialog.dismiss();
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro Linhas: " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void exemplo_alerta_fazenda(String titulo, String msg, final String fazenda) {
        barProgressDialogGeral = new ProgressDialog(this);
        barProgressDialogGeral.setTitle("Sincronizando, aguarde...");
        barProgressDialogGeral.setMessage("Baixando Informações ...");
        barProgressDialogGeral.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialogGeral.setCancelable(FALSE);
        barProgressDialogGeral.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                AsyncHttpClient client = new AsyncHttpClient();
                urlParcela = "http://adm.syspalma.com/index.php/webservice/parcela?fazenda="+fazenda;
                //client.setTimeout(3000);
                RequestParams params = new RequestParams();
                // Janela de Progresso
                client.get(urlParcela, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        updateSQLiteParcela(response, fazenda);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        exemplo_alerta("PARCELA ERRO!","FALHA: "+statusCode);
                        barProgressDialogGeral.hide();
                    }
                });
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                barProgressDialogGeral.hide();
            }
        });
        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    private void exemplo_alerta_parcela(String titulo, String msg, final String fazenda) {
        barProgressDialogGeral = new ProgressDialog(this);
        barProgressDialogGeral.setTitle("Sincronizando, aguarde...");
        barProgressDialogGeral.setMessage("Baixando Informações ...");
        barProgressDialogGeral.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialogGeral.setCancelable(FALSE);
        barProgressDialogGeral.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        builder.setCancelable(false);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                AsyncHttpClient client = new AsyncHttpClient();
                //client.setTimeout(3000);
                RequestParams params = new RequestParams();
                //INSERE AS LINHAS POR FAZENDA
                urlLinha = "http://adm.syspalma.com/index.php/webservice/linha?fazenda="+fazenda;
                //urlLinha = "http://adm.syspalma.com/webservice/linha?fazenda=Ariramba%20I";
                // Janela de Progresso
                client.get(urlLinha, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        updateSQLiteLinha(response,fazenda);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        exemplo_alerta("PARCELAS ERRO!","LINHAS: "+statusCode);
                        barProgressDialogGeral.hide();
                    }
                    @Override
                    public void onFinish() {
                        barProgressDialogGeral.hide();
                        exemplo_alerta("PARCELAS","Inventário Atualizado com sucesso! ");
                    }
                });
                //barProgressDialog.hide();

            }
        });
        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    public void resumo(){
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        //Verifica se é a parcela toda ou intervalo de linhas

            sql.append(" SELECT SUM(quant_planta) as quant_planta,");
            sql.append(" COUNT(idlinha) as linhas,");
            sql.append(" (SELECT SUM(area_parcela) FROM i_parcela) as area,");
            sql.append(" (SELECT COUNT(idparcela) FROM i_parcela) as parcela,");
            sql.append(" (SELECT COUNT(idfazenda) FROM i_fazenda) as fazenda,");
            sql.append(" (SELECT COUNT(idcolaborador) FROM p_colaborador) as mdo,");
            sql.append(" (SELECT COUNT(atividade) FROM c_atividade) as atividade,");
            sql.append(" (SELECT COUNT(idpatrimonio) FROM c_patrimonio) as patrimonio,");
            sql.append(" (SELECT COUNT(idagf_agricultor) FROM agf_agricultor) as agf,");
            sql.append(" (SELECT COUNT(idplanejado) FROM planejado) as planejado  FROM i_linha l");

        Cursor c = database.rawQuery(sql.toString(), null);
        if(c.getCount() > 0){
            c.moveToFirst();
            txtmdo.setText(c.getString(5));
            txtatividade.setText(c.getString(6));
            txtpatrimonio.setText(c.getString(7));
            txtplanejado.setText(c.getString(9));
            txtinventario.setText("Fazendas: "+c.getString(4)+" | Parcelas: "+c.getString(3)+" | Área: "+c.getString(2)+" | Linhas: "+c.getString(1)+" | Plantas: "+c.getString(0));
            txtagf.setText(c.getString(8));
        }

    }

    class DownloadDados extends AsyncTask<Void, Void, String> {
        private final Activity activity;
        private final ProgressDialog barProgressDialog;

        public DownloadDados(Configuracao activity) {
            this.activity = activity;
            barProgressDialog = new ProgressDialog(activity);
        }
        protected void onPreExecute() {
            barProgressDialog.setTitle("Sincronizando, aguarde...");
            barProgressDialog.setMessage("Aguarde ... Sincronizando Itens");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(FALSE);
            barProgressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://adm.syspalma.com/index.php/webservice/linha");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }
                return reader.readLine().toString();
                //return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String dados) {
            Toast.makeText(Configuracao.this, "novo: "+dados, Toast.LENGTH_SHORT).show();
            Log.i("Resultado", "onPostExecute: "+dados);
            barProgressDialog.dismiss();
        }
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaFazenda();

        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_lista_fazendas, new String[]{
                "idrealizado"}, new int[]{R.id.fazenda});
        myListView.setAdapter(adapter);
    }
}
