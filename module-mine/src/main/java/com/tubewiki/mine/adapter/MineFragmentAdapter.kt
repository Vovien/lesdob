package com.tubewiki.mine.adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.PhoneUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.BuildConfig.MENSES_ID
import com.jmbon.middleware.arouter.MiniProgramService
import com.jmbon.middleware.bean.CircleUnReadMessage
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.UserDetailsV2
import com.tubewiki.mine.databinding.FragmentMineLayout2Binding
import com.tubewiki.mine.databinding.FragmentMineSettingBinding
import com.tubewiki.mine.databinding.FragmentMineToolsBinding
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup

class MineFragmentAdapter :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>() {

    private val miniProgram by lazy {
        ARouter.getInstance().build("/miniprogram/start/service").navigation() as MiniProgramService
    }
    private val circleAdapter by lazy { BrowseCircleAdapter() }
    private val littleToolAdapter by lazy { LittleToolAdapter() }
    private var littleToolSkeleton: RecyclerViewSkeletonScreen? = null
    private var circleSkeleton: RecyclerViewSkeletonScreen? = null

    private var holder: BaseViewHolder? = null

    init {
        addItemType(1, R.layout.fragment_mine_layout2)
        addItemType(2, R.layout.fragment_mine_tools)
        addItemType(3, R.layout.fragment_mine_setting)
    }

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseViewHolder {
        val viewHolder = super.createBaseViewHolder(parent, layoutResId)
        return when (layoutResId) {
            R.layout.fragment_mine_layout2 -> viewHolder.withBinding(
                FragmentMineLayout2Binding::bind
            )
            R.layout.fragment_mine_tools -> viewHolder.withBinding(
                FragmentMineToolsBinding::bind
            )
            R.layout.fragment_mine_setting -> viewHolder.withBinding(
                FragmentMineSettingBinding::bind
            )

            else -> throw IllegalStateException()
        }
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {

        when (item.itemType) {
            1 -> {
                holder.getViewBinding<FragmentMineLayout2Binding>()
                    .apply {
                        //val headData = item as AskItemType1<Question>
                        this@MineFragmentAdapter.holder = holder
                        initViewData()
                    }
            }
            2 -> {
                holder.getViewBinding<FragmentMineToolsBinding>()
                    .apply {
                        toolsList.init(
                            littleToolAdapter,
                            GridLayoutManager(toolsList.context, 4)
                        )

                        toolsList.layoutManager =
                            GridLayoutManager(toolsList.context, 4)
                        littleToolSkeleton = Skeleton.bind(toolsList).adapter(littleToolAdapter)
                            .load(R.layout.item_skeleton_mine_litter_tools_list)
                            .shimmer(true)
                            .angle(20)
                            .duration(1000)
                            .maskWidth(1f)
                            .count(4)
                            .color(R.color.white_B3)
                            .frozen(true)
                            .show()


                        littleToolAdapter.setOnItemClickListener { adapter, view, position ->
                            Action {
                                miniProgram.startMiniProgram(littleToolAdapter.data[position].toolId,littleToolAdapter.data[position].toolType)
                            }.logInToIntercept()
                        }

                    }
            }
            3 -> {
                holder.getViewBinding<FragmentMineSettingBinding>()
                    .apply {

                        textSetting.setOnSingleClickListener({
                            ARouter.getInstance().build("/mine/setting/activity")
                                .jumpToActivity()
                        })
                        // 客服
                        textCustomer.setOnSingleClickListener({
                            showCallDialog()
                        })
                        // 帮助
                        textHelp.setOnSingleClickListener({
                            ARouter.getInstance().build("/webview/activity")
                                .withString("url", "https://m.jmbon.com/helpcenter")
                                .withString("title", context.getString(R.string.mine_help_center))
                                .navigation()
                        })
                        //一键反馈
                        textFeedback.setOnSingleClickListener({
                            ARouter.getInstance().build("/mine/setting/feedback")
                                .navigation()
                        })
                    }
            }
        }
    }

//    override fun convert(holder: BindingMultiItemQuickAdapter.BaseBindingHolder, item: LittleTool) {
//        holder.getViewBinding<FragmentMineLayout2Binding>().apply {
//
//
//        }
//    }

    private fun FragmentMineLayout2Binding.initViewData() {
        if (Constant.isLogin) {
            setUserInfo(Constant.userInfo)
        } else {
            setUnLoginUI()
        }

        toUserPage.setOnSingleClickListener({
            ARouter.getInstance().build("/mine/message/personal_page")
                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
                .navigation()
        })
        textMore.setOnClickListener {
            ARouter.getInstance().build("/mine/message/personal_page")
                .jumpToActivity()
        }


        val manager = LinearLayoutManager(circleList.context)
        manager.orientation = LinearLayoutManager.HORIZONTAL

        circleList.init(circleAdapter, manager, dividerHeight = 24f)

        circleAdapter.setOnItemClickListener { adapter, view, position ->
            val group = circleAdapter.data[position]
            if (group.isJoin) {
                ARouter.getInstance().build("/chat/circle/circle_detail")
                    .withString("groupName", group.name)
                    .withString("groupNumber", group.number)
                    .withString("groupId", "${group.id}").jumpToActivity()
            } else {
                //退出群没有加群时进入群详情页面
                ARouter.getInstance().build("/chat/group/info")
                    .withString("groupName", group.name)
                    .withString("circleId", "${group.circleId}")
                    .withString("groupId", "${group.id}").jumpToActivity()
            }
        }

//        littleToolAdapter.setOnItemClickListener { adapter, view, position ->
//            Action {
//                miniProgram.startMiniProgram(littleToolAdapter.data[position].toolId)
//            }.logInToIntercept()
//        }


        function.apply {
            // 圈子
            textCircle.setOnSingleClickListener({
                ARouter.getInstance().build("/group/main/activity")
                    .withInt(TagConstant.PARAMS, groupMsgCount)
                    .withInt(TagConstant.PARAMS2, otherMsg + groupMsgCount)
                    .jumpToActivity()
            })
            // 收藏
            textCollect.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/collect/activity")
                    .jumpToActivity()

            })
            // 草稿箱
            textDraft.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/draft/activity")
                    .jumpToActivity()

            })
            // 抵扣劵
            textJuanpizk.setOnSingleClickListener({
                //抵扣券跳转
                ARouter.getInstance().build("/coupon/main")
                    .jumpToActivity()
            })
            // 钱包
            textWallet.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/wallet")
                    .jumpToActivity()
            })
        }


        userFunction.apply {
            llAnswer.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt("index", 0)
                    .jumpToActivity()

            })
            llArticle.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt("index", 2)
                    .jumpToActivity()
            })
            llFollow.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/follow")
                    .jumpToActivity()
            })
            llQa.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt("index", 1)
                    .jumpToActivity()
            })
            llFans.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/fans")
                    .jumpToActivity()
            })
        }


        textMoreCircle.setOnSingleClickListener({
            ARouter.getInstance().build("/mine/recent/browse/circle")
                .jumpToActivity()
        })

        mensesTable.mensesRoot.setOnSingleClickListener({
            miniProgram.startMiniProgram(BuildConfig.MENSES_ID)
        })
