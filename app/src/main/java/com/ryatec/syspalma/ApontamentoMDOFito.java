package com.ryatec.syspalma;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
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
import android.widget.Button;
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
public class ApontamentoMDOFito extends Fragment {

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

    private TextView totalparc1, totalparc2, totalparc3, totalparc4, totalGeral;
    private Integer linhaRowTable;

    private Button salvarApontamento;
    private ImageButton upd, addMDO;
    private GetSetCache getSetCache = new GetSetCache();
    static LinearLayout apontamentoParcela;
    private Integer idParcelaAtividade = 1;

    //VARIAVES PARA A DISTRIBUIÇÃO DO APONTAMENTO
    private Integer linhaInicial1, linhaFinal1, plantas1, linhaInicial2, linhaFinal2, plantas2, linhaInicial3, linhaFinal3, plantas3, linhaInicial4, linhaFinal4, plantas4;
    private Integer idParcela1,idParcela2,idParcela3,idParcela4, Linhas1, Linhas2, Linhas3, Linhas4;
    private Double hectar1, hectar2, hectar3, hectar4, divHa1, divHa2, divHa3, divHa4;

    private Integer lin_in = 1;
    private Integer lin_fin = 1;

    public ApontamentoMDOFito() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

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

        implemento = (Spinner) view.findViewById(R.id.spinner_implemento);
        patrimonio = (Spinner) view.findViewById(R.id.spinner_patrimonio);
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

                    Linhas1 = (linhaFinal1 - linhaInicial1);
                    divHa1 = hectar1;
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

