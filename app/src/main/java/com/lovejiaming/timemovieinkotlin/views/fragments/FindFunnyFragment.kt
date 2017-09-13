package com.lovejiaming.timemovieinkotlin.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R
import kotlinx.android.synthetic.main.fragment_find_funny.*

/**
 * A simple [Fragment] subclass.
 * Use the [FindFunnyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFunnyFragment : Fragment() {
    //
    val m_listFragments: List<Fragment> by lazy {
        arrayListOf(FindFunny_NewsFragment.newInstance(),
                FindFunny_TrailersFragment.newInstance(),
                FindFunny_ReviewFragment.newInstance("", ""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_find_funny, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        val arrTitleName = arrayListOf("MTIME新闻", "MTIME预告片", "MTIME影评")
        funny_viewpager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = m_listFragments[position]
            override fun getCount(): Int = 3
            override fun getPageTitle(position: Int): CharSequence = arrTitleName[position]
        }
        funny_viewpager.offscreenPageLimit = 0
        findfunny_tablayout.setupWithViewPager(funny_viewpager)
    }

    companion object {
        fun newInstance(): FindFunnyFragment = FindFunnyFragment()
    }
}
