package com.jotangi.healthy.HpayMall.Mall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.healthy.HpayMall.Mall.Response.commodity
import com.jotangi.healthy.R
/**
 *
 * @Title: WatchAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: WatchAdapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class WatchAdapter (private val mData: List<commodity>)
    : RecyclerView.Adapter<WatchAdapter.myViewHolder>() {
    var WatchItemClick: (commodity) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):WatchAdapter.myViewHolder {
        return myViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_commodity,parent,false))
    }
    override fun onBindViewHolder(holder: WatchAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            id.text=data.id
            des.text=data.des
            itemView.setOnClickListener {

                notifyDataSetChanged()
                val bundle = Bundle()
                bundle.putString( "id",id.text.toString())
                bundle.putString("des",des.text.toString())
                WatchItemClick.invoke(data)
            }
        }

    }inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v){
        var id: TextView = v.findViewById(R.id.waId)
        var des: TextView = v.findViewById(R.id.waDe)


    }
}

