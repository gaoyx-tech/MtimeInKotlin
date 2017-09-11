package com.lovejiaming.timemovieinkotlin.views.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R

/**
 * A simple [Fragment] subclass.
 * Use the [TrailerNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrailerNewsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_trailer_news, container, false)
    }

    companion object {
        fun newInstance(): TrailerNewsFragment = TrailerNewsFragment()
    }
}
