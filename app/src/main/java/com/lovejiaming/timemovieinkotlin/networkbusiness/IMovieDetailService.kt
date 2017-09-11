package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

/**
 * Created by choujiaming on 2017/9/1.
 */

//
data class Director(val directorId: Int?, val directorName: String?, val directorNameEn: String?, val directorImg: String?)

data class Actor(val actorId: Long?, val actor: String?, val actorEn: String?, val actorImg: String?, val roleName: String?)
data class Video(val url: String?, val image: String?, val title: String?)
data class MovieDetailInfo(val image: String?, val titleCn: String?, val titleEn: String?, val rating: String?, val year: String?, val content: String?, val type: List<String>?,
                           val runTime: String?, val url: String?, val commonSpecial: String?, val director: Director, val actorList: List<Actor>, val is3D: Boolean?, val isIMAX: Boolean?,
                           val isIMAX3D: Boolean?, val isDMAX: Boolean?, val images: List<String>, val videos: List<Video>)

//
data class PersonDetail(val id: Int, val name: String, val nameEn: String, val image: String) : Serializable

data class PersonDetailItem(val typeName: String, val persons: ArrayList<PersonDetail>) : Serializable
data class PersonDetailAll(val types: ArrayList<PersonDetailItem>) : Serializable

//
data class VideoList(val id: Int, val hightUrl: String, val image: String, val title: String, val length: Int)

data class TrailersData(val totalPageCount: Int, val videoList: List<VideoList>)

//
data class DetailCommentItem(val ca: String?, val caimg: String?, val cal: String?, val cd: Long?, val ce: String?, val cr: Float?)

data class CTS(val cts: ArrayList<DetailCommentItem>)
data class CommentAll(val data: CTS?)

interface IMovieDetailService {
    //
    @GET("movie/detail.api?")
    fun requestMovieDetail(@Query("locationId") locationId: String, @Query("movieId") movieId: String): Observable<MovieDetailInfo>

    @GET("Movie/MovieCreditsWithTypes.api?")
    fun requestMovieDetailPersonlList(@Query("movieId") movieId: Int): Observable<PersonDetailAll>

    @GET("Movie/Video.api?")
    fun requestMovieAllTrailers(@Query("movieId") movieId: Int, @Query("pageIndex") pageIndex: Int): Observable<TrailersData>

    @GET("Showtime/HotMovieComments.api?")
    fun requestMovieAllComment(@Query("movieId") movieId: Int, @Query("pageIndex") pageIndex: Int = 1): Observable<CommentAll>
}
