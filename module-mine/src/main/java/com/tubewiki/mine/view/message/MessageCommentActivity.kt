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
import com.tubewiki.mine.adapter.CommentMessageAdapter
import com.tubewiki.mine.bean.CommentMessageData
import com.tubewiki.mine.databinding.ActivityMessageCommentBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 评论消息
 */
@Route(path = "/mine/message/comment")
class MessageCommentActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityMessageCommentBinding>(),
    OnRefreshLoadMoreListener {

    private val adapter by lazy { CommentMessageAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_comment))
        initStateLayout(binding.stateLayout)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        binding.recyclerView.init(adapter)

        viewModel.getCommentMessage(false)

        adapter.setOnItemClickListener { adapter, view, position ->
            val comment = adapter.data.get(position) as CommentMessageData.Comment

            if (comment.type == 3) {
                //问答
                ARouter.getInstance().build("/video/details")
                    .withInt(TagConstant.VIDEO_ID, comment.data.id)
                    .withInt(TagConstant.USER_UID, comment.data.uid)
                    .withBoolean(TagConstant.SHOW_COMMENT, true)
                    .withInt(
                        TagConstant.NEED_TOP_ID,
                        if (comment.topAnswerId != 0 && comment.data.id != comment.topAnswerId) comment.topAnswerId else comment.answerId
                    )
                    .navigation()
            } else if (comment.type == 2) {
                //问答
                ARouter.getInstance().build("/question/activity/answer_detail")
                    .withInt(TagConstant.QUESTION_ID, comment.data.questionId)
                    .withInt(TagConstant.ANSWER_ID, comment.data.answer.answerId)
                    .withBoolean(TagConstant.SHOW_COMMENT, true)
                    .withInt(
                        TagConstant.NEED_TOP_ID,
                        if (comment.replyId != 0 && comment.replyId != comment.topAnswerId && comment.replyId != comment.answerId) comment.replyId else comment.answerId
                    )
                    .navigation()
            } else if (comment.type == 1) {
                //文章
                ArouterUtils.toArticleDetailsActivityWithComment(
                    comment.data.id,
                    if (comment.topAnswerId != 0 && comment.data.id != comment.topAnswerId) comment.topAnswerId else comment.answerId
                )

            }

        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            val comment = adapter.data.get(position) as CommentMessageData.Comment
            ARouter.getInstance().build("/mine/message/personal_page")
                .withInt(TagConstant.USER_CANCEL, comment.user.isCancel)
                .withInt(TagConstant.PARAMS, comment.user.uid).navigation()
        }

    }

    private var isFirst = true
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
        viewModel.commentMessageDataResult.observe(this) {
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
        viewModel.getCommentMessage(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getCommentMessage(false)

    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getCommentMessage(true)
    }
}