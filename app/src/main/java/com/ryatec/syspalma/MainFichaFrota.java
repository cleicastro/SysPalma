package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class MainFichaFrota extends AppCompatActivity {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.listFicha);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView data = (TextView) view.findViewById(R.id.data);
                TextView ficha = (TextView) view.findViewById(R.id.ficha);
                if(ficha.getText().toString() != "") {
                    Intent intent = new Intent(getApplication(), ApontamentoPatrimonio.class);
                    intent.putExtra("apontamento_get",ficha.getText().toString());
                    startActivity(intent);
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
                Intent intent = new Intent(getApplication(), FichaNovoFrota.class);
                startActivity(intent);
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerificaConexao(MainFichaFrota.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    String insert = InsertWebserverRealizado();
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
        ArrayList<HashMap<String, String>> userList = db.ListaFichaFrota(GetSetUsuario.getMatricula());

        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);
        /*
        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});
        */
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
                Toast.makeText(getApplication(), "Apontamento: " + fichaget, Toast.LENGTH_SHORT).show();
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
        ArrayList<JSONObject> realizado = db.SelectRealizadoPatrimonioAtividadeSync(GetSetUsuario.getMatricula());

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
                return retornoSync(nameValuePairs, "realizado_apontamento_patrimonio");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    exemplo_alerta("Sincronizando apontamentos...","DADOS SINCRONIZADOS COM SUCESSO! ");
                }else{
                    Mensagem("Sincronizando  apontamentos...","FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                }
            }
        }.execute();
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
                db.deletarSyncFrota(GetSetUsuario.getMatricula());
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

    public class AdapterCursosPersonalizado extends BaseAdapter {
        ArrayList<HashMap<String, String>> list;
        Activity a;
        LayoutInflater myiflater;
        public AdapterCursosPersonalizado(Activity activity, ArrayList<HashMap<String, String>> list) {
            this.a=activity;
            this.list=list;
            myiflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = a.getLayoutInflater()
                    .inflate(R.layout.fragment_resumo_ficha, viewGroup, false);

            final HashMap<String, String> map = list.get(i);
            Database_syspalma db = new Database_syspalma(a);

            TextView apontamento = (TextView) view.findViewById(R.id.ficha);
            TextView resp = (TextView) view.findViewById(R.id.resp_op);
            TextView data = (TextView) view.findViewById(R.id.data);
            TextView km_desc = (TextView) view.findViewById(R.id.resp_tec);

            apontamento.setText(map.get("idrealizado"));
            resp.setText(map.get("id_responsavel_tecnico"));
            data.setText(map.get("data_realizado"));
            km_desc.setText(map.get("id_responsavel_operacional"));
            String rodados = map.get("status");
            if(rodados == "não"){
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            return view;
        }
    }

}
