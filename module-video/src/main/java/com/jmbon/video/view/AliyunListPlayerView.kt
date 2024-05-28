package com.jmbon.video.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import com.aliyun.loader.MediaLoader
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.AliPlayerGlobalSettings
import com.aliyun.player.source.StsInfo
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.bean.VideoDetail.VideoData
import com.jmbon.video.R
import com.jmbon.video.adapter.PagerLayoutManager
import com.jmbon.video.adapter.VideoDetailsItemAdapter
import com.jmbon.video.listener.OnViewPagerListener
import com.jmbon.video.util.getAvailablePlayAddress
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.io.File

/**
 * 短视频列表播放器
 * @author MilkCoder
 * @date 2023/9/18 9:57
 * @copyright all rights reserved by ManTang
 */
class AliyunListPlayerView : FrameLayout {
    lateinit var mListPlayerContainer: View

    /**
     * play -- pause  icon
     */
    private var mPlayIconImageView: ImageView? = null
    private var mListPlayerTextureView: TextureView? = null
    private var mListPlayerRecyclerView: RecyclerViewEmptySupport? = null
    private var mStsInfo: StsInfo? = null
    private var mPagerLayoutManager: PagerLayoutManager? = null

    private val mRecyclerViewAdapter by lazy {
        VideoDetailsItemAdapter()
    }

    /**
     * 播放器
     */
    private val mAliListPlayer by lazy {
        AliPlayerFactory.createAliListPlayer(context)
    }

    /**
     * 预加载
     */
    private val mediaLoader by lazy {
        MediaLoader.getInstance()
    }


    /**
     * 手势监听器
     */
    private var mGestureDetector: GestureDetector? = null
    /**
     * 获取关联表
     */
    /**
     * 设置关联表
     */
    /**
     * 播放资源UUID和index关联表
     */
    var correlationTable: SparseArray<String> = SparseArray()

    /**
     * 当前选中位置
     */
    private var mCurrentPosition = 0

    /**
     * 正常滑动，上一个被暂停的位置
     */
    private var mLastStopPosition = -1

    /**
     * 是否在后台
     */
    private var mIsOnBackground = false

    /**
     * 是否是暂停
     */
    private var mIsPause = false

    /**
     * 刷新View
     */
    private var mRefreshView: SmartRefreshLayout? = null

    /**
     * 是否正在刷新
     */
    private var mIsLoadingData = false

    /**
     * 刷新数据listener
     */
    private var onRefreshDataListener: OnRefreshDataListener? = null
    private var mRefreshTextView: TextView? = null
    private var mVideoListBean: MutableList<VideoData> = mutableListOf()

    constructor(context: Context?) : super(context!!) {
        initVideoView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        initVideoView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        initVideoView()
    }

    private fun initVideoView() {
        //初始化列表播放器
        initListPlayer()
        //初始化播放界面和暂停/播放按钮
        initListPlayerView()
        //初始化PagerLayoutManager
        initPagerLayoutManager()
        //初始化滑动RecyclerView
        initRecyclerView()
    }

