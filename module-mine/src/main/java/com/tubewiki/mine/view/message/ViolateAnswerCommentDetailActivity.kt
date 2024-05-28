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
import com.blankj.utilcode.util.PhoneUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.bean.AnswerBean
import com.jmbon.middleware.utils.AssetsFileUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityViolateAnswerDetailBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup


/**
 * 回答和评论违规详情页
 */
@Route(path = "/mine/message/violate_answer_comment")
class ViolateAnswerCommentDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityViolateAnswerDetailBinding>() {

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "question"

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var title = ""

    @Autowired(name = TagConstant.QUESTION_CHECK_ID)
    @JvmField
    var checkId = 0

    @Autowired(name = TagConstant.QUESTION_ID)
    @JvmField
    var questionId = 0

    @Autowired(name = TagConstant.ANSWER_ID)
    @JvmField
    var answerId = 0

    var answer = AnswerBean()

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

        setTitleName(getString(R.string.message_violate_rule))
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

        var text = ""
        if (type.equals("question")) {
            text = "您在「${title}」问题中的评论涉嫌违规，已被官方删除。"
            binding.tvComment.text = answer.subAnswer.answerContent
            binding.webView.visibility = View.GONE
        } else if (type.equals("question_answer")) {
            text = "您在「${title}」问题中的回答涉嫌违规，已被官方删除。"
            binding.tvComment.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            //initWebView(answer.answerContentHtml)

        } else if (type.equals("article")) {
            text = "您在《${title}》文章中的评论涉嫌违规，已被官方删除。"
            binding.tvComment.text = answer.answerContent
            binding.webView.visibility = View.GONE
        } else if (type.equals("video")) {
            text = "您在《${title}》视频中的评论涉嫌违规，已被官方删除。"
            binding.tvComment.text = answer.answerContent
            binding.webView.visibility = View.GONE
        } else if (type.equals("examine")) {
            //回答的审核不通过
            setTitleName(getString(R.string.message_audit_result))
            text = "您的回答修改审核未通过。"
            binding.tvComment.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            initWebView(answer.answerContentHtml)
        } else if (type.equals("check_answer")) {
            //违规回答整改
            text = "您在「${title}」问题中的回答检测到有违规内容，我们已将其标记，请及时修改。"

            binding.llModify.visible()
            binding.tvComment.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            initWebView(answer.answerContentHtml)
        }
        binding.tvTitle.text = text

        binding.tvRule.text = TextHighLight.setStringHighLight("点击查看LesDoB社区管理规定", "LesDoB社区管理规定")

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
                //.withSerializable(TagConstant.QUESTION_DATA, getQuestion())
                //.withSerializable(TagConstant.ANSWER_DATA, answerBean)
                .navigation(this)
        })


        binding.ivPhone.setOnClickListener {
            ARouter.getInstance().build("/mine/message/jm_email").navigation()

        }
    }


    private fun showCallDialog(number: String) {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.call_kefu),
                CustomDialogTypeBean.TYPE_TITLE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                number,
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(this, listData) { select ->
                    if (select == 1)
                        PhoneUtils.dial(number)
                }
            ).show()
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

    override fun getData() {
        started {
            if ("question_answer".equals(type)) {
                viewModel.getViolationQADetail(answerId, "answer").netCatch {
                    showErrorState()
                    message.showToast()
                }.next {
                    initWebView(answer.answerContentHtml.ifEmpty { answer.answerContent })
                    showContentState()
                }

            }
        }
    }

    /**
     * 格式化组装html内容
     */
    fun formatHtmlData(originHtml: String): String {

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
                "</head><body>"
        val footer = "\n" +
                "</body>\n" +
                "\n" +
                "</html>"

        val jsData = baseContext?.let {
            "<script type=\"text/javascript\">${
                AssetsFileUtils.loadLocalFile(
                    it,
                    "android_flexible.js"
                )
            } </script>"
        }
        val cssData = baseContext?.let {
            " <style type=\"text/css\">${
                AssetsFileUtils.loadLocalFile(
                    it,
                    "answerstyle.css"
                )
            } </style>"
        }
        val body = "<div class=\"content\" >${html}</div>"
        var htmlBuilder = StringBuilder(head)
        htmlBuilder.append(cssData)
        htmlBuilder.append(jsData)
        htmlBuilder.append(body)
        htmlBuilder.append(footer)

        return htmlBuilder.toString()
    }


}