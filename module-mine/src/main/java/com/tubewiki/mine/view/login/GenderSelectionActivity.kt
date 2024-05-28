package com.tubewiki.mine.view.login

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.config.Constant
import com.jmbon.widget.WheelView
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityGenderSelectionBinding
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.model.UserInfoViewModel

/**
 * 首次登录选择性别
 */
@Route(path = "/mine/login/gender/selection")
class GenderSelectionActivity :
    ViewModelActivity<UserInfoViewModel, ActivityGenderSelectionBinding>() {

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }

    private var sex = 0
    private var age = ""
    private var isStepTwo = false
    private var isStepOne = true

    override fun initView(savedInstanceState: Bundle?) {
        registerUIChange(loginView)


        binding.sexWheel.apply {
            setItems(resources.getStringArray(R.array.sex_item).toMutableList())
            setSeletion(0)
            sex = 2// 默认选中女生
            onWheelViewListener = object : WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String) {

                    if (isStepTwo) {
                        age = item
                    } else {
                        if (selectedIndex == 0) {
                            sex = 2
                        } else {
                            sex = 1
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        binding.sbSelectOk.setOnClickListener {

            if (isStepOne) {
                selectedStep1()
            } else if (age.isNullOrEmpty()) {
                selectedStep2()
            } else {
                //提交资料
                submitInfo()
            }
            isStepOne = false
        }
    }

    private fun submitInfo() {

        started {
            viewModel.setAgeAndGender(sex, age).netCatch {
                message.showToast()
                binding.sbSelectOk.stateButton()
            }.next {
                Constant.userInfo.isFirstLogin = 0
                Constant.saveUser(Constant.user)

                ARouter.getInstance().build("/main/main")
                    .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .navigation(this@GenderSelectionActivity)

                finish()
            }
        }

    }


    fun selectedStep1() {
        binding.tvTitle.text = getString(R.string.user_info_title_info_step1)
        binding.tvTips.visible()
        binding.sexWheel.visible()
        binding.flWheel.visible()
        binding.tvTips.text = getString(R.string.please_selected_gender)
        binding.sbSelectOk.text = getString(R.string.next_step)
        binding.sbSelectOk.stateButton()
    }

    fun selectedStep2() {
        isStepTwo = true
        var data = arrayListOf<String>()
        for (value in 18..70) {
            data.add("$value")
        }

        binding.tvTitle.text = getString(R.string.user_info_title_info_step2)
        binding.tvTips.visible()
        binding.sexWheel.visible()
        binding.flWheel.visible()
        binding.tvTips.text = getString(R.string.please_selected_age)
        binding.sbSelectOk.text = getString(R.string.complete)
        binding.sbSelectOk.stateButton()

        binding.sexWheel.setItems(data)
        binding.sexWheel.setSeletionNoAmimal(12)
        age = "30"
    }


    override fun getData() {
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

}