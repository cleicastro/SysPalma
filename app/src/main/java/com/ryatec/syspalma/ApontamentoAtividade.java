package com.ryatec.syspalma;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoAtividade extends Fragment {

    private Button btnApontamento;
    private Button btnQuantMdo;
    private Button btnQuantMdoApoio;
    private Button btnColheita;
    private Button btnPatrimonio;
    private Button btnInsumo;
    private ExpandableRelativeLayout mExpandApontamento;
    private ExpandableRelativeLayout mExpandQuantMdo;
    private ExpandableRelativeLayout mExpandQuantMdoApoio;
    private ExpandableRelativeLayout mExpandColheita;
    private ExpandableRelativeLayout mExpandPatrimonio;
    private ExpandableRelativeLayout mExpandInsumo;

    private Spinner parcela;
    private Spinner patrimonio;
    private Spinner implemento;
    private Spinner atividade;
    private Spinner caixa;

    private EditText edtLinhaInicial;
    private EditText edtLinhaFinal;
    private EditText edtPlantas;
    private EditText edtTotalPlantas;
    private EditText edtHectares;
    private EditText edtCachos;
    private EditText edtPeso;
    private EditText edtPesoTotal;
    private EditText edtKMincial;
    private EditText edtKMfinal;
    private EditText edtQuantMdo;
    private TextView txtDetalhe;
    static Integer linhaFinalParc;
    static Integer distLinhasPessoa;

    static Switch apoiocalc;
    static Integer quantApoio = 0;

    static EditText apoio1;
    static EditText apoio2;
    static EditText apoio3;
    static EditText apoio4;
    static EditText apoio5;

    static EditText planta1;
    static EditText planta2;
    static EditText planta3;
    static EditText planta4;
    static EditText planta5;

    static EditText ha1;
    static EditText ha2;
    static EditText ha3;
    static EditText ha4;
    static EditText ha5;

    static EditText cachos1;
    static EditText cachos2;
    static EditText cachos3;
    static EditText cachos4;
    static EditText cachos5;

    static LinearLayout linearMdo;
    static LinearLayout linearApoio;

    private Button salvarApontamento;
    private GetSetCache getSetCache = new GetSetCache();

    public ApontamentoAtividade() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandlelayout, container, false);
        btnApontamento = (Button) view.findViewById(R.id.btnApontamento);
        btnColheita = (Button) view.findViewById(R.id.btnColheita);
        btnPatrimonio = (Button) view.findViewById(R.id.btnPatrimonio);
        btnInsumo = (Button) view.findViewById(R.id.btnInsumo);
        btnQuantMdo = (Button) view.findViewById(R.id.btnDivisaoMdo);
        btnQuantMdoApoio = (Button) view.findViewById(R.id.btnDivisaoMdoApoio);

        mExpandApontamento = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandApontamento);
        mExpandColheita = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandColheita);
        mExpandPatrimonio = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandPatrimonio);
        mExpandInsumo = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandInsumo);
        mExpandQuantMdo = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandDivisaoMdo);
        mExpandQuantMdoApoio = (ExpandableRelativeLayout) view.findViewById(R.id.mExpandDivisaoMdoApoio);

        apoiocalc = view.findViewById(R.id.switch1);

          apoio1 = view.findViewById(R.id.Apoio1);
          apoio2 = view.findViewById(R.id.Apoio2);
          apoio3 = view.findViewById(R.id.Apoio3);
          apoio4 = view.findViewById(R.id.Apoio4);
          apoio5 = view.findViewById(R.id.Apoio5);

          planta1 = view.findViewById(R.id.plantas1);
          planta2 = view.findViewById(R.id.plantas2);
          planta3 = view.findViewById(R.id.plantas3);
          planta4 = view.findViewById(R.id.plantas4);
          planta5 = view.findViewById(R.id.plantas5);

          ha1 = view.findViewById(R.id.hectares1);
          ha2 = view.findViewById(R.id.hectares2);
          ha3 = view.findViewById(R.id.hectares3);
          ha4 = view.findViewById(R.id.hectares4);
          ha5 = view.findViewById(R.id.hectares5);

          cachos1 = view.findViewById(R.id.cachos1);
          cachos2 = view.findViewById(R.id.cachos2);
          cachos3 = view.findViewById(R.id.cachos3);
          cachos4 = view.findViewById(R.id.cachos4);
          cachos5 = view.findViewById(R.id.cachos5);

        apoio1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!apoio1.getText().toString().isEmpty()) {
                        String busca = apoio1.getText().toString();
                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaColaboradoresPesquisa(busca);
                        if(userList.size() > 0){
                            Toast.makeText(getContext(), "Apoio: "+userList.get(0).get("nome"), Toast.LENGTH_SHORT).show();
                            boolean apoiocalcver = apoiocalc.isChecked();
                            if(apoiocalcver){
                                apoio();
                            }else {
                                planta1.setText(edtPlantas.getText().toString());
                                ha1.setText(edtHectares.getText().toString());
                                cachos1.setText(edtCachos.getText().toString());
                            }
                        }else{
                            alerta("Atenção!","Colaborador não localizado, favor verifique a matrícula digitada.");
                        }
                    }
                }else{
                    //AO RECEBER O FOCO VERIFICA SE O CAMPO ESTÁ VAZIO PARA ZERAR OS CAMPOS
                    if (apoio1.getText().toString().isEmpty()) {
                        planta1.setText("0");
                        ha1.setText("0.00");
                        cachos1.setText("0");
                    }
                }
            }
        });
        apoio2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!apoio2.getText().toString().isEmpty()) {
                        String busca = apoio2.getText().toString();
                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaColaboradoresPesquisa(busca);
                        if(userList.size() > 0){
                            Toast.makeText(getContext(), "Apoio: "+userList.get(0).get("nome"), Toast.LENGTH_SHORT).show();
                            boolean apoiocalcver = apoiocalc.isChecked();
                            if(apoiocalcver){
                                apoio();
                            }else {
                                planta2.setText(edtPlantas.getText().toString());
                                ha2.setText(edtHectares.getText().toString());
                                cachos2.setText(edtCachos.getText().toString());
                            }
                        }else{
                            alerta("Atenção!","Colaborador não localizado, favor verifique a matrícula digitada.");
                        }
                    }
                }else{
                    //AO RECEBER O FOCO VERIFICA SE O CAMPO ESTÁ VAZIO PARA ZERAR OS CAMPOS
                    if (apoio2.getText().toString().isEmpty()) {
                        planta2.setText("0");
                        ha2.setText("0.00");
                        cachos2.setText("0");
                    }
                }
            }
        });
        apoio3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!apoio3.getText().toString().isEmpty()) {
                        String busca = apoio3.getText().toString();
                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaColaboradoresPesquisa(busca);
                        if(userList.size() > 0){
                            Toast.makeText(getContext(), "Apoio: "+userList.get(0).get("nome"), Toast.LENGTH_SHORT).show();
                            boolean apoiocalcver = apoiocalc.isChecked();
                            if(apoiocalcver){
                                apoio();
                            }else {
                                planta3.setText(edtPlantas.getText().toString());
                                ha3.setText(edtHectares.getText().toString());
                                cachos3.setText(edtCachos.getText().toString());
                            }
                        }else{
                            alerta("Atenção!","Colaborador não localizado, favor verifique a matrícula digitada.");
                        }
                    }
                }else{
                    //AO RECEBER O FOCO VERIFICA SE O CAMPO ESTÁ VAZIO PARA ZERAR OS CAMPOS
                    if (apoio3.getText().toString().isEmpty()) {
                        planta3.setText("0");
                        ha3.setText("0.00");
                        cachos3.setText("0");
                    }
                }
            }
        });
        apoio4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!apoio4.getText().toString().isEmpty()) {
                        String busca = apoio4.getText().toString();
                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaColaboradoresPesquisa(busca);
                        if(userList.size() > 0){
                            Toast.makeText(getContext(), "Apoio: "+userList.get(0).get("nome"), Toast.LENGTH_SHORT).show();
                            boolean apoiocalcver = apoiocalc.isChecked();
                            if(apoiocalcver){
                                apoio();
                            }else {
                                planta4.setText(edtPlantas.getText().toString());
                                ha4.setText(edtHectares.getText().toString());
                                cachos4.setText(edtCachos.getText().toString());
                            }
                        }else{
                            alerta("Atenção!","Colaborador não localizado, favor verifique a matrícula digitada.");
                        }
                    }
                }else{
                    //AO RECEBER O FOCO VERIFICA SE O CAMPO ESTÁ VAZIO PARA ZERAR OS CAMPOS
                    if (apoio4.getText().toString().isEmpty()) {
                        planta4.setText("0");
                        ha4.setText("0.00");
                        cachos4.setText("0");
                    }
                }
            }
        });
        apoio5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!apoio5.getText().toString().isEmpty()) {
                        String busca = apoio5.getText().toString();
                        Database_syspalma db = new Database_syspalma(getContext());
                        ArrayList<HashMap<String, String>> userList = db.ListaColaboradoresPesquisa(busca);
                        if(userList.size() > 0){
                            Toast.makeText(getContext(), "Apoio: "+userList.get(0).get("nome"), Toast.LENGTH_SHORT).show();
                            boolean apoiocalcver = apoiocalc.isChecked();
                            if(apoiocalcver){
                                apoio();
                            }else {
                                planta5.setText(edtPlantas.getText().toString());
                                ha5.setText(edtHectares.getText().toString());
                                cachos5.setText(edtCachos.getText().toString());
                            }
                        }else{
                            alerta("Atenção!","Colaborador não localizado, favor verifique a matrícula digitada.");
                        }
                    }
                }else{
                    //AO RECEBER O FOCO VERIFICA SE O CAMPO ESTÁ VAZIO PARA ZERAR OS CAMPOS
                    if (apoio5.getText().toString().isEmpty()) {
                        planta5.setText("0");
                        ha5.setText("0.00");
                        cachos5.setText("0");
                    }
                }
            }
        });
        apoiocalc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    apoio();
                }else{
                    Integer quantApoio = 1;
                    Integer setPlantasApoio = new Integer(edtPlantas.getText().toString().replace(",","")) / quantApoio;
                    Integer setCachosApoio = new Integer(edtCachos.getText().toString()) / quantApoio;
                    Double setHectaresApoio = new Double(edtHectares.getText().toString()) / quantApoio;

                    if(!apoio1.getText().toString().isEmpty()){
                        planta1.setText(setPlantasApoio.toString());
                        ha1.setText(setHectaresApoio.toString());
                        cachos1.setText(setCachosApoio.toString());
                    }
                    if(!apoio2.getText().toString().isEmpty()){
                        planta2.setText(setPlantasApoio.toString());
                        ha2.setText(setHectaresApoio.toString());
                        cachos2.setText(setCachosApoio.toString());
                    }
                    if(!apoio3.getText().toString().isEmpty()){
                        planta3.setText(setPlantasApoio.toString());
                        ha3.setText(setHectaresApoio.toString());
                        cachos3.setText(setCachosApoio.toString());
                    }
                    if(!apoio4.getText().toString().isEmpty()){
                        planta4.setText(setPlantasApoio.toString());
                        ha4.setText(setHectaresApoio.toString());
                        cachos4.setText(setCachosApoio.toString());
                    }
                    if(!apoio5.getText().toString().isEmpty()){
                        planta5.setText(setPlantasApoio.toString());
                        ha5.setText(setHectaresApoio.toString());
                        cachos5.setText(setCachosApoio.toString());
                    }
                    Toast.makeText(getContext(), "Nenhum Apoio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearMdo = view.findViewById(R.id.linearDMDO);
        linearApoio = view.findViewById(R.id.linearApoio);

        if(!getSetCache.getOperacao().equals("Colheita")){
            btnColheita.setVisibility(View.GONE);
            mExpandColheita.setVisibility(View.GONE);
        }

        parcela = (Spinner) view.findViewById(R.id.placa);
        implemento = (Spinner) view.findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) view.findViewById(R.id.spinner_patrimonio);
        atividade = (Spinner) view.findViewById(R.id.motorista);
        caixa = (Spinner) view.findViewById(R.id.spinnerCaixa);

        edtQuantMdo = (EditText) view.findViewById(R.id.editQuantmdo);
        edtLinhaInicial = (EditText) view.findViewById(R.id.cpf);
        edtLinhaFinal = (EditText) view.findViewById(R.id.transportadora);
        edtPlantas = (EditText) view.findViewById(R.id.toneladas);
        edtTotalPlantas = (EditText) view.findViewById(R.id.editTotalPlantas);
        edtHectares = (EditText) view.findViewById(R.id.editHectares);

        edtCachos = (EditText) view.findViewById(R.id.editCachos);
        edtPeso = (EditText) view.findViewById(R.id.editPeso);
        edtPesoTotal = (EditText) view.findViewById(R.id.editPesoTotal);
        edtKMincial = (EditText) view.findViewById(R.id.editkmInicial);
        edtKMfinal = (EditText) view.findViewById(R.id.editkmFinal);
        txtDetalhe = (TextView) view.findViewById(R.id.dysplayHect);

        //CARREGA OS DADOS DA DISTRIBUIÇÃO AUTOMÁTICA
        if(GetSetCache.getSetSpinnerParcela() != null && GetSetCache.getSetPessoas()>1){
            parcela.post(new Runnable() {
                @Override
                public void run() {
                    parcela.setSelection(GetSetCache.getSetSpinnerParcela());
                    atividade.setSelection(GetSetCache.getSetSpinnerAtividade());
                }
            });

            edtQuantMdo.setText(GetSetCache.getSetPessoas().toString());
            Integer newlinha = GetSetCache.getSetLinhaFinal() + 1;
            edtLinhaInicial.setText(newlinha.toString());
            Integer intervalo = GetSetCache.getSetDiferencaLinhas() + newlinha;
            edtLinhaFinal.setText(intervalo.toString());
            edtQuantMdo.setEnabled(false);
            mExpandQuantMdo.toggle();
        }

        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        btnApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandApontamento.toggle();
            }
        });

        btnQuantMdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearMdo.setVisibility(View.VISIBLE);
                mExpandQuantMdo.toggle();
            }
        });
        btnQuantMdoApoio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearApoio.setVisibility(View.VISIBLE);
                mExpandQuantMdoApoio.toggle();
            }
        });
        btnColheita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandColheita.toggle();
                edtCachos.requestFocus(edtCachos.getText().length());
            }
        });

        btnPatrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandPatrimonio.toggle();
            }
        });

        btnInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandInsumo.toggle();
            }
        });

        //Quando selecionar a atividade retorna o a área da parcela realizada
        atividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getParcela = parcela.getSelectedItem().toString();
                String getAtividade = parent.getSelectedItem().toString();
                //retornar o detalhe da parcela
                Database_syspalma database_syspalma = new Database_syspalma(getContext());
                ArrayList<HashMap<String, String>> dtlApontParcela = database_syspalma.RetornoDifLinhas(GetSetCache.getFazendaGet(), getParcela, getAtividade);
                //Caso rentorne nulo, retona o total da parcela em hectares
                if(dtlApontParcela.size() > 0) {
                    txtDetalhe.setText(dtlApontParcela.get(0).get("parcela")+" | Restando: "+dtlApontParcela.get(0).get("area_restando")+" ha | Pessoas: "+dtlApontParcela.get(0).get("mdo"));
                    if(new Double(edtHectares.getText().toString()) > new Double(dtlApontParcela.get(0).get("area_restando"))) {
                        alerta("Atenção!", "Você atingiu o total de área disponível desta parcela.");
                        edtQuantMdo.setEnabled(true);
                        edtLinhaInicial.setEnabled(true);
                        edtLinhaFinal.setEnabled(true);
                        edtQuantMdo.setFocusable(true);
                        edtLinhaInicial.setFocusable(true);
                        edtLinhaFinal.setFocusable(true);
                    }
                }else{
                    String areaParcTotal = edtHectares.getText().toString();
                    txtDetalhe.setText(getParcela+" | Restando: "+areaParcTotal+" ha | Pessoas: 0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        parcela.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getParcela = parent.getSelectedItem().toString();
                String getAtividade = atividade.getSelectedItem().toString();
                ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela,0,0);
                //CARREGA OS DADOS DA PARCELA
                if(inventario.size() > 0){
                    edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                    edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                    edtPlantas.setText(inventario.get(0).get("plantas").toString());
                    edtHectares.setText(inventario.get(0).get("hectares").toString());
                    edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());
                    GetSetCache.setIdParcela(new Integer(inventario.get(0).get("idParcela")));
                }else{
                    edtLinhaInicial.setText("0");
                    edtLinhaFinal.setText("0");
                    edtPlantas.setText("0");
                    edtHectares.setText("0.00");
                }

                //retornar o detalhe da parcela
                Database_syspalma database_syspalma = new Database_syspalma(getContext());
                ArrayList<HashMap<String, String>> dtlApontParcela = database_syspalma.RetornoDifLinhas(GetSetCache.getFazendaGet(),
                        getParcela, getAtividade);
                /* Display do detalhe da parcela
                    Caso rentorne nulo, retona o total da parcela em hectares
                */
                if(dtlApontParcela.size() >0) {
                    txtDetalhe.setText(dtlApontParcela.get(0).get("parcela") + " | Restando: " + dtlApontParcela.get(0).get("area_restando") +
                            " ha | Pessoas: " + dtlApontParcela.get(0).get("mdo"));
                } else{
                    String areaParcTotal = edtHectares.getText().toString();
                    txtDetalhe.setText(getParcela+" | Restando: "+areaParcTotal+" ha | Pessoas: 0");
                }
                //habilita a distribuição de média/dia caso a parcela seja alterada
                edtQuantMdo.setEnabled(true);
                if (position != 0) {
                    linhaFinalParc = new Integer(edtLinhaFinal.getText().toString());
                    if (getParcela.equals(GetSetCache.getSetParcela())) {
                        edtQuantMdo.setText(GetSetCache.getSetPessoas().toString());
                        Integer newlinha = GetSetCache.getSetLinhaFinal() + 1;
                        edtLinhaInicial.setText(newlinha.toString());
                        Integer intervalo = GetSetCache.getSetDiferencaLinhas() + newlinha;
                        edtLinhaFinal.setText(intervalo.toString());

                        edtQuantMdo.setEnabled(false);
                        edtLinhaInicial.setEnabled(false);
                        edtLinhaFinal.setEnabled(false);
                        //edtQuantMdo.setFocusable(false);
                        //edtLinhaInicial.setFocusable(false);
                        //edtLinhaFinal.setFocusable(false);
                        //mExpandQuantMdo.toggle();

                    } else {
                        if (GetSetCache.getSetParcela() != null) {
                            edtQuantMdo.setEnabled(true);
                            edtLinhaInicial.setEnabled(true);
                            edtLinhaFinal.setEnabled(true);
                            edtQuantMdo.setFocusable(true);
                            edtLinhaInicial.setFocusable(true);
                            edtLinhaFinal.setFocusable(true);
                            alerta("Apontamento de parcelas", "Atenção! \nEstá parcela não é a mesma do último apontamento(" + GetSetCache.getSetParcela() + "), isso afetará a distribuição de linhas automática. \nCaso não seja necessário o apontamento automático, ignore...");
                        }
                        edtQuantMdo.setText("1");
                    }
                } else {
                    linhaFinalParc = 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //ATEREI PARA QUANDO EDITAR O CONTEUDO DO EDITTEXT

        edtLinhaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!edtLinhaInicial.getText().toString().isEmpty()) {
                        Integer lin_in = new Integer(edtLinhaInicial.getText().toString());
                        Integer lin_fin = new Integer(edtLinhaFinal.getText().toString());
                        String getParcela = parcela.getSelectedItem().toString();
                        BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);
                        ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);

                        if(inventario.size() > 0){
                            edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                            edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                            edtPlantas.setText(inventario.get(0).get("plantas").toString());
                            //edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                            //CALCULOS NA REGRA DE 3 PARA O TAMANHO DA PARCELA
                            Double haGet = new Double(inventario.get(0).get("hectares").toString());
                            Integer plantasGet = new Integer(inventario.get(0).get("plantas").toString());
                            Integer totalplantasGet = new Integer(edtTotalPlantas.getText().toString());

                            Double area_calculada = (haGet / totalplantasGet) * plantasGet;
                            DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            edtHectares.setText(ndf.format(area_calculada).toString());
                        }
                    }
                }
            }
        });
        edtLinhaFinal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (!edtLinhaFinal.getText().toString().isEmpty()) {
                        Integer lin_in = new Integer(edtLinhaInicial.getText().toString());
                        Integer lin_fin = new Integer(edtLinhaFinal.getText().toString());
                        String getParcela = parcela.getSelectedItem().toString();
                        BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);
                        ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, lin_in, lin_fin);

                        if(inventario.size() > 0){
                            edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                            edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                            edtPlantas.setText(inventario.get(0).get("plantas").toString());
                            //edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                            //CALCULOS NA REGRA DE 3 PARA O TAMANHO DA PARCELA
                            Double haGet = new Double(inventario.get(0).get("hectares").toString());
                            Integer plantasGet = new Integer(inventario.get(0).get("plantas").toString());
                            Integer totalplantasGet = new Integer(edtTotalPlantas.getText().toString());

                            Double area_calculada = (haGet / totalplantasGet) * plantasGet;
                            DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            edtHectares.setText(ndf.format(area_calculada).toString());
                        }
                    }
                }
                }
        });
        edtQuantMdo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtQuantMdo.getText().length() > 0 ) {
                    if (new Integer(edtQuantMdo.getText().toString()) > 0) {
                        Integer linIn = new Integer(edtLinhaInicial.getText().toString());

                        Integer linFm = linhaFinalParc;
                        Integer pessoas = new Integer(edtQuantMdo.getText().toString());
                        distLinhasPessoa = ((linFm - linIn) / pessoas);
                        Integer operacaoLinha = (distLinhasPessoa) + linIn;
                        Toast.makeText(getContext(), "Total de linhas: " + (linFm - linIn)+" \n Divisão de linhas: "+distLinhasPessoa, Toast.LENGTH_LONG).show();

                        edtLinhaFinal.setText(operacaoLinha.toString());

                        Integer lin_inset = new Integer(edtLinhaInicial.getText().toString());
                        Integer lin_finset = new Integer(edtLinhaFinal.getText().toString());
                        String getParcelaset = parcela.getSelectedItem().toString();
                        String getAtividadelaset = atividade.getSelectedItem().toString();

                        ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcelaset, lin_inset, lin_finset);

                        if(inventario.size() > 0){
                            Integer totalplantasGetSet = new Integer(edtTotalPlantas.getText().toString());
                            Double haGetSet =  new Double(inventario.get(0).get("hectares").toString());
                            //CALCULO PARA RECUPERAR A QUANTIDADE LINHA E HECTAR REAL (Preciso revisar)
                            //Double area_calculada = (haGetSet / totalplantasGetSet) * plantasGetSet;
                            //DecimalFormat ndfset = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            //edtHectares.setText(ndfset.format(area_calculada).toString());

                            //CALCULO DE DIVISÃO SOBRE A MÉDIA EM DISTRIBUIÇÃO AUTOMÁTICA
                            Double area_calculada = haGetSet/pessoas;
                            DecimalFormat ndfset = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
                            Integer planta_calculada = totalplantasGetSet/pessoas;
                            DecimalFormat ndfsetplanta = (DecimalFormat) new DecimalFormat("0").getInstance(Locale.US);
                            edtHectares.setText(ndfset.format(area_calculada).toString());
                            edtPlantas.setText(ndfsetplanta.format(planta_calculada).toString());

                            //retornar o detalhe da parcela
                            Database_syspalma database_syspalma = new Database_syspalma(getContext());
                            ArrayList<HashMap<String, String>> dtlApontParcela = database_syspalma.RetornoDifLinhas(GetSetCache.getFazendaGet(),
                                    getParcelaset, getAtividadelaset);
                            /* Display do detalhe da parcela
                                Caso rentorne nulo, retona o total da parcela em hectares
                                GERA UM ALERTA QUANDO A PARCELA PASSAR DO TOTAL
                            */
                            if(dtlApontParcela.size() >0) {
                                if(area_calculada > new Double(dtlApontParcela.get(0).get("area_restando"))){
                                    alerta("Atenção!","Este apontamento vai exceder os hectares restante");
                                }
                            }
                        }
                    }
                }
            }
        });

        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Database_syspalma database = new Database_syspalma(getContext());
                    String matriculaget = database.RetoronoIdMat(GetSetCache.getMatricula());
                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    database.RetoronoIdParcela(parcela.getSelectedItem().toString(), 1);

                    GetSetAtividade getatividade = new GetSetAtividade();
                    getatividade.setId_parcela(GetSetCache.getIdParcela());
                    getatividade.setAtividade(atividade.getSelectedItem().toString());
                    getatividade.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                    getatividade.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                    getatividade.setPlantas(Integer.parseInt(edtPlantas.getText().toString().replace(",","")));
                    getatividade.setArea_realizada(new Double(edtHectares.getText().toString()));
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo(matriculaget);
                    getatividade.setGerar_apontamento(GetSetCache.getFicha()+idget);

                    //ARMAZENA NA CLASSE GetSetCache OS VALORES PARA O APONTAMENTO AUTOMÁTICO
                    GetSetCache.setSetParcela(parcela.getSelectedItem().toString());
                    GetSetCache.setSetLinhaFinal(new Integer(edtLinhaFinal.getText().toString()));
                    GetSetCache.setSetPessoas(new Integer(edtQuantMdo.getText().toString()));
                    GetSetCache.setSetDiferencaLinhas(distLinhasPessoa);
                    GetSetCache.setSetSpinnerParcela(parcela.getSelectedItemPosition());
                    GetSetCache.setSetSpinnerAtividade(atividade.getSelectedItemPosition());

                    //CASO A ATIVIDADE NÃO SEJA DE PRODUÇÃO ARMAZENA A PARCELA PADRÃO 1
                    Integer atividadeExt = atividade.getSelectedItemPosition();
                    Integer parcelaFic = parcela.getSelectedItemPosition();
                    if(parcelaFic == 0 && atividadeExt>0){
                        getatividade.setId_parcela(1);
                        parcelaFic = 1;
                    }

                    if (parcelaFic > 0 && atividadeExt > 0){
                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {

                            if(!apoio1.getText().toString().isEmpty()){
                                GetSetAtividade getatividadeApoio = new GetSetAtividade();
                                getatividadeApoio.setId_parcela(GetSetCache.getIdParcela());
                                getatividadeApoio.setAtividade(atividade.getSelectedItem().toString()+" (APOIO)");
                                getatividadeApoio.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                                getatividadeApoio.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                                getatividadeApoio.setPlantas(Integer.parseInt(planta1.getText().toString().replace(",","")));
                                getatividadeApoio.setArea_realizada(new Double(ha1.getText().toString()));
                                getatividadeApoio.setId_ficha(GetSetCache.getFicha());
                                getatividadeApoio.setId_mdo(apoio1.getText().toString());
                                getatividadeApoio.setGerar_apontamento(GetSetCache.getFicha()+idget+".apoio1");
                                database.InserirRealizadoApontamento(getatividadeApoio);

                                if(getSetCache.getOperacao().equals("Colheita")){
                                    GetSetAtividade colheita1 = new GetSetAtividade();
                                    colheita1.setCachos(new Integer(cachos1.getText().toString()));
                                    colheita1.setPeso(new Double(edtPeso.getText().toString()));
                                    colheita1.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                    colheita1.setId_apontamento(GetSetCache.getFicha()+idget+".apoio1");
                                    database.InserirRealizadoColheita(colheita1);
                                }
                            }
                            if(!apoio2.getText().toString().isEmpty()){
                                GetSetAtividade getatividadeApoio = new GetSetAtividade();
                                getatividadeApoio.setId_parcela(GetSetCache.getIdParcela());
                                getatividadeApoio.setAtividade(atividade.getSelectedItem().toString()+" (APOIO)");
                                getatividadeApoio.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                                getatividadeApoio.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                                getatividadeApoio.setPlantas(Integer.parseInt(planta2.getText().toString().replace(",","")));
                                getatividadeApoio.setArea_realizada(new Double(ha2.getText().toString()));
                                getatividadeApoio.setId_ficha(GetSetCache.getFicha());
                                getatividadeApoio.setId_mdo(apoio2.getText().toString());
                                getatividadeApoio.setGerar_apontamento(GetSetCache.getFicha()+idget+".apoio2");
                                database.InserirRealizadoApontamento(getatividadeApoio);

                                if(getSetCache.getOperacao().equals("Colheita")){
                                    GetSetAtividade colheita1 = new GetSetAtividade();
                                    colheita1.setCachos(new Integer(cachos2.getText().toString()));
                                    colheita1.setPeso(new Double(edtPeso.getText().toString()));
                                    colheita1.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                    colheita1.setId_apontamento(GetSetCache.getFicha()+idget+".apoio2");
                                    database.InserirRealizadoColheita(colheita1);
                                }
                            }
                            if(!apoio3.getText().toString().isEmpty()){
                                GetSetAtividade getatividadeApoio = new GetSetAtividade();
                                getatividadeApoio.setId_parcela(GetSetCache.getIdParcela());
                                getatividadeApoio.setAtividade(atividade.getSelectedItem().toString()+" (APOIO)");
                                getatividadeApoio.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                                getatividadeApoio.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                                getatividadeApoio.setPlantas(Integer.parseInt(planta3.getText().toString().replace(",","")));
                                getatividadeApoio.setArea_realizada(new Double(ha3.getText().toString()));
                                getatividadeApoio.setId_ficha(GetSetCache.getFicha());
                                getatividadeApoio.setId_mdo(apoio3.getText().toString());
                                getatividadeApoio.setGerar_apontamento(GetSetCache.getFicha()+idget+".apoio3");
                                database.InserirRealizadoApontamento(getatividadeApoio);

                                if(getSetCache.getOperacao().equals("Colheita")){
                                    GetSetAtividade colheita1 = new GetSetAtividade();
                                    colheita1.setCachos(new Integer(cachos3.getText().toString()));
                                    colheita1.setPeso(new Double(edtPeso.getText().toString()));
                                    colheita1.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                    colheita1.setId_apontamento(GetSetCache.getFicha()+idget+".apoio3");
                                    database.InserirRealizadoColheita(colheita1);
                                }

                            }
                            if(!apoio4.getText().toString().isEmpty()){
                                GetSetAtividade getatividadeApoio = new GetSetAtividade();
                                getatividadeApoio.setId_parcela(GetSetCache.getIdParcela());
                                getatividadeApoio.setAtividade(atividade.getSelectedItem().toString()+" (APOIO)");
                                getatividadeApoio.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                                getatividadeApoio.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                                getatividadeApoio.setPlantas(Integer.parseInt(planta4.getText().toString().replace(",","")));
                                getatividadeApoio.setArea_realizada(new Double(ha4.getText().toString()));
                                getatividadeApoio.setId_ficha(GetSetCache.getFicha());
                                getatividadeApoio.setId_mdo(apoio4.getText().toString());
                                getatividadeApoio.setGerar_apontamento(GetSetCache.getFicha()+idget+".apoio4");
                                database.InserirRealizadoApontamento(getatividadeApoio);

                                if(getSetCache.getOperacao().equals("Colheita")){
                                    GetSetAtividade colheita1 = new GetSetAtividade();
                                    colheita1.setCachos(new Integer(cachos4.getText().toString()));
                                    colheita1.setPeso(new Double(edtPeso.getText().toString()));
                                    colheita1.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                    colheita1.setId_apontamento(GetSetCache.getFicha()+idget+".apoio4");
                                    database.InserirRealizadoColheita(colheita1);
                                }
                            }
                            if(!apoio5.getText().toString().isEmpty()){
                                GetSetAtividade getatividadeApoio = new GetSetAtividade();
                                getatividadeApoio.setId_parcela(GetSetCache.getIdParcela());
                                getatividadeApoio.setAtividade(atividade.getSelectedItem().toString()+" (APOIO)");
                                getatividadeApoio.setLinha_inicial(new Integer(edtLinhaInicial.getText().toString()));
                                getatividadeApoio.setLinha_final(new Integer(edtLinhaFinal.getText().toString()));
                                getatividadeApoio.setPlantas(Integer.parseInt(planta5.getText().toString().replace(",","")));
                                getatividadeApoio.setArea_realizada(new Double(ha5.getText().toString()));
                                getatividadeApoio.setId_ficha(GetSetCache.getFicha());
                                getatividadeApoio.setId_mdo(apoio5.getText().toString());
                                getatividadeApoio.setGerar_apontamento(GetSetCache.getFicha()+idget+".apoio5");
                                database.InserirRealizadoApontamento(getatividadeApoio);

                                if(getSetCache.getOperacao().equals("Colheita")){
                                    GetSetAtividade colheita1 = new GetSetAtividade();
                                    colheita1.setCachos(new Integer(cachos5.getText().toString()));
                                    colheita1.setPeso(new Double(edtPeso.getText().toString()));
                                    colheita1.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                    colheita1.setId_apontamento(GetSetCache.getFicha()+idget+".apoio5");
                                    database.InserirRealizadoColheita(colheita1);
                                }
                            }
                            //INSERIR OS DADOS DA COLHEITA
                            if(getSetCache.getOperacao().equals("Colheita")){
                                GetSetAtividade colheita = new GetSetAtividade();
                                colheita.setCachos(new Integer(edtCachos.getText().toString()));
                                colheita.setPeso(new Double(edtPeso.getText().toString()));
                                colheita.setCaixa(new Integer(caixa.getSelectedItem().toString()));
                                colheita.setId_apontamento(GetSetCache.getFicha()+idget);
                                database.InserirRealizadoColheita(colheita);
                            }

                            if (patrimonio.getSelectedItemPosition() > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                                database.RetoronoIdImplemento(implemento.getSelectedItem().toString());

                                getpatrimonio.setId_patrimonio(GetSetCache.getIdPatrimonio());
                                getpatrimonio.setId_implemento(GetSetCache.getIdImplemento());
                                getpatrimonio.setMarcador_inicial(new Double(edtKMincial.getText().toString()));
                                getpatrimonio.setMarcador_final(new Double(edtKMfinal.getText().toString()));
                                getpatrimonio.setId_apontamento(GetSetCache.getFicha()+idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                        }
                        //Toast.makeText(getContext(), "Apontamento: "+id_apontamento, Toast.LENGTH_SHORT).show();
                        exemplo_alerta("Apontamento", "Apontamento Inserido com sucesso!");
                    }else{
                        alerta("Atenção!", "Nenhuma Parcela ou Atividade selecionada!");
                    }

                }catch (SQLException erro){
                    alerta("Conexão","Erro: " + erro);
                }
            }
        });
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

    public ArrayList<HashMap<String, String>> BuscaParcela(String fazenda, String parcela, Integer linhaInicial, Integer linhaFinal){
        ArrayList<HashMap<String, String>> campo;
        campo = new ArrayList<HashMap<String, String>>();
        Database_syspalma Banco = new Database_syspalma(getContext());
        SQLiteDatabase database = Banco.getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        //Verifica se é a parcela toda ou intervalo de linhas
        if(linhaInicial == 0|| linhaFinal == 0) {
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'");
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }else{
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'  AND l.linha BETWEEN "+linhaInicial+" AND "+linhaFinal);
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }

        Cursor c = database.rawQuery(sql.toString(), null);
        if(c.getCount() > 0){
            c.moveToFirst();
            HashMap<String, String> map = new HashMap<String, String>();
            /*
            dados = c.getString(1);
            edtLinhaInicial.setText(c.getString(2));
            edtLinhaFinal.setText(c.getString(3));
            edtPlantas.setText(c.getString(4));
            edtHectares.setText(c.getString(5));
            */

            map.put("idParcela", c.getString(0));
            map.put("parcela", c.getString(1));
            map.put("lInicial", c.getString(2));
            map.put("lFinal", c.getString(3));
            map.put("plantas", c.getString(4));
            map.put("hectares", c.getString(5));
            campo.add(map);
        }
        return campo;
    }

    public ArrayList<HashMap<String, String>> BuscaParcelaRealizado(String fazenda, String parcela, Integer linhaInicial, Integer linhaFinal, String atividade){
        ArrayList<HashMap<String, String>> campo;
        campo = new ArrayList<HashMap<String, String>>();
        Database_syspalma Banco = new Database_syspalma(getContext());
        SQLiteDatabase database = Banco.getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        //Verifica se é a parcela toda ou intervalo de linhas
        if(linhaInicial == 0 || linhaFinal == 0) {
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'");
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }else{
            sql.append(" SELECT p.idparcela, p.parcela, MIN(linha) as linha_inicial, MAX(linha) as linha_final,");
            sql.append(" SUM(l.quant_planta) as quant_planta, area_parcela FROM i_linha l");
            sql.append(" INNER JOIN i_parcela p");
            sql.append(" ON l.id_parcela = p.idparcela");
            sql.append(" INNER JOIN i_fazenda f");
            sql.append(" ON p.id_fazenda = f.idfazenda");
            sql.append(" WHERE f.fazenda = '" + fazenda + "' AND p.parcela = '" + parcela + "'  AND l.linha BETWEEN "+linhaInicial+" AND "+linhaFinal);
            sql.append(" GROUP BY fazenda, idparcela, parcela, area_parcela");
        }

        Cursor c = database.rawQuery(sql.toString(), null);
        if(c.getCount() > 0){
            c.moveToFirst();
            HashMap<String, String> map = new HashMap<String, String>();
            /*
            dados = c.getString(1);
            edtLinhaInicial.setText(c.getString(2));
            edtLinhaFinal.setText(c.getString(3));
            edtPlantas.setText(c.getString(4));
            edtHectares.setText(c.getString(5));
            */

            map.put("idParcela", c.getString(0));
            map.put("parcela", c.getString(1));
            map.put("lInicial", c.getString(2));
            map.put("lFinal", c.getString(3));
            map.put("plantas", c.getString(4));
            map.put("hectares", c.getString(5));
            campo.add(map);
        }
        return campo;
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
    private void apoio(){
        if(!apoio1.getText().toString().isEmpty()){
            quantApoio ++;
        }
        if(!apoio2.getText().toString().isEmpty()){
            quantApoio ++;
        }
        if(!apoio3.getText().toString().isEmpty()){
            quantApoio ++;
        }
        if(!apoio4.getText().toString().isEmpty()){
            quantApoio ++;
        }
        if(!apoio5.getText().toString().isEmpty()){
            quantApoio ++;
        }

        Integer setPlantasApoio = new Integer(edtPlantas.getText().toString().replace(",","")) / quantApoio;
        Integer setCachosApoio = new Integer(edtCachos.getText().toString()) / quantApoio;
        Double setHectaresApoio = new Double(edtHectares.getText().toString()) / quantApoio;

        if(!apoio1.getText().toString().isEmpty()){
            planta1.setText(setPlantasApoio.toString());
            ha1.setText(setHectaresApoio.toString());
            cachos1.setText(setCachosApoio.toString());
        }else{
            planta1.setText("0");
            ha1.setText("0.00");
            cachos1.setText("0");
        }
        if(!apoio2.getText().toString().isEmpty()){
            planta2.setText(setPlantasApoio.toString());
            ha2.setText(setHectaresApoio.toString());
            cachos2.setText(setCachosApoio.toString());
        }else{
            planta2.setText("0");
            ha2.setText("0.00");
            cachos2.setText("0");
        }
        if(!apoio3.getText().toString().isEmpty()){
            planta3.setText(setPlantasApoio.toString());
            ha3.setText(setHectaresApoio.toString());
            cachos3.setText(setCachosApoio.toString());
        }else{
            planta3.setText("0");
            ha3.setText("0.00");
            cachos3.setText("0");
        }
        if(!apoio4.getText().toString().isEmpty()){
            planta4.setText(setPlantasApoio.toString());
            ha4.setText(setHectaresApoio.toString());
            cachos4.setText(setCachosApoio.toString());
        }else{
            planta4.setText("0");
            ha4.setText("0.00");
            cachos4.setText("0");
        }
        if(!apoio5.getText().toString().isEmpty()){
            planta5.setText(setPlantasApoio.toString());
            ha5.setText(setHectaresApoio.toString());
            cachos5.setText(setCachosApoio.toString());
        }else{
            planta5.setText("0");
            ha5.setText("0.00");
            cachos5.setText("0");
        }
        Toast.makeText(getContext(), "Apoio: "+quantApoio, Toast.LENGTH_SHORT).show();
        quantApoio = 0;
    }
}
