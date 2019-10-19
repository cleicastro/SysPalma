package com.ryatec.syspalma;

/**
 * Created by hp on 12/23/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;


public class imprimir extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static String cod;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    private static String versionName = null;

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.impressora_main);
        mScan = (Button) findViewById(R.id.Scan);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = null;
        }

        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(imprimir.this, "Message1", Toast.LENGTH_SHORT).show();
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent,
                            REQUEST_ENABLE_BT);
                } else {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(imprimir.this,
                            imprimir_listActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);
                }
            }
            }
        });

        cod = getIntent().getStringExtra("cod_get");
        mPrint = (Button) findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                Thread t = new Thread() {
                    public void run() {
                        RequestAbastecimentoInterno(cod);
                    }
                };
                t.start();
            }
        });

        mDisc = (Button) findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                //if (mBluetoothAdapter != null)
                    //mBluetoothAdapter.disable();
                    finish();
            }
        });

    }// onCreate

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            //if (mBluetoothSocket != null)
                //mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Conectando...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, true);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(imprimir.this,
                            imprimir_listActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(imprimir.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            //mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(imprimir.this, "Impressora conectada", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    private void RequestAbastecimentoInterno(String cod) {
        Database_syspalma Banco = new Database_syspalma(this);
        SQLiteDatabase database = Banco.getReadableDatabase();
        String selectQuery = "SELECT strftime('%d/%m/%Y %H:%M:%S', data) as data, p.descricao, pp.descricao, \n" +
                "matricula_responsavel, matricula_mdo, pcr.nome, pcm.nome, horimetro_km, \n" +
                "cupom_fiscal, ins.descricao, quantidade \n" +
                "FROM abastecimento_interno rc \n" +
                "INNER JOIN c_insumo ins \n" +
                "ON ins.idinsumo = rc.id_insumo \n" +
                "INNER JOIN c_patrimonio p \n" +
                "ON p.idpatrimonio = rc.id_patrimonio \n" +
                "INNER JOIN c_patrimonio pp \n" +
                "ON pp.idpatrimonio = rc.id_patrimonio_posto \n" +
                "INNER JOIN p_colaborador pcr \n" +
                "ON pcr.matricula = rc.matricula_responsavel \n" +
                "LEFT JOIN p_colaborador pcm \n" +
                "ON pcm.matricula = rc.matricula_mdo \n"+
                "WHERE cod_abastecimento = '"+cod+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String data = cursor.getString(0);
            String patrimonio = cursor.getString(1);
            String posto = cursor.getString(2);
            String responsavel_mat = cursor.getString(3);
            String mdo_mat = cursor.getString(4);
            String responsavel_nome = cursor.getString(5);
            String mdo_nome = cursor.getString(6);
            String km = cursor.getString(7);
            String cupomFiscal = cursor.getString(8);
            String insumo = cursor.getString(9);
            String quantidade = cursor.getString(10);

            try {
                OutputStream os = mBluetoothSocket
                        .getOutputStream();
                String BILL = "";
                BILL =  "             SYSPALMA "+versionName+"     \n " +
                        "           ABASTECIMENTO POSTO INTERNO     \n " +
                        "                  CUPOM: "+cupomFiscal+"    \n"+
                        "              COD: "+cod+"    \n";
                BILL = BILL
                        + "------------------------------------------------\n";

                BILL = BILL + " "+data;
                BILL = BILL + "\n " + String.format("%1$-10s  %2$10s", "POSTO:",posto);
                BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$10s %4$10s", "PATRIMONIO: ",patrimonio, "km/hrm.:", km);
                BILL = BILL + "\n " + String.format("%1$-10s %2$10s", "Motorista/Operador:",mdo_mat)+ "\n";

                BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$10s %4$10s", "Item", "\t", "\t", "Litros");
                BILL = BILL + "\n";
                BILL = BILL
                        + "------------------------------------------------";
                BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$10s %4$10s", insumo, "\t", "\t", quantidade);

                BILL = BILL
                        + "\n------------------------------------------------";
                BILL = BILL + "\n\n\n\n";

                BILL = BILL + "------------------------------------------------\n";
                BILL = BILL +  "                 Responsavel \n";
                BILL = BILL +  "          "+responsavel_nome+"\n";
                BILL = BILL +  "                   "+responsavel_mat+"\n\n\n\n";

                BILL = BILL + "------------------------------------------------\n";
                BILL = BILL +  "                Operador/Motorista \n";
                BILL = BILL +  "          "+mdo_nome+"\n";
                BILL = BILL +  "                    "+mdo_mat+"  \n";
                BILL = BILL + "\n-----------------------------------------------";
                BILL = BILL + "\n Powered by: Ryatec Software\n\n\n";

                os.write(BILL.getBytes());
                //This is printer specific code you can comment ==== > Start

                // Setting height
                int gs = 50;
                os.write(intToByteArray(gs));
                int h = 200;
                os.write(intToByteArray(h));
                int n = 200;
                os.write(intToByteArray(n));

                // Setting Width
                int gs_width = 300;
                os.write(intToByteArray(gs_width));
                int w = 250;
                os.write(intToByteArray(w));
                int n_width = 50;
                os.write(intToByteArray(n_width));

            } catch (Exception e) {
                Log.e("MainActivity", "Exe ", e);
            }
        }
        cursor.close();
        database.close();
    }

}
