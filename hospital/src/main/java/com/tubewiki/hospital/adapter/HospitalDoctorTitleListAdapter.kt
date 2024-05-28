package com.tubewiki.hospital.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.WebScrollOffset
import com.jmbon.middleware.utils.*
import com.tubewiki.hospital.R
import com.tubewiki.hospital.bean.HospitalBean
import com.tubewiki.hospital.databinding.ItemHospitalDoctorTitleListLayoutBinding
import com.tubewiki.hospital.databinding.ItemHospitalImageLayoutBinding
import com.tubewiki.hospital.databinding.ItemRecommendHospitalLayoutBinding

/**
 * 医生医院目录导航adapter
 */
class HospitalDoctorTitleListAdapter :
    BindingQuickAdapter<WebScrollOffset, ItemHospitalDoctorTitleListLayoutBinding>() {

    var selectedPos = -1

    override fun convert(holder: BaseBindingHolder, item: WebScrollOffset) {
        holder.getViewBinding<ItemHospitalDoctorTitleListLayoutBinding>().apply {
            tvTitle.text = item.title


            if (holder.adapterPosition == selectedPos) {
                viewDot.visible()
                tvTitle.setTextColor(context.resources.getColor(R.color.color_currency))
            } else {
                viewDot.gone()
                tvTitle.setTextColor(context.resources.getColor(R.color.color_7F7F7F))

            }

        }
    }
}