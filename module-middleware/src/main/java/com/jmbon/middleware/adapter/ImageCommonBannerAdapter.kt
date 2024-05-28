package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

/**
 * 图片广告通用适配器
 * @author MilkCoder
 * @date 2023/6/9
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
class ImageCommonBannerAdapter(val action: (position: Int) -> Unit = {}) : BannerImageAdapter<CommonBanner>(listOf()) {
    override fun onBindView(
        holder: BannerImageHolder?,
        data: CommonBanner?,
        position: Int,
        size: Int
    ) {
        holder?.imageView?.let {
            GlideUtil.getInstance().loadUrl(it, data?.img ?: "", 0)
            it.setOnSingleClickListener({
                data?.apply {
                    BannerHelper.onClick(CommonBanner(
                        item_id = this.item_id,
                        item_type = this.item_type,
                        identity = this.identity
                    ))
                    action(position)
                }
            })
        }
    }
}