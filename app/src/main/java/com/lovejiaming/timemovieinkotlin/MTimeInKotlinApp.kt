package com.lovejiaming.timemovieinkotlin

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieSoonComeItemData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by xiaoxin on 2017/8/28.
 */
//ImageView扩展函数
fun ImageView.chAllDisplayImage(activity: Context, strUrl: String?) {
    Glide.with(activity).load(strUrl).skipMemoryCache(true).centerCrop().into(this).onDestroy()
}

//异步网络请求扩展函数
fun <T : Any?> Observable<T>.chAllAsyncToMainThread(): Observable<T> {
    return this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

//context启动活动ext
fun <T : Any?> Context.chAllstartActivity(supportParam: Map<String, String>, classObj: java.lang.Class<T>): Unit {
    val intent = Intent(this, classObj)
    for ((key, value) in supportParam) {
        intent.putExtra(key, value)
    }
    startActivity(intent)
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