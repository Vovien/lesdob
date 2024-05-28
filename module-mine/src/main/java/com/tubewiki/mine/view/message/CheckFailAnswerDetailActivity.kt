package com.tubewiki.mine.view.message

import android.os.Bundle
import android.view.View
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
import com.jmbon.middleware.bean.AnswerBean
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.event.AnswerAddEvent
import com.jmbon.middleware.bean.event.AnswerUpdateEvent
import com.jmbon.middleware.utils.AssetsFileUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityViolateAnswerDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 回答和评论违规详情页
 */
@Route(path = "/mine/message/chek_fail_answer")
class CheckFailAnswerDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityViolateAnswerDetailBinding>() {
    @Autowired(name = TagConstant.QUESTION_ID)
    @JvmField
    var questionId = 0

    @Autowired(name = TagConstant.ANSWER_ID)
    @JvmField
    var answerId = 0

    @Autowired(name = TagConstant.QUESTION_CHECK_ID)
    @JvmField
    var checkId = 0

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "question"

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var title = ""


    var answer = AnswerBean()
    var question = Question()

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
        setTitleName(getString(R.string.message_check_result))
        titleBarView.setBackgroundColor(resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarDarkMode(this, true)
        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this) + binding.tvTitle.paddingTop
        binding.tvTitle.setPadding(
            binding.tvTitle.paddingStart,
            statusHeight,
            binding.tvTitle.paddingEnd,
            binding.tvTitle.paddingBottom
        )


        SpanUtils.with(binding.tvTitle).append("您上报的回答").append("文本内容").setUnderline()
            .append("，未通过审核。").create()

        binding.tvComment.visibility = View.GONE
        binding.llModify.visibility = View.VISIBLE
        binding.webView.visibility = View.VISIBLE

        //binding.tvTitle.text = text

        binding.tvRule.text = TextHighLight.setStringHighLight("点击查看LesDoB社区管理规定", "LesDoB社区管理规定")

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
                ARouter.getInstance().build("/question/activity/answer")
                    .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                    .withTransition(
                        R.anim.activity_bottom_in,
                        R.anim.activity_background
                    )
                    .withSerializable(TagConstant.QUESTION_DATA, question)
                    .withSerializable(TagConstant.ANSWER_DATA, answer)
                    .withInt(TagConstant.QUESTION_CHECK_ID, checkId)
                    .navigation(this@CheckFailAnswerDetailActivity)
            }
        })
    }

    private fun initWebView(htmlStr: String) {

        binding.webView.loadDataWithBaseURL(
            null,
            formatHtmlData(htmlStr),
            "text/html",
            "utf-8",
            null
        )
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
                    initWebView(it.content)

                    question = Question(
                        questionId = it.questionId,
                        questionContent = it.title,
                        questionDetail = it.description,
                        images = it.images,
                        video = it.video,
                    )
                    answer = AnswerBean(
                        answerId = it.answerId,
                        answerContent = it.content,
                        answerContentHtml = it.description,
                    )

                    //被修改过了就禁止修改
                    if (it.isUpdate != 0) {
                        unEnabledModifyStatus()
                        if (it.isUpdate == 2) {
                            binding.tvModify.text = "回答已修改,管理员已审核"
                        }
                    }
                }

            }
        }
    }

    override fun getData() {
    }

    /**
     * 格式化组装html内容
     */
    private fun formatHtmlData(originHtml: String): String {

        val spaceRegex = "\\s*|\t|\r|\n"

        val width720Regex = "width: 720px"
        val width360Regex = "width: 360px"
        val html = originHtml.replace(spaceRegex, "")
            .replace(width720Regex, "width: 100%")
            .replace(width360Regex, "width: 50%")
            .replace("width:720px", "width: 100%")
            .replace("width:360px", "width: 50%")
            .replace("border: 2px solid transparent", "")
            .replace("contenteditable=\"false\"", "")
            .replace("<video", "<video  controlslist=\"nodownload nofullscreen\"")

        val head = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-cn\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0, user-scalable=0'\">\n" +
                "</head><body><div class='content'>"
        val footer = "\n" +
                "</div></body>\n" +
                "\n" +
                "</html>"

        val jsData = this.let {
            "<script type=\"text/javascript\">${
                AssetsFileUtils.loadLocalFile(
                    this,
                    "answer_flexible.js"
                )
            } </script>"
        }
        val cssData = this.let {
            " <style type=\"text/css\">${
                AssetsFileUtils.loadLocalFile(
                    this,
                    "answerstyle.css"
                )
            } </style>"
        }
        val body = "<div class=\"content\" >${html}</div>"
        val htmlBuilder = StringBuilder(head)
        htmlBuilder.append(cssData)
        htmlBuilder.append(jsData)
        htmlBuilder.append(body)
        htmlBuilder.append(footer)

        return htmlBuilder.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: AnswerUpdateEvent) {
        if (event.answerId == answerId || event.checkId == checkId) {
            unEnabledModifyStatus()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateQuestion(event: AnswerAddEvent) {
        if (event.questionId == question.questionId) {
            unEnabledModifyStatus()
        }
    }

    private fun unEnabledModifyStatus() {
        binding.tvModify.text = "回答已修改，审核中"
        binding.tvModify.setTextColor(resources.getColor(R.color.color_BFBFBF))
        binding.ivModify.gone()
        binding.llModify.isEnabled = false
    }
}