package com.tubewiki.hospital.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.hospital.R
import com.tubewiki.hospital.bean.HospitalBean
import com.tubewiki.hospital.databinding.ItemRecommendHospitalLayoutBinding

/**
 * 首页推荐知识adapter
 */
class RecommendHospitalAdapter :
    BindingQuickAdapter<HospitalBean, ItemRecommendHospitalLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: HospitalBean) {
        holder.getViewBinding<ItemRecommendHospitalLayoutBinding>().apply {

            GlideUtil.getInstance().loadUrlCornerRadius(
                ivCover,
                item.logo,
                8f.dp().toFloat(),
                0f,
                8f.dp().toFloat(),
                0f,
                R.drawable.icon_tube_placeholder
            )

            tvTitle.text = item.hospitalName
            tvLocation.text = "${item.province}${item.city}${item.address}"

        }
    }
}