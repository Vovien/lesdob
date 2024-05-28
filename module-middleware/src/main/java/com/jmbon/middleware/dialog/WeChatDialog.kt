package com.jmbon.middleware.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.PopupAdvBean
import com.jmbon.middleware.databinding.DialogWechatLayoutBinding
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadUrlRadius
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick

/**
 * 微信弹窗
 * @author MilkCoder
 * @date 2023/8/25
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class WeChatDialog(val mContext: Context, val bean: PopupAdvBean) :
    BaseCenterDialog<DialogWechatLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()
        binding.ivAdv.loadUrlRadius(bean.popupImg, 8.dp)
        binding.ivClose.onClick {
            dismiss()
        }
        binding.ivAdv.onClick {
            BannerHelper.onClick(
                CommonBanner(
                    item_type = bean.itemType,
                    identity = bean.identity
                )
            )
        }
    }

    fun showDialog() {
        XPopup.Builder(mContext)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(this)
            .show()
    }

    override fun getPopupWidth(): Int {
        return 280.dp
    }
}