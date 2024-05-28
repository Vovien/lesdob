package com.tubewiki.mine.view.message

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.ViewPagerAdapter
import com.tubewiki.mine.databinding.ActivityJmEmailLayoutBinding

import com.tubewiki.mine.databinding.ActivityJmHomeLayoutBinding
import com.tubewiki.mine.view.model.FeedReportViewModel
import com.tubewiki.mine.view.model.MessageCenterViewModel


/**
 * LesDoB官方邮件
 */
@Route(path = "/mine/message/jm_email")
class JmOfficialEmailActivity :
    ViewModelActivity<FeedReportViewModel, ActivityJmEmailLayoutBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_jmemail))


        binding.tvCopy.setOnClickListener {
            if (binding.tvEmail.text.toString().isNullOrEmpty()) {
                "邮箱地址获取失败"
                return@setOnClickListener
            }
            copy(binding.tvEmail.text.toString())
            "复制成功".showToast()
        }
    }


    override fun initData() {

    }

    override fun getData() {
        started {
            viewModel.getOfficialEmail().netCatch {
                binding.tvEmail.text = "postmaster@jmbon.com"
            }.next {
                binding.tvEmail.text = email
            }
        }
    }

    private fun copy(message: String) {
        //获取剪贴板管理器：
        val cm: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", message)
        // 将ClipData内容放到系统剪贴板里。
        cm?.setPrimaryClip(mClipData)
    }

}