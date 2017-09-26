package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by xiaoxin on 2017/8/28.
 **/

//
data class MovieSearchResultItem(val id: Int, val name: String?, val img: String?, val movieType: String?, val realTime: String?, val rating: String?, val rYear: Int?)

data class MovieSearchResult(val movies: List<MovieSearchResultItem>)

//tag
data class TagMovieSearchItem(val img: String?, val titleCn: String?, val type: String?, val ratingFinal: Double?, val movieId: Int?)

data class TagMovieResultSubResult(val movieModelList: List<TagMovieSearchItem>, val pageNum: Int, val totalCount: Int)
data class TagMovieSearchResult(val data: TagMovieResultSubResult)

interface ISearchMovieService {
    @FormUrlEncoded
    @POST("Showtime/SearchVoice.api")
    fun requestSearchMovieFromName(@Field("searchType") searchType: Int = 3, @Field("Keyword") Keyword: String, @Field("pageIndex") pageIndex: Int = 1, @Field("locationId") locationId: Int = 290)
            : Observable<MovieSearchResult>

    @GET("Movie/SearchMovie.api")
    fun requesSearchMovieFromTag(@Query("years") years: String, @Query("genreTypes") genreTypes: String?, @Query("areas") areas: String = "-1",
                                 @Query("sortType") sortType: Int = 3, @Query("sortMethod") sortMethod: Int = 1, @Query("pageIndex") pageIndex: Int = 1): Observable<TagMovieSearchResult>
}