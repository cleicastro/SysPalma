package com.ryatec.syspalma;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.ryatec.syspalma.R;

public class MenuOperacoesFitossanidade extends AppCompatActivity {

    String Arraymenu[] = {"ATIVIDADES", "ONLINE",  "CONFIGURAÇÕES"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_operacoes);
        final int MILISEGUNDOS = 1000;
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circlecolheita);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_bug, R.drawable.ic_envira)
                .addSubMenu(Color.parseColor("#32CD32"), R.drawable.ic_bug)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_public_black_24dp)
                .addSubMenu(Color.parseColor("#8B4513"), R.drawable.ic_settings_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index) {
                            case 0:
                                Intent intent = new Intent(MenuOperacoesFitossanidade.this,Fito.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent2 = new Intent(MenuOperacoesFitossanidade.this,PainelProducaoOline.class);
                                startActivity(intent2);
                                break;
                            case 2:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesFitossanidade.this,Configuracao.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                        }
                        Toast.makeText(MenuOperacoesFitossanidade.this, "Você selecionou " + Arraymenu[index], Toast.LENGTH_SHORT).show();
                    }
                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {

            }

            @Override
            public void onMenuClosed() {}

        });
    }
}
