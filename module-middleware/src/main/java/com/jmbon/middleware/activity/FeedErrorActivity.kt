package com.jmbon.middleware.activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.divider.GridSpacingItemDecoration
import com.jmbon.middleware.R
import com.jmbon.middleware.adapter.CertificateAdapter
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ActivityFeedErrorLayoutBinding
import com.jmbon.middleware.model.CheckErrorViewModel
import com.jmbon.middleware.model.UploadFileViewModel
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.widget.picture.SelectPhotoUtil
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


/**
 * 纠错页面
 */
@Route(path = "/middleware/activity/feed_error")
class FeedErrorActivity :
    ViewModelActivity<CheckErrorViewModel, ActivityFeedErrorLayoutBinding>() {

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var title: String = ""

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var type: Int = 1

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var contentType: Int = 1

    @Autowired(name = TagConstant.ITEM_ID)
    @JvmField
    var itemId: Int = 0

    private val updateFileViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory<Any, Any?>()
        ).get(UploadFileViewModel::class.java)
    }

    private val adapter by lazy {
        CertificateAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.check_content))


        binding.tvContent.hint = "详情描述下“${title}“内容中存在的问题吧(必填)"

        if (Constant.isLogin && Constant.userInfo.email.isNotNullEmpty()) {
            binding.tvEmail.text = Constant.userInfo.email
        }

        initRecyclerview()


        binding.rlEmail.setOnSingleClickListener({
            ARouter.getInstance().build("/middleware/activity/edit_email")
                .withString(TagConstant.PARAMS, binding.tvEmail.text.toString())
                .navigation(this, 99)
        })

        binding.tvSure.setOnClickListener {

            if (binding.tvContent.text.toString().isNullOrEmpty()) {
                "请添加详情描述".showToast()
                binding.tvSure.stateButton()
                return@setOnClickListener
            }
            if (binding.tvEmail.text.toString()
                    .isNullOrEmpty() && binding.tvEmail.text.toString() != "请填写"
            ) {
                "请添加联系邮箱".showToast()
                binding.tvSure.stateButton()
                return@setOnClickListener
            }
            //图片不是必传
            if (adapter.data.size < 2) {
                submitFeed(mutableListOf())
            } else {
                uploadFiles()
            }

        }
        initScrollHandler()

    }

    private fun initScrollHandler() {
        binding.tvContent.setOnTouchListener { v, event -> //canScrollVertically()方法为判断指定方向上是否可以滚动,参数为正数或负数,负数检查向上是否可以滚动,正数为检查向下是否可以滚动
            if (binding.tvContent.canScrollVertically(1) || binding.tvContent.canScrollVertically(
                    -1
                )
            ) {
                v?.parent
                    ?.requestDisallowInterceptTouchEvent(true)//requestDisallowInterceptTouchEvent();要求父类布局不在拦截触摸事件
                if (event?.action == MotionEvent.ACTION_UP) { //判断是否松开
                    v?.parent
                        ?.requestDisallowInterceptTouchEvent(false) //requestDisallowInterceptTouchEvent();让父类布局继续拦截触摸事件
                }
            }
            false
        }

    }

    private fun uploadFiles() {

        started {
            var data = mutableListOf<LocalMedia>()
            data.addAll(adapter.data)

            if (data.last().path.isNullOrEmpty() && data.last().compressPath.isNullOrEmpty()) {
                data.removeLast()
            }
            //上传图片
            updateFileViewModel.uploadFiles(this@FeedErrorActivity, data)
        }
    }

    private fun submitFeed(images: MutableList<String>) {

        started {
            var imagesUrl = ""
            images.forEachIndexed { index, s ->
                if (index == images.size - 1) {
                    imagesUrl += "$s"
                } else {
                    imagesUrl += "$s,"
                }
            }
            //上传图片
            viewModel.errorCorrection(
                type,
                imagesUrl,
                binding.tvContent.text.toString(),
                binding.tvEmail.text.toString(),
                contentType,
                itemId
            ).netCatch {
                message.showToast()
                binding.tvSure.stateButton()
            }.next {
                ARouter.getInstance().build("/middleware/tort/submit")
                    .withString(TagConstant.PARAMS, resources.getString(R.string.thanks_you_feed))
                    .navigation()
                finish()
            }
        }
    }

    private fun initRecyclerview() {

        adapter.addIconId = R.drawable.icon_feed_error_add_image
        val manager = gridLayout(this, 3, RecyclerView.VERTICAL, false)
        binding.rvImage.adapter = adapter
        binding.rvImage.layoutManager = manager
        binding.rvImage.addItemDecoration(GridSpacingItemDecoration(3, 4f.dp(), false))
        // 添加空数据
        adapter.addData(LocalMedia())
        adapter.setOnItemClickListener { ap, view, position ->
            if (position == adapter.data.size - 1) {
                val last = adapter.data.last()
                if (last.path.isNullOrEmpty()) {
                    SelectPhotoUtils.selectPic(
                        SelectPhotoUtil.create(this@FeedErrorActivity),
                        adapter.data.subList(0, adapter.data.size - 1),
                        9,
                        1,
                        object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(image: MutableList<LocalMedia>?) {
                                image?.let {
                                    // 新数据
                                    adapter.setList(it)
                                    if (it.size < 9)
                                        adapter.addData(LocalMedia())
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            var email = it.getStringExtra("email")
            if (email.isNotNullEmpty()) {
                binding.tvEmail.text = email
            }
        }
    }

    override fun initData() {

    }

    override fun getData() {

        updateFileViewModel.multiFileUploadResult.observe(this) {
            if (!it.isNullOrEmpty()) {
                submitFeed(it)
            } else {
                binding.tvSure.stateButton()
            }
        }

    }


}