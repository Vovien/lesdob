package com.tubewiki.mine.view.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityEditUpdateNameLayoutBinding
import com.tubewiki.mine.view.model.EditPersonInfoViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import com.jmbon.widget.GeneralDialog
import kotlinx.coroutines.flow.onCompletion

/**
 * 修改名字
 */
@Route(path = "/mine/edit/name")
class EditPersonNameActivity :
    ViewModelActivity<SettingViewModel, ActivityEditUpdateNameLayoutBinding>() {

    private val editView by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory<Any, Any?>()
        ).get(EditPersonInfoViewModel::class.java)
    }
   // private val userName by lazy { Constant.userInfo.userName.take(10) }
    private val userName by lazy { Constant.userInfo.userName }
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.modify_name))
        setRightTextMenu(getString(R.string.save))
       // initPageState()

        titleBarView.rightTextView.setTextColor(resources.getColor(R.color.color_dis_enable))
        titleBarView.rightTextView.isEnabled = false
        binding.inputName.addTextChangedListener(editView.textWatch)
        binding.inputName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputName.setBackgroundResource(R.drawable.gray_corner_bg_shape)
            }
        }
        if (Constant.userInfo.verifyType == 2) {
            binding.textUpdateCount.gone()
            binding.inputName.filters =
                arrayOf<InputFilter>(InputFilter.LengthFilter(Int.MAX_VALUE))
            binding.inputName.setText(Constant.userInfo.userName)
        } else {
          //  binding.inputName.filters = arrayOf(InputFilter.LengthFilter(10))
            binding.inputName.setText(userName)
        }

        titleBarView.rightTextView.setOnClickListener {
            setInfo()
        }

    }

    val maxSize = 10
    val minSize = 2

    @SuppressLint("SetTextI18n")
    override fun initData() {
        editView.textSize.observe(this@EditPersonNameActivity) {
//            binding.textSize.text = "$it/10"
//            binding.tipText.visibility = if (it >= maxSize) {
//                View.VISIBLE
//            } else View.INVISIBLE

            //大于等于2小于等于10
            if (it >= minSize && userName != binding.inputName.text.toString()
                    .trim()
            ) {
                titleBarView.rightTextView.setTextColor(
                    ContextCompat.getColor(
                        this@EditPersonNameActivity,
                        R.color.color_currency
                    )
                )
                titleBarView.rightTextView.isEnabled = true
            } else {
                titleBarView.rightTextView.isEnabled = false
                titleBarView.rightTextView.setTextColor(
                    ContextCompat.getColor(
                        this@EditPersonNameActivity,
                        R.color.color_dis_enable
                    )
                )
            }

        }

    }

    override fun getData() {
//        started {
//            viewModel.getNickCount().onCompletion {
//                showContentState()
//            }.next {
//                binding.textUpdateCount.text =
//                    StringUtils.getString(
//                        R.string.nickname_for_only_allow_to_modify_2_times_in_year,
//                        2 - this.count
//                    )
//
//                //医生也不可修改昵称
//                // if (this.count == 2 || this.is_doctor) {
//                if (this.is_doctor) {
//                    titleBarView.rightTextView.isEnabled = false
//                    binding.inputName.isEnabled = false
//                    binding.inputName.setBackgroundResource(R.drawable.shape_input_limit_use)
//                    binding.inputLayout.gone()
//                }
//            }
//        }
        viewModel.uploadInfoStatus.observe(this) {
            if (it) {
                val info = Constant.userInfo
                info.userName = binding.inputName.text.toString()
                Constant.updateInfo(info)
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        if (binding.inputName.text.isNullOrBlank() || userName == binding.inputName.text.toString()
            || binding.inputName.text.toString().length < 2 || Constant.userInfo.verifyType == 2
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
        if (binding.inputName.text.isNotNullEmpty() && userName != binding.inputName.text.toString()) {
            var infoMap = hashMapOf<String, String>()
            infoMap["nickname"] = binding.inputName.text.toString()
            viewModel.uploadInfo(infoMap)
        } else {
            finish()
        }
    }
}