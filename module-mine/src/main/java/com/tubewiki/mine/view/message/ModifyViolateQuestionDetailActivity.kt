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
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.event.QuestionAddEvent
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AskDetailImageAdapter
import com.tubewiki.mine.databinding.ActivityModifyViolateQuestionDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.lxj.xpopup.XPopup
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 修改问题违规详情页
 */
@Route(path = "/mine/message/modify_violate_question")
class ModifyViolateQuestionDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityModifyViolateQuestionDetailBinding>() {

    @Autowired(name = TagConstant.QUESTION_ID)
    @JvmField
    var questionId = 0

    @Autowired(name = TagConstant.QUESTION_CHECK_ID)
    @JvmField
    var checkId: Int = 0

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "question_video"  //question_video 问题视频未通过， question_images问题图片未通过

    var question: Question? = null
    var limitPath = arrayListOf<String>()
    var limitWord = arrayListOf<String>()

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化 loading
        initPageState(
            binding.root.initState(
                retry = {
                    refreshDataWhenError()
                })
        )
        if (type == "question_video") {
            binding.llModify.visible()
            setTitleName(getString(R.string.message_check_result))
        } else {
            binding.llModify.gone()
            setTitleName(getString(R.string.message_violate_rule))
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

        binding.tvQaContent.gone()

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
//            viewModel.getOfficialPhone {
//                showCallDialog(it)
//            }
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
                    //.withSerializable("limitPath", limitPath)
                    //.withSerializable("limitWord", limitWord)
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
                showErrorState()
                message.showToast()
            }.next {
                showContentState()
                //被修改过了就禁止修改
                if (isUpdate != 0) {
                    unEnabledModifyStatus()
                    if (isUpdate == 2) {
                        binding.tvModify.text = "问题已修改,管理员已审核"
                    }
                }
                var itemQuestion = Question()
                itemQuestion.questionId = questionId
                itemQuestion.questionDetail = description
                itemQuestion.questionContent = title
                itemQuestion.topics = topics
                itemQuestion.images = images
                itemQuestion.video = video

                question = itemQuestion
                //question?.questionDetail = it.originData.text
                //违禁词高亮

//            var originStr = it.originData.text
//            it.blockData.text.forEach { text ->
//                limitWord.add(text.context)
//            }

                // binding.tvQaContent.text = originStr

                binding.tvTitle.text =
                    if (this@ModifyViolateQuestionDetailActivity.type == "question_video") {
                        "您在「${question?.questionContent}」问题中添加的视频涉嫌违规，未通过审核，不予展示。"

                    } else {
                        "您在「${question?.questionContent}」问题中添加的图片涉嫌违规，已被官方删除。"
                    }


                // (binding.rvImg.adapter as AskDetailImageAdapter).limitImage = limitPath

                val data = arrayListOf<LocalMedia>()

                //视频违规或者图片违规
                if (this@ModifyViolateQuestionDetailActivity.type == "question_video") {
                    if (video.isNullOrEmpty()) {
                        limitPath.addAll(videos)
                    } else {
                        limitPath.add(video)
                    }

                    if (video.isNotNullEmpty()) {
                        data.add(
                            LocalMedia(
                                video,
                                0,
                                0,
                                PictureMimeType.MIME_TYPE_VIDEO
                            )
                        )
                    } else {
                        data.addAll(videos!!.map { path ->
                            LocalMedia(
                                path,
                                0,
                                0,
                                PictureMimeType.MIME_TYPE_VIDEO
                            )
                        }.toList())
                    }
                } else {
                    images?.apply {
                        limitPath.addAll(this)

                    } ?: let { activity ->
                        images = arrayListOf<String>()
                    }

                    if (blockImages.isNotNullEmpty()) {
                        data.addAll(blockImages!!.map { path -> LocalMedia(path, 0, 0, "") }
                            .toList())
                    } else {
                        if (images.isNotNullEmpty()) {
                            data.addAll(images!!.map { path -> LocalMedia(path, 0, 0, "") }
                                .toList())
                        }
                    }
                }
                if (!data.isNullOrEmpty()) {
                    binding.rvImg.visibility = View.VISIBLE
                    (binding.rvImg.adapter as AskDetailImageAdapter).setNewInstance(data)
                } else {
                    binding.rvImg.visibility = View.GONE
                }
            }
        }


    }

    override fun getData() {
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: QuestionAddEvent) {
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