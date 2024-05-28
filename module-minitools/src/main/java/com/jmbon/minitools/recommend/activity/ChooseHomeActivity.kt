package com.jmbon.minitools.recommend.activity

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.navigationWithFinish
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.ActivityChooseHomeLayoutBinding
import com.jmbon.minitools.recommend.router.RecommendRouter
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel

/******************************************************************************
 * Description: 选择首页(在孕小帮你想做什么)
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = RecommendRouter.RECOMMEND_CHOOSE_HOME)
class ChooseHomeActivity : ViewModelActivity<ChooseViewModel, ActivityChooseHomeLayoutBinding>() {

    private var typeValue: Int = 0

    private val menuAdapter by lazy {
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_choose_home_layout) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    viewModel.chooseFormLD.value?.getOrNull(position).apply {
                        if (!this?.form.isNullOrEmpty()) {
                            RecommendRouter.toChooseContainer(this)
                        } else {
                            typeValue = this?.value ?: 0
                            viewModel.paramsMap["type"] = typeValue
                            viewModel.getRecommendResult()
                        }
                    }
                }
            }

            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_title, item)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarColorInt(Color.TRANSPARENT)
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.TRANSPARENT)
            .init()
        binding.rvMenu.init(menuAdapter, dividerHeight = 16f, vertical = false)
    }

    override fun initViewModel() {
        super.initViewModel()
        // 请求并监听选项表单
        viewModel.chooseFormLD.observe(this) {
            it?.map { it1 -> it1.title }?.apply {
                menuAdapter.setList(this)
            } ?: kotlin.run {
                ARouter.getInstance().build("/main/main").navigationWithFinish(this)
            }
        }
        viewModel.getChooseForm()

        // 监听提交结果
        viewModel.recommendResultLD.observe(this) {
            Constant.savePregnantStatus(typeValue)
            ARouter.getInstance().build("/main/main").navigationWithFinish(this)
        }
    }

}
