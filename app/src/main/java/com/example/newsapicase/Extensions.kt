package com.example.newsapicase

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

fun AppCompatImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}