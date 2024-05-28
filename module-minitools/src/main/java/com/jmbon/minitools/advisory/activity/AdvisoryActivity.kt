package com.jmbon.minitools.advisory.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.RelativeLayout.CENTER_IN_PARENT
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.GsonUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.jmbon.minitools.advisory.adapter.AdvisoryChatAdapter
import com.jmbon.minitools.advisory.arouter.AdvisoryRouter
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm.Companion.ITEM_TYPE_ROBOT
import com.jmbon.minitools.advisory.viewmodel.AdvisoryViewModel
import com.jmbon.minitools.databinding.ActivityAdvisoryLayoutBinding
import com.jmbon.minitools.databinding.ViewAdvisoryTitleLayoutBinding
import com.qmuiteam.qmui.kotlin.onClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/******************************************************************************
 * Description: 免费咨询
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *************************************************** ****************************/
@Route(path = AdvisoryRouter.ADVISORY_HELP)
class AdvisoryActivity : ViewModelActivity<AdvisoryViewModel, ActivityAdvisoryLayoutBinding>() {

    /**
     * 咨询聊天Adapter
     */
    private val chatAdapter by lazy {
        AdvisoryChatAdapter()
    }

    private val titleBinding by lazy {
        ViewAdvisoryTitleLayoutBinding.inflate(layoutInflater).apply {
            CommonViewModel.configFlow.value?.let {
                tvTitle.text = it.dialogTitle
                ivIcon.load(it.robotAvatar)
            }
        }
    }

    @Autowired
    @JvmField
    var advisoryId: Int = 0

    @Autowired
    @JvmField
    var questionId: Int = 0

    @Autowired
    @JvmField
    var question: String = ""

    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        ImmersionBar.with(this)
            .transparentStatusBar()
            .navigationBarColorInt(Color.BLACK)
            .keyboardEnable(true)
            .init()
        setTitleBarColor(Color.BLACK)
        val imageTintList = ColorStateList.valueOf(Color.WHITE)
        titleBarView.leftImageButton.imageTintList = imageTintList
        titleBarView.mainLayout.addView(titleBinding.root, RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(CENTER_IN_PARENT)
        })

        binding.apply {
            rvContent.init(chatAdapter)
            etContent.addTextChangedListener {
                if (!it?.toString().isNullOrBlank()) {
                    ivSend.alpha = 1.0f
                } else {
                    ivSend.alpha = 0.2f
                }
            }

            etContent.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendQuestion()
                    true
                }
                false
            }

            etContent.onClick {
                Action {
                    if (viewModel.waitReplying) {
                        "别着急\n请等待小助手回复".showToast()
                        return@Action
                    }

                    KeyboardUtils.showSoftInput(binding.etContent)
                }.logInToIntercept()
            }

            ivSend.onClick {
                sendQuestion()
            }

            // 预览模式没有底部交互, 所以不需要监听键盘行为
            if (advisoryId == 0) {
                // 解决键盘弹出时底部内容被遮挡的问题
                KeyboardUtils.registerSoftInputChangedListener(this@AdvisoryActivity) { height ->
                    if (height > 200) {
                        rvContent.post {
                            if (chatAdapter.itemCount > 0) {
                                rvContent.smoothScrollToPosition(chatAdapter.itemCount - 1)
                            }
                        }
                    } else {
                        //binding.rvFunction.isVisible = !binding.gpResolved.isVisible
                        rvContent.post {
                            if (chatAdapter.itemCount > 0) {
                                rvContent.scrollToPosition(chatAdapter.itemCount - 1)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.advisoryId = advisoryId
        // 监听咨询回复
        viewModel.advisoryReplyLD.observe(this) {
            if (!it.isNullOrEmpty()) {
                // 收到回复后删除Loading样式
                val loadingIndex = chatAdapter.data.indexOfLast { it1 -> it1.content.isNullOrBlank() }
                if (loadingIndex >= 0) {
                    chatAdapter.removeAt(loadingIndex)
                }
                chatAdapter.addData(it)
                binding.rvContent.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        if (advisoryId > 0 || questionId > 0) {
            // 请求并监听咨询会话历史记录
            viewModel.advisoryConversationHistoryLD.observe(this) {
                val historyList = mutableListOf<AdvisoryItemForm>()
                if (!it?.list.isNullOrEmpty()) {
                    historyList.addAll(it?.list!!)
                }
                chatAdapter.setList(historyList)
                if (questionId > 0 && chatAdapter.itemCount > 0) {
                    binding.rvContent.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            }
            if (advisoryId > 0) {
                binding.vLine.gone()
                binding.flInput.gone()
                viewModel.getAdvisoryConversationHistory()
            } else {
                // 非审核模式下才可提问
                binding.flInput.isVisible = CommonViewModel.configFlow.value?.examine_switch == 0
                binding.vLine.isVisible = binding.flInput.isVisible
                viewModel.getDefaultConversationHistory(questionId)
            }
        }
    }

    /**
     * 发送问题
     */
    private fun sendQuestion() {
        val content = binding.etContent.text.toString()
        if (content.isNullOrBlank()) {
            "不能发送空白消息".showToast()
            return
        }
        realSendQuestion(content)
    }

    /**
     * 发送问题
     * @param content: 需要发送的问题内容
     */
    private fun realSendQuestion(content: String) {
        chatAdapter.addData(
            AdvisoryItemForm(
                role = AdvisoryItemForm.ITEM_TYPE_USER,
                content = content
            )
        )
        binding.etContent.setText("")
        KeyboardUtils.hideSoftInput(binding.etContent)
        binding.etContent.isFocusable = false
        // 追问后添加助手Loading样式
        chatAdapter.addData(AdvisoryItemForm(isFakeData = true))
        viewModel.getAdvisoryReply(content, questionId)
        binding.rvContent.scrollToPosition(chatAdapter.itemCount - 1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CommonEventHub.ReceiveReplyEvent) {
        val replyInfo = GsonUtil.gson().fromJson(event.replyInfo, AdvisoryItemForm::class.java)
            ?: return

        KeyboardUtils.hideSoftInput(binding.etContent)
        chatAdapter.addData(
            AdvisoryItemForm(
                content = replyInfo.content,
                role = ITEM_TYPE_ROBOT,
            )
        )
        binding.apply {
            rvContent.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}