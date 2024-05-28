package com.tubewiki.mine.adapter

import android.annotation.SuppressLint
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.setUserNickColor
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.IncomeList
import com.tubewiki.mine.databinding.ItemInncomeListBinding

class IncomeListAdapter : BindingQuickAdapter<IncomeList.Income, ItemInncomeListBinding>() {

    // cateType  1：文章，2：问题，3：回答，4：采纳, 5:咨询
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: IncomeList.Income) {
        holder.getViewBinding<ItemInncomeListBinding>().apply {
            imageAvatar.loadCircle(item.user.avatarFile)
            textName.text = item.user.userName
            textName.setUserNickColor(R.color.color_262626,item.user.isCancel)
            textMoney.text = "+${item.money}"
            textTime.text = TimeUtils.millis2String(item.addTime * 1000L)
            val span = SpanUtils.with(textInfo)
            var separator = " "
            var endSeparator = " "
            when (item.cateType) {
                1 -> {
                    span.append("打赏了您的文章")
                    separator = "《"
                    endSeparator = "》"
                }
                3 -> {
                    span.append("打赏了您的回答")
                    separator = "“"
                    endSeparator = "”"
                }
                4 -> {
                    span.append("您的答案被采纳")
                    separator = "“"
                    endSeparator = "”"
                }
                5 -> {
                    span.append("咨询订单已完成 ${item.title}")
                }
            }
            span.setForegroundColor(ColorUtils.getColor(R.color.color_262626))
            if (item.cateType != 5) {
                span.append(separator).append(item.title).append(endSeparator)
                    .setForegroundColor(ColorUtils.getColor(R.color.color_7F7F7F))
            }
            span.create()
            root.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/wallet/income/details")
                    .withInt(TagConstant.PARAMS, item.id)
                    .navigation()
            })
        }
    }
}