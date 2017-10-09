package com.lovejiaming.timemovieinkotlin.views.activity

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieDetailAdapter
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AutoLayoutActivity() {
    //
    val mAdapter: MovieDetailAdapter by lazy {
        MovieDetailAdapter(this)
    }
    lateinit var m_DiposableDetail: Disposable
    lateinit var m_DisposblePerson: Disposable
    val m_sMovieId: String by lazy {
        intent.getStringExtra("movieid")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        initView()
        requestDetaiOfAllPerson()
    }

    override fun onDestroy() {
        super.onDestroy()
        m_DiposableDetail.dispose()
        m_DisposblePerson.dispose()
        Glide.get(this).clearMemory()
    }

    private fun requestDetaiOfAllPerson() {
        m_DisposblePerson = NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieDetailPersonlList(m_sMovieId)
                .chAllAsyncToMainThread()
                .subscribe {
                    mAdapter.addPersonList(it)
                    requestMovieDetailShortComment()
                }
    }

    private fun requestMovieDetailInfo() {
        m_DiposableDetail = NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieDetail("290", m_sMovieId)
                .chAllAsyncToMainThread()
                .subscribe({
                    mAdapter.insertDetailData(it, movieId = m_sMovieId)
                    swipe_detail.isRefreshing = false
                }, { Log.i("neterror", "neterrr") })
    }

    private fun requestMovieDetailShortComment() {
        NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieAllComment(m_sMovieId)
                .chAllAsyncToMainThread()
                .subscribe({
                    mAdapter.insertAllComment(it.data?.cts!!)
                    requestMovieDetailInfo()
                }, { Log.i("ctscts === ", "error") })
    }

    private fun initView() {
        swipe_detail.isRefreshing = true
        //
        detail_toolbar.title = "<< ${intent.getStringExtra("moviename")} >>  "
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detail_toolbar.setNavigationOnClickListener { finish() }
        //
        detail_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutCompat.VERTICAL, false)
        detail_recyclerview.adapter = mAdapter
        detail_recyclerview.addItemDecoration(SimpleItemDecorationVer())
    }
}

class SimpleItemDecorationVer : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.bottom = 35
    }
}

class SimpleItemDecorationHor : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.right = 35
    }
}
