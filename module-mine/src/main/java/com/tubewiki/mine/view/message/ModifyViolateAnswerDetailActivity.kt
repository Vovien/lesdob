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
import com.jmbon.middleware.bean.AnswerBean
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.ViolateQuestionData
import com.jmbon.middleware.bean.event.AnswerUpdateEvent
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.utils.AssetsFileUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AskDetailImageAdapter
import com.tubewiki.mine.databinding.ActivityModifyViolateAnswerDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.jmbon.middleware.js.JsImageLoad
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 回答和评论违规详情页
 */
@Route(path = "/mine/message/modify_violate_answer")
class ModifyViolateAnswerDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityModifyViolateAnswerDetailBinding>() {

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "answer_video" //answer_video 违规视频  answer_image 违规图片

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var title = ""

    @Autowired(name = TagConstant.ANSWER_ID)
    @JvmField
    var answerId = 0

    @Autowired(name = TagConstant.QUESTION_CHECK_ID)
    @JvmField
    var checkId = 0

    var question = Question()
    var answer = AnswerBean()

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        // setTitleName(getString(R.string.message_violate_rule))
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
            StatusBarCompat.getStatusBarHeight(this) + binding.tvTitle.paddingTop
        binding.tvTitle.setPadding(
            binding.tvTitle.paddingStart,
            statusHeight,
            binding.tvTitle.paddingEnd,
            binding.tvTitle.paddingBottom
        )



        binding.tvComment.visibility = View.GONE
        binding.webView.visibility = View.GONE

        //违规回答整改

        if (type == "answer_video") {
            binding.tvTitle.text =
                "您在「${title}」问题中,提交的回答里的视频涉嫌违规，未通过审核，不予展示。"
            binding.llModify.visible()
            setTitleName(getString(R.string.message_check_result))
        } else {
            binding.tvTitle.text = "您在「${title}」问题中,提交的回答里的图片涉嫌违规，已被官方删除。"
            binding.llModify.gone()
            setTitleName(getString(R.string.message_violate_rule))
        }

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

        binding.llModify.setOnSingleClickListener({
            ARouter.getInstance().build("/question/activity/answer")
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .withTransition(
                    R.anim.activity_bottom_in,
                    R.anim.activity_background
                )
                .withSerializable(TagConstant.QUESTION_DATA, question)
                .withSerializable(TagConstant.ANSWER_DATA, answer)
                .withInt(TagConstant.QUESTION_CHECK_ID, checkId)
                //.withString("illegal_path", pathArr)
                .navigation(this)
        })


        binding.ivPhone.setOnClickListener {

            ARouter.getInstance().build("/mine/message/jm_email").navigation()

//            viewModel.getOfficialPhone {
//                showCallDialog(it)
//            }
        }

        binding.webView.apply {
            settings?.loadWithOverviewMode = true
            settings?.allowContentAccess = true

            addJavascriptInterface(
                JsImageLoad(this@ModifyViolateAnswerDetailActivity),
                "imageLoader"
            )

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    super.onPageFinished(p0, p1)

                    if (!this@ModifyViolateAnswerDetailActivity.isFinishing) {
                        // loadDataWhenFinish()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }
            }
        }
    }

    private fun loadDataWhenFinish() {
        if (pathArr.isNullOrEmpty()) {
            return
        }
        binding.webView.loadUrl("javascript:changeBorder('${pathArr}')")
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

    override fun initData() {
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()
    }

    var pathArr = ""
    override fun getData() {
        started {

            viewModel.getExamineDetail(checkId).netCatch {
                message.showToast()
                showErrorState()
            }.next {
                showContentState()
                let {
                    var itemQuestion = Question()
                    itemQuestion.questionId = it.questionId
                    itemQuestion.questionDetail = it.description
                    itemQuestion.questionContent = it.title
                    itemQuestion.topics = it.topics
                    itemQuestion.images = it.images
                    itemQuestion.video = it.video
                    question = itemQuestion

                    var itemAnswer = AnswerBean()
                    itemAnswer.answerId = it.answerId
                    itemAnswer.answerContent = it.content
                    itemAnswer.answerContentHtml = it.content
                    answer = itemAnswer

                    //被修改过了就禁止修改
                    if (it.isUpdate != 0) {
                        unEnabledModifyStatus()
                        if (it.isUpdate == 2) {
                            binding.tvModify.text = "回答已修改,管理员已审核"
                        }
                    }


//            var originStr = it.originAnswerContentSvg
//
//            //originStr = setWordTag(it,  originStr)
//
//            //var originAnswerStr = setWordTag(it, it.originAnswerContent)
//            var originAnswerStr = it.originAnswerContent
//
//            answer.answerContentHtml = originAnswerStr
//            answer.answerContent = originAnswerStr

                    // initWebView(originStr)

                    //违禁图片视频路径
                    var limitPath = arrayListOf<String>()
                    val data = arrayListOf<LocalMedia>()
                    //视频违规或者图片违规
                    if (this@ModifyViolateAnswerDetailActivity.type == "answer_video") {
                        if (it.video.isNullOrEmpty()) {
                            limitPath.addAll(it.videos)
                        } else {
                            limitPath.add(it.video)
                        }

                        if (it.video.isNotNullEmpty()) {
                            data.add(
                                LocalMedia(
                                    it.video,
                                    0,
                                    0,
                                    PictureMimeType.MIME_TYPE_VIDEO
                                )
                            )
                        } else {
                            data.addAll(it.videos!!.map { path ->
                                LocalMedia(
                                    path,
                                    0,
                                    0,
                                    PictureMimeType.MIME_TYPE_VIDEO
                                )
                            }.toList())
                        }
                    } else {
                        it.images?.apply {
                            limitPath.addAll(this)

                        } ?: let { activity ->
                            it.images = arrayListOf<String>()
                        }


                        if (it.blockImages.isNotNullEmpty()) {
                            data.addAll(it.blockImages!!.map { path -> LocalMedia(path, 0, 0, "") }
                                .toList())
                        } else {
                            if (it.images.isNotNullEmpty()) {
                                data.addAll(it.images!!.map { path -> LocalMedia(path, 0, 0, "") }
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

                    if (limitPath.isNotNullEmpty()) {
                        pathArr = "["
                        limitPath.forEach {
                            pathArr += "\"$it\","
                        }
                        pathArr = pathArr.substring(0, pathArr.length - 1)
                        pathArr += "]"
                    }
                }

            }
        }

    }

    private fun setWordTag(
        it: ViolateQuestionData,
        originStr: String
    ): String {
        var originStr1 = originStr
        it.blockData.text.forEach { text ->
            // list.add(text.context)
            if (text.context.contains("~")) {
                //匹配前缀
                var word = text.context.split("~")[0]
                var data =
                    "<u style=\"color: #FF5050;text-decoration: none;\"  contenteditable=\"false\">${word}</u>"
                // var lowcaseStr = originStr.lowercase()
                // originStr = originStr.replace(word, word, true)
                originStr1 = originStr1.replace(word, data, true)
            } else if (text.context.contains("&")) {
                //间隔匹配
                var data =
                    "<u style=\"color: #FF5050;text-decoration: none;\"  contenteditable=\"false\">${text.context}</u>"
                originStr1 = originStr1.replace(text.context.replace("&", ""), data, true)
            } else {
                //完全匹配
                var data =
                    "<u style=\"color: #FF5050;text-decoration: none;\"  contenteditable=\"false\">${text.context}</u>"
                originStr1 = originStr1.replace(text.context, data, true)
            }
        }

        return originStr1
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
        if (event.answerId == answerId) {
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