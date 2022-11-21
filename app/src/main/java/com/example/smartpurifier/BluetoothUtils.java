package com.example.smartpurifier;

import static com.example.smartpurifier.Constants.CHARACTERISTIC_COMMAND_STRING;
import static com.example.smartpurifier.Constants.SERVICE_STRING;
import static com.example.smartpurifier.Constants.CHARACTERISTIC_RESPONSE_STRING;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothUtils {

    /*
        Find characteristics of BLE
        @param gatt gatt instance
        @return list of found gatt characteristics
         */
    public static List<BluetoothGattCharacteristic> findBLECharacteristics(BluetoothGatt _gatt ) {
        List<BluetoothGattCharacteristic> matching_characteristics = new ArrayList<>();
        List<BluetoothGattService> service_list = _gatt.getServices();
        BluetoothGattService service = findGattService(service_list);
        if (service == null) {
            return matching_characteristics;
        }

        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristicList) {
            if (isMatchingCharacteristic(characteristic)) {
                matching_characteristics.add(characteristic);
            }
        }

        return matching_characteristics;
    }

    /*
    Find command characteristic of the peripheral device
    @param gatt gatt instance
    @return found characteristic
     */
    @Nullable
    public static BluetoothGattCharacteristic findCommandCharacteristic( BluetoothGatt _gatt ) {
        return findCharacteristic( _gatt, CHARACTERISTIC_COMMAND_STRING );
    }

    /*
    Find response characteristic of the peripheral device
    @param gatt gatt instance
    @return found characteristic
     */
    @Nullable
    public static BluetoothGattCharacteristic findResponseCharacteristic( BluetoothGatt _gatt ) {
        return findCharacteristic( _gatt, CHARACTERISTIC_RESPONSE_STRING );
    }

    /*
    Find the given uuid characteristic
    @param gatt gatt instance
    @param uuid_string uuid to query as string
     */
    @Nullable
    private static BluetoothGattCharacteristic findCharacteristic(BluetoothGatt _gatt, String _uuid_string) {
        List<BluetoothGattService> service_list= _gatt.getServices();
        BluetoothGattService service= BluetoothUtils.findGattService( service_list );
        if( service == null ) {
            return null;
        }

        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for( BluetoothGattCharacteristic characteristic : characteristicList) {
            if( matchCharacteristic( characteristic, _uuid_string ) ) {
                return characteristic;
            }
        }

        return null;
    }

    /*
    Match the given characteristic and a uuid string
    @param characteristic one of found characteristic provided by the server
    @param uuid_string uuid as string to match
    @return true if matched
     */
    private static boolean matchCharacteristic( BluetoothGattCharacteristic _characteristic, String _uuid_string ) {
        if( _characteristic == null ) {
            return false;
        }
        UUID uuid = _characteristic.getUuid();
        return matchUUIDs( uuid.toString(), _uuid_string );
    }

    /*
    Find Gatt service that matches with the server's service
    @param service_list list of services
    @return matched service if found
     */
    @Nullable
    private static BluetoothGattService findGattService(List<BluetoothGattService> _service_list) {
        for (BluetoothGattService service : _service_list) {
            String service_uuid_string = service.getUuid().toString();
            if (matchServiceUUIDString(service_uuid_string)) {
                return service;
            }
        }
        return null;
    }

    /*
    Try to match the given uuid with the service uuid
    @param service_uuid_string service UUID as string
    @return true if service uuid is matched
     */
    private static boolean matchServiceUUIDString(String _service_uuid_string) {
        return matchUUIDs( _service_uuid_string, SERVICE_STRING );
    }

    /*
    Check if there is any matching characteristic
    @param characteristic query characteristic
     */
    private static boolean isMatchingCharacteristic( BluetoothGattCharacteristic _characteristic ) {
        if( _characteristic == null ) {
            return false;
        }
        UUID uuid = _characteristic.getUuid();
        return matchCharacteristicUUID( uuid.toString() );
    }

    /*
    Query the given uuid as string to the provided characteristics by the server
    @param characteristic_uuid_string query uuid as string
    @return true if the matched is found
     */
    private static boolean matchCharacteristicUUID( String _characteristic_uuid_string ) {
        return matchUUIDs( _characteristic_uuid_string, CHARACTERISTIC_COMMAND_STRING, CHARACTERISTIC_RESPONSE_STRING );
    }

    /*
    Try to match a uuid with the given set of uuid
    @param uuid_string uuid to query
    @param matches a set of uuid
    @return true if matched
     */
    private static boolean matchUUIDs( String _uuid_string, String... _matches ) {
        for( String match : _matches ) {
            if( _uuid_string.equalsIgnoreCase(match) ) {
                return true;
            }
        }

        return false;
    }
}
