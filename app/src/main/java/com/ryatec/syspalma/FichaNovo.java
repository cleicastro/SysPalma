package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.ryatec.syspalma.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FichaNovo extends Activity {
    private Spinner myListView;
    private Button salvar;
    private AlertDialog alerta;//atributo da classe.
    private CalendarView data;
    private GetSetCache getSetCache = new GetSetCache();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_novo);
        myListView = (Spinner) findViewById(R.id.lista_rt);
        salvar = (Button) findViewById(R.id.salvarFicha);

        Database_syspalma db = new Database_syspalma(this);
        List<String> resp = db.SelectRT();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, resp);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myListView.setAdapter(dataAdapter);
        data = (CalendarView) findViewById(R.id.calendarView);

        salvar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try{
                    Database_syspalma database = new Database_syspalma(getApplication());
                    Database_syspalma_backup database_backup = new Database_syspalma_backup(getApplication());
                    GetSetFicha ficha = new GetSetFicha();

                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataFicha = new Date(data.getDate());
                    String dateFormatada = dt.format(dataFicha);
                    Date data_hoje = new Date();
                    Date menor_data = new Date(data_hoje.getTime() - (15 * (1000 * 60 * 60 * 24)));

                    //DATA DE HOJE
                    /*
                    Date dataget = new Date();
                    if(dataFicha.getDay() > dataget.getDay() || dataFicha.getDay() < (dataget.getDay() - 15)){
                        Toast.makeText(FichaNovo.this, "Menor: "+dataFicha.getDay()+" / "+dataget.getDay(), Toast.LENGTH_SHORT).show();
                    }
                    */

                    String getMat = database.RetoronoIdResTec(myListView.getSelectedItem().toString());

                    SimpleDateFormat dtficha = new SimpleDateFormat("dMMy");
                    Date dataFichaNovo = new Date(data.getDate());
                    String fichaFormatada = GetSetUsuario.getMatricula()+dtficha.format(dataFichaNovo)+GetSetCache.getIdPlanejamento();

                    ficha.setId_planejado(getSetCache.getIdPlanejamento());
                    ficha.setFichaRealizado(fichaFormatada);
                    ficha.setData_realizado(dateFormatada.toString());
                    ficha.setMatricula_responsavel_tecnico(getMat);
                    ficha.setMatricula_responsavel_operacional(GetSetUsuario.getMatricula());

                    if(dataFicha.after(data_hoje) || dataFicha.before(menor_data)) {
                        exemplo_alerta("Atenção!","A data da ficha não pode ser acima da data atual ou menor que "+dt.format(menor_data).toString()+"! ");
                    }else{
                        database.InserirFicha(ficha);
                        database_backup.InserirFicha(ficha);
                        finish();
                    }

                }catch (SQLException erro){
                    //exemplo_alerta("Conexão","Erro: " + erro);
                }
            }
        });
    }

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

}
