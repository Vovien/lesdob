package com.tubewiki.mine.view.message.fragment

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isVisible
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.ColorCompute
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.view.state.showEmptyState2
import com.apkdv.mvvmfast.view.state.showLoadingState
import com.blankj.utilcode.util.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jmbon.middleware.activity.PhotoPreviewActivity
import com.jmbon.middleware.arouter.VideoActivityService
import com.jmbon.middleware.bean.User
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.PersonalPageAdapter
import com.tubewiki.mine.bean.UserInfoData
import com.tubewiki.mine.databinding.FragmentPersonalPageLayoutBinding
import com.tubewiki.mine.view.collect.CollectionArticleFragment
import com.tubewiki.mine.view.collect.CollectionVideoFragment
import com.tubewiki.mine.view.mine.AnswerFragment
import com.tubewiki.mine.view.mine.QuestionsFragment
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.tubewiki.mine.view.model.PersonPageViewModel
import com.tubewiki.mine.view.model.SetNewImageViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import com.tubewiki.mine.view.setting.MinePhotoPreviewActivity
import com.jmbon.widget.progress_button.JmbonButton
import com.qmuiteam.qmui.util.QMUIColorHelper
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import kotlin.math.abs


/**
 * 其他人的个人主页
 */
@Route(path = "/mine/fragment/personal_page")
class PersonalPageFragment :
    ViewModelFragment<PersonPageViewModel, FragmentPersonalPageLayoutBinding>() {

    private val followViewModle by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory<Any, Any?>()
        ).get(MessageCenterViewModel::class.java)
    }
    private val imageViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(SetNewImageViewModel::class.java)
    }
    private val settingViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(SettingViewModel::class.java)
    }

    private val videoProvider by lazy {
        ARouter.getInstance().build("/video/details/provider").navigation() as VideoActivityService
    }

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid: Int = 0

    @Autowired(name = "index")
    @JvmField
    var index: Int = 0

    @Autowired(name = TagConstant.VIDEO_TYPE)
    @JvmField
    var isVideo: Boolean = false

    var user: UserInfoData.User? = null

    private var isPrepare = false
    private var isAnimatorFocus = false //点击关注先执行动画，不关注请求结果

    private var isMe = false

    //关注的宽度，屏幕减去其他控件和减去margin
    val followWidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(175f)

    //取消关注状态时的按钮的宽度，减去私信和margin
    val followWidth2 = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(255f)
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        registerUIChange(followViewModle)
        registerDefUIChange(imageViewModel)
    }

    override fun initView(view: View) {
        initPageState(binding.container)
        binding.container.retry = { refreshDataWhenError() }
        isMe = uid == Constant.getUserId()
        binding.titleBarLayout.apply {
            val statusHeight =
                StatusBarCompat.getStatusBarHeight(view.context) + this.paddingTop
            this.setPadding(
                this.paddingStart,
                statusHeight,
                this.paddingEnd,
                this.paddingBottom
            )
        }
        binding.disFl.setBaseView(binding.imageBackground)

        binding.disFl.setViews(
            binding.imageUserAvatar,
            binding.ivBack,
            binding.ivAvatar,
            binding.tvTopFollow,
            binding.tvName,
            binding.llFollow,
            binding.privateMessage,
        )

        val maxScroll = 149f.dp().toFloat()
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scale = abs(verticalOffset) / maxScroll
            binding.smartRefresh.setEnableOverScrollDrag(scale <= 0.5)
            setScrollChanged(scale)

            //是否还能透传头像点击，当滑动大于0.7就不能点击
            binding.disFl.isCheckBaseView = scale <= 0.8f
            binding.imageUserAvatar.isClickable = scale <= 0.8f
        })
        binding.smartRefresh.setOnMultiListener(object : SimpleMultiListener() {
            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int,
            ) {
                binding.imageBackground.scaleX = 1 + percent
                binding.imageBackground.scaleY = 1 + percent
            }
        })

        binding.imageUserAvatar.loadCircle(
            R.drawable.icon_default_login_avatar,
            R.drawable.icon_default_login_avatar
        )

        binding.ivBack.setOnClickListener {
            if (isVideo) {
                videoProvider.toVideoPage(activity)
            } else {
                activity?.finish()
            }
        }

        //判断标签

        setSelfFollowStatus()


        binding.tvTopFollow.setOnClickListener {
            if (binding.tvTopFollow.alpha <= 0.1)
                return@setOnClickListener
            if (isMe) {
                startActivity("/mine/edit/info")
            } else {
                user?.let {
                    //关注 或者取消关注
                    Action {
                        if (it.uid == Constant.getUserId()) {
                            R.string.not_focus_self.showToast()
                            return@Action
                        }

                        // focus或unfocus
                        //如果关注了或者相互关注就取消关注
                        if (!it.isFocus) {
                            isAnimatorFocus = false
                            followViewModle.focusUser(it.uid, !it.isFocus, 0)
                        } else {
                            goSendMessage()
                        }
                    }.logInToIntercept()
                }
            }

        }

        binding.privateMessage.setOnClickListener {
            if (binding.privateMessage.isVisible()) {
                goSendMessage()
            }
        }
        if (Constant.getUserId() != uid) {
            viewModel.getUserInfo(uid)
        }
    }

    private fun setSelfFollowStatus() {
        if (isMe) {
            binding.apply {
                llFollow.setBackgroundResource(R.drawable.currency_btn_raduis_8_bg)
                tvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                tvFollow.setText(R.string.edit_profile)
                llFollow.setOnSingleClickListener({
                    startActivity("/mine/edit/info")
                })
                tvTopFollow.setIsShowLoadingWhenClick(false)
                tvTopFollow.text = StringUtils.getString(R.string.edit_profile_short)

                llFocusTips.setOnSingleClickListener({
                    startActivity("/mine/follow")
                })

                llFansTips.setOnSingleClickListener({
                    startActivity("/mine/fans")
                })
            }

        } else {
            binding.apply {
                tvFollow.setText(R.string.home_follow)
                tvTopFollow.text = StringUtils.getString(R.string.home_follow)

                llFocusTips.setOnSingleClickListener({

                })
                llFansTips.setOnSingleClickListener({

                })
            }

            user?.let {
                focusStatus(it.isFocus)
                focusStatus2(it.isFocus)
            }

            //防快速点击
            binding.llFollow.setOnSingleClickListener({
                user?.let {
                    //关注 或者取消关注
                    Action {
                        if (it.uid == Constant.getUserId()) {
                            R.string.not_focus_self.showToast()
                            return@Action
                        }

                        binding.llFollow.isEnabled = false
                        isAnimatorFocus = true
                        // focus或unfocus
                        //如果关注了或者相互关注就取消关注
                        it.isFocus = !it.isFocus

                        followViewModle.focusUser(it.uid, it.isFocus, 0)
                        user?.isFocus = it.isFocus
                        focusStatus2(it.isFocus)
                        if (it.isFocus) {
                            //关注的动画
                            startFocusAnimator()
                        } else {
                            startUnFocusAnimator()
                        }
                    }.logInToIntercept()
                }
            })
        }
    }

    var isFirst = true

    fun refreshData(uid: Int) {
        if (this.uid == uid)
            return
        this.uid = uid
        isMe = uid == Constant.getUserId()
        if (uid ==0){
            stateContainer?.showEmptyState2("该用户不存在")
        }else {
            viewModel.getUserJob?.cancel()
            stateContainer?.showLoadingState()
            viewModel.getUserInfo(uid)
            fragmentList.clear()
        }

    }

    override fun onResume() {
        super.onResume()
        if (Constant.getUserId() == uid) {
            // 转换
            viewModel.getUserInfo(uid)
            isFirst = false
        }
    }

    private fun goSendMessage() {
        //跳转聊天界面
        user?.let {

            if (it.verifyType == 2) {
                "医生私信暂未开放".showToast()
                return@let
            }

            val user = User()
            user.avatarFile = it.avatarFile
            user.uid = it.uid
            user.userName = it.userName
            user.isFocus = it.isFocus
            user.isMutualFocus = it.isMutualFocus
            ARouter.getInstance().build("/mine/message/chat")
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .withParcelable(TagConstant.PARAMS, user).navigation()
        }

    }

    override fun onDestroyView() {
        KotlinBus.unregister(uid.toString() + this.hashCode().toString())
        super.onDestroyView()
    }

    private fun startActivity(url: String) {
        ARouter.getInstance().build(url).navigation(activity)
    }

    override fun getData() {
        KotlinBus.register(
            uid.toString() + this.hashCode().toString(),
            UI,
            FocusChangedEvent::class.java
        ) {
            user?.let { userData ->
                // 动画已经执行过，如果结果不一致，则重新执行动画
                if (it.id == userData.uid && it.isUser) {
                    binding.llFollow.isEnabled = true
                    if (user?.isFocus != it.isFocus) {
                        focusStatus2(it.isFocus)
                        user?.isFocus = it.isFocus
                        if (it.isFocus) {
                            //关注的动画
                            startFocusAnimator()
                        } else {
                            startUnFocusAnimator()
                        }

                        isAnimatorFocus = false
                    }
                }
            }
        }


        viewModel.userInfoResult.observe(this) {
            if (it.isCancel == 1) {
                stateContainer?.showEmptyState2("该用户已注销")
            } else {
                showContentState()
                setUserInfo(it)
                setSelfFollowStatus()
            }
        }
    }

    private val TAG = "PersonalPageFragment"
    private fun setUserInfo(it: UserInfoData.User) {
        user = it
        isPrepare = true
        initViewPager()
        binding.textName.text = it.userName
        binding.tvName.text = it.userName
        if (it.sexShow == 1)
        //                ，0：不显示，1：显示
            binding.textName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, if (it.sex == 1) R.drawable.main_gender_man_icon
                else R.drawable.main_gender_women_icon, 0
            )
        else
            binding.textName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, 0, 0
            )

        setAvatar(it.avatarFile)

        if (TextUtils.isEmpty(it.description)) {
            binding.textUserInfo.visibility = View.GONE
        } else {
            binding.textUserInfo.visibility = View.VISIBLE
        }
        binding.textUserInfo.text = it.description
        if (!isMe) {
            focusStatus(it.isFocus)
            if (it.isFocus) {
                focusAnimator(SizeUtils.dp2px(70f))
            } else {
                unfocusAnimator(0)
            }
        }

        // 关注 粉丝
        binding.textFansNumber.text = "${it.fansCount}"
        // 关注
        binding.textFocusNumber.text = "${it.focusCount}"

        if (it.verifyType == 2) {
            //医生
            binding.llInfo.visible()
            binding.ivFlag.visible()
            binding.ivFlag.setImageResource(R.drawable.icon_doctor_flag)
            binding.textQualificationThat.text = it.categoryTitle
        } else {
            when (it.jobType) {
                2 -> {
                    binding.llInfo.visible()
                    binding.ivFlag.visible()
                    binding.ivFlag.setImageResource(R.drawable.icon_good_reply_flag)
                    binding.textQualificationThat.text = it.categoryTitle
                }
                3 -> {
                    binding.llInfo.visible()
                    binding.ivFlag.visible()
                    binding.ivFlag.setImageResource(R.drawable.icon_good_creator_flag)
                    binding.textQualificationThat.text = it.categoryTitle
                }
                else -> {
                    if (it.categoryTitle.isNotNullEmpty()) {
                        binding.llInfo.visible()
                        binding.ivFlag.gone()
                        binding.textQualificationThat.text = it.categoryTitle
                    } else {
                        binding.llInfo.gone()
                    }
                }
            }
        }

        if (it.categoryTitle.isNullOrEmpty()) {
            binding.llInfo.gone()
        }

        //
        if (it.background.isNotNullEmpty())
            binding.imageBackground.loadNoPlace(it.background)

        if (Constant.getUserId() == uid) {
            val userInfo = Constant.user
            userInfo.user.description = it.description
            userInfo.user.verifyType = it.verifyType
            // circle user
            Constant.circleUser?.user?.declaration = it.description
            Constant.saveUser(userInfo)
        }


        ClickUtils.applyGlobalDebouncing(binding.imageUserAvatar) { _ ->
            startActivity(
                ARouter.getInstance().build("/mine/photo/preview")
                    .withString(
                        "path",
                        if (Constant.getUserId() == uid) Constant.userInfo.avatarFile else it.avatarFile
                    )
                    .withBoolean(TagConstant.PARAMS, Constant.getUserId() == uid)
                    .withInt("type", PhotoPreviewActivity.USER_ACCOUNT_PICTURES)
            )

        }

        binding.imageBackground.setOnClickListener { _ ->
            startActivity(
                ARouter.getInstance().build("/mine/photo/preview")
                    .withString(
                        "path",
                        if (Constant.getUserId() == uid) Constant.userInfo.background else it.background
                    )
                    .withBoolean(TagConstant.PARAMS, Constant.getUserId() == uid)
            )
        }
    }

    private fun startActivity(postcard: Postcard) {
        LogisticsCenter.completion(postcard)
        val intent = Intent(context, postcard.destination)
        intent.putExtras(postcard.extras)
        this@PersonalPageFragment.startActivityForResult(intent, TagConstant.UPDATE_PICTURE_RESULT)
    }

    private fun setAvatar(avatarFile: String) {
        binding.imageUserAvatar.loadCircle(avatarFile, R.drawable.icon_default_login_avatar)
        binding.ivAvatar.loadCircle(avatarFile)
    }

    val fragmentList = arrayListOf<Fragment>()
    private fun initViewPager() {
        if (fragmentList.isNotNullEmpty())
            return
        user?.let {

            val list = arrayListOf("回答", "提问")
            fragmentList.add(
                ARouter.getInstance().build("/mine/answer/fragment")
                    .withInt(TagConstant.PARAMS, it.uid).navigation() as AnswerFragment
            )
            fragmentList.add(
                ARouter.getInstance().build("/mine/questions/fragment")
                    .withInt(TagConstant.PARAMS, it.uid).navigation() as QuestionsFragment
            )
            if (it.hasArticle) {
                list.add("文章")
                fragmentList.add(
                    ARouter.getInstance().build("/mine/collection/article_fragment")
                        .withInt(TagConstant.PARAMS, it.uid)
                        .navigation() as CollectionArticleFragment
                )
            }
            if (it.hasVideo) {
                list.add("视频")
                fragmentList.add(
                    ARouter.getInstance().build("/mine/collection/video_fragment")
                        .withInt(TagConstant.PARAMS, it.uid).navigation() as CollectionVideoFragment
                )
            }
            activity?.apply {
                try {
                    binding.viewpager.adapter = PersonalPageAdapter(fragmentList, this)
                    binding.viewpager.offscreenPageLimit = fragmentList.size
                } catch (e: Exception) {
                }
            }
            val mViewPagerImpl = binding.viewpager.getChildAt(0) as RecyclerView
            mViewPagerImpl.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            // tabLayout 和 viewPager 联动
            TabLayoutMediator(
                binding.tabLayout,
                binding.viewpager,
                true,
                true
            ) { tab, position -> tab.text = list[position] }.attach()

            if (index in 0..fragmentList.size) {
                binding.viewpager.currentItem = index
            }
            // 视频
            if (index >= fragmentList.size) {
                binding.viewpager.currentItem = list.size - 1
            }
        }
    }

    private fun focusStatus(isFocus: Boolean) {

        binding.tvTopFollow.isSelected = isFocus

        if (isFocus) {
            //已经关注
            binding.tvFollow.visibility = View.GONE
            binding.tvCancel.visibility = View.VISIBLE
            binding.llFollow.setBackgroundResource(R.drawable.gray_bf_btn_raduis_8_bg)
            binding.privateMessage.visible()
            binding.tvTopFollow.setIsShowLoadingWhenClick(false)
//            binding.tvTopFollow.text = "私信"
//            binding.tvTopFollow.setTextColor(resources.getColor(R.color.color_262626))
//            binding.tvTopFollow.setBackgroundResource(R.drawable.gray_7f_raduis_6_bg)

            binding.tvTopFollow.setBtnStyle(JmbonButton.BLACK_BORDER)
        } else {
            //未关注
            binding.tvFollow.visibility = View.VISIBLE
            binding.tvCancel.visibility = View.GONE
            binding.llFollow.setBackgroundResource(R.drawable.currency_btn_raduis_8_bg)
            binding.privateMessage.visibility = View.GONE
            binding.tvTopFollow.setBtnStyle(JmbonButton.GREEN_FULL)
            binding.tvTopFollow.setIsShowLoadingWhenClick(true)
//            binding.tvTopFollow.text = "关注"
//            binding.tvTopFollow.setBackgroundResource(R.drawable.currency_btn_raduis_6_bg)
//            binding.tvTopFollow.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun focusStatus2(isFocus: Boolean) {
        binding.tvTopFollow.isSelected = isFocus

        if (isFocus) {
            //已经关注
            binding.tvFollow.visibility = View.GONE
            binding.tvCancel.visibility = View.VISIBLE
            binding.llFollow.setBackgroundResource(R.drawable.gray_bf_btn_raduis_8_bg)
            binding.tvTopFollow.setBtnStyle(JmbonButton.BLACK_BORDER)
            binding.tvTopFollow.setIsShowLoadingWhenClick(false)
//            binding.tvTopFollow.text = "私信"
//            binding.tvTopFollow.setTextColor(resources.getColor(R.color.color_262626))
//            binding.tvTopFollow.setBackgroundResource(R.drawable.gray_7f_raduis_6_bg)
        } else {
            //未关注
            binding.tvFollow.visibility = View.VISIBLE
            binding.tvCancel.visibility = View.GONE
            binding.llFollow.setBackgroundResource(R.drawable.currency_btn_raduis_8_bg)
            binding.tvTopFollow.setBtnStyle(JmbonButton.GREEN_FULL)
            binding.tvTopFollow.setIsShowLoadingWhenClick(true)
//            binding.tvTopFollow.text = "关注"
//            binding.tvTopFollow.setBackgroundResource(R.drawable.currency_btn_raduis_6_bg)
//            binding.tvTopFollow.setTextColor(resources.getColor(R.color.white))
        }
    }

    private val privateWidth = SizeUtils.dp2px(70f)
    private val marginWidth = SizeUtils.dp2px(10f)

    private fun startFocusAnimator() {
        var isZeroValue = false
        val valueAnimator = ValueAnimator.ofInt(0, privateWidth).setDuration(250)
        valueAnimator.addUpdateListener { animation ->
            val width = animation.animatedValue as Int
            if (width == 0) {
                isZeroValue = true
            }
            if (isZeroValue) {
                isZeroValue = false
                return@addUpdateListener
            }
            focusAnimator(width)
        }

        valueAnimator.interpolator = EaseCubicInterpolator()
        valueAnimator.start()
    }

    var width = 0
    private fun startUnFocusAnimator() {
        var isZeroValue = false
        val valueAnimator = ValueAnimator.ofInt(privateWidth, 0).setDuration(250)
        valueAnimator.addUpdateListener { animation ->
            val width = animation.animatedValue as Int
            //Log.e("addUpdateListener2", "${width}")
            if (width == privateWidth) {
                isZeroValue = true
            }
            if (isZeroValue) {
                isZeroValue = false
                return@addUpdateListener
            }
            unfocusAnimator(width)
        }

        valueAnimator.interpolator = EaseCubicInterpolator()
        valueAnimator.start()

    }


    private fun focusAnimator(width: Int) {
        var rate = width * 1.0f / privateWidth
        val myGrad = binding.llFollow.background as GradientDrawable
        if (rate > 1) {
            rate = 1.0f
        }
        myGrad.mutate()
        myGrad.setColor(
            ColorCompute.computeColor(
                Color.parseColor("#0EA9B0"),
                Color.parseColor("#BFBFBF"),
                rate
            )
        )

        val params =
            binding.privateMessage.layoutParams as ConstraintLayout.LayoutParams
        params.width = width
        //params.rightMargin = 20f.dp()
        binding.privateMessage.layoutParams = params
        if (params.width > 0) {
            binding.privateMessage.visibility = View.VISIBLE
        }

        val params2 =
            binding.llFollow.layoutParams as ConstraintLayout.LayoutParams
        //params2.rightMargin = 10f.dp()
        params2.rightMargin = (marginWidth * rate).toInt()
        params2.leftMargin = 32f.dp()
        //margin right未计算到整体的宽度，所以减去

        params2.width = followWidth - width - (marginWidth * rate).toInt()

        binding.llFollow.layoutParams = params2

    }

    private fun unfocusAnimator(width: Int) {
        var rate = width * 1.0f / privateWidth
        val myGrad = binding.llFollow.background as GradientDrawable
        if (rate > 1) {
            rate = 1.0f
        }
        myGrad.mutate()
        myGrad.setColor(
            ColorCompute.computeColor(
                Color.parseColor("#0EA9B0"),
                Color.parseColor("#BFBFBF"),
                rate
            )
        )
        val params =
            binding.privateMessage.layoutParams as ConstraintLayout.LayoutParams
        params.width = width
        //params.rightMargin = 20f.dp()
        binding.privateMessage.layoutParams = params
        if (params.width <= 0) {
            binding.privateMessage.visibility = View.GONE
        }


        val params2 =
            binding.llFollow.layoutParams as ConstraintLayout.LayoutParams
        params2.rightMargin = (marginWidth * rate).toInt()
        //params2.rightMargin = marginWidth
        //params2.leftMargin = 32f.dp()

        //最后一次没有margin
        params2.width = followWidth2 + (privateWidth - width) + (marginWidth * (1 - rate)).toInt()
        binding.llFollow.layoutParams = params2

    }


    private fun setScrollChanged(scale: Float) {
        binding.titleBarLayout.setBackgroundColor(
            QMUIColorHelper.computeColor(
                Color.argb(0, 255, 255, 255),
                Color.WHITE,
                scale
            )
        )

        binding.ivAvatar.alpha = scale
        binding.tvName.alpha = scale

        binding.tvTopFollow.alpha = scale
        binding.tvTopFollow.visibility = if (scale < 0.1) View.INVISIBLE else View.VISIBLE
        val myGrad1 = binding.ivBack.background as GradientDrawable
        myGrad1.mutate()
        myGrad1.setColor(
            QMUIColorHelper.computeColor(
                Color.parseColor("#33000000"),
                Color.WHITE,
                scale
            )
        )
        binding.ivBack.background = myGrad1

        binding.ivBack.setImageResource(if (scale > 0.9) R.drawable.icon_back_black else R.drawable.icon_back_white)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                TagConstant.UPDATE_PICTURE_RESULT -> {
                    imageViewModel.processingImages(
                        data,
                        viewLifecycleOwner.lifecycleScope,
                        settingViewModel
                    ) { type, url ->
                        if (type == MinePhotoPreviewActivity.USER_ACCOUNT_PICTURES)
                            setAvatar(url)
                        else {
                            Constant.userInfo.background = url
                            binding.imageBackground.loadNoPlace(url)
                        }
                    }
                }
            }
        }
    }
}