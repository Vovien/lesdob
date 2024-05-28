package com.tubewiki.hospital.activity

import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.bean.WebScrollOffset
import com.jmbon.middleware.bury.BuryHelper
import com.jmbon.middleware.bury.event.ClickEventEnum
import com.jmbon.middleware.utils.*
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.widget.html.X5WebView
import com.jmbon.widget.picture.SelectPhotoUtils
import com.jmbon.widget.tablayout.listener.OnTabSelectListener
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DoubleUtils
import com.lxj.xpopup.XPopup
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tubewiki.hospital.R
import com.tubewiki.hospital.adapter.*
import com.tubewiki.hospital.bean.HospitalDetailBean
import com.tubewiki.hospital.databinding.ActivityHospitalDetailBinding
import com.tubewiki.hospital.dialog.HospitalDoctorTitleListDialog
import com.tubewiki.hospital.dialog.MoreDetailDialog
import com.tubewiki.hospital.viewmodel.HospitalDoctorViewModel
import kotlin.math.abs

/**
 * 医生主页
 */
@Route(path = "/hospital/activity/hospital_detail")
class HospitalDetailActivity :
    ViewModelActivity<HospitalDoctorViewModel, ActivityHospitalDetailBinding>() {
    @Autowired(name = TagConstant.HOSPITAL_ID)
    @JvmField
    var hospitalId = 0

    private val hospitalImageAdapter by lazy {
        HospitalImageAdapter()
    }
    private val hospitalTechAdapter by lazy {
        HospitalTechAdapter()
    }
    private val hospitalLeaderAdapter by lazy {
        HospitalLeaderAdapter()
    }
    private val hospitalDoctorAdapter by lazy {
        HospitalDoctorAdapter()
    }

    private var x5WebView: X5WebView? = null
    var webScrollY = 0

    var titles = mutableListOf<String>()
    var titleTitleOffset = mutableListOf<WebScrollOffset>()
    var selectedPos = 0 //目录选择

    var offset = 44f.dp()
    var isHasMoreDoctor = true

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        initPageState()

        initWebView()

        iniRecyclerView()

        initListener()


        MobAnalysisUtils.mInstance.sendEvent(
            UMEventKey.Event_HospitalDetails_enter,
            mutableMapOf("Hospital_id" to "${hospitalId}")
        )
    }

    private fun initTabView() {

        binding.commonTabLayout.setTitle(titles.toTypedArray())
        binding.commonTabLayout.setSnapOnTabClick(true)

        binding.commonTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                Log.e("onTabSelect", "${position}")
                selectedPos = position
                when (position) {
                    0 -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.expandScroll,
                        offset
                    )
                    1 -> binding.scrollerLayout.scrollToChildWithOffset(binding.llTech, offset)
                    2 -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.tvHospitalDesc,
                        offset
                    )

                    (titleTitleOffset.size - 1) -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.tvTeam,
                        offset
                    )

                    else -> binding.scrollerLayout.scrollToChildWithOffset(
                        x5WebView,
                        0 - SizeUtils.dp2px((titleTitleOffset[position].offsetTop) - 24.toFloat())

                    )
                }

                binding.commonTabLayout.updateTabSelection(position)
                binding.commonTabLayout.scrollToCurrentTab()
                binding.commonTabLayout.invalidate()
            }

            override fun onTabReselect(position: Int) {
            }
        })

    }

    private fun initListener() {

        var isShowIng = false
        var isHideIng = false

        binding?.apply {
            binding.scrollerLayout.setSmoothScrollRate(300)
            binding.scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
                var rate =
                    abs(scrollY).toFloat() / 100f.dp()
                webScrollY = scrollY - x5WebView?.top!!
                var expandScrollTop = scrollY - expandScroll?.top!!


                autoScrollToTitle(scrollY)


                Log.e(
                    "TOP",
                    "${expandScrollTop},${expandScroll?.top},${llTech?.top},${scrollY}"
                )
                if (scrollY < 100) {
                    rate = 0f
                }
                titleBarView.centerTextView.alpha = rate

                if (rate > 0.8 && !commonTabLayout.isVisible() && binding.commonTabLayout.tabCount > 0) {
                    if (isShowIng || isHideIng) {
                        return@setOnVerticalScrollChangeListener
                    }
                    isShowIng = true

                    //执行动画
                    binding.commonTabLayout.visible()

                    var valueAnimator = ValueAnimator.ofFloat(-44f.dp().toFloat(), 0f)
                    valueAnimator.addUpdateListener {
                        var value = it.animatedValue as Float
                        binding.commonTabLayout.translationY = value
                    }
                    valueAnimator.addListener(onEnd = {
                        isShowIng = false
                    })
                    valueAnimator.duration = 200
                    valueAnimator.start()
                }


                //Log.e("TAG", "${rate},${commonTabLayout.isVisible()}")
                if (rate < 0.7 && commonTabLayout.isVisible() && binding.commonTabLayout.tabCount > 0) {

                    if (DoubleUtils.isFastDoubleClick()) {
                        return@setOnVerticalScrollChangeListener
                    }
                    if (isShowIng || isHideIng) {
                        return@setOnVerticalScrollChangeListener
                    }
                    isHideIng = true

                    //执行动画
                    var valueAnimator = ValueAnimator.ofFloat(0f, -44f.dp().toFloat())
                    valueAnimator.addUpdateListener {
                        binding.commonTabLayout.translationY = it.animatedValue as Float
                    }
                    valueAnimator.addListener(onEnd = {
                        isHideIng = false
                    })
                    valueAnimator.duration = 200
                    valueAnimator.start()

                    binding.commonTabLayout.postDelayed({
                        binding.commonTabLayout.gone()
                    }, 250)

                }


            }

            flCustomScheme.setOnSingleClickListener({
                toWxMiniProgram(ActivityUtils.getTopActivity(),5)
                BuryHelper.addEvent(ClickEventEnum.EVENT_HOSPITAL_DETAIL_BOTTOM_RESERVE_HOSPITAL.value)
            })


            tvCopy.setOnSingleClickListener({
                copy(tvAddress.text.toString())
                "复制成功".showToast()
                MobAnalysisUtils.mInstance.sendEvent(UMEventKey.Event_CopyAddress_click)

            })

            llList.setOnSingleClickListener({
                showTitleListDilaog()
            })
            llPhone.setOnSingleClickListener({
                // PhoneUtils.dial(tvPhone.text.toString())
                //弹窗dialog
                showCallDialog(tvPhone.text.toString())
            })

            tvMoreDoctor.setOnSingleClickListener({
                ARouter.getInstance().build("/hospital/activity/all_hospital_doctor")
                    .withInt(TagConstant.HOSPITAL_ID, hospitalId).navigation()
            })



            hospitalImageAdapter.setOnItemClickListener { adapter, view, pos ->
                val list = arrayListOf<LocalMedia>()
                hospitalImageAdapter.data.forEach {
                    if (it.isNotEmpty()) {
                        val media = LocalMedia(it, 0, 0, "")
                        list.add(media)
                    }
                }
                SelectPhotoUtils.externalPicturePreview(ActivityUtils.getTopActivity(), pos, list)
            }

            hospitalTechAdapter.setOnItemClickListener { adapter, view, pos ->
                ArouterUtils.toArticleDetailsActivity(hospitalTechAdapter.getItem(pos).articleId)
            }
            hospitalDoctorAdapter.setOnItemClickListener { adapter, view, pos ->
                ArouterUtils.toDoctorDetailsActivity(hospitalDoctorAdapter.getItem(pos).doctorId)
            }
            hospitalLeaderAdapter.setOnItemClickListener { adapter, view, pos ->
                ArouterUtils.toDoctorDetailsActivity(hospitalLeaderAdapter.getItem(pos).doctorId)
            }

        }

    }

    /**
     * 自动确定滚动位置的title
     */
    private fun autoScrollToTitle(scrollY: Int) {
        var recyclerDoctorRect = Rect()
        binding.recyclerDoctor?.getLocalVisibleRect(recyclerDoctorRect)

        if (scrollY < 500) {
            if (selectedPos == 0) {
                return
            }
            selectedPos = 0
            updateTab()
        } else if (scrollY < 1800) {
            if (selectedPos == 1) {
                return
            }
            selectedPos = 1
            updateTab()
        } else if (recyclerDoctorRect.top < 100 && recyclerDoctorRect.bottom > 150) {
            if (selectedPos == titleTitleOffset.size - 1) {
                return
            }
            selectedPos = titleTitleOffset.size - 1
            updateTab()
        } else {
            var scrollY2 = SizeUtils.px2dp(webScrollY.toFloat())

            for (i in 2 until titleTitleOffset.size - 1) {
                if (titleTitleOffset[i].offsetTop >= scrollY2) {
                    if (selectedPos == i) {
                        return
                    }
                    Log.e("until", "${selectedPos},${titleTitleOffset[i].offsetTop},${scrollY2}")
                    selectedPos = i
                    updateTab()
                    break
                }
            }
        }


    }

    fun updateTab() {
        binding.commonTabLayout.updateTabSelection(selectedPos)
        binding.commonTabLayout.scrollToCurrentTab()
        binding.commonTabLayout.invalidate()
    }


    private fun copy(message: String) {
        //获取剪贴板管理器：
        val cm: ClipboardManager? =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", message)
        // 将ClipData内容放到系统剪贴板里。
        cm?.setPrimaryClip(mClipData)
    }

    private fun iniRecyclerView() {
        binding?.apply {
            recyclerImage.init(
                hospitalImageAdapter,
                layoutManager = LinearLayoutManager(
                    this@HospitalDetailActivity,
                    RecyclerView.HORIZONTAL,
                    false
                ),
                8f
            )
            recycleTech.init(
                hospitalTechAdapter,
                layoutManager = LinearLayoutManager(
                    this@HospitalDetailActivity,
                    RecyclerView.HORIZONTAL,
                    false
                ),
                16f
            )
            recyclerLeader.init(
                hospitalLeaderAdapter,
                layoutManager = LinearLayoutManager(
                    this@HospitalDetailActivity,
                    RecyclerView.VERTICAL,
                    false
                )
            )
            recyclerDoctor.init(
                hospitalDoctorAdapter,
                layoutManager = LinearLayoutManager(
                    this@HospitalDetailActivity,
                    RecyclerView.VERTICAL,
                    false
                )

            )
        }

    }

    override fun initData() {
        started {

            viewModel.hospitalDetail(hospitalId).netCatch {
                showErrorState()

            }.next {
                showContentState()

                loadViewData(this)
                loadH5(hospital.content)

            }

        }

    }


    private fun loadViewData(hospitalDetailBean: HospitalDetailBean) {
        binding?.apply {
            titleBarView.centerTextView.alpha = 0f
            titleBarView.centerTextView.isSingleLine = true
            titleBarView.centerTextView.ellipsize = TextUtils.TruncateAt.END
            setTitleName(hospitalDetailBean.hospital.hospitalName)

            tvTitle.text = hospitalDetailBean.hospital.hospitalName
            ivCover.loadRadius(hospitalDetailBean.hospital.logo, 8f.dp())
            SpanUtils.with(tvSimpleName).append("简称  ")
                .append(hospitalDetailBean.hospital.hospitalShortName).setForegroundColor(
                    resources.getColor(R.color.color_262626)
                ).setBold().create()

            if (hospitalDetailBean.hospital.hospitalDescription.isNotNullEmpty()) {
                // 设置TextView可展示的宽度 ( 父控件宽度 - 左右margin - 左右padding）
                val whidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)
                textIntroduction.initWidth(whidth)
                textIntroduction.setCloseText(hospitalDetailBean.hospital.hospitalDescription)

                textIntroduction.setOnClickListener {
                    // 点击副文案

                    if (!textIntroduction.isShowAll) {
                        return@setOnClickListener
                    }

                    showMoreHospitalInfoDialog(hospitalDetailBean)
                }

            } else expandScroll.visibility = View.GONE

            hospitalImageAdapter.setNewInstance(hospitalDetailBean.hospital.images)
            hospitalTechAdapter.setNewInstance(hospitalDetailBean.hospital.technologyNames)
            hospitalLeaderAdapter.setNewInstance(hospitalDetailBean.hospital.leaders)

            hospitalDoctorAdapter.setNewInstance(hospitalDetailBean.doctors)

            if (hospitalDetailBean.doctors.isNullOrEmpty()) {
                isHasMoreDoctor = false
                binding.recyclerDoctor.gone()
                binding.tvTeam.gone()
                binding.flMoreDoctor.gone()
            } else {
                isHasMoreDoctor = true
                binding.recyclerDoctor.visible()
                binding.tvTeam.visible()
                binding.flMoreDoctor.visible()
            }


            if (hospitalDetailBean.hospital.leaders.isNullOrEmpty()) {
                binding.recyclerLeader.gone()
                binding.tvLeaderTeam.gone()
            } else {
                binding.recyclerLeader.visible()
                binding.tvLeaderTeam.visible()
            }


            binding.recyclerImage.visibility =
                if (hospitalDetailBean.hospital.images.isNullOrEmpty()) View.GONE else View.VISIBLE

            tvPhone.text = hospitalDetailBean.hospital.phone
            tvAddress.text = hospitalDetailBean.hospital.address

            // 圈子参考资料
            if (hospitalDetailBean.hospital.infos.isNotNullEmpty()) {
                infoLayout.visibility = View.VISIBLE
                createInfoItem(hospitalDetailBean.hospital.infos, llInfo)
            } else {
                infoLayout.visibility = View.GONE
            }

        }

    }


    private fun initWebView() {
        x5WebView = X5WebView(this)
//        binding.content.wevContent.addView(x5WebView)
        //  binding.flWeb.addView(x5WebView)

        binding.scrollerLayout.addView(
            x5WebView,
            binding.scrollerLayout.indexOfChild(binding.flWeb)
        )
        x5WebView!!.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        x5WebView!!.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        x5WebView?.apply {
            settings?.loadWithOverviewMode = true
            settings?.allowContentAccess = true
            addJavascriptInterface(JsImageLoad(this@HospitalDetailActivity), "imageLoader")
            addJavascriptInterface(this@HospitalDetailActivity, "android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    x5WebView?.loadUrl("javascript:window.android.resize(document.body.getBoundingClientRect().height)")
                    super.onPageFinished(p0, p1)
                    if (!this@HospitalDetailActivity.isFinishing) {
                        loadDataWhenFinish()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    request?.url.toString().let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }
            }
        }


    }

    private fun createInfoItem(
        infoList: MutableList<HospitalDetailBean.Info>,
        content: LinearLayout
    ) {
        val textParams =
            LinearLayout.LayoutParams(90f.dp(), LinearLayout.LayoutParams.WRAP_CONTENT)
        val textParams2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        textParams2.weight = 1.0f
        infoList.forEach {
            val itemView = LinearLayout(this)
            itemView.orientation = LinearLayout.HORIZONTAL
//            val startText = createTextView(it.key?:"")
            val startText = createTextView(it.key ?: "")
            startText.setPadding(0, 16f.dp(), 10f.dp(), 16f.dp())
            startText.layoutParams = textParams
            itemView.addView(startText)
            val endText = createTextView(it.value ?: "")
            endText.setPadding(16f.dp(), 16f.dp(), 0, 16f.dp())
            endText.layoutParams = textParams2
            endText.setTextColor(R.color.color_262626.Color)
            itemView.addView(endText)

            content.addView(itemView)
        }
    }

    private fun createTextView(str: String): TextView {
        val text = TextView(this)
        text.apply {
            isSingleLine = false
            setText(str)
            setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
            textSize = 16f
        }
        return text
    }


    private fun X5WebView.loadDataWhenFinish() {

        var webTop = x5WebView?.top ?: 0
        titles.add("医院简介")
        titles.add("准入技术")
        titles.add("医院概况")

        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.expandScroll.top,
                allOffsetTop = binding.expandScroll.top,
                title = "医院简介"
            )
        )
        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.llTech.top,
                allOffsetTop = binding.llTech.top,
                title = "准入技术"
            )
        )
        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.tvHospitalDesc.top,
                allOffsetTop = binding.tvHospitalDesc.top,
                title = "医院概况"
            )
        )

        if (isHasMoreDoctor) {
            titles.add("专家团队")

            titleTitleOffset.add(
                WebScrollOffset(
                    offsetTop = binding.tvTeam.top,
                    allOffsetTop = binding.tvTeam.top,
                    title = "专家团队"
                )
            )
        }

        resumed {
            evaluateJavascript("javascript:getHospitalTag()") {
                val list = GsonUtils.fromJson<ArrayList<WebScrollOffset>>(
                    it,
                    GsonUtils.getListType(WebScrollOffset::class.java)
                )
                if (list.isNullOrEmpty()) {
                    initTabView()
                    return@evaluateJavascript
                }
                // titleTitleOffset.addAll(2, list)

                list.forEach {
                    it.allOffsetTop = webTop + SizeUtils.dp2px(it.offsetTop.toFloat())
                    titleTitleOffset.add(titleTitleOffset.size - 1, it)
                }

                list.forEach {
                    titles.add(titles.size - 1, it.title)
                }

                initTabView()

                Log.e("TAG", list.toString())
            }
            showContentState()
        }

    }

    @JavascriptInterface
    fun resize(height: Float) {
        ThreadUtils.runOnUiThread {

            val layoutParams: ViewGroup.LayoutParams = x5WebView!!.layoutParams
            layoutParams.height = (height * ScreenUtils.getScreenDensity()).toInt()
            Log.e("resize", "${height},${layoutParams.height}")
            x5WebView!!.layoutParams = layoutParams
            binding.scrollerLayout.checkLayoutChange()
        }
    }

    private fun loadH5(h5Str: String) {
        var h5 =
            "<h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">历史发展</span><br></h3><p>成都中医药大学附属生殖妇幼医院前身为1985年成立的四川省计划生育科研所附属医院。</p><p>2009年11月，由四川省卫生厅批准更名为成都中医药大学第二附属医院/四川生殖卫生医院。</p><p>2018年3月，由四川省卫生和计划生育委员会批准更名为“成都中医药大学附属生殖妇幼医院/四川生殖卫生医院。</p><h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">研究成果</span></h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">科室人员曾获得国家三等发明奖、部省级及省部级科技成果进步奖、四川省人民政府科技进步三等奖等共22次，承担科研24项(其中国家级5项)，合编出版了《男性性功能障碍》、《男科学》、《实用男科学》、《现代男性不育诊疗学》、《中国慢性前列腺炎中西医结合诊疗指南》、《中国男性不育证中西医结合诊疗指南》等专著，发表学术论文150多篇。<br></span></p><h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">临床教学与研究机构</span><br></h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\"><span style=\\\"color: rgb(0, 0, 0);\\\">第二临床医学院与成都中医药大学附属第五人民医院实行院院合一管理。</span><br></span></p><h2>重点专科</h2><h3>科室设置</h3><p>开设有生殖医学中心(丈夫精液人工受精技术、体外受精—胚胎移植技术及其衍生技术)、妇科、外科、内科、中医科、中医儿科、中西医结合科、皮肤科、口腔科、中医耳鼻喉科、麻醉科、医学影像科和医学检验科、药剂科等。</p><h3>特色专科</h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 16px;\\\">成都中医药大学附属生殖妇幼医院以辅助生殖技术为特色。</span></p><h2>就医指南</h2><h3>门诊时间</h3><p>周一至周五： 门诊时间8:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:30。</p><p></p><p>周末及节假日： 门诊时间9:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:00。</p><h3>急诊时间</h3><p style=\\\"margin-left: 0px;\\\">全天24小时应诊。" +
                    "</p><h3>乘车路线</h3><p>周边交通</p><p>1、成都双流国际机场：</p><p>双流国际机场(t1航站楼)站搭乘机场专线1号线至倪家桥站下车，全程约为14.9公里。</p><p>2、成都成都站：</p><p>火车北站搭乘地铁1号线至倪家桥站下车，全程约为8.6公里。</p><p>3、市内路线：</p><p>市内搭乘地铁1号线至倪家桥站或搭乘118路、16路、501路、63路、99路、G83路、机场专线1号线等公交车至倪家桥站下车均可到达目的地。</p><h3>就诊指南</h3><p>1、为避免人群聚集，消除交叉感染的隐患，实行限号就诊，暂时取消现场挂号，提供医院微信公众号线上挂号及就医160电话预约挂号。若挂号平台显示号满，则表示该医生当日停止挂号，为减少聚集，请您按序号来院，有序就诊。</p><p>2、有我院就诊卡的患者推荐微信公众号线上挂号。预约挂号时间将于清明节后逐步恢复至可预约未来7天内的门诊，具体挂号信息以公众号挂号界面为准，每天线上挂号时间段为早上7:30到晚上23:00，除此之外的时间将不提供挂号服务。</p><p>3、无我院就诊卡患者推荐就医160电话预约挂号(400-119-1160)，目前限提前1天进行挂号，仅为预挂号，就诊优先顺序在微信公众号挂号者之后。就诊当日请携带本人身份证至窗口办卡取号。</p><p><span style=\\\"color: rgb(51, 51, 51); font-size: 15px;\\\">4、门诊时间</span><br></p><p>周一至周五： 门诊时间8:00~12:00; 消毒时间12:00~14:00;门诊时间14:00~17:30。</p><p>周末及节假日： 门诊时间9:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:00。</p><p>非本院工作人员请勿在消毒时间前往我院就医或逗留。</p><p>5、支付成功后需等待微信反馈信息，若消息提示成功方可认为挂号成功。挂号成功后，无需取号，直接到科室就诊，若失败，费用会在7个工作日内原路退回，若没有消息返回需等待确认。</p><p>6、通过微信预约的患者可通过微信端口退号。退号必须在就诊前一日23：00前完成，之后将不允许退号。7天内仅允许退号3次，30天内仅允许退号7次。</p><h2>预约咨询</h2><p style=\\\"margin-left: 0px;\\\">一、微信预约</p><p style=\\\"margin-left: 0px;\\\"><span style=\\\"color: rgb(0, 0, 0);\\\">成都中医药大学附属生殖妇幼医院中预约。</span><br></p><p style=\\\"margin-left: 0px;\\\">二、电话预约</p><p style=\\\"margin-left: 0px;\\\">医院电话预约挂号系统：<span style=\\\"color: rgb(51, 51, 51);\\\">160电话预约挂号(400-119-1160)。</span></p><p style=\\\"margin-left: 0px;\\\">三、现场预约</p><p style=\\\"margin-left: 0px;\\\">在门诊一楼大厅导医台进行预约挂号。</p><p></p>"


        var data = HospitalDoctorHtmlTools.createHospitalHtml(h5Str)

        //  LogUtils.e("TAG", "${data}")

        x5WebView?.loadDataWithBaseURL(
            BuildConfig.H5_URL,
            data,
            "text/html",
            "utf-8",
            null
        )
        // x5WebView?.loadDataWhenFinish()
    }


    override fun getData() {
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()

    }

    /**
     * 医院详情dialog
     */
    private fun showMoreHospitalInfoDialog(hospitalDetailBean: HospitalDetailBean) {
        XPopup.Builder(this@HospitalDetailActivity)
            .enableDrag(false)
            .isDestroyOnDismiss(true)
            .moveUpToKeyboard(false)
            .popupHeight((ScreenUtils.getScreenHeight() * 0.75).toInt())
            .asCustom(
                MoreDetailDialog(
                    this@HospitalDetailActivity,
                    "医院简介",
                    hospitalDetailBean.hospital.hospitalDescription

                )
            )
            .show()
    }


    /**
     * 目录dialog
     */
    private fun showTitleListDilaog() {
        XPopup.Builder(this@HospitalDetailActivity)
            .enableDrag(false)
            .isDestroyOnDismiss(true)
            .moveUpToKeyboard(false)
            .popupHeight((ScreenUtils.getScreenHeight() * 0.75).toInt())
            .asCustom(
                HospitalDoctorTitleListDialog(
                    this@HospitalDetailActivity,
                    "医院目录",
                    titleTitleOffset, selectedPos
                ) { it, pos ->
                    selectedPos = pos
                    binding.commonTabLayout.setCurrentTab(selectedPos, true)
                    // binding.scrollerLayout.scrollTo(0, it.offsetTop)
                    when (pos) {
                        0 -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.expandScroll,
                            offset
                        )
                        1 -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.llTech,
                            offset
                        )
                        2 -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.tvHospitalDesc,
                            offset
                        )
                        (titleTitleOffset.size - 1) -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.tvTeam,
                            offset
                        )
                        else -> {
                            binding.scrollerLayout.scrollToChildWithOffset(
                                x5WebView, 0 - SizeUtils.dp2px((it.offsetTop - 24).toFloat())
                            )

                        }
                    }
                }
            )
            .show()
    }

    private fun showCallDialog(number: String) {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                number,
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(this, listData) { select ->
                    if (select == 0) {

                        PhoneUtils.dial(number)
                        MobAnalysisUtils.mInstance.sendEvent(UMEventKey.Event_CallIcon_click)
                    }
                }
            ).show()
    }
}