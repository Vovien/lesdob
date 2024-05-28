package com.tubewiki.mine.view.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityEditUpdateDescriptionLayoutBinding
import com.tubewiki.mine.view.model.EditPersonInfoViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import com.jmbon.widget.GeneralDialog


/**
 * 修改简介
 */
@Route(path = "/mine/edit/description")
class EditPersonDescriptionActivity :
    ViewModelActivity<SettingViewModel, ActivityEditUpdateDescriptionLayoutBinding>() {


    private val editView by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory<Any, Any?>()
        ).get(EditPersonInfoViewModel::class.java)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.description))

        //医生不能修改简介和不限制简介字数
        if (Constant.userInfo.verifyType != 2) {
            setRightTextMenu(getString(R.string.save))
            titleBarView.rightTextView.setTextColor(
                ContextCompat.getColor(
                    this@EditPersonDescriptionActivity,
                    R.color.color_currency
                )
            )

            titleBarView.rightTextView.setOnClickListener {
                if (TextUtils.isEmpty(binding.inputIntroduce.text) || binding.inputIntroduce.text.isNullOrBlank()) {
                    binding.inputIntroduce.text?.clear()
                    R.string.person_edit_info_empty.showToast()
                    return@setOnClickListener
                }
                setInfo()
            }
        } else {
            binding.inputIntroduce.setBackgroundResource(R.drawable.unenable_edit_bg)
            binding.inputLayout.gone()
            binding.inputIntroduce.isEnabled = false
            binding.inputIntroduce.filters = arrayOf<InputFilter>(LengthFilter(10000))
        }

        binding.inputIntroduce.addTextChangedListener(editView.textWatch)
        binding.inputIntroduce.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputIntroduce.setBackgroundResource(R.drawable.gray_corner_bg_shape)
            }
        }

        if (Constant.userInfo.description.isNotNullEmpty()) {
            binding.inputIntroduce.setText(Constant.userInfo.description)
        }

        titleBarView.rightTextView.isEnabled = false
        titleBarView.rightTextView.setTextColor(
            ContextCompat.getColor(
                this@EditPersonDescriptionActivity,
                R.color.color_dis_enable
            )
        )


    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        editView.apply {
            textSize.observe(this@EditPersonDescriptionActivity) {
                binding.textSize.text = "$it/45"
                binding.tipText.visibility = if (it < 45) {
                    View.INVISIBLE
                } else View.VISIBLE


                if (it > 0 && Constant.userInfo.description != binding.inputIntroduce.text.toString()) {
                    titleBarView.rightTextView.isEnabled = true
                    titleBarView.rightTextView.setTextColor(
                        ContextCompat.getColor(
                            this@EditPersonDescriptionActivity,
                            R.color.color_currency
                        )
                    )
                } else {
                    titleBarView.rightTextView.isEnabled = false
                    titleBarView.rightTextView.setTextColor(
                        ContextCompat.getColor(
                            this@EditPersonDescriptionActivity,
                            R.color.color_dis_enable
                        )
                    )
                }
            }
        }
    }

    override fun getData() {
        viewModel.uploadInfoStatus.observe(this) {
            if (it) {
                val info = Constant.userInfo
                info.description = binding.inputIntroduce.text.toString()
                Constant.updateInfo(info)
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        if (Constant.userInfo.description == binding.inputIntroduce.text.toString() || binding.inputIntroduce.text.toString()
                .isNullOrEmpty()
        ) {
            super.onBackPressed()
        } else {
            GeneralDialog.showDialog(this,
                getString(R.string.going_home_whether_save_editor),
                "",
                getString(R.string.do_not_save),
                getString(R.string.save),
                { setInfo() },
                { super.onBackPressed() })
        }


    }

    private fun setInfo() {
        if (Constant.userInfo.description != binding.inputIntroduce.text.toString()) {
            var infoMap = hashMapOf<String, String>()
            infoMap["description"] = binding.inputIntroduce.text.toString()
            viewModel.uploadInfo(infoMap)
        } else {
            onBackPressed()
        }
    }
}