package com.ryatec.syspalma;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApontamentoMaquinario extends Fragment {
    private ListView myListView;
    private Toolbar toolbar;
    private ArrayList<HashMap<String, String>> userList;
    private HashMap<String, String> mapa;
    private List<String> mList;
    private ListAdapter adapter;
    private SwipeRefreshLayout tela_refresh;

    private String os;
    private Integer idPlanejamento;
    private String atividade;
    private String data;
    private String ficha;
    private SwipeRefreshLayout swipeContainer;

    public ApontamentoMaquinario() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ficha = GetSetCache.getFicha();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colheita_corte, container, false);

        myListView = (ListView) view.findViewById(R.id.os_corte);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Atualizando...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);

                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaMaquinarioResumo(ficha);
                        ListAdapter adapter = new AdapterCursosPersonalizado(getActivity(),userList);
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

        //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                AlertDialog.Builder Alerta = new AlertDialog.Builder(getContext());
                Alerta.setTitle("Remover Apontamento");
                Alerta.setMessage("Deseja realmente remover este apontamento?");
                Alerta.setCancelable(false);

                Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Database_syspalma db = new Database_syspalma(getContext());
                        TextView apontamento = (TextView) view.findViewById(R.id.ordemServico);

                        db.deletarApontamentoMaquinario(apontamento.getText().toString());
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

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView apontamento = (TextView) view.findViewById(R.id.ordemServico);
                Intent intent = new Intent(getActivity(), EditarMaquinario.class);
                //Passar o ID do apontamento
                intent.putExtra("apontamento", apontamento.getText().toString());
                startActivity(intent);
            }
        });
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
        chamarLista();
    }

    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(getContext());
        ArrayList<HashMap<String, String>> userList = db.ListaMaquinarioResumo(ficha);
        ListAdapter adapter = new AdapterCursosPersonalizado(getActivity(),userList);
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

            perfil.setImageResource(R.drawable.ic_if_tractor);

            matricula.setText(map.get("maquinario")+"      "+map.get("implemento"));
            nome.setText(" | Horas trabalhada: "+map.get("hrm_total"));
            funcao.setText(map.get("apontamento"));
            departamento.setText(map.get("matricula")+"      "+map.get("atividade"));
            empresa.setText(GetSetCache.getFazendaGet());

            Integer numero_pessoas = i+1;
            gestor.setText("N°: "+ numero_pessoas);
            return view;
        }
    }
}
