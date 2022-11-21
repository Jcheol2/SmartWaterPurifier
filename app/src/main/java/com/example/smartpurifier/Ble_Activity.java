package com.example.smartpurifier;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.example.smartpurifier.Constants.REQUEST_ENABLE_BT1;
import static com.example.smartpurifier.Constants.REQUEST_ENABLE_BT2;
import static com.example.smartpurifier.Constants.REQUEST_FINE_LOCATION;
import static com.example.smartpurifier.Constants.SCAN_PERIOD;
import static com.example.smartpurifier.Constants.TAG;
import static com.example.smartpurifier.Constants.SERVICE_STRING;
import static com.example.smartpurifier.Constants.UUID_TDCS_SERVICE;
import static com.example.smartpurifier.Constants.CHARACTERISTIC_COMMAND_STRING;
import static com.example.smartpurifier.Constants.UUID_CTRL_COMMAND;
import static com.example.smartpurifier.Constants.CHARACTERISTIC_RESPONSE_STRING;
import static com.example.smartpurifier.Constants.UUID_CTRL_RESPONSE;
import static com.example.smartpurifier.Constants.MAC_ADDR;
import static java.lang.Thread.sleep;

public class Ble_Activity extends AppCompatActivity {
    private BluetoothAdapter ble_Adapter;
    BluetoothLeScanner ble_scanner_;
    BluetoothLeAdvertiser ble_Advertiser;
    private TextView tv_status;
    private TextView tv_read;
    private TextView tv_data;
    private TextView tv_mac;
    private Button btn_scan;
    private Button btn_stop;
    private Button btn_send1;
    private Button btn_send2;
    private Button btn_show;
    private boolean is_scanning_ = false;
    private boolean connected_ = false;
    private Map<String, BluetoothDevice> scan_results_;
    private ScanCallback scan_cb_;
    private Handler scan_handler_;
    private BluetoothGatt ble_gatt_;
    String name;
    String engname;
    String phoneNum;
    String age;
    String sex;
    String med;
    int resId;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_ble, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.complete:
                Intent intent = new Intent(Ble_Activity.this, MainActivity.class);
                intent.putExtra("med",med);
                intent.putExtra("name",name);
                intent.putExtra("resId",resId);
                intent.putExtra("phone",phoneNum);
                intent.putExtra("engname",engname);
                intent.putExtra("birth",age);
                intent.putExtra("sex",sex);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // finish app if the BLE is not supported
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast.makeText(getApplicationContext(), "해당 기기는 BLE를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Toast.makeText(getApplicationContext(), "해당 기기는 BLE를 지원합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interlink_ble);
        getSupportActionBar().setTitle("블루투스 연결");

        tv_status = findViewById(R.id.tv_status);
        tv_read = findViewById(R.id.tv_read);
        tv_data = findViewById(R.id.tv_data);
        tv_mac = findViewById(R.id.tv_mac);
        btn_scan = findViewById(R.id.btn_scan);
        btn_stop = findViewById(R.id.btn_stop);
        btn_send1 = findViewById(R.id.btn_send1);
        btn_send2 = findViewById(R.id.btn_send2);
        btn_show = findViewById(R.id.btn_show);

        //위치권한
        requestLocationPermission();
        //블투권한
        requestEnableBLE();

        ble_Adapter = BluetoothAdapter.getDefaultAdapter();
        ble_scanner_ = ble_Adapter.getBluetoothLeScanner();
        ble_Advertiser = ble_Adapter.getBluetoothLeAdvertiser();

        Intent intent1 = getIntent();
        if(intent1.hasExtra("name5") && (intent1.hasExtra("engname5")) && (intent1.hasExtra("phoneNum5")) && (intent1.hasExtra("age5")) && (intent1.hasExtra("sex5"))&& (intent1.hasExtra("med5"))&& (intent1.hasExtra("resId5"))){
            name = intent1.getStringExtra("name5");
            engname = intent1.getStringExtra("engname5");
            phoneNum = intent1.getStringExtra("phoneNum5");
            age = intent1.getStringExtra("age5");
            sex = intent1.getStringExtra("sex5");
            med = intent1.getStringExtra("med5");
            resId = intent1.getIntExtra("resId5", R.drawable.med1);
        }

