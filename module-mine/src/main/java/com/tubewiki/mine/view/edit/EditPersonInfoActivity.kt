package com.tubewiki.mine.view.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.activity.PhotoPreviewActivity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.loadCircle
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityEditPersoninfoLayoutBinding
import com.tubewiki.mine.view.model.EditPersonInfoViewModel
import com.tubewiki.mine.view.model.SetNewImageViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import com.tubewiki.mine.view.setting.MinePhotoPreviewActivity
import com.jmbon.widget.picture.SelectPhotoUtil
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

@Route(path = "/mine/edit/info")
class EditPersonInfoActivity :
    ViewModelActivity<EditPersonInfoViewModel, ActivityEditPersoninfoLayoutBinding>() {
    private val imageViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(SetNewImageViewModel::class.java)
    }
    private val settingViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(SettingViewModel::class.java)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.edit_profile))
        initClick()
    }

    private fun initClick() {


        binding.stNickname.setOnClickListener {
            ARouter.getInstance().build("/mine/edit/name").navigation()
        }
        binding.stDisplay.setOnClickListener {
            ARouter.getInstance().build("/mine/edit/gender").navigation()
        }
        binding.stDescription.setOnClickListener {
            ARouter.getInstance().build("/mine/edit/description").navigation()
        }
        binding.qmLayoutAvatar.setOnClickListener {
            SelectPhotoUtils.selectSinglePic(
                SelectPhotoUtil.create(this@EditPersonInfoActivity), true, object :
                    OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        if (result.isNotNullEmpty()) {
                            binding.imageUserAvatar.load(result?.get(0)?.compressPath)
                        }
                    }

                    override fun onCancel() {
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun initData() {
        binding.stDescription.rightTextView.setSingleLine()
        binding.stDescription.rightTextView.ellipsize = TextUtils.TruncateAt.END

        binding.stNickname.rightTextView.isSingleLine = true
        binding.stNickname.rightTextView.ellipsize = TextUtils.TruncateAt.END
        setData()

    }

    private fun setData() {
        Constant.userInfo.apply {
            binding.imageUserAvatar.loadCircle(this.avatarFile)
            binding.stDisplay.setRightString(
                if (this.sexShow == 1) getString(R.string.not_displayed) else getString(
                    R.string.display
                )
            )
            val sex = if (sex == 1) "男" else "女"
            binding.stDisplay.setLeftString("性别（$sex）")
            binding.stNickname.setRightString(this.userName)
            binding.stDescription.setRightTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            if (description.isNotNullEmpty()) {
                binding.stDescription.setRightString(description)
            } else {
                binding.stDescription.setRightString("向别人介绍下你自己吧")
            }
            binding.imageUserAvatar.setOnClickListener {
                startActivity(
                    ARouter.getInstance().build("/mine/photo/preview")
                        .withString("path", this.avatarFile)
                        .withInt("type", PhotoPreviewActivity.USER_ACCOUNT_PICTURES)
                )


            }
            binding.imageUserAvatar.setBackgroundColor(Color.parseColor("#80000000"))
        }
    }

    private fun startActivity(postcard: Postcard) {
        LogisticsCenter.completion(postcard)
        val intent = Intent(this, postcard.destination)
        intent.putExtras(postcard.extras)
        startActivityForResult(intent, TagConstant.UPDATE_PICTURE_RESULT)
    }

    override fun getData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                TagConstant.UPDATE_PICTURE_RESULT -> {
                    imageViewModel.processingImages(
                        data,
                        lifecycleScope,
                        settingViewModel
                    ) { type, url ->
                        if (type == MinePhotoPreviewActivity.USER_ACCOUNT_PICTURES)
                            binding.imageUserAvatar.loadCircle(url)
                        else Constant.userInfo.background = url
                    }
                }
            }
        }
    }
}