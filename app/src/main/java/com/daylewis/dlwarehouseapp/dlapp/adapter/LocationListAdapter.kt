package com.daylewis.dlwarehouseapp.dlapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qrscanner.dlapp.databinding.ItemLocationListBinding

class LocationListAdapter(var context: Context, var data: List<String>,var  onClicked: (Int) -> Unit) :RecyclerView.Adapter<LocationListAdapter.MyView>() {
    class MyView(item:ItemLocationListBinding) : RecyclerView.ViewHolder(item.root){
          var txtLocation=item.txtLocation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        var v=ItemLocationListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return MyView(v)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.txtLocation.text="${position.plus(1)}. "+data[position]
       holder.itemView.setOnClickListener {
         onClicked(position)
       }
    }

    override fun getItemCount(): Int {
     return  data.size
    }
}