package com.lovejiaming.timemovieinkotlin.views.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieDetailAllPersonAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonDetailAll
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_movie_detail_of_person.*

class MovieDetailOfPersonActivity : AutoLayoutActivity() {
    //
    lateinit var mPersonAll: PersonDetailAll
    //
    val mAdapter: MovieDetailAllPersonAdapter by lazy {
        MovieDetailAllPersonAdapter(this, mPersonAll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_of_person)
        recyclerview_allperson.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //
        setSupportActionBar(toolbar_detailallperson)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detailallperson.setNavigationOnClickListener { finish() }

        setAllData()
    }

    fun setAllData() {
        mPersonAll = intent.getSerializableExtra("allperson") as PersonDetailAll
        Log.i("personall -- ", mPersonAll.toString())
        recyclerview_allperson.adapter = mAdapter
    }
}
