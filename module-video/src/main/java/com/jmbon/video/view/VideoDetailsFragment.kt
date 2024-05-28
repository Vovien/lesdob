package com.jmbon.video.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.*
import android.view.SurfaceHolder.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aliyun.loader.MediaLoader
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.nativeclass.TrackInfo
import com.aliyun.player.source.UrlSource
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.Utils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.*
import com.jmbon.video.R
import com.jmbon.video.adapter.VideoDetailsItemAdapter
import com.jmbon.video.databinding.FragmentVideoDetailsBinding
import com.jmbon.video.databinding.LayoutListPlayerViewBinding
import com.jmbon.video.util.NetWatchdog
import com.jmbon.video.util.TimeFormater
import com.jmbon.video.util.getAvailablePlayAddress
import com.jmbon.video.viewmodel.VideoDetailsViewModel
import com.jmbon.video.widget.seekbar.OnSeekChangeListener
import com.jmbon.video.widget.seekbar.SeekParams
import com.jmbon.video.widget.seekbar.TickSeekBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*

@Route(path = "/video/details/fragment")
class VideoDetailsFragment :
    ViewModelFragment<VideoDetailsViewModel, FragmentVideoDetailsBinding>() {

    /**
     * 当前播放位置
     */
    private var mCurPos = -1
    private lateinit var mViewPagerImpl: RecyclerView
    private var first = true

    /**
     * 是否是暂停
     */
    private var mIsPause = false

    private val listPlayer by lazy { AliPlayerFactory.createAliPlayer(context) }

    private val mediaLoader by lazy { MediaLoader.getInstance() }

    /**
     * 手势监听器
     */
    private lateinit var mGestureDetector: GestureDetector

    lateinit var playBinding: LayoutListPlayerViewBinding

    // 网络监听
    private val mNetWatchDog by lazy { NetWatchdog(context) }

    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
        mNetWatchDog.startWatch()
    }

    private var mTimer: Timer? = null


    // 视频的 adapter
    private val pageAdapter by lazy {
        VideoDetailsItemAdapter()
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        mNetWatchDog.setNetChangeListener(object : NetWatchdog.NetChangeListener {
            override fun onWifiTo4G() {
                if (Constant.trafficTips.not()) {
                    "当前非wifi状态\n请注意流量消耗".showToast()
                    Constant.trafficTips = true
                }
            }

            override fun on4GToWifi() {
            }

            override fun onNetDisconnected() {
                "请检查网络连接状态".showToast()
            }

        })
        initListPlayer()
        //初始化播放界面和暂停/播放按钮
        initListPlayerView()
        binding.apply {
            root.setPadding(0, statusHeight, 0, 0)
            viewPager2.apply {
                //ViewPage2内部是通过RecyclerView去实现的，它位于ViewPager2的第0个位置
                mViewPagerImpl = this.getChildAt(0) as RecyclerView
                adapter = pageAdapter
                offscreenPageLimit = 5
            }
        }
        pageAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {

                R.id.textDesc, R.id.textName, R.id.text_get_case -> {
                    val video = pageAdapter.data[position]
                    if (video.type == "adv") {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = video.itemType,
                                identity = video.identity
                            )
                        )
                    }
                }

            }
        }
        binding.viewPager2.unregisterOnPageChangeCallback(viewPageChangeCallback)
        binding.viewPager2.registerOnPageChangeCallback(viewPageChangeCallback)
    }

    private val viewPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (mCurPos == -1) {
                    return
                }
                // 取消之前的分享动画, 重置之前的视频播放
                if (mCurPos != position) {
                    Log.e("mCurPos", mCurPos.toString())
                    pageAdapter.notifyItemChanged(
                        mCurPos,
                        VideoDetailsItemAdapter.CANCEL_SHARE_ANIMATOR
                    )
                    pageAdapter.notifyItemChanged(
                        mCurPos,
                        VideoDetailsItemAdapter.VIDEO_RESET
                    )
                }
                // 俩秒后开始执行分享动画
                startShareAnimator(position)
                val data = pageAdapter.data[position]
                listPlayer.pause()
                listPlayer.stop()
                removeVideoContainer()
                startPlay(position, data)
                if (data.lastVideo) {
                    data.lastVideo = false
                    geAllVideoList(false)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                pageAdapter.canPreLoader = state == ViewPager2.SCROLL_STATE_IDLE
            }
        }

    var imageCoverView: ImageView? = null

    private fun startPlay(position: Int, data: VideoDetail.VideoData) {
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                started {
                }
                if (mTimer != null) {
                    mTimer?.cancel()
                    mTimer = null
                }
            }
        }, 1000 * 10)
        if (activity?.isFinishing?.not() == true) {
            val mViewHolder =
                mViewPagerImpl.findViewHolderForLayoutPosition(position) as BaseViewHolder?
//        removeVideoContainer()
            mViewHolder?.itemView?.apply {

                findViewById<FrameLayout>(R.id.videoControl)?.addView(playBinding.root)
                imageCoverView = findViewById(R.id.imageThumb)
//
                val urlSource = UrlSource()
                urlSource.uri = data.getAvailablePlayAddress() //播放地址，可以是第三方点播地址，或阿里云点播服务中的播放地址。
                listPlayer.setDataSource(urlSource)
                listPlayer.prepare()
                resumePlay()
                mCurPos = position
            }


        }
    }

    private fun geAllVideoList(isRefresh: Boolean) {
        resumed {
            viewModel.getAllVideoList().netCatch {
                "获取视频详情失败".showToast()
            }.next {
                if (this.isNotNullEmpty()) {
                    if (isRefresh) {
                        mCurPos = 0
                        if (pageAdapter.data.isNotEmpty()) {
                            pageAdapter.data.clear()
                        }
                        pageAdapter.addData(this)
                        if (!first) {
                            binding.viewPager2.setCurrentItem(0, false)
                        } else {
                            first = false
                        }
                    } else {
                        pageAdapter.addData(this)

                    }
//                    startShareAnimator()
                }
            }
        }
    }

    override fun getData() {
        resumed {
            showLoading(whiteBackground = false)
        }
        geAllVideoList(true)
    }


    private fun startShareAnimator(position: Int = mCurPos) {
        //第一次进入页面俩秒后开始分享动画
        binding.viewPager2.postDelayed({
            if (position != mCurPos) {
                return@postDelayed
            }

            pageAdapter.notifyItemChanged(
                mCurPos,
                VideoDetailsItemAdapter.START_SHARE_ANIMATOR
            )
        }, 2000)
    }

    /**
     * 视频暂停/恢复的时候使用，
     */
    fun onPauseClick() {
        if (mIsPause) {
            resumePlay()
        } else {
            pausePlay()
        }
    }

    /**
     * 暂停播放
     */
    private fun pausePlay() {
        mIsPause = true
        playBinding.ivPlay.visible()
        listPlayer.pause()
    }

    /**
     * 恢复播放
     */
    private fun resumePlay() {
        mIsPause = false
        playBinding.ivPlay.gone()
        listPlayer.start()
    }

    /**
     * 停止视频播放
     */
    private fun stopPlay() {
        removeVideoContainer()
        listPlayer.stop()
        listPlayer.setSurface(null)
    }

    private fun initListPlayer() {
        val path =
            File(
                Utils.getApp().getExternalFilesDir(null),
                File.separator + "video/cache/"
            ).absolutePath
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

//        AliPlayerGlobalSettings.enableLocalCache(true, 1024 * 5, path)
//        // 15 天 后过期 缓存 10G 最小 1G
//        AliPlayerGlobalSettings.setCacheFileClearConfig(
//            (60 * 24 * 15).toLong(),
//            (1024 * 10).toLong(), (1024 * 1).toLong()
//        )
//        // 使用 TCP 多路复用
//        AliPlayerGlobalSettings.setUseHttp2(true)
//        listPlayer.setTraceId(Constant.getDeviceId())
        listPlayer.isLoop = true
        //设置预加载个数。总共加载的个数为： 1 + count*2。
//        listPlayer.setPreloadCount(3)
        listPlayer.enableHardwareDecoder(true)

        // 根据网络切换清晰度
//        val trackInfos: List<TrackInfo> = listPlayer.mediaInfo.trackInfos
//        val index: Int = trackInfos.getIndex()
//        //多码率切换
//        listPlayer.selectTrack(index)
        //多码率切换并自适应
        listPlayer.selectTrack(TrackInfo.AUTO_SELECT_INDEX)


        val config = listPlayer.config
        config.mClearFrameWhenStop = true

        //设置网络超时时间，单位：毫秒
        config.mNetworkTimeout = 5000
        //设置超时重试次数。每次重试间隔为networkTimeout。networkRetryCount=0则表示不重试，重试策略app决定，默认值为2
        config.mNetworkRetryCount = 2

        // 最大缓冲区时长。单位ms。播放器每次最多加载这么长时间的缓冲数据。
        config.mMaxBufferDuration = 1000 * 10
        //高缓冲时长。单位ms。当网络不好导致加载数据时，如果加载的缓冲时长到达这个值，结束加载状态。
        config.mHighBufferDuration = 1000 * 3
        // 起播缓冲区时长。单位ms。这个时间设置越短，起播越快。也可能会导致播放之后很快就会进入加载状态。
        config.mStartBufferDuration = 1000 * 1
        //往前缓存的最大时长。单位ms。默认为0。
        config.mMaxBackwardBufferDurationMs = 0

        mediaLoader.setOnLoadStatusListener(object : MediaLoader.OnLoadStatusListener {
            override fun onError(p0: String?, p1: Int, p2: String?) {

            }

            override fun onCompleted(p0: String?) {
            }

            override fun onCanceled(p0: String?) {
            }
        })


        listPlayer.config = config
        listPlayer.setOnRenderingStartListener {
            dismissLoading()
            // 首帧渲染完成
            val duration: Long = listPlayer.duration
            currentItemView()?.findViewById<TickSeekBar>(R.id.seekBar)?.apply {
                onSeekChangeListener = seekChangeListener
                min = 0f
                max = duration.toFloat()
                this.setProgress(0f)
            }
            imageCoverView?.gone()
        }


        listPlayer.setOnInfoListener { infoBean ->
            if (infoBean.code == InfoCode.SwitchToSoftwareVideoDecoder) {
                //切换到软解
            }
            if (infoBean.code === InfoCode.CurrentPosition) {
                //extraValue为当前播放进度，单位为毫秒
                val seekBar = currentItemView()?.findViewById<TickSeekBar>(R.id.seekBar)
                val extraValue = infoBean.extraValue
                seekBar?.setProgress(extraValue.toFloat())
            }
            if (infoBean.code === InfoCode.BufferedPosition) {
                //extraValue为当前缓冲进度，单位为毫秒
            }
        }

        listPlayer.setOnCompletionListener {
            //播放完成事件
        }

        listPlayer.setOnErrorListener {
            LogUtils.e("alyVideo_error", it.code.toString() + " --- " + it.msg)
            listPlayer.stop()
        }

        listPlayer.setOnPreparedListener {
            //一般调用start开始播放视频。
        }


        listPlayer.setOnLoadingStatusListener(object : IPlayer.OnLoadingStatusListener {
            override fun onLoadingBegin() {
                //缓冲开始
            }

            override fun onLoadingProgress(percent: Int, kbps: Float) {
                //缓冲进度
//                LogUtils.e("onLoadingProgress() called with: percent = $percent, kbps = $kbps")
            }

            override fun onLoadingEnd() {
                //缓冲结束
            }

        })
        listPlayer.setOnSeekCompleteListener {
            //拖动结束
        }


        listPlayer.setOnStateChangedListener {
            //播放器状态改变事件
            // started =3
            binding.root.keepScreenOn = it == 3

        }
        listPlayer.setOnSnapShotListener { bm, with, height ->
            //截图事件
        }
    }

    val seekChangeListener = object : OnSeekChangeListener {

        override fun onSeeking(seekParams: SeekParams?) {
            seekParams?.apply {
                if (this.fromUser) {
                    // 手动隐藏控制器
                    SpanUtils.with(binding.textProgress)
                        .append(TimeFormater.formatMs(progress.toLong()))
                        .setForegroundColor(Color.WHITE)
                        .append(" / ")
                        .setForegroundColor(Color.WHITE)
                        .append(TimeFormater.formatMs(seekBar?.max?.toLong() ?: 0))
                        .setForegroundColor("#A9FFFFFF".toColorInt())
                        .create()
                }
            }

        }

        override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
            hindVideoControl(true)
            seekBar?.apply {
                this.setThumbSize(20f.dp());
                this.setThumbDrawable(resources.getDrawable(R.drawable.seekbar_video_thumb))
                this.setTrackSize(10)
                this.setTrackColor(Color.parseColor("#4F4F4F"), Color.parseColor("#CCFFFFFF"))
                seekBar.setPadding(6f.dp(), this.paddingTop, 6f.dp(), 20f.dp())
                val params = this.layoutParams as ConstraintLayout.LayoutParams
                params.topMargin = (-15f).dp()
            }
        }

        override fun onStopTrackingTouch(seekBar: TickSeekBar?) {
            seekBar?.apply {
                this.setPadding(0, this.paddingTop, 0, 10f.dp())
                listPlayer.seekTo(seekBar.progress.toLong(), IPlayer.SeekMode.Accurate)
                if (mIsPause)
                    resumePlay()
                this.setThumbSize(3f.dp());
                this.setThumbDrawable(resources.getDrawable(R.drawable.seek_bar_thumb));
                this.setTrackSize(2);
                this.setTrackColor(Color.parseColor("#4F4F4F"), Color.WHITE)
                val params = this.layoutParams as ConstraintLayout.LayoutParams
                params.topMargin = (-5f).dp()
            }
            hindVideoControl(false)
        }
    }


    fun hindVideoControl(hind: Boolean) {
        currentItemView()?.apply {
            findViewById<ConstraintLayout>(R.id.clVideoControl)?.apply {
                for (i in 0 until this.childCount) {
                    if (this.getChildAt(i).id != R.id.seekBar && this.getChildAt(i).id != R.id.imageThumb
                        && this.getChildAt(i).id != R.id.videoControl
                    ) {
                        if (hind) {
                            if (this.getChildAt(i).isVisible) {
                                this.getChildAt(i).invisible()
                            }
                        } else {
                            if (this.getChildAt(i).isInvisible) {
                                this.getChildAt(i).visible()
                            }
                        }
                    }
                }
            }
        }
        binding.textProgress.visibility = if (!hind) View.GONE else View.VISIBLE
    }

    private fun currentItemView(): View? {
        return (mViewPagerImpl.findViewHolderForLayoutPosition(mCurPos) as BaseViewHolder?)?.itemView
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListPlayerView() {
        playBinding = LayoutListPlayerViewBinding.inflate(layoutInflater)
        playBinding.listPlayerTextureview.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    val mSurface = Surface(surface)
                    if (listPlayer != null) {
                        listPlayer.setSurface(mSurface)
                        listPlayer.redraw()
                    }
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    if (listPlayer != null) {
                        listPlayer.redraw()
                    }
                }


                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    return true
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

                }
            }

        //手势监听器
        mGestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    onPauseClick()
                    return true
                }

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onScroll(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float,
                ): Boolean {
                    return false
                }
            })

        //播放列表界面的touch事件由手势监听器处理
        playBinding.listPlayerTextureview.setOnTouchListener { _, event ->
            mGestureDetector.onTouchEvent(event)
        }

    }

    private fun removeVideoContainer() {
        if (playBinding.root.parent != null) {
            (playBinding.root.parent as ViewGroup).removeView(playBinding.root)
        }
    }

    override fun onPause() {
        pausePlay()
        pageAdapter.canPreLoader = false
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }

        super.onPause()
    }


    override fun onResume() {
        resumePlay()
        pageAdapter.canPreLoader = true

        super.onResume()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun commonEvent(event: CommonEventHub.VideoPageEvent) {
        if (event.index == 1) {
            geAllVideoList(true)
        }
    }

    override fun onDestroyView() {
        if (listPlayer != null) {
            removeVideoContainer()
            listPlayer.stop()
            listPlayer.release()
            listPlayer.setSurface(null)
        }
        mTimer?.cancel()
        mNetWatchDog.stopWatch()
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
        System.gc()
    }


}