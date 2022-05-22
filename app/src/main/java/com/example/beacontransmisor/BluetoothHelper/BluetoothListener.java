package com.example.beacontransmisor.BluetoothHelper;

/**
 * Created by Fran on 25/03/22.
 */

public interface BluetoothListener {

    void initializeBluetooth(OnBluetoothSupportedCheckListener listener);

    void enableBluetooth(OnBluetoothEnabledCheckListener listener);

    interface OnBluetoothSupportedCheckListener {

        void onBLENotSupported();

        void onBluetoothNotSupported();
    }

    interface OnBluetoothEnabledCheckListener{

        void onBluetoothEnabled(boolean enable);
    }

    interface BluetoothTrigger
    {
        void initBluetooth();

        void enableBluetooth();

    }
}
