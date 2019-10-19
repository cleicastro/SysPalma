package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ryatec.syspalma.R;

import java.util.ArrayList;

public class ExpansedListiview extends BaseExpandableListAdapter {
    private ArrayList<String> listCategoria;
    private Context context;

    public ExpansedListiview(ArrayList<String> listCategoria,  Context context) {
        this.listCategoria = listCategoria;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return listCategoria.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return listCategoria.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String tituloCategoria = (String) getGroup(i);
        view = LayoutInflater.from(context).inflate(R.layout.layout_atividades, null);
        TextView textView = view.findViewById(R.id.atividades);

        if (b){
            textView.setBackgroundColor(Color.parseColor("#FFDFDEDE"));
        }else{
            textView.setBackgroundResource(android.R.color.transparent);
        }

        textView.setText(tituloCategoria);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String item = (String) getGroup(i);

        view = LayoutInflater.from(context).inflate(R.layout.layout_apontamento, null);



        switch (i){
            case 0:

                break;
            case 1:
                //view = LayoutInflater.from(context).inflate(R.layout.layout_colheita, null);

                break;
            case 2:
                //view = LayoutInflater.from(context).inflate(R.layout.layout_patrimonio, null);

                break;
            case 3:
                //view = LayoutInflater.from(context).inflate(R.layout.layout_insumo, null);
                break;
        }

        //view = LayoutInflater.from(context).inflate(R.layout.layout_apontamento, null);
        //Toast.makeText(context, "Novo"+item, Toast.LENGTH_SHORT).show();
        //TextView textView = view.findViewById(R.id.header);
        //textView.setText("TESTANDO");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
