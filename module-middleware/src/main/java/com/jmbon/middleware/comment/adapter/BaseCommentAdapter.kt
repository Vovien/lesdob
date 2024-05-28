package com.jmbon.middleware.comment.adapter

import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.R
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.databinding.ItemCommentsLayoutBinding
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup

/**
 * @author : leimg
 * time   : 2022/6/22
 * desc   :
 * version: 1.0
 */
abstract class BaseCommentAdapter<T, VB : ViewBinding> : BindingQuickAdapter<T, VB>() {

    fun showCallDialog(item: CommentList.Comment) {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                context.resources.getString(R.string.report_comment),
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                context.resources.getString(R.string.currency_cancle),
                CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(context)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(context, listData) { select ->
                    if (select == 0) {
                        ARouter.getInstance().build("/question/activity/report")
                            .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                            .withString(
                                TagConstant.PARAMS, "comment"
                            )
                            .withInt(
                                TagConstant.PARAMS2,
                                item.answerId
                            )
                            .navigation()
                    }
                }
            ).show()
    }
}