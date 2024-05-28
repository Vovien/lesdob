package com.tubewiki.mine.view.message

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.ContentTortBean
import com.tubewiki.mine.databinding.ActivityJmDelictReportLayoutBinding
import com.tubewiki.mine.databinding.ActivityJmHandleReportLayoutBinding
import com.tubewiki.mine.databinding.ActivityJmHandleReportResultLayoutBinding
import com.tubewiki.mine.view.model.FeedReportViewModel

import com.tubewiki.mine.view.model.MessageCenterViewModel


/**
 * 处理结果 通知界面
 */
@Route(path = "/mine/message/handle_report_result")
class JmHandleReportResultActivity :
    ViewModelActivity<FeedReportViewModel, ActivityJmHandleReportResultLayoutBinding>() {
    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var id = 0


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.hanlde_report_result))

        binding.tvSure.setOnClickListener {
            finish()
        }
    }


    override fun initData() {

    }

    override fun getData() {

        started {
            viewModel.getContentTortFeedback(id).netCatch {

            }.next {

                binding.tvContent.text =
                    if (feedback.feedback.isNullOrEmpty()) "暂无留言" else feedback.feedback
                binding.tvResult.text = feedback.title
            }
        }
    }

}