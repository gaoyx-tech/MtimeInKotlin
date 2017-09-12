package com.lovejiaming.timemovieinkotlin.views.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieDetailAllTrailerAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_detail_of_trailer.*

class MovieDetailOfTrailerActivity : AutoLayoutActivity() {
    //
    val mAdapter: MovieDetailAllTrailerAdapter by lazy {
        MovieDetailAllTrailerAdapter(this)
    }
    //for load more
    var m_nPageTotal = -1
    var m_nCurrentPageIndex = 1
    var m_sAcitonType = "refresh"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_of_trailer)
        initViews()
        startRequestAllTrailers()
    }

    fun startRequestAllTrailers() {
        NetWorkRealCall_Time.newInstance().getMovieDetailService()
                .requestMovieAllTrailers(intent.getIntExtra("movieid", -1), m_nCurrentPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //分頁
                    m_nPageTotal = it.totalPageCount
                    //
                    mAdapter.addAllTrailersData(it, m_sAcitonType)
                }
    }

    fun initViews() {
        setSupportActionBar(toolbar_detailalltrailer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detailalltrailer.setNavigationOnClickListener { finish() }
        //
        recyclerview_moretrailer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview_moretrailer.adapter = mAdapter
        //
        recyclerview_moretrailer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView?.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()//可见的最后一个的索引
                    val visibleItemCount = layoutManager.childCount//可见范围内有多少个
                    if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                            (lastVisibleItemPosition) == layoutManager.itemCount-1) && m_nCurrentPageIndex != m_nPageTotal) {
                        ++m_nCurrentPageIndex
                        m_sAcitonType = "loadmore"
                        startRequestAllTrailers()
                    }
                }
            }
        })

    }
}
