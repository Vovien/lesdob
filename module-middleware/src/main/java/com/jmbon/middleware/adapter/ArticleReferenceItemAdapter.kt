package com.jmbon.middleware.adapter

import android.annotation.SuppressLint
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ReferenceInfoBean
import com.jmbon.middleware.databinding.ArticleReferenceItemLayoutBinding


/**
 * @author : wangzhen
 * time   : 2021/5/8
 * desc   : 悬赏采纳消息轮播适配器
 * version: 1.0
 */
class ArticleReferenceItemAdapter :
    BaseQuickAdapter<ReferenceInfoBean, BaseViewHolder>(R.layout.article_reference_item_layout) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: ReferenceInfoBean) {
        val view = ArticleReferenceItemLayoutBinding.bind(holder.itemView)

        if (item.type == 1) {

            val titleStr = "${holder.adapterPosition + 1}.${item.title}"

            var data = SpanUtils.with(view.tvTitle).append(titleStr)
                .appendImage(R.drawable.icon_article_reference_link).append(".${item.workname}")
                .create()

            view.tvTitle.text = data

            view.tvReferenceDate.text = "[引用日期 ${item.referenceDate}]"
            view.tvArticleDate.text = "[文章发布日期 ${item.releaseDate}]"

            view.tvArticleDate.visible()

        } else {
            val titleStr = "${holder.adapterPosition + 1}.${item.authorname}. 《${item.workname}》 "
            val allTitleStr =
                titleStr + if (item.pressname.isNullOrEmpty()) "" else ".${item.pressname}"

            view.tvTitle.text = allTitleStr

            view.tvReferenceDate.text = "[引用日期 ${item.referenceDate}]"
            view.tvArticleDate.text = "[著作出版日期 ${item.releaseDate}]"

            if (item.releaseDate.isNullOrEmpty()) {
                view.tvArticleDate.gone()
            } else {
                view.tvArticleDate.visible()
            }
        }

    }
}