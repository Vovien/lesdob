package com.tubewiki.mine.view.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClickUtils
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.common.CommonViewModel.Companion.configFlow
import com.jmbon.middleware.common.CommonViewModel.Companion.versionUpdate
import com.jmbon.middleware.utils.MarketUtils
import com.jmbon.widget.dialog.UpdateDialog
import com.lxj.xpopup.XPopup
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityAboutAppBinding

@Route(path = "/mine/about/activity")
class AboutActivity : ViewModelActivity<CommonViewModel, ActivityAboutAppBinding>() {

    private val currVersionCode = AppUtils.getAppVersionCode()

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.about_sisters_bond))

        binding.version.setRightString("V${AppUtils.getAppVersionName()}")

        binding.tvAboutDes.text = configFlow.value?.appIntroduce
//        SpanUtils.with(binding.tvDesc)
//            .append("医疗免责声明")
//            .setForegroundColor(ColorUtils.getColor(R.color.color_currency_error))
//            .append("：任何关于疾病的建议都不能替代执业医师的面对面诊断，请谨慎参阅。本站不承担由此引起的法律责任。")
//            .create()

        binding.version.setOnLongClickListener {
            return@setOnLongClickListener true
        }


        ClickUtils.applySingleDebouncing(binding.aboutPrivacyPolicy) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.PRIVACY_PROTOCOL)
                .withString("title", getString(R.string.about_privacy_policy))
                .navigation()

        }

        ClickUtils.applySingleDebouncing(binding.aboutTermsService) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.USER_PROTOCOL)
                .withString("title", getString(R.string.about_terms_service))
                .navigation()
        }

        ClickUtils.applySingleDebouncing(binding.aboutBusinessLicense) {
            ARouter.getInstance().build("/webview/activity")
                .withString(
                    "url",
                    "https://image.jmbon.com/uploads/home/20231016/TE9SqZKQa06WR870CRqcX19BnxO2x5pzzUpso8CB.png "
                )
                .withString("title", getString(R.string.about_business_license))
                .navigation()
        }
        ClickUtils.applySingleDebouncing(binding.version) {
            //检测新版本
//            getNewVersion()
        }

        ClickUtils.applySingleDebouncing(binding.appScore) {
            //去评分
            MarketUtils.getTools().startMarket(this@AboutActivity)
        }

        ClickUtils.applySingleDebouncing(binding.callApp) {
            ARouter.getInstance().build("/mine/about/contact_app").navigation()
        }

        binding.socialCircleRule.gone()
        ClickUtils.applySingleDebouncing(binding.socialCircleRule) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.COMMUNITY_MANAGE_RULE)
                .withString("title", getString(R.string.social_circle_rule))
                .navigation()
        }

        ClickUtils.applySingleDebouncing(binding.logOffTitle) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.NOTICE_OF_CANCELLATION_CLAUSE)
                .withString("title", getString(R.string.user_agreement_login_off_title))
                .navigation()
        }
    }

    override fun enableBack(): Boolean {
        return true
    }

    override fun initData() {

    }


    private fun getNewVersion() {
        launch {
            versionUpdate.next {
                this?.apply {
                    if (this.versionName.isNotEmpty() && this.isForce == 1) {
                        XPopup.Builder(this@AboutActivity)
                            .autoOpenSoftInput(false) //
                            //0：不强制更新，1：强制更新
                            .dismissOnTouchOutside(false)
                            .dismissOnBackPressed(false)
                            .asCustom(UpdateDialog(this@AboutActivity, 1, this.versionName) {
                                viewModel.downloadApp(this, this@AboutActivity)
                            })
                            .show()
                    } else {
                        "已是最新版本".showToast()
                    }
                }
            }
        }
    }

    override fun getData() {

    }
}