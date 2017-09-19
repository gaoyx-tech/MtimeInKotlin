package com.lovejiaming.timemovieinkotlin.views.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R

/**
 * A simple [Fragment] subclass.
 * Use the [RankListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RankListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_rank_list, container, false)
    }

    companion object {
        fun newInstance(): RankListFragment  = RankListFragment()

    }
}
