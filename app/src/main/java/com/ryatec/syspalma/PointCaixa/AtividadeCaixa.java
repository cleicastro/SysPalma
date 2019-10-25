package com.ryatec.syspalma.PointCaixa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ryatec.syspalma.Database_syspalma;
import com.ryatec.syspalma.Database_syspalma_backup;
import com.ryatec.syspalma.GetSetUsuario;
import com.ryatec.syspalma.PainelAbastecimento;
import com.ryatec.syspalma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AtividadeCaixa extends Activity {
    private Spinner patrimonio;
    private Spinner origem;
    private EditText caixa, longitude, latitude;
    private TableLayout ll;
    private Button salvarApontamento;
    private FusedLocationProviderClient client;
    private Double cordLatitude;
    private Double cordLongitude;
    private FloatingActionButton dadosSalvos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_caixa);
        //myListView = (Spinner) findViewById(R.id.lista_rt);
        //salvar = (Button) findViewById(R.id.salvarFicha);

        Database_syspalma db = new Database_syspalma(this);
        List<String> resp = db.SelectRT();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, resp);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //myListView.setAdapter(dataAdapter);
        caixa = findViewById(R.id.caixa);
        latitude = findViewById(R.id.textLatitude);
        longitude = findViewById(R.id.txtLongitude);
        patrimonio = (Spinner) findViewById(R.id.spinner_patrimonio);
        origem = (Spinner) findViewById(R.id.origem);
        dadosSalvos = findViewById(R.id.AllPoints);

        client = LocationServices.getFusedLocationProviderClient(this);
        pedirPermissoes();
        Button cancelar = (Button) findViewById(R.id.cancelar);
        salvarApontamento = (Button) findViewById(R.id.salvar);

        salvarApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patrimonio.getSelectedItemPosition() > 0 &&
                        caixa.getText().toString().length() > 0){
                    salvar();
                }else{
                    exemplo_alerta_erro("Erro","Favor, verificar a atividade, o patrimônio ou a quilometragem!");
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dadosSalvos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AtividadeCaixa.this, PontosList.class);
                startActivity(intent);
            }
        });

        carregar_valores();
    }

    public void carregar_valores(){
        Database_syspalma db = new Database_syspalma(this);
        List<String> patrimonios = db.SelectPatrimonio("'COMBOIO','ÔNIBUS', 'TOCO/SEMI-PESADO', 'TRUCK/PESADO', 'CARRETA 3 EIXOS', 'MOTO', 'AUTOMÓVEL','CAMINHONETE','CARRO','ROÇADEIRA COSTAL'");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, patrimonios);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patrimonio.setAdapter(dataAdapter2);

        List<String> fazendas = db.SelectFazendas();
        fazendas.add("Base SDC");
        fazendas.add("Base MDR");
        fazendas.add("Base Apoio(km 14)");
        fazendas.add("Industria");
        fazendas.add("Outros");
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fazendas);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        origem.setAdapter(dataAdapter5);
    }
    public void salvar(){
        pedirPermissoes();
        Database_syspalma database = new Database_syspalma(this);
        Database_syspalma_backup database_backup = new Database_syspalma_backup(this);

        //GERAR NÚMEROS ALEATÓRIOS
        Random gerador = new Random();
        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
        Integer aleatNumber = 0;
        for (int i = 0; i < 1; i++) {
            aleatNumber = gerador.nextInt(99);
        }

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dataFicha = new Date();
        String dateLancamento = dt.format(dataFicha);

        SimpleDateFormat sdf_hora = new SimpleDateFormat("HH:mm:ss",  Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dmyHHmmss",  Locale.getDefault());
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        String dataFormatada = sdf.format(hora)+aleatNumber;
        String apontamentoCod = GetSetUsuario.getMatricula()+dataFormatada;

        SimpleDateFormat dtficha = new SimpleDateFormat("dMMy");
        Date dataFichaNovo = new Date();
        String fichaFormatada = GetSetUsuario.getMatricula()+dtficha.format(dataFichaNovo);

        GetSetPointCaixa getpatrimonio_realizado = new GetSetPointCaixa();
        getpatrimonio_realizado.setData(dateLancamento);
        getpatrimonio_realizado.setCaixa(Integer.parseInt(caixa.getText().toString()));
        getpatrimonio_realizado.setLocal(origem.getSelectedItem().toString());
        getpatrimonio_realizado.setMatricula(GetSetUsuario.getMatricula());
        getpatrimonio_realizado.setLatitude(cordLatitude);
        getpatrimonio_realizado.setLongitude(cordLongitude);
        getpatrimonio_realizado.setId_apontamento(apontamentoCod);

        int idpatr = database.RetoronoIdPatrimonio(patrimonio.getSelectedItem().toString());
        getpatrimonio_realizado.setId_patrimonio(idpatr);

        database.InserirApontamentoCaixa(getpatrimonio_realizado);
        exemplo_alerta("Salvando...","Apontamentos salvos");
    }

    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            //configurarServico();
            client.getLastLocation().addOnSuccessListener(AtividadeCaixa.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        cordLatitude = location.getLatitude();
                        cordLongitude = location.getLongitude();
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                    }
                }
            });
        }

    }
    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                public void onProviderEnabled(String provider) { }
                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void atualizar(Location location)
    {
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();
        Toast.makeText(this, "Pontos "+latPoint.toString()+" | "+lngPoint.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Pontos",latPoint.toString()+" | "+lngPoint.toString());
    }

    private void exemplo_alerta(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //finish();
                Intent intent = new Intent(AtividadeCaixa.this, PontosList.class);
                startActivity(intent);
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

    private void exemplo_alerta_erro(String titulo, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle(titulo);//define o titulo
        builder.setMessage(msg);//define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //finish();
            }
        });
        AlertDialog alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

}
