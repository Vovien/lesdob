package com.tubewiki.mine.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.setTextColor
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.ItemRecordBean
import com.tubewiki.mine.view.model.MineFragmentViewModel

/******************************************************************************
 * Description: 会话记录
 *
 * Author: jhg
 *
 * Date: 2023/10/12
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = "/mine/conversationRecord")
class ConversationRecordActivity: ViewModelActivity<MineFragmentViewModel, CommonListLayoutBinding>() {

    private val recordAdapter by lazy {
        object: BaseQuickAdapter<ItemRecordBean, BaseViewHolder>(R.layout.item_conversation_record_layout) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    ARouter.getInstance().navigation(IMiniToolProvider::class.java).toAdvisoryHelp(advisoryId = data[position].dialog_id)
                }
            }

            override fun convert(holder: BaseViewHolder, item: ItemRecordBean) {
                holder.setText(R.id.tv_create_time, item.create_time)
                holder.setText(R.id.tv_question, item.question)
                holder.getView<ImageView>(R.id.iv_robot).load(item.avatar)
                holder.setText(R.id.tv_answer, item.answer)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPageState()
        ImmersionBar.with(this)
            .statusBarColor(R.color.black)
            .transparentNavigationBar()
            .init()
        setTitleName("对话记录")
        setTitleBarColor(Color.BLACK)
        titleBarView.centerTextView.setTextColor(Color.WHITE)
        val imageTintList = ColorStateList.valueOf(Color.WHITE)
        titleBarView.leftImageButton.imageTintList = imageTintList
        binding.rvContent.setPadding(20.dp, 100.dp, 20.dp, 24.dp)
        binding.rvContent.init(recordAdapter, dividerHeight = 12f, vertical = false)
        binding.srlRefresh.setEnableRefresh(false)
        binding.srlRefresh.setEnableLoadMore(false)
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.conversationRecordListLD.observe(this) {
            showContentState()
            binding.srlRefresh.dealPage(viewModel.currentPage, it, recordAdapter) { _, emptyText ->
                emptyText.text = "暂无会话记录"
            }
            if (recordAdapter.data.size >= 10) {
                recordAdapter.addFooterLayout()
            }
        }
        viewModel.getConversationRecordList()
    }

    private fun BaseQuickAdapter<*, *>.addFooterLayout() {
        if (hasFooterLayout()) {
            return
        }

        addFooterView(TextView(this@ConversationRecordActivity).apply {
            setPadding(0, 12.dp, 0, 0)
            text = "只记录最近10条记录"
            textSize = 16f
            setTextColor(R.color.white40.toColorInt())
            gravity = Gravity.CENTER
        })
    }
}