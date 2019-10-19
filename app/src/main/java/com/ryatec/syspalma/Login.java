package com.ryatec.syspalma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ryatec.syspalma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static java.lang.Boolean.FALSE;

public class Login extends AppCompatActivity {
    private Database_syspalma db;
    private SQLiteDatabase conn;
    private Database_syspalma_backup db_backup;
    private SQLiteDatabase conn_backup;
    private static int SPLASH_TIME_OUT = 180000;

    private AlertDialog alerta;//atributo da classe.
    private static String urlUsuario = "http://adm.syspalma.com/index.php/webservice/usuario";
    private static String urlPessoas = "http://adm.syspalma.com/index.php/webservice/mdo";
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
    private Database_syspalma database = new Database_syspalma(this);
    private Database_syspalma_backup database_backup = new Database_syspalma_backup(this);
    private ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FECHA O APLICATIVO SE FICAR INATIVO POR 2 min
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_TIME_OUT);

        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        //PARA CRIAR O LOOP
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.palma);
        videoView.setVideoURI(uri);
        videoView.start();

//Comunicação com o banco de dados;
        try {
            db = new Database_syspalma(this);
            conn = db.getReadableDatabase();

            db_backup = new Database_syspalma_backup(this);
            conn_backup = db_backup.getReadableDatabase();

        } catch (SQLException erro) {
            exemplo_alerta("Conexão", "Erro: " + erro);
        }

        Button entrar = (Button) findViewById(R.id.btnLogin);
        final EditText matricula = (EditText) findViewById(R.id.txtusuario);
        final EditText senha = (EditText) findViewById(R.id.txtsenha);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StrUsuario = matricula.getText().toString();
                String StrSenha = senha.getText().toString();
                BuscaUsuario(StrUsuario, StrSenha);
            }
        });

        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.sync_usuario);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Baixando usuários", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                syncSQLite(urlUsuario);
                syncSQLitePessoas(urlPessoas);
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
                //Toast.makeText(login.this, "Você clicou no botão Ok", Toast.LENGTH_SHORT).show();
            }
        });
        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

    public String BuscaUsuario(String usuario, String senha){
        String dados="";
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT u.usuario, c.matricula, c.nome, c.funcao, c.departamento, u.tipo FROM p_usuario u");
        sql.append(" INNER JOIN p_colaborador c");
        sql.append(" ON u.matricula_colaborador = c.matricula");
        sql.append(" WHERE u.usuario='" + usuario + "'" + " AND u.senha='" + senha + "'");

        //Cursor c = database.query("p_usuario", new String[]{"usuario", "senha", "matricula_colaborador"}, "usuario='" + usuario + "'" + " AND Senha='" + senha + "'", null, null, null, null);
        Cursor c = database.rawQuery(sql.toString(), null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            dados = c.getString(0);
            //SETAR AS CONFIGURAÇÕES DO USUÁRIO
            GetSetUsuario.setUsuario(c.getString(0));
            GetSetUsuario.setMatricula(c.getString(1));
            GetSetUsuario.setNome(c.getString(2));
            GetSetUsuario.setFuncao(c.getString(3));
            GetSetUsuario.setDepartamento(c.getString(4));
            GetSetUsuario.setTipo(c.getString(5));

            Intent TrocaTela2 = new
                    Intent(Login.this, Painel.class);
            startActivity(TrocaTela2);
            finish();
        } else {
            Mensagem("Login inválido", "ID ou senha incorreto, Verifique novamente.");
            final EditText usuarioget = (EditText) findViewById(R.id.txtusuario);
            final EditText senhaget = (EditText) findViewById(R.id.txtsenha);
            usuarioget.requestFocus();
        }
        return dados;
    }

    public void Mensagem(String Titulo, String Msg){
        AlertDialog.Builder Alerta =
                new AlertDialog.Builder(Login.this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }

    //C�digo para Criar o Banco de �suarios
    public void updateSQLiteUsuario(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        try {
            final ProgressDialog barProgressDialog = new ProgressDialog(this);
            barProgressDialog.setTitle("Salvando internamente, aguarde...");
            barProgressDialog.setMessage("Salvando Informações ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(FALSE);
            barProgressDialog.show();

            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("p_usuario");
                Long id = null;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idusuario", obj.get("idusuario").toString());
                    queryValues.put("usuario", obj.get("usuario").toString());
                    queryValues.put("senha", obj.get("senha").toString());
                    queryValues.put("email", obj.get("email").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());
                    queryValues.put("matricula_colaborador", obj.get("matricula_colaborador").toString());
                    queryValues.put("situacao", obj.get("situacao").toString());
                    queryValues.put("data_cadastro", obj.get("data_cadastro").toString());
                    id =  database.insertUsuario(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Usuários atualzados com sucesso!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void updateSQLiteFuncionario(String response) {
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();
        try {
            final ProgressDialog barProgressDialog = new ProgressDialog(this);
            barProgressDialog.setTitle("Salvando internamente, aguarde...");
            barProgressDialog.setMessage("Salvando Informações ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(FALSE);
            barProgressDialog.show();

            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if (arr.length() != 0) {
                database.LimparTabela("p_colaborador");
                Long id = null;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    queryValues = new HashMap<String, String>();
                    queryValues.put("idcolaborador", obj.get("idcolaborador").toString());
                    queryValues.put("matricula", obj.get("matricula").toString());
                    queryValues.put("nome", obj.get("nome").toString());
                    queryValues.put("funcao", obj.get("funcao").toString());
                    queryValues.put("empresa", obj.get("empresa").toString());
                    queryValues.put("departamento", obj.get("departamento").toString());
                    queryValues.put("situacao", obj.get("situacao").toString());
                    queryValues.put("data_cadastro", obj.get("data_cadastro").toString());
                    queryValues.put("gestor", obj.get("gestor").toString());
                    queryValues.put("usuario", obj.get("usuario").toString());
                    queryValues.put("email", obj.get("email").toString());
                    queryValues.put("tipo", obj.get("tipo").toString());

                    id =  database.insertPessoas(queryValues);
                }
                if(id != null){
                    exemplo_alerta("Salvando...","Valores armazenados!");
                }else {
                    exemplo_alerta("Erro...","Falha ao salvar os dados!");
                }
            }
            barProgressDialog.dismiss();
        } catch (JSONException e) {
            barProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void syncSQLite(String urlParse) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Buscando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setTimeout(3000);
        RequestParams params = new RequestParams();
        // Janela de Progresso
        client.get(urlParse, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                //database.LimparTabela("p_usuario");
                //Toast.makeText(getApplicationContext(), "Atualizado com sucesso ", Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
                updateSQLiteUsuario(response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Falha " + statusCode, Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
            }
            @Override
            public void onFinish() {
                barProgressDialog.hide();
            }

        });
    }

    public void syncSQLitePessoas(String urlParsePessoas) {
        final ProgressDialog barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Sincronizando, aguarde...");
        barProgressDialog.setMessage("Buscando Informações ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(FALSE);
        barProgressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setTimeout(3000);
        RequestParams params = new RequestParams();
        // Janela de Progresso
        client.get(urlParsePessoas, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                //database.LimparTabela("p_usuario");
                //Toast.makeText(getApplicationContext(), "Atualizado com sucesso ", Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
                updateSQLiteFuncionario(response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Falha " + statusCode, Toast.LENGTH_LONG).show();
                barProgressDialog.hide();
            }
            @Override
            public void onFinish() {
                barProgressDialog.hide();
            }

        });
    }
}