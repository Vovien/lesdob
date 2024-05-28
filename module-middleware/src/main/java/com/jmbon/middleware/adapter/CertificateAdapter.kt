package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.ItemCertificateAdapterBinding
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.loadRadius
import com.luck.picture.lib.entity.LocalMedia

class CertificateAdapter(val onDelete:()->Unit = {}) : BindingQuickAdapter<LocalMedia, ItemCertificateAdapterBinding>() {

    var addIconId = R.drawable.image_select_photo_add_new

    override fun convert(holder: BaseBindingHolder, item: LocalMedia) {
        holder.getViewBinding<ItemCertificateAdapterBinding>().apply {
            if (item.path.isNullOrEmpty() && item.compressPath.isNullOrEmpty()) {
                imageItem.loadRadius("", 8f.dp(), addIconId)
                imageDel.gone()
            } else {
                imageItem.loadRadius(item.path, 8f.dp(), R.drawable.icon_topic_placeholder)
                imageDel.visible()
                imageDel.setOnClickListener {
                    remove(item)
                    if (data.size < 9) {
                        // data
                        val last = data.last()
                        if (last.path.isNotNullEmpty()) {
                            addData(LocalMedia())
                        }
                    }
                }
            }
        }
    }
}