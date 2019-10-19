package com.ryatec.syspalma;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ryatec.syspalma.R;

import java.util.List;

public class ActvityDiego extends AppCompatActivity {
    private Spinner fazenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actvity_diego);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fazenda = (Spinner) findViewById(R.id.fazenda);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        carregar_valores();
    }
    private void carregar_valores(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> fazendas = db.SelectFazendas();

        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fazendas);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fazenda.setAdapter(dataAdapter5);
    }

}
