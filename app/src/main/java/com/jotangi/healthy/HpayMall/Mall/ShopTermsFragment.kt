package com.jotangi.healthy.HpayMall.Mall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentShopTermsBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
/**
 *
 * @Title: ShopTermsFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: ShopTermsFragment 購物條款
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class ShopTermsFragment: ProjConstraintFragment() {
    private var _binding: FragmentShopTermsBinding? = null

    companion object {
        const val TAG = "ShopTermsFragment"
        fun newInstance() = ShopTermsFragment()
    }


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopTermsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_shop_terms
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



}