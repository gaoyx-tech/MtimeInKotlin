package com.lovejiaming.timemovieinkotlin

import android.app.Application
import android.content.Context
import android.widget.ImageView
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieSoonComeItemData

/**
 * Created by xiaoxin on 2017/8/28.
 */
//ImageView扩展函数
fun ImageView.mTimeDisplayImage(activity: Context, strUrl: String?) {
    Glide.with(activity).load(strUrl).skipMemoryCache(true).centerCrop().into(this).onDestroy()
}

class MTimeInKotlinApp : Application() {
    //
    var mComeSoonMonData: List<HotMovieSoonComeItemData>? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    override fun onCreate() {
        super.onCreate()
        SDKInitializer.initialize(this)
        SDKInitializer.setCoordType(CoordType.BD09LL)
    }

}