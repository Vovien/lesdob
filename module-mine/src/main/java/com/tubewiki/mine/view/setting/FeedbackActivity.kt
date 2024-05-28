package com.tubewiki.mine.view.setting

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.SolveEditTextScrollClash
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.bean.ArticleList
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.widget.progress_button.JmbonButton
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityFeedbackBinding
import com.tubewiki.mine.view.model.SettingViewModel

@Route(path = "/mine/setting/feedback")
class FeedbackActivity : ViewModelActivity<SettingViewModel, ActivityFeedbackBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var itemType = 0

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var itemId = 0

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (type == 1) {
            setTitleName("我想看的内容")
            binding.edit.hint = "写下您想看的内容~"
        } else {
            setTitleName("意见反馈")
            binding.edit.hint = "写下您宝贵的意见~"
        }

        titleBarView.setRightView(R.layout.custom_titlebar_right_submit_view)
        var jmButton = titleBarView.rightCustomView.findViewById<JmbonButton>(R.id.tv_submit)
        binding.apply {

            KeyboardUtils.showSoftInput(edit)
            edit.setOnTouchListener(SolveEditTextScrollClash(edit))

            edit.addTextChangedListener(afterTextChanged = {
                jmButton.isEnabled = it.isNotNullEmpty()
            })

            jmButton.setOnClickListener {
                started {
                    if (edit.text.toString().trim().isNullOrEmpty()) {
                        if (type == 1) {
                            "请输入想看的内容".showToast()
                        } else {
                            "请输入反馈内容".showToast()
                        }
                        jmButton.stateButton()
                        return@started
                    }

                    viewModel.feedback(
                        edit.text.toString().trim(), type
                    ).netCatch {
                        message.showToast()
                        jmButton.stateButton()
                    }.next {

                        if (type == 1) {
                            true.saveToMMKV(MMKVKey.RECOMMEND_FEEDBACK)
                        }

                        ARouter.getInstance().build("/middleware/tort/submit")
                            .withString(
                                TagConstant.PARAMS,
                                StringUtils.getString(R.string.thank_you_for_your_contribution_to_community)
                            )
                            .withBoolean(TagConstant.PARAMS3, false)
                            .navigation()
                        edit.text.clear()

                        finish()
                    }
                }
            }
        }

    }

    override fun initData() {

    }

    override fun getData() {

    }
}