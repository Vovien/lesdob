package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.load
import com.tubewiki.home.bean.HomeBean
import com.tubewiki.home.databinding.ItemHomeTubeStepLayoutBinding
import com.tubewiki.home.router.HomeRouter

class HomeTubeStepAdapter :
    BindingQuickAdapter<HomeBean.Cate, ItemHomeTubeStepLayoutBinding>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            HomeRouter.toQuestionCategory(data[position].id)
        }
    }

    override fun convert(holder: BaseBindingHolder, item: HomeBean.Cate) {
        holder.getViewBinding<ItemHomeTubeStepLayoutBinding>().apply {
            ivBg.load(item.icon)
            tvTitle.text = item.title
        }
    }
}