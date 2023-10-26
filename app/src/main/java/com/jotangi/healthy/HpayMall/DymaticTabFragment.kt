package com.jotangi.healthy.HpayMall

import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jotangi.healthy.HpayMall.Mall.CDetailFragment
import com.jotangi.healthy.HpayMall.Mall.ProListAdapter
import com.jotangi.healthy.HpayMall.Mall.ProListDiscountAdapter
import com.jotangi.healthy.HpayMall.Mall.Response.*
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentDymaticTabBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
import com.jotangi.healthy.utils.RingProgrseeBar
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils.OnConnect
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule


/**
 *
 * @Title: DymaticTabFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: 新增需求:健康商城，上方動態tab，下方商品列表
 * @author Kelly
 * @date 2021-12
 * @version hpay_32版
 */

class DymaticTabFragment : ProjConstraintFragment() {

    //private var _binding: FragmentDymaticTabBinding? = null
    private lateinit var ProListAdapter: ProListAdapter
    private lateinit var discountAdapter: ProListDiscountAdapter

    val ProTypeMap = HashMap<Int, String>()
    val ProListMap = HashMap<Int, String>()
    val listsPL = arrayListOf<Product_list>()
    val istrue: Boolean = false
    val lists = arrayListOf<Product_type>()


    companion object {
        const val TAG = "DymaticTabFragment"
        fun newInstance() = DymaticTabFragment()
        var isDiscount: Boolean = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDymaticTabBinding.inflate(inflater, container, false)

        val root: View? = binding.root
        //Pshow()
//        when {
//            MemberBean.member_id.isNullOrEmpty() || MemberBean.member_pwd.isNullOrEmpty() -> {
//                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null)
//            }
//        }
        val isGetLogin: SharedPreferences =
            requireActivity().getSharedPreferences("UserTerms", Context.MODE_PRIVATE)
        val IsRead = isGetLogin.getBoolean("IsReadInfo", false)
//
        if (!IsRead) {
            val transaction: FragmentTransaction =
                requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(
                R.id.nav_host_fragment_activity_main,
                InformationSeFragment.newInstance()
            )
//            transaction.addToBackStack(DymaticTabFragment.javaClass.getSimpleName())
            transaction.commit()
        } else {

        }

        /*確保登入後才能查詢上方動態列表*/
        if (istrue != true) {
//            GetProType()
            if (MemberBean.barcode_id != null) {
                CheckStoreInfo()
            }
        }
        Timer().schedule(500) {
            requireActivity().runOnUiThread {
                if (isDiscount) {
                    binding.rT.apply {
                        discountAdapter = ProListDiscountAdapter(listsPL)
                        layoutManager = LinearLayoutManager(context)
                        adapter = discountAdapter
                    }
                } else {
                    binding.rT.apply {
                        ProListAdapter = ProListAdapter(listsPL)
                        layoutManager = LinearLayoutManager(context)
                        adapter = ProListAdapter
                    }
                }

                binding?.dyT.apply {
                    getTabAt(0)?.select()
                    //GetProList ("c1")->query Api:product_list
                    val set: Set<Int> = ProTypeMap.keys
                    val it: Iterator<Int> = set.iterator()
                    while (it.hasNext()) {
                        val key: Int = it.next()
                        val value: String? = ProTypeMap.get(0)
                        value?.let { it1 ->
//                            GetProList(it1)
                        }
                    }
//                    istrue == false
                }
            }

        }
        val btncart: ImageButton = requireActivity().findViewById(R.id.btn_car)
//        if (MemberBean.member_id == null && MemberBean.member_pwd == null) {
//            btncart.visibility = View.VISIBLE
//        } else {
//            btncart.visibility = View.VISIBLE
//        }

        return root
    }

