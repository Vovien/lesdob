package com.tubewiki.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tubewiki.home.databinding.ViewContentAdLayoutBinding

/******************************************************************************
 * Description: 问答/经验广告
 *
 * Author: jhg
 *
 * Date: 2023/6/25
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ContentAdLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding by lazy {
        ViewContentAdLayoutBinding.inflate(LayoutInflater.from(context), this)
    }

    /**
     * 设置广告信息
     * @param adInfo: 广告信息
     */
//    fun setData(adInfo: TubeArticleDetail.RecommendAdv?,action: () -> Unit = {}) {
//        if (adInfo == null) {
//            isVisible = false
//            return
//        }
//
//        isVisible = true
//        binding.apply {
//            tvTitle.text = adInfo.title
//            ivImage.loadUrl(adInfo.cover, topLeftRadius = 12.dp, topRightRadius = 12.dp)
//            tvCaseSource.text = adInfo.desc
//            tvTip.text = adInfo.buttom_label
//            if (!adInfo.buttom_word.isNullOrBlank()) {
//                tvAdd.text = adInfo.buttom_word
//            }
//            onClick {
//                BannerHelper.onClick(
//                    CommonBanner(
//                        item_id = id,
//                        item_type = adInfo.item_type,
//                        identity = adInfo.identity
//                    )
//                )
//                action()
//            }
//        }
//    }
}