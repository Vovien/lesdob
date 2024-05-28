package com.tubewiki.mine.view.setting.password

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.RegexUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivitySetNewPasswordBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import kotlinx.coroutines.flow.onCompletion
import java.util.regex.Pattern


/**
 * 设置新的密码
 * 新的邮箱
 */

@Route(path = "/mine/setting/set/password")
class SetNewPasswordActivity :
    ViewModelActivity<SettingViewModel, ActivitySetNewPasswordBinding>(), TextWatcher {

    private var showPassword = false

    @Autowired
    @JvmField
    var type: Int = 0

    @Autowired
    @JvmField
    var phone: String = ""

    @Autowired
    @JvmField
    var token: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (type == CaptchaViewModel.SET_NEW_EMAIL || type == CaptchaViewModel.UPDATE_EMAIL) {
            binding.apply {
                textMobileTitle.text =
                    if (type == CaptchaViewModel.SET_NEW_EMAIL) getString(R.string.binding_email) else getString(
                        R.string.binding_new_email
                    )
                textRequirements.text = getString(R.string.an_account_can_only_bind_mailbox)
//                textRequirements.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                editPasswd.hint = getString(R.string.enter_correct_email_format)
                editPasswd.filters = arrayOf<InputFilter>(LengthFilter(Int.MAX_VALUE))
                imageSeePass.visibility = View.GONE
                sbGetCaptcha.text = "下一步"
            }
        } else if (type == CaptchaViewModel.SET_NEW_PASSWORD || type == CaptchaViewModel.UPDATE_PASSWORD) {
            binding.apply {
                textMobileTitle.text = getString(
                    if (type == CaptchaViewModel.SET_NEW_PASSWORD) R.string.setup_password
                    else R.string.enter_new_password
                )
                textRequirements.text = getString(R.string.password_is_required_to)
            }
            binding.editPasswd.filters = arrayOf<InputFilter>(LengthFilter(20))
            binding.editPasswd.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            binding.editPasswd.filters = filter

        }


        setPasswordShow()

        binding.editPasswd.addTextChangedListener(this)

    }

    private val filter = arrayOf(
        InputFilter { charSequence, i, i1, spanned, i2, i3 ->
            val pattern = Pattern.matches(pattern, charSequence.toString())
            val patternEn = Pattern.matches(patternEn, charSequence.toString())
            if (pattern || patternEn)
                null
            else ""
        }
    )

    private fun setPasswordShow() {
        binding.imageSeePass.setOnClickListener {
            binding.editPasswd.filters = arrayOf()
            showPassword = !showPassword
            binding.imageSeePass.setImageResource(if (showPassword) R.drawable.login_pwd_show_icon else R.drawable.login_pwd_hide_icon)
            binding.editPasswd.transformationMethod =
                if (showPassword) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            binding.editPasswd.setSelection(binding.editPasswd.text?.length ?: 0)
            binding.editPasswd.filters = filter
        }
    }

    override fun initData() {

        binding.sbGetCaptcha.setOnClickListener {
            val str = binding.editPasswd.text.toString()
            if (type == CaptchaViewModel.SET_NEW_PASSWORD || type == CaptchaViewModel.UPDATE_PASSWORD
                || type == CaptchaViewModel.RETRIEVE_PASSWORD
            ) {
                if (RegexUtils.isMatch(pattern1, str)) {
                    viewModel.setPassword(str, phone, token)
                } else passwordError()
            } else {
                if (RegexUtils.isMatch(Constant.REGEX_EMAIL,str))
                    sendEmail(str)
                else emailError(getString(R.string.please_enter_correct_email_format))
            }
        }

        viewModel.setPass.observe(this) {
            if (it) {
                ARouter.getInstance().build("/mine/setting/password/success")
                    .withInt("type", type)
                    .navigation()
                finish()
            }
            binding.sbGetCaptcha.stateButton()

        }
    }

    private fun sendEmail(str: String) {
        started {
            viewModel.sendVerifyEmail(str, token).netCatch {
                if (this.code.toInt() == 409) {
                    emailError(getString(R.string.please_enter_correct_email_format))
                } else emailError(message ?: "")
            }.onCompletion {
                binding.sbGetCaptcha.stateButton()
            }.next {
                if (data1.isNotNullEmpty() && data2.isNotNullEmpty())
                    ARouter.getInstance().build("/mine/setting/validation/email")
                        .withString("msnToken", token)
                        .withString("email", data2)
                        .withString("emailToken", data1)
                        .navigation()
                finish()
            }
        }

    }

    private fun passwordError() {
        binding.textRequirements.text =
            getString(R.string.password_must_contain_least_one_symbols_numbers)
        binding.textRequirements.setTextColor(ColorUtils.getColor(R.color.colorFF5050))
    }

    private fun emailError(str: String) {
        binding.textRequirements.text = str
        binding.textRequirements.setTextColor(ColorUtils.getColor(R.color.colorFF5050))
    }

    override fun getData() {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    // 数字,字母
    private val pattern = "^[0-9A-Za-z_]\$"

    // 英文标点
    private val patternEn = "^[`~|!@#\$%^&*()_\\-+=<>?:\"{},.\\\\/;'\\[\\]]\$"

    private val pattern1 = ".*[0-9!\"#$%&'()*+,\\-./:;<>=?@\\[\\]{}\\\\^_`~].*"
    override fun afterTextChanged(s: Editable?) {
        binding.sbGetCaptcha.isEnabled =
            if (type == CaptchaViewModel.SET_NEW_PASSWORD || type == CaptchaViewModel.UPDATE_PASSWORD || type == CaptchaViewModel.RETRIEVE_PASSWORD)
                (binding.editPasswd.text?.length
                    ?: 0) >= 8 else RegexUtils.isEmail(binding.editPasswd.text)
    }
}