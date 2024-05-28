package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.bean.TubeStepBeans
import com.tubewiki.home.databinding.ItemQuestionCategoryDetailLayoutBinding
import com.tubewiki.home.databinding.ItemTubeStepCategoryDetailLayoutBinding

/**
 * 试管流程详情item adapter
 */
class TubeStepCategoryDetailAdapter :
    BindingQuickAdapter<TubeStepBeans.Step, ItemTubeStepCategoryDetailLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: TubeStepBeans.Step) {
        holder.getViewBinding<ItemTubeStepCategoryDetailLayoutBinding>().apply {

            ivCover.loadCircle(item.icon)
            tvTitle.text = item.title

        }
    }
}