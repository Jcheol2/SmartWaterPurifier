package com.example.smartpurifier;

import java.util.UUID;

public class Constants {
    // tag for log message
    public static String TAG= "ClientActivity";
    // used to identify adding bluetooth names
    public static int REQUEST_ENABLE_BT1= 1; // 블루투스
    public static int REQUEST_ENABLE_BT2= 2; // 어드민

    // used to request fine location permission
    public static int REQUEST_FINE_LOCATION= 3;

    public static String SERVICE_STRING = "4FAFC201-1FB5-459E-8FCC-C5C9C331914B"; //ESP32
    public static UUID UUID_TDCS_SERVICE= UUID.fromString(SERVICE_STRING);
    // command uuid
    public static String CHARACTERISTIC_COMMAND_STRING = "BEB5483E-36E1-4688-B7F5-EA07361B26A8"; // Write 쪽 UUID
    public static UUID UUID_CTRL_COMMAND = UUID.fromString( CHARACTERISTIC_COMMAND_STRING );
    // response uuid
    public static String CHARACTERISTIC_RESPONSE_STRING = "BEB5483E-36E1-4688-B7F5-EA07361B26A8";
    public static UUID UUID_CTRL_RESPONSE = UUID.fromString( CHARACTERISTIC_COMMAND_STRING );
    // focus MAC address
    public final static String MAC_ADDR= "78:E3:6D:19:F4:FA"; // 정수기
    // scan period
    public static final long SCAN_PERIOD = 5000;
}