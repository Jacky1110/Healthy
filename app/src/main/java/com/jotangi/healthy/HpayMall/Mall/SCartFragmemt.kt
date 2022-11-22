package com.jotangi.healthy.HpayMall.Mall

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.healthy.HpayMall.ApiUrl
import com.jotangi.healthy.HpayMall.CustomDaialog
import com.jotangi.healthy.HpayMall.CustomDaialog.OnBtnClickListener
import com.jotangi.healthy.HpayMall.DymaticTabFragment
import com.jotangi.healthy.HpayMall.Mall.Response.Cart
import com.jotangi.healthy.HpayMall.Mall.Response.Cartdis
import com.jotangi.healthy.HpayMall.Mall.Response.Product_list
import com.jotangi.healthy.HpayMall.MemberBean
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentScartBinding
import com.jotangi.healthy.googlevision.CameraXPreviewFragment
import com.jotangi.healthy.ui.ProjBaseFragment
import com.jotangi.healthy.ui.ProjConstraintFragment
import com.jotangi.healthy.ui.account.MallPayFragment
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils.OnConnect
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @Title: SCartFragmemt.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: SCartFragmemt 購物車細節的頁面
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class SCartFragmemt : ProjConstraintFragment() {
    private var _binding: FragmentScartBinding? = null
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartdisAdapter: CartdisAdapter
    var isReset: Boolean = false
    lateinit var no: String
    var isDiscount = DymaticTabFragment.isDiscount

    companion object {
        const val TAG = "SCartFragmemt"
        fun newInstance() = SCartFragmemt()
    }

    private val binding get() = _binding!!
    val lists = arrayListOf<Cart>()
    val list2 = arrayListOf<Cartdis>()
    private var priceList: ArrayList<Int> = ArrayList<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {
            scC1.setOnClickListener {
                // ClientManager.instance.init()
                /*付款條款頁面*/

                if (fragmentListener != null) {
                    fragmentListener.onAction(fraShopTerms, null)
                }
            }
            scPay.setOnClickListener {
                /*立即付款頁面 */
                when {
                    lists.isEmpty() -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("沒有商品")
                            setNegativeButton("確認") { dialog, _ ->

                                dialog.dismiss()
                            }
                            setCancelable(true)
                        }.create().show()
                    }
                    scC.isChecked == false -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("請確認是否已詳細閱讀購物條款")
                            setNegativeButton("確認") { dialog, _ ->

                                dialog.dismiss()
                            }
                            setCancelable(true)
                        }.create().show()

                    }
                    else -> {
                        if (fragmentListener != null) {
                            fragmentListener.onAction(fraPayData, null)
                        }
                    }
                }


            }
            scDelete.setOnClickListener {
                clearShoppingCart()
            }
            scContinue.setOnClickListener {
                /*繼續購物頁面*/
                if (fragmentListener != null) {
                    fragmentListener.onAction(fraDymaticTab, null)
                }

            }
            rvScart.apply {


            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_shopcart
        ResetRecy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /*修改購物車部分->刪除*/
    private fun EditShopping_Cart(proNo: String) {

        ApiConUtils.del_shoppingcart(
            ApiUrl.API_URL,
            ApiUrl.del_shoppingcart,
            MemberBean.member_id,
            MemberBean.member_pwd,
            proNo,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        Log.e("修改購物車內商品數量 : ", "  " + jsonString)
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String = jsonObject.getString("responseMessage")
                            when {
                                (code.equals("0x0200")) -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("")
                                        setMessage("修改購物車商品成功")
                                        setNegativeButton("確認") { dialog, _ ->
                                            Pshow()
                                            ResetRecy()
                                            dialog.dismiss()
                                        }
                                        setCancelable(true)
                                    }.create().show()
                                }
                                else -> {
                                    ResetRecy()
                                    Pnoshow()
                                }
                            }

                        } catch (e: Exception) {
                        }
                    })
                }

                override fun onFailure(message: String) {}
            })


    }

    /*購物車列表的api*/
    private fun ResetRecy() {
        Log.e("購物車內參數 : ", "  " + MemberBean.member_id + " " + MemberBean.member_pwd)

        ApiConUtils.shoppingcart_list(
            ApiUrl.API_URL,
            ApiUrl.shoppingcart_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        Log.e("購物車內商品列表 : ", "  " + jsonString)
                        try {
                            lists.clear()
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val code: String = jsonObject.getString("code")
                                when {
                                    code.equals("0x0201") -> {
                                        MemberBean.btnCart = false
                                        fragmentListener.onAction(
                                            FUNC_FRAGMENT_change, MemberBean.btnCart
                                        )
                                        binding.rvScart.removeAllViews()
                                        Nodata()
                                        Pnoshow()
                                    }
                                }
                            } catch (r: Exception) {
                            }
                            var result: Int = 0
                            val jsonArray = JSONArray(jsonString)
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray[i] as JSONObject
                                lists.add(
                                    Cart(
                                        Name = jsonObject.getString("product_name"),
                                        Nt = jsonObject.getString("product_price"),
                                        Count = jsonObject.getString("order_qty"),
                                        Dollar = jsonObject.getString("total_amount"),
                                        total_amount = jsonObject.getString("total_amount"),
                                        product_picture = jsonObject.getString("product_picture"),
                                        product_no = jsonObject.getString("product_no"),
                                        channel_price = jsonObject.getString("channel_price"),
                                        group_price = jsonObject.getString("group_price"),
                                    )
                                )


                                result = jsonObject.getString("total_amount").toInt()
                                priceList.add(result)


                            }


//                               result =result.plus(result)
                            var sum: Int = 0
                            for (i in 0 until priceList.size) {
                                /*價格加總*/
                                sum += priceList.get(i);
                            }

                            Log.e("是啥", " " + sum)
                            if (!isDiscount) {
                                cartAdapter = CartAdapter(lists)
                                binding.rvScart.layoutManager = LinearLayoutManager(context)
                                binding.rvScart.adapter = cartAdapter
                                Pnoshow()
                                cartAdapter.cartItemClick = {
                                    isReset == true
                                    no = it.product_no.toString()
                                    EditShopping_Cart(it.product_no.toString())
                                }
                            } else {
                                cartdisAdapter = CartdisAdapter(lists)
                                binding.rvScart.layoutManager = LinearLayoutManager(context)
                                binding.rvScart.adapter = cartdisAdapter
                                Pnoshow()
                                cartdisAdapter.Click = {
                                    isReset == true
                                    no = it.product_no.toString()
                                    EditShopping_Cart(it.product_no.toString())
                                }

                            }

                            if (lists.isEmpty()) {
                                Nodata()
                            }

                        } catch (e: Exception) {
                        } finally {

                        }


                    })


                }

                override fun onFailure(message: String) {
                }
            })
    }

    private fun Pshow() {
        requireActivity().runOnUiThread(Runnable {
            val pb: ProgressBar =
                requireActivity().findViewById(R.id.progressBar)
            pb.visibility = View.VISIBLE
            requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        })
    }

    private fun Pnoshow() {
        requireActivity().runOnUiThread(Runnable {
            val pb: ProgressBar =
                requireActivity().findViewById(R.id.progressBar)
            pb.visibility = View.GONE
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })


    }

    private fun Nodata() {
        requireActivity().runOnUiThread {
            val img: ImageView =
                requireActivity().findViewById(R.id.scNodata)
            val imgt: TextView =
                requireActivity().findViewById(R.id.scNd0)
            binding.rvScart.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
            imgt.visibility = View.VISIBLE

        }
    }

    /*清空購物車*/
    private fun clearShoppingCart() {

        ApiConUtils.clear_shoppingcart(
            ApiUrl.API_URL,
            ApiUrl.clear_shoppingcart,
            MemberBean.member_id,
            MemberBean.member_pwd,

            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        Log.e("修改購物車內商品數量 : ", "  " + jsonString)
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String = jsonObject.getString("responseMessage")
                            when {
                                (code.equals("0x0200")) -> {

                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("")
                                        setMessage("清空購物車商品成功")
                                        setNegativeButton("確認") { dialog, _ ->
                                            Pshow()
                                            ResetRecy()
                                            dialog.dismiss()
                                        }
                                        setCancelable(true)
                                    }.create().show()
                                }
                                else -> {

                                    ResetRecy()
                                    Pnoshow()
                                }
                            }

                        } catch (e: Exception) {
                        }


                    })


                }

                override fun onFailure(message: String) {
                }
            })


    }
//    private fun CheckStoreInfo() {
//        val sid = MemberBean.barcode_id
//        ApiConUtils.store_info(
//            ApiUrl.API_URL,
//            ApiUrl.store_info,
//            MemberBean.member_id,
//            MemberBean.member_pwd,
//            sid,
//            object : OnConnect {
//                override fun onSuccess(jsonString: String) {
//                    if (activity != null) {
//                        activity!!.runOnUiThread {
//                            var channel_price = ""
//                            Log.d("登入", "CheckStoreInfo()$jsonString")
//                            try {
//                                val jsonArray = JSONArray(jsonString)
//                                for (i in 0 until jsonArray.length()) {
//                                    val jo = jsonArray[i] as JSONObject
//                                    channel_price = jo.getString("channel_price")
//                                }
//                                val data = Bundle()
//                                if (channel_price == "1") {
//                                  isDiscount=true
//                                } else {
//                                 isDiscount=false
//                                }
//                                ResetRecy()
//                            } catch (e: java.lang.Exception) {
//                            }
//
//                        }
//                    }
//                }
//
//                override fun onFailure(message: String) {
//                    activity!!.runOnUiThread { }
//                }
//            })
//    }
}