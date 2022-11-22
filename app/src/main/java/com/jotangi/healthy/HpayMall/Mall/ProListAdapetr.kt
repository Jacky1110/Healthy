package com.jotangi.healthy.HpayMall.Mall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.healthy.HpayMall.ApiUrl
import com.jotangi.healthy.HpayMall.Mall.Response.Product_list
import com.jotangi.healthy.R
import com.squareup.picasso.Picasso


/**
 *
 * @Title: ProListAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: ProListAdapter
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class ProListAdapter(private val mData: List<Product_list?>) :
    RecyclerView.Adapter<ProListAdapter.myViewHolder>() {
    var WatchItemClick: (Product_list) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProListAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_commodity, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProListAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            val ProUrl = ApiUrl.API_URL
            product_name.text = data?.product_name
            product_price.text = data?.product_price
            product_no = data?.product_no.toString()
            product_picture = data?.product_picture.toString()
//
            Picasso.get().load(ProUrl +"medicalec/"+ data?.product_picture).into(pic)
            itemView.setOnClickListener {
                data?.let { it1 -> WatchItemClick.invoke(it1) }
            }
        }

    }


    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var product_name: TextView = v.findViewById(R.id.waId)
        var product_price: TextView = v.findViewById(R.id.waDe)

        var pic: ImageView = v.findViewById(R.id.imgpl)
        lateinit var product_no: String
        lateinit var product_picture: String

    }


}


