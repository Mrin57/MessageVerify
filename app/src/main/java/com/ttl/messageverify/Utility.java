package com.ttl.messageverify;

import android.bluetooth.BluetoothAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final BluetoothAdapter BT_ADAPTER = BluetoothAdapter.getDefaultAdapter();

    public static String extractDigits(String receivedMessage) {
        final Pattern p = Pattern.compile("(\\d{6})");
        final Matcher m = p.matcher(receivedMessage);
        if (m.find()) {
            return m.group(0);
        }
        return "";
    }
}
