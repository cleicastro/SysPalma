package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static java.lang.Boolean.FALSE;

public class AgricolaiProgCaixa extends AppCompatActivity {
    private ListView myListView;
    private Switch protocolado;
    private Spinner fazendas;
    private String url = "http://adm.syspalma.com/webservice/getProgramacaoCaixas";
    private String urlFazendas = "http://adm.syspalma.com/webservice/fazenda";
    FloatingActionButton atualizar, enviar;
    private ArrayList listProg, listfazendas, listcaixas, listcheia, listvazia;
    private String fazenda_get = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syspalma_online);

        listProg = new ArrayList();
        listfazendas = new ArrayList();
        listcaixas = new ArrayList();
        listcheia = new ArrayList();
        listvazia = new ArrayList();

        myListView = findViewById(R.id.listFicha);
        protocolado = findViewById(R.id.protocolado);
        fazendas = findViewById(R.id.fazendas);
        atualizar = findViewById(R.id.atualizar);
        enviar = findViewById(R.id.enviar);
        enviar.setVisibility(View.VISIBLE);

        syncFazendaGet(urlFazendas);
        //syncSQLite(url);

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerificaConexao(getApplication())){
                    String protocolado_get = "A";
                    if(protocolado.isChecked()){
                        protocolado_get = "T";
                    }
                    syncSQLite(url+"?protocolados="+protocolado_get+"&fazenda="+fazenda_get);
                    listProg.clear();
                    listfazendas.clear();
                    listcaixas.clear();
                    listcheia.clear();
                    listvazia.clear();
                }else{
                    Mensagem("Conexão","Você precisa de uma conexão com a internet");
                }
            }
        });
        protocolado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    syncSQLite(url+"?protocolados=T&fazenda="+fazenda_get);
                }else{
                    syncSQLite(url+"?protocolados=A&fazenda="+fazenda_get);
                }
            }
        });
        fazendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {
                String selected_fazenda = fazendas.getSelectedItem().toString();
                fazenda_get = selected_fazenda;
                String protocolado_get = "A";
                if(arg2 == 0){
                    fazenda_get = "";
                }
                if(protocolado.isChecked()){
                    protocolado_get = "T";
                }
                listProg.clear();
                listfazendas.clear();
                listcaixas.clear();
                listcheia.clear();
                listvazia.clear();
                syncSQLite(url+"?protocolados="+protocolado_get+"&fazenda="+fazenda_get);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerificaConexao(getApplication())){
                    Intent intent = new Intent(AgricolaiProgCaixa.this, AgricolaProgForm.class);
                    intent.putExtra("list_prog",listProg);
                    intent.putExtra("list_fazendas",listfazendas);
                    intent.putExtra("list_caixas",listcaixas);
                    intent.putExtra("list_cheia",listcheia);
                    intent.putExtra("list_vazia",listvazia);
                    startActivity(intent);
                    listProg.clear();
                    listfazendas.clear();
                    listcaixas.clear();
                    listcheia.clear();
                    listvazia.clear();
                    syncSQLite(url+"?protocolados=A&fazenda="+fazenda_get);
                }else{
                    Mensagem("Conexão","Você precisa de uma conexão com a internet");
                }

            }
        });
    }
    public void onResume() {
        if (VerificaConexao(getApplication())){
            String protocolado_get = "A";
            if(protocolado.isChecked()){
                protocolado_get = "T";
            }
            syncSQLite(url+"?protocolados="+protocolado_get+"&fazenda="+fazenda_get);
            listProg.clear();
            listfazendas.clear();
            listcaixas.clear();
            listcheia.clear();
            listvazia.clear();
        }else{
            Mensagem("Conexão","Você precisa de uma conexão com a internet");
        }
        super.onResume();
    }
    public void fazendasSet(String response){
        List<String> queryValues;
        queryValues = new ArrayList<>();
        queryValues.add("SELECIONE UMA FAZENDA");
        try {
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues.add(obj.get("fazenda").toString());
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        queryValues.add("Nakata");
        List<String> userList = queryValues;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getApplicationContext(),android.R.layout.simple_spinner_item, userList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fazendas.setAdapter(dataAdapter);
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

                    String data = obj.get("data_programacao").toString().replaceAll("-", "/");
                    String[] s = data.split("/");
                    String dataFormat = s[2]+"/"+s[1]+"/"+s[0];

                    map.put("idprogramacao", obj.get("idprogramacao").toString());
                    map.put("cod_prog_caixa", obj.get("cod_prog_caixa").toString());
                    map.put("matricula_mdo", obj.get("matricula_mdo").toString());
                    map.put("data_programacao", dataFormat);
                    map.put("caixa_i", obj.get("caixa_i").toString());
                    map.put("caixa_ii", obj.get("caixa_ii").toString());
                    map.put("fazenda", obj.get("fazenda").toString());
                    map.put("area_ref_cheia", obj.get("area_ref_cheia").toString());
                    map.put("area_ref_vazia", obj.get("area_ref_vazia").toString());
                    map.put("placa", obj.get("placa").toString());
                    map.put("motorista", obj.get("motorista").toString());
                    map.put("cpf", obj.get("cpf").toString());
                    map.put("transportadora", obj.get("transportadora").toString());
                    map.put("toneladas", obj.get("toneladas").toString());
                    map.put("status", obj.get("status").toString());
                    map.put("protocolo", obj.get("protocolo").toString());
                    queryValues.add(map);
                }
                //Mensagem("Buscando...","Informações encontradas!");
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> userList = queryValues;

        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);

        myListView.setAdapter(adapter);
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
                chamarLista(response);
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
    public void syncFazendaGet(String urlParse) {
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
                fazendasSet(response);
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

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = a.getLayoutInflater()
                    .inflate(R.layout.content_list_progr_caixa, viewGroup, false);

            final HashMap<String, String> map = list.get(i);
            if(map.get("idprogramacao")!= null) {
                ImageView img = (ImageView) view.findViewById(R.id.imgIndustria);
                final TextView txtid = (TextView) view.findViewById(R.id.textId);
                final CardView select = view.findViewById(R.id.cardViewSelect);

                //Exibi todos os dados
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mensagem("Programação: "+map.get("cod_prog_caixa"),
                                "Responsável: "+map.get("matricula_mdo")+"\n"+
                                        "Cod.: "+map.get("cod_prog_caixa")+"\n"+
                                        "Data: "+map.get("data_programacao")+"\n"+
                                        "Caixa i: "+map.get("caixa_i")+"\n"+
                                        "Caixa ii: "+map.get("caixa_ii")+"\n"+
                                        "Fazenda: "+map.get("fazenda")+"\n"+
                                        "Responsável: "+map.get("matricula_mdo")+"\n"+
                                        "Área ref. cheia: "+map.get("area_ref_cheia")+"\n"+
                                        "Área ref. vazia: "+map.get("area_ref_vazia")+"\n"+
                                        "Placa: "+map.get("placa")+"\n"+
                                        "CPF: "+map.get("cpf")+"\n"+
                                        "Transportadora: "+map.get("transportadora")+"\n"+
                                        "Ton: "+map.get("toneladas")+"\n"+
                                        "Status: "+map.get("status")+"\n"+
                                        "Protocolo: "+map.get("protocolo")+"\n"
                        );
                    }
                });

                //CRIA O METODO PARA SELECIONAR APENAS OS AGUARDANDO CARREGAMENTO
                if(map.get("status").equals("T")){
                    select.setCardBackgroundColor(Color.CYAN);
                    img.setImageResource(R.drawable.ic_balance);
                }else{
                    select.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //CASO JÁ ESTEJA SELECIONADO ADICIONA NA LISTA DE TRANSPONTE
                            if(txtid.getText().toString().equals("")){
                                select.setCardBackgroundColor(Color.GREEN);

                                //configura o campo da caixa para exibir mais de uma caixa
                                String caixas_view = map.get("caixa_i");
                                if(!map.get("caixa_ii").contains("null") ){
                                    caixas_view += " | "+map.get("caixa_ii");
                                }
                                listProg.add(map.get("cod_prog_caixa"));
                                listfazendas.add(map.get("fazenda"));
                                listcheia.add(map.get("area_ref_cheia"));
                                listvazia.add(map.get("area_ref_vazia"));
                                listcaixas.add(caixas_view);
                                txtid.setText(map.get("idprogramacao"));

                            }else{
                                select.setCardBackgroundColor(Color.WHITE);
                                //BUSCA O ID DO ELEMENTO PARA REMOVER DA LISTA
                                for(int i=0; i < listProg.size(); ++i){
                                    if(listProg.get(i).equals(map.get("cod_prog_caixa"))){
                                        listProg.remove(i);
                                        listfazendas.remove(i);
                                        listcaixas.remove(i);
                                        listcheia.remove(i);
                                        listvazia.remove(i);
                                    }
                                }
                                txtid.setText(map.get(""));
                            }
                            Toast.makeText(a, "Registros: "+listProg.toString()+" | "+listProg.size(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }

                TextView fazenda = (TextView) view.findViewById(R.id.txtfazenda);
                TextView data = (TextView) view.findViewById(R.id.txtdata);
                TextView caixas = (TextView) view.findViewById(R.id.txtcaixas);
                TextView txtresp = (TextView) view.findViewById(R.id.txtresponsavel);

                //configura o campo da caixa para exibir mais de uma caixa
                String caixas_view = map.get("caixa_i");
                if(!map.get("caixa_ii").contains("null") ){
                    caixas_view += " | "+map.get("caixa_ii");
                }

                txtresp.setText(map.get("matricula_mdo"));
                data.setText(map.get("data_programacao"));
                caixas.setText(caixas_view);
                fazenda.setText(map.get("fazenda"));

            }else{
                TextView caixas = (TextView) view.findViewById(R.id.txtcaixas);
                caixas.setText("SEM PROGRAMAÇÃO NO MOMENTO");
            }
            return view;
        }
    }
}
