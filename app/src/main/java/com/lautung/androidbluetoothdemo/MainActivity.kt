package com.lautung.androidbluetoothdemo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lautung.androidbluetoothdemo.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX



class MainActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityMainBinding

    //获取系统蓝牙适配器
    private lateinit var mBluetoothAdapter: BluetoothAdapter

    //扫描者
    private lateinit var scanner: BluetoothLeScanner

    //是否正在扫描
    var isScanning = false

    //设备列表
    private val deviceList = mutableListOf<BlueToothDevice>()


    private val scanCallback=object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result!=null){
                Log.i("MainActivity11", "onScanResult: "+result)
                addDeviceList(BlueToothDevice(result.device,result.rssi))
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {

        }

        override fun onScanFailed(errorCode: Int) {

        }

    }



    private val enableBluetooth=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            if (BlueToothUtil.isOpenBluetooth(this@MainActivity)){
                Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "蓝牙未打开", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BlueToothUtil.init(this)

        binding.btnOpenBluetooth.setOnClickListener{

            if (BlueToothUtil.isOpenBluetooth(this@MainActivity)){
                Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.S){
                enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                return@setOnClickListener
            }

            PermissionX.init(this@MainActivity)
                .permissions(Manifest.permission.BLUETOOTH_CONNECT)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                    } else {
                        Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.btnScanBluetooth.setOnClickListener{
            isScanning=!isScanning
            if (isScanning){
                binding.btnScanBluetooth.text = "停止扫描"
                if (Build.VERSION.SDK_INT<Build.VERSION_CODES.S){
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                BlueToothUtil.startScan(scanCallback)
                            } else {
                                Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                            }
                        }
                }else{
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                BlueToothUtil.startScan(scanCallback)
                            } else {
                                Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }else{
                binding.btnScanBluetooth.text = "开始扫描"
                if (Build.VERSION.SDK_INT<Build.VERSION_CODES.S){
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                BlueToothUtil.stopScan(scanCallback)
                            } else {
                                Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                            }
                        }
                }else{
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                BlueToothUtil.startScan(scanCallback)
                            } else {
                                Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }




        }


        binding.rvDevices.apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            adapter =DevicesAdapter(deviceList)
            addItemDecoration(DividerItemDecoration(this@MainActivity,LinearLayout.VERTICAL))
        }


    }

    private fun findDeviceIndex(scanDevice: BlueToothDevice, deviceList: List<BlueToothDevice>): Int {
        var index = 0
        for (device in deviceList) {
            if (scanDevice.device.address.equals(device.device.address)) return index
            index += 1
        }
        return -1
    }

    private fun addDeviceList(device: BlueToothDevice) {
        val index = findDeviceIndex(device, deviceList)
        if (index == -1) {
            deviceList.add(device)
            binding.rvDevices.adapter?.notifyDataSetChanged()
        } else {
            deviceList[index].rssi = device.rssi
            binding.rvDevices.adapter?.notifyItemChanged(index)
        }
    }


}