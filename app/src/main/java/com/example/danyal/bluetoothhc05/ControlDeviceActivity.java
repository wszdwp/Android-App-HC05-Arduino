package com.example.danyal.bluetoothhc05;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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

    private ProgressDialog progress;

    private String name;
    private String address;

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_device);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");

        new ConnectBT().execute();
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
                showToastMessage("Error while sending Signal " + number);
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
            progress = ProgressDialog.show(ControlDeviceActivity.this, "Connecting " + name , "Address: " + address);
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = myBluetooth.getRemoteDevice(address);
                    btSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
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
