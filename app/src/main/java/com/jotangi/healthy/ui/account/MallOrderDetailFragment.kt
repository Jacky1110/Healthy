package com.jotangi.healthy.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.healthy.HpayMall.ApiUrl
import com.jotangi.healthy.HpayMall.CustomDaialog
import com.jotangi.healthy.HpayMall.Mall.*
import com.jotangi.healthy.HpayMall.Mall.Response.Cart
import com.jotangi.healthy.HpayMall.MemberBean
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentMallOrderDetailBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 *
 * @Title: MallOrderDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: MallOrderDetailFragment商城訂單部分
 * @author Kelly
 * @date 2021-12
 * @version hpay_32版
 */
class MallOrderDetailFragment : ProjConstraintFragment() {

    private var _binding: FragmentMallOrderDetailBinding? = null
    val lists = arrayListOf<Cart>()
    private lateinit var cartAdapter: PayAdapter
    private lateinit var carDisAdapter:PaydisAdapter
    var isDiscount = PayDataFragment.isDiscount_PD

    companion object {
        const val TAG = "MallOrderDetailFragment"
        fun newInstance() = MallOrderDetailFragment()
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMallOrderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val data: Bundle? = arguments

        if (data != null) {
            requireActivity().runOnUiThread {

                val mallNo = data.getString("Mallorder").toString()
                binding.rcPdd.removeAllViews()
                ecorder_info(mallNo)
            }

        }
        binding.apply {


        }
        //rvInitial()
        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.account_listitem_order
        if (!isDiscount) {
            binding.rcPdd.apply {
                cartAdapter = PayAdapter(lists)
                layoutManager = LinearLayoutManager(context)
                adapter = cartAdapter
            }

        } else {
            binding.rcPdd.apply {
                carDisAdapter = PaydisAdapter(lists)
                layoutManager = LinearLayoutManager(context)
                adapter = carDisAdapter
            }

        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    /*
    * setting txt result
    * */
    fun txtSetting(
        pdtotal: String,
        pdfreight: String, pdcoupondis: String, pdbonusdis: String, pdneedpay: String
    ) {
        binding.fdTotal.text = pdtotal
        binding.fdDe.text = pdfreight
        binding.fdCou.text = pdcoupondis
        binding.fdBon.text = pdbonusdis
        binding.fdRes.text = pdneedpay
    }

    fun DetailValue(
        Id: String,
        Date: String,
        Count: String,
        OrderStatus: String,
        PayStatus: String,
        Send: String
    ) {
        binding.fdID.setText(Id)
        binding.fdDate.setText(Date)
        binding.fdCount.setText(Count)
        binding.fdOstatus.setText(OrderStatus)
        binding.fdPstatus.setText(PayStatus)
        binding.fdDeliever.setText(Send)

    }

    private var priceList: ArrayList<Int> = ArrayList<Int>()
    private var priceDiscountList: ArrayList<Int> = ArrayList<Int>()
    private var list_size: Int = 0

    private var sum: Int = 0
    private var sum_origin: Int = 0
    private var deliver_fee: Int = 0
    private var sub_coupon: Int = 0
    private var sub_bonus: Int = 0
    private var amount: Int = 0

    private fun ecorder_info(No: String) {
        val errCode = arrayOf(
            "0x0201", "0x0202", "0x0203", "0x0204", "0x0205"
        )
        var result: Int = 0
        var result_discount: Int = 0
        val id = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        val no = No
        if (id != null && pwd != null) {
            ApiConUtils.ecorder_info(
                ApiUrl.API_URL,
                ApiUrl.ecorder_info,
                id,
                pwd, no,
                object : ApiConUtils.OnConnect {
                    override fun onSuccess(jsonString: String) {
                        activity!!.runOnUiThread {
                            Log.e("Mall's   info 細節", "ecorder_list $jsonString")
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.getString("status")
                                val code = jsonObject.getString("code")
                                val responseMessage = jsonObject.getString("responseMessage")
                                when {
                                    (listOf<String>(*errCode).contains(code)) -> {
                                        CustomDaialog.showNormal(
                                            requireActivity(),
                                            "",
                                            "$code  $responseMessage",
                                            "確定",
                                            object : CustomDaialog.OnBtnClickListener {
                                                override fun onCheck() {}
                                                override fun onCancel() {
                                                    requireActivity().onBackPressed()
                                                    CustomDaialog.closeDialog()
                                                }
                                            })
                                    }

                                }
                            } catch (e: Exception) {
                            }
                            try {
                                requireActivity().runOnUiThread {
                                    binding.rcPdd.apply {
                                        val jA = JSONArray(jsonString)
                                        for (i in 0 until jA.length()) {
                                            val pro = jA[i] as JSONObject
                                            binding.fdID.text = pro.getString("order_no")
                                            binding.fdDate.text =
                                                pro.getString("order_date")
                                            binding.fdCount.text =
                                                pro.getString("order_amount")
                                            binding.fdTotal.text = "$${pro.getString("order_amount")}"
                                            binding.fdDe.text = "$" + "0"
                                            binding.fdCou.text = "$${pro.getString("discount_amount")}"
                                            binding.fdBon.text = "$" + "0"
                                            binding.fdRes.text = "$${pro.getString("order_pay")}"
                                            statusText(
                                                pro.getString("order_status"),
                                                pro.getString("pay_status"),
                                                pro.getString("deliverystatus")
                                            )
                                            try {
                                                val jsonArray = pro.getJSONArray("product_list")
                                                list_size = jsonArray.length()
                                                for (i in 0 until jsonArray.length()) {
                                                    val proAray = jsonArray[i] as JSONObject

                                                    lists.add(
                                                        Cart(
                                                            Name = proAray.getString("product_name"),
                                                            Nt = proAray.getString("product_price"),
                                                            Count = proAray.getString("order_qty"),
                                                            Dollar = proAray.getString("total_amount"),
                                                            product_picture = proAray.getString("product_picture"),
                                                            product_no = proAray.getString("product_no"),
                                                            channel_price = proAray.getString("channel_price"),
                                                            group_price = proAray.getString("group_price"),
                                                        )
                                                    )
                                                    result = proAray.getString("order_qty").toInt() * proAray.getString("product_price").toInt()
                                                    priceList.add(result)
                                                    result_discount = proAray.getString("order_qty").toInt() * proAray.getString("channel_price").toInt()
                                                    priceDiscountList.add(result_discount)
                                                    Log.d("是啥", "product: " + proAray.getString("product_price") + ", channel: " + proAray.getString("channel_price") + ", group: " + proAray.getString("group_price") + ", total: " + proAray.getString("total_amount"))
                                                }
                                            } catch (e: Exception) {
                                                CustomDaialog.showNormal(
                                                    requireActivity(),
                                                    "",
                                                    "很抱歉，訂單異常",
                                                    "謝謝",
                                                    object : CustomDaialog.OnBtnClickListener {
                                                        override fun onCheck() {}
                                                        override fun onCancel() {
                                                            requireActivity().onBackPressed()
                                                            CustomDaialog.closeDialog()
                                                        }
                                                    })
                                            }
                                        }

//                                        if (isDiscount) {
//                                            for (i in 0 until list_size) {
//                                                /*價格加總*/
//                                                sum += priceDiscountList.get(i)
//                                                sum_origin += priceList.get(i)
//                                                Log.d("是啥", "priceDiscount: " + priceDiscountList.get(i) + ", price" + priceList.get(i))
//                                            }
//                                            sub_coupon = sum_origin - sum
//                                            Log.e("是啥", "sum_origin: " + sum_origin + ", sum: " + sum + ", isDiscount: " + isDiscount + ", sub: " + sub_coupon)
//                                            binding.fdTotal.text = "$" + sum_origin.toString()
//                                            binding.fdDe.text = "$" + "0"
//                                            binding.fdCou.text = "$" + sub_coupon
//                                            binding.fdBon.text = "$" + "0"
//                                            binding.fdRes.text = "$" + sum.toString()
//
//                                            carDisAdapter.notifyDataSetChanged()
//                                        } else {
//                                            for (i in 0 until list_size) {
//                                                /*價格加總*/
//                                                sum += priceList.get(i);
//                                            }
//                                            Log.e("是啥", "sum: " + sum + ", isDiscount: " + isDiscount + ", sub: " + sub_coupon)
//                                            binding.fdTotal.text = "$" + sum.toString()
//                                            binding.fdDe.text = "$" + "0"
//                                            binding.fdCou.text = "$" + "0"
//                                            binding.fdBon.text = "$" + "0"
//                                            binding.fdRes.text = "$" + sum.toString()
//
//                                            cartAdapter.notifyDataSetChanged()
//                                        }
                                    }

                                }
                            } catch (e: Exception) {

                            }
                        }

                    }

                    override fun onFailure(message: String) {}
                })
        }
    }

    private fun statusText(s1: String, s2: String, s3: String) {
        when (s1) {
            "0" -> {
                binding.fdOstatus.text = "處理中"
            }
            "1" -> {
                binding.fdOstatus.text = "完成"
            }
            "2" -> {
                binding.fdOstatus.text = "取消"
            }
            "-1" -> {
                binding.fdPstatus.text = "等待處理"
            }
            else -> {
                binding.fdPstatus.text = "等待處理"
            }
        }
        when (s2) {
            "0" -> {
                binding.fdPstatus.text = "未付款"
            }
            "1" -> {
                binding.fdPstatus.text = "已付款"
            }
            "-1" -> {
                binding.fdPstatus.text = "尚未付款"
            }
            "9" -> {
                binding.fdPstatus.text = "退款"
            }
            else -> {
                binding.fdPstatus.text = "尚未付款"
            }
        }
        when (s3) {
            "0" -> {
                binding.fdDeliever.text = "尚未出貨"
            }
            "1" -> {
                binding.fdDeliever.text = "已出貨"
            }
            "-1" -> {
                binding.fdPstatus.text = "等待出貨"
            }
            else -> {
                binding.fdPstatus.text = "等待出貨"
            }
        }
    }

}