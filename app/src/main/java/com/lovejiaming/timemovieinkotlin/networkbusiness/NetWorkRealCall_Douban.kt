package com.lovejiaming.timemovieinkotlin.networkbusiness

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by xiaoxin on 2017/8/29.
 */
class NetWorkRealCall_Douban private constructor() {
    private var mRetrofit: Retrofit? = null

    init {
        val okhttpClient = OkHttpClient.Builder().build()
        mRetrofit = Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getMovieSearchService() = mRetrofit?.create(ISearchMovieService::class.java)!!

    companion object {
        var BASE_URL = "http://api.douban.com/v2/"
        val instance: NetWorkRealCall_Douban by lazy {
            NetWorkRealCall_Douban()
        }

        fun newInstance() = instance
    }
}