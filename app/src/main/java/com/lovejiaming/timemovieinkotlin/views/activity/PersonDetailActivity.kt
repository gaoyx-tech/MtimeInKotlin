package com.lovejiaming.timemovieinkotlin.views.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.R.id.*
import com.lovejiaming.timemovieinkotlin.adapter.PersonDetailAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_person_detail.*

class PersonDetailActivity : AutoLayoutActivity() {

    val mAdapter: PersonDetailAdapter by lazy {
        PersonDetailAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)
        initViews()
        requestPersonMovies()
    }

    fun requestPersonDetail() {
        NetWorkRealCall_Time.newInstance().getPersonDetailService()
                .requestPersonDetail(intent.getIntExtra("personid", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    swipe_person.isRefreshing = false
                    mAdapter.insertPersonDetailData(it)
                    recycler_persondetail.scrollToPosition(0)
                }, { Log.i("error", "neterror") })
    }

    fun requestPersonMovies() {
        NetWorkRealCall_Time.newInstance().getPersonDetailService()
                .requestPersonAllMovie(intent.getIntExtra("personid", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    PersonDetailAdapter.g_listAllMovies.clear()
                    PersonDetailAdapter.g_listAllMovies = it
                    requestPersonDetail()
                }, { Log.i("neterror", "neterror") })
    }

    fun initViews() {
        //
        swipe_person.isRefreshing = true

        recycler_persondetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_persondetail.addItemDecoration(SimpleItemDecorationVer())
        recycler_persondetail.adapter = mAdapter
        //
        toolbar_persondetail.title = "${intent.getStringExtra("personname")} "
        setSupportActionBar(toolbar_persondetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_persondetail.setNavigationOnClickListener { finish() }
    }
}
