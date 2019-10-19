package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class EditarMaquinario extends Activity {

    private Spinner patrimonio;
    private Spinner implemento;

    private EditText edtKMincial;
    private EditText edtKMfinal;

    static String id;
    private Button salvarApontamento;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_maquinario);

        //Recuperar o ID passado como parâmentro do intent
        id = getIntent().getStringExtra("apontamento");

        implemento = (Spinner) findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) findViewById(R.id.spinner_patrimonio);

        salvarApontamento= findViewById(R.id.salvar);
        salvarApontamento.setText("Editar");

        edtKMincial = (EditText) findViewById(R.id.editkmInicial);
        edtKMfinal = (EditText) findViewById(R.id.editkmFinal);

        carregar_valores();
        carregarValoresEdit(id);
        //Toast.makeText(this, "teste"+id, Toast.LENGTH_SHORT).show();
        //Quando selecionar a atividade retorna o a área da parcela realizada


        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Database_syspalma database = new Database_syspalma(getApplication());
                    Database_syspalma_backup database_backup = new Database_syspalma_backup(getApplication());
                    //CARREGAR OS DADOS DOS MAQUINÁRIOS
                    Integer trator = 1;
                    Integer implement = 1;
                    if (patrimonio.getSelectedItemPosition() > 0) {
                        trator = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                        implement = database.RetoronoIdImplemento(implemento.getSelectedItem().toString());
                    }

                    database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                    database.RetoronoIdImplemento(implemento.getSelectedItem().toString());

                    GetSetAtividade getatividade = new GetSetAtividade();

                    getatividade.setId_patrimonio(trator);
                    getatividade.setId_implemento(implement);
                    getatividade.setMarcador_inicial(new Double(edtKMincial.getText().toString()));
                    getatividade.setMarcador_final(new Double(edtKMfinal.getText().toString()));

                    //CASO A ATIVIDADE NÃO SEJA DE PRODUÇÃO ARMAZENA A PARCELA PADRÃO 1
                    database.alteraApontamentoMaquinario(getatividade, id);
                    database_backup.alteraApontamentoMaquinario(getatividade, id);

                    exemplo_alerta("Apontamento", "Apontamento alterado com sucesso!");
                }catch (SQLException erro){
                    exemplo_alerta("Banco de Dados","Erro: " + erro);
                }
            }
        });
        Button cancelar = (Button) findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void carregar_valores(){

        Database_syspalma db = new Database_syspalma(this);

        List<String> patrimonios = db.SelectPatrimonio("'TRATOR'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> implementos = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, implementos);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        implemento.setAdapter(dataAdapter3);

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
    private void carregarValoresEdit(String id){
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();


        StringBuilder sql_patrimonio = new StringBuilder();
        sql_patrimonio.append("SELECT p.descricao, pi.descricao, marcador_inicial, marcador_final FROM realizado_patrimonio rp ");
        sql_patrimonio.append("INNER JOIN c_patrimonio p ");
        sql_patrimonio.append("ON p.idpatrimonio = id_patrimonio ");
        sql_patrimonio.append("INNER JOIN c_patrimonio pi ");
        sql_patrimonio.append("ON pi.idpatrimonio = id_implemento ");
        sql_patrimonio.append("WHERE apontamento = '"+id+"'");

        Cursor c_patrimonio = database.rawQuery(sql_patrimonio.toString(), null);
        if(c_patrimonio.getCount() > 0){
            c_patrimonio.moveToFirst();
            for(int i =0; i < patrimonio.getCount();i++){
                if(patrimonio.getItemAtPosition(i).equals(c_patrimonio.getString(0))){
                    patrimonio.setSelection(i);
                }
            }
            for(int i =0; i < implemento.getCount();i++){
                if(implemento.getItemAtPosition(i).equals(c_patrimonio.getString(1))){
                    implemento.setSelection(i);
                }
            }
            edtKMincial.setText(c_patrimonio.getString(2));
            edtKMfinal.setText(c_patrimonio.getString(3));
        }
        database.close();
    }

}
