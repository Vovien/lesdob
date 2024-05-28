package com.jmbon.widget.dialog

import android.app.Activity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.jmbon.widget.databinding.DialogTopicExitLayoutBinding
import com.lxj.xpopup.XPopup


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :退出提示dialog
 * version: 1.0
 */
class ExitDialog(
    var mContext: Activity,
    var title: String,
    var desc: String = "",
    var cancel: String = "",
    var sure: String = "",
    var exit: () -> Unit = {},
    var cancelAction: () -> Unit = {},
) :
    BaseCenterDialog<DialogTopicExitLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()
        binding.tvTitle.text = title
        binding.tvDesc.text = desc
        binding.tvDesc.isVisible = !desc.isNullOrBlank()
        binding.tvExit.text = sure
        binding.tvWait.text = cancel
        binding.tvWait.isVisible = !cancel.isNullOrBlank()
        binding.viewLine2.isInvisible = cancel.isNullOrBlank()
        binding.tvExit.setOnClickListener {
            dismiss()
            exit()
        }
        binding.tvWait.setOnClickListener {
            dismiss()
            cancelAction()
        }
    }

    fun showDialog() {
        XPopup.Builder(mContext)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(this)
            .show()
    }
}