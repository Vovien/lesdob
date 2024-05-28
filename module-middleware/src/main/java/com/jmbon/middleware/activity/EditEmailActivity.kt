package com.jmbon.middleware.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.RegexUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ActivityEditEmailLayoutBinding
import com.jmbon.middleware.model.CheckErrorViewModel
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty

import com.jmbon.widget.GeneralDialog

/**
 * 修改邮件
 */
@Route(path = "/middleware/activity/edit_email")
class EditEmailActivity :
    ViewModelActivity<CheckErrorViewModel, ActivityEditEmailLayoutBinding>() {


    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var email: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.contact_email))
        setRightTextMenu(getString(R.string.save))


        titleBarView.rightTextView.setTextColor(resources.getColor(R.color.color_dis_enable))
        titleBarView.rightTextView.isEnabled = false
        binding.inputEmail.addTextChangedListener(viewModel.textWatch)

        if (email.isNotNullEmpty() && email != "请填写") {
            binding.inputEmail.setText(email)
        }

        binding.inputEmail.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputEmail.setBackgroundResource(R.drawable.gray_corner_bg_shape)
            }
        }

        titleBarView.rightTextView.setOnClickListener {
            setInfo()
        }

    }

    val maxSize = 100
    val minSize = 1

    @SuppressLint("SetTextI18n")
    override fun initData() {
        viewModel.textSize.observe(this@EditEmailActivity) {
            if (it >= minSize && email != binding.inputEmail.text.toString()
                    .trim()
            ) {
                titleBarView.rightTextView.setTextColor(
                    ContextCompat.getColor(
                        this@EditEmailActivity,
                        R.color.color_currency
                    )
                )
                titleBarView.rightTextView.isEnabled = true
            } else {
                titleBarView.rightTextView.isEnabled = false
                titleBarView.rightTextView.setTextColor(
                    ContextCompat.getColor(
                        this@EditEmailActivity,
                        R.color.color_dis_enable
                    )
                )
            }

        }

    }

    override fun getData() {

    }

    override fun onBackPressed() {
        if (binding.inputEmail.text.isNullOrBlank() || email == binding.inputEmail.text.toString()
            || binding.inputEmail.text.toString().isEmpty()
        ) {
            super.onBackPressed()
        } else {
            GeneralDialog.showDialog(this,
                getString(R.string.exit_giveup_data),
                "",
                "再等等",
                "退出",
                { super.onBackPressed()  },
                { })
        }
    }

    private fun setInfo() {
        val email = binding.inputEmail.text.toString()
        if (!RegexUtils.isMatch(Constant.REGEX_EMAIL,email)) {
            "请输入正确邮箱地址".showToast()
            return
        }

        var intent = Intent()
        intent.putExtra("email", email)
        setResult(99, intent)
        finish()
    }


}