package com.jmbon.middleware.activity

import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.jmbon.middleware.R
import com.jmbon.middleware.adapter.ShareCircleAdapter
import com.jmbon.middleware.bean.ShareCircleBean
import com.jmbon.middleware.bean.event.GroupShareEvent
import com.jmbon.middleware.databinding.ActivityShareToCircleBinding
import com.jmbon.middleware.dialog.ShareToCircleDialog
import com.jmbon.middleware.model.ShareToCircleViewModel
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 分享到圈子的圈子列表activity
 */
@Route(path = "/middleware/circle/share_to_circle")
class ShareToCircleActivity :
    ViewModelActivity<ShareToCircleViewModel, ActivityShareToCircleBinding>(), OnLoadMoreListener {


    @Autowired(name = "itemId")
    @JvmField
    var itemId = 0

    @Autowired(name = "answerId")
    @JvmField
    var answerId = 0

    @Autowired(name = "title")
    @JvmField
    var title = ""

    @Autowired(name = "type")
    @JvmField
    var type = ""


    private val circleAdapter by lazy { ShareCircleAdapter() }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

        EventBus.getDefault().register(this)

    }

    override fun initView(savedInstanceState: Bundle?) {

        initPageState(binding.stateLayout)
        binding.stateLayout.retry = { refreshDataWhenError() }

        setTitleName("分享到圈子")
        titleBarView.leftImageButton.setImageResource(R.drawable.icon_nav_close)
        binding.smartRefresh.setOnLoadMoreListener(this)

        binding.recyclerView.init(circleAdapter)

        circleAdapter.setOnItemClickListener { adapter, view, position ->
            val circle = circleAdapter.data[position]
            showShareDialog(circle, itemId, title)
        }

    }


    private fun showShareDialog(
        circle: ShareCircleBean.Circle,
        id: Int,
        title: String
    ) {
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(true)
            .isDestroyOnDismiss(true)
            .shadowBgColor(Color.parseColor("#66000000"))
            .asCustom(
                ShareToCircleDialog(
                    this, circle,
                    id,
                    title, type
                ) { type ->

                    doShareAction(circle, type, id)

                }
            )
            .show()
    }

    private fun doShareAction(
        circle: ShareCircleBean.Circle,
        type: Int,
        id: Int
    ) {
        started {
            viewModel.circleShare(circle.id, type, id, answerId)
                .next {
                    "分享成功".showToast()
                    //跳转到圈子
                    ARouter.getInstance().build("/chat/circle/circle_detail")
                        .withString("groupName", circle.name)
                        .withString("circleId", "${circle.id}")
                        .withString("groupId", "${circle.groupId}")
                        .withString("groupNumber", "${circle.number}")
                        .navigation()
                    finish()
                }
        }
    }

    override fun initData() {

    }


    /**
     * 长按头像操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun groupShareEvent(event: GroupShareEvent) {
        circleId = event.groupId
    }

    var circleId = ""

    override fun getData() {
        getCircleList(circleId, true)
    }

    var page = 1
    private fun getCircleList(circleId: String, isRefresh: Boolean) {

        if (isRefresh) {
            page = 1
        }

        started {
            viewModel.getCircleShareList(circleId, page).next {
                showContentState()
                if (pageCount <= page) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                }
                //过滤圈子

                if (isRefresh) {
                    if (circles.isNullOrEmpty()) {
                        binding.tvEmptyTips.visible()
                    } else {
                        binding.tvEmptyTips.gone()
                    }

                    var data = circles.filter {
                        "${it.id}" == circleId
                    }
                    if (data.isNotNullEmpty()) {
                        //circleAdapter.setNewInstance(circles)
                        circleAdapter.setNewInstance(data.toMutableList())
                    } else {
                        circleAdapter.setNewInstance(circles)
                    }

                } else {
                    circleAdapter.addData(circles)
                }
                page++
                binding.smartRefresh.finish()

            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getCircleList(circleId, false)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getCircleList(circleId, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }
}