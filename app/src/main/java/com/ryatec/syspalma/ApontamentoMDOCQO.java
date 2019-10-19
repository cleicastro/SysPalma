package com.ryatec.syspalma;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoMDOCQO extends Fragment {
    public ApontamentoMDOCQO() {
        // Required empty public constructor
    }

    private Integer linhaRowTable;
    private TableLayout ll;
    private Integer lin_in = 1;
    private Integer lin_fin = 1;
    private Integer lin_total = 1;
    private Double divHa = 0.00;
    private Integer idParcela = 1;
    private Integer plantas = 0;
    private Double hectar = 0.00;


    private EditText edtLinhaInicial;
    private EditText edtLinhaFinal;
    private EditText edtPlantas;
    private EditText edtTotalPlantas;
    private EditText edtHectares;
    private Spinner parcela;
    private TextView legendaI, legendaII;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_cqo, container, false);

        ll = (TableLayout) view.findViewById(R.id.tabela_os);
        edtLinhaInicial = (EditText) view.findViewById(R.id.cpf);
        edtLinhaFinal = (EditText) view.findViewById(R.id.transportadora);
        edtPlantas = (EditText) view.findViewById(R.id.toneladas);
        edtTotalPlantas = (EditText) view.findViewById(R.id.editTotalPlantas);
        edtHectares = (EditText) view.findViewById(R.id.editHectares);
        parcela = view.findViewById(R.id.spinnerParcela);
        legendaI = view.findViewById(R.id.textLegendaI);
        legendaII = view.findViewById(R.id.textLegendaII);

        StringBuilder lengedIString = new StringBuilder();
        lengedIString.append("LIN: Linha\n");
        lengedIString.append("AVT: AVISOU O TÉCNICO\n");
        lengedIString.append("CR: CACHOS ESQUECIDO\n");
        lengedIString.append("CV: CACHO VERDE\n");
        lengedIString.append("CI: CACHO INCHADO\n");

        StringBuilder lengedIIString = new StringBuilder();
        lengedIIString.append("TCF: TALO COM FRUTO\n");
        lengedIIString.append("CNC: CACHO NÃO CARREADO\n");
        lengedIIString.append("FSC: FOLHA SEMI CORTADA\n");
        lengedIIString.append("FME: FOLHA MAL EMPILHADA\n");

        legendaI.setText(lengedIString.toString());
        legendaII.setText(lengedIIString.toString());

        final LinearLayout updButton = view.findViewById(R.id.linearLayoutAdd);

        parcela.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //LIMPA OS CAMPOS DAS LINHAS PREPARANDO PARA O PROXIMO INTERVALO
                int childCount = ll.getChildCount();
                if (childCount > 1) {
                    ll.removeViews(1, childCount - 1);
                }
                String getParcela = parent.getSelectedItem().toString();
                ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, 0, 0);

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

                    idParcela = (new Integer(inventario.get(0).get("idParcela")));
                    lin_in = new Integer(inventario.get(0).get("lInicial").toString());
                    lin_fin = new Integer(inventario.get(0).get("lFinal").toString());
                    plantas = new Integer(inventario.get(0).get("plantas").toString());
                    //hectar = new Double(edtHectares.getText().toString());

                    lin_total = lin_fin - lin_in;
                    divHa = area_calculada;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtLinhaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    lin_in = 0;
                    lin_fin = 0;
                    int childCount = ll.getChildCount();
                    if (childCount > 1) {
                        ll.removeViews(1, childCount - 1);
                    }
                    if (edtLinhaInicial.getText().toString().length() > 0 && edtLinhaFinal.getText().toString().length() > 0) {
                        lin_in = new Integer(edtLinhaInicial.getText().toString());
                        lin_fin = new Integer(edtLinhaFinal.getText().toString());

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

                            idParcela = (new Integer(inventario.get(0).get("idParcela")));
                            lin_in = new Integer(inventario.get(0).get("lInicial").toString());
                            lin_fin = new Integer(inventario.get(0).get("lFinal").toString());
                            plantas = new Integer(inventario.get(0).get("plantas").toString());
                            //hectar = new Double(edtHectares.getText().toString());

                            divHa = area_calculada;
                        }
                        lin_total = lin_fin - lin_in;

                        for (int i =0; i<=lin_total; i++){
                            addLinha();
                        }
                        updButton.setVisibility(View.VISIBLE);

                    }else{
                        edtLinhaInicial.setText(lin_in.toString());
                        edtLinhaFinal.setText(lin_fin.toString());
                    }
                }
            }
        });
        edtLinhaFinal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    lin_in = 0;
                    lin_fin = 0;
                    int childCount = ll.getChildCount();
                    if (childCount > 1) {
                        ll.removeViews(1, childCount - 1);
                    }
                    if (edtLinhaFinal.getText().toString().length() > 0 && edtLinhaInicial.getText().toString().length() > 0) {
                        lin_in = new Integer(edtLinhaInicial.getText().toString());
                        lin_fin = new Integer(edtLinhaFinal.getText().toString());
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

                            idParcela = (new Integer(inventario.get(0).get("idParcela")));
                            lin_in = new Integer(inventario.get(0).get("lInicial").toString());
                            lin_fin = new Integer(inventario.get(0).get("lFinal").toString());
                            plantas = new Integer(inventario.get(0).get("plantas").toString());
                            //hectar = new Double(edtHectares.getText().toString());

                            divHa = area_calculada;
                        }
                        lin_total = lin_fin - lin_in;
                        for (int i =0; i<=lin_total; i++){
                            addLinha();
                        }
                        updButton.setVisibility(View.VISIBLE);
                    }else{
                        edtLinhaInicial.setText(lin_in.toString());
                        edtLinhaFinal.setText(lin_fin.toString());
                    }
                }
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
    }
    public void addLinha(){
        linhaRowTable = ll.getChildCount() - 1;
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(2,2,2,2);


        EditText edtMatricula = new EditText(getContext());
        edtMatricula.setEms(5);
        edtMatricula.setInputType(1);
        edtMatricula.setGravity(Gravity.CENTER);
        //edtMatricula.setLayoutParams(params);
        edtMatricula.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtMatricula.setText("1120");
        edtMatricula.requestFocus(4);
        edtMatricula.setId(linhaRowTable+2);
        edtMatricula.setTag("edt1maticula");
        //edtMatricula.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Integer linha = linhaRowTable + lin_in;

        EditText edtLIN = new EditText(getContext());
        edtLIN.setText(linha.toString());
        edtLIN.setEms(2);
        edtLIN.setGravity(Gravity.CENTER);
        edtLIN.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtLIN.setId(linhaRowTable+3);
        edtLIN.setLayoutParams(params);
        edtLIN.setTextColor(ContextCompat.getColor(getContext(), R.color.vermelho));
        edtLIN.setTag("edtLIN");

        EditText edtCE = new EditText(getContext());
        edtCE.setEms(2);
        edtCE.setGravity(Gravity.CENTER);
        edtCE.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtCE.setId(linhaRowTable+4);
        edtCE.setLayoutParams(params);
        edtCE.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtCE.setHint("0");
        edtCE.setTag("edtCE");

        CheckBox AVTCE = new CheckBox(getContext());
        AVTCE.setGravity(Gravity.CENTER);
        AVTCE.setId(linhaRowTable+5);
        AVTCE.setTag("AVTCE");

        EditText edtCV = new EditText(getContext());
        edtCV.setEms(2);
        edtCV.setGravity(Gravity.CENTER);
        edtCV.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtCV.setId(linhaRowTable+6);
        edtCV.setLayoutParams(params);
        edtCV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtCV.setHint("0");
        edtCV.setTag("edtCV");

        EditText edtCI = new EditText(getContext());
        edtCI.setEms(2);
        edtCI.setGravity(Gravity.CENTER);
        edtCI.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtCI.setId(linhaRowTable+7);
        edtCI.setLayoutParams(params);
        edtCI.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtCI.setHint("0");
        edtCI.setTag("edtCI");

        EditText edtTCF = new EditText(getContext());
        edtTCF.setEms(2);
        edtTCF.setGravity(Gravity.CENTER);
        edtTCF.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtTCF.setId(linhaRowTable+8);
        edtTCF.setLayoutParams(params);
        edtTCF.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtTCF.setHint("0");
        edtTCF.setTag("edtTCF");

        EditText edtTC = new EditText(getContext());
        edtTC.setEms(2);
        edtTC.setGravity(Gravity.CENTER);
        edtTC.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtTC.setId(linhaRowTable+9);
        edtTC.setLayoutParams(params);
        edtTC.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtTC.setHint("0");
        edtTC.setTag("edtTC");

        EditText edtCNC = new EditText(getContext());
        edtCNC.setEms(2);
        edtCNC.setGravity(Gravity.CENTER);
        edtCNC.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtCNC.setId(linhaRowTable+10);
        edtCNC.setLayoutParams(params);
        edtCNC.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtCNC.setHint("0");
        edtCNC.setTag("edtCNC");

        CheckBox AVTC = new CheckBox(getContext());
        AVTC.setGravity(Gravity.CENTER);
        AVTC.setId(linhaRowTable+11);
        AVTC.setTag("AVTC");

        EditText edtFSC = new EditText(getContext());
        edtFSC.setEms(2);
        edtFSC.setGravity(Gravity.CENTER);
        edtFSC.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtFSC.setId(linhaRowTable+12);
        edtFSC.setLayoutParams(params);
        edtFSC.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtFSC.setHint("0");
        edtFSC.setTag("edtFSC");

        EditText edtFME = new EditText(getContext());
        edtFME.setEms(2);
        edtFME.setGravity(Gravity.CENTER);
        edtFME.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtFME.setId(linhaRowTable+13);
        edtFME.setLayoutParams(params);
        edtFME.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtFME.setHint("0");
        edtFME.setTag("edtFME");

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        row.addView(edtMatricula);
        row.addView(edtLIN);
        row.addView(edtCE);
        row.addView(AVTCE);
        row.addView(edtCV);
        row.addView(edtCI);
        row.addView(edtTCF);
        row.addView(edtTC);
        row.addView(edtCNC);
        row.addView(AVTC);
        row.addView(edtFSC);
        row.addView(edtFME);
        ll.addView(row,linhaRowTable+1);
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
}
