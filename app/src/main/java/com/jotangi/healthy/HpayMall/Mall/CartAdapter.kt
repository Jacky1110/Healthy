package com.jotangi.healthy.HpayMall.Mall

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.healthy.HpayMall.ApiUrl
import com.jotangi.healthy.HpayMall.DymaticTabFragment
import com.jotangi.healthy.HpayMall.Mall.Response.Cart
import com.jotangi.healthy.HpayMall.Mall.Response.Cartdis
import com.jotangi.healthy.HpayMall.Mall.Response.Ecorder_list
import com.jotangi.healthy.R
import com.squareup.picasso.Picasso


/**
 *
 * @Title: CDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: CartAdapter購物車的adapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class CartAdapter(private val mData: List<Cart>) :
    RecyclerView.Adapter<CartAdapter.myViewHolder>() {
    var cartItemClick: (Cart) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scart_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            name.text = data.Name
            nt.text = data.Nt
            count.text = data.Count
            total.text = data.Dollar
            total.text = data.total_amount
            product_picture = data.product_picture.toString()
            product_no = data.product_no.toString()
            Picasso.get().load(ApiUrl.API_URL +"medicalec/"+ product_picture).into(image)
            delete01.setOnClickListener {
                cartItemClick.invoke(data)

            }

        }

    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.sdName)
        var nt: TextView = v.findViewById(R.id.sdNt)
        var count: TextView = v.findViewById(R.id.sdCount)
        var image: ImageView = v.findViewById(R.id.sdImage)
        var delete01: TextView = v.findViewById(R.id.sd01)
        var total: TextView = v.findViewById(R.id.sdDollar)
        lateinit var product_picture: String
        lateinit var product_no: String

    }


}

/**
 *
 * @Title: EcoderAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: EcoderAdapter商城訂單的adapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class EcoderAdapter(private val mData: List<Ecorder_list>) :
    RecyclerView.Adapter<EcoderAdapter.myViewHolder>() {
    var EcoderClick: (Ecorder_list) -> Unit = {}
    var payClick: (Ecorder_list) -> Unit = {}
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EcoderAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mall, parent, false)
        )
    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var id: TextView = v.findViewById(R.id.moId)
        var date: TextView = v.findViewById(R.id.moDate)
        var dollar: TextView = v.findViewById(R.id.moDollar)
        var status: TextView = v.findViewById(R.id.moStatus)
        var pay: Button = v.findViewById(R.id.moPay)
        var view: Button = v.findViewById(R.id.moView)
    }

    override fun onBindViewHolder(holder: EcoderAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            id.text = data.order_no
            date.text = data.order_date
            dollar.text = data.order_pay
            status.text = data.order_status
            pay.visibility = (if (data.IsPay) View.INVISIBLE else View.INVISIBLE)
            Log.d("三小  ", "" + data.order_status.toBoolean())
            when {

                (data.order_status.equals("0")) -> {
                    status.text = "處理中"
                }
                (data.order_status.equals("2")) -> {
                    status.text = "已退款"
                }
                (data.order_status.equals("1")) -> {
                    status.text = "已完成"
                }
                (data.order_status.equals("9")) -> {
                    status.text = "已取消"
                }
                else -> {
                    pay.visibility = View.VISIBLE
                }

            }
            view.setOnClickListener { EcoderClick.invoke(data) }
            pay.setOnClickListener { payClick.invoke(data) }

        }

    }


}
/**
 *
 * @Title: CDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: CartAdapter購物車的adapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class CartdisAdapter(private val mData: List<Cart>) :
    RecyclerView.Adapter<CartdisAdapter.mydisHolder>() {
    var Click: (Cart) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartdisAdapter.mydisHolder {
        return mydisHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scart_detail_dicount, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartdisAdapter.mydisHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            name.text = data.Name
            nt.text = data.Nt
            channel_price.text=data.channel_price
            count.text = data.Count
           var a=data.Count?.toInt()
            var b=data. channel_price?.toInt()
            var  c= a!! *b!!
            if(DymaticTabFragment.isDiscount==true){
                total.text=c.toString()
            }else{
                total.text = data.total_amount
            }

            product_picture = data.product_picture.toString()
            product_no = data.product_no.toString()
            Picasso.get().load(ApiUrl.API_URL +"medicalec/"+ product_picture).into(image)
            delete01.setOnClickListener {
                Click.invoke(data)

            }

        }

    }

    inner class mydisHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.sdName)
        var nt: TextView = v.findViewById(R.id.sdNt)
        var channel_price: TextView = v.findViewById(R.id.sdChannel)
        var count: TextView = v.findViewById(R.id.sdCount)
        var image: ImageView = v.findViewById(R.id.sdImage)
        var delete01: TextView = v.findViewById(R.id.sd01)
        var total: TextView = v.findViewById(R.id.sdDollar)
        lateinit var product_picture: String
        lateinit var product_no: String

    }


}