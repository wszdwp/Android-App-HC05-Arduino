package com.example.danyal.bluetoothhc05;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PairDeviceActivity extends AppCompatActivity implements CustomItemClickListener {

    private static final String TAG = "PairDeviceActivity";
    private static final int REQUEST_ENABLE_BLUETOOTH = 100;

    private BluetoothAdapter bluetoothAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView deviceRecyclerView;
    private DeviceRecyclerViewAdapter deviceRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BluetoothDevice> pairedDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pair_device_layout);

        findAndInitViews();

        checkBluetoothAvailability();
        registerFoundDeviceBroadcastReceiver();

        searchBluetoothDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterFoundDeviceBroadcastReceiver();
    }

    private void checkBluetoothAvailability() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            // do nothing
        }
    }

    private void registerFoundDeviceBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private void unregisterFoundDeviceBroadcastReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void findAndInitViews() {
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        deviceRecyclerView = findViewById(R.id.deviceList);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchBluetoothDevices();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        layoutManager = new LinearLayoutManager(this);
        deviceRecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), LinearLayoutManager.VERTICAL));
        deviceRecyclerView.setLayoutManager(layoutManager);
        deviceRecyclerViewAdapter = new DeviceRecyclerViewAdapter(pairedDevices, this);
        deviceRecyclerView.setAdapter(deviceRecyclerViewAdapter);
    }

    private void searchBluetoothDevices() {
        pairedDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        deviceRecyclerViewAdapter.setDevices(pairedDevices);
        deviceRecyclerViewAdapter.notifyDataSetChanged();

        if (pairedDevices.size() == 0) {
            Toast.makeText(getApplicationContext(), "No Device Found!", Toast.LENGTH_LONG).show();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.v(TAG, "Found new device " + device.getName() + " " + device.getAddress());
                pairedDevices.clear();
                pairedDevices.add(device);
                deviceRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onItemClick(View view, int position) {
        BluetoothDevice device = pairedDevices.get(position);
        Log.v(TAG, "position " + position + " item clicked");
        Intent intent = new Intent(PairDeviceActivity.this, ControlDeviceActivity.class);
        intent.putExtra("address", device.getAddress());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {

            } else {

            }
        }
    }
}
