package com.example.newsapicase

import android.view.MenuItem
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.newsapicase.presentation.FavoriteFragment
import com.example.newsapicase.presentation.NewsFragment
import com.example.newsapicase.presentation.main.MainActivity

fun AppCompatImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) return
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}

private var activeFragment: Fragment? = null

fun MainActivity.onNavigationButtonClicked(item: MenuItem): Boolean {
    val newsFragment = supportFragmentManager.findFragmentByTag(NewsFragment.TAG)
        ?: NewsFragment()
    val favoriteFragment = supportFragmentManager.findFragmentByTag(FavoriteFragment.TAG)
        ?: FavoriteFragment()

    if (activeFragment == null) {
        supportFragmentManager.beginTransaction()
            .add(R.id.center, newsFragment, NewsFragment.TAG)
            .hide(newsFragment)
            .add(R.id.center, favoriteFragment, FavoriteFragment.TAG)
            .hide(favoriteFragment)
            .commit()

        activeFragment = favoriteFragment
    }

    return when (item.itemId) {
        R.id.news -> {
            if (activeFragment == newsFragment) {
                return false
            }

            supportFragmentManager.beginTransaction()
                .show(newsFragment)
                .hide(activeFragment!!)
                .commit()

            activeFragment = newsFragment
            true
        }

        R.id.favorites -> {
            if (activeFragment == favoriteFragment) {
                return false
            }

            supportFragmentManager.beginTransaction()
                .show(favoriteFragment)
                .hide(activeFragment!!)
                .commit()

            activeFragment = favoriteFragment
            true
        }

        else -> false
    }
}