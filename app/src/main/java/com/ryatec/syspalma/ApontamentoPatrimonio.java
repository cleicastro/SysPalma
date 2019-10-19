package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApontamentoPatrimonio extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandApontamento;
    private ExpandableRelativeLayout mExpandPatrimonio;
    private Button btnApontamento;
    private Button btnPatrimonio;
    private EditText edtKMincial;
    private EditText edtKMfinal;
    private Spinner patrimonio;
    private Spinner implemento;
    private Spinner atividade;
    private Spinner origem, destino;
    private Spinner caixa;
    private Integer linhaRowTable;
    private TableLayout ll;
    private Button salvarApontamento;
    private ImageButton addMDO;
    private GetSetCache getSetCache = new GetSetCache();
    private CalendarView data;
    static String apontamento_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_patrimonio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apontamento_edt = getIntent().getStringExtra("apontamento_get");

        data = (CalendarView) findViewById(R.id.data_lanc);
        data.setVisibility(View.GONE);
        btnApontamento = (Button) findViewById(R.id.btnApontamento);
        btnPatrimonio = (Button) findViewById(R.id.btnPatrimonio);
        mExpandApontamento = (ExpandableRelativeLayout) findViewById(R.id.mExpandApontamento);
        mExpandPatrimonio = (ExpandableRelativeLayout) findViewById(R.id.mExpandPatrimonio);
        edtKMincial = (EditText) findViewById(R.id.editkmInicial);
        edtKMfinal = (EditText) findViewById(R.id.edt1kmfinal);

        implemento = (Spinner) findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) findViewById(R.id.spinner_patrimonio);
        atividade = (Spinner) findViewById(R.id.motorista);
        origem = (Spinner) findViewById(R.id.origem);
        destino = (Spinner) findViewById(R.id.destino);
        caixa = (Spinner) findViewById(R.id.spinnerCaixa);

        ll = (TableLayout) findViewById(R.id.tabela_os);

        Button cancelar = (Button) findViewById(R.id.cancelar);
        addMDO = (ImageButton) findViewById(R.id.addLinha);
        salvarApontamento = (Button) findViewById(R.id.salvar);

        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        carregar_valores();
        carregarValoresEdit(apontamento_edt);
    }

    public void carregar_valores(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> patrimonios = db.SelectPatrimonio("'ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'MOTO', 'AUTOMÓVEL','CARRO','COMBOIO','VEÍCULO DE APOIO'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> implementos = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, implementos);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        implemento.setAdapter(dataAdapter3);

        /*
        List<String> atividades = db.SelectAtividade(getSetCache.getOperacao());
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, atividades);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        atividade.setAdapter(dataAdapter4);
        */


        List<String> fazendas = db.SelectFazendas();
        fazendas.add("Base SDC");
        fazendas.add("Base MDR");
        fazendas.add("Base Apoio(km 14)");
        fazendas.add("Industria");
        fazendas.add("Outros");
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fazendas);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        origem.setAdapter(dataAdapter5);
        destino.setAdapter(dataAdapter5);
    }
    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);
        Database_syspalma_backup database_backup = new Database_syspalma_backup(this);

        SimpleDateFormat sdf_hora = new SimpleDateFormat("HH:mm:ss",  Locale.getDefault());
        Date data_atual = new Date();
        //INSERI O REALIZADO
        //EditText obs = findViewById(R.id.edt1kmfinal);

        GetSetAtividade getpatrimonio_realizado = new GetSetAtividade();
        getpatrimonio_realizado.setAtiviade_patrimonio(atividade.getSelectedItem().toString());
        getpatrimonio_realizado.setOrigem(origem.getSelectedItem().toString());
        getpatrimonio_realizado.setDestino(destino.getSelectedItem().toString());
        getpatrimonio_realizado.setObs("");

        //INSERIR OS DADOS DA FROTA
        EditText editkmInicial = findViewById(R.id.editkmInicial);
        EditText editkmFinal = findViewById(R.id.edt1kmfinal);

        int idpatr = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
        int idimp = database.RetoronoIdImplemento(implemento.getSelectedItem().toString());

        getpatrimonio_realizado.setId_patrimonio(idpatr);
        getpatrimonio_realizado.setId_implemento(((Integer) idimp));

        Double horimetroInicial = 0.0;
        Double horimetroFinal = 0.0;
        if(!editkmFinal.getText().toString().isEmpty()){
            horimetroFinal = new Double(String.valueOf(editkmFinal.getText().toString()));
        }
        if(!editkmInicial.getText().toString().isEmpty()){
            String horaFormatada = sdf_hora.format(data_atual);
            horimetroInicial = new Double(String.valueOf(editkmInicial.getText().toString()));
            getpatrimonio_realizado.setMarcador_inicial(horimetroInicial);
            getpatrimonio_realizado.setMarcador_final(horimetroFinal);
            getpatrimonio_realizado.setHora_final(horaFormatada);

            database.alteraApontamentoFrota(getpatrimonio_realizado, apontamento_edt);
            database_backup.alteraApontamentoFrota(getpatrimonio_realizado, apontamento_edt);

            exemplo_alerta("Salvando...","Apontamentos salvos");
        }else{
            exemplo_alerta("Atenção","Informe o km inicial");
        }
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

    private void carregarValoresEdit(String apontamento){
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();

        StringBuilder sql_apontamento = new StringBuilder();
        sql_apontamento.append("SELECT data_realizado, atividade, origem, destino, matricula_mdo, apontamento, obs, p.descricao, pi.descricao, marcador_inicial, marcador_final FROM realizado_apontamento_patrimonio rap ");
        sql_apontamento.append("INNER JOIN c_patrimonio p ");
        sql_apontamento.append("ON p.idpatrimonio = id_patrimonio ");
        sql_apontamento.append("INNER JOIN c_patrimonio pi ");
        sql_apontamento.append("ON pi.idpatrimonio = id_implemento ");
        sql_apontamento.append("WHERE apontamento = '"+apontamento+"'");

        Cursor c_apontamento = database.rawQuery(sql_apontamento.toString(), null);
        if(c_apontamento.getCount() > 0){
            c_apontamento.moveToFirst();
            for(int i =0; i < atividade.getCount();i++){
                if(atividade.getItemAtPosition(i).equals(c_apontamento.getString(1))){
                    atividade.setSelection(i);
                }
            }
            for(int i =0; i < origem.getCount();i++){
                if(origem.getItemAtPosition(i).equals(c_apontamento.getString(2))){
                    origem.setSelection(i);
                }
            }

            for(int i =0; i < destino.getCount();i++){
                if(destino.getItemAtPosition(i).equals(c_apontamento.getString(3))){
                    destino.setSelection(i);
                }
            }
            for(int i =0; i < patrimonio.getCount();i++){
                if(patrimonio.getItemAtPosition(i).equals(c_apontamento.getString(7))){
                    patrimonio.setSelection(i);
                }
            }
            for(int i =0; i < implemento.getCount();i++){
                if(implemento.getItemAtPosition(i).equals(c_apontamento.getString(8))){
                    implemento.setSelection(i);
                }
            }
            edtKMincial.setText(c_apontamento.getString(9));
            edtKMfinal.setText(c_apontamento.getString(10));
        }
        database.close();
    }
}
