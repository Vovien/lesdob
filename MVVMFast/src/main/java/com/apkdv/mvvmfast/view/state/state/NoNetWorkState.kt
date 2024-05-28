package com.apkdv.mvvmfast.view.state.state

import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.TextView
import com.apkdv.mvvmfast.R
import com.blankj.utilcode.util.Utils


class NoNetWorkState : BaseState() {

    override fun getLayoutId(): Int = R.layout.default_viewstatus_no_netwrok

    override fun onMultiStateViewCreate(
        view: View,
        retry: () -> Unit,
        offset: Int,
        orientation: Int
    ) {

        val tvRetry = view.findViewById<View>(R.id.sbReTry)
        val setNet = view.findViewById<TextView>(R.id.toNetWorkSet)
        setNet.setOnSingleClickListener({
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            Utils.getApp().startActivity(intent)
        })
        tvRetry.setOnSingleClickListener({ retry.invoke() })
    }

    fun View.setOnSingleClickListener(onClick: () -> Unit, delayMillis: Long = 500) {
        this.setOnClickListener {
            this.isClickable = false
            onClick.invoke()
            this.postDelayed({
                this.isClickable = true
            }, delayMillis)
        }
    }

}