    /**
     * 初始化列表播放器
     */
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
        AliPlayerGlobalSettings.enableLocalCache(true, 1024 * 2, path)
        AliPlayerGlobalSettings.setCacheFileClearConfig(
            (60 * 24 * 15).toLong(),
            (1024 * 2).toLong(),
            1024
        )
        mAliListPlayer.setPreloadCount(3)
        mAliListPlayer.isLoop = true
        val config = mAliListPlayer.config
        config.mClearFrameWhenStop = true
        mAliListPlayer.config = config
        mAliListPlayer.setOnPreparedListener {
            if (!mIsPause && !mIsOnBackground) {
                mAliListPlayer.start()
            }
        }
        mediaLoader.setOnLoadStatusListener(object : MediaLoader.OnLoadStatusListener {
            override fun onError(p0: String?, p1: Int, p2: String?) {

            }

            override fun onCompleted(p0: String?) {
            }

            override fun onCanceled(p0: String?) {
            }
        })
        mAliListPlayer.setOnRenderingStartListener {
            val mViewHolder =
                mListPlayerRecyclerView!!.findViewHolderForLayoutPosition(mCurrentPosition)
            if (mViewHolder != null) {
                val coverView = mViewHolder.itemView.findViewById<View>(R.id.clVideoControl)
                    .findViewById<ImageView>(R.id.imageThumb)
                coverView.visibility = GONE
            }
        }
        mAliListPlayer.setOnInfoListener { }
        mAliListPlayer.setOnErrorListener { errorInfo ->
            Log.e(
                "www",
                errorInfo.msg
            )
        }
    }

    /**
     * 初始化播放界面
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListPlayerView() {
        mListPlayerContainer = inflate(context, R.layout.layout_list_player_view, null)
        mPlayIconImageView = mListPlayerContainer.findViewById(R.id.iv_play)
        mListPlayerTextureView = mListPlayerContainer.findViewById(R.id.list_player_textureview)

        //TextureView
        mListPlayerTextureView?.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                val mSurface = Surface(surfaceTexture)
                if (mAliListPlayer != null) {
                    mAliListPlayer.setSurface(mSurface)
                    mAliListPlayer.redraw()
                }
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                if (mAliListPlayer != null) {
                    mAliListPlayer.redraw()
                }
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }

        //手势监听器
        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                onPauseClick()
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
        })

        //播放列表界面的touch事件由手势监听器处理
        mListPlayerContainer.setOnTouchListener { view, motionEvent ->
            mGestureDetector!!.onTouchEvent(
                motionEvent
            )
        }
    }

    /**
     * 初始化滑动RecyclerView
     */
    private fun initRecyclerView() {
        val mListPlayerRecyclerViewRoot = LayoutInflater.from(context)
            .inflate(R.layout.layout_list_player_recyclerview, this, true)
        mListPlayerRecyclerView =
            mListPlayerRecyclerViewRoot.findViewById(R.id.list_player_recyclerview)
        mRefreshView = mListPlayerRecyclerViewRoot.findViewById(R.id.refresh_view)
        mRefreshTextView = mListPlayerRecyclerViewRoot.findViewById(R.id.tv_refresh)
        mRefreshView?.setOnRefreshListener {
            if (onRefreshDataListener != null) {
                mIsLoadingData = true
                onRefreshDataListener?.onRefresh()
            }
        }
        mRefreshTextView?.setOnClickListener {

            if (onRefreshDataListener != null) {
                mIsLoadingData = true
                onRefreshDataListener?.onRefresh()
            }
        }
        mListPlayerRecyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = mPagerLayoutManager
//            setEmptyView(mListPlayerRecyclerViewRoot.findViewById(R.id.rl_empty_view))
            adapter = mRecyclerViewAdapter
        }

        mRecyclerViewAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.textDesc, R.id.textName, R.id.text_get_case -> {
                    val video = mRecyclerViewAdapter.data[position]
//                    if (video.type == "adv") {
//                        BannerHelper.onClick(
//                            CommonBanner(
//                                item_type = video.itemType,
//                                identity = video.identity
//                            )
//                        )
//                        BuryHelper.addEvent(
//                            BuryPointInfo(
//                                event_id = ClickEventEnum.EVENT_CLICK_VIDEO_AD_DETAIL_AD.value,
//                                params = mutableMapOf((Pair("video_ad_id", "${video.id}")))
//                            )
//                        )
//                    }
                }

            }
        }

    }

    private fun initPagerLayoutManager() {
        if (mPagerLayoutManager == null) {
            mPagerLayoutManager = PagerLayoutManager(context)
            mPagerLayoutManager!!.isItemPrefetchEnabled = true
        }
        if (mPagerLayoutManager!!.viewPagerListenerIsNull()) {
            mPagerLayoutManager?.setOnViewPagerListener(object : OnViewPagerListener {
                override fun onInitComplete() {
                    val position = mPagerLayoutManager!!.findFirstVisibleItemPosition()
                    if (position != -1) {
                        mCurrentPosition = position
                    }
                    val itemCount = mRecyclerViewAdapter.itemCount
                    if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !mIsLoadingData) {
                        // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
                        mIsLoadingData = true
                        loadMore()
                    }
                    startPlay(mCurrentPosition)
                    mLastStopPosition = -1
                }

                @SuppressLint("CutPasteId")
                override fun onPageRelease(isNext: Boolean, position: Int, view: View?) {
                    if (mCurrentPosition == position) {
                        mLastStopPosition = position
                        stopPlay()
                        val mViewHolder =
                            mListPlayerRecyclerView?.findViewHolderForLayoutPosition(position)
                        if (mViewHolder != null) {
                            val coverView =
                                mViewHolder.itemView.findViewById<View>(R.id.clVideoControl)
                                    .findViewById<ImageView>(R.id.imageThumb)
                            val case = mViewHolder.itemView.findViewById<View>(R.id.clVideoControl)
                                .findViewById<TextView>(R.id.text_get_case)
                            case.setBackgroundResource(R.drawable.bg_1affffff_with_radius_9dp)
                            coverView.visibility = VISIBLE
                        }
                    }
                }

                override fun onPageSelected(position: Int, bottom: Boolean, view: View?) {
                    //重新选中视频不播放，如果该位置被stop过则会重新播放视频
                    if (mCurrentPosition == position && mLastStopPosition != position) {
                        return
                    }
                    val itemCount = mRecyclerViewAdapter.itemCount
                    if (itemCount == position + 1) {
//                        Toast.makeText(getContext(), R.string.alivc_player_tip_last_video, Toast.LENGTH_SHORT).show();
                    }
                    if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !mIsLoadingData) {
                        // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
                        mIsLoadingData = true
                        loadMore()
                    }
                    startShareAnimator(position)
                    //开始播放选中视频
                    startPlay(position)
                    mCurrentPosition = position
                }
            })
        }
    }

    /**
     * 加载更多
     */
    private fun loadMore() {
        onRefreshDataListener?.onLoadMore()
    }

    /**
     * 播放视频
     */
    private fun startPlay(position: Int) {
        if (position < 0 || position > mVideoListBean.size) {
            return
        }
        //恢复界面状态
        mIsPause = false
        mPlayIconImageView?.visibility = GONE
        val mViewHolder = mListPlayerRecyclerView?.findViewHolderForLayoutPosition(position)
        val parent = mListPlayerContainer.parent
        if (parent is FrameLayout) {
            (parent as ViewGroup).removeView(mListPlayerContainer)
        }
        if (mViewHolder != null) {
            val fl = mViewHolder.itemView.findViewById<View>(R.id.clVideoControl)
                .findViewById<FrameLayout>(R.id.videoControl)
            fl.addView(mListPlayerContainer, 0)
        }

        //防止退出后台之后，再次调用start方法，导致视频播放
        if (!mIsOnBackground) {
            mAliListPlayer.moveTo(correlationTable[position])
        }
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
     * 设置StsInfo
     */
    fun setStsInfo(stsInfo: StsInfo?) {
        mStsInfo = stsInfo
    }

    /**
     * 开始播放
     */
    fun moveTo(uuid: String?) {
        if (mAliListPlayer != null) {
            mAliListPlayer.moveTo(uuid)
        }
    }

    fun moveTo(uuid: String?, stsInfo: StsInfo?) {
        if (mAliListPlayer != null) {
            mAliListPlayer.moveTo(uuid, stsInfo)
        }
    }

    /**
     * 添加视频源
     * @date 2023/9/18 11:11
     */
    fun addUrl(videoUrl: String?, randomUUID: String?) {
        if (mAliListPlayer != null) {
            mAliListPlayer.addUrl(videoUrl, randomUUID)
        }
    }

    /**
     * 清空视频源
     * @date 2023/9/18 11:11
     */
    fun removeAllUrl() {
        if (correlationTable.isNotEmpty()) {
            correlationTable.forEach { _, value ->
                mAliListPlayer?.removeSource(value)
            }
        }
    }

    private fun startShareAnimator(position: Int = mCurrentPosition) {
        //第一次进入页面俩秒后开始分享动画
        mListPlayerRecyclerView?.postDelayed({
            if (position != mCurrentPosition) {
                return@postDelayed
            }

            mRecyclerViewAdapter.notifyItemChanged(
                mCurrentPosition,
                VideoDetailsItemAdapter.START_SHARE_ANIMATOR
            )
        }, 2000)
    }

    /**
     * 添加视频
     */
    fun addVid(videoId: String?, randomUUID: String?) {
        if (mAliListPlayer != null) {
            mAliListPlayer.addVid(videoId, randomUUID)
        }
    }

    /**
     * 设置播放源
     */
    fun setData(videoListBeanItems: List<VideoData>) {
        mIsLoadingData = false
        if (mRefreshView != null && mRefreshView!!.isRefreshing) {
            mRefreshView?.finishRefresh()
        }
        mRecyclerViewAdapter.data.clear()
        mRecyclerViewAdapter.addData(videoListBeanItems)
        // 设置预加载
        videoListBeanItems.forEach {
            mediaLoader.load(it.getAvailablePlayAddress(), 1000 * 3)
        }
        mRecyclerViewAdapter.notifyDataSetChanged()
        mListPlayerRecyclerView?.scrollToPosition(0)
        mVideoListBean.clear()
        mVideoListBean.addAll(videoListBeanItems)
    }

    /**
     * 加载更多数据
     */
    fun addMoreData(videoListBeanItems: List<VideoData>) {
        mIsLoadingData = false
        mRecyclerViewAdapter.addData(videoListBeanItems)
        // 设置预加载
        videoListBeanItems.forEach {
            mediaLoader.load(it.getAvailablePlayAddress(), 1000 * 3)
        }
        mVideoListBean.addAll(videoListBeanItems)
        hideRefresh()
    }

    fun hideRefresh() {
        if (mRefreshView != null) {
            mRefreshView?.finishRefresh()
        }
    }

    /**
     * 停止视频播放
     */
    private fun stopPlay() {
        val parent = mListPlayerContainer!!.parent
        if (parent is FrameLayout) {
            parent.removeView(mListPlayerContainer)
        }
        mAliListPlayer.stop()
        mAliListPlayer.setSurface(null)
    }

    /**
     * activity不可见或者播放页面不可见时调用该方法
     */
    fun setOnBackground(isOnBackground: Boolean) {
        mIsOnBackground = isOnBackground
        if (isOnBackground) {
            pausePlay()
        } else {
            resumePlay()
        }
    }

    /**
     * 暂停播放
     */
    private fun pausePlay() {
        mIsPause = true
        mPlayIconImageView!!.visibility = VISIBLE
        mAliListPlayer.pause()
    }

    /**
     * 恢复播放
     */
    private fun resumePlay() {
        mIsPause = false
        mPlayIconImageView!!.visibility = GONE
        mAliListPlayer.start()
    }

    /**
     * 销毁
     */
    fun destroy() {
        if (mAliListPlayer != null) {
            mAliListPlayer.stop()
            mAliListPlayer.release()
        }
    }

    /**
     * 刷新数据
     */
    interface OnRefreshDataListener {
        /**
         * 下拉刷新
         */
        fun onRefresh()

        /**
         * 上拉加载
         */
        fun onLoadMore()
    }

    fun setOnRefreshDataListener(onRefreshDataListener: OnRefreshDataListener?) {
        this.onRefreshDataListener = onRefreshDataListener
    }

    companion object {
        /**
         * 预加载位置, 默认离底部还有5条数据时请求下一页视频列表
         */
        private const val DEFAULT_PRELOAD_NUMBER = 5
    }
}