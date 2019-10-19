package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditarApontamento extends Activity {

    private Button btnApontamento;
    private Button btnQuantMdo;
    private Button btnColheita;
    private Button btnPatrimonio;
    private Button btnInsumo;
    private Button btnApoio;
    private ExpandableRelativeLayout mExpandApontamento;
    private ExpandableRelativeLayout mExpandQuantMdo;
    private ExpandableRelativeLayout mExpandColheita;
    private ExpandableRelativeLayout mExpandPatrimonio;
    private ExpandableRelativeLayout mExpandInsumo;
    private ExpandableRelativeLayout mExpandApoio;

    private Spinner parcela;
    private Spinner patrimonio;
    private Spinner implemento;
    private Spinner atividade;
    private Spinner caixa;

    private EditText edtLinhaInicial;
    private EditText edtLinhaFinal;
    private EditText edtPlantas;
    private EditText edtTotalPlantas;
    private EditText edtHectares;
    private EditText edtCachos;
    private EditText edtPeso;
    private EditText edtPesoTotal;
    private EditText edtKMincial;
    private EditText edtMatMDO;
    private EditText edtKMfinal;
    private EditText edtQuantMdo;
    private TextView txtDetalhe;
    private LinearLayout linhaQnt;
    private LinearLayout linhaApoio;
    private Integer idParcela;
    private Double areaCache;

    static String id;
    private Button salvarApontamento;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_expandlelayout);

        ScrollView telaprincipal = (ScrollView) findViewById(R.id.activity_main);
        //telaprincipal.setBackgroundColor(Color.parseColor("#FFFFFF"));

        //REMOVE O CAMPO DE DISTRIBUIIÇÃO AUTOMATICA DE LINHAS
        linhaQnt = findViewById(R.id.bac);
        linhaQnt.setVisibility(View.GONE);

        //Removi o linearLayout do apoio
        //linhaApoio = findViewById(R.id.bacajudande);
        //linhaApoio.setVisibility(View.GONE);
        //Recuperar o ID passado como parâmentro do intent
        id = getIntent().getStringExtra("idget");

        btnApontamento = (Button) findViewById(R.id.btnApontamento);
        btnColheita = (Button) findViewById(R.id.btnColheita);
        btnPatrimonio = (Button) findViewById(R.id.btnPatrimonio);
        btnInsumo = (Button) findViewById(R.id.btnInsumo);
        btnQuantMdo = (Button) findViewById(R.id.btnDivisaoMdo);
        btnApoio = (Button) findViewById(R.id.btnDivisaoMdoApoio);

        btnApontamento.setBackgroundColor(Color.parseColor("#E6E6E6"));
        btnColheita.setBackgroundColor(Color.parseColor("#E6E6E6"));
        btnPatrimonio.setBackgroundColor(Color.parseColor("#E6E6E6"));
        btnInsumo.setBackgroundColor(Color.parseColor("#E6E6E6"));
        btnQuantMdo.setBackgroundColor(Color.parseColor("#E6E6E6"));

        mExpandApontamento = (ExpandableRelativeLayout) findViewById(R.id.mExpandApontamento);
        mExpandColheita = (ExpandableRelativeLayout) findViewById(R.id.mExpandColheita);
        mExpandPatrimonio = (ExpandableRelativeLayout) findViewById(R.id.mExpandPatrimonio);
        mExpandInsumo = (ExpandableRelativeLayout) findViewById(R.id.mExpandInsumo);
        mExpandQuantMdo = (ExpandableRelativeLayout) findViewById(R.id.mExpandDivisaoMdo);
        mExpandApoio = (ExpandableRelativeLayout) findViewById(R.id.mExpandDivisaoMdoApoio);

        parcela = (Spinner) findViewById(R.id.placa);
        implemento = (Spinner) findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) findViewById(R.id.spinner_patrimonio);
        atividade = (Spinner) findViewById(R.id.motorista);
        caixa = (Spinner) findViewById(R.id.spinnerCaixa);
        salvarApontamento= findViewById(R.id.salvar);
        salvarApontamento.setText("Editar");

        //Remover a aba de apoio
        btnApoio.setVisibility(View.GONE);
        mExpandApoio.setVisibility(View.GONE);

        //VERIFICA A ATIVIDADE PARA PRELOAD DAS ATIVIDADES
        if (GetSetCache.getAtividade().equals("CARREAMENTO")){
            String[] carreamento = getResources().getStringArray(R.array.array_atividades_carreamento);
            ArrayAdapter<String> dataAdapterl = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, carreamento);
            dataAdapterl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            atividade.setAdapter(dataAdapterl);
        }else{
            String[] corte = getResources().getStringArray(R.array.array_atividades);
            ArrayAdapter<String> dataAdapterl = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, corte);
            dataAdapterl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            atividade.setAdapter(dataAdapterl);
        }

        edtMatMDO = (EditText) findViewById(R.id.matricula_mdo);
        edtQuantMdo = (EditText) findViewById(R.id.editQuantmdo);
        edtLinhaInicial = (EditText) findViewById(R.id.cpf);
        edtLinhaFinal = (EditText) findViewById(R.id.transportadora);
        edtPlantas = (EditText) findViewById(R.id.toneladas);
        edtTotalPlantas = (EditText) findViewById(R.id.editTotalPlantas);
        edtHectares = (EditText) findViewById(R.id.editHectares);

        edtCachos = (EditText) findViewById(R.id.editCachos);
        edtPeso = (EditText) findViewById(R.id.editPeso);
        edtPesoTotal = (EditText) findViewById(R.id.editPesoTotal);
        edtKMincial = (EditText) findViewById(R.id.editkmInicial);
        edtKMfinal = (EditText) findViewById(R.id.editkmFinal);
        txtDetalhe = (TextView) findViewById(R.id.dysplayHect);

        //VERIFICAR SE A MATRICULA ESTÁ ATIVA
        edtMatMDO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Database_syspalma database = new Database_syspalma(getApplicationContext());
                if(!hasFocus){
                    String matriculaget = database.RetoronoIdMat(edtMatMDO.getText().toString());
                    if(matriculaget == null){
                        alerta("Busca Matricula","Verifique se esta matricula está ativa ou cadastrada no sistema!");
                        //edtMatMDO.setText("");
                        //edtMatMDO.requestFocus();
                    }
                }
            }
        });

        btnApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandApontamento.toggle();
            }
        });

        btnQuantMdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandQuantMdo.toggle();
            }
        });

        btnColheita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandColheita.toggle();
                edtCachos.requestFocus(edtCachos.getText().length());
            }
        });

        btnPatrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandPatrimonio.toggle();
            }
        });

        btnInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandInsumo.toggle();
            }
        });

        carregar_valores();
        carregarValoresEdit(id);
        //Toast.makeText(this, "teste"+id, Toast.LENGTH_SHORT).show();
        //Quando selecionar a atividade retorna o a área da parcela realizada

        atividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getParcela = parcela.getSelectedItem().toString();
                String getAtividade = parent.getSelectedItem().toString();
                //retornar o detalhe da parcela
                Database_syspalma database_syspalma = new Database_syspalma(getApplicationContext());
                ArrayList<HashMap<String, String>> dtlApontParcela = database_syspalma.RetornoDifLinhas(GetSetCache.getFazendaGet(),
                        getParcela, getAtividade);
                //Caso rentorne nulo, retona o total da parcela em hectares
                if(dtlApontParcela.size() >0) {
                    txtDetalhe.setText(dtlApontParcela.get(0).get("parcela") + " | Restando: " + dtlApontParcela.get(0).get("area_restando") +
                            " ha | Pessoas: " + dtlApontParcela.get(0).get("mdo"));
                } else{
                    String areaParcTotal = edtHectares.getText().toString();
                    txtDetalhe.setText(getParcela+" | Restando: "+areaParcTotal+" ha | Pessoas: 0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        parcela.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getParcela = parent.getSelectedItem().toString();
                String getAtividade = atividade.getSelectedItem().toString();
                BuscaParcela(GetSetCache.getFazendaGet(), getParcela,0,0);
                edtTotalPlantas.setText(edtPlantas.getText().toString());
                //retornar o detalhe da parcela
                Database_syspalma database_syspalma = new Database_syspalma(getApplicationContext());
                ArrayList<HashMap<String, String>> dtlApontParcela = database_syspalma.RetornoDifLinhas(GetSetCache.getFazendaGet(),
                        getParcela, getAtividade);
                //Caso rentorne nulo, retona o total da parcela em hectares
                if(dtlApontParcela.size() >0) {
                    txtDetalhe.setText(dtlApontParcela.get(0).get("parcela") + " | Restando: " + dtlApontParcela.get(0).get("area_restando") +
                            " ha | Pessoas: " + dtlApontParcela.get(0).get("mdo"));
                } else{
                    String areaParcTotal = edtHectares.getText().toString();
                    txtDetalhe.setText(getParcela+" | Restando: "+areaParcTotal+" ha | Pessoas: 0");
                }
                Integer linhaIni = new Integer(edtLinhaInicial.getText().toString());
                Integer linhaFim = new Integer(edtLinhaFinal.getText().toString());
                //Buscar id parcela
                ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, linhaIni, linhaFim);
                if(inventario.size() > 0){
                    edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                    edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                    edtPlantas.setText(inventario.get(0).get("plantas").toString());
                    edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());
                    idParcela = new Integer(inventario.get(0).get("idParcela"));

                    //CALCULOS NA REGRA DE 3 PARA O TAMANHO DA PARCELA
                    Double haGet = new Double(inventario.get(0).get("hectares").toString());
                    Integer plantasGet = new Integer(inventario.get(0).get("plantas").toString());
                    Integer totalplantasGet = new Integer(edtTotalPlantas.getText().toString());

                    Double area_calculada = (haGet / totalplantasGet) * plantasGet;
                    DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                    edtHectares.setText(ndf.format(area_calculada).toString());
                    areaCache = area_calculada;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edtPlantas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!edtPlantas.getText().toString().isEmpty()){
                        Integer plt = new Integer(edtPlantas.getText().toString());
                        Integer totalPlt = new Integer(edtTotalPlantas.getText().toString());

                        Double area_calculada = (areaCache / totalPlt)*plt;
                        DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                        edtHectares.setText(ndf.format(area_calculada).toString());
                    }
                }
            }
        });
        edtLinhaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!edtLinhaInicial.getText().toString().isEmpty() || !edtLinhaFinal.getText().toString().isEmpty()) {
                        Integer lin_in = 1;
                        Integer lin_fin = 1;
                        if(!edtLinhaInicial.getText().toString().isEmpty()){
                            lin_in = new Integer(edtLinhaInicial.getText().toString());
                            lin_fin = new Integer(edtLinhaFinal.getText().toString());
                        }
                        String getParcela = parcela.getSelectedItem().toString();
                        ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);

                        if(inventario.size() > 0) {
                            edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                            edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                            edtPlantas.setText(inventario.get(0).get("plantas").toString());
                            //edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                            //CALCULOS NA REGRA DE 3 PARA O TAMANHO DA PARCELA
                            Double haGet = new Double(inventario.get(0).get("hectares").toString());
                            Integer plantasGet = new Integer(inventario.get(0).get("plantas").toString());
                            Integer totalplantasGet = new Integer(edtTotalPlantas.getText().toString());

                            Double area_calculada = (haGet / totalplantasGet) * plantasGet;
                            DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            edtHectares.setText(ndf.format(area_calculada).toString());
                        }
                    }
                }
            }
        });
        edtLinhaFinal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus)
                {
                    if (!edtLinhaFinal.getText().toString().isEmpty() || !edtLinhaInicial.getText().toString().isEmpty()) {
                        Integer lin_in = 1;
                        Integer lin_fin = 1;
                        if(!edtLinhaFinal.getText().toString().isEmpty()){
                            lin_in = new Integer(edtLinhaInicial.getText().toString());
                            lin_fin = new Integer(edtLinhaFinal.getText().toString());
                        }
                        String getParcela = parcela.getSelectedItem().toString();
                        ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);

                        if(inventario.size() > 0){
                            edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                            edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                            edtPlantas.setText(inventario.get(0).get("plantas").toString());
                            //edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                            //CALCULOS NA REGRA DE 3 PARA O TAMANHO DA PARCELA
                            Double haGet = new Double(inventario.get(0).get("hectares").toString());
                            Integer plantasGet = new Integer(inventario.get(0).get("plantas").toString());
                            Integer totalplantasGet = new Integer(edtTotalPlantas.getText().toString());

                            Double area_calculada = (haGet / totalplantasGet) * plantasGet;
                            DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            edtHectares.setText(ndf.format(area_calculada).toString());
                        }
                    }
                }
            }
        });
        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Database_syspalma database = new Database_syspalma(getApplication());
                    Database_syspalma_backup database_backup = new Database_syspalma_backup(getApplication());
                    //CARREGAR OS DADOS DOS MAQUINÁRIOS
                    Integer trator = 1;
                    Integer implement = 1;
                    if (patrimonio.getSelectedItemPosition() > 0) {
                        trator = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                        implement = database.RetoronoIdImplemento(implemento.getSelectedItem().toString());
                    }

                    database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                    database.RetoronoIdImplemento(implemento.getSelectedItem().toString());

                    GetSetAtividade getatividade = new GetSetAtividade();
                    getatividade.setId_parcela(idParcela);
                    getatividade.setAtividade(atividade.getSelectedItem().toString());
                    getatividade.setId_mdo(edtMatMDO.getText().toString());
                    getatividade.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                    getatividade.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                    getatividade.setPlantas(new Integer(edtPlantas.getText().toString()));
                    getatividade.setArea_realizada(new Double(edtHectares.getText().toString()));

                    getatividade.setCachos(new Integer(edtCachos.getText().toString()));
                    getatividade.setPeso(new Double(edtPeso.getText().toString()));
                    getatividade.setCaixa(new Integer(caixa.getSelectedItem().toString()));

                    getatividade.setId_patrimonio(trator);
                    getatividade.setId_implemento(implement);
                    getatividade.setMarcador_inicial(new Double(edtKMincial.getText().toString()));
                    getatividade.setMarcador_final(new Double(edtKMfinal.getText().toString()));

                    //CASO A ATIVIDADE NÃO SEJA DE PRODUÇÃO ARMAZENA A PARCELA PADRÃO 1
                    Integer atividadeExt = atividade.getSelectedItemPosition();
                    Integer parcelaFic = parcela.getSelectedItemPosition();
                    if(parcelaFic == 0 && atividadeExt>0){
                        getatividade.setId_parcela(1);
                    }
                    database.alteraApontamento(getatividade, id);
                    database_backup.alteraApontamento(getatividade, id);

                    exemplo_alerta("Apontamento", "Apontamento alterado com sucesso!");
                }catch (SQLException erro){
                    exemplo_alerta("Banco de Dados","Erro: " + erro);
                }
            }
        });
        Button cancelar = (Button) findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void carregar_valores(){

        Database_syspalma db = new Database_syspalma(this);
        List<String> parcelas = db.SelectParcela(GetSetCache.getFazendaGet());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, parcelas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcela.setAdapter(dataAdapter);

        List<String> patrimonios = db.SelectPatrimonio("'TRATOR'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> implementos = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, implementos);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        implemento.setAdapter(dataAdapter3);

        List<String> atividades = db.SelectAtividade(GetSetCache.getOperacao());
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, atividades);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        atividade.setAdapter(dataAdapter4);
    }

    public ArrayList<HashMap<String, String>> BuscaParcela(String fazenda, String parcela, Integer linhaInicial, Integer linhaFinal){
        ArrayList<HashMap<String, String>> campo;
        campo = new ArrayList<HashMap<String, String>>();
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        //Verifica se é a parcela toda ou intervalo de linhas
        if(linhaInicial == 0|| linhaFinal == 0) {
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'");
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }else{
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'  AND l.linha BETWEEN "+linhaInicial+" AND "+linhaFinal);
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }

        Cursor c = database.rawQuery(sql.toString(), null);
        if(c.getCount() > 0){
            c.moveToFirst();
            HashMap<String, String> map = new HashMap<String, String>();
            /*
            dados = c.getString(1);
            edtLinhaInicial.setText(c.getString(2));
            edtLinhaFinal.setText(c.getString(3));
            edtPlantas.setText(c.getString(4));
            edtHectares.setText(c.getString(5));
            */

            map.put("idParcela", c.getString(0));
            map.put("parcela", c.getString(1));
            map.put("lInicial", c.getString(2));
            map.put("lFinal", c.getString(3));
            map.put("plantas", c.getString(4));
            map.put("hectares", c.getString(5));
            campo.add(map);
        }
        return campo;
    }

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    private void alerta(String titulo, String msg) {
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
    private void carregarValoresEdit(String id){
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();

        StringBuilder sql_apontamento = new StringBuilder();
        sql_apontamento.append("SELECT p.parcela, atividade, linha_inicial, linha_final, plantas, area_realizada, ra.id_parcela, ra.matricula_mdo FROM realizado_apontamento ra ");
        sql_apontamento.append("INNER JOIN i_parcela p ");
        sql_apontamento.append("ON p.idparcela = ra.id_parcela ");
        sql_apontamento.append("WHERE apontamento = '"+id+"'");

        StringBuilder sql_colheita = new StringBuilder();
        sql_colheita.append("SELECT * FROM realizado_colheita ");
        sql_colheita.append("WHERE apontamento = '"+id+"'");

        StringBuilder sql_patrimonio = new StringBuilder();
        sql_patrimonio.append("SELECT p.descricao, pi.descricao, marcador_inicial, marcador_final FROM realizado_patrimonio rp ");
        sql_patrimonio.append("INNER JOIN c_patrimonio p ");
        sql_patrimonio.append("ON p.idpatrimonio = id_patrimonio ");
        sql_patrimonio.append("INNER JOIN c_patrimonio pi ");
        sql_patrimonio.append("ON pi.idpatrimonio = id_implemento ");
        sql_patrimonio.append("WHERE apontamento = '"+id+"'");

        StringBuilder sql_insumo = new StringBuilder();
        sql_insumo.append("SELECT * FROM realizado_insumo ");
        sql_insumo.append("WHERE apontamento = '"+id+"'");

        Cursor c_apontamento = database.rawQuery(sql_apontamento.toString(), null);
        if(c_apontamento.getCount() > 0){
            c_apontamento.moveToFirst();
            for(int i =0; i < parcela.getCount();i++){
                if(parcela.getItemAtPosition(i).equals(c_apontamento.getString(0))){
                    parcela.setSelection(i);
                }
            }
            for(int i =0; i < atividade.getCount();i++){
                if(atividade.getItemAtPosition(i).equals(c_apontamento.getString(1))){
                    atividade.setSelection(i);
                }
            }

            edtLinhaInicial.setText(c_apontamento.getString(2));
            edtLinhaFinal.setText(c_apontamento.getString(3));
            edtPlantas.setText(c_apontamento.getString(4));
            edtHectares.setText(c_apontamento.getString(5));
            idParcela = c_apontamento.getInt(6);
            edtMatMDO.setText(c_apontamento.getString(7));
        }
        Cursor c_colheita = database.rawQuery(sql_colheita.toString(), null);
        if(c_colheita.getCount() > 0){
            c_colheita.moveToFirst();
            edtCachos.setText(c_colheita.getString(1));
            edtPeso.setText(c_colheita.getString(2));

            for(int i =0; i < caixa.getCount();i++){
                if(caixa.getItemAtPosition(i).equals(c_colheita.getString(4))){
                    caixa.setSelection(i);
                }
            }

        }
        Cursor c_patrimonio = database.rawQuery(sql_patrimonio.toString(), null);
        if(c_patrimonio.getCount() > 0){
            c_patrimonio.moveToFirst();
            for(int i =0; i < patrimonio.getCount();i++){
                if(patrimonio.getItemAtPosition(i).equals(c_patrimonio.getString(0))){
                    patrimonio.setSelection(i);
                }
            }
            for(int i =0; i < implemento.getCount();i++){
                if(implemento.getItemAtPosition(i).equals(c_patrimonio.getString(1))){
                    implemento.setSelection(i);
                }
            }
            edtKMincial.setText(c_patrimonio.getString(2));
            edtKMfinal.setText(c_patrimonio.getString(3));
        }
        database.close();
    }

}
