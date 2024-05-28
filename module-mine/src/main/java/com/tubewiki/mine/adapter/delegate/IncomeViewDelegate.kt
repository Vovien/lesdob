package com.tubewiki.mine.adapter.delegate

import android.annotation.SuppressLint
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.IncomeList
import com.tubewiki.mine.databinding.ItemInncomeListBinding
import com.tubewiki.mine.utils.StringFormat

class IncomeViewDelegate : ViewBindingDelegate<IncomeList.Income, ItemInncomeListBinding>() {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewBindingViewHolder<ItemInncomeListBinding>, item: IncomeList.Income) {
        holder.binding.apply {
            imageAvatar.loadCircle(item.user.avatarFile)
            textName.text = item.user.userName


            textMoney.text = "+${StringFormat.dataFormat("${item.money}", true)}"
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
                6 -> {
                    imageAvatar.loadCircle(R.drawable.icon_app_message)
                    textName.text = "官方消息"
                    textMoney.text = StringFormat.dataFormat("${item.money}", true)
                    textMoney.setTextColor(ColorUtils.getColor(R.color.color_262626))
                    span.append("您的悬赏问题被删除，金额已退回")
                }
            }

            textMoney.setTextColor(
                if (item.cateType == 6) ColorUtils.getColor(R.color.color_262626) else
                    ColorUtils.getColor(R.color.colorFF9823)
            )

            span.setForegroundColor(ColorUtils.getColor(R.color.color_262626))
            if (item.cateType in 1..5) {
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