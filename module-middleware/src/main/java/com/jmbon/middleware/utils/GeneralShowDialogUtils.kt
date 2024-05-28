package com.jmbon.middleware.utils

import android.content.Context
import android.util.Log
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.AppUtils
import com.jmbon.middleware.dialog.TipsDialog
import com.lxj.xpopup.XPopup
import java.util.*

/**
 * @author : leimg
 * time   : 2021/12/1
 * desc   :
 * version: 1.0
 */
object GeneralShowDialogUtils {

    fun showLockCheckDialog(context: Context, isQa: Boolean = true) {
        var title =
            context.resources.getString(if (isQa) com.jmbon.middleware.R.string.qa_lock_title else com.jmbon.middleware.R.string.article_lock_title)
        var content =
            context.resources.getString(if (isQa) com.jmbon.middleware.R.string.qa_lock_content else com.jmbon.middleware.R.string.article_lock_content)

        XPopup.Builder(context)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                TipsDialog(context, title, content, false) {

                }
            ).show()
    }

    fun showQaCheckDialog(context: Context) {
        var title = "审核中"
        var content = "LesDoB社区规定需要过审的内容在审核期间无法对其进行交互操作（如分享、点赞、评论、修改、收藏等）"

        XPopup.Builder(context)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                TipsDialog(context, title, content, false) {

                }
            ).show()
    }
}