package com.lautung.androidbluetoothdemo

import android.bluetooth.BluetoothDevice

/**
 * @Auther: lautung
 * @datetime: 2023/7/12
 * @desc:
 */
data class BlueToothDevice(val device: BluetoothDevice, var rssi: Int )
