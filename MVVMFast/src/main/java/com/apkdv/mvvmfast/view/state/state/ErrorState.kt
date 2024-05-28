package com.apkdv.mvvmfast.view.state.state

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.view.state.MultiStatePage


class ErrorState : BaseState() {

    private lateinit var tvErrorMsg: TextView
    private lateinit var imgError: ImageView

    private lateinit var rootView: View
    private lateinit var tvRetry: View

    override fun getLayoutId(): Int = R.layout.default_viewstatus_loading_fail

    override fun onMultiStateViewCreate(
        view: View,
        retry: () -> Unit,
        offset: Int,
        orientation: Int
    ) {
        super.onMultiStateViewCreate(view, retry, offset, orientation)
        rootView = view
        tvErrorMsg = view.findViewById(R.id.textErrorSet)
        imgError = view.findViewById(R.id.imageError)
        tvRetry = view.findViewById(R.id.sbReTry)

        setErrorMsg(MultiStatePage.config.errorMsg)
        setErrorIcon(MultiStatePage.config.errorIcon)
        view.findViewById<View>(R.id.sbReTry).setOnSingleClickListener({ retry.invoke() })
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

    fun setErrorMsg(errorMsg: String?) {
        errorMsg?.let {
            tvErrorMsg.text = errorMsg
        }
    }

    fun setErrorIcon(@DrawableRes errorIcon: Int) {
        imgError.setImageResource(errorIcon)
    }

    fun setErrorBgColor(bgColor: Int) {
        rootView.setBackgroundColor(bgColor)
        tvRetry.setBackgroundColor(bgColor)
    }

}