package com.lovejiaming.timemovieinkotlin.views.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.MTimeInKotlinApp
import com.lovejiaming.timemovieinkotlin.adapter.HotMovieSoonComeOfAllAdapter
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_all_come_soon_movie.*

class AllComeSoonMovieActivity : AutoLayoutActivity() {

    val mAdapter: HotMovieSoonComeOfAllAdapter by lazy {
        HotMovieSoonComeOfAllAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_come_soon_movie)
        initViews()
        setAllComeData()
    }

    fun initViews() {
        allcometoolbar.title = "${intent.getIntExtra("month", -1)}月即将上映电影"
        setSupportActionBar(allcometoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        allcometoolbar.setNavigationOnClickListener { finish() }
        //
        recycler_allcomesoon.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun setAllComeData() {
        val groupByDay = (application as MTimeInKotlinApp).mComeSoonMonData?.sortedBy { it.rDay }?.groupBy { it.rDay }
        //
        mAdapter.addAllGroupData(groupByDay!!)
        recycler_allcomesoon.adapter = mAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }
}
