package com.lautung.androidbluetoothdemo

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE

/**
 * @Auther: lautung
 * @datetime: 2023/7/12
 * @desc:
 */
object BlueToothUtil {
    private  var isScanning =false
    //获取系统蓝牙适配器
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    //扫描者
    private lateinit var scanner: BluetoothLeScanner

    fun init(context: Context){
        if(isOpenBluetooth(context)){
            mBluetoothAdapter =(context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
            scanner=mBluetoothAdapter.bluetoothLeScanner
        }
    }

    fun isOpenBluetooth(context: Context): Boolean {
        val manager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return false
        return adapter.isEnabled
    }

    @SuppressLint("MissingPermission")
     fun startScan(scanCallback: ScanCallback) {
        if (!isScanning) {
            scanner.startScan(scanCallback)
            isScanning = true
        }
    }

    @SuppressLint("MissingPermission")
     fun stopScan(scanCallback: ScanCallback) {
        if (isScanning) {
            scanner.stopScan(scanCallback)
            isScanning = false
        }
    }

}