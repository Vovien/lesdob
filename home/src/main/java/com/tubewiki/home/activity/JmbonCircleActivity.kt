package com.tubewiki.home.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.AppUtils
import com.jmbon.middleware.dialog.RecommendFeedbackDialog
import com.jmbon.middleware.dialog.TipsDialog
import com.jmbon.middleware.utils.*
import com.lxj.xpopup.XPopup
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ActivityJmbonCircleDetailBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel


/**
 * 圈子推广姐妹邦详情页
 */
@Route(path = "/home/activity/jmbon_circle")
class JmbonCircleActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityJmbonCircleDetailBinding>() {


    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var spreadImg: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        StatusBarCompat.setTransparentStatusBar(this.window)
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.ivCircle.loadRadius(spreadImg, 8f.dp())


        binding.jbDownload.setOnClickListener {

            showTipsDialog()
        }

    }

    private fun showTipsDialog() {
        XPopup.Builder(this)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(true)
            .enableDrag(false)

            .asCustom(TipsDialog(this, "温馨提示", "即将跳转到孕小帮APP", true, "不了，谢谢", "立即前往") {
                //去评分
                if (AppUtils.isAppInstalled("com.jmbon.android")) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    val componentName =
                        ComponentName("com.jmbon.android", "com.jmbon.android.view.WelcomeActivity")
                    intent.component = componentName
                    startActivity(intent)
                } else {
                    if (!MarketUtils.getTools().startMarket(this, "com.jmbon.android")) {
                        openBrowser("https://home.jmbon.com/downloadApp")
                    }
                }
            }).show()
    }

    private fun openBrowser(url: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } else {
            "链接错误或无默认浏览器".showToast()
        }
    }

    override fun initData() {
    }


    override fun getData() {
    }


}