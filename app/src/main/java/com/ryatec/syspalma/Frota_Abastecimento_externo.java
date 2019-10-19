package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Frota_Abastecimento_externo extends AppCompatActivity {
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
    private Spinner fornecedor;
    private Spinner posto;
    private Spinner combustivel;
    private LinearLayout dados_litros;
    private LinearLayout label_litros;
    private TextView label_nf;
    private EditText nf;
    private EditText responsavel;
    private EditText litros;
    private EditText km;
    private EditText valor_litro;
    private EditText total_litro, valorToken;
    private EditText cupom_fiscal;
    private FloatingActionButton salvar;
    private Spinner patrimonio;
    private ListView myListView;

    private ImageButton upd, addMDO;

    private Integer linhaRowTable;
    private TableLayout ll;

    private AlertDialog alerta;//atributo da classe.

    private boolean mUpdating;
    private NumberFormat mNF = NumberFormat.getCurrencyInstance();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abastecimento_externo);

        patrimonio = (Spinner) findViewById(R.id.patrimonio);
        ll = (TableLayout) findViewById(R.id.listItem);
        myListView = (ListView) findViewById(R.id.itemList);
        valorToken = findViewById(R.id.valorToken);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        dataatual = (EditText) findViewById(R.id.data);
        final long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMMdd");
        String dateString = sdf.format(date);
        dataatual.setText(dateString);

        String dateStringToken = sdfToken.format(date);
        valorToken.setText(dateStringToken+GetSetUsuario.getMatricula().replace("1120",""));

        label_nf = (TextView) findViewById(R.id.label_nf);
        km = (EditText) findViewById(R.id.km);
        cupom_fiscal = (EditText) findViewById(R.id.cupom);
        responsavel = (EditText) findViewById(R.id.responsavel);
        fornecedor = (Spinner) findViewById(R.id.fornecedor);
        // = (LinearLayout) findViewById(R.id.dados_litros);
        //label_litros = (LinearLayout) findViewById(R.id.label_litros);
        litros = (EditText) findViewById(R.id.quant);
        valor_litro = (EditText) findViewById(R.id.valor);
        total_litro = (EditText) findViewById(R.id.valorTotal);
        posto = (Spinner) findViewById(R.id.posto_interno);
        combustivel = (Spinner) findViewById(R.id.combustivel);

        responsavel.setText(GetSetUsuario.getNome());

        litros.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(litros.getText().toString().equals("0.0"))
                {
                    litros.setText("");
                }

                if (!hasFocus) {
                    if(litros.getText().length() > 0 && valor_litro.getText().length() > 0) {
                        double vLitro = Double.valueOf(litros.getText().toString());
                        double vValorLitro = Double.valueOf(valor_litro.getText().toString());
                        double vTotal = vLitro * vValorLitro;
                        total_litro.setText(String.valueOf(vTotal));
                        if(vLitro > 10000 || vValorLitro > 10){
                            exemplo_alerta("Atenção!", "Litros ou valor do litro acima do permitido");
                            litros.requestFocus();
                        }
                    }else{
                        total_litro.setText("0.0");
                    }
                }

            }
        });
        valor_litro.addTextChangedListener(new TextWatcher() {
            private boolean mUpdating;
            private NumberFormat mNF = NumberFormat.getCurrencyInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(valor_litro.getText().length() > 0 && litros.getText().length() > 0) {
                    double vLitro = Double.valueOf(litros.getText().toString());
                    double vValorLitro = Double.valueOf(valor_litro.getText().toString());
                    double vTotal = vLitro * vValorLitro;

                    //CONVERTER PARA MOEDA DA REGIÃO
                    DecimalFormat df = new DecimalFormat("0.000");
                    String dx = df.format(vTotal);
                    total_litro.setText("R$ "+dx);
                }else{
                    total_litro.setText("R$ 0,000");
                }
            }
        });
        valor_litro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(valor_litro.getText().toString().equals("0.0"))
                {
                    valor_litro.setText("");
                }

                if (!hasFocus) {
                    if(valor_litro.getText().length() > 0 && litros.getText().length() > 0) {
                        double vLitro = Double.valueOf(litros.getText().toString());
                        double vValorLitro = Double.valueOf(valor_litro.getText().toString());
                        double vTotal = vLitro * vValorLitro;

                        //CONVERTER PARA MOEDA DA REGIÃO
                        DecimalFormat df = new DecimalFormat("0.000");
                        String dx = df.format(vTotal);
                        total_litro.setText("R$ "+dx);
                    }else{
                        total_litro.setText("R$ 0,000");
                    }
                }
            }
        });
        addMDO = (ImageButton) findViewById(R.id.addLinha);
        addMDO.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if(fornecedor.getSelectedItemPosition() > 0 &&
                            patrimonio.getSelectedItemPosition() > 0 &&
                            combustivel.getSelectedItemPosition() > 0 &&
                            km.getText().toString().length() > 0 &&
                            cupom_fiscal.getText().toString().length() > 0 &&
                            litros.getText().toString().length() > 0 &&
                            valor_litro.getText().toString().length() > 0){
                        salvar();
                        addItem();
                    }else{
                        exemplo_alerta("Erro","Favor, todos os campos são obrgatórios!");
                    }
                }
            }
        });

        chamarLista();
        ImageButton printer = findViewById(R.id.imprimir);
        printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TrocaTela2 = new Intent(Frota_Abastecimento_externo.this, imprimir.class);
                TrocaTela2.putExtra("placa", "OFM-2345");
                TrocaTela2.putExtra("motorista", GetSetUsuario.getNome());
                TrocaTela2.putExtra("km", "432453");
                TrocaTela2.putExtra("litros", "56");
                TrocaTela2.putExtra("fazenda", "Teste");
                TrocaTela2.putExtra("cupomFiscal", "fasdf");
                TrocaTela2.putExtra("combustivel", "DIESEL S10");
                TrocaTela2.putExtra("ValorLitro", "3.12");
                TrocaTela2.putExtra("ValorTotal", "R$ 300,00");
                TrocaTela2.putExtra("Lote", "12132");
                startActivity(TrocaTela2);
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView ViewId = (TextView) view.findViewById(R.id.id);
                Integer GetId = Integer.parseInt(ViewId.getText().toString());
                ConfirmacaoExclusao("Excluir Abastecimento","Realmente deseja excluir este abastecimento?",GetId);
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);

        List<String> fornecedores = db.SelectFornecedores("'POSTO'");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,R.layout.spinner_item, fornecedores);
        dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        fornecedor.setAdapter(dataAdapter1);

        List<String> patrimonios = db.SelectPatrimonio("'COMBOIO','ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'MOTO', 'AUTOMÓVEL','CAMINHONETE','CARRO','ROÇADEIRA COSTAL','TANQUE'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> itens = db.SelectInsumo("'COMBUSTÍVEL','ÓLEO','LUBRIFICANTE'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,R.layout.spinner_item, itens);
        dataAdapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        combustivel.setAdapter(dataAdapter3);

        addItem();
    }
    public void addItem(){
        Database_syspalma database = new Database_syspalma(this);
        ListAdapter adapter = new SimpleAdapter(this, database.ListaComprasToken(valorToken.getText().toString()), R.layout.resumo_itens, new String[]{
                "id","n","item", "quantidade","total"}, new int[]{R.id.id, R.id.contItem, R.id.listItem, R.id.listQuant,R.id.listTotal});
        myListView.setAdapter(adapter);
    }

    public void salvar(){
        Database_syspalma database = new Database_syspalma(this);
        Database_syspalma_backup database_backup = new Database_syspalma_backup(this);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date data_atual = new Date();
        String dateFormatada = dt.format(data_atual);
        //INSERI O COMPRA
        int idforncedor = database.RetoronoIdSelect(fornecedor.getSelectedItem().toString(), "c_fornecedor", "idfornecedor");
        int idpatrimonio = database.RetoronoIdSelect(patrimonio.getSelectedItem().toString(), "c_patrimonio", "idpatrimonio");
        int idinsumo = database.RetoronoIdSelect(combustivel.getSelectedItem().toString(), "c_insumo", "idinsumo");
        String comboio = database.RetoronoIdSelectString(idpatrimonio, "c_patrimonio", "tipo","idpatrimonio");

        GetSetCompra compra = new GetSetCompra();
        compra.setData(dateFormatada);
        compra.setToken(valorToken.getText().toString());
        compra.setId_fornecedor(idforncedor);
        compra.setId_patrimonio(idpatrimonio);
        compra.setKm(km.getText().toString());
        compra.setCupom_fiscal(cupom_fiscal.getText().toString());
        compra.setId_mdo(GetSetUsuario.getMatricula());
        compra.setId_insumo(idinsumo);
        compra.setQuantidade(new Double(litros.getText().toString()));
        compra.setValor_unit(new Double(valor_litro.getText().toString()));

        //VERIFICA O TIPO DE PATRIMONIO, CASO SEJA O COMBOIO ENTRA A REGRA DO TANQUE
        if(comboio.equals("COMBOIO")){
            if(database.RetoronoQuantTanque(idpatrimonio) < 2){
                database.InserirCompra(compra);
                database.InserirTanque(compra);
            }else{
                exemplo_alerta("Atenção!","Transfira o restante do combustível do tanque para poder liberar um novo abastecimento");
            }
        }else{
            database.InserirCompra(compra);
            fornecedor.setSelection(0);
            combustivel.setSelection(0);
            km.setText("");
            cupom_fiscal.setText("");
            litros.setText("");
            valor_litro.setText("");
            total_litro.setText("");

            exemplo_alerta("Salvando...","Item adicionado com sucesso!");
        }
    }

    private void exemplo_alerta(String titulo, String msg) {
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

    public void ConfirmacaoExclusao(String Titulo, String Msg, final Integer Gtid) {
        final Database_syspalma db = new Database_syspalma(this);

        android.support.v7.app.AlertDialog.Builder Alerta = new android.support.v7.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setCancelable(false);
        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deletarCompra_item(Gtid);
                Toast.makeText(getApplication(),"Item: " +Gtid + " removido com sucesso",Toast.LENGTH_SHORT).show();
                addItem();
            }
        });
        Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Alerta.show();
    }

}
