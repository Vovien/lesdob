package com.jmbon.middleware.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.jmbon.middleware.databinding.DialogCopyWechatLayoutBinding
import com.jmbon.middleware.utils.dp
import com.lxj.xpopup.XPopup
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext

/******************************************************************************
 * Description: 复制微信号弹框
 *
 * Author: jhg
 *
 * Date: 2023/6/14
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CopyWechatDialog(val mContext: Context): BaseCenterDialog<DialogCopyWechatLayoutBinding>(mContext) {

    fun showDialog() {
        XPopup.Builder(mContext)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(this)
            .show()
    }

    override fun getPopupWidth(): Int {
        return 220.dp
    }
}