package com.ryatec.syspalma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ryatec.syspalma.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TratosAdubacao extends Fragment {
    private ListView myListView;
    private GetSetCache getSetCache = new GetSetCache();
    public static String ATIVIDADE = "Adubação";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colheita_corte, container, false);
        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        myListView = (ListView) view.findViewById(R.id.os_corte);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView os = (TextView) view.findViewById(R.id.ordemServico);
                TextView idplanejamento = (TextView) view.findViewById(R.id.ficha);
                TextView idfazenda = (TextView) view.findViewById(R.id.textIdFazenda);
                TextView getfazenda = (TextView) view.findViewById(R.id.fazenda);

                if(os.getText().toString() != "") {
                    getSetCache.setOsPlanejamento(os.getText().toString());
                    getSetCache.setIdPlanejamento(new Integer(idplanejamento.getText().toString()));
                    getSetCache.setIdFazenda(new Integer(idfazenda.getText().toString()));
                    getSetCache.setFazendaGet(getfazenda.getText().toString());
                    getSetCache.setAtividade("ADUBAÇÃO");
                    getSetCache.setOperacao(ATIVIDADE);
                    Intent intent = new Intent(getContext(), MainFicha.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Atividade sem planejamento, favor sincronize as atividades!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chamarLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //chamarLista();
    }
    public void chamarLista(){
        Database_syspalma db = new Database_syspalma(getContext());
        ArrayList<HashMap<String, String>> userList = db.ListaPlanejado(ATIVIDADE);

        ListAdapter adapter = new SimpleAdapter(getContext(), userList, R.layout.fragment_resumo_os, new String[]{
                "id","fazenda","ordem_servico","descricao","data_inicio","data_fim","ha","idfazenda"}, new int[]{R.id.ficha, R.id.fazenda,R.id.ordemServico,
                R.id.descricao, R.id.dataInicio, R.id.dataFim, R.id.ha, R.id.textIdFazenda});
        myListView.setAdapter(adapter);
    }

}
