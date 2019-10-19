package com.ryatec.syspalma;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ryatec.syspalma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApontamentoEstatistica extends Fragment {

    final static String myBlogAddr = "https://chart.googleapis.com/chart?" +
            "cht=p3&" + //define o tipo do gráfico "linha"
            "chxt=x,y&" + //imprime os valores dos eixos X, Y
            "chs=500x400&" + //define o tamanho da imagem
            "chd=t:10.4,9.2,12.4&" + //valor de cada coluna do gráfico
            "chdl= B5|B3|C2&" + //Dados do grafico
            "chtt=Hectares/Colhidos";  //cabeçalho do gráfico

    String myUrl;

    private TextView mdo;
    private TextView ha;
    private TextView plantas;
    private TextView faltas;

    public ApontamentoEstatistica() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estatistica, container, false);

        /*
        WebView myWebView = (WebView) view.findViewById(R.id.mywebview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        if(myUrl == null){
            myUrl = myBlogAddr;
        }
        myWebView.loadUrl(myUrl);
        */
        mdo = view.findViewById(R.id.esMdo);
        ha = view.findViewById(R.id.esha);
        plantas = view.findViewById(R.id.esplantas);
        faltas = view.findViewById(R.id.esfalta);
        return view;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            myUrl = url;
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        estatisticas();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setRetainInstance(true);
    }

    private void estatisticas(){

        Database_syspalma Banco = new Database_syspalma(getContext());
        SQLiteDatabase database = Banco.getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT COUNT(DISTINCT(matricula_mdo)) as mdo, COUNT(DISTINCT(id_parcela)) as parcela, SUM(DISTINCT(area_realizada)) as ha, SUM(plantas) as plantas,");
        sql.append(" (SELECT COUNT(atividade) FROM realizado_apontamento WHERE ficha = '" + GetSetCache.getFicha() + "' AND atividade LIKE '%FALTA%') as faltas");
        sql.append(" FROM realizado_apontamento");
        sql.append(" WHERE ficha = '" + GetSetCache.getFicha() + "'");

        Cursor c = database.rawQuery(sql.toString(), null);
        if(c.getCount() > 0){

            c.moveToFirst();
            if(new Integer(c.getString(0)) > 0) {
                mdo.setText(c.getString(0).toString());
                ha.setText(c.getString(2).toString());
                plantas.setText(c.getString(3).toString());
                faltas.setText(c.getString(4).toString());
            }else{
                mdo.setText("0");
                ha.setText("0");
                plantas.setText("0");
                faltas.setText("0");
            }
        }
        database.close();
    }

}
