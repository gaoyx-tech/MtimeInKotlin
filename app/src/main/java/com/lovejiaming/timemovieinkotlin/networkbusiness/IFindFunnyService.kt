package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by xiaoxin on 2017/9/12.
 */
//news
data class NewsArray(val newsList: List<NewsItem>)
data class NewsItem(val id: Int?, val image: String?, val publishTime: Long?, val title: String?, val title2: String?, val images: List<ImageItem>)
data class ImageItem(val url1: String?)

interface IFindFunnyService {
    @GET("News/NewsList.api?")
    fun requestFunnyNewsList(@Query("pageIndex") pageIndex: Int): Observable<NewsArray>
}