                    Linhas4 = (linhaFinal4 - linhaInicial4);
                    divHa4 = hectar4;
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
        upd.setVisibility(View.GONE);
        salvarApontamento = (Button) view.findViewById(R.id.salvar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        addMDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinha();
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
                    if(funcsalvar > 0){
                        exemplo_alerta("Salvando...","Apontamentos salvos");
                    }else{
                        alerta("Salvando...","Nenhum apontamento salvo");
                    }
                }else if(atividadeExt > 0 && !atividadeGet.equals("SELECIONE UMA ATIVIDADE") && parcela2.getSelectedItemPosition()==0 && !atividadeGet.equals("CAIXEIRO")){
                    int funcsalvar = salvarOAtividades();
                    if(funcsalvar > 0){
                        exemplo_alerta("Salvando...","Apontamentos salvos");
                    }else{
                        alerta("Salvando...","Nenhum apontamento salvo");
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
        //REMOVE O DESLOCAMENTO DO MAQUINÁRIO
        dataAdapter.remove(dataAdapter.getItem(1));
        dataAdapter.notifyDataSetChanged();

        parcela2.setAdapter(dataAdapter);
        parcela3.setAdapter(dataAdapter);
        parcela4.setAdapter(dataAdapter);
        parcela5.setAdapter(dataAdapter);

        List<String> atividades = db.SelectAtividade(getSetCache.getOperacao());
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
    public void addLinha(){
            linhaRowTable = ll.getChildCount() - 1;
            TableRow row= new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(15,0,0,0);

            EditText edtN = new EditText(getContext());
            edtN.setEnabled(false);
            edtN.setEms(2);
            edtN.setInputType(1);
            //edtN.setLayoutParams(params);
            edtN.setGravity(Gravity.CENTER);
            edtN.setText(linhaRowTable.toString());
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
            edtParc1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            edtParc1.setId(linhaRowTable+3);
            edtParc1.setLayoutParams(params);
            edtParc1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            edtParc1.setHint("P1");
            edtParc1.setTag("edt1parc1");

            EditText edtParc2 = new EditText(getContext());
            edtParc2.setEms(2);
            edtParc2.setGravity(Gravity.CENTER);
            edtParc2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
            edtParc3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            edtParc3.setId(linhaRowTable+5);
            edtParc3.setLayoutParams(params);
            edtParc3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            edtParc3.setHint("P3");
            edtParc3.setTag("edt1parc3");

            EditText edtParc4 = new EditText(getContext());
            edtParc4.setEms(2);
            edtParc4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
            ll.addView(row,linhaRowTable+1);
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
            for(int i = 2; i < childCount; i++){
                View childView = ll.getChildAt(i);

                EditText Total = (EditText)(childView.findViewWithTag("edt1total"));

                EditText linha = (EditText)(childView.findViewWithTag("edt1n"));
                String glinha = linha.getText().toString();

                EditText matricula = (EditText)(childView.findViewWithTag("edt1maticula"));
                String gmatricula = (matricula.getText().toString());

                //recupera os dados das 4 parcelas
                EditText parcProd1 = (EditText)(childView.findViewWithTag("edt1parc1"));
                String gparcProd1 = (parcProd1.getText().toString());

                EditText parcProd2 = (EditText)(childView.findViewWithTag("edt1parc2"));
                String gparcProd2 = (parcProd2.getText().toString());

                EditText parcProd3 = (EditText)(childView.findViewWithTag("edt1parc3"));
                String gparcProd3 = (parcProd3.getText().toString());

                EditText parcProd4 = (EditText)(childView.findViewWithTag("edt1parc4"));
                String gparcProd4 = (parcProd4.getText().toString());

                if(!gparcProd1.equals("") && getpar1 > 0) {
                    ContadorParcMDO1 ++;
                    TotalProd1 = new Double(gparcProd1);
                    TotalProdGeral1 += TotalProd1;
                }
                if(!gparcProd2.equals("")  && getpar2 > 0) {
                    ContadorParcMDO2 ++;
                    TotalProd2 = new Double(gparcProd2);
                    TotalProdGeral2 += TotalProd2;
                }
                if(!gparcProd3.equals("")  && getpar3 > 0) {
                    ContadorParcMDO3 ++;
                    TotalProd3 = new Double(gparcProd3);
                    TotalProdGeral3 += TotalProd3;
                }
                if(!gparcProd4.equals("")  && getpar4 > 0) {
                    ContadorParcMDO4 ++;
                    TotalProd4 = new Double(gparcProd4);
                    TotalProdGeral4 += TotalProd4;
                }
                TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                Total.setText(TotalProd.toString());
                TotalProdGeral += TotalProd;
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
                Integer linhasCalc1 = childCount - 2;
                Integer Linhas = (linhaFinal1 - linhaInicial1) / linhasCalc1;
                Double divHa = hectar1 / linhasCalc1;
                Integer restoPlantas = plantas1 % linhasCalc1;
                Integer divplantas = (plantas1 / linhasCalc1);
                for(int j = 2; j < childCount; j++) {
                    View childView = ll.getChildAt(j);
                    EditText calcParcProd1 = (EditText)(childView.findViewWithTag("edt1parc1"));
                    /*
                    if(j == (childCount-1)){
                        //VERIFICA SE É IMPA
                        if((plantas1 % linhasCalc1) != 0){
                            divplantas = divplantas + restoPlantas;
                            calcParcProd1.setText(divplantas.toString());
                        }else{
                            calcParcProd1.setText(divplantas.toString());
                        }
                    }else{
                        calcParcProd1.setText(divplantas.toString());
                    }
                    */
                    /*calcParcProd1.setText(divplantas.toString());
                    EditText Total = (EditText)(childView.findViewWithTag("edt1parc1"));
                    TotalProd1 = new Double(Total.getText().toString());
                    TotalProdGeral1 += TotalProd1;

                    EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                    TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                    TotalParcelas.setText(TotalProd.toString());
                    TotalProdGeral += TotalProd;
                    */
                }
            }
            if (plantas2 > 0) {
                Integer linhasCalc2 = childCount - 2;
                Integer Linhas = (linhaFinal2 - linhaInicial2) / linhasCalc2;
                Double divHa = hectar2 / linhasCalc2;
                Integer restoPlantas = plantas2 % linhasCalc2;
                Integer divplantas = (plantas2 / linhasCalc2) - restoPlantas;
                for(int j = 2; j < childCount; j++) {
                    View childView = ll.getChildAt(j);
                    EditText calcParcProd2 = (EditText)(childView.findViewWithTag("edt1parc2"));
                    /*
                    if(j == (childCount-1)){
                        //VERIFICA SE É IMPA
                        if((plantas1 % linhasCalc2) != 0){
                            divplantas = divplantas + restoPlantas;
                            calcParcProd2.setText(divplantas.toString());
                        }else{
                            calcParcProd2.setText(divplantas.toString());
                        }
                    }else{
                        calcParcProd2.setText(divplantas.toString());
                    }
                    */
                    /*calcParcProd2.setText(divplantas.toString());
                    EditText Total = (EditText)(childView.findViewWithTag("edt1parc2"));
                    TotalProd2 = new Double(Total.getText().toString());
                    TotalProdGeral2 += TotalProd2;

                    EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                    TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                    TotalParcelas.setText(TotalProd.toString());
                    TotalProdGeral += TotalProd;
                    */
                }
            }

            if (plantas3 > 0) {
                Integer linhasCalc3 = childCount - 2;
                Integer Linhas = (linhaFinal3 - linhaInicial3) / linhasCalc3;
                Double divHa = hectar3 / linhasCalc3;
                Integer restoPlantas = plantas3 % linhasCalc3;
                Integer divplantas = (plantas3 / linhasCalc3) - restoPlantas;
                for(int j = 2; j < childCount; j++) {
                    View childView = ll.getChildAt(j);
                    EditText calcParcProd3 = (EditText)(childView.findViewWithTag("edt1parc3"));
                    /*
                    if(j == (childCount-1)){
                        //VERIFICA SE É IMPA
                        if((plantas1 % linhasCalc3) != 0){
                            divplantas = divplantas + restoPlantas;
                            calcParcProd3.setText(divplantas.toString());
                        }else{
                            calcParcProd3.setText(divplantas.toString());
                        }
                    }else{
                        calcParcProd3.setText(divplantas.toString());
                    }
                    */
                    /*calcParcProd3.setText(divplantas.toString());
                    EditText Total = (EditText)(childView.findViewWithTag("edt1parc3"));
                    TotalProd3 = new Double(Total.getText().toString());
                    TotalProdGeral3 += TotalProd3;

                    EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                    TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                    TotalParcelas.setText(TotalProd.toString());
                    TotalProdGeral += TotalProd;
                    */
                }
            }

            if (plantas4 > 0) {
                Integer linhasCalc4 = childCount - 2;
                Integer Linhas = (linhaFinal4 - linhaInicial4) / linhasCalc4;
                Double divHa = hectar4 / linhasCalc4;
                Integer restoPlantas = plantas4 % linhasCalc4;
                Integer divplantas = (plantas4 / linhasCalc4) - restoPlantas;
                for(int j = 2; j < childCount; j++) {
                    View childView = ll.getChildAt(j);
                    EditText calcParcProd4 = (EditText)(childView.findViewWithTag("edt1parc4"));
                    /*
                    if(j == (childCount-1)){
                        //VERIFICA SE É IMPA
                        if((plantas1 % linhasCalc4) != 0){
                            divplantas = divplantas + restoPlantas;
                            calcParcProd4.setText(divplantas.toString());
                        }else{
                            calcParcProd4.setText(divplantas.toString());
                        }
                    }else{
                        calcParcProd4.setText(divplantas.toString());
                    }
                    */
                    /*calcParcProd4.setText(divplantas.toString());
                    EditText Total = (EditText)(childView.findViewWithTag("edt1parc4"));
                    TotalProd4 = new Double(Total.getText().toString());
                    TotalProdGeral4 += TotalProd4;

                    EditText TotalParcelas = (EditText)(childView.findViewWithTag("edt1total"));
                    TotalProd = TotalProd1 + TotalProd2 + TotalProd3 + TotalProd4;
                    TotalParcelas.setText(TotalProd.toString());
                    TotalProdGeral += TotalProd;
                    */
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

        Double TotalProd = 0.00, TotalProdGeral = 0.00, TotalProdGeral1 = 0.00, TotalProdGeral2 = 0.00, TotalProdGeral3 = 0.00, TotalProdGeral4 = 0.00;
        Double TotalProd1 = 0.0;
        Double TotalProd2 = 0.0;
        Double TotalProd3 = 0.0;
        Double TotalProd4 = 0.0;

        DecimalFormat ndf = (DecimalFormat) new DecimalFormat("0.00").getInstance(Locale.US);
        //VERIFICA SE A ATIVIDADE NÃO FOR COLHEITA ADICIONA O CAMPO DA PRODUÇÃO EM MÉDIA DE PLANTAS PELA EQUIPE
        for(int i = 2; i < childCount; i++){
            View childView = ll.getChildAt(i);

            EditText Total = (EditText)(childView.findViewWithTag("edt1total"));

            EditText linha = (EditText)(childView.findViewWithTag("edt1n"));

            EditText matricula = (EditText)(childView.findViewWithTag("edt1maticula"));
            String gmatricula = (matricula.getText().toString());

            String matriculaget = database.RetoronoIdMat(gmatricula);
            if(matriculaget == null){
                linha.setBackgroundResource(R.color.vermelho);
                linha.setTextColor(ContextCompat.getColor(getContext(), R.color.yello));
            }else{
                linha.setBackgroundResource(0);
                Toast.makeText(getContext(), "Matricula Ativa: "+matriculaget, Toast.LENGTH_SHORT).show();
                linha.setTextColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
            }

            //recupera os dados das 4 parcelas
            EditText parcProd1 = (EditText)(childView.findViewWithTag("edt1parc1"));
            String gparcProd1 = (parcProd1.getText().toString());

            EditText parcProd2 = (EditText)(childView.findViewWithTag("edt1parc2"));
            String gparcProd2 = (parcProd2.getText().toString());

            EditText parcProd3 = (EditText)(childView.findViewWithTag("edt1parc3"));
            String gparcProd3 = (parcProd3.getText().toString());

            EditText parcProd4 = (EditText)(childView.findViewWithTag("edt1parc4"));
            String gparcProd4 = (parcProd4.getText().toString());

            Double valoresParc = 0.0;
            if(!gparcProd1.equals("") && getpar1 > 0) {
                ContadorParcMDO1 ++;
                valoresParc += new Double(gparcProd1);
                TotalProd1 = new Double(gparcProd1);
                TotalProdGeral1 += TotalProd1;
                TotalProdGeral += TotalProd;
            }
            if(!gparcProd2.equals("")  && getpar2 > 0) {
                ContadorParcMDO2 ++;
                valoresParc += new Double(gparcProd2);
                TotalProd2 = new Double(gparcProd2);
                TotalProdGeral2 += TotalProd2;
                TotalProdGeral += TotalProd;
            }
            if(!gparcProd3.equals("")  && getpar3 > 0) {
                ContadorParcMDO3 ++;
                valoresParc += new Double(gparcProd3);
                TotalProd3 = new Double(gparcProd3);
                TotalProdGeral3 += TotalProd3;
                TotalProdGeral += TotalProd;
            }
            if(!gparcProd4.equals("")  && getpar4 > 0) {
                ContadorParcMDO4 ++;
                valoresParc += new Double(gparcProd4);
                TotalProd4 = new Double(gparcProd4);
                TotalProdGeral4 += TotalProd4;
                TotalProdGeral += TotalProd;
            }
            Total.setText(valoresParc.toString());
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

        for(int i = 2; i < childCount; i++){
            View childView = ll.getChildAt(i);

            EditText Total = (EditText)(childView.findViewWithTag("edt1total"));

            EditText linha = (EditText)(childView.findViewWithTag("edt1n"));
            String glinha = linha.getText().toString();

            EditText matricula = (EditText)(childView.findViewWithTag("edt1maticula"));
            String gmatricula = (matricula.getText().toString());

            //recupera os dados das 4 parcelas
            EditText parcProd1 = (EditText)(childView.findViewWithTag("edt1parc1"));
            String gparcProd1 = (parcProd1.getText().toString());

            EditText parcProd2 = (EditText)(childView.findViewWithTag("edt1parc2"));
            String gparcProd2 = (parcProd2.getText().toString());

            EditText parcProd3 = (EditText)(childView.findViewWithTag("edt1parc3"));
            String gparcProd3 = (parcProd3.getText().toString());

            EditText parcProd4 = (EditText)(childView.findViewWithTag("edt1parc4"));
            String gparcProd4 = (parcProd4.getText().toString());

            if(!gparcProd1.equals("") && getpar1 > 0) {
                try {
                    JSONObject map1 = new JSONObject();
                    map1.put("idparcela", idParcela1);
                    map1.put("atividade", atividade.getSelectedItem().toString());
                    map1.put("cachos",gparcProd1);
                    map1.put("matricula", gmatricula);
                    usersList1.add(map1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ContadorParcMDO1 ++;
            }
            if(!gparcProd2.equals("")  && getpar2 > 0) {
                try {
                    JSONObject map2 = new JSONObject();
                    map2.put("idparcela", idParcela2);
                    map2.put("atividade", atividade.getSelectedItem().toString());
                    map2.put("cachos",gparcProd2);
                    map2.put("matricula", gmatricula);
                    usersList2.add(map2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ContadorParcMDO2 ++;
            }
            if(!gparcProd3.equals("")  && getpar3 > 0) {
                try {
                    JSONObject map3 = new JSONObject();
                    map3.put("idparcela", idParcela3);
                    map3.put("atividade", atividade.getSelectedItem().toString());
                    map3.put("cachos",gparcProd3);
                    map3.put("matricula", gmatricula);
                    usersList3.add(map3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ContadorParcMDO3 ++;
            }
            if(!gparcProd4.equals("")  && getpar4 > 0) {
                try {
                    JSONObject map4 = new JSONObject();
                    map4.put("idparcela", idParcela4);
                    map4.put("atividade", atividade.getSelectedItem().toString());
                    map4.put("cachos",gparcProd4);
                    map4.put("matricula", gmatricula);
                    usersList4.add(map4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ContadorParcMDO4 ++;
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
                    getatividade.setPlantas(divplantas);
                    getatividade.setArea_realizada(divHa1);
                    getatividade.setId_ficha(GetSetCache.getFicha());
                    getatividade.setId_mdo((String) usersList1.get(n).get("matricula"));
                    getatividade.setGerar_apontamento(apontamentoCod + idget);

                    if (parcelaFic > 0) {

                        Long id_apontamento = database.InserirRealizadoApontamento(getatividade);
                        database_backup.InserirRealizadoApontamento(getatividade);
                        //SALVAR O APOIO
                        if (id_apontamento > 0) {
                            returnLinhasAft++;
                            GetSetAtividade colheita1 = new GetSetAtividade();
                            colheita1.setProducao(new Double((String) usersList1.get(n).get("cachos")));
                            colheita1.setPeso(0.00);
                            colheita1.setCaixa(0);
                            colheita1.setId_apontamento(apontamentoCod + idget);
                            database.InserirRealizadoProducaoFito(colheita1);
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList1.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linhaInicial = linhaFinal+1;
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
                    getatividade.setPlantas(divplantas);
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
                            GetSetAtividade colheita1 = new GetSetAtividade();
                            colheita1.setProducao(new Double((String) usersList2.get(n).get("cachos")));
                            colheita1.setPeso(0.00);
                            colheita1.setCaixa(0);
                            colheita1.setId_apontamento(apontamentoCod + idget);
                            database.InserirRealizadoProducaoFito(colheita1);
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList2.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linhaInicial = linhaFinal+1;
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
                    getatividade.setPlantas(divplantas);
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
                            GetSetAtividade colheita1 = new GetSetAtividade();
                            colheita1.setProducao(new Double((String) usersList3.get(n).get("cachos")));
                            colheita1.setPeso(0.00);
                            colheita1.setCaixa(0);
                            colheita1.setId_apontamento(apontamentoCod + idget);
                            database.InserirRealizadoProducaoFito(colheita1);
                            //database_backup.InserirRealizadoColheita(colheita1);
                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList3.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linhaInicial = linhaFinal+1;
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
                    getatividade.setAtividade((String) usersList4.get(n).get("atividade"));
                    getatividade.setLinha_inicial(linhaInicial);
                    getatividade.setLinha_final(linhaFinal);
                    getatividade.setPlantas(divplantas);
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
                            GetSetAtividade colheita1 = new GetSetAtividade();
                            colheita1.setProducao(new Double((String) usersList4.get(n).get("cachos")));
                            colheita1.setPeso(0.00);
                            colheita1.setCaixa(0);
                            colheita1.setId_apontamento(apontamentoCod + idget);
                            database.InserirRealizadoProducaoFito(colheita1);
                            //database_backup.InserirRealizadoColheita(colheita1);

                            //alerta("Salvando...", "Apontamento: " + id_apontamento + " Cachos: " + new Integer((String) usersList4.get(n).get("cachos")) + " salvo com sucesso");
                        } else {
                            alerta("Erro...", "Falha no apontamento");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linhaInicial = linhaFinal+1;
                linhaFinal = linhaFinal + Linhas4;
            }
        }
        return returnLinhasAft++;
    }
    public int salvarOAtividades(){
        parcela2.setSelection(1);
        String parcelaView = parcela2.getSelectedItem().toString();
        ArrayList<HashMap<String, String>> inventarioGetParc = BuscaParcela(GetSetCache.getFazendaGet(), parcelaView, 0, 0);
        idParcelaAtividade = (new Integer(inventarioGetParc.get(0).get("idParcela")));

        int childCount = ll.getChildCount();
        int returnLinhasAft = 0;
        Integer getpar1 = idParcelaAtividade;

        Integer ContadorParcMDO1 = 0;
        GetSetAtividade getatividade = new GetSetAtividade();

        //GERAR NÚMEROS ALEATÓRIOS
        Random gerador = new Random();
        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
        Integer aleatNumber = 0;
        for (int r = 0; r < 1; r++) {
            aleatNumber = gerador.nextInt(99);
        }
        final long date = System.currentTimeMillis();
        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMdhms");
        String dateStringToken = sdfToken.format(date);

        String cod = dateStringToken + GetSetUsuario.getMatricula().replace("1120","")+aleatNumber;
        String apontamentoCod = cod;

        for(int i = 2; i < childCount; i++){
            View childView = ll.getChildAt(i);

            EditText Total = (EditText) (childView.findViewWithTag("edt1total"));
            EditText linha = (EditText) (childView.findViewWithTag("edt1n"));
            String glinha = linha.getText().toString();

            EditText matricula = (EditText) (childView.findViewWithTag("edt1maticula"));
            String gmatricula = (matricula.getText().toString());

            //VERIFICA SE A MATRICULA ESTÁ COMPLETA
            if (gmatricula.length() > 4 && !atividade.getSelectedItem().toString().equals("SELECIONE UMA ATIVIDADE")) {
                Database_syspalma database = new Database_syspalma(getContext());
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
                //SALVAR O APOIO
                if (id_apontamento > 0) {
                    returnLinhasAft++;
                } else {
                    alerta("Erro...", "Falha no apontamento");
                }
            }

        }
        return returnLinhasAft++;
    }
    public void limparCampos(){
        int childCount = ll.getChildCount();
        for(int i = 2; i < childCount; i++) {
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

            parcProd1.setText("");
            parcProd2.setText("");
            parcProd3.setText("");
            parcProd4.setText("");
        }
    }
}
