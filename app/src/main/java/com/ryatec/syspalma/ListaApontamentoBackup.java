package com.ryatec.syspalma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryatec.syspalma.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaApontamentoBackup extends Activity {
    private ListView myListView;
    private String ficha;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_colheita_corte);
        myListView = (ListView) findViewById(R.id.os_corte);
        ficha = GetSetCache.getFicha();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getBaseContext(), "Atualizando...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);

                        Database_syspalma db = new Database_syspalma(getBaseContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaApontamentoResumo(ficha);
                        ListAdapter adapter = new AdapterCursosPersonalizado(ListaApontamentoBackup.this,userList);
                        myListView.setAdapter(adapter);
                    }
                }, 3000); // tempo atualizando
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        chamarLista();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView matricula = (TextView) view.findViewById(R.id.ficha);
                Intent intent = new Intent(getBaseContext(), ApontamentoLista.class);
                //Passar o ID do apontamento
                intent.putExtra("matricula", matricula.getText().toString());
                intent.putExtra("ficha",ficha);
                intent.putExtra("backup",true);
                startActivity(intent);
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

    public void onResume() {
        super.onResume();
        chamarLista();
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaApontamentoResumo(ficha);
        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);
/*
        ListAdapter adapter = new SimpleAdapter(getContext(), userList, R.layout.fragment_resumo_mdo, new String[]{
                "matricula","nome","atividade","funcao","cont", "fazenda"}, new int[]{R.id.ficha, R.id.fazenda,R.id.ordemServico,
                R.id.descricao, R.id.dataInicio, R.id.dataFim});
        //, R.id.dataInicio, R.id.dataFim, R.id.ha
*/
        myListView.setAdapter(adapter);
    }

    public class AdapterCursosPersonalizado extends BaseAdapter {
        ArrayList<HashMap<String, String>> list;
        Activity a;
        LayoutInflater myiflater;
        public AdapterCursosPersonalizado(Activity activity, ArrayList<HashMap<String, String>> list) {
            this.a=activity;
            this.list=list;
            myiflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = a.getLayoutInflater()
                    .inflate(R.layout.fragment_resumo_mdo, viewGroup, false);
            HashMap<String, String> map = list.get(i);

            ConstraintLayout fundo = (ConstraintLayout) view.findViewById(R.id.rowLayout);
            ImageView perfil = (ImageView) view.findViewById(R.id.imageStatus);
            TextView matricula = (TextView) view.findViewById(R.id.ficha);
            TextView nome = (TextView) view.findViewById(R.id.fazenda);
            TextView funcao = (TextView) view.findViewById(R.id.ordemServico);
            TextView departamento = (TextView) view.findViewById(R.id.descricao);
            TextView gestor = (TextView) view.findViewById(R.id.dataInicio);
            TextView empresa = (TextView) view.findViewById(R.id.dataFim);

            if(!map.get("matricula").contains("Nenhum")) {
                if (map.get("atividade").equals("FALTA")) {
                    perfil.setImageResource(R.drawable.ic_person_outline_red_24dp);
                } else if (map.get("atividade").contains("SERVIÇOS")) {
                    perfil.setImageResource(R.drawable.ic_person_outline_blue_24dp);
                } else {
                    perfil.setImageResource(R.drawable.ic_person_outline_black_24dp);
                }
            }

            matricula.setText(map.get("matricula"));
            nome.setText(map.get("nome"));
            funcao.setText(map.get("atividade"));
            departamento.setText(map.get("funcao"));
            empresa.setText(GetSetCache.getFazendaGet());

            Integer numero_pessoas = i+1;
            gestor.setText("N°: "+ numero_pessoas);
            return view;
        }
    }

}
