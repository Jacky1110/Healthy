package com.jotangi.healthy.HpayMall.Mall

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jotangi.healthy.HpayMall.*
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentCDetailBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
import com.jotangi.healthy.utils.ProjSharePreference
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils.OnConnect
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 *
 * @Title: CDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: 商品詳細頁面
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class CDetailFragment : ProjConstraintFragment() {
    private lateinit var binding: FragmentCDetailBinding
    private lateinit var WatchAdapter: WatchAdapter
    private var cdInt: Int = 0
    var isDiscount = DymaticTabFragment.isDiscount
    var channel_Price: String? = null

    companion object {
        const val TAG = "CDetailFragment"
        fun newInstance() = CDetailFragment()
    }

    lateinit var proNo: String
    lateinit var proDollar: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val data: Bundle? = arguments

        if (data != null) {
            binding.apply {

            }
            binding.cdname.setText(data.getString("Cname"))
            binding.cdDollar.setText("$" + data.getString("Cdollar"))
            proDollar = data.getString("Cdollar").toString()
            proNo = data.getString("Cno").toString()
            channel_Price = data.getString("Cdis").toString()
            data.getString("Cno")?.let {
                GetProInfo(it)
            }
        }
        /*加入購物車*/
        binding.cdAdd.setOnClickListener {
            if (ProjSharePreference.getLoginState(requireContext())) {
                AddShopping_Cart(1)
                MemberBean.btnCart = true
                fragmentListener.onAction(FUNC_FRAGMENT_change, MemberBean.btnCart)
            } else {
                Toast.makeText(
                    requireContext(),
                    "請先登入帳號",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.cdBuy.setOnClickListener {
            if (ProjSharePreference.getLoginState(requireContext())) {
                PurchaseNow()
            } else {
                Toast.makeText(
                    requireContext(),
                    "請先登入帳號",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.apply {
            cdCount.text = "1"
            var cou = Integer.parseInt(cdCount.text.toString());
            //counting product amount
            cdPlus.setOnClickListener {
                /*加商品數目*/
                cou = cou + 1
                requireActivity().runOnUiThread {
                    cdCount.text = cou.toString()
                }
            }
            cdReduce.setOnClickListener {
                /*減商品數目*/
                when {
                    cou > 0 -> {
                        cou = cou - 1
                        requireActivity().runOnUiThread {
                            cdCount.text = cou.toString()
                        }
                    }
                    else -> {
                        requireActivity().runOnUiThread(Runnable {
                            CustomDaialog.showNormal(
                                requireActivity(),
                                "",
                                "商品數目為0，請重新確認",
                                "謝謝",
                                object : CustomDaialog.OnBtnClickListener {
                                    override fun onCheck() {}
                                    override fun onCancel() {
                                        CustomDaialog.closeDialog()
                                    }
                                })
                        })
                    }
                }


            }


        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_commodity
        if (isDiscount) {
            binding.apply {
                cdT1.visibility = View.VISIBLE
                cdT2.visibility = View.VISIBLE
                cdT2.text = channel_Price
            }
        } else {
            binding.apply {
                cdT1.visibility = View.INVISIBLE
                cdT2.visibility = View.INVISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /*立刻購買*/
    fun PurchaseNow() {
        requireActivity().runOnUiThread {
            AddShopping_Cart(2)

        }

    }

    /*if type.equal("1")->Api:success,code=""0x0200,will show success message*/
    //type.equal("2")->Api:success,code=""0x0200,do nothing
    private fun AddShopping_Cart(type: Int) {
        val id = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        val orderQty = binding.cdCount.text
        var a: Int? = null
        if (isDiscount) {
            //a= Integer.parseInt(channel_Price)
            a = Integer.parseInt(proDollar)

        } else {
            a = Integer.parseInt(proDollar)

        }
        val b: Int = Integer.parseInt(orderQty.toString())
        val total_amount = a * b
        ApiConUtils.add_shoppingcart(
            ApiUrl.API_URL,
            ApiUrl.add_shoppingcart,
            id,
            pwd,
            proNo, a, b, total_amount,
            object : OnConnect {
                override fun onSuccess(jsonString: String) {
                    activity!!.runOnUiThread {
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String = jsonObject.getString("responseMessage")
                            when {
                                type.equals(1) -> {
                                    if (code.equals("0x0200")) {
                                        showDialog(
                                            "", "加入購物車成功 ",
                                            DialogInterface.OnClickListener { dialog, which ->
                                                dialog!!.dismiss()
                                            })
                                    } else {
                                        showDialog(
                                            "Error ", responseMessage,
                                            DialogInterface.OnClickListener { dialog, which ->
                                                dialog!!.dismiss()
                                            })
                                    }
                                }

                                type.equals(2) -> {
                                    if (!code.equals("0x0200")) {

                                        showDialog(
                                            "Error ", responseMessage,
                                            DialogInterface.OnClickListener { dialog, which ->
                                                dialog!!.dismiss()
                                            })
                                    } else {
                                        if (fragmentListener != null) {
                                            MemberBean.btnCart = true
                                            fragmentListener.onAction(
                                                FUNC_FRAGMENT_change,
                                                MemberBean.btnCart
                                            )

                                            fragmentListener.onAction(fraShoppingCart, null)
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {

                        }

                    }
                }

                override fun onFailure(message: String) {
                    activity!!.runOnUiThread {
                    }
                }
            })
    }

    private var dialog: AlertDialog? = null
    private fun showDialog(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {

        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
        }
        dialog = AlertDialog.Builder(requireContext()).create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setTitle(title)
        dialog!!.setMessage(message)
        dialog!!.setButton(
            AlertDialog.BUTTON_NEUTRAL, "確認",
            DialogInterface.OnClickListener { dialog, which -> listener.onClick(dialog, which) })
        dialog!!.show()
    }

    /*商品資訊*/
    private fun GetProInfo(type: String) {
        ApiConUtils.product_info(
            ApiUrl.API_URL,
            ApiUrl.product_info,
//            MemberBean.member_id,
//            MemberBean.member_pwd,
            type,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        Log.e("商品資訊", "  " + jsonString + "\r\n")
                        val jsonArray = JSONArray(jsonString)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray[i] as JSONObject
                            var product_description = jsonObject.getString("product_description")
                            var product_picture = jsonObject.getString("product_picture")
                            var product_name = jsonObject.getString("product_name")
                            binding.apply {
                                val ProUrl = ApiUrl.API_URL
                                cdDes.setText(product_description)
                                cdname.setText(product_name)
                                Picasso.get().load(ProUrl + "medicalec/" + product_picture)
                                    .into(cdBcard)
                            }
                        }
                    })
                }

                override fun onFailure(message: String) {
                }
            })
    }

}