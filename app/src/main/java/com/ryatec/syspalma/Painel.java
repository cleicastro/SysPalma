package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ryatec.syspalma.AGFIndustria.AGFProgCaixa;
import com.ryatec.syspalma.AGFIndustria.AGFProgForm;
import com.ryatec.syspalma.AGFIndustria.AGFProgramacaoList;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Painel extends AppCompatActivity implements
        TextToSpeech.OnInitListener {
    private CardView agricola, agf;
    private CardView frota;
    private CardView fito, epi;
    private TextView matricula, nome, funcao;
    private FloatingActionButton sair;
    private CollapsingToolbarLayout collapsingtoolbar;
    private TextToSpeech tts;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel);

        //FECHA A INTENT CASO O USUÁRIO NÃO SEJA SETADO
        if (GetSetUsuario.getMatricula() == null) {
            finish();
        }
        UpdateHelper appUpdateChecker=new UpdateHelper(this);  //pass the activity in constructure
        appUpdateChecker.checkForUpdate(true); //mannual check false here

        collapsingtoolbar = findViewById(R.id.collapsingtoolbar);
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        collapsingtoolbar.setTitle("SysPalma Mobile v."+versionName);
        agricola = (CardView) findViewById(R.id.cardView2);
        agf = (CardView) findViewById(R.id.cardEGF);
        frota = (CardView) findViewById(R.id.cardFrota);
        fito = (CardView) findViewById(R.id.cardFito);
        sair = findViewById(R.id.sair);
        epi = findViewById(R.id.cardEPI);

        matricula = (TextView) findViewById(R.id.idUser);
        nome = (TextView) findViewById(R.id.nomeUser);
        funcao = (TextView) findViewById(R.id.funcaoUser);

        matricula.setText(GetSetUsuario.getMatricula());
        nome.setText(GetSetUsuario.getNome());
        funcao.setText(GetSetUsuario.getFuncao());

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSair();
            }
        });

        agricola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GetSetUsuario.getTipo().equals("Administrador") || GetSetUsuario.getDepartamento().equals("AGE") || GetSetUsuario.getDepartamento().equals("FITO")) {
                    Intent intent = new Intent(Painel.this, OperacoesAgricola.class);
                    startActivity(intent);
                } else {
                    Mensagem("Acesso negado", "Você não tem permissão para acessar este módulo");
                }
            }
        });
        agf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(!GetSetUsuario.getTipo().equals("Padrão")){
                    intent = new Intent(Painel.this, AGFProgCaixa.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(Painel.this, AGFProgramacaoList.class);
                    startActivity(intent);
                }

            }
        });
        frota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Painel.this, MenuOperacoesFrota.class);
                startActivity(intent);
            }
        });
        fito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GetSetUsuario.getTipo().equals("Administrador") || GetSetUsuario.getDepartamento().equals("AGE") || GetSetUsuario.getDepartamento().equals("FITO")) {
                    Intent intent = new Intent(Painel.this, MenuOperacoesFitossanidade.class);
                    startActivity(intent);
                } else {
                    Mensagem("Acesso negado", "Você não tem permissão para acessar este módulo");
                }
            }
        });
        epi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Painel.this, MenuOperacoesEPI.class);
                startActivity(intent);
            }
        });
        tts = new TextToSpeech(this, this);
    }
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result=tts.setLanguage(Locale.getDefault());
            if(result==TextToSpeech.LANG_MISSING_DATA ||
                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("error", "This Language is not supported");
                Toast.makeText(Painel.this, "This Language is not supported", Toast.LENGTH_SHORT).show();
            }
            else{
                Falar(GetSetUsuario.getNome());
            }
        }
        else
            Log.e("error", "Initilization Failed!");
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause() ;
    }

    private void Falar(String texto) {
        if(texto==null||"".equals(texto))
        {
            texto = "Usuário não reconhecido";
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            tts.setSpeechRate(0.95f);
            tts.setPitch(0.8f);
            //tts.setSpeechRate(1.f);
            String wel = null;
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);

            if(hour >= 6 && hour <= 11) {
                wel = "Bom dia, ";
            }
            else if(hour > 11 && hour < 19) {
                wel = "Boa tarde, ";
            }
            else {
                wel = "Boa noite, ";
            }

            String[] textoSeparado = texto.split(" ");
            tts.speak(wel + textoSeparado[0]+ " " +textoSeparado[1] + ", Seja Bem Vindo ao SysPalma! Eu sou a Ryanna sua assistente digital", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void Mensagem(String Titulo, String Msg) {
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(Painel.this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

    private void btnSair() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle("SysPalma - Agrícola");//define o titulo
        builder.setMessage("Você deseja realmente sair da aplicação?");//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        android.app.AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
}
