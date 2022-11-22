package com.jotangi.healthy.HpayMall.Mall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.healthy.HpayMall.Mall.Response.MallOrder
import com.jotangi.healthy.R
/**
 *
 * @Title: MallOrderAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: MallOrderAdapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class MallOrderAdapter(private val mData: List<MallOrder>) :
    RecyclerView.Adapter<MallOrderAdapter.myViewHolder>() {
    var moItemClick: (MallOrder) -> Unit = {}
    var moItempayClick: (MallOrder) -> Unit = {}
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MallOrderAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mall, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MallOrderAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            pay.setOnClickListener { moItempayClick.invoke(data) }
            view.setOnClickListener { moItemClick.invoke(data) }
        }

    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var id: TextView = v.findViewById(R.id.moId)
        var date: TextView = v.findViewById(R.id.moDate)
        var dollar: TextView = v.findViewById(R.id.moDollar)
        var status: TextView = v.findViewById(R.id.moStatus)
        var pay: Button = v.findViewById(R.id.moPay)
        var view: Button = v.findViewById(R.id.moView)


    }

}