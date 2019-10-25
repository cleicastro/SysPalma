package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class AbastecimentosFrota extends AppCompatActivity {
    private ListView myListView;
    private String os;
    private Integer idPlanejamento;
    private String atividade;
    private String insertReturn = "não";
    private GetSetCache getSetCache = new GetSetCache();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ficha);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.listFicha);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView data = (TextView) view.findViewById(R.id.data);
                TextView ficha = (TextView) view.findViewById(R.id.ficha);
                if(ficha.getText().toString() != "") {
                    Intent intent = new Intent(getApplication(), imprimir.class);
                    intent.putExtra("cod_get",ficha.getText().toString());
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }else{
                    Mensagem("Atenção","Não há abastecimentos para imprimir");
                }
            }
        });
        //LONGO CLICK PARA EXCLUIR O REGISTRO SELECIONADO
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView ficha = (TextView) view.findViewById(R.id.ficha);
                TextView cupom = (TextView) view.findViewById(R.id.idFicha);
                remover(ficha.getText().toString(), cupom.getText().toString());
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton sync = (FloatingActionButton) findViewById(R.id.sync_enviar);
        fab.setVisibility(View.GONE);
        sync.setVisibility(View.GONE);
        chamarLista();
    }

    public void onResume() {
        super.onResume();
        chamarLista();
    }

    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(this);
        ArrayList<HashMap<String, String>> userList = db.ListaAbastecimentosFrota();

        ListAdapter adapter = new AdapterCursosPersonalizado(this,userList);
        /*
        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.fragment_resumo_ficha, new String[]{
                "idrealizado","id_responsavel_tecnico","data_realizado","id_responsavel_operacional"}, new int[]{R.id.ficha, R.id.resp_op,R.id.data,
                R.id.resp_tec});
        */
        myListView.setAdapter(adapter);
    }
    public void remover(final String fichaget, final String cupomget){
        AlertDialog.Builder Alerta = new AlertDialog.Builder(this);
        Alerta.setTitle("Remover Abastecimento");
        Alerta.setMessage("Deseja realmente remover este abastecimento?");
        Alerta.setCancelable(false);

        Alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Database_syspalma db = new Database_syspalma(getApplication());
                db.deletarAbastecimento(fichaget, cupomget);
                Toast.makeText(getApplication(), "Abastecimento: " + fichaget, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplication(), "Removido", Toast.LENGTH_SHORT).show();
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

    public void Mensagem(String Titulo, String Msg){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
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

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = a.getLayoutInflater()
                    .inflate(R.layout.fragment_resumo_ficha, viewGroup, false);

            final HashMap<String, String> map = list.get(i);
            Database_syspalma db = new Database_syspalma(a);

            TextView idCupom = (TextView) view.findViewById(R.id.idFicha);
            TextView label_text = (TextView) view.findViewById(R.id.ficha2);
            TextView apontamento = (TextView) view.findViewById(R.id.ficha);
            TextView resp = (TextView) view.findViewById(R.id.resp_op);
            TextView data = (TextView) view.findViewById(R.id.data);
            TextView km_desc = (TextView) view.findViewById(R.id.resp_tec);
            ImageView img = (ImageView) view.findViewById(R.id.imageView_geral);

            label_text.setText("Cód");
            apontamento.setText(map.get("cod_abastecimento"));
            km_desc.setText(map.get("nome"));
            data.setText(map.get("patrimonio"));
            idCupom.setText(map.get("cupom"));
            resp.setText(map.get("data")+" - "+map.get("km_horimetro")+"          "+map.get("insumo")+"    Litros: "+map.get("quantidade"));

            if(map.get("tipo") != null && map.get("tipo").equals("TRATOR")){
                img.setImageResource(R.drawable.ic_if_tractor);
            }else if(map.get("tipo") != null && map.get("tipo").equals("TANQUE")){
                img.setImageResource(R.drawable.ic_abastecimento_verde);
            }else{
                img.setImageResource(R.drawable.ic_caminhao);
            }
            return view;
        }
    }

}
