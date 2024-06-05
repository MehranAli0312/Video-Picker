package com.example.gallery.extensions

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery.R
import com.google.android.material.imageview.ShapeableImageView
import java.util.concurrent.TimeUnit


fun ShapeableImageView.loadThumb(imagePath: String) {
    Glide.with(this.context).load(imagePath)
        .override(500)
        .placeholder(R.drawable.frame_placer)
        .thumbnail(0.4f)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .skipMemoryCache(false).apply {
            into(this@loadThumb)
        }
}

fun Long.formatDuration(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.MINUTES.toSeconds(1)

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}