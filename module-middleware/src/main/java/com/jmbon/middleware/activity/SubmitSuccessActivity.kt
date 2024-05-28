package com.jmbon.middleware.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.ActivitySubmitSuccessBinding
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.navigationWithFinish
import com.jmbon.widget.progress_button.JmbonButton

@Route(path = "/middleware/tort/submit")
class SubmitSuccessActivity : AppBaseActivity<ActivitySubmitSuccessBinding>() {

    @Autowired(name = TagConstant.PARAMS4)
    @JvmField
    var isLogOff: Boolean = false

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var content: String = ""


    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var subContent: String = ""

    @Autowired(name = TagConstant.PARAMS3)
    @JvmField
    var needSub: Boolean = true
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

        supportActionBar?.hide()

    }

    override fun initView(savedInstanceState: Bundle?) {

        if (content.isNotNullEmpty()) {
            binding.tvContent.text = content
        }
        if (needSub) {
            binding.textSub.visible()
            if (subContent.isNotNullEmpty())
                binding.textSub.text = subContent
        } else {
            binding.textSub.gone()
        }
        if (isLogOff) {
            binding.submittedSuccessful.text = StringUtils.getString(R.string.log_off_success)
            binding.jbFinish.setBtnStyle(JmbonButton.GREEN_FULL)
        }


        binding.jbFinish.setOnClickListener {
            if (isLogOff) {
                ARouter.getInstance().build("/main/main").navigationWithFinish(this)
            }
            finish()
        }
    }

    override fun initData() {

    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    override fun getData() {

    }
}