package com.tubewiki.mine.view.message

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.decoration.SpaceItemDecoration
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.AskDetailImageAdapter
import com.tubewiki.mine.bean.ContentTortBean
import com.tubewiki.mine.databinding.ActivityJmDelictReportLayoutBinding
import com.tubewiki.mine.view.model.FeedReportViewModel

import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia


/**
 * 侵权行为举报详情
 */
@Route(path = "/mine/message/delict_report")
class JmDelictReportActivity :
    ViewModelActivity<FeedReportViewModel, ActivityJmDelictReportLayoutBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var id = 0

    var contentTortBean: ContentTortBean? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.delict_report_detail))

        binding.rvImage.setHasFixedSize(true)
        binding.rvImage.layoutManager = GridLayoutManager(this, 3)
        binding.rvImage.adapter = AskDetailImageAdapter()
        binding.rvImage.addItemDecoration(SpaceItemDecoration(0, 4))


        binding.tvHandle.setOnSingleClickListener({
            ARouter.getInstance().build("/mine/message/handle_report")
                .withInt(TagConstant.PARAMS, id).navigation()
            finish()
        })
        binding.tvLink.setOnSingleClickListener({
            contentTortBean?.let {
                // 【1：文章，2：提问，3：回答】',
                when (it.contentTort.type) {
                    1 -> {

                        ArouterUtils.toArticleDetailsActivity( it.contentTort.itemId)
                    }
                    2 -> {
                        ARouter.getInstance().build("/question/activity/ask_detail")
                            .withInt(TagConstant.QUESTION_ID, it.contentTort.itemId)
                            .navigation()
                    }
                    3 -> {
                        ARouter.getInstance().build("/question/activity/answer_detail")
                            .withInt(TagConstant.QUESTION_ID, it.contentTort.answerQuestionId)
                            .withInt(TagConstant.ANSWER_ID, it.contentTort.itemId)
                            .navigation()
                    }
                }
            }
        })
    }


    override fun initData() {

    }

    override fun getData() {
        started {
            viewModel.getContentTortInfo(id).netCatch {
                message.showToast()
            }.next {
                contentTortBean = this
                binding.tvLink.text = contentTort.originContentUrl
                binding.tvDetail.text = contentTort.reason

                if (this.contentTort.status == 1) {
                    binding.tvHandle.isEnabled = false
                }

                var data = mutableListOf<LocalMedia>()

                contentTort.material.forEach {
                    data.add(LocalMedia(it, 0, 0, PictureMimeType.MIME_TYPE_IMAGE))
                }
                (binding.rvImage.adapter as AskDetailImageAdapter).setNewInstance(data)
            }
        }

    }


}