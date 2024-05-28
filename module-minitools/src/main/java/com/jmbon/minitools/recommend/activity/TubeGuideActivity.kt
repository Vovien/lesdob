package com.jmbon.minitools.recommend.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.glide.GlideApp
import com.apkdv.mvvmfast.ktx.launch
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.navigationWithFinish
import com.jmbon.middleware.utils.parseColor
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.ActivityTubeGuideLayoutBinding
import com.jmbon.minitools.recommend.bean.GuideButtonStyleItemBean
import com.jmbon.minitools.recommend.router.RecommendRouter
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/******************************************************************************
 * Description: 我要怀孕/我要试管引导页
 *
 * Author: jhg
 *
 * Date: 2023/9/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = RecommendRouter.MINI_TOOL_TUBE_GUIDE)
class TubeGuideActivity : ViewModelActivity<ChooseViewModel, ActivityTubeGuideLayoutBinding>() {

    @Autowired
    @JvmField
    var guidePictureId: Int = 0

    @Autowired
    @JvmField
    var guidePictureUrl: String? = null

    @Autowired
    @JvmField
    var buttonStyleList: ArrayList<GuideButtonStyleItemBean>? = null

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .transparentNavigationBar()
            .init()
        if (guidePictureUrl.isNullOrBlank() && buttonStyleList.isNullOrEmpty()) {
            // 如果是从推送跳转, 则需要重新请求数据
            initPageState()
            viewModel.getGuideInfo(guidePictureId)
        } else {
            realInitView()
        }

        binding.ivClose.onClick {
            ARouter.getInstance().build("/main/main").navigationWithFinish(this)
        }
        binding.tvLeft.onClick {
            BannerHelper.onClick(
                CommonBanner(
                    item_type = buttonStyleList!![0].item_type,
                    identity = buttonStyleList!![0].identity
                )
            )
        }
        binding.tvRight.onClick {
            BannerHelper.onClick(
                CommonBanner(
                    item_type = buttonStyleList!![1].item_type,
                    identity = buttonStyleList!![1].identity
                )
            )
        }
    }

    private fun realInitView(dataByRequest: Boolean = false) {
        if (dataByRequest) {
            showContentState()
        }
        launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    GlideApp.with(Utils.getApp())
                        .asBitmap()
                        .load(guidePictureUrl ?: "")
                        .apply(RequestOptions().timeout(3000))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit()
                        .get()
                }.getOrNull()
            }.apply {
                if (this == null || this.isRecycled) {
                    return@launch
                }

                val hScale = this.width * 1.0f / ScreenUtils.getScreenWidth()
                if (hScale > 0) {
                    if (this.height / hScale > ScreenUtils.getScreenHeight()) {
                        binding.ivContent.adjustViewBounds = true
                    } else {
                        binding.ivContent.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                } else {
                    binding.ivContent.scaleType = ImageView.ScaleType.CENTER_CROP
                }
                binding.ivContent.setImageBitmap(this)
            }
        }

        if (buttonStyleList.isNullOrEmpty()) {
            binding.llBottom.isVisible = false
            return
        }

        binding.tvLeft.apply {
            val buttonStyle = buttonStyleList!![0]
            text = buttonStyle.title
            setBackground(
                backgroundColor = parseColor(
                    buttonStyle.bg_color,
                    R.color.themePrimary.toColorInt()
                ),
                radius = 14.dp
            )
        }
        if (buttonStyleList!!.size > 1) {
            binding.tvRight.apply {
                val buttonStyle = buttonStyleList!![1]
                text = buttonStyle.title
                setBackground(
                    backgroundColor = parseColor(
                        buttonStyle.bg_color,
                        R.color.themePrimary.toColorInt()
                    ),
                    radius = 14.dp
                )
            }
        } else {
            binding.tvRight.isVisible = false
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.guideInfoLD.observe(this) {
            guidePictureUrl = it?.guide_page_img
            buttonStyleList = it?.guide_button_list
            realInitView(true)
        }
    }

    override fun onBackPressed() {
        ARouter.getInstance().build("/main/main").navigationWithFinish(this)
    }
}