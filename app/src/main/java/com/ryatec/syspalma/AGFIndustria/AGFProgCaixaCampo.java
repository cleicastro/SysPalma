package com.ryatec.syspalma.AGFIndustria;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ryatec.syspalma.Database_syspalma;
import com.ryatec.syspalma.GetSetCache;
import com.ryatec.syspalma.GetSetProgCaixa;
import com.ryatec.syspalma.GetSetUsuario;
import com.ryatec.syspalma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AGFProgCaixaCampo extends AppCompatActivity {
    private EditText data;
    private EditText dataatual, cod, caixa_i, ton, cachos, peso_medio, caixa_cheia, responsavel, cpf, clifor, inscricao_estadual, romaneio;
    private FloatingActionButton btsalvar;
    private Button salvar;
    private AutoCompleteTextView agricultor;
    static String cod_transporte;

    private AlertDialog alerta;//atributo da classe.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog_caixa_agf);

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
        ton = (EditText) findViewById(R.id.ton);
        caixa_i = (EditText) findViewById(R.id.caixa_i);
        agricultor = (AutoCompleteTextView) findViewById(R.id.agricultor);
        caixa_cheia = (EditText) findViewById(R.id.caixa_cheia);
        cachos = (EditText) findViewById(R.id.cachos);
        peso_medio = (EditText) findViewById(R.id.pm);
        data.setText(dateString);
        cod.setText(cod_transporte);
        responsavel.setText(responsavel_get);
        cpf = (EditText) findViewById(R.id.cpf);
        clifor = (EditText) findViewById(R.id.clifor);
        inscricao_estadual = (EditText) findViewById(R.id.inscricao_estadual);
        romaneio = (EditText) findViewById(R.id.romaneio);

        agricultor.requestFocus();

        agricultor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getAdapter().getItem(position).toString();
                String[] s = value.split(", ");
                agricultor.setText(s[0]);
                cpf.setText(s[3]);
                clifor.setText(s[2]);
                inscricao_estadual.setText(s[1]);
                caixa_i.requestFocus();
            }
        });

        agricultor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(cpf.getText().toString().isEmpty() || clifor.getText().toString().isEmpty()){
                        Mensagem("AGRICULTOR","SELECIONE UM AGRICULTOR CORRETAMENTE :(");
                    }
                }else{
                    agricultor.requestFocus(agricultor.getText().length()+1);
                }
            }
        });

        agricultor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_DEL) {
                    cpf.setText("");
                    clifor.setText("");
                    inscricao_estadual.setText("");
                }
                return false;
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
                new DatePickerDialog(AGFProgCaixaCampo.this, dategs, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                data.setText(dateFormatter.format(myCalendar.getTime()));
            }
        });

        ton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cachosGet = cachos.getText().toString();
                Double toneladasset;
                Integer cachosset;

                if(editable.toString().isEmpty() || cachosGet.isEmpty()){
                    peso_medio.setText("");
                }else{
                    toneladasset = Double.parseDouble(editable.toString());
                    cachosset = Integer.parseInt(cachosGet);

                    Double peso_medio_set = (toneladasset) / cachosset;
                    peso_medio.setText(new DecimalFormat("0.00").format(peso_medio_set));
                }
            }
        });

        cachos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tonGet = ton.getText().toString();
                Double toneladasset;
                Integer cachosset;

                if(editable.toString().isEmpty() || tonGet.isEmpty()){
                    peso_medio.setText("");
                }else{
                    toneladasset = Double.parseDouble(tonGet);
                    cachosset = Integer.parseInt(editable.toString());

                    Double peso_medio_set = (toneladasset) / cachosset;
                    peso_medio.setText(new DecimalFormat("0.00").format(peso_medio_set));
                }

            }
        });

        btsalvar = findViewById(R.id.fab_salvar);
        salvar = findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agricultor.getText().length() > 0 && caixa_i.getText().length() > 0 &&
                ! cpf.getText().toString().isEmpty() && ! clifor.getText().toString().isEmpty()){
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

    public void onResume() {
        super.onResume();
        getValues();
    }
    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);

        String datas = data.getText().toString().replaceAll("-", "/");
        String[] s = datas.split("/");
        String novaData = s[2]+"-"+s[1]+"-"+s[0];

        //CONVERTE O NÚMERO DAS CAIXAS PARA INTEIRO
        Integer caixai_convert = new Integer(caixa_i.getText().toString());
        Double tonelada_convert = new Double(ton.getText().toString());
        Integer cachos_convert = new Integer(cachos.getText().toString());

        GetSetProgCaixaAGF programacao = new GetSetProgCaixaAGF();
        programacao.setData(novaData);
        programacao.setCod(cod_transporte);
        programacao.setResponsavel(GetSetUsuario.getMatricula());
        programacao.setClifor(clifor.getText().toString());
        programacao.setCaixa_i(caixai_convert);
        programacao.setTonelada((tonelada_convert/1000));
        programacao.setCachos(cachos_convert);
        programacao.setRomaneio(romaneio.getText().toString());
        programacao.setCaixa_cheia(caixa_cheia.getText().toString());
        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        database.InserirProgCaixaAGF(programacao);
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
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

    public void getValues(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> userList = db.selecAGF();
        ArrayAdapter<String>  adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, userList);
        agricultor.setAdapter(adapter);
    }

}
