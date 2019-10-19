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
import android.widget.ImageView;
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

public class AgricolaPluviometriaList extends AppCompatActivity {
    private ListView myListView;
    private String insertReturn = "não";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ficha);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.listFicha);
        //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView cod = (TextView) view.findViewById(R.id.ficha);
                String codProgramacao = cod.getText().toString();
                remover("pluviometria", "idpluviometria",codProgramacao);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_chuva);
        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.sync_enviar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AgricolaPluviometria.class);
                startActivity(intent);
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerificaConexao(AgricolaPluviometriaList.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    String insert = InsertWebserverPluviometria();
                }else {
                    Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //wifi.setWifiEnabled(false); //desabilita
                    wifi.setWifiEnabled(true); //habilita
                }
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
        ArrayList<HashMap<String, String>> userList = db.ListaPluviometria();

        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);
        /*
        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});
        */
        myListView.setAdapter(adapter);
    }
    public void remover(final String tabela, final String coluna, final String idCod){
        AlertDialog.Builder Alerta = new AlertDialog.Builder(this);
        Alerta.setTitle("Remover Pluviometria");
        Alerta.setMessage("Deseja realmente remover esta informação?");
        Alerta.setCancelable(false);

        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Database_syspalma db = new Database_syspalma(getApplication());
                db.deletarRegistroFuncao(tabela, coluna, idCod);
                Toast.makeText(getApplication(), "Pluviometria: " + idCod, Toast.LENGTH_SHORT).show();
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
    public String InsertWebserverPluviometria() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> pluviometria = db.SelectPluviometriaSync();

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",pluviometria.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "pluviometria");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    Mensagem("Sincronizando Pluviometria...","DADOS SINCRONIZADOS COM SUCESSO! ");
                    db.LimparTabela("pluviometria");
                    chamarLista();
                }else{
                    Mensagem("Erro","FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                }
            }
        }.execute();
        return insertReturn;
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

            TextView idCupom = (TextView) view.findViewById(R.id.idFicha);
            TextView label_text = (TextView) view.findViewById(R.id.ficha2);
            TextView apontamento = (TextView) view.findViewById(R.id.ficha);
            TextView resp = (TextView) view.findViewById(R.id.resp_op);
            TextView data = (TextView) view.findViewById(R.id.data);
            TextView km_desc = (TextView) view.findViewById(R.id.resp_tec);
            ImageView img = (ImageView) view.findViewById(R.id.imageView_geral);

            label_text.setText("Cód.");
            idCupom.setText(map.get("idpluviometria"));
            apontamento.setText(map.get("idpluviometria"));
            km_desc.setText(map.get("fazenda")+" \t "+map.get("quantidade")+" mm");
            resp.setText("");
            data.setText(map.get("data"));

            Double quant_mm = new Double(map.get("quantidade"));
            if(quant_mm > 0 && quant_mm < 8)
            {
                img.setImageResource(R.drawable.ic_nublado);
            }else if(quant_mm >= 8 && quant_mm < 25)
            {
                img.setImageResource(R.drawable.ic_chuva);
            }else if(quant_mm >= 25)
            {
                img.setImageResource(R.drawable.ic_raio);
            }else{
                img.setImageResource(R.drawable.ic_sol2);
            }
            return view;
        }
    }

}
