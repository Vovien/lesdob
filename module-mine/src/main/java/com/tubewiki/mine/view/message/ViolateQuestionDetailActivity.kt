package com.tubewiki.mine.view.message

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig.COMMUNITY_MANAGE_RULE
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.TextHighLight
import com.apkdv.mvvmfast.view.state.initState
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AskDetailImageAdapter
import com.tubewiki.mine.bean.OfficialMessageData
import com.tubewiki.mine.databinding.ActivityViolateQuestionDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia


/**
 * 问题违规详情页
 */
@Route(path = "/mine/message/violate_question")
class ViolateQuestionDetailActivity :
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

    var question: Question? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化 loading
        initPageState(
            binding.root.initState(
                retry = {
                    refreshDataWhenError()
                })
        )

        if (type == "question") {
            setTitleName(getString(R.string.message_violate_rule))
        } else if (type == OfficialMessageData.typeReportQuestionFail) {
            setTitleName(getString(R.string.message_check_result))
            binding.llModify.visible()
        } else {
            setTitleName(getString(R.string.message_audit_result))
        }
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
                .withString("url", COMMUNITY_MANAGE_RULE)
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
        getData()
    }

    override fun initData() {

    }

    override fun getData() {
        started {

            viewModel.getViolationQADetail(questionId, type).netCatch {
                showErrorState()
                message.showToast()
            }.next {
                if (type == "examine_question") {
                    //修改问题返回数据
                    this@ViolateQuestionDetailActivity.question = this.examine
                } else {
                    this@ViolateQuestionDetailActivity.question = this.question
                }
                this@ViolateQuestionDetailActivity.question?.let {
                    binding.tvQaContent.text = it.questionDetail
                    binding.tvQaTitle.text = it.questionContent

                    if (it.isReward > 0) {
                        binding.tvDesc.visibility = View.VISIBLE
                        binding.tvTitle.text = "您的提问涉嫌违规，已被官方删除。"

                        //未采纳才退回相关悬赏金额
                        if (!it.hasAdopt && it.rewardAnswerId <= 0) {
                            binding.tvDesc.text = "悬赏金¥${it.reward_money}已退还至您的微信/支付宝"
                        } else {
                            binding.tvDesc.visibility = View.GONE
                        }
                    } else {
                        binding.tvTitle.text = "您的提问涉嫌违规，已被官方删除。"
                        binding.tvDesc.visibility = View.GONE
                    }

                    if (type.equals("examine_question")) {
                        //问题的的审核不通过
                        binding.tvTitle.text = "您的问题修改审核未通过。"
                    } else if (type.equals(OfficialMessageData.typeQuestionVideoExamineFail)) {
                        //问题的的审核不通过
                        binding.tvTitle.text = "您上报的提问内容，未通过审核。"
                    }

                    val data = arrayListOf<LocalMedia>()
                    if (it.video.isNotNullEmpty())
                        data.add(LocalMedia(it.video, 0, 0, PictureMimeType.MIME_TYPE_VIDEO))
                    if (it.images.isNotNullEmpty())
                        data.addAll(it.images!!.map { path -> LocalMedia(path, 0, 0, "") }.toList())
                    if (!data.isNullOrEmpty()) {
                        binding.rvImg.visibility = View.VISIBLE
                        (binding.rvImg.adapter as AskDetailImageAdapter).setNewInstance(data)
                    } else {
                        binding.rvImg.visibility = View.GONE
                    }
                }

                showContentState()
            }
        }
    }


}