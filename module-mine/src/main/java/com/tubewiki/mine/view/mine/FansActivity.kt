package com.tubewiki.mine.view.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.jmbon.middleware.bean.User
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.closeDefaultAnimator
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.FansAdapter
import com.tubewiki.mine.databinding.ActivityFansBinding
import com.tubewiki.mine.view.model.FollowViewModel
import com.tubewiki.mine.view.model.MineFragmentViewModel


/**
 * 粉丝列表
 */
@Route(path = "/mine/fans")
class FansActivity :
    ViewModelActivity<MineFragmentViewModel, ActivityFansBinding>(),
    OnRefreshLoadMoreListener {

    private val uid = Constant.getUserId()
    private val adapter by lazy { FansAdapter() }

    @Autowired(name = TagConstant.QUESTION_ID)
    @JvmField
    var questionId = 0


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

    }

    private val focusViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(FollowViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (questionId > 0) {
            titleBarView.setCenterView(
                LayoutInflater.from(this).inflate(R.layout.title_fans_center_layout, null)
            )
            adapter.type = 1
            //titleBarView.setCenterView2(R.layout.title_fans_center_layout)
        } else {
            setTitleName(getString(R.string.fans_list))

        }
        initPageState(binding.container)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)
        binding.recyclerView.init(adapter)
        loadData(true)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val fan = adapter.data.get(position) as User
            when (view.id) {
                R.id.sb_focus_on -> Action {
                    // focus或unfocus
                    //如果关注了或者相互关注就取消关注
                    focusViewModel.focusUser(
                        fan.uid,
                        !fan.isFocus,
                        position
                    )
                }.logInToIntercept()
            }
        }

        adapter.setOnItemClickListener { adapter, view, position ->
            val fan = adapter.data.get(position) as User
            ARouter.getInstance().build("/mine/message/personal_page")
                .withInt(TagConstant.USER_CANCEL, fan.isCancel)
                .withInt(TagConstant.PARAMS, fan.uid).navigation()
        }

        binding.recyclerView.closeDefaultAnimator()

    }

    override fun initData() {
    }

    override fun getData() {
        viewModel.fansUserResult.observe(this, {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (!it.data1.isNullOrEmpty()) {
                if (it.data2) {
                    adapter.setNewInstance(it.data1)

                } else {
                    adapter.addData(it.data1)
                }
            }
            binding.smartRefresh.finish()
        })

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadData(false)
    }

    private fun loadData(isRefresh: Boolean) {
        if (questionId > 0) {
            viewModel.getQuestionFans(questionId, isRefresh) {
                titleBarView.findViewById<TextView>(R.id.tv_num).text = "$it"
            }
        } else {
            viewModel.getUserFans(uid, false, isRefresh)
        }
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadData(true)
    }
}