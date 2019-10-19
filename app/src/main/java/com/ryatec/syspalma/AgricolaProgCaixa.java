package com.ryatec.syspalma;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AgricolaProgCaixa extends AppCompatActivity {
    private EditText data;
    private EditText dataatual, cod, caixa_i, caixa_ii, caixa_vazia, caixa_cheia, responsavel;
    private FloatingActionButton btsalvar;
    private Button salvar;
    private Spinner fazenda;
    static String cod_transporte;

    private AlertDialog alerta;//atributo da classe.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog_caixa);

        String responsavel_get = RequesResponsavel(GetSetUsuario.getMatricula());

        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMMddhhmmss");
        String dateStringToken = sdfToken.format(date);

        cod_transporte = dateStringToken + GetSetUsuario.getMatricula().replace("1120","");

        data = (EditText) findViewById(R.id.data);
        cod = (EditText) findViewById(R.id.cod);
        responsavel = (EditText) findViewById(R.id.responsavel);
        caixa_i = (EditText) findViewById(R.id.caixa_i);
        caixa_ii = (EditText) findViewById(R.id.caixa_ii);
        caixa_i = (EditText) findViewById(R.id.caixa_i);
        fazenda = (Spinner) findViewById(R.id.fazenda);
        caixa_cheia = (EditText) findViewById(R.id.caixa_cheia);
        caixa_vazia = (EditText) findViewById(R.id.caixa_vazia);

        data.setText(dateString);
        cod.setText(cod_transporte);
        responsavel.setText(responsavel_get);

        chamarLista();

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
                new DatePickerDialog(AgricolaProgCaixa.this, dategs, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                data.setText(dateFormatter.format(myCalendar.getTime()));
            }
        });

        btsalvar = findViewById(R.id.fab_salvar);
        salvar = findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fazenda.getSelectedItemPosition() > 0 && caixa_i.getText().length() > 0){
                    salvar();
                }else{
                    Mensagem("Falha","Verifica a fazenda ou a caixa programada!");
                }
            }
        });

        //sair da tela
        btsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> fazendalist = null;
        fazendalist = db.SelectFazendas();
        fazendalist.add("Nakata");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_item, fazendalist);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        fazenda.setAdapter(dataAdapter2);
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }
    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);

        String datas = data.getText().toString().replaceAll("-", "/");
        String[] s = datas.split("/");
        String novaData = s[2]+"-"+s[1]+"-"+s[0];

        //CONVERTE O NÚMERO DAS CAIXAS PARA INTEIRO
        Integer caixai_convert = new Integer(caixa_i.getText().toString());
        Integer caixaii_convert = 0;
        if(caixa_ii.getText().length()>0){
            caixaii_convert = new Integer(caixa_ii.getText().toString());
        }
        //INSERI O COMPRA

        GetSetProgCaixa programacao = new GetSetProgCaixa();
        programacao.setData(novaData);
        programacao.setCod(cod_transporte);
        programacao.setResponsavel(GetSetUsuario.getMatricula());
        programacao.setFazenda(fazenda.getSelectedItem().toString());
        programacao.setCaixa_i(caixai_convert);
        programacao.setCaixa_ii(caixaii_convert);
        programacao.setCaixa_cheia(caixa_cheia.getText().toString());
        programacao.setCaixa_vazia(caixa_vazia.getText().toString());
        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        database.InserirProgCaixa(programacao);
        exemplo_alerta("Salvando...","Programação realizada com sucesso!");
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

    public String RequesResponsavel(String matricula){
        String nome_get = null;
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();
        String selectQuery = "SELECT nome FROM p_colaborador WHERE matricula = '"+matricula+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            nome_get = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return nome_get;
    }

    public void Mensagem(String Titulo, String Msg){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

}
