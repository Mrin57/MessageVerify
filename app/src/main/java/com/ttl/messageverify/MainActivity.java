package com.ttl.messageverify;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
    private static final String TAG = "#######";
    private static MainActivity instance;
    private EditText etMobileNo, etOtp, etBluetoothName;
    private String mPhoneNumber = "0000000000";
    private GoogleApiClient mGoogleApiClient;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMobileNo = findViewById(R.id.et_mobile_no);
        etOtp = findViewById(R.id.et_otp);
        etBluetoothName = findViewById(R.id.et_bt_name);
        Button btnVerify = findViewById(R.id.btn_verify);
        Button btnPublish = findViewById(R.id.btn_publish);
        Button btnScan = findViewById(R.id.btn_scan);
        TextView tvBluetooth = findViewById(R.id.tv_bluetooth);
        btnVerify.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        String macAddr = Settings.Secure.getString(getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
        tvBluetooth.setText(macAddr);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, RECEIVE_SMS) ==
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            /*TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();*/
            //tvNum1.setText(mPhoneNumber);
            return;
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE,
                    RECEIVE_SMS, ACCESS_FINE_LOCATION}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(this, RECEIVE_SMS) !=
                                PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                //mPhoneNumber = tMgr.getLine1Number();
                //tvNum1.setText(mPhoneNumber);


                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            Log.e("", "Could not start hint picker Intent", e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {
                    Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
//                    cred.getId====: ====+919*******
                    Log.e("cred.getId", cred.getId());
                    String userMob = cred.getId();
                    etMobileNo.setText(userMob);


                } else {
                    // Sim Card not found!
                    Log.e("cred.getId", "1008 else");

                    return;
                }
                break;
            case 101:
                if (resultCode == RESULT_OK) {
                    String btName = etBluetoothName.getText().toString();
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    if (adapter.getState() == BluetoothAdapter.STATE_ON)
                        adapter.setName(btName);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_verify) {
            if (etOtp.getText().toString().equals("234567")) {
                Toast.makeText(this, "OTP matched", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "OTP did not match, please try again", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_publish) {
            String btName = etBluetoothName.getText().toString();
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter.getState() == BluetoothAdapter.STATE_ON)
                adapter.setName(btName);
            else
                startBlueTooth();
        } else if (v.getId() == R.id.btn_scan) {
            Intent btIntent = new Intent(MainActivity.this, BtDeviceActivity.class);
            startActivity(btIntent);
        }
    }

    private void startBlueTooth() {
        Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(eintent, 101);
    }

    public void updateOtp(String otp) {
        etOtp.setText(otp);
    }
}
