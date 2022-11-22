package com.jotangi.healthy.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.healthy.HpayMall.CustomDaialog
import com.jotangi.healthy.HpayMall.CustomDaialog.OnBtnClickListener
import com.jotangi.healthy.R
import com.jotangi.healthy.databinding.FragmentMallOrderBinding
import com.jotangi.healthy.ui.ProjConstraintFragment
/**
 *
 * @Title:
 * @Package com.jotangi.healthy.HpayMall
 * @Description:
 * @author Kelly
 * @date 2022-02-07
 * @version hpay_32版
 */
class MallOrderFragment : ProjConstraintFragment() {

    private var _binding: FragmentMallOrderBinding? = null

    companion object {
        const val TAG = "MallOrderFragment"
        fun newInstance() = MallOrderFragment()
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMallOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {

            // switchToMall(ProjBaseFragment.FUNC_MAIN_TO_MALL, null);
//           requireActivity(). runOnUiThread(Runnable {
//                CustomDaialog.showNormal(
//                    requireActivity(),
//                    "",
//                    "Coming Soon",
//                    "確定",
//                    object : OnBtnClickListener {
//                        override fun onCheck() {}
//                        override fun onCancel() {
//                            CustomDaialog.closeDialog()
//                        }
//                    })
//            })
//            rvMall.adapter
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.account_listitem_order
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