package com.lovejiaming.timemovieinkotlin.views.ownerdraw

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by gaoyx on 2017/9/19.
 */
class MtimeViewPagerTransform : ViewPager.PageTransformer {

    override fun transformPage(page: View?, position: Float) {
        if (position <= -1f) {
            page?.scaleX = -0.9f
            page?.scaleY = -0.9f
        }
    }
}