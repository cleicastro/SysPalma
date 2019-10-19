package com.ryatec.syspalma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ryatec.syspalma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static java.lang.Boolean.FALSE;

public class SyspalmaOnline extends AppCompatActivity {
    private ListView myListView;
    private String urlFichas = "http://adm.syspalma.com/webservice/listfichaindividual?";
    private String urlApontamento = "http://adm.syspalma.com/webservice/listfichaapontamento?ficha=";
    private String urlApontamentoIndividual = "http://adm.syspalma.com/webservice/list_ficha_apontamento_individual?";
    private boolean fichaon = true, matriculaon = false;
    private String ficha = null;
    private String data_inicial, data_final;
    private static String FISCAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syspalma_online);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data_inicial =  getIntent().getStringExtra("data_inicial");
        data_final =  getIntent().getStringExtra("data_final");
        if(GetSetUsuario.getMatricula().equals("1120237")){
            FISCAL = "1120061";
        }else{
            FISCAL = GetSetUsuario.getMatricula();
        }

        myListView = (ListView) findViewById(R.id.listFicha);
        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.atualizar);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView cod = (TextView) view.findViewById(R.id.ficha);
                TextView label_ficha = (TextView) view.findViewById(R.id.ficha2);
                label_ficha.setText("Matricula:");
                String codget = cod.getText().toString();
                if(fichaon){
                    ficha = codget;
                }
                fichaon = false;
                if(!fichaon && !matriculaon){
                    syncSQLite(urlApontamento+ficha, "apontamento");
                    matriculaon = true;
                }else{
                    label_ficha.setText("Apontamento:");
                    syncSQLite(urlApontamentoIndividual+"ficha="+ficha+"&matricula="+codget, "individual");
                    matriculaon = false;
                }
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerificaConexao(SyspalmaOnline.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    syncSQLite(urlFichas+"data_inicial="+data_inicial+"&data_final="+data_final+"&fiscal="+FISCAL, "fichas");
                    fichaon = true;
                    matriculaon = false;
                    TextView label_ficha = (TextView) findViewById(R.id.ficha2);
                    label_ficha.setText("Ficha:");
                }else {
                    Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //wifi.setWifiEnabled(false); //desabilita
                    wifi.setWifiEnabled(true); //habilita
                }
            }
        });
        syncSQLite(urlFichas+"data_inicial="+data_inicial+"&data_final="+data_final+"&fiscal="+FISCAL, "fichas");

    }
    public void chamarLista(String response){
        ArrayList<HashMap<String, String>> queryValues;
        queryValues = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Long id = null;
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    String data = obj.get("data_realizado").toString().replaceAll("-", "/");
                    String[] s = data.split("/");
                    String dataFormat = s[2]+"/"+s[1]+"/"+s[0];

                    map.put("idrealizado", obj.get("ficha").toString());
                    map.put("id_responsavel_tecnico", obj.get("nome").toString());
                    map.put("data_realizado", dataFormat);
                    map.put("id_responsavel_operacional", obj.get("fazenda").toString() + " | "+obj.get("atividade").toString());
                    queryValues.add(map);
                }
                Mensagem("Buscando...","Informações encontradas!");
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> userList = queryValues;

        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});

        myListView.setAdapter(adapter);
    }
    public void chamarListaApontamento(String response){
        ArrayList<HashMap<String, String>> queryValues;
        queryValues = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Long id = null;
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("idrealizado", obj.get("matricula_mdo").toString());
                    map.put("id_responsavel_tecnico", obj.get("plantas").toString()+" Plantas");
                    map.put("data_realizado", obj.get("matricula_mdo").toString());
                    map.put("id_responsavel_operacional", obj.get("area_realizada").toString()+" hectares");
                    queryValues.add(map);
                }
                Mensagem("Buscando...","Informações encontradas!");
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> userList = queryValues;

        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});

        myListView.setAdapter(adapter);
    }
    public void chamarListaIndividual(String response){
        ArrayList<HashMap<String, String>> queryValues;
        queryValues = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Long id = null;
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("idrealizado", obj.get("apontamento").toString());
                    map.put("id_responsavel_tecnico", obj.get("plantas").toString()+" Plantas");
                    map.put("data_realizado", obj.get("parcela").toString() + " | " + obj.get("cachos").toString()+" Cachos");
                    map.put("id_responsavel_operacional", obj.get("area_realizada").toString()+" hectares");
                    queryValues.add(map);
                }
                Mensagem("Buscando...","Informações encontradas!");
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> userList = queryValues;

        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});

        myListView.setAdapter(adapter);
    }

    public void syncSQLite(String urlParse, final String modelo) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Buscando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        RequestParams params = new RequestParams();
        // Janela de Progresso
        client.get(urlParse, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if(modelo == "fichas"){
                    chamarLista(response);
                }else if(modelo == "apontamento"){
                    chamarListaApontamento(response);
                }else if(modelo == "individual"){
                    chamarListaIndividual(response);
                }

                barProgressDialog.hide();

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
    public void Mensagem(String Titulo, String Msg){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
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
}
