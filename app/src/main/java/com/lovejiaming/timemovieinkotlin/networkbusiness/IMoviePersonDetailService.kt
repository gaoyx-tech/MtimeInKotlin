package com.lovejiaming.timemovieinkotlin.networkbusiness

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by gaoyx on 2017/9/8.
 */
//
data class AwardDetailItem(val festivalEventYear: String?, val awardName: String?, val movieId: Int?, val movieTitle: String?, val movieYear: String?, val image: String?)

data class AwardsDetailArray(val festivalId: Int, val winAwards: ArrayList<AwardDetailItem>, val nominateAwards: ArrayList<AwardDetailItem>)
data class HotMovie(val movieId: Int, val movieCover: String, val movieTitleCn: String, val ratingFinal: Double, val type: String)
data class PersonDetailResponse(val nameCn: String?, val nameEn: String?, val birthYear: Int, val birthMonth: Int, val birthDay: Int, val ratingFinal: String, val address: String, val profession: String,
                                val content: String, val image: String, val url: String, val hotMovie: HotMovie, val awards: ArrayList<AwardsDetailArray>)

//
data class PersonOffices(val name: String?)

data class PersonAllMovie(val id: Int?, val image: String?, val name: String?, val offices: ArrayList<PersonOffices>?)

interface IMoviePersonDetailService {
    @GET("person/detail.api?")
    fun requestPersonDetail(@Query("personid") personid: Int): Observable<PersonDetailResponse>

    @GET("person/movie.api?")
    fun requestPersonAllMovie(@Query("personid") personid: Int): Observable<ArrayList<PersonAllMovie>>
}