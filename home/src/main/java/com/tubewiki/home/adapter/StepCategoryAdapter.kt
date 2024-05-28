package com.tubewiki.home.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.setTextStyle
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.bean.CommonQuestionBean
import com.tubewiki.home.bean.TubeStepBeans
import com.tubewiki.home.databinding.ItemAllColumnLayoutBinding
import com.tubewiki.home.databinding.ItemHomePregentHelpLayoutBinding
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding
import com.tubewiki.home.databinding.ItemQuestionCategoryLayoutBinding

/**
 * 常见问题分类adapter
 */
class StepCategoryAdapter :
    BindingQuickAdapter<TubeStepBeans.Step, ItemQuestionCategoryLayoutBinding>() {

    var selectedPos = 0


    override fun convert(holder: BaseBindingHolder, item: TubeStepBeans.Step) {
        holder.getViewBinding<ItemQuestionCategoryLayoutBinding>().apply {
            if (selectedPos == holder.adapterPosition) {
                ivTag.visibility = View.VISIBLE
                root.setBackgroundColor(context.resources.getColor(R.color.white))
                tvTitle.setTextColor(context.resources.getColor(R.color.color_262626))
                tvTitle.setTextStyle(Typeface.BOLD)
            } else {
                ivTag.visibility = View.INVISIBLE
                root.setBackgroundColor(Color.parseColor("#FFF5F5F5"))
                tvTitle.setTextColor(Color.parseColor("#FF7F7F7F"))
                tvTitle.setTextStyle(Typeface.NORMAL)
            }

            tvTitle.text = item.title

        }
    }
}