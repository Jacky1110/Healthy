package com.jotangi.healthy.HpayMall

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils

class Progressbar {
    private var mContext: Context? = null
    private var mTitle: String? = null
    private var mMessage: String? = null
    private var progressDialog: ProgressDialog? = null


    fun MyProgressDialog(context: Context, title: String?, message: String?) {
        mContext = context
        mTitle = title
        mMessage = message
    }

    fun show(enableProgressBar: Boolean) {
        progressDialog = ProgressDialog(mContext)
        if (!TextUtils.isEmpty(mTitle)) {
            progressDialog!!.setTitle(mTitle)
        }
        if (!TextUtils.isEmpty(mMessage)) {
            progressDialog!!.setMessage(mMessage)
        }
        if (enableProgressBar) {
            progressDialog!!.progress = 0
            progressDialog!!.max = 100
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.isIndeterminate = false
        }
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun dismiss() {
        progressDialog!!.dismiss()
    }

    fun isShowing(): Boolean {
        return progressDialog!!.isShowing
    }

    fun setProgress(progress: Int) {
        progressDialog!!.progress = progress
    }
}