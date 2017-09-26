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
import com.lovejiaming.timemovieinkotlin.adapter.HotMovieNowadaysAdapter
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieNowadaysItemData
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import kotlinx.android.synthetic.main.fragment_hot_movie_nowadays.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HotMovie_NowadaysFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HotMovie_NowadaysFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotMovie_NowadaysFragment : Fragment() {
    //SAM转换
//    interface IClickHaveSeenBtn {
//        fun click(action: String)
//    }

    val mHotAdapter: HotMovieNowadaysAdapter by lazy {
        HotMovieNowadaysAdapter(activity, { action ->
            val snackbar = Snackbar.make(snack_container_now, action, Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(Color.parseColor("#bf360c"))
            snackbar.view.findViewById<TextView>(R.id.snackbar_text).setTextAppearance(R.style.SnackbarTextStyle)
            snackbar.show()
        })
    }
    lateinit var m_listResponse: List<HotMovieNowadaysItemData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_hot_movie_nowadays, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        requestNetWorkData()
    }

    fun requestNetWorkData() {
        NetWorkRealCallMtime.newInstance()
                .getHotMovieService()
                .requestNowadaysHotMovie("290")
                .chAllAsyncToMainThread()
                .subscribe({ t ->
                    swipe_refresh.isRefreshing = false
                    //it是解析成对象后的
                    m_listResponse = t.ms
                    mHotAdapter.insertDatas(t.ms.toMutableList())
                }, { t -> Log.i("throwble == ", t.toString()) })
    }

    fun initViews() {
        nowadays_recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        nowadays_recyclerview.adapter = mHotAdapter
        //
        swipe_refresh.isRefreshing = true
        swipe_refresh.setOnRefreshListener {
            labelScore.setColor(Color.BLACK)
            labelHot.setColor(Color.BLACK)
            requestNetWorkData()
        }
        //
        labelScore.setText("按分数排序")
        labelHot.setText("按热度排序")
        labelScore.setOnClickListener {
            labelHot.setColor(Color.BLACK)
            nowadays_recyclerview.scrollToPosition(0)
            mHotAdapter.insertDatas(m_listResponse.sortedByDescending { it.r }.toMutableList())
        }
        labelHot.setOnClickListener {
            nowadays_recyclerview.scrollToPosition(0)
            labelScore.setColor(Color.BLACK)
            mHotAdapter.insertDatas(m_listResponse.sortedByDescending { it.wantedCount }.toMutableList())
        }
    }

    companion object {
        fun newInstance(): HotMovie_NowadaysFragment = HotMovie_NowadaysFragment()
    }
}
