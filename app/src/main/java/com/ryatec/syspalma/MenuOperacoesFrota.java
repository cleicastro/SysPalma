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
import com.ryatec.syspalma.PointCaixa.AtividadeCaixa;

public class MenuOperacoesFrota extends AppCompatActivity {

    String Arraymenu[] = {"ABASTECIMENTO", "ATIVIDADES", "CAIXAS", "OFÍCINA", "CONFIGURAÇÕES"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_operacoes);
        final int MILISEGUNDOS = 1000;
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circlecolheita);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_car, R.drawable.ic_caminhao_frota)
                .addSubMenu(Color.parseColor("#FFD700"), R.drawable.ic_abastecimento)
                .addSubMenu(Color.parseColor("#32CD32"), R.drawable.ic_atividade)
                .addSubMenu(Color.parseColor("#FF7F50"), R.drawable.ic_location_on_black_24dp)
                .addSubMenu(Color.parseColor("#7B68EE"), R.drawable.ic_oficina)
                .addSubMenu(Color.parseColor("#8B4513"), R.drawable.ic_settings_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index) {
                            case 0:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesFrota.this,PainelAbastecimento.class);
                                        GetSetCache.setFrota(true);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 1:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesFrota.this,MainFichaFrota.class);
                                        GetSetCache.setFrota(true);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 2:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesFrota.this, AtividadeCaixa.class);
                                        GetSetCache.setFrota(true);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 3:
                                Toast.makeText(MenuOperacoesFrota.this, "Sistema em manuteção!", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoesFrota.this,Configuracao.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                        }
                        Toast.makeText(MenuOperacoesFrota.this, "Você selecionou " + Arraymenu[index], Toast.LENGTH_SHORT).show();
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