        int i=0;
        String SendData="";
        boolean success=false;
        Intent intent = getIntent();
        if(intent.hasExtra("name1")&&intent.hasExtra("med1")){
            String name1 = intent.getStringExtra("name1");
            String med1 = intent.getStringExtra("med1");
            SendData=SendData+name1+"("+med1+")";
            success=true;
            i+=1;
            if(i==1){SendData=SendData+"\n2. ";}
        }
        if(intent.hasExtra("name2")&&intent.hasExtra("med2")){
            String name2 = intent.getStringExtra("name2");
            String med2 = intent.getStringExtra("med2");
            SendData=SendData+name2+"("+med2+")";
            success=true;
            i+=1;
            if(i==1){SendData=SendData+"\n2. ";}
        }
        if(intent.hasExtra("name3")&&intent.hasExtra("med3")){
            String name3 = intent.getStringExtra("name3");
            String med3 = intent.getStringExtra("med3");
            SendData=SendData+name3+"("+med3+")";
            success=true;
            i+=1;
            if(i==1){SendData=SendData+"\n2. ";}
        }
        if(intent.hasExtra("name4")&&intent.hasExtra("med4")){
            String name4 = intent.getStringExtra("name4");
            String med4 = intent.getStringExtra("med4");
            SendData=SendData+name4+"("+med4+")";
            success=true;
            i+=1;
            if(i==1){SendData=SendData+"\n2. ";}
        }
        if(intent.hasExtra("engname5")&&intent.hasExtra("med5")){
            SendData=SendData+engname+"("+med+")";
            success=true;
            i+=1;
            if(i==1){SendData=SendData+"\n2. ";}
        }
        if(success) {
            tv_data.setText("전송할 데이터 :\n"+"1. "+SendData);
        }

