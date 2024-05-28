package com.jmbon.video.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.palette.graphics.Palette
import com.apkdv.mvvmfast.glide.GlideApp
import com.apkdv.mvvmfast.glide.GlideUtil
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView
import com.jmbon.middleware.adapter.EventViewBindingDelegate
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.utils.*
import com.jmbon.video.R
import com.jmbon.video.bean.VideoStream
import com.jmbon.video.databinding.ItemVideoListCircleBinding

class VideoStreamCircleDelegate :
    EventViewBindingDelegate<CircleChangedEvent, VideoStream.VideoStreamResult, ItemVideoListCircleBinding>() {


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewBindingViewHolder<ItemVideoListCircleBinding>,
        item: VideoStream.VideoStreamResult,
    ) {
        holder.binding.apply {
//            root.setViewMargins()
            root.layoutParams.height = 204f.dp()
            val circleCovers = arrayListOf<ShapeableImageView>()
            circleCovers.add(imageCircleCover1)
            circleCovers.add(imageCircleCover2)
            circleCovers.add(imageCircleCover3)

            item.circle.forEachIndexed { index, circle ->
                circleCovers[index].loadRadius(circle.covers, 7f.dp())

                circleCovers[index].setOnSingleClickListener({
                    setCircleTitle(circle)
                    circleCovers.forEach {
                        it.strokeWidth = 0f
                    }
                    circleCovers[index].strokeWidth = 2f.dp().toFloat()

                    root.setOnSingleClickListener({
                        joinCircle(
                            circle.name,
                            circle.number,
                            circle.isJoin
                        )
                    })
                })
                if (index == 0) {
                    root.setOnSingleClickListener({
                        joinCircle(circle.name, circle.number, circle.isJoin)
                    })
                    setCircleTitle(circle)
                    circleCovers[index].strokeWidth = 2f.dp().toFloat()
                }

            }

        }
    }


    private fun ItemVideoListCircleBinding.loadImageCover(covers: String) {
        GlideApp.with(root.context)
            .asBitmap()
            .load(covers.urlToWep)
            .thumbnail(
                GlideUtil.getInstance()
                    .loadRoundedTransformBitmap(root.context,
                        R.drawable.icon_circle_placeholder,
                        8f.dp())
            )
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8f.dp())))
            .apply(
                RequestOptions().placeholder(R.drawable.icon_circle_placeholder)
                    .error(R.drawable.icon_circle_placeholder)
            )
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?,
                ) {
                    val color =
                        Palette.Builder(resource).addFilter(ColorFilter.DEFAULT_FILTER).generate()
                    //  imageView.background = (ImageUtils.fastBlur(resource, 0.3f, 25f).toDrawable(resources))
                    imageColor.setImageDrawable(ImageUtils.fastBlur(resource, 0.3f, 25f)
                        .toDrawable(root.context.resources))

                    //imageView.background = resource.toDrawable(resources)
                    val backGroundColor = when {
                        color.darkVibrantSwatch != null -> color.darkVibrantSwatch!!.rgb
                        color.vibrantSwatch != null -> {
                            color.vibrantSwatch!!.rgb
                        }
                        color.mutedSwatch != null -> {
                            color.mutedSwatch!!.rgb
                        }
                        else -> {
                            ColorUtils.getColor(R.color.color_334d4e)
                        }
                    }
                    val backColor = ColorUtils.setAlphaComponent(backGroundColor, 0.9f)
                    imageOverlay.setBackgroundColor(backColor)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun ItemVideoListCircleBinding.setCircleTitle(
        circle: VideoStream.VideoStreamResult.Circle,
    ) {
        loadImageCover(circle.covers)
        textCircleName.text = circle.name
        textCircleDesc.text = circle.description
//        //xxxxx位姐妹在讨论
        textDiscuss.text = "${circle.memberCount}位姐妹在讨论"
    }

    override fun setEventData(
        it: CircleChangedEvent, item: Any, viewBinding: ItemVideoListCircleBinding,
    ) {
        if (it.type == CircleChangedEvent.JOIN ) {
            if (item is VideoStream.VideoStreamResult) {
                item.circle.forEach { circle ->
                    if (it.id.toInt() == circle.id) {
                        circle.isJoin = it.isJoin
                    }
                }
            }
        }

    }
}