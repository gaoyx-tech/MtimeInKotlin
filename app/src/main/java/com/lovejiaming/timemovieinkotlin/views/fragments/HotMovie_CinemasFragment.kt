package com.lovejiaming.timemovieinkotlin.views.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.HotMovieCinemaAdapter
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import kotlinx.android.synthetic.main.fragment_hot_movie_cinemas.*

/**
 * A simple [Fragment] subclass.
 * Use the [HotMovie_CinemasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotMovie_CinemasFragment : Fragment() {
    //
    val mAdapter: HotMovieCinemaAdapter by lazy {
        HotMovieCinemaAdapter(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_hot_movie_cinemas, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NetWorkRealCallMtime.newInstance()
                .getHotMovieService()
                .requestAllCinemas("290")
                .chAllAsyncToMainThread()
                .subscribe({
                    mAdapter.insertCinemaData(it.toMutableList())
                }, { t -> Log.i("throwble == ", t.toString()) })
        //
        cinema_recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cinema_recyclerview.adapter = mAdapter
    }

    companion object {
        fun newInstance(): HotMovie_CinemasFragment = HotMovie_CinemasFragment()
    }

}
