package com.lautung.androidbluetoothdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lautung.androidbluetoothdemo.databinding.ItemDeviceBinding

/**
 * @Auther: lautung
 * @datetime: 2023/7/12
 * @desc:
 */
class DevicesAdapter(private  val devices:List<BlueToothDevice>) : RecyclerView.Adapter<DevicesAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder=Viewholder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context)))


    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.tvAddress.text = devices[position].device.address
        holder.tvName.text = devices[position].device.name
        holder.tvRssi.text = devices[position].rssi.toString()

    }

    override fun getItemCount(): Int=devices.size

    class  Viewholder(binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root){
        val tvAddress:TextView
        val tvName:TextView
        val tvRssi:TextView

        init {
            tvAddress=binding.tvAddress
            tvName=binding.tvName
            tvRssi=binding.tvRssi
        }
    }
}