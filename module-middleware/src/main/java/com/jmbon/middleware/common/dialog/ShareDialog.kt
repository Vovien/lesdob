package com.jmbon.middleware.common.dialog

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.OvershootInterpolator
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ClickUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ShareBean
import com.jmbon.middleware.databinding.DialogShareLayoutBinding
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import java.util.*


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :分享dialog
 * version: 1.0
 */
class ShareDialog(
    val mContext: Context,
    val shareBean: ShareBean? = null,
    val isShareToCircle: Boolean = false,
    val click: (() -> Unit)? = null,
) :
    BaseBottomDialog<DialogShareLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

        binding.rlCircle.visibility =
            if (isShareToCircle) View.VISIBLE else View.GONE
        shareBean?.let {
            if (it.shareText.isNotNullEmpty() && click != null) {
                binding.imageShareIcon.setImageResource(it.shareIcon)
                binding.textShare.text = it.shareText
                binding.rlCircle.setOnSingleClickListener({
                    Action {
                        click.invoke()
                    }.logInToIntercept()
                })
            } else {
                ClickUtils.applySingleDebouncing(binding.rlCircle) {
                    Action {
                        //分享到圈子
                        shareBean.let { it ->
                            val type = when {
                                shareBean.url.contains("question") -> {
                                    "answer"
                                }
                                shareBean.url.contains("article") -> {
                                    "article"
                                }
                                else -> {
                                    "help"
                                }
                            }
                            ARouter.getInstance().build("/middleware/circle/share_to_circle")
                                .withString("title", it.title)
                                .withInt("itemId", it.pageId)
                                .withInt("answerId", it.answerId)
                                .withString("type", type)
                                .withTransition(
                                    R.anim.activity_bottom_in,
                                    R.anim.activity_background
                                )
                                .navigation(mContext)
                        }

                        dismiss()
                    }.logInToIntercept()
                }
            }
        }

        ClickUtils.applySingleDebouncing(binding.llQq) {
            share(ShareUtils.TYPE_SHARE_QQ)
            dismiss()
        }
        ClickUtils.applySingleDebouncing(binding.llWechat) {
//            shareWx()
            share(ShareUtils.TYPE_SHARE_WECHAT)
            dismiss()
        }
        ClickUtils.applySingleDebouncing(binding.llWechatCircle) {
            share(ShareUtils.TYPE_SHARE_WECHAT_FRIENDS)
            dismiss()
        }
        ClickUtils.applySingleDebouncing(binding.llWeibo) {
            share(ShareUtils.TYPE_SHARE_SINA)
            dismiss()
        }
        ClickUtils.applySingleDebouncing(binding.ivClose) {
            dismiss()
        }
        //showAnimator()
    }

    fun shareWx() {
        shareBean?.path = when {
            shareBean?.url?.contains("/article/") == true -> {
                "/pages/articledetail/articledetail?id="
            }
            shareBean?.url?.contains("/c/") == true -> {
                "/pages/columndetail/columndetail?id="
            }
            shareBean?.url?.contains("/question/") == true -> {
                "/pages/question/question?id="
            }
            shareBean?.url?.contains("/topic/") == true -> {
                "/pages/topicdetail/topicdetail?id="
            }
            else -> ""
        }
        if (shareBean?.path.isNotNullEmpty()) {
            ShareUtils.shareWx(shareBean, object : PlatformActionListener {
                override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                    "分享成功".showToast()
                }

                override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                    "分享失败".showToast()
                    p2?.printStackTrace()
                }

                override fun onCancel(p0: Platform?, p1: Int) {
                    //"分享取消".showToast()
                }

            })
        } else share(ShareUtils.TYPE_SHARE_WECHAT)
    }

    fun share(type: Int) {
        shareBean?.content = shareBean?.content?.take(100)
        shareBean?.friendTargetDesc = shareBean?.friendTargetDesc?.take(100)

        //qq或者微信好友分享单独处理分享desc
        if ((type == ShareUtils.TYPE_SHARE_QQ || type == ShareUtils.TYPE_SHARE_WECHAT_FRIENDS) && shareBean?.friendTargetDesc.isNotNullEmpty()) {
            shareBean?.content = shareBean?.friendTargetDesc
        }

        ShareUtils.share(type, shareBean, object : PlatformActionListener {
            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                "分享成功".showToast()
            }

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                "分享失败".showToast()
            }

            override fun onCancel(p0: Platform?, p1: Int) {
                //"分享取消".showToast()
            }

        })
    }

    fun share(shareBean: ShareBean, type: Int) {
        ShareUtils.share(type, shareBean, object : PlatformActionListener {
            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                "分享成功".showToast()
            }

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                "分享失败".showToast()
            }

            override fun onCancel(p0: Platform?, p1: Int) {
                //"分享取消".showToast()
            }

        })
    }


    private val offset = 160f.dp().toFloat()
    private fun showAnimator() {
        val animatorWx =
            ObjectAnimator.ofFloat(binding.llWechat, "translationY", offset, 0f).setDuration(300)
        animatorWx.interpolator = OvershootInterpolator()
        val animatorCircle =
            ObjectAnimator.ofFloat(binding.llWechatCircle, "translationY", offset, 0f)
                .setDuration(300)
        animatorCircle.interpolator = OvershootInterpolator()
        val animatorQq =
            ObjectAnimator.ofFloat(binding.llQq, "translationY", offset, 0f).setDuration(300)
        animatorQq.interpolator = OvershootInterpolator()
        val animatorWb =
            ObjectAnimator.ofFloat(binding.llWeibo, "translationY", offset, 0f).setDuration(300)
        animatorWb.interpolator = OvershootInterpolator()


        postDelayed({
            binding.llWechat.alpha = 1f
            animatorWx.start()
        }, 100)
        postDelayed({
            binding.llWechatCircle.alpha = 1f
            animatorCircle.start()
        }, 150)
        postDelayed({
            binding.llQq.alpha = 1f
            animatorQq.start()
        }, 200)
        postDelayed({
            binding.llWeibo.alpha = 1f
            animatorWb.start()
        }, 250)
    }
}