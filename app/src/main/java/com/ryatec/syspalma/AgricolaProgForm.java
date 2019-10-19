package com.ryatec.syspalma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
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

import static java.lang.Boolean.FALSE;

public class AgricolaProgForm extends AppCompatActivity {
    private Spinner placa;
    private AutoCompleteTextView motorista;
    private Button salvar;
    private EditText cpf, transportadora, toneladas;
    private TextView list_prog;
    private String url = "http://adm.syspalma.com/webservice/mdo";
    private ArrayList programados;
    private ArrayList fazendas;
    private ArrayList caixas;
    private ArrayList cheia;
    private ArrayList vazia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricola_prog_form);
        placa = findViewById(R.id.placa);
        list_prog = findViewById(R.id.list_prog);
        salvar = findViewById(R.id.salvar);
        motorista = findViewById(R.id.motorista);
        cpf = findViewById(R.id.cpf);
        transportadora = findViewById(R.id.transportadora);
        toneladas = findViewById(R.id.toneladas);
        programados = getIntent().getStringArrayListExtra("list_prog");
        fazendas = getIntent().getStringArrayListExtra("list_fazendas");
        caixas = getIntent().getStringArrayListExtra("list_caixas");
        cheia = getIntent().getStringArrayListExtra("list_cheia");
        vazia = getIntent().getStringArrayListExtra("list_vazia");

        String listagem = "";
        for (int i = 0; i < programados.size(); i++){
            listagem += "\nProgramação: "+programados.get(i).toString()+
                    " | Fazenda: "+fazendas.get(i).toString()+
                    " | Caixas: "+caixas.get(i).toString();
        }
        list_prog.setText(listagem);

        chamarLista();
        syncSQLite(url);
        motorista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getAdapter().getItem(position).toString();
                String[] s = value.split(", ");
                motorista.setText(s[0]);
                cpf.setText(s[1]);
                transportadora.setText(s[2]);
            }
        });

        motorista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(cpf.getText().toString().isEmpty() || transportadora.getText().toString().isEmpty()){
                        Mensagem("AGRICULTOR","SELECIONE UM MOTORISTA CORRETAMENTE :(");
                    }
                }
            }
        });

        motorista.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_DEL) {
                    cpf.setText("");
                    transportadora.setText("");
                }
                return false;
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(placa.getSelectedItemPosition() > 0
                        && motorista.getText().length() > 0
                        && cpf.getText().length() > 0
                        && transportadora.getText().length() > 0
                        && toneladas.getText().length() > 0 && VerificaConexao(getApplication())){

                    //PERCORRE A LISTAGEM DAS PROGRAMAÇÕES
                    String getprotocolo, getplaca, getmotorista, getcpf, gettransportadora, gettoneladas;
                    ArrayList<JSONObject> dataArrays = new ArrayList<>();

                    //GERA O NÚMERO DO PROTOCOLO
                    final long date = System.currentTimeMillis();
                    SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                    String dateStringToken = sdfToken.format(date);
                    String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","");

                    getplaca = placa.getSelectedItem().toString();
                    getmotorista = motorista.getText().toString();
                    getcpf = cpf.getText().toString();
                    gettoneladas = toneladas.getText().toString();
                    gettransportadora = transportadora.getText().toString();
                    getprotocolo = cod;

                    for (int i = 0; i < programados.size(); i++){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("cod_prog_caixa", programados.get(i).toString());
                            jsonObject.put("placa", getplaca);
                            jsonObject.put("motorista", getmotorista);
                            jsonObject.put("cpf", getcpf);
                            jsonObject.put("transportadora", gettransportadora);
                            jsonObject.put("toneladas", gettoneladas);
                            jsonObject.put("protocolo", getprotocolo);
                            jsonObject.put("status", "T");
                            dataArrays.add(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    InsertWebserverProgramacao(dataArrays);
                }else{
                    Mensagem("Atenção","Verifique os campos e a conexão com a internet!");
                }

            }
        });
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(getApplication());

        List<String> patrimonios = db.SelectPatrimonio("'CARRETA 3 EIXOS'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplication(), R.layout.spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        placa.setAdapter(dataAdapter2);
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
    public void getValues(String response){
        ArrayList<String> queryValues;
        queryValues = new ArrayList<String>();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            Long id = null;
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("matricula", obj.get("matricula").toString());
                    map.put("nome", obj.get("nome").toString());
                    map.put("funcao", obj.get("funcao").toString());
                    map.put("empresa", obj.get("empresa").toString());
                    map.put("cpf", obj.get("cpf").toString());
                    //Add no array list
                    queryValues.add(obj.get("nome").toString()+", "+obj.get("cpf").toString()+", "+obj.get("empresa").toString());
                }
                //Mensagem("Buscando...","Informações encontradas!");
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ArrayList<String> userList = queryValues;

        ArrayAdapter<String>  adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, userList);
        motorista.setAdapter(adapter);
    }
    public void syncSQLite(String urlParse) {
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
                getValues(response);
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
    public void MensagemSalvar(String Titulo, String Msg, final ArrayList<JSONObject> valores ){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setCancelable(false);
        Alerta.setPositiveButton("Enviar Email Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Email(valores);
                finish();
            }
        }).setNegativeButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
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

    //ENVIAR AS INFORMAÇÕES PARA O WEBSERVICE
    public void InsertWebserverProgramacao(final ArrayList<JSONObject> valores) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Aguarde ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.cancel();
        barProgressDialog.show();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("json",valores.toString()));

        new AsyncTask<Void,Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return retornoSync(nameValuePairs);
            }
            @Override
            protected void onPostExecute(String result) {
                barProgressDialog.hide();
                MensagemSalvar("Programação",result, valores);
                if(!result.contains("não")){
                  //finish();
                    Email(valores);
                }
            }
        }.execute();
    }
    public String retornoSync(List<NameValuePair> nameValuePairs){
        String url = "http://adm.syspalma.com/webservice/enviarProgramacao";
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
    public void Email(ArrayList<JSONObject> valores) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);

        //SELECIONA O NÚMERO DO PROTOCOLO
        JSONObject obj = valores.get(0);
        String protocolo = null;
        try {
            protocolo = obj.get("protocolo").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //CORPO DO EMAIL
        StringBuilder email = new StringBuilder();
        email.append("Data: " + data_completa);
        email.append("<br /> Protocolo: " + protocolo);
        for (int i = 0; i < valores.size(); i++) {
            JSONObject obj_valores = valores.get(i);
            try {
                email.append("<div style='border: 1px solid; text-align: center; padding: 5px; margin-button: 10px; background:yellow'>" + fazendas.get(i).toString() + "</div>");
                email.append("<div style='border: 1px solid; float: left; padding: 5px; width:20%'>");
                email.append("<div>Caixas</div>");
                email.append("<div>Toneladas</div>");
                email.append("<div>Placa</div>");
                email.append("<div>Motorista</div>");
                email.append("<div>CPF</div>");
                email.append("<div>Transportadora</div>");
                email.append("<div>Caixa Cheia</div>");
                email.append("<div>Caixa Vazia</div>");
                email.append("</div>");

                email.append("<div style='border: 1px solid; padding: 5px; margin-button: 10px;'>");
                email.append("<div>" + caixas.get(i).toString() + "</div>");
                email.append("<div>" + obj_valores.get("toneladas").toString() + "</div>");
                email.append("<div>" + obj_valores.get("placa").toString() + "</div>");
                email.append("<div>" + obj_valores.get("motorista").toString() + "</div>");
                email.append("<div>" + obj_valores.get("cpf").toString() + "</div>");
                email.append("<div>" + obj_valores.get("transportadora").toString() + "</div>");
                email.append("<div>" + cheia.get(i).toString() + "</div>");
                email.append("<div>" + vazia.get(i).toString() + "</div>");
                email.append("</div> <p />");
                email.append("<p />");
                email.append("<p />");
                email.append("<p /><p />");
                email.append("Att, <p /> <p />" +
                        "Agrícola <br />" +
                        "SysPalma <br />" +
                        "Transporte de CFF <br />");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] TO = {"jeane.oliveira@adm.com", "shirlley.costa@adm.com"};

        String[] CC = new String[0];
        if (transportadora.getText().toString().equals("Palmlog")) {
            CC = new String[]{"raimundo.cavalcante@adm.com", "e divan.amorim@adm.com", "erasmo.ferreira@adm.com", "renara.santos@adm.com", "palmlogt@gmail.com", "harlisonm.borges@adm.com", "duselene.silva@adm.com"};
        } else if (transportadora.getText().toString().equals("Palmlog")) {
            CC = new String[]{"raimundo.cavalcante@adm.com", "edivan.amorim@adm.com", "erasmo.ferreira@adm.com", "renara.santos@adm.com", "financeiro@brtransp.com.br", "harlisonm.borges@adm.com", "duselene.silva@adm.com"};
        } else {
            CC = new String[]{"raimundo.cavalcante@adm.com", "edivan.amorim@adm.com", "erasmo.ferreira@adm.com", "renara.santos@adm.com", "harlisonm.borges@adm.com", "duselene.silva@adm.com"};
        }

        String titulo = protocolo + " " + data_completa;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, titulo);
        emailIntent.putExtra(Intent.EXTRA_TEXT, email.toString());
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviando Email :D"));
            //finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AgricolaProgForm.this, "Nenhum app de email instalado", Toast.LENGTH_SHORT).show();
        }
    }
}
