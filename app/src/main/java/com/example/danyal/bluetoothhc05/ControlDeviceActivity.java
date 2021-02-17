package com.example.danyal.bluetoothhc05;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ControlDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button comboBtn1, comboBtn2, comboBtn3, comboBtn4, comboBtn5, disConnectBtn;
    private TextView lumn;
    private ProgressDialog progress;

    private String address = null;
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_device);

        address = getIntent().getStringExtra("address");
        findViews();

        new ConnectBT().execute();
    }

    private void findViews() {
        comboBtn1 = findViewById(R.id.comboBtn1);
        comboBtn1 = findViewById(R.id.comboBtn2);
        comboBtn1 = findViewById(R.id.comboBtn3);
        comboBtn1 = findViewById(R.id.comboBtn4);
        comboBtn1 = findViewById(R.id.comboBtn5);
        disConnectBtn = findViewById(R.id.disconnectBtn);
        lumn = findViewById(R.id.textView2);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.comboBtn1) {
            sendSignal("Combo Button 1");
        }
        else if (vId == R.id.comboBtn2) {
            sendSignal("Combo Button 2");
        }
        else if (vId == R.id.comboBtn3) {
            sendSignal("Combo Button 3");
        }
        else if (vId == R.id.comboBtn4) {
            sendSignal("Combo Button 4");
        }
        else if (vId == R.id.comboBtn5) {
            sendSignal("Combo Button 5");
        }
        else if (vId == R.id.disconnectBtn) {
            disConnect();
        }
    }

    private void sendSignal(String number) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(number.getBytes());
            } catch (IOException e) {
                showToastMessage("Error while sending Signal.");
            }
        }
    }

    private void disConnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                showToastMessage("Error while closing bluetooth socket.");
            }
        }

        finish();
    }

    private void showToastMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connected = true;

        @Override
        protected  void onPreExecute() {
            progress = ProgressDialog.show(ControlDeviceActivity.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                connected = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!connected) {
                showToastMessage("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                showToastMessage("Connected");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }
}
