package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class PainelProducaoOline extends AppCompatActivity {
    private CardView list_ficha;
    private CardView list_producao;
    private CardView list_os;
    private CardView mapa_corte;
    private CardView mapa_carreamento;
    private EditText edt_data_inicial, edt_data_final;
    private FloatingActionButton sair;
    private String insertReturn = "não";
    private CalendarView data_inicial, data_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ficha_online);

        data_inicial = (CalendarView) findViewById(R.id.data_ini);
        data_final = (CalendarView) findViewById(R.id.data_fim);
        edt_data_inicial = (EditText) findViewById(R.id.data_inicial);
        edt_data_final = (EditText) findViewById(R.id.data_final);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        /*
        Date dataIni = new Date(data_inicial.getDate());
        Date dataFim = new Date(data_final.getDate());

        String dateFormatadaIni = dt.format(dataIni);
        String dateFormatadaFim = dt.format(dataFim);
        */
        Date data_hoje = new Date();
        String data_hoje_format = dt.format(data_hoje);

        edt_data_inicial.setText(data_hoje_format);
        edt_data_final.setText(data_hoje_format);

        data_inicial.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date dataIni = new Date(data_inicial.getDate());
                String dateFormatadaIni = dt.format(dataIni);
                edt_data_inicial.setText(dateFormatadaIni);
            }
        });
        data_final.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date dataIni = new Date(data_final.getDate());
                String dateFormatadaIni = dt.format(dataIni);
                edt_data_final.setText(dateFormatadaIni);
            }
        });

        //FECHA A INTENT CASO O USUÁRIO NÃO SEJA SETADO
        if (GetSetUsuario.getMatricula() == null) {
            finish();
        }

        list_ficha = (CardView) findViewById(R.id.list_ficha);
        list_producao = (CardView) findViewById(R.id.list_producao);
        list_os = (CardView) findViewById(R.id.os);
        mapa_corte = (CardView) findViewById(R.id.mapa_corte);
        mapa_carreamento = (CardView) findViewById(R.id.mapa_carreamento);

        list_ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelProducaoOline.this, SyspalmaOnline.class);
                intent.putExtra("data_inicial",edt_data_inicial.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                startActivity(intent);
            }
        });
        list_os.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PainelProducaoOline.this, FichadeServico.class);
                Intent intent = new Intent(PainelProducaoOline.this, SysPalmaOnlineOS.class);
                intent.putExtra("data_inicial",edt_data_inicial.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                startActivity(intent);
            }
        });
        list_producao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PainelProducaoOline.this, FichadeServico.class);
                Intent intent = new Intent(PainelProducaoOline.this, BuscaMatriculaApontamento.class);
                intent.putExtra("data_inicial",edt_data_inicial.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                startActivity(intent);
            }
        });

        //geração de mapas da atividade
        mapa_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PainelProducaoOline.this, FichadeServico.class);
                Intent intent = new Intent(PainelProducaoOline.this, SysPalmaOnlineOS.class);
                intent.putExtra("data_inicial",edt_data_inicial.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                intent.putExtra("atividade","CORTE");
                startActivity(intent);
            }
        });
        mapa_carreamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PainelProducaoOline.this, FichadeServico.class);
                Intent intent = new Intent(PainelProducaoOline.this, SysPalmaOnlineOS.class);
                intent.putExtra("data_inicial",edt_data_inicial.getText().toString());
                intent.putExtra("data_final",edt_data_final.getText().toString());
                intent.putExtra("atividade","CARREAMENTO");
                startActivity(intent);
            }
        });
    }

    public void Mensagem(String Titulo, String Msg) {
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(PainelProducaoOline.this);
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
