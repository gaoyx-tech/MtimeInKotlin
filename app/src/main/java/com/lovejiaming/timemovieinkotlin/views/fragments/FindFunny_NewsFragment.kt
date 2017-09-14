package com.lovejiaming.timemovieinkotlin.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.FindFunnyNewsAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_funny_news.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FindFunny_NewsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FindFunny_NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFunny_NewsFragment : Fragment() {

    private val mAdapter: FindFunnyNewsAdapter by lazy {
        FindFunnyNewsAdapter(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_find_funny_news, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_news.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        recycler_news.adapter = mAdapter
        swipe_refresh_funnynews.isRefreshing = true//第一次
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        recycler_news?.let {
            recycler_news.visibility = View.GONE
            recycler_news.scrollToPosition(0)
            swipe_refresh_funnynews.isRefreshing = true//除了第一次
        }
        if (isVisibleToUser) {
            NetWorkRealCall_Time.newInstance().getFindFunnyService()
                    .requestFunnyNewsList(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        mAdapter.insertNewsData(it)
                        recycler_news.visibility = View.VISIBLE
                        swipe_refresh_funnynews.isRefreshing = false
                    }
        } else {
            onStop()
        }
    }

    override fun onStop() {
        super.onStop()
        Glide.get(activity).clearMemory()
    }

    companion object {
        fun newInstance(): FindFunny_NewsFragment = FindFunny_NewsFragment()
    }
}
