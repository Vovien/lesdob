package com.tubewiki.mine.adapter.delegate

import android.annotation.SuppressLint
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.ViewBindingDelegate
import com.jmbon.middleware.utils.ViewBindingViewHolder
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.WithdrawalList
import com.tubewiki.mine.databinding.ItemWithdrawalListBinding
import com.tubewiki.mine.utils.StringFormat

class WithdrawalViewDelegate :
    ViewBindingDelegate<WithdrawalList.Withdrawal, ItemWithdrawalListBinding>() {
    // cateType  1：文章，2：问题，3：回答，4：采纳, 5:咨询
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewBindingViewHolder<ItemWithdrawalListBinding>,
        item: WithdrawalList.Withdrawal
    ) {
        holder.binding.apply {
            //imageAvatar.loadCircle(Constant.userInfo.avatarFile)

            if (item.type == 1) {
                //支付宝
                imageAvatar.setImageResource(R.drawable.icon_alipay)
            } else {
                //2是微信
                imageAvatar.setImageResource(R.drawable.icon_wechat)
            }


            // status 1 成功 0 审核
            textName.text =
                if (item.status == 1) "提现成功" else if (item.status == -1) "提现申请被拒绝" else "提现审核中"
            textName.setTextColor(
                if (item.status == 1) ColorUtils.getColor(R.color.color_currency) else
                    ColorUtils.getColor(R.color.color_7F7F7F)
            )
            textTime.text = TimeUtils.millis2String(item.createTime * 1000L)

            if (item.status == -1) {
                textMoney.text = StringFormat.dataFormat("${item.amount}", true)
            } else {
                textMoney.text = "-${StringFormat.dataFormat("${item.amount}", true)}"
            }

            root.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/wallet/withdrawal/details")
                    .withInt(TagConstant.PARAMS, item.id)
                    .navigation()
            })
        }
    }

}