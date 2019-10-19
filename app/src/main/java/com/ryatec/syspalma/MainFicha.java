package com.ryatec.syspalma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ryatec.syspalma.R;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class MainFicha extends AppCompatActivity {
    private ListView myListView;
    private String os;
    private Integer idPlanejamento;
    private String atividade;
    private String insertReturn = "não";
    private GetSetCache getSetCache = new GetSetCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ficha);

        if(GetSetCache.getOsPlanejamento() == null){
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        atividade = getSetCache.getAtividade();
        idPlanejamento = getSetCache.getIdPlanejamento();
        os = getSetCache.getOsPlanejamento();

        getSupportActionBar().setTitle("Ficha de serviço ("+ getSetCache.getFazendaGet() +") ");
        getSupportActionBar().setSubtitle("OS: "+os+" / "+atividade);

        myListView = (ListView) findViewById(R.id.listFicha);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView data = (TextView) view.findViewById(R.id.data);
                TextView ficha = (TextView) view.findViewById(R.id.ficha);
                if(ficha.getText().toString() != "") {
                    getSetCache.setDataFicha(data.getText().toString());
                    getSetCache.setFicha(ficha.getText().toString());
                    if(GetSetCache.isFrota()){
                        Intent intent = new Intent(getApplication(), ApontamentoPatrimonio.class);
                        startActivity(intent);
                    }else if (GetSetCache.getAtividade().equals("TRANSPORTE")) {
                        Intent intent = new Intent(getApplication(), ApontamentoPatrimonio.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplication(), ApontamentoFragment.class);
                        startActivity(intent);
                    }
                }else{
                    Mensagem("Atenção","Favor, crie uma ficha de serviço");
                }
            }
        });
        //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView ficha = (TextView) view.findViewById(R.id.ficha);
                remover(ficha.getText().toString());
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.sync_enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                Intent intent = new Intent(getApplication(), FichaNovo.class);
                startActivity(intent);
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerificaConexao(MainFicha.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    InsertWebserverRealizado2();
                }else {
                    Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //wifi.setWifiEnabled(false); //desabilita
                    wifi.setWifiEnabled(true); //habilita
                }

                /*
                if(insert.equals("sim")){
                    String insertApont = InsertWebserverRealizadoApontamento();
                    if(insertApont.equals("sim")){
                        InsertWebserverRealizadoColheita();
                        InsertWebserverRealizadoPatrimonio();

                        Database_syspalma db = new Database_syspalma(getApplication());
                        db.deletarSync(GetSetCache.getIdPlanejamento());
                        chamarLista();
                    }
                }
                */
            }
        });
        chamarLista();
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaFicha(GetSetCache.getIdPlanejamento());

        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});
        myListView.setAdapter(adapter);
    }
    public void remover(final String fichaget){
        AlertDialog.Builder Alerta = new AlertDialog.Builder(this);
        Alerta.setTitle("Remover Apontamento");
        Alerta.setMessage("Deseja realmente remover este apontamento?");
        Alerta.setCancelable(false);

        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Database_syspalma db = new Database_syspalma(getApplication());
                db.deletarFicha(fichaget);
                Toast.makeText(getApplication(), "Ficha: " + fichaget, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplication(), "Removido", Toast.LENGTH_SHORT).show();
                chamarLista();
            }
        });
        Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Alerta.create();
        Alerta.show();
    }

    public String InsertWebserverRealizado() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        ArrayList<JSONObject> realizado = db.SelectRealizadoSync(GetSetCache.getIdPlanejamento());

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",realizado.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    db.deletarApontamentoErro();
                    InsertWebserverRealizadoApontamento();
                    Mensagem("Sincronizando fichas...","DADOS SINCRONIZADOS COM SUCESSO! ");
                }else{
                    Mensagem("Sincronizando  fichas...","FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                }
            }
        }.execute();
    return insertReturn;
    }
    public String InsertWebserverRealizadoApontamento() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> realizadoApont = db.SelectidRealizadoApontamentoSync(GetSetCache.getIdPlanejamento());

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",realizadoApont.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado_apontamento");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    InsertWebserverRealizadoPatrimonio();
                    InsertWebserverRealizadoColheita();
                    exemplo_alerta("Sincronizando apontamentos...","DADOS SINCRONIZADOS COM SUCESSO! ");
                    //InsertWebserverRealizadoPatrimonio();
                }else{
                    Mensagem("Sincronizando apontamentos...","FALHA NA EXPORTAÇÃO DOS DADOS! "+result+ "Dados: "+realizadoApont.toString());
                }
            }
        }.execute();
    return insertReturn;
    }
    public String InsertWebserverRealizadoPatrimonio() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        ArrayList<JSONObject> realizado = db.SelectRealizadoPatrimonioSync(GetSetCache.getIdPlanejamento());

        if(realizado.size()>0) {
            final ProgressDialog barProgressDialog = new ProgressDialog(this);
            barProgressDialog.setTitle("Sincronizando, aguarde...");
            barProgressDialog.setMessage("Aguarde ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.cancel();
            barProgressDialog.show();
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("json", realizado.toString()));

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return retornoSync(nameValuePairs, "realizado_patrimonio");
                }

                @Override
                protected void onPostExecute(String result) {
                    barProgressDialog.hide();
                    if (result.contains("Sim")) {
                        insertReturn = "não";
                        Mensagem("Sincronizando patrimônio...", "DADOS SINCRONIZADOS COM SUCESSO! ");
                    } else {
                        Mensagem("Sincronizando patrimônio...", "FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                    }
                    //InsertWebserverRealizadoColheita();
                }
            }.execute();
        }else{
            Mensagem("Sincronizando patrimônio...", "NÃO HÁ DADOS DE MAQUINÁRIOS PARA SINCRONIZAR!");
        }
    return insertReturn;
    }
    public String InsertWebserverRealizadoColheita() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        ArrayList<JSONObject> realizado = db.SelectRealizadoColheitaSync(GetSetCache.getIdPlanejamento());

        if(realizado.size()>0) {
            final ProgressDialog barProgressDialog = new ProgressDialog(this);
            barProgressDialog.setTitle("Sincronizando, aguarde...");
            barProgressDialog.setMessage("Aguarde ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.cancel();
            barProgressDialog.show();
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("json", realizado.toString()));

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return retornoSync(nameValuePairs, "realizado_colheita");
                }

                @Override
                protected void onPostExecute(String result) {
                    barProgressDialog.hide();
                    if (result.contains("Sim")) {
                        insertReturn = "sim";
                        Mensagem("Sincronizando colheita", "DADOS SINCRONIZADOS COM SUCESSO! ");
                    } else {
                        Mensagem("Sincronizando colheita", "FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                    }
                }
            }.execute();
        }else{
            Mensagem("Sincronizando colheita", "NÃO HÁ DADOS DE COLHEITA PARA SINCRONIZAR! ");
        }
        return insertReturn;
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
    public void Mensagem(String Titulo, String Msg){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }
    private void exemplo_alerta(String titulo, String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Database_syspalma db = new Database_syspalma(getApplication());
                db.deletarSync(GetSetCache.getIdPlanejamento());
                chamarLista();
            }
        });
        android.app.AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
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

    //NOVO METODO DE ENVIO
    public void InsertWebserverRealizado2() {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando fichas");
        barProgressDialog.setMessage("Aguarde, procecessando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();

        final ArrayList<JSONObject> realizado, realizadoApont;
        final Database_syspalma db = new Database_syspalma(this);
        //monta os dados locais para sincronizar
        realizado = db.SelectRealizadoSync(GetSetCache.getIdPlanejamento());
        realizadoApont = db.SelectidRealizadoApontamentoSync(GetSetCache.getIdPlanejamento());

        if(realizado.size() > 0){
            barProgressDialog.show();
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("json", realizado.toString()));

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return retornoSync(nameValuePairs, "realizado");
                }
                @Override
                protected void onPostExecute(String result) {
                    if (result.contains("Sim")) {
                        barProgressDialog.dismiss();
                        Toast.makeText(MainFicha.this, "Iniciando sincronização dos apontamentos :)", Toast.LENGTH_SHORT).show();
                        InsertWebserverRealizadoApontamento2(realizadoApont);
                    }else{
                        barProgressDialog.dismiss();
                        Mensagem("Falha na sincronização Realizado","Tente novamente! Houve um problema na comunicação com o servidor :(");
                    }
                }
            }.execute();
        }else{
            Mensagem("Atenção!","Você não possuí fichas para sincronizar!");
        }
    }

    public void InsertWebserverRealizadoApontamento2(ArrayList<JSONObject> realizadoApont) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando apontamentos");
        barProgressDialog.setMessage("Aguarde, procecessando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();

        final ArrayList<JSONObject> realizadoPatrimonio, realizadoColheita, realizadoFito;
        final Database_syspalma db = new Database_syspalma(this);
        //monta os dados locais para sincronizar
        realizadoPatrimonio = db.SelectRealizadoPatrimonioSync(GetSetCache.getIdPlanejamento());
        realizadoColheita = db.SelectRealizadoColheitaSync(GetSetCache.getIdPlanejamento());
        realizadoFito = db.SelectRealizadoFitoSync(GetSetCache.getIdPlanejamento());

        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json", realizadoApont.toString()));

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado_apontamento");
            }
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Sim")) {
                    //COLHEITA
                    if(realizadoColheita.size() > 0){
                        barProgressDialog.dismiss();
                        Toast.makeText(MainFicha.this, "Iniciando sincronização da colheita :)", Toast.LENGTH_SHORT).show();
                        InsertWebserverRealizadoColheita2(realizadoColheita);
                    }else if(realizadoPatrimonio.size() > 0){
                        barProgressDialog.dismiss();
                        Toast.makeText(MainFicha.this, "Iniciando sincronização dos maquinários :)", Toast.LENGTH_SHORT).show();
                        InsertWebserverRealizadoPatrimonio2(realizadoPatrimonio);
                    }else if(realizadoFito.size() > 0){
                        barProgressDialog.dismiss();
                        Toast.makeText(MainFicha.this, "Iniciando sincronização da fitossanidade :)", Toast.LENGTH_SHORT).show();
                        InsertWebserverRealizadoFitossanidade2(realizadoFito);
                    }else{
                        barProgressDialog.dismiss();
                        exemplo_alerta("Sincronizando finalizada", "OPERAÇÃO FINALIZADA COM SUCESSO, OBRIGADO POR ENVIAR SUAS INFORMAÇÕES! ");
                    }
                }else{
                    barProgressDialog.dismiss();
                    Mensagem("Falha na sincronização Apontamento","Tente novamente! Houve um problema na comunicação com o servidor :(");
                }
            }
        }.execute();
    }

    public void InsertWebserverRealizadoColheita2(ArrayList<JSONObject> realizadoColheita) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando Colheita");
        barProgressDialog.setMessage("Aguarde, procecessando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();

        final ArrayList<JSONObject> realizadoPatrimonio;
        final Database_syspalma db = new Database_syspalma(this);
        //monta os dados locais para sincronizar
        realizadoPatrimonio = db.SelectRealizadoPatrimonioSync(GetSetCache.getIdPlanejamento());

        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json", realizadoColheita.toString()));

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado_colheita");
            }
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Sim")) {
                    barProgressDialog.dismiss();
                    if(realizadoPatrimonio.size() > 0){
                        Toast.makeText(MainFicha.this, "Iniciando sincronização da maquinários :)", Toast.LENGTH_SHORT).show();
                        InsertWebserverRealizadoPatrimonio2(realizadoPatrimonio);
                    }else{
                        exemplo_alerta("Sincronizando finalizada", "OPERAÇÃO FINALIZADA COM SUCESSO, OBRIGADO POR ENVIAR SUAS INFORMAÇÕES! ");
                    }
                }else{
                    barProgressDialog.dismiss();
                    Mensagem("Falha na sincronização Colheita","Tente novamente! Houve um problema na comunicação com o servidor :( ");
                }
            }
        }.execute();
    }

    public void InsertWebserverRealizadoFitossanidade2(ArrayList<JSONObject> realizadoFito) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando Fito");
        barProgressDialog.setMessage("Aguarde, procecessando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();

        final ArrayList<JSONObject> realizadoPatrimonio;
        final Database_syspalma db = new Database_syspalma(this);
        //monta os dados locais para sincronizar
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json", realizadoFito.toString()));

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado_fitossanidade");
            }
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Sim")) {
                    barProgressDialog.dismiss();
                    exemplo_alerta("Sincronizando finalizada", "OPERAÇÃO FINALIZADA COM SUCESSO, OBRIGADO POR ENVIAR SUAS INFORMAÇÕES! ");
                }else{
                    barProgressDialog.dismiss();
                    Mensagem("Falha na sincronização Fitossanidade","Tente novamente! Houve um problema na comunicação com o servidor :( ");
                }
            }
        }.execute();
    }

    public void InsertWebserverRealizadoPatrimonio2(ArrayList<JSONObject> realizadoPatrimonio) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando Maquinários");
        barProgressDialog.setMessage("Aguarde, procecessando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();

        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json", realizadoPatrimonio.toString()));
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "realizado_patrimonio");
            }
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Sim")) {
                    exemplo_alerta("Sincronizando finalizada", "OPERAÇÃO FINALIZADA COM SUCESSO, OBRIGADO POR ENVIAR SUAS INFORMAÇÕES! ");
                }else{
                    barProgressDialog.dismiss();
                    Mensagem("Falha na sincronização Maquinários","Tente novamente! Houve um problema na comunicação com o servidor :(");
                }
            }
        }.execute();
    }

}
