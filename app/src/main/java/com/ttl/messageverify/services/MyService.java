package com.ttl.messageverify.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ttl.messageverify.BtDeviceActivity;
import com.ttl.messageverify.Utility;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    Intent mIntent = null;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND == action) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String myDevice = device.getName();
                int strength = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                BtDeviceActivity.updateList(myDevice, strength);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIntent = intent;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!Utility.BT_ADAPTER.isDiscovering()) {
                    Utility.BT_ADAPTER.startDiscovery();
                    IntentFilter iFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    iFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    iFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    try {
                        Boolean flag = myReceiver.isOrderedBroadcast();
                        if (!flag) {
                            registerReceiver(myReceiver, iFilter);
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
        timer.schedule(task, 100, 1000);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = mIntent;
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        unregisterReceiver(myReceiver);
        super.onTaskRemoved(rootIntent);
    }
}
