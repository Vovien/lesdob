package com.tubewiki.mine.view.collect

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.adapter.TablayoutViewPagerAdapter
import com.tubewiki.mine.databinding.FragmentCollectionBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel


@Route(path = "/mine/collection/fragment")
class CollectionFragment : ViewModelFragment<MineFragmentViewModel, FragmentCollectionBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var collectionType: String = "answer"

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var isSearch: Boolean = false


    private val mOriginTitles = arrayOf("回答", "文章","视频", "医院")
    private val mTitles = arrayOf("回答0", "文章0", "视频0", "医院0")

    private val fragmentList by lazy {
        arrayListOf(
            ARouter.getInstance().build("/mine/collection/answer_fragment")
                .withBoolean(TagConstant.PARAMS2, isSearch)
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .navigation() as Fragment,

            ARouter.getInstance().build("/mine/collection/article_fragment")
                .withBoolean(TagConstant.PARAMS2, isSearch)
                .withBoolean(TagConstant.PAGE_TYPE, true)
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .navigation() as Fragment,
       ARouter.getInstance().build("/mine/collection/video_fragment")
                .withBoolean(TagConstant.PARAMS2, isSearch)
                .withBoolean(TagConstant.PAGE_TYPE, true)
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .navigation() as Fragment,

            ARouter.getInstance().build("/mine/collection/hospital_fragment")
                .withBoolean(TagConstant.PARAMS2, isSearch)
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .navigation() as Fragment,
        )
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(view: View) {
        binding.viewpager.offscreenPageLimit = fragmentList.size
        binding.viewpager.adapter =
            TablayoutViewPagerAdapter(fragmentList, childFragmentManager, mTitles)

        binding.commonTabLayout.setViewPager(binding.viewpager)

        if ("answer" == collectionType || TextUtils.isEmpty(collectionType)) {
            binding.viewpager.currentItem = 0
        } else if ("article" == collectionType) {
            binding.viewpager.currentItem = 1
        } else if ("video" == collectionType) {
            binding.viewpager.currentItem = 2
        } else {
            binding.viewpager.currentItem = 2
        }
    }


    /**
     * 更新title 个数
     */
    fun updateTitle(index: Int, num: Int) {
        mTitles[index] = "${mOriginTitles[index]}${num}"
        binding.commonTabLayout.notifyDataSetChanged()

    }

    private var answer = true
    private var article = true
    private var hospital = true
    private var video = true
    fun searchData(keyWord: String) {
        binding.commonTabLayout.gone()
        answer = false
        article = false
        hospital = false
        video = false
        (fragmentList[0] as CollectionAnswerFragment).searchData(keyWord)
        (fragmentList[1] as CollectionArticleFragment).searchData(keyWord)
        (fragmentList[2] as CollectionVideoFragment).searchData(keyWord)
        (fragmentList[3] as CollectionHospitalFragment).searchData(keyWord)
        // view page 定位到 0
        binding.viewpager.currentItem = 0
        binding.viewpager.isPagingEnabled = false
    }


    private fun setTabShow() {
        if (answer || article|| video || hospital) {
            binding.commonTabLayout.visible()
            binding.viewpager.isPagingEnabled = true
        }
    }

    fun setSearchFinish(index: Int) {
        when (index) {
            0 -> answer = true
            1 -> article = true
            2 -> video = true
            3 -> hospital = true
        }
        setTabShow()
    }

}