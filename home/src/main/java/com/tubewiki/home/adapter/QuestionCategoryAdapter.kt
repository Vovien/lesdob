package com.tubewiki.home.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.setTextStyle
import com.tubewiki.home.R
import com.tubewiki.home.bean.CommonQuestionBean
import com.tubewiki.home.databinding.ItemQuestionCategoryLayoutBinding

/**
 * 常见问题分类adapter
 */
class QuestionCategoryAdapter :
    BindingQuickAdapter<CommonQuestionBean.Data, ItemQuestionCategoryLayoutBinding>() {

    var selectedPos = 0


    override fun convert(holder: BaseBindingHolder, item: CommonQuestionBean.Data) {
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

            tvTitle.text = item.problemName

        }
    }
}