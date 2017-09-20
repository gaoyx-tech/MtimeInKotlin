package com.lovejiaming.timemovieinkotlin.views.ownerdraw

import android.support.v4.view.ViewPager
import android.view.View
import android.support.v4.view.ViewCompat.setScaleY
import android.support.v4.view.ViewCompat.setScaleX
import android.util.Log


/**
 * Created by gaoyx on 2017/9/19.
 */
class MtimeViewPagerTransform : ViewPager.PageTransformer {

    override fun transformPage(page: View?, position: Float) {
        val MIN_SCALE = 0.90f
        val MIN_ALPHA = 0.5f

        if (position < -1 || position > 1) {
            page?.alpha = MIN_ALPHA
            page?.scaleX = MIN_SCALE
            page?.scaleY = MIN_SCALE
        } else if (position <= 1) { // [-1,1]
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            if (position < 0) {
                val scaleX = 1 + 0.1f * position
                page?.scaleX = scaleX
                page?.scaleY = scaleX
            } else {
                val scaleX = 1 - 0.1f * position
                page?.scaleX = scaleX
                page?.scaleY = scaleX
            }
            page?.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
        }
    }
}