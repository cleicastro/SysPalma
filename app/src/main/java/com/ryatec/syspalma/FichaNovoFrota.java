package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FichaNovoFrota extends Activity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_patrimonio);
        //myListView = (Spinner) findViewById(R.id.lista_rt);
        //salvar = (Button) findViewById(R.id.salvarFicha);

        Database_syspalma db = new Database_syspalma(this);
        List<String> resp = db.SelectRT();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, resp);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //myListView.setAdapter(dataAdapter);
        data = (CalendarView) findViewById(R.id.data_lanc);

        btnPatrimonio = (Button) findViewById(R.id.btnPatrimonio);
        mExpandApontamento = (ExpandableRelativeLayout) findViewById(R.id.mExpandApontamento);
        mExpandPatrimonio = (ExpandableRelativeLayout) findViewById(R.id.mExpandPatrimonio);
        edtKMincial = (EditText) findViewById(R.id.editkmInicial);
        edtKMfinal = (EditText) findViewById(R.id.editkmFinal);

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
                if(atividade.getSelectedItemPosition() > 0 &&
                        patrimonio.getSelectedItemPosition() > 0 &&
                        edtKMincial.getText().toString().length() > 0){
                    salvar();
                }else{
                    exemplo_alerta_erro("Erro","Favor, verificar a atividade, o patrimônio ou a quilometragem!");
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        carregar_valores();
    }

    public void carregar_valores(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> patrimonios = db.SelectPatrimonio("'COMBOIO','ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'MOTO', 'AUTOMÓVEL','CAMINHONETE','CARRO','ROÇADEIRA COSTAL'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        //GERAR NÚMEROS ALEATÓRIOS
        Random gerador = new Random();
        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
        Integer aleatNumber = 0;
        for (int i = 0; i < 1; i++) {
            aleatNumber = gerador.nextInt(99);
        }

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date dataFicha = new Date(data.getDate());
        Date data_atual = new Date();
        String dateLancamento = dt.format(dataFicha);

        SimpleDateFormat sdf_hora = new SimpleDateFormat("HH:mm:ss",  Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dmyHHmmss",  Locale.getDefault());
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        String dataFormatada = sdf.format(hora)+aleatNumber;
        String apontamentoCod = GetSetUsuario.getMatricula()+dataFormatada;


        SimpleDateFormat dtficha = new SimpleDateFormat("dMMy");
        Date dataFichaNovo = new Date();
        String fichaFormatada = GetSetUsuario.getMatricula()+dtficha.format(dataFichaNovo);

        GetSetAtividade getpatrimonio_realizado = new GetSetAtividade();
        getpatrimonio_realizado.setData_realizado(dateLancamento);
        getpatrimonio_realizado.setAtiviade_patrimonio(atividade.getSelectedItem().toString());
        getpatrimonio_realizado.setOrigem(origem.getSelectedItem().toString());
        getpatrimonio_realizado.setDestino(destino.getSelectedItem().toString());
        getpatrimonio_realizado.setObs("");
        getpatrimonio_realizado.setId_mdo(GetSetUsuario.getMatricula());
        getpatrimonio_realizado.setId_apontamento(apontamentoCod);

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
            getpatrimonio_realizado.setHora_inicial(horaFormatada);
            getpatrimonio_realizado.setHora_final(horaFormatada);
            getpatrimonio_realizado.setId_apontamento(apontamentoCod);

            database.InserirRealizadoPatrimonioAtividade(getpatrimonio_realizado);
            database_backup.InserirRealizadoPatrimonioAtividade(getpatrimonio_realizado);
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

    private void exemplo_alerta_erro(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

}
