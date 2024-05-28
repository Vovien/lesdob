package com.jmbon.widget.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.widget.databinding.DialogUpdateBinding
import com.jmbon.widget.download_progress.AnimDownloadProgressButton
import com.jmbon.widget.download_progress.DownloadProgressEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UpdateDialog(
    context: Context,
    val isForceUpdate: Int,
    val versionName: String,
    val update: () -> Unit
) :
    BaseCenterDialog<DialogUpdateBinding>(context) {

    /**
     * Apk下载后的路径
     */
    private var apkPath = ""

    var isStartUpdate = false
    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
//        binding.textUpdateInfo.text = version.description
        // need?
        binding.jbOk.setCurrentText("立即更新")
        if (isForceUpdate == 1)
            binding.textCancel.gone()
        binding.textCancel.setOnClickListener {
            // z 跳过
            true.saveToMMKV("TagConstant.APP_VERSION${versionName}")
            dismiss()
        }

        binding.jbOk.setOnClickListener {
            if (!isStartUpdate) {
                update.invoke()
                isStartUpdate = true
                binding.jbOk.state = AnimDownloadProgressButton.DOWNLOADING
                binding.jbOk.setProgressText("下载中", 0f)
            } else if (apkPath.isNotBlank()) {
                AppUtils.installApp(apkPath)
            }
//            dismiss()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setProgress(process: DownloadProgressEvent) {
        if (process.progress >= 100) {
            apkPath = process.apkPath
            binding.jbOk.setCurrentText("安装")
        } else {
            binding.jbOk.setProgressText("下载中", process.progress.toFloat())
        }
    }

    override fun getPopupWidth(): Int {
        return SizeUtils.dp2px(220f)
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}