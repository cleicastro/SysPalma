package com.ryatec.syspalma;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoListaErro extends Fragment {
    private ListView myListView;
    private String ficha;
    private String matricula;
    private Button btneditar;
    private Button btnexcluir;

    public ApontamentoListaErro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ficha = GetSetCache.getFicha();
        matricula = GetSetCache.getMatricula();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colheita_corte, container, false);

        myListView = (ListView) view.findViewById(R.id.os_corte);

       /*
        //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                myListView.setClickable(true);
                final TextView idApontmento = view.findViewById(R.id.textId);
                AlertDialog.Builder Alerta = new AlertDialog.Builder(getContext());
                Alerta.setTitle("Remover Apontamento");
                Alerta.setMessage("Deseja realmente remover este apontamento?");
                Alerta.setCancelable(false);

                Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Database_syspalma db = new Database_syspalma(getContext());
                        db.deletarRegistro(new Integer(idApontmento.getText().toString()));
                        Toast.makeText(getContext(), "Id: "+new Integer(idApontmento.getText().toString()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Removido", Toast.LENGTH_SHORT).show();
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
        //CLICK CURTO PARA EDITAR O REGISTRO
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idApontmento = view.findViewById(R.id.textId);
                Integer idget = new Integer(idApontmento.getText().toString());
                Intent intent = new Intent(getContext(), EditarApontamento.class);
                //Passar o ID do apontamento
                intent.putExtra("idget",idget);
                startActivity(intent);
            }
        });
        */
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chamarLista();
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //chamarLista();
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(getContext());
        ArrayList<HashMap<String, String>> userList = db.ListaApontamentoErro(ficha);

        ListAdapter adapter = new AdapterCursosPersonalizado(getActivity(),userList);
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

            if(map.get("atividade")!= null) {
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

                parcela.setText(map.get("parcela"));
                atividade.setText(map.get("atividade"));
                ha.setText(map.get("ha"));
                txtPlantas.setText(map.get("planta"));
                txtLinhas.setText(map.get("linhas"));
                txtCachos.setText(map.get("cachos"));
                txtMat.setText(map.get("matricula"));
                txtNome.setText(map.get("nome"));

                editarlinha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String idget = map.get("apontamento");
                        Intent intent = new Intent(getContext(), EditarApontamento.class);
                        //Passar o ID do apontamento
                        intent.putExtra("idget", idget);
                        startActivity(intent);
                    }
                });

                excluirlinha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder Alerta = new AlertDialog.Builder(getContext());
                        Alerta.setTitle("Remover Apontamento");
                        Alerta.setMessage("Deseja realmente remover este apontamento?");
                        Alerta.setCancelable(false);

                        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Database_syspalma db = new Database_syspalma(getContext());
                                db.deletarRegistro(map.get("apontamento"));
                                Toast.makeText(getContext(), "Apontamento: " + map.get("apontamento"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Removido", Toast.LENGTH_SHORT).show();
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
                TextView txtPlantas = (TextView) view.findViewById(R.id.txtPlantas);
                txtPlantas.setText("SEM APONTAMENTO");
            }

            return view;
        }
    }

}
