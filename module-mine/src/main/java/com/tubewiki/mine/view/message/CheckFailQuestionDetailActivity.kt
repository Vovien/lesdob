package com.tubewiki.mine.view.message

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.TextHighLight
import com.apkdv.mvvmfast.view.state.initState
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.event.QuestionAddEvent
import com.jmbon.middleware.bean.event.QuestionUpdateEvent
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AskDetailImageAdapter
import com.tubewiki.mine.databinding.ActivityViolateQuestionDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 审核不通过详情页
 */
@Route(path = "/mine/message/check_fail_question")
class CheckFailQuestionDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityViolateQuestionDetailBinding>() {

    @Autowired(name = TagConstant.QUESTION_ID)
    @JvmField
    var questionId = 0

    @Autowired(name = TagConstant.QUESTION_CHECK_ID)
    @JvmField
    var checkId = 0

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "question"


    var isFirst = true

    var question: Question? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_check_result))

        // 初始化 loading
        initPageState(
            binding.root.initState(
                retry = {
                    refreshDataWhenError()
                })
        )

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

        binding.tvRule.text = TextHighLight.setStringHighLight("点击查看LesDoB社区管理规定", "LesDoB社区管理规定")

        binding.rvImg.setHasFixedSize(true)
        binding.rvImg.layoutManager = GridLayoutManager(this, 3)
        binding.rvImg.adapter = AskDetailImageAdapter()
        binding.rvImg.addItemDecoration(SpaceItemDecoration(0, 4))
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
            question?.let {
                ARouter.getInstance().build("/question/activity/ask")
                    .withTransition(
                        R.anim.activity_bottom_in,
                        R.anim.activity_background
                    )
                    .withParcelable(TagConstant.QUESTION_DATA, it)
                    .withInt(TagConstant.QUESTION_CHECK_ID, checkId)
                    .navigation(this)
            }
        })

    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()
    }


    override fun initData() {

        started {

            viewModel.getExamineDetail(checkId).netCatch {
                message.showToast()

                showErrorState()
            }.next {
                showContentState()
                let {
                    question = Question(
                        questionId = it.questionId,
                        questionContent = it.title,
                        questionDetail = it.description,
                        images = it.images,
                        video = it.video,
                    )
                    question?.let {
                        SpanUtils.with(binding.tvTitle).append("您上报的提问").append("文本内容")
                            .setUnderline()
                            .append("，未通过审核。").create()
                        binding.tvDesc.visibility = View.GONE
                        binding.llModify.visible()
                        binding.tvQaContent.text = it.questionDetail
                        binding.tvQaTitle.text = it.questionContent

                        val data = arrayListOf<LocalMedia>()
                        if (it.video.isNotNullEmpty())
                            data.add(LocalMedia(it.video, 0, 0, PictureMimeType.MIME_TYPE_VIDEO))
                        if (it.images.isNotNullEmpty())
                            data.addAll(it.images!!.map { path -> LocalMedia(path, 0, 0, "") }
                                .toList())
                        if (!data.isNullOrEmpty()) {
                            binding.rvImg.visibility = View.VISIBLE
                            (binding.rvImg.adapter as AskDetailImageAdapter).setNewInstance(data)
                        } else {
                            binding.rvImg.visibility = View.GONE
                        }
                    }

                    //被修改过了就禁止修改
                    if (it.isUpdate != 0) {
                        unEnabledModifyStatus()
                        if (it.isUpdate == 2) {
                            binding.tvModify.text = "问题已修改,管理员已审核"
                        }
                    }
                }
            }


        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            started {
                viewModel.getExamineDetail(checkId).netCatch {
                }.next {
                    //被修改过了就禁止修改
                    if (isUpdate != 0) {
                        unEnabledModifyStatus()
                        if (isUpdate == 2) {
                            binding.tvModify.text = "问题已修改,管理员已审核"
                        }
                    }
                }
            }
        }
        isFirst = false
    }

    override fun getData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: QuestionAddEvent) {
        if (event.questionId == question?.questionId || event.checkLogId == checkId) {
            unEnabledModifyStatus()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: QuestionUpdateEvent) {
        if (event.questionId == question?.questionId) {
            unEnabledModifyStatus()
        }
    }

    private fun unEnabledModifyStatus() {
        binding.tvModify.text = "问题已修改，审核中"
        binding.tvModify.setTextColor(resources.getColor(R.color.color_BFBFBF))
        binding.ivModify.gone()
        binding.llModify.isEnabled = false
    }
}