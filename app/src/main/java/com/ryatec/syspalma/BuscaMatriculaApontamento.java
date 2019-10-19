package com.ryatec.syspalma;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class BuscaMatriculaApontamento extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak, btnAddaMatricula;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextToSpeech tts;
    private String text, data_inicial, data_final;
    private ListView listview;
    private ArrayList listmdo;
    private FloatingActionButton ficha;
    private EditText matNova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_matricula_apontamento);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnAddaMatricula = (ImageButton) findViewById(R.id.buscaManual);
        listview = findViewById(R.id.listMatricula);
        ficha = findViewById(R.id.buscar);
        listmdo = new ArrayList();
        matNova = findViewById(R.id.busca_matricula);
        matNova.setSelection(matNova.getText().length());

        data_inicial =  getIntent().getStringExtra("data_inicial");
        data_final =  getIntent().getStringExtra("data_final");

        ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PainelProducaoOline.this, FichadeServico.class);
                Intent intent = new Intent(BuscaMatriculaApontamento.this, FichadeServico.class);
                intent.putExtra("data_inicial",data_inicial);
                intent.putExtra("data_final",data_final);
                intent.putExtra("matriculas",listmdo);
                startActivity(intent);
            }
        });
        btnAddaMatricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listmdo.add(matNova.getText().toString());
                matNova.setText("1120");
                matNova.setSelection(matNova.getText().length());
                chamarLista();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listmdo.remove(position);
                chamarLista();
                return false;
            }
        });
        Falar(GetSetUsuario.getNome(), true);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
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

    private void Falar(final String texto, final boolean entrar) {
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                    if(texto==null||"".equals(texto))
                    {
                        String texto2 = "Usuário não reconhecido";
                        tts.speak(texto2, TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        tts.setSpeechRate(0.85f);
                        tts.setPitch(0.85f);
                        //tts.setSpeechRate(1.f);
                        if(entrar){
                            tts.speak("Olá, para falar comigo aperte o botão.", TextToSpeech.QUEUE_FLUSH, null);
                        }else{
                            if(texto.equals("sim")){
                                tts.speak(texto+" Adicionado na lista de busca", TextToSpeech.QUEUE_FLUSH, null);
                            }else if(texto.equals("não")){
                                listmdo.remove(listmdo.size()-1);
                                chamarLista();
                                //tts.speak("Sua Resposta é, NÃO", TextToSpeech.QUEUE_FLUSH, null);
                            }else{
                                tts.speak("1120"+ texto + ", Adicionado na lista", TextToSpeech.QUEUE_FLUSH, null);
                                listmdo.add("1120"+texto);
                                chamarLista();
                                //promptSpeechInput();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    Falar(result.get(0), false);
                }
                break;
            }

        }
    }
    public void chamarLista(){
        ArrayAdapter<String> meuAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listmdo
        );
        listview.setAdapter(meuAdapter);
    }
    public void remover(final Integer idlista){
        AlertDialog.Builder Alerta = new AlertDialog.Builder(this);
        Alerta.setTitle("Remover Apontamento");
        Alerta.setMessage("Deseja realmente remover este apontamento?");
        Alerta.setCancelable(false);

        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listmdo.remove(idlista);
                Toast.makeText(getApplication(), "Item: " + idlista, Toast.LENGTH_SHORT).show();
                chamarLista();
            }
        });
        Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Alerta.create();
        Alerta.show();
    }
}