    private lateinit var binding: FragmentDymaticTabBinding
    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_mall

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
        MemberBean.channel_id = 3
    }

    lateinit var tab1: String
    private fun tabSetting(tabList: ArrayList<Product_type>) {
        for (i in 0 until tabList.size) {
            val tText = tabList[i].product_type + ":" + tabList[i].producttype_name
            tabList[i].product_type?.let { ProTypeMap.put(i, it) }
            val tab: String = tText.substringAfterLast(":")
            tab1 = tText.substringBeforeLast(":")
            binding.dyT.apply {
                addTab(binding.dyT.newTab().setText(tab))
                Pnoshow()
                addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        listsPL.clear()
                        if (isDiscount) {
                            discountAdapter.notifyDataSetChanged()

                        } else {
                            ProListAdapter.notifyDataSetChanged()
                        }
                        var position = tab?.position
                        val set: Set<Int> = ProTypeMap.keys
                        val it: Iterator<Int> = set.iterator()
                        while (it.hasNext()) {
                            val key: Int = it.next()
                            if (position == key) {
                                val value: String? = ProTypeMap.get(position)
                                value?.let { it1 ->
//                                    GetProList(it1)
                                }
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                })
            }
        }
        //  }
    }

    //CDetailFragment() will show  product detail
    /*商品個別細節查詢*/
    fun tabContext(listPL: ArrayList<Product_list>) {
        requireActivity().runOnUiThread {
            try {
                if (isDiscount) {
                    discountAdapter.disClick = {
                        val fragment = CDetailFragment()
                        val args = Bundle()
                        args.putString("Cname", it.product_name.toString())
                        args.putString("Cdollar", it.product_price.toString())
                        args.putString("Cno", it.product_no.toString())
                        args.putString("Cdis", it.channel_price.toString())
                        fragment.setArguments(args)
                        val transaction: FragmentTransaction =
                            requireActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                        transaction.commit()
                    }
                } else {
                    ProListAdapter.WatchItemClick = {
                        val fragment = CDetailFragment()
                        val args = Bundle()
                        args.putString("Cname", it.product_name.toString())
                        args.putString("Cdollar", it.product_price.toString())
                        args.putString("Cno", it.product_no.toString())
                        args.putString("Cdis", it.channel_price.toString())
                        fragment.setArguments(args)
                        val transaction: FragmentTransaction =
                            requireActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                        transaction.commit()

                    }
                }

            } catch (e: Exception) {
            } finally {
                Pnoshow()
            }
        }
    }

    fun progressTest() {
        binding.ring.apply {
            setProgressFormatter(object : RingProgrseeBar.ProgressFormatter {
                override fun format(progress: Int, max: Int): CharSequence? {
                    return progress.toString() + "%"
                }
            })
            setOnClickListener {
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        setProgressTextColor(Color.RED);
                        setProgressStartColor(Color.GREEN)

                        setDrawBackgroundOutsideProgress(true)
                    }

                }
            }
        }
    }

    private fun simulateProgress() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.ring.setProgress(progress)

        }
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = 4000
        animator.start()
    }

    /*上方商品列表項目查詢*/
    fun GetProType() {
        val accout = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        //Api: product_type  ->will dymatic add tab in tabLayout
        ApiConUtils.product_type(
            ApiUrl.API_URL, ApiUrl.product_type,
//            accout,
//            pwd,
            object : OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    activity!!.runOnUiThread {
                        val jsonArray = JSONArray(jsonString)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray[i] as JSONObject
                            lists.add(
                                Product_type(
                                    producttype_name = jsonObject.getString("producttype_name"),
                                    product_type = jsonObject.getString("product_type"),
                                )
                            )
                        }
                        tabSetting(lists)
                    }
                }

                override fun onFailure(message: String) {
                }
            })
    }

    /*商品分類列表個別查詢*/
    private fun GetProList(type: String) {
        Pshow()
        ApiConUtils.product_list(
            ApiUrl.API_URL,
            ApiUrl.product_list,
//            MemberBean.member_id,
//            MemberBean.member_pwd,
            object : OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            listsPL.clear()
                            try {
                                val data: Bundle? = arguments
                                val jsonArray = JSONArray(jsonString)
                                for (i in 0 until jsonArray.length()) {
                                    val jsonObject = jsonArray[i] as JSONObject
                                    val proType: String = jsonObject.getString("product_type")
                                    when {
                                        type.equals(proType) -> {
                                            listsPL.add(
                                                Product_list(
                                                    product_type = jsonObject.getString("product_type"),
                                                    product_name = jsonObject.getString("product_name"),
                                                    product_price = jsonObject.getString("product_price"),
                                                    product_no = jsonObject.getString("product_no"),
                                                    product_picture = jsonObject.getString("product_picture"),
                                                    channel_price = jsonObject.getString("channel_price"),
                                                )
                                            )
                                        }
                                    }
                                }
                                if (isDiscount) {
                                    discountAdapter.notifyDataSetChanged()

                                } else {
                                    ProListAdapter.notifyDataSetChanged()

                                }
//
                            } catch (e: Exception) {
                                Pnoshow()
                            } finally {
                                tabContext(listsPL)
                            }
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Pnoshow()
                }
            }
        )
    }

    private fun Pshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding.pb2.visibility = View.VISIBLE
            }
        }
    }

    private fun Pnoshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.rT.visibility = View.VISIBLE
                binding.pb2.visibility = View.GONE
            }
        }
    }

    private fun CheckStoreInfo() {
        val sid = MemberBean.barcode_id
        ApiConUtils.store_info(
            ApiUrl.API_URL,
            ApiUrl.store_info,
            MemberBean.member_id,
            MemberBean.member_pwd,
            sid,
            object : ApiConUtils.OnConnect {
                override fun onSuccess(jsonString: String) {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            var channel_price = ""
                            Log.d("登入", "CheckStoreInfo()$jsonString")
                            try {
                                val jsonArray = JSONArray(jsonString)
                                for (i in 0 until jsonArray.length()) {
                                    val jo = jsonArray[i] as JSONObject
                                    channel_price = jo.getString("channel_price")
                                }
                                val data = Bundle()
                                if (channel_price == "1") {
                                    isDiscount = true
                                } else {
                                    isDiscount = false
                                }

                            } catch (e: java.lang.Exception) {
                            }

                        }
                    }
                }

                override fun onFailure(message: String) {
                    activity!!.runOnUiThread { }
                }
            })
    }
}
