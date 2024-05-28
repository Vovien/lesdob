package com.tubewiki.mine.view.message

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AnswerMessageAdapter
import com.tubewiki.mine.bean.AnswerMessageData
import com.tubewiki.mine.databinding.ActivityMessageAnswerBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 回答消息
 */
@Route(path = "/mine/message/answer")
class MessageAnswerActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityMessageAnswerBinding>(),
    OnRefreshLoadMoreListener {
    private var isFirst = true

    private val adapter by lazy { AnswerMessageAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.answer))
        initStateLayout(binding.stateLayout)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)


        binding.recyclerView.init(adapter)

        viewModel.getAnswerMessage(false)

        adapter.setOnItemChildClickListener { adapter, view, position ->

            val answer = adapter.data[position] as AnswerMessageData.Answer

            when (view.id) {
                R.id.tv_name, R.id.iv_avatar ->
                    ARouter.getInstance().build("/mine/message/personal_page")
                        .withInt(TagConstant.USER_CANCEL,answer.user.isCancel)
                        .withInt(TagConstant.PARAMS, answer.user.uid).navigation()
                R.id.tv_answer ->
                    ARouter.getInstance().build("/question/activity/answer_detail")
                        .withInt(TagConstant.QUESTION_ID, answer.questionId)
                        .withInt(TagConstant.ANSWER_ID, answer.answerId)
                        .navigation()
                R.id.ll_title -> ARouter.getInstance().build("/question/activity/ask_detail")
                    .withInt(TagConstant.QUESTION_ID, answer.questionId)
                    .navigation()
            }
        }

    }

    override fun initData() {
    }

    override fun getData() {
        viewModel.answerMessageDataResult.observe(this) {
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

    override fun onResume() {
        super.onResume()

        if (!isFirst) {
            adapter.isAllRead = true
            adapter.notifyDataSetChanged()
        }
        isFirst = false
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getAnswerMessage(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getAnswerMessage(false)

    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getAnswerMessage(true)
    }
}