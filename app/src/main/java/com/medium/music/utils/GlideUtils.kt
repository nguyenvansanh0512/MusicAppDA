package com.medium.music.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.medium.music.R

object GlideUtils {
    fun loadUrlBanner(url: String?, imageView: ImageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.img_no_image)
            return
        }
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.img_no_image)
            .dontAnimate()
            .into(imageView)
    }

    fun loadUrl(url: String?, imageView: ImageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.image_no_available)
            return
        }
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.image_no_available)
            .dontAnimate()
            .into(imageView)
    }
}