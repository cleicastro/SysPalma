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

public class MenuOperacoes extends AppCompatActivity {

    String Arraymenu[] = {"PLANTIO", "TRATOS CULTURAIS", "COLHEITA", "CONFIGURAÇÕES"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_operacoes);
        final int MILISEGUNDOS = 1000;
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circlecolheita);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_agricola, R.drawable.ic_envira)
                .addSubMenu(Color.parseColor("#FFD700"), R.drawable.ic_pied_piper_hat)
                .addSubMenu(Color.parseColor("#32CD32"), R.drawable.ic_pagelines)
                .addSubMenu(Color.parseColor("#7B68EE"), R.drawable.ic_colheita_branco)
                .addSubMenu(Color.parseColor("#8B4513"), R.drawable.ic_settings_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index) {
                            case 0:
                                Toast.makeText(MenuOperacoes.this, "Sistema em manuteção!", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoes.this,Tratos.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 2:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoes.this,Colheita.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                            case 3:
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MenuOperacoes.this,Configuracao.class);
                                        startActivity(intent);
                                    }
                                }, MILISEGUNDOS);
                                break;
                        }
                        Toast.makeText(MenuOperacoes.this, "Você selecionou " + Arraymenu[index], Toast.LENGTH_SHORT).show();
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
