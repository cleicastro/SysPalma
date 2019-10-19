package com.ryatec.syspalma;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class FichadeServico extends AppCompatActivity {

    private WebView wv;
    private String url = "http://adm.syspalma.com/webservice/ficha_producao?";
    private String data_inicial, data_final;
    private ArrayList matriculas;
    private String urlMatricula = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichade_servico);

        wv = (WebView) findViewById(R.id.web);
        data_inicial =  getIntent().getStringExtra("data_inicial");
        data_final =  getIntent().getStringExtra("data_final");
        matriculas =  getIntent().getStringArrayListExtra("matriculas");

        WebSettings ws = wv.getSettings();
        //ZOOM NA PÁGINA
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        for (int i=0; i < matriculas.size(); i++){
            urlMatricula += "&matricula%5B%5D="+matriculas.get(i);
        }

        wv.loadUrl(url+"data_inicial="+data_inicial+"&data_final="+data_final+urlMatricula);
        wv.setWebViewClient(new WebViewClient());
        /*
        wv.setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType,
                                        long size)
            {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(Uri.parse(url), mimeType);
                try
                {
                    startActivity(viewIntent);
                }
                catch (ActivityNotFoundException ex)
                {
                }
            }
        });
        */
        /*
        File file = new File(url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("pdf/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST);
        */

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