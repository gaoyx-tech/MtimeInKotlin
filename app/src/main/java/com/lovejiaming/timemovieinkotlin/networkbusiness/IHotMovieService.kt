package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

/**
 * Created by xiaoxin on 2017/8/24.
 */

//每一个
data class HotMovieSoonComeItemData(val id: Int?, val title: String?, val wantedCount: Int?, val image: String?, val rMonth: Int?, val rDay: Int?, val releaseDate: String, val director: String, val type: String)

//即将放映总信息
data class HotMovieSoonComeAllData(val attention: List<HotMovieSoonComeItemData>, val moviecomings: List<HotMovieSoonComeItemData>)

//显示在列表中的信息
data class HotMovieNowadaysItemData(val actors: String?, val img: String?, val tCn: String?, val r: Double?, val id: Int?, val wantedCount: Int?)

//正在热映总信息
data class HotMovieNowadaysArrayData(val bImg: String?, val ms: List<HotMovieNowadaysItemData>)

data class CinemaFeature(val has3D: Int?, val hasFeature4D: Int?, val hasFeature4K: Int?, val hasFeatureDolby: Int?, val hasFeatureHuge: Int?, val hasIMAX: Int?,
                         val hasLoveseat: Int?, val hasPark: Int?, val hasServiceTicket: Int?, val hasVIP: Int?, val hasWifi: Int?) : Serializable

data class CinemaData(val address: String?, val baiduLatitude: Double?, val baiduLongitude: Double?, val cinameName: String?, val feature: CinemaFeature?)

interface IHotMovieService {
    @GET("Showtime/LocationMovies.api")
    fun requestNowadaysHotMovie(@Query("locationId") locationId: String): Observable<HotMovieNowadaysArrayData>

    @GET("Movie/MovieComingNew.api")
    fun requestSoonComeHotMovie(@Query("locationId") locationId: String): Observable<HotMovieSoonComeAllData>

    @GET("OnlineLocationCinema/OnlineCinemasByCity.api")
    fun requestAllCinemas(@Query("locationId") locationId: String): Observable<List<CinemaData>>
}