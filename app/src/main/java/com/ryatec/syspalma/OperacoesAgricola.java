package com.ryatec.syspalma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class OperacoesAgricola extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView myListView;
    private GetSetCache getSetCache = new GetSetCache();
    private TextView mdo;
    private TextView ha;
    private TextView plantas;
    private TextView faltas;
    private ConstraintLayout statistic;
    private String insertReturn = "não";
    private Integer ficha_os = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacoes_agricola);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Colheita");

        //FECHA A INTENT CASO O USUÁRIO NÃO SEJA SETADO
        if(GetSetUsuario.getMatricula() == null){
            finish();
        }

        myListView = (ListView) findViewById(R.id.listFicha);
        mdo = findViewById(R.id.esMdo);
        ha = findViewById(R.id.esha);
        plantas = findViewById(R.id.esplantas);
        faltas = findViewById(R.id.esfalta);
        statistic = findViewById(R.id.rowLayout);
        //DESABILITA O BOTÃO ENVIAR DA TELA PRINCIPAL
        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.sync_enviar);
        sync.setVisibility(View.INVISIBLE);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView os = (TextView) view.findViewById(R.id.ordemServico);
                TextView idplanejamento = (TextView) view.findViewById(R.id.ficha);
                TextView idfazenda = (TextView) view.findViewById(R.id.textIdFazenda);
                TextView getfazenda = (TextView) view.findViewById(R.id.fazenda);
                TextView getatividade = (TextView) view.findViewById(R.id.atividade);

                if(ficha_os == 0 && os.getText().toString() != "" ) {
                    getSetCache.setOsPlanejamento(os.getText().toString());
                    getSetCache.setIdPlanejamento(new Integer(idplanejamento.getText().toString()));
                    getSetCache.setIdFazenda(new Integer(idfazenda.getText().toString()));
                    getSetCache.setFazendaGet(getfazenda.getText().toString());
                    getSetCache.setAtividade(getatividade.getText().toString().toUpperCase());
                    getSetCache.setOperacao(getatividade.getText().toString());
                    Intent intent = new Intent(getApplication(), MainFicha.class);
                    startActivity(intent);
                }else if(ficha_os == 1 && idplanejamento.getText().toString() != ""){
                    TextView data = (TextView) view.findViewById(R.id.data);
                }else{
                    Toast.makeText(getApplication(), "Atividade sem planejamento, favor sincronize as atividades!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Abrindo fichas", Snackbar.LENGTH_LONG)
                        .setAction("Carregando...", null).show();
                Intent intent = new Intent(getApplication(),MainFichaGeral.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GetSetCache.setAtividadeTipo("Colheita");
        //chamarLista("Colheita");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onResume() {
        super.onResume();

        //VERIFICA SE EXISTE FICHA PENDENTES PARA ENVIO
        Database_syspalma Banco = new Database_syspalma(this);
        Integer fichas = Banco.contarFicha();
        if(fichas > 0){
            Mensagem("ATENÇÃO!","VOCÊ TEM FICHAS PRONTAS PARA SINCRONIZAR");
        }
        chamarLista(toolbar.getSubtitle().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operacoes_agricola, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplication(),Configuracao.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.adubacao) {
            GetSetCache.setAtividadeTipo("Adubação");
            chamarLista("Adubação");
            toolbar.setSubtitle("Adubação");
        } else if (id == R.id.rocagem) {
            GetSetCache.setAtividadeTipo("Roçagem");
            chamarLista("Roçagem");
            toolbar.setSubtitle("Roçagem");
        } else if (id == R.id.coroamento) {
            GetSetCache.setAtividadeTipo("Coroamento");
            chamarLista("Coroamento");
            toolbar.setSubtitle("Coroamento");
        } else if (id == R.id.poda) {
            GetSetCache.setAtividadeTipo("Poda");
            chamarLista("Poda");
            toolbar.setSubtitle("Poda");
        } else if (id == R.id.herbicida) {
            GetSetCache.setAtividadeTipo("Herbicida");
            chamarLista("Herbicida");
            toolbar.setSubtitle("Herbicida");
        } else if (id == R.id.corte) {
            GetSetCache.setAtividadeTipo("Colheita");
            chamarLista("Colheita");
            toolbar.setSubtitle("Corte");
        } else if (id == R.id.carreamento) {
            GetSetCache.setAtividadeTipo("Carreamento");
            chamarLista("Colheita");
            toolbar.setSubtitle("Carreamento");
        } else if (id == R.id.fichas_menu) {
            Intent intent = new Intent(getApplication(),MainFichaGeral.class);
            startActivity(intent);
        }else if(id == R.id.transporte_cff){
            Intent intent = null;
            if(!GetSetUsuario.getTipo().equals("Padrão")){
                intent = new Intent(getApplication(),AgricolaiProgCaixa.class);
            }else{
                intent = new Intent(getApplication(),AgricolaProgramacaoList.class);
            }
            startActivity(intent);
        }else if(id == R.id.pluviometria){
            Intent intent = new Intent(getApplication(),AgricolaPluviometriaList.class);
            startActivity(intent);
        }
        else if (id == R.id.online) {
            if(VerificaConexao(getApplication())){
                Intent intent = new Intent(getApplication(), PainelProducaoOline.class);
                startActivity(intent);
            }else{
                Mensagem("Atenção","Verifique os campos e a conexão com a internet!");
            }
            /*
            if (VerificaConexao(this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                Intent intent = new Intent(getApplication(), PainelProducaoOline.class);
                startActivity(intent);
            }else {
                Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                //wifi.setWifiEnabled(false); //desabilita
                wifi.setWifiEnabled(true); //habilita
            }
            */

        }
        else if (id == R.id.config) {
            Intent intent = new Intent(getApplication(),Configuracao.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void chamarLista(String ATIVIDADE){
        //setContentView(R.layout.activity_operacoes_agricola);
        myListView.setVisibility(View.VISIBLE);
        //statistic.setVisibility(View.GONE);
        ficha_os = 0;

        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaPlanejado(ATIVIDADE);

        ListAdapter adapter = new SimpleAdapter(getApplication(), userList, R.layout.fragment_resumo_os, new String[]{
                "id","fazenda","ordem_servico","descricao","data_inicio","data_fim","ha","idfazenda","atividade"}, new int[]{R.id.ficha, R.id.fazenda,R.id.ordemServico,
                R.id.descricao, R.id.dataInicio, R.id.dataFim, R.id.ha, R.id.textIdFazenda,R.id.atividade});
        myListView.setAdapter(adapter);
    }
    public String InsertWebserverRealizado() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        ArrayList<JSONObject> realizado = db.SelectRealizadoSync(0);

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
                    Mensagem("Sincronizando  fichas...","FALHA NA EXPORTAÇÃO DOS DADOS! ");
                }
            }
        }.execute();
        return insertReturn;
    }
    public String InsertWebserverRealizadoApontamento() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> realizadoApont = db.SelectidRealizadoApontamentoSync(0);

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
                    Mensagem("Sincronizando apontamentos...","FALHA NA EXPORTAÇÃO DOS DADOS! "+result);
                }
            }
        }.execute();
        return insertReturn;
    }
    public String InsertWebserverRealizadoPatrimonio() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        ArrayList<JSONObject> realizado = db.SelectRealizadoPatrimonioSync(0);

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
        ArrayList<JSONObject> realizado = db.SelectRealizadoColheitaSync(0);

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
                chamarLista("Colheita");
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
}

