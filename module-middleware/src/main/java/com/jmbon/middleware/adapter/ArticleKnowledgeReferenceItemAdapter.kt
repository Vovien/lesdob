package com.jmbon.middleware.adapter

import android.annotation.SuppressLint
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.ArticleDetails
import com.jmbon.middleware.bean.ArticleList
import com.jmbon.middleware.databinding.ArticleKnowledgeReferenceItemLayoutBinding
import com.jmbon.middleware.utils.TagConstant


/**
 * @author : wangzhen
 * time   : 2021/5/8
 * desc   : 文章相关引用知识点
 * version: 1.0
 */
class ArticleKnowledgeReferenceItemAdapter :
    BaseQuickAdapter<ArticleDetails.Article, BaseViewHolder>(R.layout.article_knowledge_reference_item_layout) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: ArticleDetails.Article) {
        val view = ArticleKnowledgeReferenceItemLayoutBinding.bind(holder.itemView)
        view.tvTitle.text = item.title

        view.root.setOnClickListener {
            val articleList = GsonUtils.fromJson(GsonUtils.toJson(item),ArticleList::class.java)
            articleList.contentHtml = item.contentHtml
            ArouterUtils.toArticleDetailsActivity(articleList)

        }

    }
}