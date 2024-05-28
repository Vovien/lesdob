package com.jmbon.middleware.comment

import android.annotation.SuppressLint
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.dialog.BaseViewModelBottomDialog
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.SizeUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.comment.`interface`.SendStateListener
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.comment.event.CleanTextEvent
import com.jmbon.middleware.comment.event.CommentEvent
import com.jmbon.middleware.comment.event.SubCommentEvent
import com.jmbon.middleware.comment.viewmodel.CommentViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.DialogLayoutBottomInputBinding
import com.jmbon.middleware.utils.RequestParamsUtils
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.loadCircle
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@SuppressLint("ViewConstructor")
class CommentEditTextDialog(
    var articleId: Int,
    var type: String,
    val isInDialog: Boolean,
    activity: FragmentActivity,
) :
    BaseViewModelBottomDialog<CommentViewModel, DialogLayoutBottomInputBinding>(activity) {
    var sendListener: SendStateListener? = null
    var subEvent = SubCommentEvent()
    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        binding.apply {
            if (Constant.isLogin) {
                imageHead.loadCircle(Constant.userInfo.avatarFile)
            } else {
                imageHead.loadCircle(R.drawable.icon_default_login_avatar)
            }
            sbSend.setOnClickListener {
                if (binding.editComment.text.isNullOrBlank()) {
                    binding.editComment.text?.clear()
                    R.string.comment_empty.showToast()
                    return@setOnClickListener
                }

                sendComment()
            }

            editComment.addTextChangedListener(afterTextChanged = {
                sbSend.isEnabled = it.isNotNullEmpty()
            })
        }
        KeyboardUtils.registerSoftInputChangedListener(activity) {
            if (it <= 100) {
                dismiss()
            }
        }
    }

    private fun sendComment() {
        KeyboardUtils.hideSoftInput(binding.editComment)
        if (binding.editComment.text.isNotNullEmpty())
            viewModel.viewModelScope.launch {
                val clientId = RequestParamsUtils.MD5(UUID.randomUUID().toString())
                val answerContent = binding.editComment.text.toString()
                viewModel.answerComment(
                    articleId,
                    answerContent,
                    type,
                    subEvent.answerId, clientId
                ).onStart {
                    val comment = CommentList.Comment(
                        answerContent = answerContent,
                        uid = Constant.getUserId(),
                        isGiven = false,
                        isCollect = false,
                        topAnswerId = subEvent.topAnswerId,
                        secondAnswerId = subEvent.secondAnswerId,
                        replyId = subEvent.replyId,
                        receiver = subEvent.receiver,
                        articlePublishedUid = subEvent.articlePublishedUid,
                        videoPublishedUid = subEvent.videoPublishedUid,
                        questionPublishedUid = subEvent.questionPublishedUid,
                        answerPublishedUid = subEvent.answerPublishedUid,
                        owner = CommentList.Comment.UserReceiver(
                            uid = Constant.getUserId(),
                            userName = Constant.userInfo.userName,
                            avatarFile = Constant.userInfo.avatarFile
                        ),
                        clientId = clientId, isPreview = true
                    )
                    val commentList = CommentList(arrayListOf(comment))
                    binding.editComment.text?.clear()
                    sendListener?.onStart(createEvent(commentList, clientId))
                }.netCatch {
                    message.showToast()
                    sendListener?.onFail(createEvent(null, clientId))
                }.next {
                    sendListener?.onSuccess(createEvent(this, clientId))
                    binding.editComment.setText("")
                    sendEvent(this, clientId)
                    EventBus.getDefault().post(CleanTextEvent(true, isInDialog))
                }
            }
    }

    private fun sendEvent(list: CommentList?, clientId: String) {
        EventBus.getDefault().post(
            createEvent(list, clientId)
        )
    }

    private fun createEvent(list: CommentList?, clientId: String): CommentEvent {
        list?.let {
            it.answers[0].clientId = clientId
        }
        return CommentEvent(
            list,
            articleId,
            subEvent.answerId,
            subEvent.topPosition,
            subEvent.isSubComment, clientId
        )
    }


    fun getComment(): String {
        return try {
            binding.editComment.text.toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun cleanComment() {
        try {
            binding.editComment.text?.clear()
        } catch (e: Exception) {
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun commentEvent(event: SubCommentEvent) {
        //比较hashcode 避免其他event事件影响
        if (event.hashCode != 0 && event.hashCode != this.hashCode()) {
            return
        }
        subEvent = event
        //onShow修改状态有个延迟
        binding.apply {
            if (subEvent.replyName.isNotNullEmpty())
                editComment.hint = "回复@${subEvent.replyName}"
            else editComment.setHint(R.string.published_under_your_point_view)
        }
        //show()
        //   Log.e("TAG", "SubCommentEvent ${this.hashCode()}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun commentEvent(event: CommentEvent) {
        if (event.commentList != null)
            binding.editComment.text?.clear()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun getPopupWidth(): Int {
        return SizeUtil.getWidth()
    }

    override fun onDismiss() {
        if (binding.editComment.text.isNotNullEmpty())
            EventBus.getDefault()
                .post(CleanTextEvent(binding.editComment.text.toString(), isInDialog))
        else EventBus.getDefault().post(CleanTextEvent(true, isInDialog))
        super.onDismiss()
    }
}