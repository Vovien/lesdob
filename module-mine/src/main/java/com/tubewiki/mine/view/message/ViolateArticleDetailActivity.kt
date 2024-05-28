package com.tubewiki.mine.view.message

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig.COMMUNITY_MANAGE_RULE
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.TextHighLight
import com.apkdv.mvvmfast.view.state.initState
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.bean.ArticleInfo
import com.jmbon.middleware.bean.WebScrollOffset
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityViolateArticleDetailBinding
import com.tubewiki.mine.databinding.LayoutInfoItemBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.widget.html.X5WebView
import com.lxj.xpopup.XPopup
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


/**
 * 违规文章详情页
 */
@Route(path = "/mine/message/violate_article")
class ViolateArticleDetailActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityViolateArticleDetailBinding>() {


    @Autowired(name = TagConstant.ARTICLE_ID)
    @JvmField
    var articleId = 0


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

        binding.tvRule.text = TextHighLight.setStringHighLight("点击查看LesDoB社区管理规定", "LesDoB社区管理规定")
        binding.tvRule.setOnSingleClickListener({
            ARouter.getInstance().build("/webview/activity")
                .withString("url", COMMUNITY_MANAGE_RULE)
                .withString("title", getString(R.string.social_circle_rule))
                .navigation()
        })
        binding.ivPhone.setOnClickListener {
            ARouter.getInstance().build("/mine/message/jm_email").navigation()
//            viewModel.getOfficialPhone {
//                showCallDialog(it)
//            }
        }


        initWebView()


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


    private var x5WebView: X5WebView? = null

    private fun initWebView() {

        x5WebView = X5WebView(this)
        x5WebView?.setPadding(20f.dp(), 0, 20f.dp(), 0)

        x5WebView?.settings?.loadWithOverviewMode = true
        x5WebView?.settings?.allowContentAccess = true
        x5WebView?.addJavascriptInterface(JsImageLoad(this), "imageLoader")

        x5WebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                x5WebView?.loadUrl(
                    """
                    javascript:(function(){
                     let objs = document.getElementsByTagName("img");
                    let imgUrl = "";
                    for (let i = 0; i < objs.length; i++) {
                        imgUrl += objs[i].src + ',';
                        objs[i].onclick = function () {
                            window.imageLoader.imageBrowsing(i,imgUrl);
                        }
                    }
                    })()
                """.trimIndent()
                )

                x5WebView?.evaluateJavascript("javascript:getTag()") {
                    val list = GsonUtils.fromJson<ArrayList<WebScrollOffset>>(
                        it,
                        GsonUtils.getListType(WebScrollOffset::class.java)
                    )

                }
                false

            }
        }
        binding.textIntroduction.maxLine = 6
        binding.textIntroduction.setCloseStr(getString(R.string.all_profile))
        binding.textIntroduction.setOpenStr(getString(R.string.introduction_fold))


    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()
    }


    override fun initData() {
        viewModel.getArticleDetails(articleId)

    }

    override fun getData() {
        viewModel.articleDetails.observe(this) {

            if (it == null) {
                showErrorState()
                return@observe
            } else {
                showContentState()
            }

            // 设置右侧点击
            binding.csContent.apply {
                binding.flWeb.removeAllViews()

                binding.flWeb.addView(x5WebView)

                x5WebView?.loadDataWithBaseURL(
                    BuildConfig.H5_URL,
                    HtmlTools.createHtml("${it.content}" ?: ""),
                    "text/html",
                    "UTF-8", null
                )

                if (it.abstracts.isNotNullEmpty()) {
                    binding.textIntroduction.setText(
                        HtmlTools.delHTMLTag(
                            it.abstracts
                                ?: ""
                        )
                    )
                } else {
                    binding.mcInfo.visibility = View.GONE
                    binding.space.visibility = View.GONE
                    binding.space2.visibility = View.GONE
                    binding.viewGray.visibility = View.GONE
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    binding.textIntroduction.textView.lineHeight = SizeUtils.dp2px(28f)
                }
                if (it.infos.isNotNullEmpty()) {
                    binding.infoLayout.visibility = View.VISIBLE
                    createInfoItem(it.infos, binding.llInfo)
                } else {
                    binding.infoLayout.visibility = View.GONE
                }


            }


        }
    }

    private fun createInfoItem(infoList: ArrayList<ArticleInfo>, content: LinearLayout) {
        val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(54f))
        infoList.forEach {
            val itemView = LayoutInfoItemBinding.inflate(layoutInflater)
            itemView.apply {
                textInfoKey.text = it.key ?: ""
                textInfoValue.text = it.value ?: ""
            }
            itemView.root.layoutParams = params
            content.addView(itemView.root)
        }
    }

}