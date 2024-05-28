package com.tubewiki.home.dialog

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.allen.library.shape.ShapeConstraintLayout
import com.apkdv.mvvmfast.base.dialog.BaseViewModelCenterDialog
import com.apkdv.mvvmfast.utils.GsonUtil
import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.extention.getDivider
import com.jmbon.middleware.extention.saveToAlbum
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.ThirdAppUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.load
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick
import com.sxu.shadowdrawable.ShadowDrawable
import com.tubewiki.home.R
import com.tubewiki.home.bean.SchemeConfigBean
import com.tubewiki.home.databinding.DialogGetSchemeLayoutBinding
import com.tubewiki.home.viewmodel.GetSchemeViewModel

/******************************************************************************
 * Description: 获取案例弹框
 *
 * Author: jhg
 *
 * Date: 2023/6/2
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class GetSchemeDialog(val mContext: Context, val type: Int) :
    BaseViewModelCenterDialog<GetSchemeViewModel, DialogGetSchemeLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()
        // 直接使用缓存信息
        val key = "${MMKVKey.SCHEME_CONFIG}${type}"
        val schemeConfig =
            GsonUtil.gson().fromJson(key.getString() ?: "", SchemeConfigBean::class.java)
        if (schemeConfig != null) {
            initLayout(schemeConfig)
        }
        // 请求并监听配置信息
        viewModel.schemeConfigLD.observe(this) {
            if (it != null) {
                if (schemeConfig == null) {
                    initLayout(it)
                }
                // 数据请求成功后更新缓存信息
                GsonUtil.gson().toJson(it)?.saveToMMKV(key)
            } else {
                if (schemeConfig == null) {
                    dismiss()
                }
            }
        }
        viewModel.getSchemeConfig(type)
    }

    private fun initLayout(configInfo: SchemeConfigBean) {
        binding.apply {
            val marginLayoutParams  = ivTitle.layoutParams as  MarginLayoutParams
            marginLayoutParams.width = if (type == 2) 150.dp else 100.dp
            marginLayoutParams.height = 24.dp
            marginLayoutParams.setMargins(24.dp,24.dp,0,0)
            ivTitle.layoutParams = marginLayoutParams
            ivTitle.load(configInfo.title_img)
            ivBg.load(configInfo.bg_img)
            llDesc.removeAllViews()
            llDesc.dividerDrawable = getDivider(Color.TRANSPARENT, 12.dp, 12.dp)
            configInfo.content_list?.forEach {
                TextView(context).apply {
                    text = "$it"
                    textSize = 14f
                    setTextColor(R.color.color_262626.toColorInt())
                    llDesc.addView(this)
                }
            }
            ShadowDrawable.setShadowDrawable(
                llQrCode,
                Color.TRANSPARENT,
                12.dp,
                R.color.black05.toColorInt(),
                4.dp,
                0,
                1.dp
            )
            ivQrCode.load(configInfo.qrcode)
            tvWechatNumber.text = "微信号：${configInfo.wechat}"
            if (!configInfo.button_word.isNullOrBlank()) {
                tvOpenWechat.text = configInfo.button_word
            }

            ivClose.onClick {
                dismiss()
            }
            tvOpenWechat.onClick {
                val result = ThirdAppUtils.openWechatWithData(mContext, configInfo.wechat)
                if (result) {
                    dismiss()
                }
                configInfo.qrcode.saveToAlbum(mContext)
            }
        }
    }

    override fun getMaxWidth(): Int {
        return ScreenUtils.getAppScreenWidth() - 40.dp
    }

    fun showDialog() {
        XPopup.Builder(mContext)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(this)
            .show()
    }
}