package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class EPI_requisicao extends AppCompatActivity {

    private EditText data, cod_epi, requisicao, matricula;
    private AutoCompleteTextView epi;
    private EditText quantidade;
    private FloatingActionButton btlist;
    private Button salvar;
    private ListView list;
    private String data_req, req;
    private String insertReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epi_requisicao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        data_req = sdf.format(date);

        //Requisição
        SimpleDateFormat sdfReq = new SimpleDateFormat("ddMMyy");
        String dateStringReq = sdfReq.format(date);
        req = dateStringReq+GetSetUsuario.getMatricula().replace("1120","");

        data = (EditText) findViewById(R.id.data);
        epi = findViewById(R.id.item);
        cod_epi = findViewById(R.id.idepi);
        requisicao = findViewById(R.id.requisicao);
        matricula = findViewById(R.id.matricula);
        matricula.requestFocus(4);
        quantidade = findViewById(R.id.quantidade);
        salvar = findViewById(R.id.salvar);
        list = findViewById(R.id.list_epi_req);
        data.setText(data_req);
        requisicao.setText(req);

        ChamarLista();
        addItem();
        epi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getAdapter().getItem(position).toString();
                String[] s = value.split("; ");
                epi.setText(s[0]);
                cod_epi.setText(s[1]);
                quantidade.requestFocus();
            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cod_ep = cod_epi.getText().toString();
                String quant = quantidade.getText().toString();
                String mat_mdo = matricula.getText().toString();
                if(cod_ep.isEmpty() || quant.isEmpty() || mat_mdo.length()<5){
                    exemplo_alerta("Atenção","Verifique os campos digitados");
                }else{
                    Salvar();
                    Snackbar.make(view, "Salvando requisição de EPI, verifique na lista abaixo", Snackbar.LENGTH_LONG)
                            .setAction("Atualizano...", null).show();
                }
            }
        });
        epi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    cod_epi.setText("");
                    epi.setText("");
                }else{
                    String cod_ep = cod_epi.getText().toString();
                    if(cod_ep.isEmpty()){
                        exemplo_alerta("Atenção!","Você não selecionou nenhum EPI :)");
                    }
                }
            }
        });

        FloatingActionButton sync = findViewById(R.id.fab);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerificaConexao(EPI_requisicao.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    String insert = InsertWebserverRequisicao();
                    //Toast.makeText(MainFichaGeral.this, "valor: "+insert, Toast.LENGTH_SHORT).show();
                }else {
                    Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //wifi.setWifiEnabled(false); //desabilita
                    wifi.setWifiEnabled(true); //habilita
                }
            }
        });
        btlist = findViewById(R.id.list_req);
        btlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxLista();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView ViewId = (TextView) view.findViewById(R.id.id);
                Integer GetId = Integer.parseInt(ViewId.getText().toString());
                ConfirmacaoExclusao("Remover Requisição","Realmente deseja excluir este item? :(",GetId);
                return false;
            }
        });
    }
    private void Salvar(){
        Database_syspalma database = new Database_syspalma(this);
        String data_inicio = data.getText().toString().replaceAll("/", "-");
        String[] s = data_inicio.split("-");
        String novaData = s[2]+"/"+s[1]+"/"+s[0];
        Integer epi_cod = new Integer(cod_epi.getText().toString());
        Integer quantidade_set = new Integer(quantidade.getText().toString());

        GetSetEPI EPI = new GetSetEPI();
        EPI.setData(novaData);
        EPI.setId_insumo(epi_cod);
        EPI.setMatricula(matricula.getText().toString());
        EPI.setRequisicao(requisicao.getText().toString());
        EPI.setQuantidade(quantidade_set);
        database.InserirEPI(EPI);
        addItem();
        matricula.setText("1120");
        epi.setText("");
        cod_epi.setText("");
        quantidade.setText("");
        matricula.requestFocus(4);
    }
    public void addItem(){
        Database_syspalma database = new Database_syspalma(this);
        ListAdapter adapter = new SimpleAdapter(this, database.ListaRequisicaoEPI(requisicao.getText().toString()), R.layout.resumo_itens, new String[]{
                "id","n","item", "quantidade","total"}, new int[]{R.id.id, R.id.contItem, R.id.listItem, R.id.listQuant,R.id.listTotal});
        list.setAdapter(adapter);
    }

    public void ChamarLista(){
        Database_syspalma db = new Database_syspalma(getApplication());

        List<String> userListEPI = db.SelectEPI();
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, userListEPI);
        epi.setAdapter(adapter);
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
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    private void ConfirmacaoExclusao(String titulo, String msg, final Integer Id) {
        final Database_syspalma db = new Database_syspalma(this);

        android.support.v7.app.AlertDialog.Builder Alerta = new android.support.v7.app.AlertDialog.Builder(this);
        Alerta.setTitle(titulo);
        Alerta.setMessage(msg);
        Alerta.setCancelable(false);
        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deletarEPIRequisicao_item(Id);
                Toast.makeText(getApplication(),"Item: " +Id + " removido com sucesso ;)",Toast.LENGTH_SHORT).show();
                addItem();
            }
        });
        Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Alerta.show();
    }

    private void ConfigReq(String data_reqget, String reqget){
        data.setText(data_reqget);
        requisicao.setText(reqget);
        addItem();
    }

    private void BoxLista(){
        Database_syspalma database = new Database_syspalma(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EPI_requisicao.this, android.R.layout.select_dialog_singlechoice, database.ListaRequisicaoEPIHistorico());

        //arrayAdapter.add("13241321");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(EPI_requisicao.this);
        builderSingle.setIcon(R.drawable.ic_epi);
        builderSingle.setTitle("SELECIONE UMA REQUISIÇÃO :)");

        builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setPositiveButton("Novo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfigReq(data_req, req);
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strValores = arrayAdapter.getItem(which);
                String[] s = strValores.split(" - ");
                /*
                AlertDialog.Builder builderInner = new AlertDialog.Builder(EPI_requisicao.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
                */

                ConfigReq(s[1], s[0]);
                Toast.makeText(EPI_requisicao.this, "Você selecionou a requisição: "+strValores, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    public String retornoSync(List<NameValuePair> nameValuePairs, String tabela){
        String url = "http://adm.syspalma.com/webservice/inserir?tabela="+tabela;
        String response = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("CAT","POST Response >>> " + response);
        return response;
    }
    public String InsertWebserverRequisicao() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> epi_requisicao = db.SelectEPIRequisicaoSync();

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",epi_requisicao.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "epi_requisicao");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    Mensagem("Sincronizando requisições...","DADOS SINCRONIZADOS COM SUCESSO! ");
                    db.LimparTabela("epi_requisicao");
                    addItem();
                }else{
                    Mensagem("Erro","FALHA NA EXPORTAÇÃO DOS DADOS! "+result+ "Dados: "+epi_requisicao.toString());
                }
            }
        }.execute();
        return insertReturn;
    }
    public boolean VerificaConexao(Context contexto){
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager)contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if(conmag.getActiveNetworkInfo() != null
                && conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable()
                && conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
            conectado = true;
        }
        //Verifica o 3G
        else if(conmag.getActiveNetworkInfo() != null
                && conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable()
                && conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            conectado = true;
        }
        else{
            conectado = false;
        }
        return conectado;
    }

    public void Mensagem(String Titulo, String Msg) {
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(EPI_requisicao.this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

}
