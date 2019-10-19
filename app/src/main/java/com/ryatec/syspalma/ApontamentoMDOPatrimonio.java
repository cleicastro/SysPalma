package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoMDOPatrimonio extends Fragment {

    private Spinner parcela1;
    private Spinner parcela2;
    private Spinner parcela3;
    private Spinner parcela4;
    private Spinner parcela5;
    private Spinner parcela6;
    private Spinner parcela7;
    private Spinner patrimonio;
    private Spinner implemento;
    private Spinner atividade;
    private Spinner caixa;
    private TableLayout ll;

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
    private EditText parcela;
    private EditText parcelaNumero;

    static Switch swapoio2;
    static Switch swapoio4;

    static EditText apoio1;
    static EditText apoio2;
    static EditText apoio3;
    static EditText apoio4;
    static EditText apoio5;

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

    static LinearLayout linearApoio;

    private TextView totalparc1, totalparc2, totalparc3, totalparc4, totalGeral;
    private Integer linhaRowTable;

    private Button salvarApontamento;
    private ImageButton upd, addMDO;
    private GetSetCache getSetCache = new GetSetCache();
    static LinearLayout apontamentoParcela;
    static TableRow linhaapoio1;
    static TableRow linhaapoio2;
    static TableRow linhaapoio3;
    static TableRow linhaapoio4;
    static EditText matApoioReset1;
    static EditText matApoioReset2;
    static EditText matApoioReset3;
    static EditText matApoioReset4;
    private Integer idParcelaAtividade = 1;

    //VARIAVES PARA A DISTRIBUIÇÃO DO APONTAMENTO
    private Integer linhaInicial1, linhaFinal1, plantas1, linhaInicial2, linhaFinal2, plantas2, linhaInicial3, linhaFinal3, plantas3, linhaInicial4, linhaFinal4, plantas4;
    private Integer idParcela1,idParcela2,idParcela3,idParcela4, Linhas1, Linhas2, Linhas3, Linhas4;
    private Double hectar1, hectar2, hectar3, hectar4, divHa1, divHa2, divHa3, divHa4;
    private Integer lin_in = 1;
    private Integer lin_fin = 1;

    public ApontamentoMDOPatrimonio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_maquinario, container, false);

        //parcela1 = (Spinner) view.findViewById(R.id.SpinnerParcela);
        parcela2 = (Spinner) view.findViewById(R.id.spinner2);
        parcela3 = (Spinner) view.findViewById(R.id.spinner3);
        parcela4 = (Spinner) view.findViewById(R.id.spinner4);
        parcela5 = (Spinner) view.findViewById(R.id.spinner5);
        //parcela6 = (Spinner) view.findViewById(R.id.spinner6);
        //parcela7 = (Spinner) view.findViewById(R.id.spinner7);
        parcela = view.findViewById(R.id.setParcelaGet);
        parcelaNumero = view.findViewById(R.id.setParcelaNumero);
        apontamentoParcela = view.findViewById(R.id.linearApontamento);

        implemento = (Spinner) view.findViewWithTag("spinner_implemento");
        patrimonio = (Spinner) view.findViewWithTag("spinner_patrimonio");
        atividade = (Spinner) view.findViewById(R.id.spinnerAtividade);
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

        totalparc1 = (TextView) view.findViewById(R.id.totaParc1);
        totalparc2 = (TextView) view.findViewById(R.id.totaParc2);
        totalparc3 = (TextView) view.findViewById(R.id.totaParc3);
        totalparc4 = (TextView) view.findViewById(R.id.totaParc4);
        totalGeral = (TextView) view.findViewById(R.id.totaParcTotal);

        //CONTROLE DE CONFIGURAÇÃO DO APOIO
        linhaapoio1 = (TableRow) view.findViewWithTag("idlinhaApoio1");
        linhaapoio2 = (TableRow) view.findViewWithTag("idlinhaApoio2");
        linhaapoio3 = (TableRow) view.findViewWithTag("idlinhaApoio3");
        linhaapoio4 = (TableRow) view.findViewWithTag("idlinhaApoio4");
        swapoio2 = (Switch) view.findViewById(R.id.swapoio2);
        swapoio4 = (Switch) view.findViewById(R.id.swapoio4);
        //swequipe = (Switch) view.findViewById(R.id.swdivEquipe);
        matApoioReset1 = (EditText) view.findViewWithTag("matapoio1_1");
        matApoioReset2 = (EditText) view.findViewWithTag("matapoio1_2");
        matApoioReset3 = (EditText) view.findViewWithTag("matapoio1_3");
        matApoioReset4 = (EditText) view.findViewWithTag("matapoio1_4");

        swapoio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int childCount = ll.getChildCount();
                Integer procLinha1 = 2;
                TableLayout tab = (TableLayout) view.findViewById(R.id.tabela_os);
                for(int i = 0; i < childCount; i++) {
                    if (i == (procLinha1)) {
                        View childView1 = ll.getChildAt(i + 3);
                        View childView2 = ll.getChildAt(i + 4);
                        View childView3 = ll.getChildAt(i + 5);
                        View childView4 = ll.getChildAt(i + 6);

                        if(isChecked){
                            tab.getChildAt(i + 3).setVisibility(View.VISIBLE);
                            tab.getChildAt(i + 4).setVisibility(View.VISIBLE);
                            swapoio4.setChecked(false);
                        }else{
                            tab.getChildAt(i + 3).setVisibility(View.GONE);
                            tab.getChildAt(i + 4).setVisibility(View.GONE);
                            tab.getChildAt(i + 5).setVisibility(View.GONE);
                            tab.getChildAt(i + 6).setVisibility(View.GONE);

                            EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                            EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                            EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                            EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));

                            matApoio.setText("1120");
                            matApoio2.setText("1120");
                            matApoio3.setText("1120");
                            matApoio4.setText("1120");
                            swapoio4.setChecked(false);
                        }
                        procLinha1 += 7;
                    }
                }
            }
        });
        swapoio4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int childCount = ll.getChildCount();
                Integer procLinha1 = 2;
                TableLayout tab = (TableLayout) view.findViewById(R.id.tabela_os);
                for(int i = 0; i < childCount; i++) {
                    if (i == (procLinha1)) {

                        View childView3 = ll.getChildAt(i + 5);
                        View childView4 = ll.getChildAt(i + 6);

                        if(isChecked){
                            tab.getChildAt(i + 3).setVisibility(View.VISIBLE);
                            tab.getChildAt(i + 4).setVisibility(View.VISIBLE);
                            tab.getChildAt(i + 5).setVisibility(View.VISIBLE);
                            tab.getChildAt(i + 6).setVisibility(View.VISIBLE);

                            swapoio2.setChecked(true);
                            swapoio4.setChecked(true);
                        }else{

                            tab.getChildAt(i + 5).setVisibility(View.GONE);
                            tab.getChildAt(i + 6).setVisibility(View.GONE);

                            EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                            EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));

                            matApoio3.setText("1120");
                            matApoio4.setText("1120");
                            swapoio4.setChecked(false);
                        }
                        procLinha1 += 7;
                    }
                }
            }
        });

        ll = (TableLayout) view.findViewById(R.id.tabela_os);

        parcela2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    apontamentoParcela.setVisibility(View.VISIBLE);
                    String getParcela = parent.getSelectedItem().toString();
                    parcela.setText(getParcela);
                    parcelaNumero.setText("1");
                    String getAtividade = atividade.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, 0, 0);
                    //CARREGA OS DADOS DA PARCELA
                    if (inventario.size() > 0) {
                        edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                        edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                        edtPlantas.setText(inventario.get(0).get("plantas").toString());
                        edtHectares.setText(inventario.get(0).get("hectares").toString());
                        edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                        idParcela1 = (new Integer(inventario.get(0).get("idParcela")));
                        linhaInicial1 = new Integer(inventario.get(0).get("lInicial").toString());
                        linhaFinal1 = new Integer(inventario.get(0).get("lFinal").toString());
                        plantas1 = new Integer(inventario.get(0).get("plantas").toString());
                        hectar1 = new Double(inventario.get(0).get("hectares").toString());

                        Linhas1 = (linhaFinal1 - linhaInicial1);
                        divHa1 = hectar1;
                    }
                }else{
                    edtLinhaInicial.setText("0");
                    edtLinhaFinal.setText("0");
                    edtPlantas.setText("0");
                    edtHectares.setText("0.00");
                    parcela.setText("Parcela: -");
                    parcelaNumero.setText("1");

                    idParcela1 = 1;
                    linhaInicial1 = 0;
                    linhaFinal1 = 0;
                    plantas1 = 0;
                    hectar1 = 0.00;

                    Linhas1 = 0;
                    divHa1 = 0.00;
                }
                lin_in = new Integer(edtLinhaInicial.getText().toString());
                lin_fin = new Integer(edtLinhaFinal.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        parcela3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    apontamentoParcela.setVisibility(View.VISIBLE);
                    String getParcela = parent.getSelectedItem().toString();
                    parcela.setText(getParcela);
                    parcelaNumero.setText("2");
                    String getAtividade = atividade.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, 0, 0);
                    //CARREGA OS DADOS DA PARCELA
                    if (inventario.size() > 0) {
                        edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                        edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                        edtPlantas.setText(inventario.get(0).get("plantas").toString());
                        edtHectares.setText(inventario.get(0).get("hectares").toString());
                        edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                        idParcela2 = (new Integer(inventario.get(0).get("idParcela")));
                        linhaInicial2 = new Integer(inventario.get(0).get("lInicial").toString());
                        linhaFinal2 = new Integer(inventario.get(0).get("lFinal").toString());
                        plantas2 = new Integer(inventario.get(0).get("plantas").toString());
                        hectar2 = new Double(inventario.get(0).get("hectares").toString());

                        Linhas2 = (linhaFinal2 - linhaInicial2);
                        divHa2 = hectar2;
                    }
                }else{
                    edtLinhaInicial.setText("0");
                    edtLinhaFinal.setText("0");
                    edtPlantas.setText("0");
                    edtHectares.setText("0.00");
                    parcela.setText("Parcela: -");
                    parcelaNumero.setText("1");

                    idParcela2 = 1;
                    linhaInicial2 = 0;
                    linhaFinal2 = 0;
                    plantas2 = 0;
                    hectar2 = 0.00;

                    Linhas2 = (linhaFinal2 - linhaInicial2);
                    divHa2 = hectar2;
                }
                lin_in = new Integer(edtLinhaInicial.getText().toString());
                lin_fin = new Integer(edtLinhaFinal.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        parcela4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    apontamentoParcela.setVisibility(View.VISIBLE);
                    String getParcela = parent.getSelectedItem().toString();
                    parcela.setText(getParcela);
                    parcelaNumero.setText("3");
                    String getAtividade = atividade.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, 0, 0);
                    //CARREGA OS DADOS DA PARCELA
                    if (inventario.size() > 0) {
                        edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                        edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                        edtPlantas.setText(inventario.get(0).get("plantas").toString());
                        edtHectares.setText(inventario.get(0).get("hectares").toString());
                        edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                        idParcela3 = (new Integer(inventario.get(0).get("idParcela")));
                        linhaInicial3 = new Integer(inventario.get(0).get("lInicial").toString());
                        linhaFinal3 = new Integer(inventario.get(0).get("lFinal").toString());
                        plantas3 = new Integer(inventario.get(0).get("plantas").toString());
                        hectar3 = new Double(inventario.get(0).get("hectares").toString());

                        Linhas3 = (linhaFinal3 - linhaInicial3);
                        divHa3 = hectar3;
                    }
                }else{
                    edtLinhaInicial.setText("0");
                    edtLinhaFinal.setText("0");
                    edtPlantas.setText("0");
                    edtHectares.setText("0.00");
                    parcela.setText("Parcela: -");
                    parcelaNumero.setText("1");

                    idParcela3 = 1;
                    linhaInicial3 = 0;
                    linhaFinal3 = 0;
                    plantas3 = 0;
                    hectar3 = 0.00;

                    Linhas3 = (linhaFinal3 - linhaInicial3);
                    divHa3 = hectar3;
                }
                lin_in = new Integer(edtLinhaInicial.getText().toString());
                lin_fin = new Integer(edtLinhaFinal.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        parcela5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    apontamentoParcela.setVisibility(View.VISIBLE);
                    String getParcela = parent.getSelectedItem().toString();
                    parcela.setText(getParcela);
                    parcelaNumero.setText("4");
                    String getAtividade = atividade.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventario = BuscaParcela(GetSetCache.getFazendaGet(), getParcela, 0, 0);
                    //CARREGA OS DADOS DA PARCELA
                    if (inventario.size() > 0) {
                        edtLinhaInicial.setText(inventario.get(0).get("lInicial").toString());
                        edtLinhaFinal.setText(inventario.get(0).get("lFinal").toString());
                        edtPlantas.setText(inventario.get(0).get("plantas").toString());
                        edtHectares.setText(inventario.get(0).get("hectares").toString());
                        edtTotalPlantas.setText(inventario.get(0).get("plantas").toString());

                        idParcela4 = (new Integer(inventario.get(0).get("idParcela")));
                        linhaInicial4 = new Integer(inventario.get(0).get("lInicial").toString());
                        linhaFinal4 = new Integer(inventario.get(0).get("lFinal").toString());
                        plantas4 = new Integer(inventario.get(0).get("plantas").toString());
                        hectar4 = new Double(inventario.get(0).get("hectares").toString());

                        Linhas4 = (linhaFinal4 - linhaInicial4);
                        divHa4 = hectar4;
                    }
                }else{
                    edtLinhaInicial.setText("0");
                    edtLinhaFinal.setText("0");
                    edtPlantas.setText("0");
                    edtHectares.setText("0.00");
                    parcela.setText("Parcela: -");
                    parcelaNumero.setText("1");

                    idParcela4 = 1;
                    linhaInicial4 = 0;
                    linhaFinal4 = 0;
                    plantas4 = 0;
                    hectar4 = 0.00;
                }
                lin_in = new Integer(edtLinhaInicial.getText().toString());
                lin_fin = new Integer(edtLinhaFinal.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtLinhaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (edtLinhaInicial.getText().toString().length() > 0 && edtLinhaFinal.getText().toString().length() > 0) {
                        lin_in = new Integer(edtLinhaInicial.getText().toString());
                        lin_fin = new Integer(edtLinhaFinal.getText().toString());

                        String getParcela = parcela.getText().toString();
                        Integer getParcelaNum = new Integer(parcelaNumero.getText().toString());
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

                            switch (getParcelaNum){
                                case 1:
                                    idParcela1 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial1 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal1 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas1 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar1 = new Double(edtHectares.getText().toString());

                                    Linhas1 = (linhaFinal1 - linhaInicial1);
                                    divHa1 = area_calculada;
                                break;
                                case 2:
                                    idParcela2 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial2 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal2 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas2 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar2 = new Double(edtHectares.getText().toString());

                                    Linhas2 = (linhaFinal2 - linhaInicial2);
                                    divHa2 = area_calculada;
                                break;
                                case 3:
                                    idParcela3 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial3 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal3 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas3 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar3 = new Double(edtHectares.getText().toString());

                                    Linhas3 = (linhaFinal3 - linhaInicial3);
                                    divHa3 = area_calculada;
                                break;
                                case 4:
                                    idParcela4 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial4 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal4 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas4 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar4 = new Double(edtHectares.getText().toString());

                                    Linhas4 = (linhaFinal4 - linhaInicial4);
                                    divHa4 = area_calculada;
                                break;
                            }
                        }
                    }else{
                        edtLinhaInicial.setText(lin_in.toString());
                        edtLinhaFinal.setText(lin_fin.toString());
                    }
                    //edtLinhaFinal.requestFocus();
                }
            }
        });
        edtLinhaFinal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if (edtLinhaFinal.getText().toString().length() > 0 && edtLinhaInicial.getText().toString().length() > 0) {
                        lin_in = new Integer(edtLinhaInicial.getText().toString());
                        lin_fin = new Integer(edtLinhaFinal.getText().toString());

                        String getParcela = parcela.getText().toString();
                        Integer getParcelaNum = new Integer(parcelaNumero.getText().toString());
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

                            switch (getParcelaNum){
                                case 1:
                                    idParcela1 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial1 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal1 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas1 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar1 = new Double(inventario.get(0).get("hectares").toString());

                                    Linhas1 = (linhaFinal1 - linhaInicial1);
                                    divHa1 = area_calculada;
                                    break;
                                case 2:
                                    idParcela2 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial2 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal2 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas2 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar2 = new Double(inventario.get(0).get("hectares").toString());

                                    Linhas2 = (linhaFinal2 - linhaInicial2);
                                    divHa2 = area_calculada;
                                    break;
                                case 3:
                                    idParcela3 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial3 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal3 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas3 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar3 = new Double(inventario.get(0).get("hectares").toString());

                                    Linhas3 = (linhaFinal3 - linhaInicial3);
                                    divHa3 = area_calculada;
                                    break;
                                case 4:
                                    idParcela4 = (new Integer(inventario.get(0).get("idParcela")));
                                    linhaInicial4 = new Integer(inventario.get(0).get("lInicial").toString());
                                    linhaFinal4 = new Integer(inventario.get(0).get("lFinal").toString());
                                    plantas4 = new Integer(inventario.get(0).get("plantas").toString());
                                    hectar4 = new Double(inventario.get(0).get("hectares").toString());

                                    Linhas4 = (linhaFinal4 - linhaInicial4);
                                    divHa4 = area_calculada;
                                    break;
                            }
                        }
                    }else{
                        edtLinhaInicial.setText(lin_in.toString());
                        edtLinhaFinal.setText(lin_fin.toString());
                    }
                }
            }
        });

        Button cancelar = (Button) view.findViewById(R.id.cancelar);
        addMDO = (ImageButton) view.findViewById(R.id.addLinha);
        upd = (ImageButton) view.findViewById(R.id.updLinha);
        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        addMDO.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    addLinha();
                }else{
                    addLinha();
                }
                addLinhaMaquinario();
                addLinhaImplemento();

                addLinhaApoio1();
                addLinhaApoio2();
                addLinhaApoio3();
                addLinhaApoio4();
            }
        });
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updt();
            }
        });

        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer atividadeExt = atividade.getSelectedItemPosition();
                String atividadeGet = atividade.getSelectedItem().toString();
                if(atividadeExt > 0 && parcela2.getSelectedItemPosition() > 0 && !atividadeGet.equals("SELECIONE UMA ATIVIDADE")) {
                    int funcsalvar = salvar();
                    updt();
                    if(funcsalvar > 0){
                        exemplo_alerta("Salvando...","Apontamentos salvos");
                    }else{
                        alerta("Erro","Nenhum apontamento salvo");
                    }
                }else if(atividadeExt > 0 && !atividadeGet.equals("SELECIONE UMA ATIVIDADE") && parcela2.getSelectedItemPosition()==0){
                    int funcsalvar = salvarOAtividades();
                    if(funcsalvar > 0){
                        exemplo_alerta("Salvando...","Apontamentos salvos");
                    }else{
                        alerta("Erro","Nenhum apontamento salvo");
                    }
                }
                else{
                    alerta("Atividade","Selecione uma atividade");
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
        //parcela1.setAdapter(dataAdapter);
        parcela2.setAdapter(dataAdapter);
        parcela3.setAdapter(dataAdapter);
        parcela4.setAdapter(dataAdapter);
        parcela5.setAdapter(dataAdapter);
        //parcela6.setAdapter(dataAdapter);
        //parcela7.setAdapter(dataAdapter);

        List<String> patrimonios = db.SelectPatrimonio("'TRATOR'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> implementos = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, implementos);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        implemento.setAdapter(dataAdapter3);

        List<String> atividades = db.SelectAtividade(getSetCache.getAtividade());
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, atividades);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            //getActivity().finish();
            parcela2.post(new Runnable() {
                @Override
                public void run() {
                    parcela2.setSelection(0);
                    //atividade.setSelection(GetSetCache.getSetSpinnerAtividade());
                }
            });
            parcela3.post(new Runnable() {
                @Override
                public void run() {
                    parcela3.setSelection(0);
                    //atividade.setSelection(GetSetCache.getSetSpinnerAtividade());
                }
            });
            parcela4.post(new Runnable() {
                @Override
                public void run() {
                    parcela4.setSelection(0);
                    //atividade.setSelection(GetSetCache.getSetSpinnerAtividade());
                }
            });
            parcela5.post(new Runnable() {
                @Override
                public void run() {
                    parcela5.setSelection(0);
                    //atividade.setSelection(GetSetCache.getSetSpinnerAtividade());
                }
            });
                limparCampos();
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addLinha(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        row.setBackgroundResource(R.drawable.borderoperador);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);
        Integer contCod = (linhaRowTable / 7)+1;

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);
        edtN.setText(contCod.toString());
        edtN.setId(linhaRowTable+1);
        edtN.setTag("edt1n");
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("P1");
        edtParc1.setTag("edt1parc1");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("P2");
        edtParc2.setTag("edt1parc2");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("P3");
        edtParc3.setTag("edt1parc3");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("P4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("edt1parc4");

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("edt1total");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        edt();

        row.addView(edtN);
        row.addView(edtMatricula);
        row.addView(edtParc1);
        row.addView(edtParc2);
        row.addView(edtParc3);
        row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaMaquinario(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(true);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setText("0.00");
        //edtN.setLayoutParams(params);
        edtN.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtN.setGravity(Gravity.CENTER);
        edtN.setHint("hr Inicial");
        edtN.setTag("edithrInicial");
        edtN.setVisibility(View.INVISIBLE);
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Spinner edtPatrimonio = new Spinner(getContext());
        edtPatrimonio.setGravity(Gravity.CENTER);
        edtPatrimonio.setId(linhaRowTable+3);
        edtPatrimonio.setLayoutParams(params);
        edtPatrimonio.setTag("spinner_patrimonio");
        //edtPatrimonio.setLayoutParams(params);

        Database_syspalma db = new Database_syspalma(getContext());
        List<String> patrimonios = db.SelectPatrimonio("'TRATOR'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtPatrimonio.setAdapter(dataAdapter2);

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("hr Inicial");
        edtParc1.setTag("edthr1");
        //edtParc1.setText("0.00");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("hr Final");
        edtParc2.setTag("edthr2");
        //edtParc2.setText("0.00");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        /*
        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("hr3");
        edtParc3.setTag("edthr3");
        //edtParc3.setText("0.00");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("hr4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("edthr4");
        //edtParc4.setText("0.00");
        */

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("edthrtotal");
        edtTotal.setHint("0.00");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        //edt();

        row.addView(edtN);
        row.addView(edtPatrimonio);
        row.addView(edtParc1);
        row.addView(edtParc2);
        //row.addView(edtParc3);
        //row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaImplemento(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);

        Spinner edtImplemento = new Spinner(getContext());
        edtImplemento.setId(linhaRowTable+3);
        //edtImplemento.setLayoutParams(params);
        edtImplemento.setTag("spinner_implemento");

        //CARREGA OS DADOS PARA O SPINNER CRIADO
        Database_syspalma db = new Database_syspalma(getContext());
        List<String> patrimonios = db.SelectPatrimonio("'IMPLEMENTO'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtImplemento.setAdapter(dataAdapter2);

        row.addView(edtN);
        row.addView(edtImplemento);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaApoio1(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);
        row.setTag("idlinhaApoio1");
        if(swapoio2.isChecked()){
            row.setVisibility(View.VISIBLE);
        }else {
            row.setVisibility(View.GONE);
        }

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);
        edtN.setText("1");
        edtN.setId(linhaRowTable+1);
        edtN.setTag("edtapoio1");
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtMatricula = new EditText(getContext());
        edtMatricula.setEms(5);
        edtMatricula.setInputType(1);
        edtMatricula.setGravity(Gravity.CENTER);
        //edtMatricula.setLayoutParams(params);
        edtMatricula.setTextColor(ContextCompat.getColor(getContext(), R.color.vermelho));
        edtMatricula.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtMatricula.setText("1120");
        edtMatricula.requestFocus(4);
        edtMatricula.setId(linhaRowTable+2);
        edtMatricula.setTag("matapoio1_1");
        //edtMatricula.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("P1");
        edtParc1.setTag("apoioparc1_1");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("P2");
        edtParc2.setTag("apoioparc2_1");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("P3");
        edtParc3.setTag("apoioparc3_1");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("P4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("apoioparc4_1");

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("apoiototal1_1");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        edt();

        row.addView(edtN);
        row.addView(edtMatricula);
        row.addView(edtParc1);
        row.addView(edtParc2);
        row.addView(edtParc3);
        row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaApoio2(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);
        row.setTag("idlinhaApoio2");
        if(swapoio2.isChecked()){
            row.setVisibility(View.VISIBLE);
        }else {
            row.setVisibility(View.GONE);
        }

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);
        edtN.setText("2");
        edtN.setId(linhaRowTable+1);
        edtN.setTag("edtapoio2");
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtMatricula = new EditText(getContext());
        edtMatricula.setEms(5);
        edtMatricula.setInputType(1);
        edtMatricula.setGravity(Gravity.CENTER);
        //edtMatricula.setLayoutParams(params);
        edtMatricula.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtMatricula.setText("1120");
        edtMatricula.requestFocus(4);
        edtMatricula.setId(linhaRowTable+2);
        edtMatricula.setTextColor(ContextCompat.getColor(getContext(), R.color.vermelho));
        edtMatricula.setTag("matapoio1_2");
        //edtMatricula.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("P1");
        edtParc1.setTag("apoioparc1_2");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("P2");
        edtParc2.setTag("apoioparc2_2");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("P3");
        edtParc3.setTag("apoioparc3_2");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("P4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("apoioparc4_2");

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("apoioparc2_2");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        edt();

        row.addView(edtN);
        row.addView(edtMatricula);
        row.addView(edtParc1);
        row.addView(edtParc2);
        row.addView(edtParc3);
        row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaApoio3(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);
        row.setTag("idlinhaApoio3");
        if(swapoio4.isChecked()){
            row.setVisibility(View.VISIBLE);
        }else {
            row.setVisibility(View.GONE);
        }

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);
        edtN.setText("3");
        edtN.setId(linhaRowTable+1);
        edtN.setTag("edtapoio3");
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtMatricula = new EditText(getContext());
        edtMatricula.setEms(5);
        edtMatricula.setInputType(1);
        edtMatricula.setGravity(Gravity.CENTER);
        //edtMatricula.setLayoutParams(params);
        edtMatricula.setTextColor(ContextCompat.getColor(getContext(), R.color.vermelho));
        edtMatricula.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtMatricula.setText("1120");
        edtMatricula.requestFocus(4);
        edtMatricula.setId(linhaRowTable+2);
        edtMatricula.setTag("matapoio1_3");
        //edtMatricula.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("P1");
        edtParc1.setTag("apoioparc1_3");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("P2");
        edtParc2.setTag("apoioparc2_3");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("P3");
        edtParc3.setTag("apoioparc3_3");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("P4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("apoioparc4_3");

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("apoiototal1_3");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        edt();

        row.addView(edtN);
        row.addView(edtMatricula);
        row.addView(edtParc1);
        row.addView(edtParc2);
        row.addView(edtParc3);
        row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void addLinhaApoio4(){
        linhaRowTable = ll.getChildCount();
        TableRow row= new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(15,0,0,0);
        row.setTag("idlinhaApoio4");
        if(swapoio4.isChecked()){
            row.setVisibility(View.VISIBLE);
        }else {
            row.setVisibility(View.GONE);
        }

        EditText edtN = new EditText(getContext());
        edtN.setEnabled(false);
        edtN.setEms(2);
        edtN.setInputType(1);
        //edtN.setLayoutParams(params);
        edtN.setGravity(Gravity.CENTER);
        edtN.setText("4");
        edtN.setId(linhaRowTable+1);
        edtN.setTag("edtapoio4");
        //edtN.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtMatricula = new EditText(getContext());
        edtMatricula.setEms(5);
        edtMatricula.setInputType(1);
        edtMatricula.setGravity(Gravity.CENTER);
        //edtMatricula.setLayoutParams(params);
        edtMatricula.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtMatricula.setText("1120");
        edtMatricula.requestFocus(4);
        edtMatricula.setId(linhaRowTable+2);
        edtMatricula.setTextColor(ContextCompat.getColor(getContext(), R.color.vermelho));
        edtMatricula.setTag("matapoio1_4");
        //edtMatricula.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc1 = new EditText(getContext());
        edtParc1.setEms(2);
        edtParc1.setGravity(Gravity.CENTER);
        edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc1.setId(linhaRowTable+3);
        edtParc1.setLayoutParams(params);
        edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc1.setHint("P1");
        edtParc1.setTag("apoioparc1_4");

        EditText edtParc2 = new EditText(getContext());
        edtParc2.setEms(2);
        edtParc2.setGravity(Gravity.CENTER);
        edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc2.setId(linhaRowTable+4);
        edtParc2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc2.setLayoutParams(params);
        edtParc2.setHint("P2");
        edtParc2.setTag("apoioparc2_4");
        //edtParc2.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        //edtParc2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText edtParc3 = new EditText(getContext());
        edtParc3.setEms(2);
        edtParc3.setGravity(Gravity.CENTER);
        edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc3.setId(linhaRowTable+5);
        edtParc3.setLayoutParams(params);
        edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc3.setHint("P3");
        edtParc3.setTag("apoioparc3_4");

        EditText edtParc4 = new EditText(getContext());
        edtParc4.setEms(2);
        edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtParc4.setGravity(Gravity.CENTER);
        edtParc4.setLayoutParams(params);
        edtParc4.setId(linhaRowTable+6);
        edtParc4.setHint("P4");
        edtParc4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        edtParc4.setTag("apoioparc4_4");

        EditText edtTotal = new EditText(getContext());
        edtTotal.setEnabled(false);
        edtTotal.setEms(5);
        edtTotal.setGravity(Gravity.CENTER);
        edtTotal.setTypeface(null, Typeface.BOLD_ITALIC);
        edtTotal.setInputType(1);
        edtTotal.setId(linhaRowTable+7);
        edtTotal.setLayoutParams(params);
        edtTotal.setTag("apoioparc2_4");
        //edtTotal.setTextSt

        //CHAMA A FUNÇÃO PARA EXIBIR AS LINAHS DE TESTES
        edt();

        row.addView(edtN);
        row.addView(edtMatricula);
        row.addView(edtParc1);
        row.addView(edtParc2);
        row.addView(edtParc3);
        row.addView(edtParc4);
        row.addView(edtTotal);
        ll.addView(row,linhaRowTable);
    }
    public void edt(){
        int childCount = ll.getChildCount();
        Integer getpar1 = parcela2.getSelectedItemPosition();
        Integer getpar2 = parcela3.getSelectedItemPosition();
        Integer getpar3 = parcela4.getSelectedItemPosition();
        Integer getpar4 = parcela5.getSelectedItemPosition();
        Integer ContadorParcMDO1 = 0, ContadorParcMDO2 = 0,ContadorParcMDO3 = 0, ContadorParcMDO4 = 0;

        Double TotalProd = 0.00, TotalProdGeral = 0.00, TotalProdGeral1 = 0.00, TotalProdGeral2 = 0.00, TotalProdGeral3 = 0.00, TotalProdGeral4 = 0.00;
        Double TotalProd1 = 0.00;
        Double TotalProd2 = 0.00;
        Double TotalProd3 = 0.00;
        Double TotalProd4 = 0.00;

        DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);

        //VERIFICA SE A ATIVIDADE NÃO FOR COLHEITA ADICIONA O CAMPO DA PRODUÇÃO EM MÉDIA DE PLANTAS PELA EQUIPE
        if(getSetCache.getOperacao().equals("Colheita")) {
            Integer procLinha1 = 2;
            for(int i = 0; i < childCount; i++){
                if(i==(procLinha1)) {
                    View childView = ll.getChildAt(i);

                    EditText Total = (EditText) (childView.findViewWithTag("edt1total"));

                    EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
                    String glinha = linha.getText().toString();

                    EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));
                    String gmatricula = (matricula.getText().toString());

                    //recupera os dados das 4 parcelas
                    EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc1"));
                    String gparcProd1 = (parcProd1.getText().toString());

                    EditText parcProd2 = (EditText) (childView.findViewWithTag("edt1parc2"));
                    String gparcProd2 = (parcProd2.getText().toString());

                    EditText parcProd3 = (EditText) (childView.findViewWithTag("edt1parc3"));
                    String gparcProd3 = (parcProd3.getText().toString());

                    EditText parcProd4 = (EditText) (childView.findViewWithTag("edt1parc4"));
                    String gparcProd4 = (parcProd4.getText().toString());

                    if (!gparcProd1.equals("") && getpar1 > 1) {
                        ContadorParcMDO1++;
                        TotalProd1 = new Double(gparcProd1);
                        TotalProdGeral1 += TotalProd1;
                    }
                    if (!gparcProd2.equals("") && getpar2 > 1) {
                        ContadorParcMDO2++;
                        TotalProd2 = new Double(gparcProd2);
                        TotalProdGeral2 += TotalProd2;
                    }
                    if (!gparcProd3.equals("") && getpar3 > 1) {
                        ContadorParcMDO3++;
                        TotalProd3 = new Double(gparcProd3);
                        TotalProdGeral3 += TotalProd3;
                    }
                    if (!gparcProd4.equals("") && getpar4 > 1) {
                        ContadorParcMDO4++;
                        TotalProd4 = new Double(gparcProd4);
                        TotalProdGeral4 += TotalProd4;
                    }
                    TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                    Total.setText(TotalProd.toString());
                    TotalProdGeral += TotalProd;
                    procLinha1 += 7;
                }
            }
            if (ContadorParcMDO1 > 0) {
                Integer linhasCalc1 = ContadorParcMDO1;
                Integer Linhas = (linhaFinal1 - linhaInicial1) / linhasCalc1;
                Double divHa = hectar1 / linhasCalc1;
                Integer divplantas = plantas1 / linhasCalc1;
            }

            if (ContadorParcMDO2 > 0) {
                Integer linhasCalc2 = ContadorParcMDO2;
                Integer Linhas = (linhaFinal2 - linhaInicial2) / linhasCalc2;
                Double divHa = hectar2 / linhasCalc2;
                Integer divplantas = plantas2 / linhasCalc2;
            }

            if (ContadorParcMDO3 > 0) {
                Integer linhasCalc3 = ContadorParcMDO3;
                Integer Linhas = (linhaFinal3 - linhaInicial3) / linhasCalc3;
                Double divHa = hectar3 / linhasCalc3;
                Integer divplantas = plantas3 / linhasCalc3;
            }

            if (ContadorParcMDO4 > 0) {
                Integer linhasCalc4 = ContadorParcMDO4;
                Integer Linhas = (linhaFinal4 - linhaInicial4) / linhasCalc4;
                Double divHa = hectar4 / linhasCalc4;
                Integer divplantas = plantas4 / linhasCalc4;
            }
        }else{
            if (plantas1 > 0) {
                Integer linhasCalc1 = (childCount / 5)+1;
                Integer Linhas = (linhaFinal1 - linhaInicial1) / linhasCalc1;
                Double divHa = hectar1 / linhasCalc1;
                Integer restoPlantas = plantas1 % linhasCalc1;
                Integer divplantas = (plantas1 / linhasCalc1);

                Integer procLinha1 = 2;
                //PERCORRE TODAS AS LINHAS
                for(int j = 0; j < childCount; j++) {
                    if(j==(procLinha1)) {
                        View childView = ll.getChildAt(j);
                        View childView2 = ll.getChildAt(j+1);
                        View childView3 = ll.getChildAt(j+2);
                        EditText calcParcProd1 = (EditText)(childView.findViewWithTag("edt1parc1"));
                        //EditText calcParcProd2 = (EditText)(childView2.findViewWithTag("apoioparc1_1"));
                        //EditText calcParcProd3 = (EditText)(childView3.findViewWithTag("apoioparc1_2"));

                        if(j == (childCount-1)){
                            //VERIFICA SE É IMPA
                            if((plantas1 % linhasCalc1) != 0){
                                divplantas = divplantas + restoPlantas;
                                calcParcProd1.setText(divplantas.toString());
                                //calcParcProd2.setText(divplantas.toString());
                                //calcParcProd3.setText(divplantas.toString());
                            }else{
                                calcParcProd1.setText(divplantas.toString());
                                //calcParcProd2.setText(divplantas.toString());
                                //calcParcProd3.setText(divplantas.toString());
                            }
                        }else{
                            calcParcProd1.setText(divplantas.toString());
                            //calcParcProd2.setText(divplantas.toString());
                            //calcParcProd3.setText(divplantas.toString());
                        }

                        calcParcProd1.setText(divplantas.toString());
                        //calcParcProd2.setText(divplantas.toString());
                        //calcParcProd3.setText(divplantas.toString());

                        EditText Total = (EditText)(childView.findViewWithTag("edt1parc1"));

                        TotalProd1 = new Double(Total.getText().toString());
                        TotalProdGeral1 += TotalProd1;

                        EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                        TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                        TotalParcelas.setText(TotalProd.toString());
                        TotalProdGeral += TotalProd;

                        procLinha1 += 7;
                    }
                }
            }
            if (plantas2 > 0) {
                Integer linhasCalc2 = (childCount / 5)+1;
                Integer Linhas = (linhaFinal2 - linhaInicial2) / linhasCalc2;
                Double divHa = hectar2 / linhasCalc2;
                Integer restoPlantas = plantas2 % linhasCalc2;
                Integer divplantas = (plantas2 / linhasCalc2) - restoPlantas;

                Integer procLinha1 = 2;
                //PERCORRE TODAS AS LINHAS
                for(int j = 0; j < childCount; j++) {
                    if(j==(procLinha1)) {
                        View childView = ll.getChildAt(j);
                        EditText calcParcProd1 = (EditText)(childView.findViewWithTag("edt1parc2"));

                        if(j == (childCount-1)){
                            //VERIFICA SE É IMPA
                            if((plantas2 % linhasCalc2) != 0){
                                divplantas = divplantas + restoPlantas;
                                calcParcProd1.setText(divplantas.toString());
                            }else{
                                calcParcProd1.setText(divplantas.toString());
                            }
                        }else{
                            calcParcProd1.setText(divplantas.toString());
                        }

                        EditText Total = (EditText)(childView.findViewWithTag("edt1parc2"));
                        TotalProd2 = new Double(Total.getText().toString());
                        TotalProdGeral2 += TotalProd2;


                        EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                        TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                        TotalParcelas.setText(TotalProd.toString());
                        TotalProdGeral += TotalProd;
                        procLinha1 += 7;
                    }
                }
            }

            if (plantas3 > 0) {
                Integer linhasCalc3 = (childCount / 5)+1;
                Integer Linhas = (linhaFinal3 - linhaInicial3) / linhasCalc3;
                Double divHa = hectar3 / linhasCalc3;
                Integer restoPlantas = plantas3 % linhasCalc3;
                Integer divplantas = (plantas3 / linhasCalc3) - restoPlantas;

                Integer procLinha1 = 2;
                //PERCORRE TODAS AS LINHAS
                for(int j = 0; j < childCount; j++) {
                    if(j==(procLinha1)) {
                        View childView = ll.getChildAt(j);
                        EditText calcParcProd1 = (EditText)(childView.findViewWithTag("edt1parc3"));

                        if(j == (childCount-1)){
                            //VERIFICA SE É IMPA
                            if((plantas3 % linhasCalc3) != 0){
                                divplantas = divplantas + restoPlantas;
                                calcParcProd1.setText(divplantas.toString());
                            }else{
                                calcParcProd1.setText(divplantas.toString());
                            }
                        }else{
                            calcParcProd1.setText(divplantas.toString());
                        }

                        EditText Total = (EditText)(childView.findViewWithTag("edt1parc3"));
                        TotalProd3 = new Double(Total.getText().toString());
                        TotalProdGeral3 += TotalProd3;


                        EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                        TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                        TotalParcelas.setText(TotalProd.toString());
                        TotalProdGeral += TotalProd;
                        procLinha1 += 7;
                    }
                }
            }

            if (plantas4 > 0) {
                Integer linhasCalc4 = (childCount / 5)+1;
                Integer Linhas = (linhaFinal4 - linhaInicial4) / linhasCalc4;
                Double divHa = hectar4 / linhasCalc4;
                Integer restoPlantas = plantas4 % linhasCalc4;
                Integer divplantas = (plantas4 / linhasCalc4) - restoPlantas;

                Integer procLinha1 = 2;
                //PERCORRE TODAS AS LINHAS
                for(int j = 0; j < childCount; j++) {
                    if(j==(procLinha1)) {
                        View childView = ll.getChildAt(j);
                        EditText calcParcProd1 = (EditText)(childView.findViewWithTag("edt1parc4"));

                        if(j == (childCount-1)){
                            //VERIFICA SE É IMPA
                            if((plantas3 % linhasCalc4) != 0){
                                divplantas = divplantas + restoPlantas;
                                calcParcProd1.setText(divplantas.toString());
                            }else{
                                calcParcProd1.setText(divplantas.toString());
                            }
                        }else{
                            calcParcProd1.setText(divplantas.toString());
                        }

                        EditText Total = (EditText)(childView.findViewWithTag("edt1parc4"));
                        TotalProd4 = new Double(Total.getText().toString());
                        TotalProdGeral4 += TotalProd4;


                        EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                        TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                        TotalParcelas.setText(TotalProd.toString());
                        TotalProdGeral += TotalProd;
                        procLinha1 += 7;
                    }
                }
            }

        }

        //CRIAR OS TOTAIS NO RODAPÉ
        totalparc1.setText(TotalProdGeral1.toString());
        totalparc2.setText(TotalProdGeral2.toString());
        totalparc3.setText(TotalProdGeral3.toString());
        totalparc4.setText(TotalProdGeral4.toString());
        totalGeral.setText(TotalProdGeral.toString());
    }
    public void updt(){
        Database_syspalma database = new Database_syspalma(getContext());

        int childCount = ll.getChildCount();
        Integer getpar1 = parcela2.getSelectedItemPosition();
        Integer getpar2 = parcela3.getSelectedItemPosition();
        Integer getpar3 = parcela4.getSelectedItemPosition();
        Integer getpar4 = parcela5.getSelectedItemPosition();
        Integer ContadorParcMDO1 = 0, ContadorParcMDO2 = 0,ContadorParcMDO3 = 0, ContadorParcMDO4 = 0;

        Double TotalProdGeral = 0.00, TotalProdGeral1 = 0.00, TotalProdGeral2 = 0.00, TotalProdGeral3 = 0.00, TotalProdGeral4 = 0.00;
        Double TotalProd1 = 0.00;
        Double TotalProd2 = 0.00;
        Double TotalProd3 = 0.00;
        Double TotalProd4 = 0.00;

        DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
        Integer procLinha1 = 2;
        //PERCORRE TODAS AS LINHAS
        for (int i = 0; i < childCount; i++) {
            if (i == (procLinha1)) {
                View childView = ll.getChildAt(i);
                View childViewPat = ll.getChildAt(i+1);

                EditText Total = (EditText) (childView.findViewWithTag("edt1total"));
                EditText horInicial = (EditText) (childViewPat.findViewWithTag("edthr1"));
                EditText horFinal = (EditText) (childViewPat.findViewWithTag("edthr2"));
                EditText horTotal = (EditText) (childViewPat.findViewWithTag("edthrtotal"));

                Double hrmFim = 0.00;
                Double hrmIni = 0.00;
                Double totalHrm = 0.00;
                if(horInicial.getText().length() > 0 || horFinal.getText().length() > 0){
                    if(horInicial.getText().toString().length() > 0 && horFinal.getText().toString().length() > 0){
                        hrmIni = new Double(horInicial.getText().toString());
                        hrmFim = new Double(horFinal.getText().toString());
                    }
                }
                totalHrm = hrmFim - hrmIni;
                if(totalHrm >= 0 && totalHrm < 10){
                    horTotal.setText(totalHrm.toString());
                }else{
                    alerta("Maquinário","Verifique o horímetro ("+totalHrm+" horas trabalhadas)");
                    horInicial.setText("");
                    horFinal.setText("");
                    horTotal.setText("");
                    horInicial.requestFocus();
                }

                View childView1 = ll.getChildAt(i+3);
                View childView2 = ll.getChildAt(i+4);
                View childView3 = ll.getChildAt(i+5);
                View childView4 = ll.getChildAt(i+6);

                EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
                EditText linha1 = (EditText) (childView1.findViewWithTag("edtapoio1"));
                EditText linha2 = (EditText) (childView2.findViewWithTag("edtapoio2"));
                EditText linha3 = (EditText) (childView3.findViewWithTag("edtapoio3"));
                EditText linha4 = (EditText) (childView4.findViewWithTag("edtapoio4"));

                EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));

                EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));

                String matriculaget = database.RetoronoIdMat(matricula.getText().toString());
                String matriculaget1 = database.RetoronoIdMat(matApoio.getText().toString());
                String matriculaget2 = database.RetoronoIdMat(matApoio2.getText().toString());
                String matriculaget3 = database.RetoronoIdMat(matApoio3.getText().toString());
                String matriculaget4 = database.RetoronoIdMat(matApoio4.getText().toString());

                //VERIFICA AS MATRICULAS
                if(matriculaget == null){
                    linha.setBackgroundResource(R.color.vermelho);
                    linha.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
                }else{
                    linha.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget, Toast.LENGTH_SHORT).show();
                    linha.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                }

                if(matriculaget1 == null){
                    linha1.setBackgroundResource(R.color.vermelho);
                    linha1.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
                }else{
                    linha1.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget1, Toast.LENGTH_SHORT).show();
                    linha1.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                }
                if(matriculaget2 == null){
                    linha2.setBackgroundResource(R.color.vermelho);
                    linha2.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
                }else{
                    linha2.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget2, Toast.LENGTH_SHORT).show();
                    linha2.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                }
                if(matriculaget3 == null){
                    linha3.setBackgroundResource(R.color.vermelho);
                    linha3.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
                }else{
                    linha3.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget3, Toast.LENGTH_SHORT).show();
                    linha3.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                }
                if(matriculaget4 == null){
                    linha4.setBackgroundResource(R.color.vermelho);
                    linha4.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
                }else{
                    linha4.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget4, Toast.LENGTH_SHORT).show();
                    linha4.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                }

                //recupera os dados das 4 parcelas
                EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc1"));
                String gparcProd1 = (parcProd1.getText().toString());

                EditText parcProd2 = (EditText) (childView.findViewWithTag("edt1parc2"));
                String gparcProd2 = (parcProd2.getText().toString());

                EditText parcProd3 = (EditText) (childView.findViewWithTag("edt1parc3"));
                String gparcProd3 = (parcProd3.getText().toString());

                EditText parcProd4 = (EditText) (childView.findViewWithTag("edt1parc4"));
                String gparcProd4 = (parcProd4.getText().toString());

                Double valoresParc = 0.0;

                if(atividade.getSelectedItem().equals("CAIXEIRO")) {
                    parcela2.setSelection(2);
                    String parcelaView = parcela2.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
                    //idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));

                    idParcela1 = (new Integer(inventarioGetParc.get(0).get("idParcela")));
                    linhaInicial1 = new Integer(inventarioGetParc.get(0).get("lInicial").toString());
                    linhaFinal1 = new Integer(inventarioGetParc.get(0).get("lFinal").toString());
                    plantas1 = new Integer(inventarioGetParc.get(0).get("plantas").toString());
                    hectar1 = new Double(inventarioGetParc.get(0).get("hectares").toString());

                    String media_equipe = database.RetoronoCaixeiro(GetSetCache.getFicha()).toString();
                    parcProd1.setText(media_equipe);
                    gparcProd1 = media_equipe;
                }

                if (gparcProd1.length() > 0 && getpar1 > 0) {
                    ContadorParcMDO1++;
                    valoresParc += new Double(gparcProd1);
                }
                if (gparcProd2.length() > 0 && getpar2 > 0) {
                    ContadorParcMDO2++;
                    valoresParc += new Double(gparcProd2);
                }
                if (gparcProd3.length() > 0 && getpar3 > 0) {
                    ContadorParcMDO3++;
                    valoresParc += new Double(gparcProd3);
                }
                if (gparcProd4.length() > 0 && getpar4 > 0) {
                    ContadorParcMDO4++;
                    valoresParc += new Double(gparcProd4);
                }
                Total.setText(valoresParc.toString());
                procLinha1 += 7;
            }
        }

        if (ContadorParcMDO1 > 0) {
            final Integer divplantas;
            Integer linhasCalc1 = ContadorParcMDO1;
            Integer Linhas = (linhaFinal1 - linhaInicial1) / linhasCalc1;
            Double divHa = hectar1 / linhasCalc1;
            divplantas = plantas1 / linhasCalc1;

            procLinha1 = 2;
            for(int j = 0; j < childCount; j++) {
                if (j == (procLinha1)) {
                    View childView = ll.getChildAt(j);
                    View childView1 = ll.getChildAt(j+3);
                    View childView2 = ll.getChildAt(j+4);
                    View childView3 = ll.getChildAt(j+5);
                    View childView4 = ll.getChildAt(j+6);

                    //recupera os dados da 1 parcelas
                    EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc1"));
                    String gparcProd1 = (parcProd1.getText().toString());
                    EditText Total = (EditText) (childView.findViewWithTag("edt1total"));

                    EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                    EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                    EditText parcProdApoio1 = (EditText) (childView1.findViewWithTag("apoioparc1_1"));
                    EditText parcProdApoio2 = (EditText) (childView2.findViewWithTag("apoioparc1_2"));

                    EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                    EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                    EditText parcProdApoio3 = (EditText) (childView3.findViewWithTag("apoioparc1_3"));
                    EditText parcProdApoio4 = (EditText) (childView4.findViewWithTag("apoioparc1_4"));

                    if (getpar1 > 0) {
                        //VERIFICA O TIPO DA PRODUÇÃO PARA ADICIONAR
                        String produc;
                        if(GetSetCache.operacao.equals("Colheita")){
                            //CACHOS
                            produc = parcProd1.getText().toString();
                        }else if(!gparcProd1.equals("")){
                            //PLANTAS
                            produc = divplantas.toString();
                        }else{
                            produc = null;
                        }
                        parcProd1.setText(produc);
                        if(matApoio.getText().length()>4){
                            parcProdApoio1.setText(produc);
                        }
                        if(matApoio2.getText().length()>4){
                            parcProdApoio2.setText(produc);
                        }
                        if(matApoio3.getText().length()>4){
                            parcProdApoio3.setText(produc);
                        }
                        if(matApoio4.getText().length()>4){
                            parcProdApoio4.setText(produc);
                        }
                    }
                    if(parcProd1.length()>0){
                        TotalProd1 = new Double(parcProd1.getText().toString());
                    }else{
                        TotalProd1 = 0.00;
                    }

                    TotalProdGeral1 += TotalProd1;
                    TotalProdGeral += TotalProd1;
                    procLinha1 += 7;
                }
            }
        }

        if (ContadorParcMDO2 > 0) {
            final Integer divplantas;
            Integer linhasCalc1 = ContadorParcMDO2;
            Integer Linhas = (linhaFinal2 - linhaInicial2) / linhasCalc1;
            Double divHa = hectar2 / linhasCalc1;
            divplantas = plantas2 / linhasCalc1;

            procLinha1 = 2;
            for(int j = 0; j < childCount; j++) {
                if (j == (procLinha1)) {
                    View childView = ll.getChildAt(j);
                    View childView1 = ll.getChildAt(j+3);
                    View childView2 = ll.getChildAt(j+4);
                    View childView3 = ll.getChildAt(j+5);
                    View childView4 = ll.getChildAt(j+6);

                    //recupera os dados da 1 parcelas
                    EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc2"));
                    String gparcProd1 = (parcProd1.getText().toString());
                    EditText Total = (EditText) (childView.findViewWithTag("edt2total"));

                    EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                    EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                    EditText parcProdApoio1 = (EditText) (childView1.findViewWithTag("apoioparc2_1"));
                    EditText parcProdApoio2 = (EditText) (childView2.findViewWithTag("apoioparc2_2"));

                    EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                    EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                    EditText parcProdApoio3 = (EditText) (childView3.findViewWithTag("apoioparc2_3"));
                    EditText parcProdApoio4 = (EditText) (childView4.findViewWithTag("apoioparc2_4"));

                    if (getpar1 > 0) {
                        //VERIFICA O TIPO DA PRODUÇÃO PARA ADICIONAR
                        String produc;
                        if(GetSetCache.operacao.equals("Colheita")){
                            //CACHOS
                            produc = parcProd1.getText().toString();
                        }else if(!gparcProd1.equals("")){
                            //PLANTAS
                            produc = divplantas.toString();
                        }else{
                            produc = null;
                        }
                        parcProd1.setText(produc);
                        if(matApoio.getText().length()>4){
                            parcProdApoio1.setText(produc);
                        }
                        if(matApoio2.getText().length()>4){
                            parcProdApoio2.setText(produc);
                        }
                        if(matApoio3.getText().length()>4){
                            parcProdApoio3.setText(produc);
                        }
                        if(matApoio4.getText().length()>4){
                            parcProdApoio4.setText(produc);
                        }
                    }
                    if(parcProd1.length()>0){
                        TotalProd1 = new Double(parcProd1.getText().toString());
                    }else{
                        TotalProd1 = 0.00;
                    }

                    TotalProdGeral2 += TotalProd1;
                    TotalProdGeral += TotalProd1;
                    procLinha1 += 7;
                }
            }
        }

        if (ContadorParcMDO3 > 0) {
            Integer divplantas;
            Integer linhasCalc1 = ContadorParcMDO3;
            Integer Linhas = (linhaFinal3 - linhaInicial3) / linhasCalc1;
            Double divHa = hectar3 / linhasCalc1;
            divplantas = plantas3 / linhasCalc1;

            procLinha1 = 2;
            for(int j = 0; j < childCount; j++) {
                if (j == (procLinha1)) {
                    View childView = ll.getChildAt(j);
                    View childView1 = ll.getChildAt(j+3);
                    View childView2 = ll.getChildAt(j+4);
                    View childView3 = ll.getChildAt(j+5);
                    View childView4 = ll.getChildAt(j+6);

                    //recupera os dados da 1 parcelas
                    EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc3"));
                    String gparcProd1 = (parcProd1.getText().toString());
                    EditText Total = (EditText) (childView.findViewWithTag("edt3total"));

                    EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                    EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                    EditText parcProdApoio1 = (EditText) (childView1.findViewWithTag("apoioparc3_1"));
                    EditText parcProdApoio2 = (EditText) (childView2.findViewWithTag("apoioparc3_2"));

                    EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                    EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                    EditText parcProdApoio3 = (EditText) (childView3.findViewWithTag("apoioparc3_3"));
                    EditText parcProdApoio4 = (EditText) (childView4.findViewWithTag("apoioparc3_4"));

                    if (getpar1 > 0) {
                        //VERIFICA O TIPO DA PRODUÇÃO PARA ADICIONAR
                        String produc;
                        if(GetSetCache.operacao.equals("Colheita")){
                            //CACHOS
                            produc = parcProd1.getText().toString();
                        }else if(!gparcProd1.equals("")){
                            //PLANTAS
                            produc = divplantas.toString();
                        }else{
                            produc = null;
                        }
                        parcProd1.setText(produc);
                        if(matApoio.getText().length()>4){
                            parcProdApoio1.setText(produc);
                        }
                        if(matApoio2.getText().length()>4){
                            parcProdApoio2.setText(produc);
                        }
                        if(matApoio3.getText().length()>4){
                            parcProdApoio3.setText(produc);
                        }
                        if(matApoio4.getText().length()>4){
                            parcProdApoio4.setText(produc);
                        }
                    }
                    if(parcProd1.length()>0){
                        TotalProd1 = new Double(parcProd1.getText().toString());
                    }else{
                        TotalProd1 = 0.00;
                    }

                    TotalProdGeral3 += TotalProd1;
                    TotalProdGeral += TotalProd1;
                    procLinha1 += 7;
                }
            }
        }


        if (ContadorParcMDO4 > 0) {
            Integer divplantas;
            Integer linhasCalc1 = ContadorParcMDO4;
            Integer Linhas = (linhaFinal4 - linhaInicial4) / linhasCalc1;
            Double divHa = hectar4 / linhasCalc1;
            divplantas = plantas4 / linhasCalc1;

            procLinha1 = 2;
            for(int j = 0; j < childCount; j++) {
                if (j == (procLinha1)) {
                    View childView = ll.getChildAt(j);
                    View childView1 = ll.getChildAt(j+3);
                    View childView2 = ll.getChildAt(j+4);
                    View childView3 = ll.getChildAt(j+5);
                    View childView4 = ll.getChildAt(j+6);

                    //recupera os dados da 1 parcelas
                    EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc4"));
                    String gparcProd1 = (parcProd1.getText().toString());
                    EditText Total = (EditText) (childView.findViewWithTag("edt4total"));

                    EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                    EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                    EditText parcProdApoio1 = (EditText) (childView1.findViewWithTag("apoioparc4_1"));
                    EditText parcProdApoio2 = (EditText) (childView2.findViewWithTag("apoioparc4_2"));

                    EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                    EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                    EditText parcProdApoio3 = (EditText) (childView3.findViewWithTag("apoioparc4_3"));
                    EditText parcProdApoio4 = (EditText) (childView4.findViewWithTag("apoioparc4_4"));

                    if (getpar1 > 0) {
                        //VERIFICA O TIPO DA PRODUÇÃO PARA ADICIONAR
                        String produc;
                        if(GetSetCache.operacao.equals("Colheita")){
                            //CACHOS
                            produc = parcProd1.getText().toString();
                        }else if(!gparcProd1.equals("")){
                            //PLANTAS
                            produc = divplantas.toString();
                        }else{
                            produc = "";
                        }
                        parcProd1.setText(produc);
                        if(matApoio.getText().length()>4){
                            parcProdApoio1.setText(produc);
                        }
                        if(matApoio2.getText().length()>4){
                            parcProdApoio2.setText(produc);
                        }
                        if(matApoio3.getText().length()>4){
                            parcProdApoio3.setText(produc);
                        }
                        if(matApoio4.getText().length()>4){
                            parcProdApoio4.setText(produc);
                        }
                    }
                    if(parcProd1.length()>0){
                        TotalProd1 = new Double(parcProd1.getText().toString());
                    }else{
                        TotalProd1 = 0.00;
                    }

                    TotalProdGeral4 += TotalProd1;
                    TotalProdGeral += TotalProd1;
                    procLinha1 += 7;
                }
            }
        }

        //CRIAR OS TOTAIS NO RODAPÉ
        totalparc1.setText(TotalProdGeral1.toString());
        totalparc2.setText(TotalProdGeral2.toString());
        totalparc3.setText(TotalProdGeral3.toString());
        totalparc4.setText(TotalProdGeral4.toString());
        totalGeral.setText(TotalProdGeral.toString());
    }
    public int salvar(){
        int childCount = ll.getChildCount();
        int returnLinhasAft = 0;
        Integer getpar1 = parcela2.getSelectedItemPosition();
        Integer getpar2 = parcela3.getSelectedItemPosition();
        Integer getpar3 = parcela4.getSelectedItemPosition();
        Integer getpar4 = parcela5.getSelectedItemPosition();

        Integer ContadorParcMDO1 = 0, ContadorParcMDO2 = 0,ContadorParcMDO3 = 0, ContadorParcMDO4 = 0;
        GetSetAtividade getatividade = new GetSetAtividade();

        //CASO A ATIVIDADE NÃO SEJA DE PRODUÇÃO ARMAZENA A PARCELA PADRÃO 1
        Integer atividadeExt = atividade.getSelectedItemPosition();
        Integer parcelaFic = getpar1;
        if(parcelaFic == 0 && atividadeExt>0){
            getatividade.setId_parcela(1);
            parcelaFic = 1;
        }

        ArrayList<JSONObject> usersList1;
        usersList1 = new ArrayList<>();

        ArrayList<JSONObject> usersList2;
        usersList2 = new ArrayList<>();

        ArrayList<JSONObject> usersList3;
        usersList3 = new ArrayList<>();

        ArrayList<JSONObject> usersList4;
        usersList4 = new ArrayList<>();

        Integer procLinha1 = 2;
        for(int i = 0; i < childCount; i++){
            if (i == (procLinha1)) {
                View childView = ll.getChildAt(i);
                View childViewPat = ll.getChildAt(i+1);
                View childViewImp = ll.getChildAt(i+2);

                View childView1 = ll.getChildAt(i+3);
                View childView2 = ll.getChildAt(i+4);
                View childView3 = ll.getChildAt(i+5);
                View childView4 = ll.getChildAt(i+6);

                EditText Total = (EditText) (childView.findViewWithTag("edt1total"));

                EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
                String glinha = linha.getText().toString();

                EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));
                String gmatricula = (matricula.getText().toString());

                //recupera os dados das 4 parcelas
                EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc1"));
                String gparcProd1 = (parcProd1.getText().toString());

                EditText parcProd2 = (EditText) (childView.findViewWithTag("edt1parc2"));
                String gparcProd2 = (parcProd2.getText().toString());

                EditText parcProd3 = (EditText) (childView.findViewWithTag("edt1parc3"));
                String gparcProd3 = (parcProd3.getText().toString());

                EditText parcProd4 = (EditText) (childView.findViewWithTag("edt1parc4"));
                String gparcProd4 = (parcProd4.getText().toString());

                EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                String getmatApoio = matApoio.getText().toString();
                EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                String getmatApoio2 = matApoio2.getText().toString();

                EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                String getmatApoio3 = matApoio3.getText().toString();
                EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                String getmatApoio4 = matApoio4.getText().toString();

                EditText parcProdApoio1_1 = (EditText) (childView1.findViewWithTag("apoioparc1_1"));
                String gparcApoio1_1 = (parcProdApoio1_1.getText().toString());
                EditText parcProdApoio2_1 = (EditText) (childView1.findViewWithTag("apoioparc2_1"));
                String gparcApoio2_1 = (parcProdApoio2_1.getText().toString());
                EditText parcProdApoio3_1 = (EditText) (childView1.findViewWithTag("apoioparc3_1"));
                String gparcApoio3_1 = (parcProdApoio3_1.getText().toString());
                EditText parcProdApoio4_1 = (EditText) (childView1.findViewWithTag("apoioparc4_1"));
                String gparcApoio4_1 = (parcProdApoio4_1.getText().toString());

                EditText parcProdApoio1_2 = (EditText) (childView2.findViewWithTag("apoioparc1_2"));
                String gparcApoio1_2 = (parcProdApoio1_2.getText().toString());
                EditText parcProdApoio2_2 = (EditText) (childView2.findViewWithTag("apoioparc2_2"));
                String gparcApoio2_2 = (parcProdApoio2_2.getText().toString());
                EditText parcProdApoio3_2 = (EditText) (childView2.findViewWithTag("apoioparc3_2"));
                String gparcApoio3_2 = (parcProdApoio3_2.getText().toString());
                EditText parcProdApoio4_2 = (EditText) (childView2.findViewWithTag("apoioparc4_2"));
                String gparcApoio4_2 = (parcProdApoio4_2.getText().toString());

                EditText parcProdApoio1_3 = (EditText) (childView3.findViewWithTag("apoioparc1_3"));
                String gparcApoio1_3 = (parcProdApoio1_3.getText().toString());
                EditText parcProdApoio2_3 = (EditText) (childView3.findViewWithTag("apoioparc2_3"));
                String gparcApoio2_3 = (parcProdApoio2_3.getText().toString());
                EditText parcProdApoio3_3 = (EditText) (childView3.findViewWithTag("apoioparc3_3"));
                String gparcApoio3_3 = (parcProdApoio3_3.getText().toString());
                EditText parcProdApoio4_3 = (EditText) (childView3.findViewWithTag("apoioparc4_3"));
                String gparcApoio4_3 = (parcProdApoio4_3.getText().toString());

                EditText parcProdApoio1_4 = (EditText) (childView4.findViewWithTag("apoioparc1_4"));
                String gparcApoio1_4 = (parcProdApoio1_4.getText().toString());
                EditText parcProdApoio2_4 = (EditText) (childView4.findViewWithTag("apoioparc2_4"));
                String gparcApoio2_4 = (parcProdApoio2_4.getText().toString());
                EditText parcProdApoio3_4 = (EditText) (childView4.findViewWithTag("apoioparc3_4"));
                String gparcApoio3_4 = (parcProdApoio3_4.getText().toString());
                EditText parcProdApoio4_4 = (EditText) (childView4.findViewWithTag("apoioparc4_4"));
                String gparcApoio4_4 = (parcProdApoio4_4.getText().toString());

                //MAQUINÁRIO
                EditText horInicial = (EditText) (childViewPat.findViewWithTag("edithrInicial"));
                String ghorInicial = (horInicial.getText().toString());
                Spinner patrimonio = (Spinner) (childViewPat.findViewWithTag("spinner_patrimonio"));
                //recupera os dados dos 4 horimetros
                EditText hor1 = (EditText) (childViewPat.findViewWithTag("edthr1"));
                String ghor1 = (hor1.getText().toString());

                EditText hor2 = (EditText) (childViewPat.findViewWithTag("edthr2"));
                String ghor2 = (hor2.getText().toString());

                //EditText hor3 = (EditText) (childViewPat.findViewWithTag("edthr3"));
                //String ghor3 = (hor3.getText().toString());

                //EditText hor4 = (EditText) (childViewPat.findViewWithTag("edthr4"));
                //String ghor4 = (hor4.getText().toString());

                if(ghorInicial.length() == 0){
                    horInicial.setText("0.00");
                    ghorInicial = "0.00";
                }
                if(ghor1.isEmpty()){
                    hor1.setText("0.00");
                    ghor1 = "0.00";
                }

                if(ghor2.isEmpty()){
                    hor2.setText("");
                    ghor2 = "0";
                }
                /*
                if(ghor3.length() == 0){
                    hor3.setText("0.00");
                    ghor3 = "0.00";
                }
                if(ghor4.length() == 0){
                    hor4.setText("0.00");
                    ghor4 = "0.00";
                }
                */

                //IMPLEMENTO
                Spinner implemento = (Spinner) (childViewImp.findViewWithTag("spinner_implemento"));
                Database_syspalma database = new Database_syspalma(getContext());

                int idpatr = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                int idimp = database.RetoronoIdImplemento(implemento.getSelectedItem().toString());

                if (!gparcProd1.equals("") && getpar1 > 1) {
                    try {
                        JSONObject map1 = new JSONObject();
                        String atividadeAlt = null;
                        if(idpatr>0){
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }else{
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }

                        map1.put("idparcela", idParcela1);
                        map1.put("atividade", atividadeAlt);
                        map1.put("cachos", gparcProd1);
                        map1.put("matricula", gmatricula);
                        map1.put("maquinario", idpatr);
                        map1.put("implemento", idimp);
                        map1.put("hor_inicial", ghorInicial);
                        map1.put("hor_1", ghor1);
                        map1.put("hor_2", ghor2);
                        //map1.put("hor_3", ghor3);
                        //map1.put("hor_4", ghor4);

                        //PARA A GALERA DO APOIO I
                        map1.put("idparcelaApoio1", idParcela1);
                        map1.put("atividadeApoio1", atividade.getSelectedItem().toString());
                        map1.put("cachosApoio1", gparcApoio1_1);
                        map1.put("matriculaApoio1", getmatApoio);

                        //PARA A GALERA DO APOIO II
                        map1.put("idparcelaApoio2", idParcela1);
                        map1.put("atividadeApoio2", atividade.getSelectedItem().toString());
                        map1.put("cachosApoio2", gparcApoio1_2);
                        map1.put("matriculaApoio2", getmatApoio2);

                        //PARA A GALERA DO APOIO III
                        map1.put("idparcelaApoio3", idParcela1);
                        map1.put("atividadeApoio3", atividade.getSelectedItem().toString());
                        map1.put("cachosApoio3", gparcApoio1_3);
                        map1.put("matriculaApoio3", getmatApoio3);

                        //PARA A GALERA DO APOIO IV
                        map1.put("idparcelaApoio4", idParcela1);
                        map1.put("atividadeApoio4", atividade.getSelectedItem().toString());
                        map1.put("cachosApoio4", gparcApoio1_4);
                        map1.put("matriculaApoio4", getmatApoio4);
                        usersList1.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO1++;
                }else if(getpar1 == 1){
                    parcela2.setSelection(2);
                    String parcelaView = parcela2.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
                    idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));
                    Integer getparDescloc1 = idParcelaAtividade;

                    try {
                        JSONObject map1 = new JSONObject();
                        map1.put("idparcela", getparDescloc1);
                        map1.put("atividade", "Deslocamento de Maquinário");
                        map1.put("cachos", "0");
                        map1.put("matricula", gmatricula);
                        map1.put("maquinario", idpatr);
                        map1.put("implemento", idimp);
                        map1.put("hor_inicial", ghorInicial);
                        map1.put("hor_1", ghor1);
                        map1.put("hor_2", ghor2);
                        //map1.put("hor_3", ghor3);
                        //map1.put("hor_4", ghor4);

                        usersList1.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO1++;
                }
                if (!gparcProd2.equals("") && getpar2 > 1) {
                    try {
                        JSONObject map2 = new JSONObject();
                        String atividadeAlt = null;
                        if(idpatr>0){
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }else{
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }
                        map2.put("idparcela", idParcela2);
                        map2.put("atividade", atividadeAlt);
                        map2.put("cachos", gparcProd2);
                        map2.put("matricula", gmatricula);
                        map2.put("maquinario", idpatr);
                        map2.put("implemento", idimp);
                        map2.put("hor_inicial", ghorInicial);
                        map2.put("hor_1", ghor1);
                        map2.put("hor_2", ghor2);
                        //map2.put("hor_3", ghor3);
                        //map2.put("hor_4", ghor4);

                        //PARA A GALERA DO APOIO I
                        map2.put("idparcelaApoio1", idParcela2);
                        map2.put("atividadeApoio1", atividade.getSelectedItem().toString());
                        map2.put("cachosApoio1", gparcApoio2_1);
                        map2.put("matriculaApoio1", getmatApoio);

                        //PARA A GALERA DO APOIO II
                        map2.put("idparcelaApoio2", idParcela2);
                        map2.put("atividadeApoio2", atividade.getSelectedItem().toString());
                        map2.put("cachosApoio2", gparcApoio2_2);
                        map2.put("matriculaApoio2", getmatApoio2);

                        //PARA A GALERA DO APOIO III
                        map2.put("idparcelaApoio3", idParcela2);
                        map2.put("atividadeApoio3", atividade.getSelectedItem().toString());
                        map2.put("cachosApoio3", gparcApoio2_3);
                        map2.put("matriculaApoio3", getmatApoio3);

                        //PARA A GALERA DO APOIO IV
                        map2.put("idparcelaApoio4", idParcela2);
                        map2.put("atividadeApoio4", atividade.getSelectedItem().toString());
                        map2.put("cachosApoio4", gparcApoio2_4);
                        map2.put("matriculaApoio4", getmatApoio4);
                        usersList2.add(map2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO2++;
                }else if(getpar2 == 1){
                    parcela3.setSelection(2);
                    String parcelaView = parcela3.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
                    idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));
                    Integer getparDescloc2 = idParcelaAtividade;

                    try {
                        JSONObject map1 = new JSONObject();
                        map1.put("idparcela", getparDescloc2);
                        map1.put("atividade", "Deslocamento de Maquinário");
                        map1.put("cachos", "0");
                        map1.put("matricula", gmatricula);
                        map1.put("maquinario", idpatr);
                        map1.put("implemento", idimp);
                        map1.put("hor_inicial", ghorInicial);
                        map1.put("hor_1", ghor1);
                        map1.put("hor_2", ghor2);
                        //map1.put("hor_3", ghor3);
                        //map1.put("hor_4", ghor4);

                        usersList1.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO1++;
                }
                if (!gparcProd3.equals("") && getpar3 > 1) {
                    try {
                        JSONObject map3 = new JSONObject();
                        String atividadeAlt = null;
                        if(idpatr>0){
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }else{
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }
                        map3.put("idparcela", idParcela3);
                        map3.put("atividade", atividadeAlt);
                        map3.put("cachos", gparcProd3);
                        map3.put("matricula", gmatricula);
                        map3.put("maquinario", idpatr);
                        map3.put("implemento", idimp);
                        map3.put("hor_inicial", ghorInicial);
                        map3.put("hor_1", ghor1);
                        map3.put("hor_2", ghor2);
                        //map3.put("hor_3", ghor3);
                        //map3.put("hor_4", ghor4);

                        //PARA A GALERA DO APOIO I
                        map3.put("idparcelaApoio1", idParcela1);
                        map3.put("atividadeApoio1", atividade.getSelectedItem().toString());
                        map3.put("cachosApoio1", gparcApoio3_1);
                        map3.put("matriculaApoio1", getmatApoio);

                        //PARA A GALERA DO APOIO II
                        map3.put("idparcelaApoio2", idParcela1);
                        map3.put("atividadeApoio2", atividade.getSelectedItem().toString());
                        map3.put("cachosApoio2", gparcApoio3_2);
                        map3.put("matriculaApoio2", getmatApoio2);

                        //PARA A GALERA DO APOIO III
                        map3.put("idparcelaApoio3", idParcela1);
                        map3.put("atividadeApoio3", atividade.getSelectedItem().toString());
                        map3.put("cachosApoio3", gparcApoio3_3);
                        map3.put("matriculaApoio3", getmatApoio3);

                        //PARA A GALERA DO APOIO IV
                        map3.put("idparcelaApoio4", idParcela1);
                        map3.put("atividadeApoio4", atividade.getSelectedItem().toString());
                        map3.put("cachosApoio4", gparcApoio3_4);
                        map3.put("matriculaApoio4", getmatApoio4);
                        usersList3.add(map3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO3++;
                }else if(getpar3 == 1){
                    parcela4.setSelection(2);
                    String parcelaView = parcela4.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
                    idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));
                    Integer getparDescloc3 = idParcelaAtividade;

                    try {
                        JSONObject map1 = new JSONObject();
                        map1.put("idparcela", getparDescloc3);
                        map1.put("atividade", "Deslocamento de Maquinário");
                        map1.put("cachos", "0");
                        map1.put("matricula", gmatricula);
                        map1.put("maquinario", idpatr);
                        map1.put("implemento", idimp);
                        map1.put("hor_inicial", ghorInicial);
                        map1.put("hor_1", ghor1);
                        map1.put("hor_2", ghor2);
                        //map1.put("hor_3", ghor3);
                        //map1.put("hor_4", ghor4);

                        usersList1.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO1++;
                }
                if (!gparcProd4.equals("") && getpar4 > 1) {
                    try {
                        JSONObject map4 = new JSONObject();
                        String atividadeAlt = null;
                        if(idpatr>0){
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }else{
                            atividadeAlt = atividade.getSelectedItem().toString();
                        }
                        map4.put("idparcela", idParcela4);
                        map4.put("atividade", atividadeAlt);
                        map4.put("cachos", gparcProd4);
                        map4.put("matricula", gmatricula);
                        map4.put("maquinario", idpatr);
                        map4.put("implemento", idimp);
                        map4.put("hor_inicial", ghorInicial);
                        map4.put("hor_1", ghor1);
                        map4.put("hor_2", ghor2);
                        //map4.put("hor_3", ghor3);
                        //map4.put("hor_4", ghor4);

                        //PARA A GALERA DO APOIO I
                        map4.put("idparcelaApoio1", idParcela1);
                        map4.put("atividadeApoio1", atividade.getSelectedItem().toString());
                        map4.put("cachosApoio1", gparcApoio4_1);
                        map4.put("matriculaApoio1", getmatApoio);

                        //PARA A GALERA DO APOIO II
                        map4.put("idparcelaApoio2", idParcela1);
                        map4.put("atividadeApoio2", atividade.getSelectedItem().toString());
                        map4.put("cachosApoio2", gparcApoio4_2);
                        map4.put("matriculaApoio2", getmatApoio2);

                        //PARA A GALERA DO APOIO III
                        map4.put("idparcelaApoio3", idParcela1);
                        map4.put("atividadeApoio3", atividade.getSelectedItem().toString());
                        map4.put("cachosApoio3", gparcApoio4_3);
                        map4.put("matriculaApoio3", getmatApoio3);

                        //PARA A GALERA DO APOIO IV
                        map4.put("idparcelaApoio4", idParcela1);
                        map4.put("atividadeApoio4", atividade.getSelectedItem().toString());
                        map4.put("cachosApoio4", gparcApoio4_4);
                        map4.put("matriculaApoio4", getmatApoio4);
                        usersList4.add(map4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO4++;
                }else if(getpar4 == 1){
                    parcela5.setSelection(2);
                    String parcelaView = parcela5.getSelectedItem().toString();
                    ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
                    idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));
                    Integer getparDescloc5 = idParcelaAtividade;

                    try {
                        JSONObject map1 = new JSONObject();
                        map1.put("idparcela", getparDescloc5);
                        map1.put("atividade", "Deslocamento de Maquinário");
                        map1.put("cachos", "0");
                        map1.put("matricula", gmatricula);
                        map1.put("maquinario", idpatr);
                        map1.put("implemento", idimp);
                        map1.put("hor_inicial", ghorInicial);
                        map1.put("hor_1", ghor1);
                        map1.put("hor_2", ghor2);
                        //map1.put("hor_3", ghor3);
                        //map1.put("hor_4", ghor4);

                        usersList1.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ContadorParcMDO1++;
                }
                procLinha1 += 7;
            }
        }
        if (ContadorParcMDO1 > 0) {
            Integer linhasCalc1 = ContadorParcMDO1;
            Linhas1 = Linhas1 / linhasCalc1;
            divHa1 = divHa1 / linhasCalc1;
            Integer divplantas = plantas1 / linhasCalc1;

            Integer tamLista1 = usersList1.size();
            Database_syspalma database = new Database_syspalma(getContext());
            Database_syspalma_backup database_backup = new Database_syspalma_backup(getContext());
            Integer linhaInicial = linhaInicial1, linhaFinal= linhaInicial1 + Linhas1;

            for(int n = 0; n < linhasCalc1; n++) {
                try {
                    //GERAR NÚMEROS ALEATÓRIOS
                    Random gerador = new Random();
                    //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                    Integer aleatNumber = 0;
                    for (int i = 0; i < 1; i++) {
                        aleatNumber = gerador.nextInt(99);
                    }
                    final long date = System.currentTimeMillis();
                    SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                    String dateStringToken = sdfToken.format(date);

                    String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                    String apontamentoCod = cod;

                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    getatividade.setId_parcela((Integer) usersList1.get(n).get("idparcela"));
                    getatividade.setAtividade((String) usersList1.get(n).get("atividade"));
                    getatividade.setLinha_inicial(linhaInicial);
                    getatividade.setLinha_final(linhaFinal);
                    if(GetSetCache.operacao.equals("Colheita")) {
                        getatividade.setPlantas(divplantas);

                    }else{
                        getatividade.setPlantas(new Integer((String) usersList1.get(n).get("cachos")));
                    }
                    getatividade.setArea_realizada(divHa1);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo((String) usersList1.get(n).get("matricula"));
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                    database_backup.InserirRealizadoApontamento(getatividade);
                    //SALVAR O APOIO
                    if (id_apontamento > 0) {
                        returnLinhasAft++;
                        if (getSetCache.getOperacao().equals("Colheita")) {
                            GetSetAtividade colheita1 = new GetSetAtividade();
                            colheita1.setCachos(new Integer((String) usersList1.get(n).get("cachos")));
                            colheita1.setPeso(0.00);
                            colheita1.setCaixa(0);
                            colheita1.setId_apontamento(apontamentoCod + idget);
                            database.InserirRealizadoColheita(colheita1);
                            database_backup.InserirRealizadoColheita(colheita1);
                        }
                        Integer patrimonioCad = ((Integer) usersList1.get(n).get("maquinario"));

                        Double horimetroInicial = new Double(String.valueOf(usersList1.get(n).get("hor_1")));
                        Double horimetroFinal = new Double(String.valueOf(usersList1.get(n).get("hor_2")));

                        if(patrimonioCad>0 && horimetroInicial>0 &&  horimetroFinal>0 && horimetroInicial <= horimetroFinal){
                            GetSetAtividade getpatrimonio = new GetSetAtividade();
                            getpatrimonio.setId_patrimonio(((Integer) usersList1.get(n).get("maquinario")));
                            getpatrimonio.setId_implemento(((Integer) usersList1.get(n).get("implemento")));

                            getpatrimonio.setMarcador_inicial(horimetroInicial);
                            getpatrimonio.setMarcador_final(horimetroFinal);
                            getpatrimonio.setId_apontamento(apontamentoCod + idget);
                            getpatrimonio.setHora_inicial("00:00:00");
                            getpatrimonio.setHora_final("00:00:00");
                            database.InserirRealizadoPatrimonio(getpatrimonio);
                            database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                        }
                        //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList1.get(n).get("cachos")) + " salvo com sucesso");
                    } else {
                        alerta("Erro...", "Falha no apontamento");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO I
                try {
                    String matInt = (String) usersList1.get(n).get("matriculaApoio1");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList1.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList1.get(n).get("atividadeApoio1"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList1.get(n).get("cachosApoio1")));
                        }
                        getatividade.setArea_realizada(divHa1);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList1.get(n).get("matriculaApoio1"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList1.get(n).get("cachosApoio1")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList1.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList1.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList1.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList1.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList1.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO II
                try {
                    String matInt = (String) usersList1.get(n).get("matriculaApoio2");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList1.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList1.get(n).get("atividadeApoio2"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList1.get(n).get("cachosApoio2")));
                        }
                        getatividade.setArea_realizada(divHa1);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList1.get(n).get("matriculaApoio2"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList1.get(n).get("cachosApoio2")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList1.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList1.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList1.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList1.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList1.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO III
                try {
                    String matInt = (String) usersList1.get(n).get("matriculaApoio3");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList1.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList1.get(n).get("atividadeApoio3"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList1.get(n).get("cachosApoio3")));
                        }
                        getatividade.setArea_realizada(divHa1);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList1.get(n).get("matriculaApoio3"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList1.get(n).get("cachosApoio3")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList1.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList1.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList1.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList1.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList1.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO IV
                try {
                    String matInt = (String) usersList1.get(n).get("matriculaApoio4");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList1.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList1.get(n).get("atividadeApoio4"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);
                        } else {
                            getatividade.setPlantas(new Integer((String) usersList1.get(n).get("cachosApoio4")));
                        }
                        getatividade.setArea_realizada(divHa1);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList1.get(n).get("matriculaApoio4"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList1.get(n).get("cachosApoio4")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList1.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList1.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList1.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList1.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList1.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                linhaInicial = linhaFinal + 1;
                linhaFinal = linhaFinal + Linhas1;

            }
        }

        //PARCELA 2
        if (ContadorParcMDO2 > 0) {
            Integer linhasCalc1 = ContadorParcMDO2;
            Linhas2 = Linhas2 / linhasCalc1;
            divHa2 = divHa2 / linhasCalc1;
            Integer divplantas = plantas2 / linhasCalc1;

            Database_syspalma database = new Database_syspalma(getContext());
            Database_syspalma_backup database_backup = new Database_syspalma_backup(getContext());
            Integer linhaInicial = linhaInicial2, linhaFinal= linhaInicial2 + Linhas2;

            for(int n = 0; n < linhasCalc1; n++) {
                try {
                    //GERAR NÚMEROS ALEATÓRIOS
                    Random gerador = new Random();
                    //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                    Integer aleatNumber = 0;
                    for (int i = 0; i < 1; i++) {
                        aleatNumber = gerador.nextInt(99);
                    }
                    final long date = System.currentTimeMillis();
                    SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                    String dateStringToken = sdfToken.format(date);

                    String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                    String apontamentoCod = cod;

                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    getatividade.setId_parcela((Integer) usersList2.get(n).get("idparcela"));
                    getatividade.setAtividade((String) usersList2.get(n).get("atividade"));
                    getatividade.setLinha_inicial(linhaInicial);
                    getatividade.setLinha_final(linhaFinal);
                    if(GetSetCache.operacao.equals("Colheita")) {
                        getatividade.setPlantas(divplantas);

                    }else{
                        getatividade.setPlantas(new Integer((String) usersList2.get(n).get("cachos")));
                    }
                    getatividade.setArea_realizada(divHa2);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo((String) usersList2.get(n).get("matricula"));
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    if (parcelaFic > 0) {
                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList2.get(n).get("cachos")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList2.get(n).get("maquinario"));
                            /*
                            if(patrimonioCad>0){
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList2.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList2.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList2.get(n).get("hor_inicial"))) + new Double(String.valueOf(usersList2.get(n).get("hor_1")));
                                Double horimetroFinal = new Double(String.valueOf(usersList2.get(n).get("hor_2")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(GetSetCache.getFicha()+aleatNumber + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO I
                try {
                    String matInt = (String) usersList2.get(n).get("matriculaApoio1");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList2.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList2.get(n).get("atividadeApoio1"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList2.get(n).get("cachosApoio1")));
                        }
                        getatividade.setArea_realizada(divHa2);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList2.get(n).get("matriculaApoio1"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList2.get(n).get("cachosApoio1")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList2.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList2.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList2.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList2.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList2.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO II
                try {
                    String matInt = (String) usersList2.get(n).get("matriculaApoio2");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList2.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList2.get(n).get("atividadeApoio2"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList2.get(n).get("cachosApoio2")));
                        }
                        getatividade.setArea_realizada(divHa2);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList2.get(n).get("matriculaApoio2"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList2.get(n).get("cachosApoio2")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList2.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList2.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList2.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList2.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList2.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO III
                try {
                    String matInt = (String) usersList2.get(n).get("matriculaApoio3");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList2.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList2.get(n).get("atividadeApoio3"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList2.get(n).get("cachosApoio3")));
                        }
                        getatividade.setArea_realizada(divHa2);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList2.get(n).get("matriculaApoio3"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList2.get(n).get("cachosApoio3")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList2.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList2.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList2.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList2.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList2.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO IV
                try {
                    String matInt = (String) usersList2.get(n).get("matriculaApoio4");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList2.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList2.get(n).get("atividadeApoio4"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList2.get(n).get("cachosApoio4")));
                        }
                        getatividade.setArea_realizada(divHa2);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList2.get(n).get("matriculaApoio4"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList2.get(n).get("cachosApoio4")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList2.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList2.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList2.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList2.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList2.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                linhaInicial = linhaFinal + 1;
                linhaFinal = linhaFinal + Linhas2;

            }
        }

        //PARCELA 3
        if (ContadorParcMDO3 > 0) {
            Integer linhasCalc1 = ContadorParcMDO3;
            Linhas3 = Linhas3 / linhasCalc1;
            divHa3 = divHa3 / linhasCalc1;
            Integer divplantas = plantas3 / linhasCalc1;

            Database_syspalma database = new Database_syspalma(getContext());
            Database_syspalma_backup database_backup = new Database_syspalma_backup(getContext());
            Integer linhaInicial = linhaInicial3, linhaFinal= linhaInicial3 + Linhas3;

            for(int n = 0; n < linhasCalc1; n++) {
                try {
                    //GERAR NÚMEROS ALEATÓRIOS
                    Random gerador = new Random();
                    //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                    Integer aleatNumber = 0;
                    for (int i = 0; i < 1; i++) {
                        aleatNumber = gerador.nextInt(99);
                    }
                    final long date = System.currentTimeMillis();
                    SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                    String dateStringToken = sdfToken.format(date);

                    String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                    String apontamentoCod = cod;

                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    getatividade.setId_parcela((Integer) usersList3.get(n).get("idparcela"));
                    getatividade.setAtividade((String) usersList3.get(n).get("atividade"));
                    getatividade.setLinha_inicial(linhaInicial);
                    getatividade.setLinha_final(linhaFinal);
                    if(GetSetCache.operacao.equals("Colheita")) {
                        getatividade.setPlantas(divplantas);

                    }else{
                        getatividade.setPlantas(new Integer((String) usersList3.get(n).get("cachos")));
                    }
                    getatividade.setArea_realizada(divHa3);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo((String) usersList3.get(n).get("matricula"));
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    if (parcelaFic > 0) {
                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList3.get(n).get("cachos")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList3.get(n).get("maquinario"));
                            /*
                            if(patrimonioCad>0){
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList3.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList3.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList3.get(n).get("hor_inicial"))) + new Double(String.valueOf(usersList3.get(n).get("hor_1")))
                                        + new Double(String.valueOf(usersList3.get(n).get("hor_2")));
                                Double horimetroFinal = new Double(String.valueOf(usersList3.get(n).get("hor_3")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(GetSetCache.getFicha()+aleatNumber + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList3.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO I
                try {
                    String matInt = (String) usersList3.get(n).get("matriculaApoio1");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList3.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList3.get(n).get("atividadeApoio1"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList3.get(n).get("cachosApoio1")));
                        }
                        getatividade.setArea_realizada(divHa3);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList3.get(n).get("matriculaApoio1"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList3.get(n).get("cachosApoio1")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList3.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList3.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList3.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList3.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList3.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO II
                try {
                    String matInt = (String) usersList3.get(n).get("matriculaApoio2");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList3.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList3.get(n).get("atividadeApoio2"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList3.get(n).get("cachosApoio2")));
                        }
                        getatividade.setArea_realizada(divHa3);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList3.get(n).get("matriculaApoio2"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList3.get(n).get("cachosApoio2")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList3.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList3.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList3.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList3.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList3.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO III
                try {
                    String matInt = (String) usersList3.get(n).get("matriculaApoio3");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList3.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList3.get(n).get("atividadeApoio3"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList3.get(n).get("cachosApoio3")));
                        }
                        getatividade.setArea_realizada(divHa3);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList3.get(n).get("matriculaApoio3"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList3.get(n).get("cachosApoio3")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList3.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList3.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList3.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList3.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList3.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO IV
                try {
                    String matInt = (String) usersList3.get(n).get("matriculaApoio4");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList3.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList3.get(n).get("atividadeApoio4"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList3.get(n).get("cachosApoio4")));
                        }
                        getatividade.setArea_realizada(divHa3);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList3.get(n).get("matriculaApoio4"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList3.get(n).get("cachosApoio4")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList3.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList3.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList3.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList3.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList3.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                linhaInicial = linhaFinal + 1;
                linhaFinal = linhaFinal + Linhas3;
            }
        }
        //PARCELA 4
        if (ContadorParcMDO4 > 0) {
            Integer linhasCalc1 = ContadorParcMDO4;
            Linhas4 = Linhas4 / linhasCalc1;
            divHa4 = divHa4 / linhasCalc1;
            Integer divplantas = plantas4 / linhasCalc1;

            Database_syspalma database = new Database_syspalma(getContext());
            Database_syspalma_backup database_backup = new Database_syspalma_backup(getContext());
            Integer linhaInicial = linhaInicial4, linhaFinal= linhaInicial4 + Linhas4;
            for(int n = 0; n < linhasCalc1; n++) {
                try {
                    ///GERAR NÚMEROS ALEATÓRIOS
                    Random gerador = new Random();
                    //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                    Integer aleatNumber = 0;
                    for (int i = 0; i < 1; i++) {
                        aleatNumber = gerador.nextInt(99);
                    }
                    final long date = System.currentTimeMillis();
                    SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                    String dateStringToken = sdfToken.format(date);

                    String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                    String apontamentoCod = cod;

                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    getatividade.setId_parcela((Integer) usersList4.get(n).get("idparcela"));
                    getatividade.setAtividade((String) usersList4.get(n).get("atividade"));
                    getatividade.setLinha_inicial(linhaInicial);
                    getatividade.setLinha_final(linhaFinal);
                    if(GetSetCache.operacao.equals("Colheita")) {
                        getatividade.setPlantas(divplantas);

                    }else{
                        getatividade.setPlantas(new Integer((String) usersList4.get(n).get("cachos")));
                    }
                    getatividade.setArea_realizada(divHa4);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo((String) usersList4.get(n).get("matricula"));
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    if (parcelaFic > 0) {
                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList4.get(n).get("cachos")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList4.get(n).get("maquinario"));
                            /*
                            if(patrimonioCad>0){
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList4.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList4.get(n).get("implemento")));
                                Double horimetroInicial = new Double(String.valueOf(usersList4.get(n).get("hor_inicial"))) + new Double(String.valueOf(usersList4.get(n).get("hor_1")))
                                        + new Double(String.valueOf(usersList4.get(n).get("hor_2"))) + new Double(String.valueOf(usersList4.get(n).get("hor_3")));
                                Double horimetroFinal = new Double(String.valueOf(usersList4.get(n).get("hor_4")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(GetSetCache.getFicha()+aleatNumber + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList4.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO I
                try {
                    String matInt = (String) usersList4.get(n).get("matriculaApoio1");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList4.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList4.get(n).get("atividadeApoio1"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList4.get(n).get("cachosApoio1")));
                        }
                        getatividade.setArea_realizada(divHa4);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList4.get(n).get("matriculaApoio1"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList4.get(n).get("cachosApoio1")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList4.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList4.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList4.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList4.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList4.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO II
                try {
                    String matInt = (String) usersList4.get(n).get("matriculaApoio2");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList4.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList4.get(n).get("atividadeApoio2"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList4.get(n).get("cachosApoio2")));
                        }
                        getatividade.setArea_realizada(divHa4);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList4.get(n).get("matriculaApoio2"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList4.get(n).get("cachosApoio2")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList4.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList4.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList4.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList4.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList4.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO III
                try {
                    String matInt = (String) usersList4.get(n).get("matriculaApoio3");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList4.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList4.get(n).get("atividadeApoio3"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList4.get(n).get("cachosApoio3")));
                        }
                        getatividade.setArea_realizada(divHa4);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList4.get(n).get("matriculaApoio3"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList4.get(n).get("cachosApoio3")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList4.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList4.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList4.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList4.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList4.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                            */
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SALVAR O APOIO II
                try {
                    String matInt = (String) usersList4.get(n).get("matriculaApoio4");
                    if(matInt.length()>4) {
                        //GERAR NÚMEROS ALEATÓRIOS
                        Random gerador = new Random();
                        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
                        Integer aleatNumber = 0;
                        for (int i = 0; i < 1; i++) {
                            aleatNumber = gerador.nextInt(99);
                        }
                        final long date = System.currentTimeMillis();
                        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
                        String dateStringToken = sdfToken.format(date);

                        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
                        String apontamentoCod = cod;

                        Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                        getatividade.setId_parcela((Integer) usersList4.get(n).get("idparcela"));
                        getatividade.setAtividade((String) usersList4.get(n).get("atividadeApoio4"));
                        getatividade.setLinha_inicial(linhaInicial);
                        getatividade.setLinha_final(linhaFinal);
                        if (GetSetCache.operacao.equals("Colheita")) {
                            getatividade.setPlantas(divplantas);

                        } else {
                            getatividade.setPlantas(new Integer((String) usersList4.get(n).get("cachosApoio2")));
                        }
                        getatividade.setArea_realizada(divHa4);
                        getatividade.setId_ficha(GetSetCache.getFicha());
                        getatividade.setId_mdo((String) usersList4.get(n).get("matriculaApoio4"));
                        getatividade.setGerar_apontamento(apontamentoCod + idget);

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            if (getSetCache.getOperacao().equals("Colheita")) {
                                GetSetAtividade colheita1 = new GetSetAtividade();
                                colheita1.setCachos(new Integer((String) usersList4.get(n).get("cachosApoio4")));
                                colheita1.setPeso(0.00);
                                colheita1.setCaixa(0);
                                colheita1.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoColheita(colheita1);
                                database_backup.InserirRealizadoColheita(colheita1);
                            }
                            Integer patrimonioCad = ((Integer) usersList4.get(n).get("maquinario"));
                            /*
                            if (patrimonioCad > 0) {
                                GetSetAtividade getpatrimonio = new GetSetAtividade();
                                getpatrimonio.setId_patrimonio(((Integer) usersList4.get(n).get("maquinario")));
                                getpatrimonio.setId_implemento(((Integer) usersList4.get(n).get("implemento")));

                                Double horimetroInicial = new Double(String.valueOf(usersList4.get(n).get("hor_inicial")));
                                Double horimetroFinal = new Double(String.valueOf(usersList4.get(n).get("hor_1")));

                                getpatrimonio.setMarcador_inicial(horimetroInicial);
                                getpatrimonio.setMarcador_final(horimetroFinal);
                                getpatrimonio.setId_apontamento(apontamentoCod + idget);
                                database.InserirRealizadoPatrimonio(getpatrimonio);
                                database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                            }
                            */
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                linhaInicial = linhaFinal + 1;
                linhaFinal = linhaFinal + Linhas4;
            }
        }
        return returnLinhasAft++;
    }
    public int salvarOAtividades(){
        parcela2.setSelection(2);
        String parcelaView = parcela2.getSelectedItem().toString();
        ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
        idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));

        int childCount = ll.getChildCount();
        int returnLinhasAft = 0;
        Integer getpar1 = idParcelaAtividade;

        GetSetAtividade getatividade = new GetSetAtividade();

        //GERAR NÚMEROS ALEATÓRIOS
        Random gerador = new Random();
        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
        Integer aleatNumber = 0;
        for (int i = 0; i < 1; i++) {
            aleatNumber = gerador.nextInt(99);
        }
        final long date = System.currentTimeMillis();
        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
        String dateStringToken = sdfToken.format(date);

        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
        String apontamentoCod = cod;

        Integer procLinha1 = 2;
        for(int i = 0; i < childCount; i++){
            if (i == (procLinha1)) {
                View childView = ll.getChildAt(i);
                View childViewPat = ll.getChildAt(i+1);
                View childViewImp = ll.getChildAt(i+2);

                //SALVAR O HORÍMETRO DOS MAQUINÁRIOS
                EditText horInicial = (EditText) (childViewPat.findViewWithTag("edithrInicial"));
                String ghorInicial = (horInicial.getText().toString());
                Spinner patrimonio = (Spinner) (childViewPat.findViewWithTag("spinner_patrimonio"));
                //recupera os dados dos 4 horimetros
                EditText hor1 = (EditText) (childViewPat.findViewWithTag("edthr1"));
                String ghor1 = (hor1.getText().toString());

                EditText hor2 = (EditText) (childViewPat.findViewWithTag("edthr2"));
                String ghor2 = (hor2.getText().toString());

                if(ghorInicial.length() == 0){
                    horInicial.setText("0.00");
                    ghorInicial = "0.00";
                }
                if(ghor1.isEmpty()){
                    hor1.setText("0.00");
                    ghor1 = "0.00";
                }

                if(ghor2.isEmpty()){
                    hor2.setText("");
                    ghor2 = "0";
                }

                //IMPLEMENTO
                Spinner implemento = (Spinner) (childViewImp.findViewWithTag("spinner_implemento"));
                Database_syspalma database = new Database_syspalma(getContext());

                int idpatr = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
                int idimp = database.RetoronoIdImplemento(implemento.getSelectedItem().toString());


                EditText Total = (EditText) (childView.findViewWithTag("edt1total"));

                EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
                String glinha = linha.getText().toString();

                EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));
                String gmatricula = (matricula.getText().toString());

                //VERIFICA SE A MATRICULA ESTÁ COMPLETA
                if(gmatricula.length()>4 && !atividade.getSelectedItem().toString().equals("SELECIONE UMA ATIVIDADE")) {
                    Database_syspalma_backup database_backup = new Database_syspalma_backup(getContext());

                    Long idget = database.RetoronoIdRealizado(GetSetCache.getFicha());
                    getatividade.setId_parcela(getpar1);
                    getatividade.setAtividade(atividade.getSelectedItem().toString());
                    getatividade.setLinha_inicial(0);
                    getatividade.setLinha_final(0);
                    getatividade.setPlantas(0);
                    getatividade.setArea_realizada(0.00);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo(gmatricula);
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                    database_backup.InserirRealizadoApontamento(getatividade);
                    //SALVAR O MAQUINÁRIO
                    if (id_apontamento > 0) {
                        Double horimetroFinal = 0.00;
                        Double horimetroInicial = 0.00;
                        if(hor1.getText().toString().length() > 0 && hor2.getText().toString().length() > 0){
                            horimetroFinal = new Double(hor2.getText().toString());
                            horimetroInicial = new Double(hor1.getText().toString());
                        }

                        if(idpatr>0 && horimetroInicial>0 &&  horimetroFinal>0 && horimetroInicial <= horimetroFinal){
                            GetSetAtividade getpatrimonio = new GetSetAtividade();
                            getpatrimonio.setId_patrimonio(idpatr);
                            getpatrimonio.setId_implemento(idimp);

                            getpatrimonio.setMarcador_inicial(horimetroInicial);
                            getpatrimonio.setMarcador_final(horimetroFinal);
                            getpatrimonio.setId_apontamento(apontamentoCod + idget);
                            getpatrimonio.setHora_inicial("00:00:00");
                            getpatrimonio.setHora_final("00:00:00");
                            database.InserirRealizadoPatrimonio(getpatrimonio);
                            database_backup.InserirRealizadoPatrimonio(getpatrimonio);
                        }
                        returnLinhasAft++;
                    } else {
                        alerta("Erro...", "Falha no apontamento");
                    }
                }
                procLinha1 += 7;
            }
        }
        return returnLinhasAft++;
    }
    public void limparCampos(){
        int childCount = ll.getChildCount();

        Integer procLinha1 = 2;
        for(int i = 0; i < childCount; i++) {
            if (i == (procLinha1)) {
                View childView = ll.getChildAt(i);
                View childViewPat = ll.getChildAt(i + 1);
                View childViewImp = ll.getChildAt(i + 2);

                View childView1 = ll.getChildAt(i + 3);
                View childView2 = ll.getChildAt(i + 4);
                View childView3 = ll.getChildAt(i + 5);
                View childView4 = ll.getChildAt(i + 6);

                EditText Total = (EditText) (childView.findViewWithTag("edt1total"));

                EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
                String glinha = linha.getText().toString();

                EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));
                String gmatricula = (matricula.getText().toString());

                //recupera os dados das 4 parcelas
                EditText parcProd1 = (EditText) (childView.findViewWithTag("edt1parc1"));
                String gparcProd1 = (parcProd1.getText().toString());

                EditText parcProd2 = (EditText) (childView.findViewWithTag("edt1parc2"));
                String gparcProd2 = (parcProd2.getText().toString());

                EditText parcProd3 = (EditText) (childView.findViewWithTag("edt1parc3"));
                String gparcProd3 = (parcProd3.getText().toString());

                EditText parcProd4 = (EditText) (childView.findViewWithTag("edt1parc4"));
                String gparcProd4 = (parcProd4.getText().toString());

                EditText matApoio = (EditText) (childView1.findViewWithTag("matapoio1_1"));
                String getmatApoio = matApoio.getText().toString();
                EditText matApoio2 = (EditText) (childView2.findViewWithTag("matapoio1_2"));
                String getmatApoio2 = matApoio2.getText().toString();

                EditText matApoio3 = (EditText) (childView3.findViewWithTag("matapoio1_3"));
                String getmatApoio3 = matApoio3.getText().toString();
                EditText matApoio4 = (EditText) (childView4.findViewWithTag("matapoio1_4"));
                String getmatApoio4 = matApoio4.getText().toString();

                EditText parcProdApoio1_1 = (EditText) (childView1.findViewWithTag("apoioparc1_1"));
                String gparcApoio1_1 = (parcProdApoio1_1.getText().toString());
                EditText parcProdApoio2_1 = (EditText) (childView1.findViewWithTag("apoioparc2_1"));
                String gparcApoio2_1 = (parcProdApoio2_1.getText().toString());
                EditText parcProdApoio3_1 = (EditText) (childView1.findViewWithTag("apoioparc3_1"));
                String gparcApoio3_1 = (parcProdApoio3_1.getText().toString());
                EditText parcProdApoio4_1 = (EditText) (childView1.findViewWithTag("apoioparc4_1"));
                String gparcApoio4_1 = (parcProdApoio4_1.getText().toString());

                EditText parcProdApoio1_2 = (EditText) (childView2.findViewWithTag("apoioparc1_2"));
                String gparcApoio1_2 = (parcProdApoio1_2.getText().toString());
                EditText parcProdApoio2_2 = (EditText) (childView2.findViewWithTag("apoioparc2_2"));
                String gparcApoio2_2 = (parcProdApoio2_2.getText().toString());
                EditText parcProdApoio3_2 = (EditText) (childView2.findViewWithTag("apoioparc3_2"));
                String gparcApoio3_2 = (parcProdApoio3_2.getText().toString());
                EditText parcProdApoio4_2 = (EditText) (childView2.findViewWithTag("apoioparc4_2"));
                String gparcApoio4_2 = (parcProdApoio4_2.getText().toString());

                EditText parcProdApoio1_3 = (EditText) (childView3.findViewWithTag("apoioparc1_3"));
                String gparcApoio1_3 = (parcProdApoio1_3.getText().toString());
                EditText parcProdApoio2_3 = (EditText) (childView3.findViewWithTag("apoioparc2_3"));
                String gparcApoio2_3 = (parcProdApoio2_3.getText().toString());
                EditText parcProdApoio3_3 = (EditText) (childView3.findViewWithTag("apoioparc3_3"));
                String gparcApoio3_3 = (parcProdApoio3_3.getText().toString());
                EditText parcProdApoio4_3 = (EditText) (childView3.findViewWithTag("apoioparc4_3"));
                String gparcApoio4_3 = (parcProdApoio4_3.getText().toString());

                EditText parcProdApoio1_4 = (EditText) (childView4.findViewWithTag("apoioparc1_4"));
                String gparcApoio1_4 = (parcProdApoio1_4.getText().toString());
                EditText parcProdApoio2_4 = (EditText) (childView4.findViewWithTag("apoioparc2_4"));
                String gparcApoio2_4 = (parcProdApoio2_4.getText().toString());
                EditText parcProdApoio3_4 = (EditText) (childView4.findViewWithTag("apoioparc3_4"));
                String gparcApoio3_4 = (parcProdApoio3_4.getText().toString());
                EditText parcProdApoio4_4 = (EditText) (childView4.findViewWithTag("apoioparc4_4"));
                String gparcApoio4_4 = (parcProdApoio4_4.getText().toString());

                //MAQUINÁRIO
                EditText horInicial = (EditText) (childViewPat.findViewWithTag("edithrInicial"));
                String ghorInicial = (horInicial.getText().toString());
                Spinner patrimonio = (Spinner) (childViewPat.findViewWithTag("spinner_patrimonio"));
                //recupera os dados dos 4 horimetros
                EditText hor1 = (EditText) (childViewPat.findViewWithTag("edthr1"));
                String ghor1 = (hor1.getText().toString());

                EditText hor2 = (EditText) (childViewPat.findViewWithTag("edthr2"));
                String ghor2 = (hor2.getText().toString());

                //EditText hor3 = (EditText) (childViewPat.findViewWithTag("edthr3"));
                //String ghor3 = (hor3.getText().toString());

                //EditText hor4 = (EditText) (childViewPat.findViewWithTag("edthr4"));
                //String ghor4 = (hor4.getText().toString());

                parcProd1.setText("");
                parcProd2.setText("");
                parcProd3.setText("");
                parcProd4.setText("");

                if(!ghor2.equals("")){
                    hor1.setText("");
                    hor2.setText("");
                }

                parcProdApoio1_1.setText("");
                parcProdApoio2_1.setText("");
                parcProdApoio3_1.setText("");
                parcProdApoio4_1.setText("");

                parcProdApoio1_2.setText("");
                parcProdApoio2_2.setText("");
                parcProdApoio3_2.setText("");
                parcProdApoio4_2.setText("");

                parcProdApoio1_3.setText("");
                parcProdApoio2_3.setText("");
                parcProdApoio3_3.setText("");
                parcProdApoio4_3.setText("");

                parcProdApoio1_4.setText("");
                parcProdApoio2_4.setText("");
                parcProdApoio3_4.setText("");
                parcProdApoio4_4.setText("");

                procLinha1 += 7;
            }
        }
    }
}
