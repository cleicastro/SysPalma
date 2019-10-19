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

public class MenuOperacoesEPI extends AppCompatActivity {

    String Arraymenu[] = {"REQUISIÇÕES DE EPI", "MAPA DE DISTRIBUIÇÃO", "ALMOXARIFADO", "CONFIGURAÇÕES"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_operacoes);
        final int MILISEGUNDOS = 1000;
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circlecolheita);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_diagnoses, R.drawable.ic_apps)
                .addSubMenu(Color.parseColor("#FFD700"), R.drawable.ic_mail_outline_black_24dp)
                .addSubMenu(Color.parseColor("#32CD32"), R.drawable.ic_people_black_24dp)
                .addSubMenu(Color.parseColor("#7B68EE"), R.drawable.ic_account_balance_black_24dp)
                .addSubMenu(Color.parseColor("#8B4513"), R.drawable.ic_settings_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index) {
                            case 0:

                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesEPI.this,EPI_requisicao.class);
                                        GetSetCache.setFrota(true);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 1:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesEPI.this,EPIMapa.class);
                                        GetSetCache.setFrota(true);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 2:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        if(GetSetUsuario.getTipo().equals("Padrão")){
                                            Toast.makeText(MenuOperacoesEPI.this, "Você não tem permissão para acessar esse módulo!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Intent intent = new Intent(MenuOperacoesEPI.this,SysPalmaOnlineOS.class);
                                            intent.putExtra("almoxarifado","almoxarifado");
                                            startActivity(intent);
                                        }

                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 3:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesEPI.this,Configuracao.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                        }
                        Toast.makeText(MenuOperacoesEPI.this, "Você selecionou " + Arraymenu[index], Toast.LENGTH_SHORT).show();
                    }
                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GetSetCache.setFrota(false);
    }
}
