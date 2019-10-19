package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class PainelAbastecimento extends AppCompatActivity {
    private CardView agricola;
    private CardView frota;
    private CardView fito;
    private CardView transferir;
    private CardView syncdados;
    private TextView matricula, nome, funcao;
    private FloatingActionButton sair;
    private String insertReturn = "não";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_abastecimento);

        //FECHA A INTENT CASO O USUÁRIO NÃO SEJA SETADO
        if (GetSetUsuario.getMatricula() == null) {
            finish();
        }

        agricola = (CardView) findViewById(R.id.posto_externo);
        frota = (CardView) findViewById(R.id.posto_comboio);
        fito = (CardView) findViewById(R.id.posto_interno);
        transferir = (CardView) findViewById(R.id.list_tanque);
        syncdados = (CardView) findViewById(R.id.sync_enviar_abastecimento);

        agricola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelAbastecimento.this, Frota_Abastecimento_externo.class);
                startActivity(intent);
            }
        });
        frota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelAbastecimento.this, MainAbastecimentosTanque.class);
                intent.putExtra("posto","COMBOIO");
                startActivity(intent);
            }
        });
        fito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelAbastecimento.this, MainAbastecimentosTanque.class);
                intent.putExtra("posto","TANQUE");
                startActivity(intent);
            }
        });
        transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelAbastecimento.this, AbastecimentosTanque.class);
                startActivity(intent);
            }
        });
        syncdados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerificaConexao(PainelAbastecimento.this)) { //Se o objeto for nulo ou nao tem conectividade retorna false
                    String insert = InsertWebserverCompra();
                    //Toast.makeText(MainFichaGeral.this, "valor: "+insert, Toast.LENGTH_SHORT).show();
                }else {
                    Mensagem("Atenção!","Você precisa de uma conexão com a internet para realizar está ação, seu wifi será habilitado e tente novamente.");
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //wifi.setWifiEnabled(false); //desabilita
                    wifi.setWifiEnabled(true); //habilita
                }
            }
        });
    }

    public void Mensagem(String Titulo, String Msg) {
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(PainelAbastecimento.this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
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

    public String InsertWebserverCompra() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> compra = db.SelectCompraSync();

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",compra.toString()));


        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "rc_compra");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    InsertWebserverAbastecimentoInterno();
                    Mensagem("Sincronizando abastecimentos externos...","DADOS SINCRONIZADOS COM SUCESSO! ");
                    db.LimparTabela("rc_compra");
                }else{
                    if(compra.isEmpty()){
                        InsertWebserverAbastecimentoInterno();
                    }else{
                        Mensagem("Erro","FALHA NA EXPORTAÇÃO DOS DADOS! " + result);
                    }
                }
            }
        }.execute();
        return insertReturn;
    }
    public String InsertWebserverAbastecimentoInterno() {
        insertReturn = "não";
        final Database_syspalma db = new Database_syspalma(this);
        final ArrayList<JSONObject> abastecimento = db.SelectAbastecimentoSync();

        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",abastecimento.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs, "abastecimento_interno");
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                if(result.contains("Sim")){
                    insertReturn = "sim";
                    Mensagem("Sincronizando abastecimento...","DADOS SINCRONIZADOS COM SUCESSO! ");
                    db.LimparTabela("abastecimento_interno");
                }else{
                    Mensagem("Erro","FALHA NA EXPORTAÇÃO DOS DADOS! "+result+ "Dados: "+abastecimento.toString());
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

    private void btnSair() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle("SysPalma - Agrícola");//define o titulo
        builder.setMessage("Você deseja realmente sair da aplicação?");//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
}
