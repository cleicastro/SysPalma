package com.ryatec.syspalma;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
public class AgricolaPluviometria extends AppCompatActivity {
    private EditText data;
    private EditText quantidade;
    private FloatingActionButton btsalvar;
    private Button salvar;
    private Spinner fazenda;

    private AlertDialog alerta;//atributo da classe.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluviometria);

        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        data = (EditText) findViewById(R.id.data);
        quantidade = (EditText) findViewById(R.id.litros);
        fazenda = (Spinner) findViewById(R.id.fazenda);
        data.setText(dateString);

        btsalvar = findViewById(R.id.fab_salvar);
        salvar = findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fazenda.getSelectedItemPosition() > 0 && quantidade.getText().length() > 0){
                    salvar();
                }else{
                    Mensagem("Falha","Verifica a fazenda ou os mm!");
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
                new DatePickerDialog(AgricolaPluviometria.this, dategs, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    data.setText(dateFormatter.format(myCalendar.getTime()));
            }
        });

        chamarLista();
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> fazendalist = null;
        fazendalist = db.SelectFazendas();
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
        //INSERI O PLUVIOMETRIA

        //ALTERA O TIPO DA DATA
        String datas = data.getText().toString().replaceAll("-", "/");
        String[] s = datas.split("/");
        String novaData = s[2]+"/"+s[1]+"/"+s[0];

        GetSetPluviometria pluviometria = new GetSetPluviometria();
        pluviometria.setFazenda(fazenda.getSelectedItem().toString());
        pluviometria.setQuantidade(new Double(quantidade.getText().toString()));
        pluviometria.setData(novaData);
        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        database.InserirPluviometria(pluviometria);
        exemplo_alerta("Salvando...","Pluviometra registrada com sucesso!");
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

    public void Mensagem(String Titulo, String Msg){
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

}
