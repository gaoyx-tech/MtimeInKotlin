package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by gaoyx on 2017/9/19.
 */
//
data class WeeklyMostFocusItem(val id: Int?, val name: String?, val posterUrl: String?)
data class WeeklyMostFocusTitle(val summary: String?)
data class WeeklyMostFocusData(val movies: MutableList<WeeklyMostFocusItem>, val topList: WeeklyMostFocusTitle)

interface IRankListService {
    @GET("TopList/TopListDetailsByRecommend.api?")
    fun requestTopList(@Query("pageSubAreaID") pageSubAreaID: Int, @Query("pageIndex") pageIndex: Int = 1, @Query("locationId") locationId: Int = 290): Observable<WeeklyMostFocusData>

}