package com.tubewiki.mine.view.message

import android.os.Bundle

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.TextHighLight
import com.blankj.utilcode.util.ActivityUtils

import com.jmbon.middleware.bean.event.QuestionAddEvent
import com.jmbon.middleware.utils.*
import com.jmbon.widget.picture.SelectPhotoUtil
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityModifyViolateVideoDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 修改视频违规详情页
 */
@Route(path = "/mine/message/modify_violate_video")
class ModifyViolateVideoDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityModifyViolateVideoDetailBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var videoUrl = ""

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var cover = ""


    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "video"


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_violate_rule))

        titleBarView.setBackgroundColor(resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarDarkMode(this, true)
        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this) + binding.llTitle.paddingTop
        binding.llTitle.setPadding(
            binding.llTitle.paddingStart,
            statusHeight,
            binding.llTitle.paddingEnd,
            binding.llTitle.paddingBottom
        )

        binding.ivVideo.loadRadius(cover, 8f.dp())
        binding.tvTitle.text = "您的视频涉嫌违规，已被官方删除。"

        binding.tvRule.text = TextHighLight.setStringHighLight("点击查看LesDoB社区管理规定", "LesDoB社区管理规定")


        binding.llVideo.setOnSingleClickListener({
            if (videoUrl.isNullOrEmpty()) {
                "播放地址为空".showToast()
                return@setOnSingleClickListener
            }
            SelectPhotoUtil.externalVideoPreview(ActivityUtils.getTopActivity(), videoUrl)
        })

        binding.tvRule.setOnSingleClickListener({
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.COMMUNITY_MANAGE_RULE)
                .withString("title", getString(R.string.social_circle_rule))
                .navigation()
        })

        binding.ivPhone.setOnClickListener {

            ARouter.getInstance().build("/mine/message/jm_email").navigation()

        }
        binding.llModify.setOnSingleClickListener({


        })

    }


    override fun initData() {
    }

    override fun getData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: QuestionAddEvent) {

    }

    private fun unEnabledModifyStatus() {
        binding.tvModify.text = "问题已修改，审核中"
        binding.tvModify.setTextColor(resources.getColor(R.color.color_BFBFBF))
        binding.ivModify.gone()
        binding.llModify.isEnabled = false
    }
}