package com.jmbon.widget.html

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.jmbon.widget.databinding.ActivityHtml5Binding

@Route(path = "/webview/activity")
class Html5Activity : AppBaseActivity<ActivityHtml5Binding>(), Html5WebView.OnHtml5Listener,
    Html5WebView.OnHtml5CallPhoneListener, Html5WebView.OnHtml5Progress ,
    GeneralWebView.OnHtml5Listener,
    GeneralWebView.OnHtml5CallPhoneListener, GeneralWebView.OnHtml5Progress {


    @JvmField
    @Autowired
    var url = ""

    @JvmField
    @Autowired
    var title = ""

    @JvmField
    @Autowired
    var htmlData = ""

    @JvmField
    @Autowired
    var enableTBS = true

    private val html5WebView by lazy { Html5WebView(this) }
    private val generalWebView by lazy { GeneralWebView(this) }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (title.isNotEmpty()) {
            setTitleName(title)
        }

        binding.root.addView(if (enableTBS) html5WebView else generalWebView)
        if (enableTBS) {
            html5WebView.setOnHtml5Listener(this)
            html5WebView.setOnHtml5CallPhoneListener(this)
            html5WebView.setOnHtml5Progress(this)
        } else {
            generalWebView.setOnHtml5Listener(this)
            generalWebView.setOnHtml5CallPhoneListener(this)
            generalWebView.setOnHtml5Progress(this)
        }
    }


    override fun initData() {
        if (url.isNotEmpty())
            if (enableTBS)
                html5WebView.loadUrl(url)
            else generalWebView.loadUrl(url)
        if (htmlData.isNotEmpty())
            if (enableTBS)
                html5WebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
            else generalWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
    }

    override fun getData() {
    }

    override fun onDestroy() {
        if (enableTBS)
            html5WebView.destroy()
        else generalWebView.destroy()
        binding.root.removeAllViews()
        super.onDestroy()
    }

    override fun onReceivedTitle(webTitle: String?) {
        if (title.isEmpty() && !webTitle.isNullOrEmpty())
            setTitleName(webTitle)

    }

    override fun onCallPhone(Phone: String?) {

    }

    override fun onProgressStart() {

    }

    override fun onProgressFinish() {

    }

    override fun onProgressError() {

    }
}