package com.tubewiki.mine.view.message

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.MutuallyMessageAdapter
import com.tubewiki.mine.bean.MutuallyMessageData
import com.tubewiki.mine.databinding.FragmentMutuallyMessageBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/fragment/message/mutually")
class MutuallyMessageFragment :
    ViewModelFragment<MessageCenterViewModel, FragmentMutuallyMessageBinding>(),
    OnRefreshLoadMoreListener {
    private val adapter by lazy { MutuallyMessageAdapter() }

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "all"

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initStateLayout(binding.stateLayout)
        showContentState()
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        binding.recyclerView.init(adapter)

        viewModel.getMutuallyMessage(type, false)

        adapter.setOnItemClickListener { adapter, view, position ->
            val comment = adapter.data.get(position) as MutuallyMessageData.Message
            if (comment.data.answer != null && comment.data.answer.answerId != 0) {
                //1 文章 2问题 3 视频
                if (comment.data.answer.type == 3) {
                    //视频
                    ARouter.getInstance().build("/video/details")
                        .withInt(TagConstant.VIDEO_ID, comment.data.id)
                        .withInt(TagConstant.USER_UID, comment.uid)
                        .withBoolean(TagConstant.SHOW_COMMENT, true)
                        .withInt(TagConstant.NEED_TOP_ID,
                            if (comment.data.comment?.topAnswerId != 0) comment.data.comment?.topAnswerId
                                ?: 0 else comment.data.answer.answerId ?: 0)
                        .navigation()

                } else if (comment.data.answer.type == 2) {
                    //判断是不是问题回答的评论，如果是跳转回答显示评论，不是就只跳转回答
                    ARouter.getInstance().build("/question/activity/answer_detail")
                        .withInt(TagConstant.QUESTION_ID, comment.data.questionId)
                        .withInt(TagConstant.ANSWER_ID, comment.data.answer.answerId)
                        .withBoolean(
                            TagConstant.SHOW_COMMENT,
                            comment.data.comment != null && comment.data.comment?.answerId != 0
                        )
                        .withInt(TagConstant.NEED_TOP_ID,
                            if (comment.data.comment?.topAnswerId != 0 && comment.data.comment?.topAnswerId != comment.data.answer.answerId) comment.data.comment?.topAnswerId
                                ?: 0 else comment.data.comment?.answerId ?: 0)
                        .navigation()

                } else {
                    ArouterUtils.toArticleDetailsActivityWithComment(
                        comment.data.id,
                        if (comment.data.comment?.topAnswerId != 0) comment.data.comment?.topAnswerId
                            ?: 0 else comment.data.comment?.answerId ?: 0
                    )

                }
            } else {
                //判断是不是文章的评论
                if (!TextUtils.isEmpty(comment.data.title)) {
                    if (comment.dataType == "video") {
                        //视频
                        ARouter.getInstance().build("/video/details")
                            .withInt(TagConstant.VIDEO_ID, comment.data.id)
                            .withInt(TagConstant.USER_UID, comment.uid)
                            .navigation()
                    } else {
                        ArouterUtils.toArticleDetailsActivityWithComment(
                            comment.data.id,
                            comment.data.comment?.answerId?:0
                        )


                    }
                } else {
                    ARouter.getInstance().build("/question/activity/ask_detail")
                        .withInt(TagConstant.QUESTION_ID, comment.data.questionId)
                        .navigation()
                }
            }

        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            val comment = adapter.data.get(position) as MutuallyMessageData.Message
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


    override fun getData() {
        super.getData()
        viewModel.mutuallyMessageDataResult.observe(this) {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                adapter.setNewInstance(it.data1)

            } else {
                adapter.addData(it.data1)
            }

        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        viewModel.getMutuallyMessage(type, true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        viewModel.getMutuallyMessage(type, false)
    }

    override fun showContentState() {
        super.showContentState()
        binding.smartRefresh.finish()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getMutuallyMessage(type, true)

    }
}