package com.jmbon.widget

import android.content.Context
import android.widget.TextView
import com.apkdv.mvvmfast.ktx.gone
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.SimpleCallback


object GeneralDialog {

    fun showDialog(
        context: Context,
        title: String?,
        content: String?,
        cancelBtnText: String = "取消",
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
        cancelListener: OnCancelListener? = null,
        isHideCancel: Boolean = false
    ) {
        XPopup.Builder(context)
            .setPopupCallback(object : SimpleCallback() {
                override fun onCreated(popupView: BasePopupView?) {
                    super.onCreated(popupView)
                    popupView?.popupImplView?.apply {
                        if (content.isNullOrEmpty()) {
                            findViewById<TextView>(R.id.tv_content).gone()
                        }
                    }
                }
            })
            .asConfirm(
                title,
                content,
                cancelBtnText,
                confirmBtnText,
                confirmListener,
                cancelListener,
                isHideCancel,
                R.layout.dialog_general_layout
            ).show()
    }

    fun showWarnDialog(
        context: Context,
        title: String?,
        content: String?,
        cancelBtnText: String = "取消",
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
        cancelListener: OnCancelListener? = null,
        isHideCancel: Boolean = false
    ) {
        XPopup.Builder(context)
            .setPopupCallback(object : SimpleCallback() {
                override fun onCreated(popupView: BasePopupView?) {
                    super.onCreated(popupView)
                    popupView?.popupImplView?.apply {
                        if (content.isNullOrEmpty()) {
                            findViewById<TextView>(R.id.tv_content).gone()
                        }
                    }
                }
            })
            .asConfirm(
                title,
                content,
                cancelBtnText,
                confirmBtnText,
                confirmListener,
                cancelListener,
                isHideCancel,
                R.layout.dialog_general_warn_layout
            ).show()
    }


    fun showNormalDialog(
        context: Context,
        title: String?,
        content: String,
        cancelBtnText: String = "取消",
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
        cancelListener: OnCancelListener? = null,
        isHideCancel: Boolean = false
    ) {
        XPopup.Builder(context)
            .asConfirm(
                title,
                content,
                cancelBtnText,
                confirmBtnText,
                confirmListener,
                cancelListener,
                isHideCancel,
                R.layout.dialog_general_layout
            ).show()
    }

    fun showNormalDialog2(
        context: Context,
        title: String?,
        content: String,
        cancelBtnText: String = "取消",
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
        cancelListener: OnCancelListener? = null,
        isHideCancel: Boolean = false
    ) {
        XPopup.Builder(context)
            .setPopupCallback(object : SimpleCallback() {
                override fun onCreated(popupView: BasePopupView?) {
                    super.onCreated(popupView)
                    popupView?.popupImplView?.apply {
                        findViewById<TextView>(R.id.tv_cancel).setTextColor(
                            context.resources.getColor(
                                R.color.color_currency
                            )
                        )
                    }
                }
            })
            .asConfirm(
                title,
                content,
                cancelBtnText,
                confirmBtnText,
                confirmListener,
                cancelListener,
                isHideCancel,
                R.layout.dialog_general_layout
            ).show()
    }

}