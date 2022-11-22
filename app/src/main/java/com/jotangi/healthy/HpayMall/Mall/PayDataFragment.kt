package com.jotangi.healthy.HpayMall.Mall

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.healthy.HpayMall.ApiUrl
import com.jotangi.healthy.HpayMall.DymaticTabFragment
import com.jotangi.healthy.HpayMall.Mall.Response.Cart
import com.jotangi.healthy.HpayMall.MemberBean
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentPayDataBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
import com.jotangi.healthy.ui.account.MallPayFragment
import com.jotangi.healthy.utils.EditTextUtil.setOnTextChangeListener
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 *
 * @Title: PayDataFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: 付款頁面
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class PayDataFragment : ProjConstraintFragment() {
    private lateinit var binding: FragmentPayDataBinding

    private lateinit var payAdapter: PayAdapter
    private lateinit var paydisAdapter: PaydisAdapter

    private lateinit var area: Spinner
    private lateinit var areaAdapter: ArrayAdapter<String>
    val lists = arrayListOf<Cart>()
    private var priceList: ArrayList<Int> = ArrayList<Int>()
    private var priceDiscountList: ArrayList<Int> = ArrayList<Int>()

    private var listAmount: Int = 0

    private var orderAmount: Int = 0 // 之前RD弄的 現在改 沒在用這個參數了
    private var orderAmountTrue: Int = 0 // 應付金額
    private var orderPay: Int = 0 // 實付金額
    private var couponNo: String = "" // 優惠代碼
    private var subCoupon: Int = 0  // 優惠券折扣
    private var phone: String? = null

    companion object {
        const val TAG = "PayDataFragment"
        fun newInstance() = PayDataFragment()
        var isDiscount_PD = DymaticTabFragment.isDiscount
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayDataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        area = root.findViewById(R.id.pdAspinner)

        binding.apply {
            pdBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pdPay.setOnClickListener {
                pay()
            }
            /*依據發票選擇切換ui高度*/
            pdBillC.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val text: String =
                            binding.pdBillC.getSelectedItem().toString()
                        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
                        when (position) {
                            0 -> {
                                pdBillC.setSelection(0)
                                pdBill001.layoutParams.height = height
                                pdBill001.setText(getString(R.string.pd_22))
                                etInvoiceNumber.layoutParams.height = 0//edittext
                                pdBill003.layoutParams.height = 0
                                etCompanyTitle.layoutParams.height = 0
                                pdBill00302.layoutParams.height = 0
                                etUniformNo.layoutParams.height = 0
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0
                                pdBill00201.layoutParams.height = 0

                            }
                            1 -> {
                                pdBillC.setSelection(1)
                                etInvoiceNumber.layoutParams.height = height//edittext
                                pdBill001.text = getString(R.string.pd_23)
                                pdBill00201.layoutParams.height = height
                                pdBill003.layoutParams.height = 0
                                etCompanyTitle.layoutParams.height = 0
                                pdBill00302.layoutParams.height = 0
                                etUniformNo.layoutParams.height = 0
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0

                            }
                            2 -> {
                                pdBillC.setSelection(2)
                                etInvoiceNumber.layoutParams.height = 0
                                pdBill00201.layoutParams.height = 0
                                pdBill003.layoutParams.height = height
                                etCompanyTitle.layoutParams.height = height//edittext
                                pdBill00302.layoutParams.height = height
                                etUniformNo.layoutParams.height = height//edittext
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0//edittext
                            }
                        }
                    }

                }
            rvInitial()
            SameMember()
            spiSetting()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
        isDiscount_PD = false
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_pay_data
        ResetRecy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun rvInitial() {
        ResetRecy()
    }

    private var listSize: Int = 0

    /*購物車的商品列表*/
    fun ResetRecy() {
        ApiConUtils.shoppingcart_list(
            ApiUrl.API_URL,
            ApiUrl.shoppingcart_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread {
                        Log.e("購物車內商品列表 : ", "  " + jsonString)
                        try {
                            lists.clear()
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val code: String = jsonObject.getString("code")
                                when (code) {
                                    "0x0201" -> {
                                        MemberBean.btnCart = false
                                        /*通知購物車清空，button紅點清除*/
                                        fragmentListener.onAction(
                                            FUNC_FRAGMENT_change,
                                            MemberBean.btnCart
                                        )
                                        binding.rvPay.removeAllViews()
                                        binding.pa2.visibility = View.GONE
                                        binding.pdNodata.visibility = View.VISIBLE
                                        binding.pdNd0.visibility = View.VISIBLE
                                    }
                                }
                            } catch (r: Exception) {

                            }
                            var result: Int = 0
                            var result_discount: Int = 0
                            val jsonArray = JSONArray(jsonString)
                            listSize = jsonArray.length()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray[i] as JSONObject
                                lists.add(
                                    Cart(
                                        Name = jsonObject.getString("product_name"),
                                        Nt = jsonObject.getString("product_price"),
                                        Count = jsonObject.getString("order_qty"),
                                        Dollar = jsonObject.getString("total_amount"),
                                        product_picture = jsonObject.getString("product_picture"),
                                        product_no = jsonObject.getString("product_no"),
                                        channel_price = jsonObject.getString("channel_price"),
                                        group_price = jsonObject.getString("group_price"),
                                    )

                                )
                                result = jsonObject.getString("order_qty")
                                    .toInt() * jsonObject.getString("product_price").toInt()
                                priceList.add(result)
                                result_discount = jsonObject.getString("order_qty")
                                    .toInt() * jsonObject.getString("channel_price").toInt()
                                priceDiscountList.add(result_discount)
                            }
                            if (isDiscount_PD) {
                                paydisAdapter = PaydisAdapter(lists)
                                binding.rvPay.layoutManager = LinearLayoutManager(context)
                                binding.rvPay.adapter = paydisAdapter

                            } else {
                                payAdapter = PayAdapter(lists)
                                binding.rvPay.layoutManager = LinearLayoutManager(context)
                                binding.rvPay.adapter = payAdapter
                                payAdapter.PayItemClick = {}
                            }
                            if (lists.isEmpty()) {
                                binding.pa2.visibility = View.GONE
                                binding.pdNodata.visibility = View.VISIBLE
                                binding.pdNd0.visibility = View.VISIBLE

                            }

                        } catch (e: Exception) {
                        }

                        Log.d("總和是啥", "list_size: " + listSize)
                        var sum: Int = 0
                        for (i in 0 until listSize) {
                            /*價格加總*/
                            sum += priceList[i];
                            Log.d("總和是啥", "priceList.get(i): " + priceList.get(i));
                        }
                        orderAmountTrue = sum
                        Log.d("總和是啥", "onSuccess: $lists")
                        binding.tvOrderAmount.text = "$ $orderAmountTrue"
                        binding.pdM02.text = "$ " + "0"
                        binding.tvCouponDiscount.text = "$ " + "0"
                        binding.tvBounsPoint.text = "$ " + "0"
                        binding.pdM05.text = "$ $orderAmountTrue"
                        orderPay = orderAmountTrue

                        setOnTextChangeListener(binding.pdCoupon) {
                            println("text Changed: " + binding.pdCoupon.text)
                            orderAmount = 0
                            var sum_origin: Int = 0
                            var deliver_fee: Int = 0

                            var sub_bonus: Int = 0
                            var amount: Int = 0
                            if (binding.pdCoupon.text.toString().equals("DIGIMED008")) {
                                paydisAdapter = PaydisAdapter(lists)
                                binding.rvPay.layoutManager = LinearLayoutManager(context)
                                binding.rvPay.adapter = paydisAdapter

                                isDiscount_PD = true
                                couponNo = "DIGIMED008"

                                for (i in 0 until listSize) {
                                    /*價格加總*/
                                    orderAmount += priceDiscountList.get(i)
                                    sum_origin += priceList.get(i)
                                }
                                subCoupon = sum_origin - orderAmount
                                Log.d(
                                    "總和是啥11",
                                    "isDiscount: " + isDiscount_PD + ", sub: " + subCoupon
                                )
//                                binding.tvOrderAmount.text = "$ $orderAmount"
                                binding.pdM02.text = "$ " + "0"
                                binding.tvCouponDiscount.text = "$ " + subCoupon
                                binding.tvBounsPoint.text = "$ " + "0"
                                binding.pdM05.text = "$ " + orderAmount.toString()
                                listAmount = orderAmount
                                orderPay = orderAmount
                                isDiscount_PD = true
                            } else {
                                payAdapter = PayAdapter(lists)
                                binding.rvPay.layoutManager = LinearLayoutManager(context)
                                binding.rvPay.adapter = payAdapter
                                payAdapter.PayItemClick = {}

                                isDiscount_PD = false

                                for (i in 0 until listSize) {
                                    /*價格加總*/
                                    orderAmount += priceList[i];
                                }
                                Log.d("總和是啥12", "isDiscount: " + isDiscount_PD)
//                                binding.tvOrderAmount.text = "$ $orderAmount"
                                binding.pdM02.text = "$ " + "0"
                                binding.tvCouponDiscount.text = "$ " + "0"
                                binding.tvBounsPoint.text = "$ " + "0"
                                listAmount = orderAmount
                                orderPay = orderAmountTrue
                                binding.pdM05.text = "$ $orderPay"
                            }
                        }

                    }


                }

                override fun onFailure(message: String) {
                }
            })
    }

    /*同會員資料勾起的ui設定*/
    private fun SameMember() {

        binding.apply {
            pdSame.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    pdName.setText(MemberBean.member_name)
                    phone =
                        if (MemberBean.member_phone.equals("null") || MemberBean.member_phone.isNullOrEmpty()) {
                            pdPhone.setText(MemberBean.member_id)
                            MemberBean.member_id
                        } else {
                            pdPhone.setText(MemberBean.member_phone)
                            MemberBean.member_phone
                        }
                    if (MemberBean.member_email.equals("null") || MemberBean.member_email.isNullOrEmpty()) {
                        pdEmail.text = null
                    } else {
                        pdEmail.setText(MemberBean.member_email)
                    }


                    pdName.isEnabled = false
                    pdPhone.isEnabled = false

                } else {
                    pdCspinner.isEnabled = true
                    pdAspinner.isEnabled = true
                    pdName.isEnabled = true
                    pdAdress.isEnabled = true
                    pdPhone.isEnabled = true
                    pdEmail.isEnabled = true

                }

            }


        }
    }

    /*地址選擇的adapter*/
    @SuppressLint("ResourceType")
    fun spiSetting() {
        binding.pdCspinner.apply {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                CityData.instance.counties
            )

            setAdapter(adapter)
            setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> adapter2(CityData.instance.districts00)
                        /*
                        * 臺北市
                        * */
                        1 -> adapter2(CityData.instance.districts02)
                        /*
                        * 新北市
                        * */
                        2 -> adapter2(CityData.instance.districts04)
                        /*
                      * 桃園市
                      * */
                        3 -> adapter2(CityData.instance.districts06)
                        /*
                      * 新竹縣
                      * */
                        4 -> adapter2(CityData.instance.districts05)
                        /*
                      * 新竹市
                      * */
                        5 -> adapter2(CityData.instance.districts07)
                        /*
                      * 苗栗縣
                      * */
                        6 -> adapter2(CityData.instance.districts08)
                        /*
                      * 臺中市
                      * */
                        7 -> adapter2(CityData.instance.districts09)
                        /*
                      * 彰化縣
                      * */
                        8 -> adapter2(CityData.instance.districts13)
                        /*
                      * 雲林縣
                      * */
                        9 -> adapter2(CityData.instance.districts12)
                        /*
                      * 嘉義縣
                      * */
                        10 -> adapter2(CityData.instance.districts11)
                        /*
                      * 嘉義市
                      * */
                        11 -> adapter2(CityData.instance.districts14)
                        /*
                      * 臺南市
                      * */
                        12 -> adapter2(CityData.instance.districts15)
                        /*
                      * 高雄市
                      * */
                        13 -> adapter2(CityData.instance.districts18)
                        /*
                      * 屏東縣
                      * */
                        14 -> adapter2(CityData.instance.districts10)
                        /*
                      * 南投縣
                      * */
                        15 -> adapter2(CityData.instance.districts01)
                        /*
                      * 基隆市
                      * */
                        16 -> adapter2(CityData.instance.districts03)
                        /*
                      * 宜蘭縣
                      * */
                        17 -> adapter2(CityData.instance.districts20)
                        /*
                      * 花蓮縣
                      * */
                        18 -> adapter2(CityData.instance.districts19)
                        /*
                      * 臺東縣
                      * */
                        19 -> adapter2(CityData.instance.districts16)
                        /*
                      * 澎湖縣
                      * */
                        20 -> adapter2(CityData.instance.districts21)
                        /*
                      * 連江縣
                      * */
                        21 -> adapter2(CityData.instance.districts17)
                        /*
                      * 金門縣
                      * */


                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })


        }
    }

    fun adapter2(data: Array<String>) {
        binding.pdAspinner.apply {
            areaAdapter =
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, data)
            setAdapter(areaAdapter)
            setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }
    }

    var invoice_type: String? = null

    /*立即付款的api*/
    private fun pay() {

        if (!checkInput()) {
            return
        } else {
            Log.e("invoice_type", "  " + invoice_type)
            var address =
                binding.pdCspinner.selectedItem.toString() + binding.pdAspinner.selectedItem.toString() + binding.pdAdress.text.toString()
            Log.e("address", "  " + address)
            var invoiceNumber: String? = null
            var companyTitle: String? = null
            var uniformNo: String? = null
            var sid: String? = null

            if (invoice_type.equals("2")) {
                invoiceNumber = binding.etInvoiceNumber.text.toString()

            } else {
                invoiceNumber = MemberBean.member_id
            }

            if (invoice_type.equals("3")) {
                companyTitle = binding.etCompanyTitle.text.toString()
                uniformNo = binding.etUniformNo.text.toString()
            } else {
                companyTitle = ""
                uniformNo = ""
            }
//            Log.e("蝦逼",MemberBean.barcode_id)

            sid = if (MemberBean.barcode_id == null) {
                ""; } else {
                MemberBean.barcode_id
            }

            Log.d(
                "安安->忽略ApiUrl跟會員帳密, 請幫我依序傳變數進入下列Api構造方法",
                "\n應付金額: $orderAmountTrue\n優惠券: $couponNo \n折扣金額: $subCoupon \n 點數 : 0 \n實付: $orderPay " +
                        "\n運送方式: 1\n收件name: ${binding.pdName.text} \n收件地址: ${binding.pdAdress.text} " +
                        "\n收件電話: ${binding.pdPhone.text} \n收件人信箱: ${binding.pdEmail.text} \n發票方式:  $invoice_type " +
                        "\ninvoicePhone: ${binding.etInvoiceNumber.text} \n公司名抬頭: ${binding.etCompanyTitle.text}" +
                        "\nuniform: ${binding.etUniformNo.text}\nsid: $sid"
            )
            /*新增訂單的api*/
            ApiConUtils.add_ecorder(ApiUrl.API_URL,
                ApiUrl.add_ecorder,
                MemberBean.member_id,
                MemberBean.member_pwd,
                orderAmountTrue.toString(),
                couponNo,
                subCoupon.toString(),
                "0",
                orderPay.toString(),
                "1",
                binding.pdName.text.toString(),
                address,
                binding.pdPhone.text.toString(),
                binding.pdEmail.text.toString(),
                invoice_type,
                binding.etInvoiceNumber.text.toString(),
                binding.etCompanyTitle.text.toString(),
                binding.etUniformNo.text.toString(),
                sid,
                object : ApiConUtils.OnConnect {
                    @Throws(JSONException::class)
                    override fun onSuccess(jsonString: String) {
                        Log.e("蝦逼", jsonString)
                        Handler(Looper.getMainLooper()).post {
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val status: String = jsonObject.getString("status")
                                val code: String = jsonObject.getString("code")
                                val responseMessage: String =
                                    jsonObject.getString("responseMessage")
                                when (code) {
                                    "0x0200" -> {
                                        AlertDialog.Builder(requireContext()).apply {
                                            setTitle("新增訂單成功")
                                            setMessage(responseMessage)
                                            //                                            setCanceledOnTouchOutside(false)
                                            setNegativeButton("確認") { dialog, _ ->
                                                val fra = MallPayFragment.newInstance()
                                                val data = Bundle()
                                                data.putString("ResOrder", responseMessage)
                                                fra.arguments = data
                                                val transaction: FragmentTransaction =
                                                    requireActivity().supportFragmentManager
                                                        .beginTransaction();
                                                transaction.replace(
                                                    R.id.nav_host_fragment_activity_main,
                                                    fra
                                                )
                                                //                                                    transaction.addToBackStack(DymaticTabFragment.javaClass.getSimpleName())
                                                transaction.commit()
                                                dialog.dismiss()
                                            }
                                            setCancelable(false)
                                        }.create().show()
                                    }
                                    "0x0208" -> {
                                        AlertDialog.Builder(requireContext()).apply {
                                            setTitle("載具號碼不正確")
                                            setMessage(responseMessage)
                                            setNegativeButton("確認") { dialog, _ ->
                                                binding.etInvoiceNumber.text.clear()
                                                dialog.dismiss()
                                            }
                                            setCancelable(true)

                                        }.create().show()
                                    }
                                    else -> {
                                        AlertDialog.Builder(requireContext()).apply {
                                            setTitle("Error :$code")
                                            setMessage(responseMessage)
                                            setNegativeButton("確認") { dialog, _ ->

                                                dialog.dismiss()
                                            }
                                            setCancelable(true)
                                        }.create().show()
                                    }
                                }
                            } catch (e: Exception) {

                            }
                        }
                    }

                    override fun onFailure(message: String) {
                    }
                })
        }
    }

    var dialog: AlertDialog? = null
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

    private fun checkInput(): Boolean {
        binding.apply {
            when {
                pdName.text.isNullOrEmpty() -> {
                    showDialog(
                        "", "請輸入姓名 ",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog!!.dismiss()
                        })
                    return false
                }
                pdAdress.text.isNullOrEmpty() -> {
                    showDialog(
                        "", "請輸入地址 ",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog!!.dismiss()
                        })
                    return false
                }
                pdPhone.text.isNullOrEmpty() -> {
                    showDialog(
                        "", "請輸入電話 ",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog!!.dismiss()
                        })
                    return false
                }
                pdName.text.isNullOrEmpty() -> {
                    showDialog(
                        "", "請輸入姓名",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog!!.dismiss()
                        })
                    return false
                }
                pdEmail.text.isNullOrEmpty() -> {
                    showDialog(
                        "", "請輸入電子發票收件的E-MAIL",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog!!.dismiss()
                        })
                    return false
                }
                pdBillC.selectedItemPosition.equals(0) -> {
//                    when {
//                        binding.pdBill002.text.isNullOrEmpty() -> {
//                            showDialog(
//                                "", "請輸入個人電子發票 ",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    dialog!!.dismiss()
//                                })
//                            return false
//                        }
//
//                    }
                    invoice_type = "1"
                }
                pdBillC.selectedItemPosition.equals(1) -> {
                    when {
                        etInvoiceNumber.text.isNullOrEmpty() -> {
                            showDialog(
                                "", "請輸入個人電子發票 ",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog!!.dismiss()
                                })
                            return false
                        }
                    }
                    invoice_type = "2"
                }
                pdBillC.selectedItemPosition.equals(2) -> {
                    when {
                        etCompanyTitle.text.isNullOrEmpty() -> {
                            showDialog(
                                "", "請輸入公司行號抬頭 ",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog!!.dismiss()
                                })
                            return false
                        }
                        etUniformNo.text.isNullOrEmpty() -> {
                            showDialog(
                                "", "請輸入統一編號 ",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog!!.dismiss()
                                })
                            return false
                        }
//                        binding.pdBill00305.text.isNullOrEmpty() -> {
//                            showDialog(
//                                "", "請輸入E-Mail ",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    dialog!!.dismiss()
//                                })
//                            return false
//                        }
                    }
                    invoice_type = "3"
                }
            }
        }
        return true
    }
}



