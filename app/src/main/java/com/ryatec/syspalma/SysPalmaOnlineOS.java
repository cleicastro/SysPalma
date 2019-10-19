package com.ryatec.syspalma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class SysPalmaOnlineOS extends AppCompatActivity {
    private WebView wv;
    private String url = "http://adm.syspalma.com/webservice/getfichafiscal?";
    private String url_mapa = "http://adm.syspalma.com/webservice/mapa_producao?";
    private String urlEPIAlmoxarifado = "http://adm.syspalma.com/EPI/almoxarifado";
    private String data_inicial, data_final, atividade, almoxarifado;
    private static String FISCAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichade_servico);
        wv = (WebView) findViewById(R.id.web);
        data_inicial =  getIntent().getStringExtra("data_inicial");
        data_final =  getIntent().getStringExtra("data_final");
        atividade =  getIntent().getStringExtra("atividade");
        almoxarifado = getIntent().getStringExtra("almoxarifado");

        if(GetSetUsuario.getMatricula().equals("1120237")){
            FISCAL = "1120061";
        }else{
            FISCAL = GetSetUsuario.getMatricula();
        }

        WebSettings ws = wv.getSettings();
        //ZOOM NA PÁGINA
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        if(atividade != null){
            wv.loadUrl(url_mapa+"data_inicial="+data_inicial+"&data_final="+data_final+"&fiscal="+FISCAL+"&atividade="+atividade);
        }else if(almoxarifado != null){
            wv.loadUrl(urlEPIAlmoxarifado);
        }
        else{
            wv.loadUrl(url+"data_inicial="+data_inicial+"&data_final="+data_final+"&fiscal="+FISCAL);
        }
        wv.setWebViewClient(new SysPalmaOnlineOS.WebViewClient());
    }
    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
