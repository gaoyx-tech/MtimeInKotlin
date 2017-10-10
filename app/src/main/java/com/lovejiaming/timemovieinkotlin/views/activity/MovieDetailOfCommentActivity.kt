package com.lovejiaming.timemovieinkotlin.views.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieDetailAllCommentAdapter
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_movie_detail_of_comment.*

class MovieDetailOfCommentActivity : AutoLayoutActivity() {

    private var m_nPageCount: Int = 2
    private val mAdapter: MovieDetailAllCommentAdapter by lazy {
        MovieDetailAllCommentAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_of_comment)
        //
        recycler_all_shortcomment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_all_shortcomment.adapter = mAdapter
        refresh_allcomment.isRefreshing = true
        requestMoreComment()
    }

    private fun requestMoreComment() {
        NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieAllComment(intent.getStringExtra("movieid"), m_nPageCount)
                .chAllAsyncToMainThread()
                .subscribe {
                    mAdapter.addCommentData(it.data?.cts!!)
                    ++m_nPageCount
                    refresh_allcomment.isRefreshing = false
                }
    }
}
