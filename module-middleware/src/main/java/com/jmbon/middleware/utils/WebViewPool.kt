package com.jmbon.middleware.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.widget.html.AnswerX5WebView
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class WebViewPool {

    companion object {


    }

    private var webViewPool: MutableList<WebVieWrap> = ArrayList()


    private var maxSize = 5


    /**
     * webView 初始化
     * 最好放在application onCreate里
     */
    fun init() {
        for (i in 0 until maxSize) {
            createWebViewWrap()
        }
    }

    private fun createWebViewWrap(): WebVieWrap {
        val webView = createWebView(Utils.getApp())

        val webVieWrap = WebVieWrap()
        webVieWrap.x5WebView = webView
        webViewPool.add(webVieWrap)
        return webVieWrap
    }

    private fun createWebView(context: Context): AnswerX5WebView {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        //动态设置id.否则AndResGuard资源混淆后id找不到
        val x5WebView = AnswerX5WebView(context)
        x5WebView.tag = webViewPool.size
        x5WebView.settings?.apply {
            loadWithOverviewMode = true
            allowContentAccess = true
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            setAllowFileAccessFromFileURLs(true)
            setAllowUniversalAccessFromFileURLs(true)
            // 屏蔽字体大小
            textZoom = 100
        }
        x5WebView.addJavascriptInterface(JsImageLoad(context), "imageLoader")
        x5WebView.addJavascriptInterface(this, "android")
        x5WebView.setShowProgress(false)
        x5WebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                // x5WebView.loadUrl("javascript:window.android.resize(document.body.getBoundingClientRect().height)")
                x5WebView.evaluateJavascript(
                    "(function() { return document.body.getBoundingClientRect().height; })();"
                ) {
                    if (it.isNullOrEmpty() || it.equals("null")) {
                        return@evaluateJavascript
                    }
                    x5WebView.post {
                        try {
                            setWebViewHeight(x5WebView, SizeUtils.dp2px(it.toFloat()))
                        } catch (e: Exception) {
                            LogUtils.e(e)
                        }
                    }
                }
                if (x5WebView.onHtml5Progress != null) {
                    x5WebView.onHtml5Progress.onProgressFinish()
                }
//                x5WebView.postDelayed({
//                    val wlp = x5WebView.layoutParams as FrameLayout.LayoutParams
//                    wlp.height = SizeUtils.dp2px(x5WebView.contentHeight.toFloat())
//                    x5WebView.layoutParams = wlp
//                }, 500)

                super.onPageFinished(p0, p1)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    H5ArouterUtils.urlProcessing(it)
                }
                return true
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.url.toString().let {
                    H5ArouterUtils.urlProcessing(it)
                }
                return true
            }

        }

        return x5WebView
    }

    private fun setWebViewHeight(x5WebView: AnswerX5WebView?, height: Int) {
        x5WebView?.apply {
            val layoutParams: ViewGroup.LayoutParams = this.layoutParams
            layoutParams.height = height
            this.layoutParams = layoutParams
        }
    }

    /**
     * 获取webView
     */
    fun getWebView(): AnswerX5WebView? {
        Log.e("getWebView", "${webViewPool.size}")
        if (webViewPool.size < maxSize) {
            return buildWebView()
        }
        var x5WebView = checkWebView()
        if (x5WebView != null) {
            return x5WebView
        }

        var singleWebView = buildWebView()
        return singleWebView
    }


    private fun checkWebView(): AnswerX5WebView? {
        val notUser = webViewPool.filter { !it.inUse }

        return if (notUser.isNullOrEmpty())
            null
        else {
            notUser[0].inUse = true
            return notUser[0].x5WebView
        }
    }


    /**
     * 回收webView
     * @param webView
     */
    fun recycleWebView(webView: AnswerX5WebView) {
        for (i in webViewPool.indices) {
            val webVieWrap = webViewPool[i]
            val temp = webVieWrap.x5WebView
            if (webView === temp) {
                temp.destroy()
                webVieWrap.inUse = false
                break
            }
        }
    }

    /**
     * 创建webView
     * @return
     */
    private fun buildWebView(): AnswerX5WebView? {
        val webVieWrap = createWebViewWrap()
        webVieWrap.inUse = true
        return webVieWrap.x5WebView
    }


    /**
     * 销毁连接池
     */
    fun destroyPool() {
        try {
            if (webViewPool.size == 0) {
                return
            }
            for (webVieWrap in webViewPool) {
                val webView = webVieWrap.x5WebView
                webView?.let {
                    it.loadUrl("about:blank")
                    it.clearCache(true)
                    it.webViewClient = null
                    it.webChromeClient = null
                    it.destroy()
                    it == null
                }

            }
            webViewPool.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 回收webView ,解绑
     *
     * @param webView 需要被回收的webView
     */
    fun recycleWebView(view: ViewGroup?, webView: AnswerX5WebView?) {
        if (view != null && webView != null) {
            recycleWebView(webView)
            view.removeView(webView)
        }
    }

    private val TAG = "WebViewPool"
    fun recycleWebViewNoDestroy(view: ViewGroup?, webView: AnswerX5WebView?) {
        if (view != null && webView != null) {
            for (i in webViewPool.indices) {
                val webVieWrap = webViewPool[i]
                val temp = webVieWrap.x5WebView
                if (webView === temp) {
                    temp.loadUrl("about:blank")
                    webVieWrap.inUse = false
                    break
                }
            }
            if (webView.parent != null) {
                (webView.parent as ViewGroup).removeAllViews()
            }
        }
    }

    fun destroyWebViewNoUser(webView1: AnswerX5WebView?, webView2: AnswerX5WebView?) {
        try {
            if (webView1 != null) {
                for (i in (webViewPool.size - 1) downTo 0) {
                    Log.e("WebView", "destroy:${i}")
                    val webVieWrap = webViewPool[i]
                    val temp = webVieWrap.x5WebView
                    if (webView1 === temp || webView2 === temp) {
                        webVieWrap.inUse = true
                    } else {
                        temp?.let {
                            if (it.parent != null) {
                                (it.parent as ViewGroup).removeAllViews()
                            }
                            it!!.destroy()
                            it == null
                            webViewPool.removeAt(i)
                        }
                    }

                }
            } else {
                Log.e("WebView", "webview is null")
            }
            Log.e("WebView", "destroyWebViewNoUser:${webViewPool.size}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 设置webView池个数
     *
     * @param size webView池个数
     */
    fun setMaxPoolSize(size: Int) {
        maxSize = size
    }


    fun getWebViewPos(webView: AnswerX5WebView):Int{
        webViewPool.forEach {
            if(it.x5WebView == webView){
                return it.pos
            }
        }
        return -1
    }
    fun setWebViewPos(web:AnswerX5WebView,pos:Int){
        webViewPool.forEach {
            if(it.x5WebView == web){
                it.pos = pos
            }
        }
    }

    class WebVieWrap {
        var x5WebView: AnswerX5WebView? = null
        var inUse = false
        var isSingle = false
        var pos = -1
    }

}
