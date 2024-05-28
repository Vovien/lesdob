package com.jmbon.middleware.comment

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseViewModelBottomDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isGone
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.SizeUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.comment.`interface`.SendStateListener
import com.jmbon.middleware.comment.adapter.ArticleCommentAdapter
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.comment.event.CleanTextEvent
import com.jmbon.middleware.comment.event.CommentEvent
import com.jmbon.middleware.comment.event.GivenEvent
import com.jmbon.middleware.comment.event.SubCommentEvent
import com.jmbon.middleware.comment.utils.ReplyUtils
import com.jmbon.middleware.comment.viewmodel.CommentViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.DialogReplyBottomPopupBinding
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupStatus
import com.lxj.xpopup.util.XPopupUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class CommentReplyDialogV2(activity: FragmentActivity) :
    BaseViewModelBottomDialog<CommentViewModel, DialogReplyBottomPopupBinding>(
        activity
    ) {

    var articleId: Int = 0
    var answerId: Int = 0
    var answerUid: Int = 0
    var publishUid: Int = 0
    var answerPositionInActivity: Int = 0
    var type: String = ""
    var lazyLoad = false

    fun init(
        articleId: Int,
        answerId: Int,
        answerUid: Int,
        publishUid: Int,
        answerPositionInActivity: Int,
        type: String,
        showQaCheckTips: Int = 0,
        needTopId: Int = 0,
        highlighted: Int = -1,
        lazyLoad: Boolean = false,
    ) {
        if (answerId == this.answerId && this.answerId != 0)
            return
        this.articleId = articleId
        this.answerId = answerId
        this.answerUid = answerUid
        this.publishUid = publishUid
        this.answerPositionInActivity = answerPositionInActivity
        this.type = type
        this.showQaCheckTips = showQaCheckTips
        this.needTopId = needTopId
        this.highlighted = highlighted
        commentAdapter.data.clear()
        commentAdapter.notifyDataSetChanged()
        thisPageUserCommentData.clear()
        refresh = true
        this.lazyLoad = lazyLoad
        sort = "default"
        if (!isFirst) {
            onViewInit()
            binding.stateLayout.showLoadingView()
        }

        editDialog.articleId = articleId
        editDialog.type = type
    }

    val offsetHeight = 52f.dp() //回答输入框高度
    var offsetScroll = 0 //是否已经滚动,已经滚动值大于0
    private val commentAdapter by lazy {

        ArticleCommentAdapter(type, {
            openMore(it)
        }, { position, recyclerView, view, event ->
            Action {
                if (showQaCheckTips == 2) {
                    //showQaCheckTips = 0
                    GeneralShowDialogUtils.showQaCheckDialog(activity)
                    return@Action
                }
                if (needShowLockTips && !Constant.isSpecialMember()) {
                    //needShowLockTips = false
                    GeneralShowDialogUtils.showLockCheckDialog(context, answerId != 0)
                    return@Action
                }


                replyBottomDialog.show()
                event.hashCode = editDialog.hashCode()
                ReplyUtils.showReplyInput(event)
                val rect = Rect()
                val array = IntArray(2)
                view.getLocalVisibleRect(rect)
                view.getLocationOnScreen(array)

                binding.recyclerView.postDelayed({
                    val bottomHeight =
                        ScreenUtils.getScreenHeight() - array[1] - rect.height() //当前view bottom 距离屏幕底部的距离
                    if (keyboardHeight + offsetHeight > bottomHeight) {//如果当前评论view在键盘高度加上输入框52dp之下就需要滚动

                        offsetScroll =
                            keyboardHeight - bottomHeight + offsetHeight + 12f.dp()  // 偏移12dp
                        binding.recyclerView.smoothScrollBy(0, offsetScroll)
                    }
                }, 300)
            }.logInToIntercept()


        })
    }

    private val mTitles = arrayOf("默认", "时间")

    // 刷新
    private var refresh = false

    // 排序
    private var sort = "default"

    // 需要显示输入框
    var needShowEdit = false

    // 需要显示锁定提示
    var needShowLockTips = false

    // 需要显示审核禁止交互提示
    var showQaCheckTips = 0

    var needTopId: Int = 0


    // 用户在当前页面的回复数据。每次要放到最前
    val thisPageUserCommentData by lazy { LinkedList<CommentEvent>() }

    // 需要高亮显示的部分
    var highlighted = -1

    // 需要高亮显示的部分
    var highlightedId = -1

    // 头部评论
    var headComment = mutableListOf<CommentList.Comment>()

    private val editDialog by lazy {
        CommentEditTextDialog(articleId, type, true, activity)
    }

    private val replyBottomDialog by lazy {
        XPopup.Builder(context)
            .autoOpenSoftInput(true) //                        .hasShadowBg(false)
            .moveUpToKeyboard(true)
            .asCustom(editDialog)
    }
    private var isFirst = true
    override fun onCreate() {
        super.onCreate()
        isFirst = false
        EventBus.getDefault().register(this)
        initAdapter()
        initRefresh()
        KeyboardUtils.registerSoftInputChangedListener(activity.window) {
            if (it > 200) {
                keyboardHeight = it
                viewModel.keyboardHeight.postValue(it)
                binding.recyclerView.setPadding(0, 0, 0, it)
            } else {
                binding.recyclerView.setPadding(0, 0, 0, 0)

                if (abs(offsetScroll) > 0) {
                    binding.recyclerView.smoothScrollBy(0, -offsetScroll)
                    offsetScroll = 0
                }
            }
        }
        onViewInit()
    }

    private fun onViewInit() {
        if (needTopId != 0) {
            highlighted = 0
        }
        binding.apply {
            orderLayout.initTabView(mTitles.asList())
            imageDown.setOnClickListener {
                dismiss()
            }
            textCommentsSize.gone()

            if (Constant.isLogin) {
                imageHead.loadCircle(Constant.userInfo.avatarFile)
            } else {
                imageHead.loadCircle(R.drawable.icon_default_login_avatar)
            }
            inputLayout.setOnClickListener {
                Action {
                    if (showQaCheckTips == 2) {
                        //showQaCheckTips = 0
                        GeneralShowDialogUtils.showQaCheckDialog(activity)
                        return@Action
                    }
                    if (needShowLockTips && !Constant.isSpecialMember()) {
                        // needShowLockTips = false
                        GeneralShowDialogUtils.showLockCheckDialog(activity, answerId != 0)
                        return@Action
                    }

                    // 直接回复问题 文章
                    replyBottomDialog.popupStatus = PopupStatus.Dismiss
                    replyBottomDialog.show()
                    ReplyUtils.showReplyInput(
                        SubCommentEvent(
                            answerId,
                            type,
                            editDialog.hashCode(),
                            setComment()
                        )
                    )
                }.logInToIntercept()
            }
            binding.stateLayout.showLoadingView()

            initData()
            orderLayout.setSelectedClickListener {
                sort = if (it == 0) "default" else "time"
                refresh = true
                binding.smartRefresh.resetNoMoreData()
                thisPageUserCommentData.clear()
                getData()
            }
        }
        if (lazyLoad) {
            getData()
        }


        if (needTopId != 0) {
            needTopId = 0
            commentAdapter.fullItem = true
        }
    }

    var keyboardHeight = 0

    @SuppressLint("SetTextI18n")
    private fun initData() {
        viewModel.apply {
            // 文章详情评论列表
            commentList.observe(activity) {
                binding.stateLayout.showContentView()
                try {
                    if (thisPageUserCommentData.isNotNullEmpty()) {
                        thisPageUserCommentData.reversed().forEach { forData ->
                            removeOrAdd(forData, it.data1.answers, false)
                        }
                    }
                } catch (e: Exception) {
                }

                commentAdapter.setDiffNewData(it.data1.answers)
                setItemNotify()
                if (refresh) {
                    // commentAdapter.data.clear()
                    Looper.myQueue().addIdleHandler {
                        binding.recyclerView.scrollToPosition(0)
                        false
                    }
                }
                binding.apply {
                    smartRefresh.finishLoadMore(300, true, it.data2)
                    setReplySize(it.data1.total)
                    if (commentAdapter.data.isNullOrEmpty())
                        stateLayout.showEmptyView()
                }
                // 需要再加更多的子评论


            }


            // 问答详情评论列表
            commentResult.observe(activity) {
                binding.stateLayout.showContentView()
                if (it.data3) {
                    commentAdapter.data.clear()
                    commentAdapter.notifyDataSetChanged()
                }

                try {
                    if (thisPageUserCommentData.isNotNullEmpty()) {
                        thisPageUserCommentData.reversed().forEach { forData ->
                            removeOrAdd(forData, it.data1.answers, false)
                        }
                    }
                } catch (e: Exception) {
                }

                commentAdapter.setDiffNewData(it.data1.answers)
                setItemNotify()
                if (it.data3) {
                    binding.recyclerView.scrollToPosition(0)
                }
                if (it.data2) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                }

                binding.apply {
                    smartRefresh.finish()
                    setReplySize(it.data1.total)
                    if (commentAdapter.data.isNullOrEmpty())
                        stateLayout.showEmptyView()
                }
            }


            // 回答评论的子评论
            childCommentResult.observe(activity) {
                if (it.data1.answers.isNotNullEmpty()) {
                    val allList = commentAdapter.data
                    val item = allList[it.data3]
                    it.data1.answers = it.data1.answers.filter {
                        var result = true
                        item.secondAnswers.forEach { localeData ->
                            if (localeData.answerId == it.answerId)
                                result = false
                        }
                        return@filter result
                    }.toMutableList()
                    item.secondAnswers.addAll(it.data1.answers)



                    item.secondAnswerFinish = it.data1.pageCount <= it.data2
                    item.secondAnswerPage = item.secondAnswerPage + 1

                    commentAdapter.notifyDataSetChanged()
                } else {
                    //失败也要暂停loading
                    commentAdapter.notifyDataSetChanged()
                }
            }
            givenCount.observe(activity) {
                if (it.data3 == -1) {
                    commentAdapter.data[it.data2].giveCount = it.data1.data1
                    commentAdapter.data[it.data2].isGiven = it.data1.data2
                } else {
                    commentAdapter.data[it.data2].secondAnswers[it.data3].giveCount = it.data1.data1
                    commentAdapter.data[it.data2].secondAnswers[it.data3].isGiven = it.data1.data2
                }
                commentAdapter.notifyItemChanged(it.data2)
            }
            defLayout.showError.observe(activity) {
                binding.stateLayout.showErrorView()
            }
        }

        editDialog.sendListener = object : SendStateListener {
            override fun onStart(event: CommentEvent) {
                if (articleId != event.originId) {
                    return
                }
                if (type == Constant.TYPE_QUESTION && event.commentList.answers[0].topAnswerId != answerId)
                    return

                removeOrAdd(event, commentAdapter.data, true)
                commentAdapter.notifyDataSetChanged()
                if (!event.isSubComment)
                    binding.recyclerView.smoothScrollToPosition(0)
                // 没有刷新功能不需要处理 list
                thisPageUserCommentData.add(event)
                try {
                    val size =
                        binding.textCommentsSize.text.toString().filter { it.isDigit() }.toInt()
                    setReplySize(size + 1)
                } catch (e: Exception) {
                }
            }

            override fun onSuccess(event: CommentEvent) {
                if (articleId != event.originId) {
                    return
                }
                if (type == Constant.TYPE_QUESTION && event.commentList.answers[0].topAnswerId != answerId)
                    return
                thisPageUserCommentData.map {
                    if (it.clientId == event.clientId) {
                        it.commentList = event.commentList
                        it.topPosition = event.topPosition
                        it.isSubComment = event.isSubComment
                        it.originId = event.originId
                        it.topAnswerId = event.topAnswerId
                        it.commentList.answers[0].isPreview = false
                    }
                }

                if (event.isSubComment) {
                    commentAdapter.data[event.topPosition].secondAnswers.removeAll { it.clientId == event.clientId }
                } else {
                    commentAdapter.data.removeAll { it.clientId == event.clientId }
                }
                thisPageUserCommentData.forEach { forData ->
                    removeOrAdd(forData, commentAdapter.data, false)
                }
            }

            override fun onFail(event: CommentEvent) {
                thisPageUserCommentData.removeAll { it.clientId == event.clientId }
                if (event.isSubComment) {
                    commentAdapter.data[event.topPosition].secondAnswers.removeAll { it.clientId == event.clientId }
                } else {
                    commentAdapter.data.removeAll { it.clientId == event.clientId }
                }
                commentAdapter.notifyDataSetChanged()
                try {
                    val size =
                        binding.textCommentsSize.text.toString().filter { it.isDigit() }.toInt()
                    setReplySize(size - 1)
                } catch (e: Exception) {
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setReplySize(size: Int) {
        binding.apply {
            textCommentsSize.text = "${size}条评论"
            if (textCommentsSize.isGone()) {
                textCommentsSize.visible()
            }
        }
    }

    private fun initAdapter() {
        if (binding.recyclerView.adapter != null) {
            return
        }
        binding.recyclerView.adapter = commentAdapter
        binding.recyclerView.isMotionEventSplittingEnabled = false
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.closeDefaultAnimator()
        commentAdapter.itemClick = { top, sub ->
            Action {
                val item = commentAdapter.data[top].secondAnswers[sub]
                viewModel.answerGiven(item.answerId, !item.isGiven, top, sub)
            }.logInToIntercept()
        }
        commentAdapter.setDiffCallback(DiffUtilCallBack.replyCallBack())
        commentAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = commentAdapter.data[position]
            if (item.isPreview)
                return@setOnItemChildClickListener
            when (view.id) {
                R.id.ll_more -> {
                    openMore(position)
                }
                R.id.cl_click_layout -> {
                    // 回复主评论
                    Action {
                        if (showQaCheckTips == 2) {
                            //showQaCheckTips = 0
                            GeneralShowDialogUtils.showQaCheckDialog(activity)
                            return@Action
                        }
                        if (needShowLockTips && !Constant.isSpecialMember()) {
                            //needShowLockTips = false
                            GeneralShowDialogUtils.showLockCheckDialog(activity, answerId != 0)
                            return@Action
                        }

                        replyBottomDialog.popupStatus = PopupStatus.Dismiss
                        replyBottomDialog.show()
                        ReplyUtils.showReplyInput(
                            SubCommentEvent(
                                item.answerId,
                                position, item.topAnswerId, item.owner,
                                item.user.userName ?: "",
                                type,
                                editDialog.hashCode(),
                                setComment()
                            )
                        )

                        val array = IntArray(2)
                        view.getLocationOnScreen(array)
                        val rect = Rect()
                        view.getLocalVisibleRect(rect)
                        binding.recyclerView.postDelayed({
                            val bottomHeight =
                                ScreenUtils.getScreenHeight() - array[1] - rect.height()  //当前view bottom 距离屏幕底部的距离
                            if (keyboardHeight + offsetHeight > bottomHeight) {//如果当前评论view在键盘高度加上输入框52dp之下就需要滚动
                                offsetScroll =
                                    keyboardHeight - bottomHeight + offsetHeight + 12f.dp() // 底部12dp间距
                                binding.recyclerView.smoothScrollBy(0, offsetScroll)
                            }
                        }, 300)
                    }.logInToIntercept()
                }
//                R.id.text_send_like -> {
//                    Action {
//                        viewModel.answerGiven(item.answerId, !item.isGiven, position, -1)
//                    }.logInToIntercept()
//                }

                R.id.text_user_name, R.id.image_user -> {

                    ARouter.getInstance().build("/mine/message/personal_page")
                        .withInt(TagConstant.PARAMS, item.user.uid).navigation()
                }

            }
        }
    }

    private fun openMore(position: Int) {
        viewModel.getAnswerCommentsChild(
            commentAdapter.data[position].answerId,
            if (Constant.TYPE_ARTICLE == type) 1 else if (Constant.TYPE_VIDEO == type) 3 else 2,
            commentAdapter.data[position].secondAnswerPage,
            position
        )
    }

    private fun initRefresh() {
//        binding.smartRefresh.setRefreshFooter(footer)
        binding.smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                refresh = true
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                refresh = false
                getData()
            }

        })
    }

    private fun getData() {

        if (Constant.TYPE_QUESTION == type) {
            //回答评论
            viewModel.getAnswerTopComment(articleId, answerId, sort, refresh, needTopId)
        } else {
            //文章、视频详情评论
            viewModel.getComments(
                articleId,
                if (Constant.TYPE_ARTICLE == type) 1 else 3,
                sort,
                refresh,
                needTopId
            )
        }

        //文章详情评论
//        if (Constant.TYPE_ARTICLE == type) {
//            viewModel.getComments(articleId, sort, refresh, needTopId)
//        } else if (Constant.TYPE_VIDEO == type) {
//            //回答评论
//            viewModel.getVideoTopComment(articleId, sort, refresh, needTopId)
//        } else {
//            //回答评论
//            viewModel.getAnswerTopComment(articleId, answerId, sort, refresh, needTopId)
//        }
    }


    override fun onShow() {
        setItemNotify()
        updateLikeNum()
        super.onShow()
        if (!lazyLoad) {
            binding.root.postDelayed({
                getData()
                lazyLoad = true
            }, 500)

        }

    }

    private fun updateLikeNum() {
        if (commentAdapter.data.isNullOrEmpty() || headComment.isNullOrEmpty())
            return

        headComment.forEach { comment ->
            commentAdapter.data.forEach {
                if (comment.answerId == it.answerId) {
                    it.giveCount = comment.giveCount
                    it.isGiven = comment.isGiven
                }
            }
        }
        commentAdapter.notifyDataSetChanged()

    }

    private fun setItemNotify() {
        if (commentAdapter.data.isNullOrEmpty())
            return
        if (highlightedId != -1) {
            highlighted =
                commentAdapter.data.indexOf(commentAdapter.data.singleOrNull { it.answerId == highlightedId })
            highlightedId = -1
        }
        if (highlighted != -1) {
            commentAdapter.highlighted = highlighted
            commentAdapter.notifyItemChanged(highlighted)

            try {
                binding.recyclerView.postDelayed({
                    commentAdapter.fullItem = false
                    binding.recyclerView?.smoothScrollToPosition(highlighted)
                    highlighted = -1
                }, 500)
            } catch (e: Exception) {
            }
        }
    }


    override fun doAfterShow() {
        super.doAfterShow()
        if (needShowEdit) {
            if (showQaCheckTips == 2) {
                needShowEdit = false
                //showQaCheckTips = 0
                GeneralShowDialogUtils.showQaCheckDialog(activity)
                return
            }
            if (needShowLockTips && !Constant.isSpecialMember()) {
                needShowEdit = false
                //needShowLockTips = false
                GeneralShowDialogUtils.showLockCheckDialog(activity, answerId != 0)
                return
            }

            replyBottomDialog.show()
            ReplyUtils.showReplyInput(
                SubCommentEvent(
                    answerId, type, editDialog.hashCode(),
                    setComment()
                )
            )
            needShowEdit = false
        }
    }

    private fun setComment(): CommentList.Comment {
        val comment = CommentList.Comment()
        comment.answerPublishedUid = answerUid
        comment.articlePublishedUid = publishUid
        comment.questionPublishedUid = publishUid
        return comment
    }


    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.8f).toInt()
    }

    override fun getPopupHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.8f).toInt()
    }


    override fun getMaxWidth(): Int {
        return SizeUtil.getWidth()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun cleanEvent(event: CleanTextEvent) {
        if (event.isInDialog) {
            if (event.isClean) {
                binding.textReply.text = ""
            }
            if (event.content.isNotNullEmpty()) {
                binding.textReply.text = event.content
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            binding.imageHead.loadCircle(Constant.userInfo.avatarFile)
        } else {
            binding.imageHead.loadCircle(R.drawable.icon_default_login_avatar)
        }
    }


    private fun removeOrAdd(
        event: CommentEvent,
        dataList: MutableList<CommentList.Comment>,
        isAdd: Boolean,
    ) {
        if (event.commentList.answers.isNullOrEmpty())
            return
        if (event.isSubComment) {
            if (Constant.TYPE_ARTICLE == type || Constant.TYPE_VIDEO == type) {
                // 移除已经存在的
                //过滤重复数据
                val iterator = dataList[event.topPosition].secondAnswers.iterator()
                while (iterator.hasNext()) {
                    val data = iterator.next()
                    if (data.answerId == event.commentList.answers[0].answerId
                    ) {
                        iterator.remove()
                    }
                }
                if (dataList[event.topPosition].answerId == event.commentList.answers[0].replyId ||
                    dataList[event.topPosition].answerId == event.commentList.answers[0].topAnswerId
                ) {
                    dataList[event.topPosition].secondAnswers.addAll(
                        0,
                        event.commentList.answers
                    )

                    if (isAdd) {
                        dataList[event.topPosition].secondAnswerCount += event.commentList.answers.size
                    }
                }


                commentAdapter.notifyDataSetChanged()
            } else {
                //过滤重复数据
                val iterator = dataList[event.topPosition].secondAnswers.iterator()
                while (iterator.hasNext()) {
                    val data = iterator.next()
                    if (data.answerId == event.commentList.answers[0].answerId) {
                        iterator.remove()
                    }
                }

                if (dataList[event.topPosition].answerId == event.commentList.answers[0].secondAnswerId) {
                    dataList[event.topPosition].secondAnswers.addAll(
                        0,
                        event.commentList.answers
                    )
                    if (isAdd) {
                        dataList[event.topPosition].secondAnswerCount += event.commentList.answers.size
                    }
                }
                commentAdapter.notifyDataSetChanged()
            }
        } else {
            val item = dataList.filter { it.answerId == event.commentList.answers[0].answerId }
            if (item.isNotNullEmpty()) {
                dataList.removeAll(item)
            }
            dataList.addAll(0, event.commentList.answers)
            commentAdapter.notifyDataSetChanged()
        }
        if (dataList.isNullOrEmpty())
            binding.stateLayout.showEmptyView()
        else
            binding.stateLayout.showContentView()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onDismiss() {
        binding.textReply.text = ""
        editDialog.cleanComment()
        highlighted = -1
        if (binding.orderLayout.currentTab == 0) {
            EventBus.getDefault().post(
                GivenEvent(
                    articleId,
                    answerId,
                    commentAdapter.data.take(3),
                    commentAdapter.data.size,
                    answerPositionInActivity
                )
            )
        }

        super.onDismiss()
    }

}