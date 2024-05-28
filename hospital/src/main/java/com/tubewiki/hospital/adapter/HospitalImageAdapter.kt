package com.tubewiki.hospital.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.hospital.R
import com.tubewiki.hospital.bean.HospitalBean
import com.tubewiki.hospital.databinding.ItemHospitalImageLayoutBinding
import com.tubewiki.hospital.databinding.ItemRecommendHospitalLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalImageAdapter :
    BindingQuickAdapter<String, ItemHospitalImageLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: String) {
        holder.getViewBinding<ItemHospitalImageLayoutBinding>().apply {

            ivCover.loadRadius(item, 8f.dp())

        }
    }
}