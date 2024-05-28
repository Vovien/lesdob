package com.jmbon.minitools.content.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.next
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.adapter.ContentAdapter
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.extention.ShadowHelper
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.getRealStatusHeight
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.jmbon.minitools.R
import com.jmbon.minitools.content.bean.KnowledgeInfoBean
import com.jmbon.minitools.content.bean.RandomUserBean
import com.jmbon.minitools.content.viewmodel.ContentViewModel
import com.jmbon.minitools.databinding.HeaderKnowledgeLayoutBinding
import com.jmbon.minitools.databinding.ItemBannerLayoutBinding
import com.qmuiteam.qmui.kotlin.onClick
import com.youth.banner.adapter.BannerAdapter

/******************************************************************************
 * Description: 知识Fragment
 *
 * Author: jhg
 *
 * Date: 2023/10/23
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class KnowledgeFragment: ViewModelFragment<ContentViewModel, CommonListLayoutBinding>() {

    private val headerBinding by lazy {
        HeaderKnowledgeLayoutBinding.inflate(LayoutInflater.from(context))
    }

    private val contentAdapter by lazy {
        ContentAdapter().apply {
            addHeaderView(headerBinding.root)
        }
    }

    private val tipContentAdapter by lazy {
        object: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_header_tip_layout) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_content, item)
            }
        }
    }

    override fun initView(view: View) {
        binding.apply {
            srlRefresh.clipToPadding = true
            srlRefresh.setPadding(0, getRealStatusHeight(), 0, 0)
            srlRefresh.setEnableRefresh(false)
            srlRefresh.setEnableLoadMore(false)
            rvContent.init(contentAdapter,
                dividerColor = R.color.white06.toColorInt(),
                dividerHeight = 1f,
                left = 20f,
                right = 20f,
                vertical = false)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.knowledgeInfoLD.observe(this) {
            binding.srlRefresh.dealPage(viewModel.currentPage, it?.article_list, contentAdapter) { _, emptyText ->
                emptyText.text = "暂无相关内容"
            }
            headerBinding.initHeader(it)
        }
        viewModel.getKnowledgeInfo()

        launch {
            CommonViewModel.configFlow.next {
                headerBinding.tvTitle.text = if (this?.examine_switch == 0) {
                    "推荐"
                } else {
                    "精选"
                }
            }
        }
    }

    private fun HeaderKnowledgeLayoutBinding.initHeader(knowledgeInfo: KnowledgeInfoBean?) {
        if (knowledgeInfo?.card == null) {
            gpHeader.gone()
            return
        }

        knowledgeInfo.card.apply {
            if (!this.bg_img.isNullOrBlank()) {
                ivHeader.load(this.bg_img)
            }
            ivPeople.load(this.person_img)
            ivTitle.load(this.title_img)
            tvButton.setTextWhenNotEmpty(this.button_txt)
            ShadowHelper.Builder()
                .setBgColor(Color.parseColor("#FF832E"))
                .setShapeRadius(11.dp)
                .setShadowColor(ColorUtils.setAlphaComponent(Color.parseColor("#FF832E"), 0.4f))
                .setShadowRadius(16.dp)
                .setShadowSpacing(12.dp, 0, 12.dp, 32.dp)
                .applyTo(tvButton)
            tipContentAdapter.setList(this.text_list)
            rvContent.addItemDecoration(GridSpacingItemDecoration(spacing = 12.dp))
            rvContent.adapter = tipContentAdapter
            rvContent.suppressLayout(true)

            vPlaceholder.onClick {
                BannerHelper.onClick(CommonBanner(item_type = this.item_type, identity = this.identity))
            }
            ivPeople.onClick {
                vPlaceholder.performClick()
            }
        }

        bannerTip.setAdapter(object: BannerAdapter<RandomUserBean, BaseViewHolder>(knowledgeInfo.card.rand_user) {

            override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
                return BaseViewHolder(ItemBannerLayoutBinding.inflate(LayoutInflater.from(context)).root.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                })
            }

            override fun onBindView(
                holder: BaseViewHolder?,
                data: RandomUserBean,
                position: Int,
                size: Int
            ) {
                holder?.getView<ImageView>(R.id.iv_icon)?.loadCircle(data.avatar_file)
                holder?.setText(R.id.tv_content, "${data.user_name} 刚刚参与了拼购")
            }

        })
        bannerTip.setUserInputEnabled(false)
    }
}