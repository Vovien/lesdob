package com.tubewiki.mine.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.convertToArticleList
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.CollectionArticleData
import com.tubewiki.mine.databinding.ItemMineCollectionArticleBinding


/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 我的-收藏-文章适配器
 * version: 1.0
 */
class MineCollectionArticleAdapter :
    BindingQuickAdapter<CollectionArticleData.Data, ItemMineCollectionArticleBinding>() {
    var highlight = ""
    override fun convert(holder: BaseBindingHolder, item: CollectionArticleData.Data) {
        holder.getViewBinding<ItemMineCollectionArticleBinding>().apply {
            textArticleTitle.text=item.title.setKeyHighLight(highlight)
            textPraise.text = "${item.givenCount} 赞"
            textComments.text = "${item.comments} 评论"
            imageArticleBg.loadRadius(
                item.image,
                8f.dp(),
                R.drawable.icon_question_answer_placeholder
            )
            textName.text = DateFormatUtil.getStringByFormat(
                item.addTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )
            root.setOnClickListener {
//                ARouter.getInstance().build("/home/article/details")
//                    .withInt("articleId", item.id)
//                    .navigation()

//                ARouter.getInstance().build("/home/article/details")
//                    .withInt("articleId", item.id)
//                    .withParcelable(
//                        TagConstant.ARTICLE_CONTENT, convertToArticleList(
//                            item.abstract, item.addTime,
//                            item.comments, item.contentHtml, item.id, item.infos, item.title
//                        )
//                    )
//                    .navigation()

                ARouter.getInstance().build("/question/activity/answer_detail")
                    .withInt(TagConstant.ARTICLE_ID, item.id)
                    .withParcelable(TagConstant.ARTICLE_CONTENT, convertToArticleList(
                        item.abstract, item.addTime,
                        item.comments, item.contentHtml, item.id, item.infos, item.title
                    )
                    )
                    .withTransition(
                       R.anim.activity_bottom_in,
                       R.anim.activity_background
                    )
                    .navigation()

            }
        }

    }
}
