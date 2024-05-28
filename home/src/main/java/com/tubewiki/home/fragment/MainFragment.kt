package com.tubewiki.home.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.utils.*
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.adapter.HomeTubeStepAdapter
import com.tubewiki.home.bean.HomeBean
import com.tubewiki.home.databinding.FragmentMainLayoutBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel


/**
 * 我的首页
 */
@Route(path = "/home/main/fragment")
class MainFragment : DataBindingFragment<MainFragmentViewModel, FragmentMainLayoutBinding>() {

    private val tubeStepAdapter by lazy { HomeTubeStepAdapter() }

    override fun initView(view: View) {
        initStateLayout(binding.stateLayout)
        initListener()
        initRecyclerview()
        binding.tvAdvisory.setBackground(
            backgroundColor = Color.WHITE,
            borderWidth = 3.dp,
            borderGradientColors = intArrayOf(
                Color.parseColor("#E02020"),
                Color.parseColor("#FA6400"),
                Color.parseColor("#F7B500"),
                Color.parseColor("#6DD400"),
                Color.parseColor("#0091FF"),
                Color.parseColor("#6236FF"),
                Color.parseColor("#B620E0"),
            ),
            borderOrientation = GradientDrawable.Orientation.RIGHT_LEFT,
            radius = 16.dp
        )
        binding.tvAdvisory.onClick {
            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toAdvisoryHelp()
        }
    }

    private fun initListener() {
        tubeStepAdapter.setOnItemClickListener { adapter, view, postion ->
//            ARouter.getInstance().build("/home/activity/tube_step")
//                .withInt(TagConstant.COLUMN_ID, tubeStepAdapter.getItem(postion).id).navigation()
        }
    }

    private fun initRecyclerview() {

        binding.recycleTube.apply {
            init(
                tubeStepAdapter,
                layoutManager = GridLayoutManager(activity, 2),
                dividerHeight = 10f,
                vertical = false,
                showEnd = true
            )
        }
    }


    override fun getData() {
        super.getData()

        started {
            viewModel.index().netCatch {
                showErrorState()
            }.next {
                showContentState()
                this.apply {
                    binding.homeBean = this
                    tubeStepAdapter.setNewInstance(cateList)
                }
            }
        }
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()
    }


}