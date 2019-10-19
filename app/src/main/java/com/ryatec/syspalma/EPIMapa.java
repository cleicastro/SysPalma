package com.ryatec.syspalma;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

public class EPIMapa extends AppCompatActivity {

    ListView myListView;
    String entregaSet = "DE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epimapa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        syncSQLite();

        myListView = findViewById(R.id.list_mapa);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Atualizando Lista", Snackbar.LENGTH_LONG)
                        .setAction("Ação", null).show();
                syncSQLite();
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idepi_requisicao = view.findViewById(R.id.idFicha);
                TextView get_mat_req = view.findViewById(R.id.resp_tec);
                TextView get_epi_req = view.findViewById(R.id.data);
                TextView get_quantidade_req = view.findViewById(R.id.resp_op);

                final String idReq = idepi_requisicao.getText().toString();
                String matReq = get_mat_req.getText().toString();
                String epiReq = get_epi_req.getText().toString();
                String quantReq = get_quantidade_req.getText().toString();

                if(matReq.equals(GetSetUsuario.getMatricula())){
                    matReq = "1120";
                    quantReq = "";
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EPIMapa.this);
                alertDialog.setTitle("MAPA DE DISTRIBUIÇÃO DE EPI");
                alertDialog.setMessage("");
                alertDialog.setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_mapa_epi, null);
                final EditText matricula = mView.findViewById(R.id.matricula);
                final EditText EPI = mView.findViewById(R.id.EPI);
                final EditText quantidade = mView.findViewById(R.id.quantidade);
                final EditText data = mView.findViewById(R.id.data);
                Button salvar = mView.findViewById(R.id.salvarSaida);

                RadioGroup radioGroup = mView.findViewById(R.id.radioEntrega);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // find which radio button is selected
                        if(checkedId == R.id.radioEX) {
                            entregaSet = "EX";
                        } else if(checkedId == R.id.radioDN) {
                            entregaSet = "DN";
                        } else {
                            entregaSet = "DE";
                        }
                    }
                });

                //Recupera os dados da requisição
                matricula.setText(matReq);
                EPI.setText(epiReq);
                quantidade.setText(quantReq);
                alertDialog.setIcon(R.drawable.ic_map_black_24dp);

                //CÓDIGO PARA ALTERAR DATA
                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener dategs = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        data.setText(dateFormatter.format(myCalendar.getTime()));
                    }
                };
                data.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(EPIMapa.this, dategs, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        data.setText(dateFormatter.format(myCalendar.getTime()));
                    }
                });

                //Bnt Salvar
                salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(data.getText().toString().isEmpty() || matricula.getText().toString().length() < 5 || idReq.isEmpty() || entregaSet.isEmpty() || quantidade.getText().toString().isEmpty()){
                            exemplo_alerta("Dados Obrigatórios","Atenção! Verifique todos os dados digitados "+
                                    data.getText().toString()+", "+matricula.getText().toString()+", "+idReq+", "+entregaSet+", "+quantidade.getText().toString());
                        }else{
                            ArrayList<JSONObject> dataArrays = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject();

                            String dataSetGet = data.getText().toString().replaceAll("/", "-");
                            String[] s = dataSetGet.split("-");
                            String novaData = s[2]+"-"+s[1]+"-"+s[0];

                            try {
                                jsonObject.put("id_epi_requisicao", idReq);
                                jsonObject.put("data_mapa", novaData);
                                jsonObject.put("matricula", matricula.getText().toString());
                                jsonObject.put("quantidade", quantidade.getText().toString());
                                jsonObject.put("devolucao", entregaSet);
                                dataArrays.add(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            InsertWebserverProgramacao(dataArrays);
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.setView(mView);
                alertDialog.show();
            }
        });

    }
    private void chamarlista(ArrayList<HashMap<String, String>> userList){
        Database_syspalma db = new Database_syspalma(this);
        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_epi_mapa, new String[]{
                "token","quantidade","descricao","data_requisicao","matricula_requisitante","idepi_requisicao"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data, R.id.data_req,
                R.id.resp_tec, R.id.idFicha});
        myListView.setAdapter(adapter);
    }

    public void syncSQLite() {
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
        client.get("http://adm.syspalma.com/webservice/requisicao_epi?responsavel="+GetSetUsuario.getMatricula(), params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                updateSQLiteOS(response);
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
                ArrayList<HashMap<String, String>> queryValues;
                queryValues = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("idepi_requisicao", obj.get("idepi_requisicao").toString());
                    map.put("token", obj.get("token").toString());
                    map.put("matricula_requisitante", obj.get("matricula_requisitante").toString());
                    map.put("descricao", obj.get("descricao").toString());
                    map.put("data_requisicao", obj.get("data_requisicao").toString());
                    map.put("quantidade", obj.get("quantidade").toString());
                    queryValues.add(map);
                }
                chamarlista(queryValues);
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
    private void mensagem(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent refresh = new Intent(getBaseContext(), EPIMapa.class);
                startActivity(refresh);
                finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

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
                mensagem("Salvando",result);
            }
        }.execute();
    }
    public String retornoSync(List<NameValuePair> nameValuePairs){
        String url = "http://adm.syspalma.com/webservice/enviar_mapa_epi";
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
}

