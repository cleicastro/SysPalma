package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Frota_Abastecimento_interno extends AppCompatActivity {
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
    private FloatingActionButton btsalvar;
    private Button salvar;
    private Spinner patrimonio;
    private String cupom_get, item_get, fornecedor_get, tanque_get;
    static String cod_abastecimento;

    private AlertDialog alerta;//atributo da classe.
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento_interno);

        dataatual = (EditText) findViewById(R.id.data);
        patrimonio = (Spinner) findViewById(R.id.patrimonio);
        fornecedor = findViewById(R.id.fornecedor);
        item = findViewById(R.id.item);
        cupom = findViewById(R.id.cupom);
        fornecedor = findViewById(R.id.fornecedor);
        motorista = findViewById(R.id.motorista);
        km = findViewById(R.id.km);
        litros = findViewById(R.id.litros);

        cupom_get = getIntent().getStringExtra("cupom_fiscal");
        item_get = getIntent().getStringExtra("item");
        fornecedor_get = getIntent().getStringExtra("fornecedor");
        tanque_get = getIntent().getStringExtra("tanque");

        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        dataatual.setText(dateString);

        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMMddhhmmss");
        String dateStringToken = sdfToken.format(date);

        cod_abastecimento = dateStringToken + GetSetUsuario.getMatricula().replace("1120","");

        cupom.setText(cupom_get);
        item.setText(item_get);
        fornecedor.setText(fornecedor_get);

        chamarLista();
        matricula_mdo = findViewById(R.id.matricula);
        matricula_mdo.setText("1120");
        matricula_mdo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String getMat = RequestOperadorMotorista(matricula_mdo.getText().toString());
                if(getMat != null){
                    motorista.setText(getMat);
                }else{
                    Mensagem("Colaborador","Colaborador não encontrado, favor verifique a matricula digitada!");
                    matricula_mdo.setText("1120");
                }
            }
            }
        });

        litros.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(litros.getText().toString().equals("0.00"))
                {
                    litros.setText("");
                }

                if (!hasFocus) {
                    if(litros.getText().length() < 1) {
                        litros.setText("0.00");
                    }
                }

            }
        });
        btsalvar = findViewById(R.id.fab_salvar);
        btsalvar.setVisibility(View.GONE);
        salvar = findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Double valor1 = new Double(tanque_get);
                Double valor2 = new Double(litros.getText().toString());

                Double litros_tanq = valor1-valor2;

                if(litros_tanq >=  0 &&
                    patrimonio.getSelectedItemPosition() > 0 &&
                    item.getText().toString().length() > 0 &&
                    km.getText().toString().length() > 0 &&
                    cupom.getText().toString().length() > 0 &&
                    litros.getText().toString().length() > 0 &&
                    matricula_mdo.getText().toString().length() > 4){
                    salvar();
                }else{
                    Mensagem("Erro","Favor, todos os campos são obrgatórios ou litros no tanque insuficiente!");
                }
            }
            }
        });
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> patrimonios = null;
        if(tanque_get.equals("TANQUE")){
            patrimonios = db.SelectPatrimonio("'COMBOIO','ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'TRATOR','CAMINHONETE'");
        }else{
            patrimonios = db.SelectPatrimonio("'COMBOIO','ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'TRATOR','CAMINHONETE','TANQUE'");
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    public String RequestOperadorMotorista(String matricula){
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
    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data_atual = new Date();
        String dateFormatada = dt.format(data_atual);
        //INSERI O COMPRA
        int idpatrimonio = database.RetoronoIdSelect(patrimonio.getSelectedItem().toString(), "c_patrimonio", "idpatrimonio");
        int idforncedor = database.RetoronoIdSelect(fornecedor_get, "c_patrimonio", "idpatrimonio");
        int idinsumo = database.RetoronoIdSelect(item_get, "c_insumo", "idinsumo");

        GetSetCompra compra = new GetSetCompra();
        compra.setData(dateFormatada);
        compra.setId_fornecedor(idforncedor);
        compra.setMdo_motorista(matricula_mdo.getText().toString());
        compra.setId_patrimonio(idpatrimonio);
        compra.setKm(km.getText().toString());
        compra.setCupom_fiscal(cupom.getText().toString());
        compra.setId_mdo(GetSetUsuario.getMatricula());
        compra.setId_insumo(idinsumo);
        compra.setQuantidade(new Double(litros.getText().toString()));
        compra.setCod(cod_abastecimento);

        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        database.InserirAbastecimentoInterno(compra);

        Double valor1 = new Double(tanque_get);
        Double valor2 = new Double(litros.getText().toString());

        Double litros_tanq = valor1-valor2;
        //litros_item.setText(litros_tanq.toString()+" Litros no tanque");
        Toast.makeText(this,"Litros: "+litros_tanq, Toast.LENGTH_SHORT).show();
        database.UpdTanque(litros_tanq, cupom_get, null);

        exemplo_alerta("Salvando...","Abastecimento realizado com sucesso!");
    }
    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplication(), imprimir.class);
                intent.putExtra("cod_get",cod_abastecimento);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

}
