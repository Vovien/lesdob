package com.tubewiki.mine.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.GeneralShowDialogUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.MineDraftData
import com.tubewiki.mine.bean.convertQuestion
import com.tubewiki.mine.databinding.ItemMineDraftBinding

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 我的草稿adapter
 * version: 1.0
 */
class DraftAdapter :
    BindingQuickAdapter<MineDraftData.Data, ItemMineDraftBinding>() {


    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: MineDraftData.Data) {
        holder.getViewBinding<ItemMineDraftBinding>().apply {
            tvTitle.text = item.title

            if (TextUtils.isEmpty(item.answerContent)) {
                //todo 确定后台数据
                tvContent.text = if (item.images.isNotNullEmpty()) "[图片]" else "[视频]"
            } else {
                tvContent.text = item.answerContent.replace("&nbsp;", "")
            }
            tvDate.text = "最后编辑于${
                DateFormatUtil.getStringByFormat(
                    item.updateTime * 1000L,
                    DateFormatUtil.datePointFormatYMD
                )
            }"

            root.setOnClickListener {
                if (item.lock == 1 && !Constant.isSpecialMember()) {
                    GeneralShowDialogUtils.showLockCheckDialog(context)
                    return@setOnClickListener
                }
                ARouter.getInstance().build("/question/activity/answer")
                    .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                    .withTransition(R.anim.activity_bottom_in, R.anim.activity_background)
                    .withSerializable(TagConstant.QUESTION_DATA, item.convertQuestion())
                    .navigation(context)
            }
        }

    }
}