package com.jmbon.middleware.arouter

import android.app.Activity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ArticleList
import com.jmbon.middleware.utils.TagConstant

/**
 * @author : leimg
 * time   : 2022/5/23
 * desc   :
 * version: 1.0
 */
object ArouterUtils {
    /**
     * 跳转文章详情页
     */
    fun toArticleDetailsActivity(articleId: Int) {

        ARouter.getInstance().build("/home/article/details")
            .withInt(TagConstant.ARTICLE_ID, articleId)
            .navigation()
    }

    /**
     * 跳转医生详情页
     */
    fun toDoctorDetailsActivity(doctorId: Int) {

        ARouter.getInstance().build("/hospital/activity/doctor_detail")
            .withInt(TagConstant.DOCTOR_ID, doctorId)
            .navigation()
    }

    /**
     * 跳转医院详情页
     */
    fun toHospitalDetailsActivity(hospitalId: Int) {
        ARouter.getInstance().build("/hospital/activity/hospital_detail")
            .withInt(TagConstant.HOSPITAL_ID, hospitalId)
            .navigation()
    }

    /**
     * 跳转文章专栏详情页
     */
    fun toArticleColumnDetailActivity(topicId: Int) {

        ARouter.getInstance().build("/home/activity/column_detail")
            .withInt(TagConstant.TOPIC_ID, topicId)
            .withTransition(
                R.anim.activity_bottom_in,
                R.anim.activity_background
            )
            .navigation()

    }

    /**
     * 跳转文章详情页 点击评论
     */
    fun toArticleDetailsActivityWithComment(articleId: Int, commentId: Int) {

        ARouter.getInstance().build("/question/activity/answer_detail")
            .withInt(TagConstant.ARTICLE_ID, articleId)
            .withBoolean(TagConstant.SHOW_COMMENT, true)
            .withInt(TagConstant.NEED_TOP_ID, commentId)
            .navigation()

    }

    /**
     * 跳转文章详情页
     */
    fun toArticleDetailsActivity(item: ArticleList) {

        ARouter.getInstance().build("/question/activity/answer_detail")
            .withInt(TagConstant.ARTICLE_ID, item.id)
            .withParcelable(TagConstant.ARTICLE_CONTENT, item)
            .withTransition(
                R.anim.activity_bottom_in,
                R.anim.activity_background
            )
            .navigation()

    }

    /**
     * 跳转隐私协议页面
     */
    fun topPrivacyAgreeActivity(activity: Activity) {

        ARouter.getInstance().build("/main/privacy_agree")
            .withTransition(
                R.anim.fade_in,
                R.anim.fade_out
            )
            .navigation(activity, object: NavCallback() {
                override fun onArrival(postcard: Postcard?) {
                    activity.finish()
                }
            })
    }

}