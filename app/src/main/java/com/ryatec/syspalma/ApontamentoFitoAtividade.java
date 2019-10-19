package com.ryatec.syspalma;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoFitoAtividade extends Fragment {

    private Button btnApontamento;
    private Button btnColheita;
    private Button btnPatrimonio;
    private ExpandableRelativeLayout mExpandApontamento;
    private ExpandableRelativeLayout mExpandColheita;
    private ExpandableRelativeLayout mExpandPatrimonio;

    private Spinner parcela;
    private Spinner patrimonio;
    private Spinner implemento;
    private Spinner atividade;
    private Spinner caixa;

    static LinearLayout linearApoio;

    private Button salvarApontamento;
    private GetSetCache getSetCache = new GetSetCache();

    public ApontamentoFitoAtividade() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandlelayout_fito, container, false);
        btnApontamento = (Button) view.findViewById(R.id.btnApontamento);
        btnColheita = (Button) view.findViewById(R.id.btnColheita);
        btnPatrimonio = (Button) view.findViewById(R.id.btnPatrimonio);

        mExpandApontamento = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandApontamento);
        mExpandColheita = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandColheita);
        mExpandPatrimonio = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandPatrimonio);


        parcela = (Spinner) view.findViewById(R.id.placa);
        implemento = (Spinner) view.findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) view.findViewById(R.id.spinner_patrimonio);
        atividade = (Spinner) view.findViewById(R.id.motorista);
        caixa = (Spinner) view.findViewById(R.id.spinnerCaixa);


        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        btnApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandApontamento.toggle();
            }
        });
        btnColheita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandColheita.toggle();
            }
        });

        btnPatrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandPatrimonio.toggle();
            }
        });
        //Quando selecionar a atividade retorna o a área da parcela realizada
        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        Button cancelar = (Button) view.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carregar_valores();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void carregar_valores(){

        Database_syspalma db = new Database_syspalma(getContext());
        List<String> parcelas = db.SelectParcela(GetSetCache.getFazendaGet());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, parcelas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcela.setAdapter(dataAdapter);

        List<String> patrimonios = db.SelectPatrimonio("'TRATOR'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> implementos = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, implementos);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        implemento.setAdapter(dataAdapter3);

        List<String> atividades = db.SelectAtividade(getSetCache.getOperacao());
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, atividades);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        atividade.setAdapter(dataAdapter4);
    }

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                getActivity().finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    private void alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//Cria o gerador do AlertDialog
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
