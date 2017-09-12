package com.lovejiaming.timemovieinkotlin.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R
import kotlinx.android.synthetic.main.fragment_hot_movie.*

/**
 * A simple [Fragment] subclass.
 * Use the [HotMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * 正在热映和即将热映
 */
class HotMovieFragment : Fragment() {
    //titles
    val mArrTitles: List<String> by lazy {
        listOf("正在热映 ", "即将热映 ", "本地影院 ")
    }
    //m_listFragments
    val mArrFragments: List<Fragment> by lazy {
        listOf(HotMovie_NowadaysFragment.newInstance(),
                HotMovie_SoonComeFragment.newInstance(),
                HotMovie_CinemasFragment.newInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_hot_movie, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }

    fun initTabLayout() {
        titleviewpager.offscreenPageLimit = 3
        titleviewpager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = mArrFragments[position]

            override fun getCount(): Int = mArrFragments.size

            override fun getPageTitle(position: Int): CharSequence = mArrTitles[position]
        }
        titletablayout.setupWithViewPager(titleviewpager)
    }

    companion object {
        fun newInstance(): HotMovieFragment = HotMovieFragment()
    }

}
