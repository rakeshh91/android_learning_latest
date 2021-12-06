package com.rakesh.newsappsample.presentation.util

import android.widget.ImageView
import com.rakesh.newsappsample.R
import com.rakesh.newsappsample.di.modules.GlideApp

object ImageHelper {

    //We need to get the generic placeholder images
    fun loadImage(
        view: ImageView, imgUrl: String,
        errorResourceId: Int = R.drawable.image_news_placeholder,
        placeHolderResourceId: Int = R.drawable.image_news_placeholder
    ) {
        GlideApp.with(view).load(imgUrl)
            .error(errorResourceId)
            .placeholder(placeHolderResourceId)
            .into(view)
    }
}