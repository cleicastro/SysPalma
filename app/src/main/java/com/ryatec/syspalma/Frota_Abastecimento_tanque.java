package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Frota_Abastecimento_tanque extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private EditText data;
    private EditText dataatual;
    private EditText matricula_mdo, motorista;
    private EditText litros;
    private EditText cupom, item, fornecedor, km;
    private Button salvar;
    private Spinner patrimonio, combustivel;
    private String token;
    static String cod_abastecimento;

    private AlertDialog alerta;//atributo da classe.
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento_tanque);

        dataatual = (EditText) findViewById(R.id.data);
        patrimonio = (Spinner) findViewById(R.id.fornecedor_interno);
        combustivel = (Spinner) findViewById(R.id.item);
        cupom = findViewById(R.id.cupom);
        litros = findViewById(R.id.litros);


        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        dataatual.setText(dateString);

        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMMddhhmmss");
        token = sdfToken.format(date);

        chamarLista();

        litros.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if(litros.getText().toString().equals("0.00"))
            {
                litros.setText("");
            }

            }
        });

        salvar = findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                if( patrimonio.getSelectedItemPosition() > 0 &&
                    combustivel.getSelectedItemPosition() > 0 &&
                    cupom.getText().toString().length() > 0 &&
                    litros.getText().toString().length() > 0){
                    salvar();
                }else{
                    Mensagem("Erro","Favor, todos os campos são obrgatórios!");
                }
            }
            }
        });
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);

        List<String> patrimonios = db.SelectPatrimonio("'COMBOIO','TANQUE'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> itens = db.SelectInsumo("'COMBUSTÍVEL'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,R.layout.spinner_item, itens);
        dataAdapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        combustivel.setAdapter(dataAdapter3);
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    public void Mensagem(String Titulo, String Msg){
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }
    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);
        Database_syspalma_backup database_backup = new Database_syspalma_backup(this);

        //INSERI O COMPRA
        int idpatrimonio = database.RetoronoIdSelect(patrimonio.getSelectedItem().toString(), "c_patrimonio", "idpatrimonio");
        int idinsumo = database.RetoronoIdSelect(combustivel.getSelectedItem().toString(), "c_insumo", "idinsumo");

        GetSetCompra compra = new GetSetCompra();
        compra.setToken(token);
        compra.setId_patrimonio(idpatrimonio);
        compra.setCupom_fiscal(cupom.getText().toString());
        compra.setId_insumo(idinsumo);
        compra.setQuantidade(new Double(litros.getText().toString()));
        compra.setValor_unit(0.000);

        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        database.InserirTanque(compra);
        exemplo_alerta("Salvando...","Tanque abastecido com sucesso!");
    }
    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
               // Intent intent = new Intent(getApplication(), imprimir.class);
                //intent.putExtra("cod_get",cod_abastecimento);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //startActivity(intent);
                finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

}
