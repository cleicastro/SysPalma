package com.ryatec.syspalma;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryatec.syspalma.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ApontamentoLista extends AppCompatActivity {
    private ListView myListView;
    private String ficha;
    private String matricula;
    private Button btneditar;
    private Button btnexcluir;
    private boolean backup = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_ficha);

        ficha = getIntent().getStringExtra("ficha");
        matricula = getIntent().getStringExtra("matricula");
        backup = getIntent().getBooleanExtra("backup",false);

        myListView = (ListView) findViewById(R.id.listFicha);
        chamarLista();
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        chamarLista();
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaApontamento(ficha, matricula);

        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);
        /*
        ListAdapter adapter = new SimpleAdapter(getContext(), userList, R.layout.fragment_resumo_apontamento, new String[]{
                "idapontamento","parcela","atividade","ha","planta","linhas","cachos"}, new int[]{R.id.textId, R.id.parcela,
                R.id.atividade, R.id.ha, R.id.txtPlantas, R.id.txtLinhas, R.id.txtCachos});
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
                    .inflate(R.layout.fragment_resumo_apontamento, viewGroup, false);

                final HashMap<String, String> map = list.get(i);
                Database_syspalma db = new Database_syspalma(a);

            if(map.get("parcela")!= null) {
                ImageButton editarlinha = (ImageButton) view.findViewById(R.id.imageedt);
                ImageButton excluirlinha = (ImageButton) view.findViewById(R.id.imageexl);

                TextView parcela = (TextView) view.findViewById(R.id.parcela);
                //TextView id = (TextView) view.findViewById(R.id.textId);
                TextView atividade = (TextView) view.findViewById(R.id.atividade);
                TextView ha = (TextView) view.findViewById(R.id.ha);
                TextView txtPlantas = (TextView) view.findViewById(R.id.txtPlantas);
                TextView txtLinhas = (TextView) view.findViewById(R.id.txtLinhas);
                TextView txtCachos = (TextView) view.findViewById(R.id.txtCachos);
                TextView txtMat = (TextView) view.findViewById(R.id.txtviewmat);
                TextView txtNome = (TextView) view.findViewById(R.id.txtviewnome);
                TextView txtNomeprod = (TextView) view.findViewById(R.id.textView3);

                CardView apont = (CardView) view.findViewById(R.id.cardView2);
                CardView btn_acao = (CardView) view.findViewById(R.id.cardView3);

                parcela.setText(map.get("parcela"));
                atividade.setText(map.get("atividade"));
                ha.setText(map.get("ha"));
                txtPlantas.setText(map.get("planta"));
                txtLinhas.setText(map.get("linhas"));
                if(GetSetCache.getOperacao().equals("Colheita")){
                    txtCachos.setText(map.get("cachos"));
                    txtNomeprod.setText("Cachos");
                }else{
                    txtCachos.setText(map.get("producao"));
                    txtNomeprod.setText("Produção");
                }
                txtMat.setText(map.get("matricula"));
                txtNome.setText(map.get("nome"));

                //VERIFICA SE A TELA NÃO PERTENCE
                if(backup == false ) {
                    //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
                    apont.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder Alerta = new AlertDialog.Builder(getBaseContext());
                            Alerta.setTitle("Remover Apontamento");
                            Alerta.setMessage("Deseja realmente remover este apontamento?");
                            Alerta.setCancelable(false);

                            Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Database_syspalma db = new Database_syspalma(getBaseContext());
                                    db.deletarRegistro(map.get("apontamento"));
                                    Toast.makeText(getBaseContext(), "Apontamento: " + map.get("apontamento"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Removido", Toast.LENGTH_SHORT).show();
                                    chamarLista();
                                }
                            });
                            Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            Alerta.show();
                            return true;
                        }
                    });
                    apont.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String idget = map.get("apontamento");
                            Intent intent = new Intent(getBaseContext(), EditarApontamento.class);
                            //Passar o ID do apontamento
                            intent.putExtra("idget", idget);
                            startActivity(intent);
                        }
                    });

                    editarlinha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String idget = map.get("apontamento");
                            Intent intent = new Intent(getBaseContext(), EditarApontamento.class);
                            //Passar o ID do apontamento
                            intent.putExtra("idget", idget);
                            startActivity(intent);
                        }
                    });

                    excluirlinha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder Alerta = new AlertDialog.Builder(ApontamentoLista.this);
                            Alerta.setTitle("Remover Apontamento");
                            Alerta.setMessage("Deseja realmente remover este apontamento?");
                            Alerta.setCancelable(false);

                            Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Database_syspalma db = new Database_syspalma(getBaseContext());
                                    db.deletarRegistro(map.get("apontamento"));
                                    Toast.makeText(getBaseContext(), "Apontamento: " + map.get("apontamento"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Removido", Toast.LENGTH_SHORT).show();
                                    chamarLista();
                                }
                            });
                            Alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            Alerta.show();
                        }
                    });
                }else{
                    btn_acao.setVisibility(View.GONE);
                }
            }else{
                TextView txtPlantas = (TextView) view.findViewById(R.id.txtPlantas);
                txtPlantas.setText("SEM APONTAMENTO");
            }

            return view;
        }
    }

}
