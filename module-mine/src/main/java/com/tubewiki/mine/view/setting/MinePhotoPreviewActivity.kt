package com.tubewiki.mine.view.setting

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityMinePhotoPrevicewBinding
import com.tubewiki.mine.view.model.SettingViewModel
import com.jmbon.widget.picture.SelectPhotoUtil
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


@Route(path = "/mine/photo/preview")
class MinePhotoPreviewActivity :
    ViewModelActivity<SettingViewModel, ActivityMinePhotoPrevicewBinding>() {

    @Autowired
    @JvmField
    var path: String = ""

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var isEdit: Boolean = true

    @Autowired
    @JvmField
    var type: Int = 0


    var avatarFile = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        StatusBarCompat.setNavigationBarColor(this.window, Color.BLACK)
        StatusBarCompat.setTransparentStatusBarAndFullScreen(this.window)
    }

    override fun initView(savedInstanceState: Bundle?) {
//        binding.previewImage.isZoomable = false
        binding.changePhoto.visibility = if (isEdit) View.VISIBLE else View.GONE

        binding.changePhoto.text =
            if (type == USER_ACCOUNT_PICTURES) StringUtils.getString(R.string.change_your_photo) else getString(
                R.string.change_background
            )
        // 压缩过的gif就不是gif了
        binding.changePhoto.setOnClickListener {
            SelectPhotoUtils.selectSinglePic(
                SelectPhotoUtil.create(this), true, object :
                    OnResultCallbackListener<LocalMedia> {
                    override fun onResult(list: MutableList<LocalMedia>?) {
                        if (list.isNotNullEmpty()) {
                            //binding.previewImage.load(list!![0].cutPath)

                            setImage(list!![0].cutPath)

                            // 设置新的图片
                            val intent = Intent()
                            intent.putExtra(TagConstant.IMAGE_PATH,
                                list[0].compressPath.ifEmpty { list[0].path })
                            intent.putExtra(TagConstant.TYPE, type)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
//                            started {
//                                viewModel.uploadImage(list[0]).netCatch {
//                                    message.showToast()
//                                }.next {
//                                    when (type) {
//                                        USER_ACCOUNT_PICTURES -> {
//                                            avatarFile = this
//                                            viewModel.uploadInfo("avatar_file", avatarFile)
//                                        }
//                                        else -> {
//                                            viewModel.setBackground(this)
//                                        }
//                                    }
//                                }
//                            }
                        }
                    }

                    override fun onCancel() {
                    }

                },
                if (type == USER_ACCOUNT_PICTURES) 1 else 3,
                if (type == USER_ACCOUNT_PICTURES) 1 else 2
            )
        }

        binding.root.setOnClickListener {
            finishAfterTransition()
        }
        binding.previewImage.setOnClickListener {
            finishAfterTransition()
        }


    }

    override fun initData() {
        started {

            setImage(path)
        }
        //  binding.previewImage.loadNoPlace(path)
//        Glide.with(Utils.getApp())
//            .load(path)
//            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate())
//            .into(binding.previewImage)

    }

    private fun setImage(path: String) {
        GlideUtil.getInstance()
            .loadUrlSimpleTarget(path, object : GlideUtil.GlideUtilSimpleTarget {
                override fun onResourceReady(resource: Drawable?) {

                    binding.previewImage.setImageDrawable(
                        zoomDrawable(
                            resource!!,
                            ScreenUtils.getScreenWidth(),
                            ScreenUtils.getScreenHeight()
                        )
                    )
                }

                override fun onLoadFailed() {

                }
            })
    }


    override fun getData() {
        viewModel.uploadInfoStatus.observe(this) {
            if (it) {
                when (type) {
                    USER_ACCOUNT_PICTURES -> {
                        val info = Constant.userInfo
                        info.avatarFile = avatarFile
                        Constant.updateInfo(info)
                        getString(R.string.with_head_is_in_success).showToast()
                    }
                    else -> {
                        getString(R.string.change_background_successfully).showToast()
                    }
                }
                finish()
            }
        }
    }

    companion object {
        // 用户账号图片
        const val USER_ACCOUNT_PICTURES = 1
    }

    private fun zoomDrawable(drawable: Drawable, w: Int, h: Int): Drawable? {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val oldbmp = drawableToBitmap(drawable)
        val matrix = Matrix()
        var scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height

        scaleWidth = scaleHeight.coerceAtMost(scaleWidth)

        matrix.postScale(scaleWidth, scaleWidth)
        val newbmp = Bitmap.createBitmap(
            oldbmp, 0, 0, width, height,
            matrix, true
        )
        return BitmapDrawable(null, newbmp)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(width, height, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }
}