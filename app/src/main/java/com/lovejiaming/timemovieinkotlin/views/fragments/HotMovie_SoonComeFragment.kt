package com.lovejiaming.timemovieinkotlin.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.HotMovieSoonComeAdapter
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import kotlinx.android.synthetic.main.fragment_hot_movie_sooncome.*

/**
 * A simple [Fragment] subclass.
 * Use the [HotMovie_SoonComeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotMovie_SoonComeFragment : Fragment() {
    //
    val mAdapter: HotMovieSoonComeAdapter by lazy {
        //点击想看回调
        HotMovieSoonComeAdapter(activity, { action ->
            val snackbar = Snackbar.make(snack_container, action, Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(Color.parseColor("#bf360c"))
            snackbar.view.findViewById<TextView>(R.id.snackbar_text).setTextAppearance(R.style.SnackbarTextStyle)
            snackbar.show()
        })
    }

    //想看，snackbar------------>SAM转化掉了
//    interface IWannaSeeListener {
//        fun wannaSee(action: String)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_hot_movie_sooncome, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sooncome_recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        sooncome_recyclerview.adapter = mAdapter

        //
        swipe_refresh.isRefreshing = true
        swipe_refresh.setOnRefreshListener {
            requestNetWorkData()
        }
        requestNetWorkData()
    }

    fun requestNetWorkData() {
        NetWorkRealCall_Time.newInstance()
                .getHotMovieService()
                .requestSoonComeHotMovie("290")
                .chAllAsyncToMainThread()
                .subscribe({
                    swipe_refresh.isRefreshing = false
                    mAdapter.addAllData(it)
                }, { t -> Log.i("throwble == ", t.toString()) })
    }

    companion object {
        fun newInstance(): HotMovie_SoonComeFragment = HotMovie_SoonComeFragment()
    }

}
