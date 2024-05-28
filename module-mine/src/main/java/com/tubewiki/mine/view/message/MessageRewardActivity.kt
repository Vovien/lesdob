package com.tubewiki.mine.view.message

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.RewardMessageAdapter
import com.tubewiki.mine.bean.RewardMessageData
import com.tubewiki.mine.databinding.ActivityMessageRewardBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 打赏评论
 */
@Route(path = "/mine/message/reward")
class MessageRewardActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityMessageRewardBinding>(),
    OnRefreshLoadMoreListener {

    private var isFirst = true

    private val adapter by lazy { RewardMessageAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.exceptional))
        initStateLayout(binding.stateLayout)
        //showLoading()
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        binding.recyclerView.init(adapter)

        viewModel.getRewardMessage(false)

        adapter.setOnItemClickListener { adapter, view, position ->
            val reward = adapter.data[position] as RewardMessageData.Reward
            if (reward.cateType == 3) {
                //问答
                ARouter.getInstance().build("/question/activity/answer_detail")
                    .withInt(TagConstant.QUESTION_ID, reward.data.questionId)
                    .withInt(TagConstant.ANSWER_ID, reward.data.answerId)
                    .navigation()
            } else if (reward.cateType == 1) {
                //文章
                ArouterUtils.toArticleDetailsActivity(reward.data.id)

            }
        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            val reward = adapter.data.get(position) as RewardMessageData.Reward
            if (view.id == R.id.tv_wallet) {
                ARouter.getInstance().build("/mine/wallet").navigation()
            } else if (view.id == R.id.tv_name || view.id == R.id.iv_avatar) {

                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt(TagConstant.USER_CANCEL,reward.user.isCancel)
                    .withInt(TagConstant.PARAMS, reward.user.uid).navigation()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (!isFirst) {
            adapter.isAllRead = true
            adapter.notifyDataSetChanged()
        }
        isFirst = false
    }

    override fun initData() {
    }

    override fun getData() {
        viewModel.rewardMessageDataResult.observe(this) {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data1.isNotNullEmpty()) {
                if (it.data2) {
                    adapter.setNewInstance(it.data1)

                } else {
                    adapter.addData(it.data1)
                }
            }
            binding.smartRefresh.finish()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getRewardMessage(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getRewardMessage(false)

    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getRewardMessage(true)
    }
}