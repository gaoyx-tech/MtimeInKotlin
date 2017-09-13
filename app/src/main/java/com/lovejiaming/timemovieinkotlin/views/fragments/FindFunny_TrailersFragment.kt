package com.lovejiaming.timemovieinkotlin.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.FindFunnyTrailersAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import com.lovejiaming.timemovieinkotlin.views.activity.SimpleItemDecorationVer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_funny_trailers.*

class FindFunny_TrailersFragment : Fragment() {

    val mAdapter: FindFunnyTrailersAdapter by lazy {
        FindFunnyTrailersAdapter(this.activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_find_funny_trailers, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_all_trailer.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_all_trailer.addItemDecoration(SimpleItemDecorationVer())
        recycler_all_trailer.adapter = mAdapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        recycler_all_trailer?.let {
            recycler_all_trailer.visibility = View.GONE
            recycler_all_trailer.scrollToPosition(0)
        }

        if (isVisibleToUser) {
            NetWorkRealCall_Time.newInstance().getFindFunnyService()
                    .requestFunnyTrailerList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mAdapter.insertAllTrailers(it)
                        recycler_all_trailer.visibility = View.VISIBLE
                    }
        } else {
            onStop()
        }
    }

    companion object {
        fun newInstance(): FindFunny_TrailersFragment = FindFunny_TrailersFragment()
    }
}