//        setting.apply {
//            textSetting.setOnSingleClickListener({
//                ARouter.getInstance().build("/mine/setting/activity")
//                    .jumpToActivity()
//            })
//            // 客服
//            textCustomer.setOnSingleClickListener({
//                showCallDialog()
//            })
//            // 帮助
//            textHelp.setOnSingleClickListener({
//                ARouter.getInstance().build("/webview/activity")
//                    .withString("url", "https://m.jmbon.com/helpcenter")
//                    .withString("title", context.getString(R.string.mine_help_center))
//                    .navigation()
//            })
//            //一键反馈
//            textFeedback.setOnSingleClickListener({
//                ARouter.getInstance().build("/webview/activity")
//                    .withString("url", "https://m.jmbon.com/feedback")
//                    .withString("title", context.getString(R.string.mine_feedback))
//                    .navigation()
//            })
//        }

        // 小工具
        //initLittleToolSkeleton()
        initCircleSkeleton(manager)

    }


    private fun initLittleToolSkeleton() {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
//            toolsList.layoutManager =
//                GridLayoutManager(toolsList.context, 4)
//            littleToolSkeleton = Skeleton.bind(toolsList).adapter(littleToolAdapter)
//                .load(R.layout.item_skeleton_mine_litter_tools_list)
//                .shimmer(true)
//                .angle(20)
//                .duration(1000)
//                .maskWidth(1f)
//                .count(4)
//                .color(R.color.white_B3)
//                .frozen(true)
//                .show()
        }
    }

    private fun initCircleSkeleton(manager: RecyclerView.LayoutManager) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            circleList.layoutManager = manager
            circleSkeleton = Skeleton.bind(circleList).adapter(circleAdapter)
                .load(R.layout.item_skeleton_mine_circle_list)
                .shimmer(true)
                .angle(20)
                .duration(1000)
                .maskWidth(1f)
                .count(12)
                .color(R.color.white_B3)
                .frozen(false)
                .show()
        }
    }

    var isFirst = true
    var groupMsgCount = 0
    var otherMsg = 0
    fun getUserDetailV2(result: ResultThreeData<UserDetailsV2, Int, CircleUnReadMessage>) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            if (isFirst) {
                if (result.data1.circles.isNotNullEmpty()) {
                    if (!llCircle.isVisible()) {
                        ExpandCosAnimUtil.expand(llCircle)
                    }
                    circleAdapter.data.clear()
                    circleAdapter.addData(result.data1.circles)
                    circleList.scrollToPosition(0)
                    circleSkeleton?.hide()

                } else {
                    circleSkeleton?.hide()
                    //binding.llCircle.gone()
                    ExpandCosAnimUtil.collapse(llCircle)
                    circleAdapter.data.clear()
                    circleAdapter.notifyDataSetChanged()
                }

                littleToolAdapter.data.clear()
                littleToolAdapter.addData(result.data1.tools)
                littleToolSkeleton?.hide()

//                //设置姨妈期提示
                loadMensesView(result)
                isFirst = false
            }

            // 更新 User
            if (Constant.isLogin) {
                result.data1.user?.apply {
                    val userInfo = Constant.user
                    userInfo.user.avatarFile = avatarFile
                    userInfo.user.userName = userName
                    userInfo.user.doctorName = doctorName
                    userInfo.user.verifyType = verifyType
                    userInfo.user.pregnantStatus = pregnantStatus
                    userInfo.user.jobType = jobType
                    userInfo.user.categoryTitle = categoryTitle
                    // circle user
                    Constant.circleUser?.user?.avatarFile = avatarFile
                    Constant.circleUser?.user?.userName = userName
                    Constant.circleUser?.user?.name = doctorName
                    Constant.circleUser?.user?.verifyType = verifyType
                    Constant.saveUser(userInfo)
//                    userFunction.let {
//                        if (answerCount >= 0)
//                            it.tvAnswerNum.text = "$answerCount"
//                        if (questionCount >= 0)
//                            it.tvQaNum.text = "$questionCount"
//                        if (focusCount >= 0)
//                            it.tvFollowNum.text = "$focusCount"
//                        if (fansCount >= 0)
//                            it.tvFansNum.text = "$fansCount"
//                        if (hasArticle) {
//                            userFunction.llArticle.visibility = View.VISIBLE
//                            it.tvArticleNum.text = "$articleCount"
//                        } else it.llArticle.gone()
//                    }
                }

                setUserInfo(Constant.userInfo)

                // 消息数
                otherMsg = result.data2
                //消息总数
            }


          //  setMsgCount(otherMsg + groupMsgCount)

        }
    }


    private fun loadMensesView(result: ResultThreeData<UserDetailsV2, Int, CircleUnReadMessage>) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            result.data1.menstrual?.apply {

                //类型【1：姨妈期，2：距离姨妈期还有多少天，3：易孕期与排卵期，4：未设置经期（不显示）】
                if (type == 4) {
                    //未设置姨妈期
                    ExpandCosAnimUtil.collapse(mensesTable.mensesRoot, 98f.dp())
                } else {
                    if (!mensesTable.mensesRoot.isVisible()) {
                        ExpandCosAnimUtil.expand(mensesTable.mensesRoot, 98f.dp())
                    }
                    //更新数据
                    mensesTable.tvTitle.text = title
                    mensesTable.tvDesc.text = sub_title
                    when (type) {
                        1 -> {
                            updateMensesColor(
                                R.drawable.color_menses_ing_bg,
                                R.drawable.icon_menses_more_white,
                                com.jmbon.resources.R.color.white
                            )
                        }
                        2 -> {
                            updateMensesColor(
                                R.drawable.color_menses_pre_bg,
                                R.drawable.icon_menses_more_pink,
                                com.jmbon.resources.R.color.pink
                            )
                        }
                        3 -> {
                            updateMensesColor(
                                R.drawable.color_menses_end_bg,
                                R.drawable.icon_menses_more_white,
                                com.jmbon.resources.R.color.white
                            )

                        }
                    }
                }


            } ?: apply {
                ExpandCosAnimUtil.collapse(mensesTable.mensesRoot)
            }
        }
    }

    private fun updateMensesColor(bgDrawable: Int, iconId: Int, textColor: Int) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            if (mensesTable.mensesLayout.background == null) {
                mensesTable.mensesLayout.setBackgroundResource(bgDrawable)
            } else {
                val data = arrayOf<Drawable>(
                    mensesTable.mensesLayout.background,
                    context.resources.getDrawable(bgDrawable)
                )
                val td = TransitionDrawable(data)
                mensesTable.mensesLayout.setBackgroundDrawable(td)
                td.startTransition(300)
            }
            mensesTable.ivMore.setImageResource(iconId)
            mensesTable.tvDesc.setTextColor(context.getColor(textColor))
            mensesTable.tvTitle.setTextColor(context.getColor(textColor))
        }
    }

    fun setMsgCount(count: Int) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            if (!Constant.isLogin) {
                return
            }
            function.tvCountNum.text = if (count > 99) "99+" else "$count"
            // 圈子通知
            function.tvCountNum.visibility =
                if (count == 0) View.GONE else View.VISIBLE
        }
    }


    private fun setUserInfo(user: UserData.User?) {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            if (user != null) {
                imageAvatar.loadCircle(user.avatarFile, R.drawable.icon_default_login_avatar)
                textName.text =
                    if (user.verifyType == 2 && user.doctorName.isNotNullEmpty()) user.doctorName else user.userName

                textMore.text = context.getString(R.string.personal_home_page)
                //状态 0：未设置，1：备孕中，2：已怀孕，3：有宝宝
                // binding.viewTitleBar.textStatus.text = Constant.getDefaultPregnantStatus()
                circleList.visible()
                if (user.categoryTitle.isEmpty())
                    textSub.gone()
                else {
                    textSub.visible()
                    textSub.text = user.categoryTitle
                }

                if (user.verifyType == 2) {
                    //医生
                    ivFlag.visible()
                    ivFlag.setImageResource(R.drawable.icon_doctor_flag)
                } else {
                    when (user.jobType) {
                        2 -> {
                            ivFlag.visible()
                            ivFlag.setImageResource(R.drawable.icon_good_reply_flag)
                        }
                        3 -> {
                            ivFlag.visible()
                            ivFlag.setImageResource(R.drawable.icon_good_creator_flag)
                        }
                        else -> {
                            ivFlag.gone()
                        }
                    }
                }
            }
        }
    }


    private fun setUnLoginUI() {
        holder!!.getViewBinding<FragmentMineLayout2Binding>().apply {
            ExpandCosAnimUtil.collapse(llCircle)
            // llCircle.gone()
            imageAvatar.loadCircle(
                R.drawable.icon_login_out_user,
                R.drawable.icon_default_login_avatar
            )
            textName.text = "登录/注册"
            textMore.text = context.getString(R.string.my_home_page)
            //状态 0：未设置，1：备孕中，2：已怀孕，3：有宝宝
            // binding.viewTitleBar.textStatus.text = Constant.getDefaultPregnantStatus()
            function.tvCountNum.gone()
            textSub.visible()
            textSub.visible()

            userFunction.tvAnswerNum.text = "-"
            userFunction.tvQaNum.text = "-"
            userFunction.tvArticleNum.text = "-"
            userFunction.tvFansNum.text = "-"
            userFunction.tvFollowNum.text = "-"
            ivFlag.gone()
        }
    }

    private fun showCallDialog() {
        val listData = arrayListOf(
            CustomDialogTypeBean(
                context.getString(R.string.call_kefu),
                CustomDialogTypeBean.TYPE_TITLE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                context.getString(R.string.call_kefu_phone),
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                context.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(context)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(context, listData) { select ->
                    if (select == 1)
                        PhoneUtils.dial(context.getString(R.string.call_kefu_phone))
                }
            ).show()
    }

    fun loginEvent(event: UserLoginEvent) {
        circleAdapter.data.clear()
        circleAdapter.notifyDataSetChanged()
        if (event.login) {
            circleSkeleton?.show()
            setUserInfo(Constant.userInfo)
        } else {
            Constant.circleUser = null
            setUnLoginUI()
            circleAdapter.data.clear()
        }
    }

    private fun Postcard.jumpToActivity() {
        Action {
            this.navigation()
        }.logInToIntercept()
    }
}