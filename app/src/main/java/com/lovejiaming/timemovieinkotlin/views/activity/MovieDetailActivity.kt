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
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
        RequestDetaiOfAllPerson()
    }

    override fun onDestroy() {
        super.onDestroy()
        m_DiposableDetail.dispose()
        m_DisposblePerson.dispose()
        Glide.get(this).clearMemory()
    }

    fun RequestDetaiOfAllPerson() {
        m_DisposblePerson = NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieDetailPersonlList(m_sMovieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter.addPersonList(it)
                    RequestMovieDetailShortComment()
                }
    }

    fun RequestMovieDetailInfo() {
        m_DiposableDetail = NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieDetail("290", m_sMovieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mAdapter.insertDetailData(it, movieId = m_sMovieId)
                    swipe_detail.isRefreshing = false
                }, { Log.i("neterror", "neterrr") })
    }

    fun RequestMovieDetailShortComment() {
        NetWorkRealCallMtime.newInstance().getMovieDetailService()
                .requestMovieAllComment(m_sMovieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    MovieDetailAdapter.MovieDetailCommentAdapter.insertAllComment(it.data?.cts!!)
                    RequestMovieDetailInfo()
                }, { Log.i("ctscts === ", "error") })
    }

    fun initView() {
        swipe_detail.isRefreshing = true
        //
        detail_toolbar.title = "<< ${intent.getStringExtra("moviename")} >>  MTIME "
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
