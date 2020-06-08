package com.ttl.messageverify;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.messageverify.adapter.BtAdapter;
import com.ttl.messageverify.model.BtDevice;
import com.ttl.messageverify.services.MyService;

import java.util.ArrayList;
import java.util.List;

public class BtDeviceActivity extends AppCompatActivity {
    private static List<BtDevice> btDevices = new ArrayList<>();
    private static RecyclerView rvDevices;

    public static void updateList(String deviceName, int strength) {
        BtDevice btDevice = new BtDevice();
        btDevice.setDevieName(deviceName != null ? deviceName : "NA");
        btDevice.setStrength(strength);
        btDevices.add(btDevice);
        rvDevices.getAdapter().notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_device);
        rvDevices = findViewById(R.id.rv_bt_device);
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        BtAdapter adapter = new BtAdapter(btDevices);
        rvDevices.setAdapter(adapter);

        startMyService();


        /*final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();*/

        /*final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    //String deviceHardwareAddress = device.getAddress();
                    int strength = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                    if (deviceName == null)
                        deviceName = "NA";
                    BtDevice btDevice = new BtDevice();
                    btDevice.setDevieName(deviceName);
                    btDevice.setStrength(strength);
                    btDevices.add(btDevice);
                    rvDevices.getAdapter().notifyDataSetChanged();
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);*/
    }

    private void startMyService() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

}
