package com.lovejiaming.timemovieinkotlin.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.FindFunnyReviewAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import com.lovejiaming.timemovieinkotlin.views.activity.SimpleItemDecorationVer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_funny_review.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FindFunny_ReviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FindFunny_ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFunny_ReviewFragment : Fragment() {

    val mAdapter: FindFunnyReviewAdapter by lazy {
        FindFunnyReviewAdapter(this.activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_find_funny_review, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        recyclerview_funnyreview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        swipe_refresh_funnyreview.isRefreshing = true
        recyclerview_funnyreview.adapter = mAdapter
        recyclerview_funnyreview.addItemDecoration(SimpleItemDecorationVer())
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        recyclerview_funnyreview?.let {
            swipe_refresh_funnyreview.isRefreshing = true
            recyclerview_funnyreview.visibility = View.GONE
            recyclerview_funnyreview.scrollToPosition(0)
        }
        //
        if (isVisibleToUser) {
            NetWorkRealCall_Time.newInstance().getFindFunnyService()
                    .requestFunnyReview()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        swipe_refresh_funnyreview.isRefreshing = false
                        recyclerview_funnyreview.visibility = View.VISIBLE
                        mAdapter.insertAllReviews(it)
                    }
        } else {
            onPause()
        }
    }

    companion object {
        fun newInstance(): FindFunny_ReviewFragment = FindFunny_ReviewFragment()
    }
}