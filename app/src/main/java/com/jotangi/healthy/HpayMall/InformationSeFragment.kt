package com.jotangi.healthy.HpayMall

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.jotangi.healthy.HpayMall.Mall.EcoderAdapter
import com.jotangi.healthy.HpayMall.Mall.MallOrderAdapter
import com.jotangi.healthy.HpayMall.Mall.Response.Ecorder_list
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentInformationSeBinding
import com.jotangi.healthy.ui.ProjConstraintFragment

/**
 *
 * @Title: InformationSeFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: 新增需求->先跳使用者消費權益ＱＡ才跳轉商城
 * @author Kelly
 * @date 2022-2
 * @version hpay_32版
 */
class InformationSeFragment : ProjConstraintFragment() {

    private var _binding: FragmentInformationSeBinding? = null
    private lateinit var moAdapter: MallOrderAdapter

    companion object {
        const val TAG = "InformationSeFragment"
        fun newInstance() = InformationSeFragment()
    }

    lateinit var orderNo: String
    private lateinit var ListAdapter: EcoderAdapter

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationSeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.btnIs.setOnClickListener {
            savaTermsStatus(true)
            val transaction: FragmentTransaction =
                requireActivity().getSupportFragmentManager().beginTransaction()
            transaction.replace(
                R.id.nav_host_fragment_activity_main,
                DymaticTabFragment.newInstance()
            )
//            transaction.addToBackStack(DymaticTabFragment.javaClass.getSimpleName())
            transaction.commit()

        }

        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_is
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun savaTermsStatus(status: Boolean) {
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("UserTerms", Context.MODE_PRIVATE)
        pref.edit()
            .putBoolean("IsReadInfo", status)
            .commit()
    }

}