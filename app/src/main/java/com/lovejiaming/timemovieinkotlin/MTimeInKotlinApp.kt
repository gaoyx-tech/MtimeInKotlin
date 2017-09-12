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
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
    }

}