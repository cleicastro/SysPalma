package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainAbastecimentosTanque extends AppCompatActivity {
    private ListView myListView;
    TextView litros_item, item_1, item_2, tanque, litros_item2, tanque2, cupom1, cupom2, tanq_get2, tanq_get1, posto_interno_1, posto_interno_2;
    ImageView img_tanque1, img_tanque2;
    private Double tanque_medio, tanque_abaixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litros_tanque);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getStringExtra("posto").equals("TANQUE")){
            tanque_medio = 1000.00;
            tanque_abaixo = 500.00;
        }else{
            tanque_medio = 2000.00;
            tanque_abaixo = 1000.00;
        }

        tanque();
        findViewById(R.id.posto_comboio2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView cupom = (TextView) findViewById(R.id.cupom_2);
                TextView item = (TextView) findViewById(R.id.item_2);
                TextView fornecedor = (TextView) findViewById(R.id.posto_interno_2);
                TextView tanq_reque = findViewById(R.id.taq_get_2);
                if(cupom.getText().toString() != "") {
                    Intent intent = new Intent(getApplication(), Frota_Abastecimento_interno.class);
                    intent.putExtra("cupom_fiscal",cupom.getText().toString());
                    intent.putExtra("item",item.getText().toString());
                    intent.putExtra("fornecedor",fornecedor.getText().toString());
                    intent.putExtra("tanque",tanq_reque.getText().toString());
                    startActivity(intent);
                }else{
                    Mensagem("Atenção","Favor, crie uma ficha de serviço");
                }
            }
        });
        findViewById(R.id.lista_abastecimento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AbastecimentosFrota.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.novo_comboio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Frota_Abastecimento_tanque.class);
                startActivity(intent);
            }
        });

        //chamarLista();
        findViewById(R.id.posto_comboio1).setOnLongClickListener(new MyOnLongClickListener());
        findViewById(R.id.topright).setOnDragListener(new MyOnDragListener(1));
    }

    public void onResume() {
        super.onResume();

        tanq_get2 = findViewById(R.id.taq_get_1);
        if(tanq_get2.getText().toString().equals("-") && getIntent().getStringExtra("posto").equals("TANQUE")){
            findViewById(R.id.novo_comboio).setVisibility(View.VISIBLE);
            findViewById(R.id.posto_comboio1).setVisibility(View.GONE);
        }else{
            //findViewById(R.id.posto_comboio2).setVisibility(View.GONE);
            //findViewById(R.id.posto_comboio1).setVisibility(View.GONE);
            findViewById(R.id.novo_comboio).setVisibility(View.GONE);
        }
        tanque();
    }

    public void chamarLista(){
        Database_syspalma database = new Database_syspalma(this);

        ListAdapter adapter = new AdapterCursosPersonalizado(this,database.ListaComboio());
        myListView.setAdapter(adapter);
    }

    public void Mensagem(String Titulo, String Msg){
        android.app.AlertDialog.Builder Alerta =
                new android.app.AlertDialog.Builder(this);
        Alerta.setTitle(Titulo);
        Alerta.setMessage(Msg);
        Alerta.setNeutralButton("OK", null);
        Alerta.show();
    }
    private void alerta_questao(String titulo, String msg, final View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Database_syspalma db = new Database_syspalma(getApplication());
                ///db.deletarSync(GetSetCache.getIdPlanejamento());

                cupom2 = findViewById(R.id.cupom_1);
                cupom1 = findViewById(R.id.cupom_2);
                litros_item = findViewById(R.id.tanque_2);
                tanque2 = findViewById(R.id.taq_get_1);
                tanq_get2 = findViewById(R.id.taq_get_1);
                tanq_get1 = findViewById(R.id.taq_get_2);
                TextView posto_interno_get = findViewById(R.id.posto_interno_1);
                TextView item_interno_get = findViewById(R.id.item_1);

                Double valor1 = new Double(tanq_get1.getText().toString());
                Double valor2 = new Double(tanq_get2.getText().toString());

                Double litros_tanq = valor1+valor2;
                //litros_item.setText(litros_tanq.toString()+" Litros no tanque");
                Toast.makeText(MainAbastecimentosTanque.this,"Litros: "+litros_tanq, Toast.LENGTH_SHORT).show();
                db.UpdTanque(litros_tanq, cupom1.getText().toString(), cupom2.getText().toString());

                int idpatrimonio = db.RetoronoIdSelect(posto_interno_get.getText().toString(), "c_patrimonio", "idpatrimonio");
                int idinsumo = db.RetoronoIdSelect(item_interno_get.getText().toString(), "c_insumo", "idinsumo");

                salvar(idpatrimonio, idinsumo, cupom1.getText().toString(), valor2);
                //view.setVisibility(View.GONE);
                recreate();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        android.app.AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }
    public void salvar(int patrimonio, int insumo, String Cupom, Double litros){
        Database_syspalma database = new Database_syspalma(this);

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data_atual = new Date();
        String dateFormatada = dt.format(data_atual);

        final long date = System.currentTimeMillis();
        SimpleDateFormat sdfToken = new SimpleDateFormat("yyMMddhhmmss");
        String dateStringToken = sdfToken.format(date);

        String cod_abastecimento = dateStringToken + GetSetUsuario.getMatricula().replace("1120","");

        //INSERIR ABASTECIMENTO

        GetSetCompra compra = new GetSetCompra();
        compra.setData(dateFormatada);
        compra.setId_fornecedor(patrimonio);
        compra.setMdo_motorista(GetSetUsuario.getMatricula());
        compra.setId_patrimonio(patrimonio);
        compra.setKm("0.00");
        compra.setCupom_fiscal(Cupom);
        compra.setId_mdo(GetSetUsuario.getMatricula());
        compra.setId_insumo(insumo);
        compra.setQuantidade(litros);
        compra.setCod(cod_abastecimento);

        database.InserirAbastecimentoInterno(compra);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void tanque(){
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();
        String selectQuery = null;
        if(getIntent().getStringExtra("posto").equals("TANQUE")){
            selectQuery =
                "SELECT ins.descricao, quantidade, valor_unit, p.descricao, tanque, cupom_fiscal FROM tanque_interno rc \n"+
                    "INNER JOIN c_insumo ins \n"+
                    "ON ins.idinsumo = rc.id_insumo \n"+
                    "INNER JOIN c_patrimonio p \n"+
                    "ON p.idpatrimonio = rc.id_patrimonio \n"+
                    "WHERE tanque = (SELECT MAX(tanque) FROM tanque_interno WHERE id_patrimonio = rc.id_patrimonio AND cupom_fiscal = rc.cupom_fiscal) AND p.tipo = 'TANQUE' ORDER BY tanque DESC LIMIT 1\n";
        }else{
            selectQuery =
                "SELECT ins.descricao, quantidade, valor_unit, p.descricao, tanque, cupom_fiscal FROM tanque_interno rc \n"+
                    "INNER JOIN c_insumo ins \n"+
                    "ON ins.idinsumo = rc.id_insumo \n"+
                    "INNER JOIN c_patrimonio p \n"+
                    "ON p.idpatrimonio = rc.id_patrimonio \n"+
                    "WHERE tanque = (SELECT MAX(tanque) FROM tanque_interno WHERE id_patrimonio = rc.id_patrimonio AND cupom_fiscal = rc.cupom_fiscal) AND p.tipo = 'COMBOIO' ORDER BY tanque DESC LIMIT 1\n";
        }
        Cursor cursor = database.rawQuery(selectQuery, null);
        Integer n = 0;
        if (cursor.moveToFirst()) {
            Double total = cursor.getDouble(2) * cursor.getDouble(1);
            DecimalFormat df = new DecimalFormat("0.000");
            String dx = df.format(total);
            Double tanqueAtual = cursor.getDouble(4);

            cupom1 = findViewById(R.id.cupom_2);
            litros_item = findViewById(R.id.tanque_2);
            tanque = findViewById(R.id.id_tanque_2);
            img_tanque1 = findViewById(R.id.img_tanque_2);
            tanq_get1 = findViewById(R.id.taq_get_2);
            item_1 = findViewById(R.id.item_2);
            posto_interno_1 = findViewById(R.id.posto_interno_2);

            cupom1.setText(cursor.getString(5));
            item_1.setText(cursor.getString(0));
            litros_item.setText(tanqueAtual.toString() + " Litros no tanque");
            posto_interno_1.setText(cursor.getString(3));
            tanque.setText(cursor.getString(1)+" Litros abastecidos\n"+"R$ "+dx);
            tanq_get1.setText(tanqueAtual.toString());

            if(tanqueAtual < tanque_abaixo){
                img_tanque1.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_vermelho));
            }else if(tanqueAtual > tanque_medio && tanqueAtual < tanque_abaixo ){
                img_tanque1.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_amarelo));
            }else{
                img_tanque1.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_verde));
            }

            //findViewById(R.id.posto_comboio2).setVisibility(View.VISIBLE);
            //findViewById(R.id.posto_comboio1).setVisibility(View.GONE);

            String selectQuery2 = null;
            if(getIntent().getStringExtra("posto").equals("TANQUE")){
                selectQuery2 =
                    "SELECT ins.descricao, quantidade, valor_unit, p.descricao, tanque, cupom_fiscal FROM tanque_interno rc \n"+
                        "INNER JOIN c_insumo ins \n"+
                        "ON ins.idinsumo = rc.id_insumo \n"+
                        "INNER JOIN c_patrimonio p \n"+
                        "ON p.idpatrimonio = rc.id_patrimonio \n"+
                        "WHERE cupom_fiscal != '"+cursor.getString(5)+"' AND p.tipo = 'TANQUE'\n" +
                        "AND tanque = (SELECT MIN(tanque) FROM tanque_interno WHERE id_patrimonio = rc.id_patrimonio AND cupom_fiscal = rc.cupom_fiscal)  ORDER BY tanque DESC LIMIT 1";
            }else{
                selectQuery2 =
                    "SELECT ins.descricao, quantidade, valor_unit, p.descricao, tanque, cupom_fiscal FROM tanque_interno rc \n"+
                        "INNER JOIN c_insumo ins \n"+
                        "ON ins.idinsumo = rc.id_insumo \n"+
                        "INNER JOIN c_patrimonio p \n"+
                        "ON p.idpatrimonio = rc.id_patrimonio \n"+
                        "WHERE cupom_fiscal != '"+cursor.getString(5)+"' AND p.tipo = 'COMBOIO'\n" +
                        "AND tanque = (SELECT MIN(tanque) FROM tanque_interno WHERE id_patrimonio = rc.id_patrimonio AND cupom_fiscal = rc.cupom_fiscal)  ORDER BY tanque DESC LIMIT 1";
            }
            Cursor cursor2 = database.rawQuery(selectQuery2, null);
            if (cursor2.moveToFirst()) {
                Double total2 = cursor2.getDouble(2) * cursor2.getDouble(1);
                DecimalFormat df2 = new DecimalFormat("0.000");
                String dx2 = df2.format(total2);
                Double tanqueAtual2 = cursor2.getDouble(4);

                cupom2 = findViewById(R.id.cupom_1);
                item_2 = findViewById(R.id.item_1);
                litros_item2 = findViewById(R.id.tanque_1);
                tanque2 = findViewById(R.id.id_tanque_1);
                img_tanque2 = findViewById(R.id.img_tanque_1);
                tanq_get2 = findViewById(R.id.taq_get_1);
                posto_interno_2 = findViewById(R.id.posto_interno_1);

                cupom2.setText(cursor2.getString(5));
                item_2.setText(cursor2.getString(0));
                litros_item2.setText(tanqueAtual2.toString() + " Litros no tanque");
                posto_interno_2.setText(cursor2.getString(3));
                tanque2.setText(cursor2.getString(1)+" Litros abastecidos\n"+"R$ "+dx2);
                tanq_get2.setText(tanqueAtual2.toString());

                if(tanqueAtual2 < tanque_abaixo){
                    img_tanque2.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_vermelho));
                }else if(tanqueAtual2 > tanque_medio && tanqueAtual2 < tanque_abaixo ){
                    img_tanque2.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_amarelo));
                }else{
                    img_tanque2.setBackground(getResources().getDrawable(R.drawable.ic_abastecimento_verde));
                }
                if(cursor2.getDouble(4) <= 0.0){
                    findViewById(R.id.posto_comboio1).setVisibility(View.GONE);
                }
            }else{
                findViewById(R.id.posto_comboio1).setVisibility(View.GONE);
                if(getIntent().getStringExtra("posto").equals("TANQUE")){
                    findViewById(R.id.novo_comboio).setVisibility(View.VISIBLE);
                }
            }
            cursor2.close();
        }else{
            findViewById(R.id.posto_comboio2).setVisibility(View.GONE);
            findViewById(R.id.posto_comboio1).setVisibility(View.GONE);
            if(getIntent().getStringExtra("posto").equals("TANQUE")){
                findViewById(R.id.novo_comboio).setVisibility(View.VISIBLE);
            }
        }
        cursor.close();
        database.close();
    }

    public class AdapterCursosPersonalizado extends BaseAdapter {
        ArrayList<HashMap<String, String>> list;
        Activity a;
        LayoutInflater myiflater;
        public AdapterCursosPersonalizado(Activity activity, ArrayList<HashMap<String, String>> list) {
            this.a=activity;
            this.list=list;
            myiflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = a.getLayoutInflater()
                    .inflate(R.layout.fragment_resumo_ficha, viewGroup, false);

            final HashMap<String, String> map = list.get(i);
            Database_syspalma db = new Database_syspalma(a);

            TextView apontamento = (TextView) view.findViewById(R.id.ficha);
            TextView resp = (TextView) view.findViewById(R.id.resp_op);
            TextView data = (TextView) view.findViewById(R.id.data);
            TextView km_desc = (TextView) view.findViewById(R.id.resp_tec);
            ImageView icone = view.findViewById(R.id.imageView_geral);

            Double tanque = new Double(map.get("quantidade"));

            apontamento.setText(map.get("id"));
            km_desc.setText(tanque.toString() + " Litros no tanque");
            data.setText(map.get("item"));
            resp.setText(map.get("quantidade")+" Total de Litros");
            String valor = map.get("valor");

            if(tanque < 50){
                view.setBackgroundColor(getResources().getColor(R.color.vermelho));
                icone.setImageResource(R.drawable.ic_abastecimento_amarelo);
            }else if(tanque > 100 && tanque < 500 ){
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                icone.setImageResource(R.drawable.ic_abastecimento_vermelho);
            }else{
                icone.setImageResource(R.drawable.ic_abastecimento_verde);
            }

            return view;
        }
    }
    class MyOnLongClickListener implements View.OnLongClickListener {
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("simple_text","Texto teste");
            View.DragShadowBuilder sb = new View.DragShadowBuilder(v);
            v.startDrag(data, sb, v, 0);
            //v.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    class MyOnDragListener implements View.OnDragListener {
        private int num;
        public MyOnDragListener (int num){
            super();
            this.num = num;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i("Script",num +" - ACTION_DRAG_ENTERED");
                        v.setBackgroundColor(Color.GREEN);
                        break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.i("Script",num +" - ACTION_DRAG_LOCATION");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Script",num +" - ACTION_DRAG_EXITED");
                    v.setBackgroundColor(Color.WHITE);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("Script",num +" - ACTION_DROP");

                    View view = (View) event.getLocalState();
                    ViewGroup parent = (ViewGroup) view.getParent();
                    parent.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    alerta_questao("Tanque","Você realmente deseja transferir estes litros?",view);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("Script",num +" - ACTION_DRAG_ENDED");
                    v.setBackgroundColor(Color.LTGRAY);
                    break;
            }
            return true;
        }
    }
}