        btn_scan.setOnClickListener((v) -> {
            startScan(v);
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectGattServer();
            }
        });
        btn_send1.setOnClickListener((v) -> {
            sendData1(v);
        });
        btn_send2.setOnClickListener((v) -> {
            sendData2(v);
        });
    }

    private void startScan(View v) {
        disconnectGattServer();
        tv_status.setText("Scanning...");

        /*List<ScanFilter> filters= new ArrayList<>();
        ScanFilter scan_filter= new ScanFilter.Builder()
                .setServiceUuid( new ParcelUuid( UUID_TDCS_SERVICE ) )
                .build();
        filters.add( scan_filter );*/

        //필터 설정
        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter scan_filter = new ScanFilter.Builder()
                .setDeviceAddress(MAC_ADDR)
                .build();
        filters.add(scan_filter);

        //스캔 설정
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();

        scan_results_ = new HashMap<>();
        scan_cb_ = new BLEScanCallback(scan_results_);
        //스캔 시작
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            ble_scanner_.startScan(filters, settings, scan_cb_);
            is_scanning_ = true;
            scan_handler_ = new Handler(Looper.getMainLooper());
            scan_handler_.postDelayed(this::Connection, SCAN_PERIOD);
            return;
        }
    }

    private void Connection() {
        // 스캔중인가?
        if (is_scanning_ && ble_scanner_ != null && ble_Adapter != null && ble_Adapter.isEnabled()) {
            // stop scanning
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
                ble_scanner_.stopScan(scan_cb_);
                scanComplete();
                return;
            }
        }
        // reset flags
        scan_cb_ = null;
        is_scanning_ = false;
        scan_handler_ = null;
        // update the status
        tv_status.setText("Scan Device Not Found");
    }

    private void scanComplete() {
        // check if nothing found
        if (scan_results_.isEmpty()) {
            tv_status.setText("scan results is empty");
            return;
        }
        // loop over the scan results and connect to them
        for (String device_addr : scan_results_.keySet()) {
            Log.d(TAG, "Found device: " + device_addr);
            // get device instance using its MAC address
            BluetoothDevice device = scan_results_.get(device_addr);
            if (MAC_ADDR.equals(device_addr)) {
                Toast.makeText(getApplicationContext(), "connecting device: " + device_addr, Toast.LENGTH_SHORT).show();
                // connect to the device
                connectDevice(device);
            }
            //if(UUID_TDCS_SERVICE.equals(characteristic.getUuid())){}
        }
    }

    private void connectDevice(BluetoothDevice _device) {
        // update the status
        tv_status.setText("Connecting");
        tv_mac.setText(_device.getAddress());
        GattClientCallback gatt_client_cb = new GattClientCallback();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            ble_gatt_ = _device.connectGatt(this, false, gatt_client_cb);
            return;
        }
    }

    private void sendData1(View v) {
        // check connection
        if (!connected_) {
            Log.e(TAG, "Failed to sendData due to no connection");
            Toast.makeText(getApplicationContext(), "먼저 BLE 기기를 연결해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // find command characteristics from the GATT server
        BluetoothGattCharacteristic cmd_characteristic = BluetoothUtils.findCommandCharacteristic(ble_gatt_);
        // disconnect if the characteristic is not found
        if (cmd_characteristic == null) {
            Log.e(TAG, "Unable to find cmd characteristic");
            disconnectGattServer();
            return;
        }
        // start stimulation
        startStimulation1(cmd_characteristic, 1);
    }

    private void startStimulation1(BluetoothGattCharacteristic _cmd_characteristic, final int _program_id) {
        String SendData="";
        Intent intent = getIntent();
        if(intent.hasExtra("name1")&&intent.hasExtra("med1")){
            String name1 = intent.getStringExtra("name1");
            String med1 = intent.getStringExtra("med1");
            SendData=SendData+name1+","+med1;
        }
        else if(intent.hasExtra("name2")&&intent.hasExtra("med2")){
            String name2 = intent.getStringExtra("name2");
            String med2 = intent.getStringExtra("med2");
            SendData=SendData+name2+","+med2;
        }
        else if(intent.hasExtra("name3")&&intent.hasExtra("med3")){
            String name3 = intent.getStringExtra("name3");
            String med3 = intent.getStringExtra("med3");
            SendData=SendData+name3+","+med3;
        }
        else if(intent.hasExtra("name4")&&intent.hasExtra("med4")){
            String name4 = intent.getStringExtra("name4");
            String med4 = intent.getStringExtra("med4");
            SendData=SendData+name4+","+med4;;
        }
        else if(intent.hasExtra("engname5")&&intent.hasExtra("med5")){
            String engname5 = intent.getStringExtra("engname5");
            String med5 = intent.getStringExtra("med5");
            SendData=SendData+engname5+","+med5;
        }
        // set values to the characteristic
        _cmd_characteristic.setValue("1/"+SendData);
        // write the characteristic
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        boolean success = ble_gatt_.writeCharacteristic(_cmd_characteristic);
        // check the result
        if( success ) {
            Log.d( TAG, _program_id + ". Send Data1 Success");
            Toast.makeText(getApplicationContext(), "데이터 전송에 성공했습니다.\n"+SendData, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.e( TAG, "Failed to write command" );
            Toast.makeText(getApplicationContext(), "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendData2(View v) {
        // check connection
        if (!connected_) {
            Log.e(TAG, "Failed to sendData due to no connection");
            Toast.makeText(getApplicationContext(), "먼저 BLE 기기를 연결해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // find command characteristics from the GATT server
        BluetoothGattCharacteristic cmd_characteristic = BluetoothUtils.findCommandCharacteristic(ble_gatt_);
        // disconnect if the characteristic is not found
        if (cmd_characteristic == null) {
            Log.e(TAG, "Unable to find cmd characteristic");
            disconnectGattServer();
            return;
        }
        // start stimulation
        startStimulation2(cmd_characteristic, 2);
    }

    private void startStimulation2(BluetoothGattCharacteristic _cmd_characteristic, final int _program_id) {
        String SendData="";
        Intent intent = getIntent();
        if(intent.hasExtra("engname5")&&intent.hasExtra("med5")){
            String engname5 = intent.getStringExtra("engname5");
            String med5 = intent.getStringExtra("med5");
            SendData=SendData+engname5+","+med5;
        }
        else if(intent.hasExtra("name4")&&intent.hasExtra("med4")){
            String name4 = intent.getStringExtra("name4");
            String med4 = intent.getStringExtra("med4");
            SendData=SendData+name4+","+med4;
        }
        else if(intent.hasExtra("name3")&&intent.hasExtra("med3")){
            String name3 = intent.getStringExtra("name3");
            String med3 = intent.getStringExtra("med3");
            SendData=SendData+name3+","+med3;
        }
        else if(intent.hasExtra("name2")&&intent.hasExtra("med2")){
            String name2 = intent.getStringExtra("name2");
            String med2 = intent.getStringExtra("med2");
            SendData=SendData+name2+","+med2;
        }
        else if(intent.hasExtra("name1")&&intent.hasExtra("med1")){
            String name1 = intent.getStringExtra("name1");
            String med1 = intent.getStringExtra("med1");
            SendData=SendData+name1+","+med1;
        }

        // set values to the characteristic
        _cmd_characteristic.setValue("2/"+SendData);
        // write the characteristic
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        boolean success = ble_gatt_.writeCharacteristic(_cmd_characteristic);
        // check the result
        if( success ) {
            Log.d( TAG, _program_id + ". Send Data2 Success");
            Toast.makeText(getApplicationContext(), "데이터 전송에 성공했습니다.\n"+SendData, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.e( TAG, "Failed to write command" );
            Toast.makeText(getApplicationContext(), "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt _gatt, int _status, int _new_state) {
            super.onConnectionStateChange(_gatt, _status, _new_state);
            if (_status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer();
                return;
            } else if (_status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer();
                return;
            }
            if (_new_state == BluetoothProfile.STATE_CONNECTED) {
                // set the connection flag
                try {
                connected_ = true;
                Log.d(TAG, "Connected to the GATT server");
                    _gatt.discoverServices();
                }catch(SecurityException e){
                    e.printStackTrace();
                }
            } else if (_new_state == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer();
            }
        }
        @Override
        public void onServicesDiscovered( BluetoothGatt _gatt, int _status ) {
            super.onServicesDiscovered( _gatt, _status );
            // check if the discovery failed
            if( _status != BluetoothGatt.GATT_SUCCESS ) {
                Log.e( TAG, "Device service discovery failed, status: " + _status );
                return;
            }
            // find discovered characteristics
            List<BluetoothGattCharacteristic> matching_characteristics= BluetoothUtils.findBLECharacteristics( _gatt );
            if( matching_characteristics.isEmpty() ) {
                Log.e( TAG, "Unable to find characteristics" );
                return;
            }
            // log for successful discovery
            Log.d( TAG, "Services discovery is successful" );
        }

        @Override
        public void onCharacteristicChanged( BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic ) {
            super.onCharacteristicChanged( _gatt, _characteristic );

            Log.d( TAG, "characteristic changed: " + _characteristic.getUuid().toString() );
            readCharacteristic( _characteristic );
        }

        @Override
        public void onCharacteristicWrite( BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic, int _status ) {
            super.onCharacteristicWrite( _gatt, _characteristic, _status );
            if( _status == BluetoothGatt.GATT_SUCCESS ) {
                Log.d( TAG, "Characteristic written successfully" );
                Toast.makeText(getApplicationContext(), "데이터 전송에 성공했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e( TAG, "Characteristic write unsuccessful, status: " + _status) ;
                Toast.makeText(getApplicationContext(), "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                disconnectGattServer();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d (TAG, "Characteristic read successfully" );
                readCharacteristic(characteristic);
            } else {
                Log.e( TAG, "Characteristic read unsuccessful, status: " + status);
                // Trying to read from the Time Characteristic? It doesnt have the property or permissions
                // set to allow this. Normally this would be an error and you would want to:
                // disconnectGattServer();
            }
        }

        /*
        Log the value of the characteristic
        @param characteristic
         */
        private void readCharacteristic( BluetoothGattCharacteristic _characteristic ) {
            byte[] msg= _characteristic.getValue();
            tv_read.setText( "read: " + msg.toString() );
            Log.d( TAG, "read: " + msg.toString() );
        }
    }

    public void disconnectGattServer() {

        tv_status.setText("<Status>");
        tv_mac.setText("<Mac>");
        // reset the connection flag
        connected_ = false;
        // disconnect and close the gatt
        if (ble_gatt_ != null) {
            Toast.makeText(getApplicationContext(), "BLE 연결을 종료합니다.", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
                ble_gatt_.disconnect();
                ble_gatt_.close();
                return;
            }
        }
    }

    //블루투스 권한 요청
    private void requestEnableBLE() {
        //Intent ble_enable_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{ Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT1 );
            //startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{ Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT2 );
            //startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT);
            return;
        }
    }
    //위치 권한 요청
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    private class BLEScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> cb_scan_results_;

        BLEScanCallback( Map<String, BluetoothDevice> _scan_results ) {
            cb_scan_results_= _scan_results;
        }

        @Override
        public void onScanResult( int _callback_type, ScanResult _result ) {
            Log.d( TAG, "onScanResult" );
            addScanResult( _result );
        }

        @Override
        public void onBatchScanResults( List<ScanResult> _results ) {
            for( ScanResult result: _results ) {
                addScanResult( result );
            }
        }

        @Override
        public void onScanFailed( int _error ) {
            Log.e( TAG, "BLE scan failed with code " +_error );
        }

        private void addScanResult( ScanResult _result ) {
            BluetoothDevice device= _result.getDevice();
            String device_address= device.getAddress();
            cb_scan_results_.put( device_address, device );
            tv_status.setText( "Device Found" );
        }
    }
}