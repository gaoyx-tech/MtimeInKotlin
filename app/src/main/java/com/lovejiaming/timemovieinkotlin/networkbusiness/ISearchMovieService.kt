package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by xiaoxin on 2017/8/28.
 * douban search movie interface
 * http://api.douban.com/v2/movie/search?q=战狼
 * http://api.douban.com/v2/movie/search?tag=惊悚 or 2017
 **/
//
data class Rating(val average: Double?)

//
data class Directors(val name: String?)

//
data class Images(val large: String?)

//
data class MovieSearchResultList(val rating: Rating, val genres: List<String>, val title: String?, val directors: List<Directors>, val images: Images, val id: String?, val year: String?)

data class MovieSearchResult(val subjects: List<MovieSearchResultList>, val start: Int?)
interface ISearchMovieService {
    @GET("movie/search")
    fun requestSearchMovieFromName(@Query("q") q: String, @Query("start") start: Int = 0): Observable<MovieSearchResult>

    @GET("movie/search")
    fun requesSearchMovieFromTag(@Query("tag") tag: String, @Query("start") start: Int = 0): Observable<MovieSearchResult>
}