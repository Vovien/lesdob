package com.tubewiki.mine.view.message

import android.os.Bundle
import android.view.MotionEvent
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityJmHandleReportLayoutBinding
import com.tubewiki.mine.view.model.FeedReportViewModel


/**
 * 处理反馈界面
 */
@Route(path = "/mine/message/handle_report")
class JmHandleReportActivity :
    ViewModelActivity<FeedReportViewModel, ActivityJmHandleReportLayoutBinding>() {
    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var id = 0

    var type = 1

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.hanlde_report_detail))

        SpanUtils.with(binding.tvTitle).append("* ")
            .setForegroundColor(resources.getColor(R.color.colorFF5050)).append("处理状态").create()

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_button1 -> type = 1
                R.id.radio_button2 -> type = 2
                R.id.radio_button3 -> type = 3
                R.id.radio_button4 -> type = 4
            }
        }


        binding.tvSure.setOnClickListener {
            submitHandle()
        }

        //监听软键盘
        KeyboardUtils.registerSoftInputChangedListener(window) {
            //软键盘弹出，顶起pop布局
            if (it > 200 && binding.edContent.lineCount < 3) {
                //偷个懒，不动态计算，保证滑动到底部
                binding.scroll.smoothScrollTo(0, 10000)
            } else {
                // binding.scroll.scrollBy(0, 0)
            }
        }

        //initScrollHandler()
    }

    private fun submitHandle() {

        started {
            viewModel.contentTortFeedback(type, binding.edContent.text.toString(), id).netCatch {
                message.showToast()
                binding.tvSure.stateButton()
            }.next {
                ARouter.getInstance().build("/question/tort/submit")
                    .navigation()
                finish()
            }

        }

    }


    override fun initData() {

    }

    override fun getData() {

    }

    private fun initScrollHandler() {
        binding.edContent.setOnTouchListener { v, event -> //canScrollVertically()方法为判断指定方向上是否可以滚动,参数为正数或负数,负数检查向上是否可以滚动,正数为检查向下是否可以滚动
            if (binding.edContent.canScrollVertically(1) || binding.edContent.canScrollVertically(
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

}