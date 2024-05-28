package com.jmbon.middleware.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity

import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.jmbon.middleware.databinding.ActivityPreviewPhotoLayoutBinding

import com.jmbon.middleware.utils.loadNoPlace


/**
 * 预览图片view界面
 */
@Route(path = "/middleware/photo/preview")
class PhotoPreviewActivity :
    AppBaseActivity<ActivityPreviewPhotoLayoutBinding>() {

    @Autowired
    @JvmField
    var path: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        StatusBarCompat.setNavigationBarColor(this.window, Color.BLACK)
        StatusBarCompat.setTransparentStatusBarAndFullScreen(this.window)
    }

    override fun initView(savedInstanceState: Bundle?) {


        binding.root.setOnClickListener {
            finishAfterTransition()
        }
        binding.previewImage.setOnClickListener {
            finishAfterTransition()
        }

        binding.previewImage.isZoomable = false
    }

    override fun initData() {
        Looper.myQueue().addIdleHandler {
            binding.previewImage.loadNoPlace(path)
            return@addIdleHandler false
        }
    }


    override fun getData() {

    }

    companion object {
        const val USER_ACCOUNT_PICTURES = 1
    }
}