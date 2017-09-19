package com.lovejiaming.timemovieinkotlin.views.fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.R.id.title_weeklyfocus
import com.lovejiaming.timemovieinkotlin.R.id.viewpager_weeklyfocus
import com.lovejiaming.timemovieinkotlin.mTimeDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import com.lovejiaming.timemovieinkotlin.networkbusiness.WeeklyMostFocusItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rank_list.*

/**
 * A simple [Fragment] subclass.
 * Use the [RankListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RankListFragment : Fragment() {
    //
    lateinit var m_listWeeklyFocus: MutableList<WeeklyMostFocusItem>
    val m_listWeeklyFocusCover: MutableList<ImageView> by lazy {
        MutableList(m_listWeeklyFocus.size, {
            ImageView(activity).apply {
                //                layoutParams = ViewGroup.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT)
//                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_rank_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestWeeklyFocus()
    }

    fun requestWeeklyFocus() {
        NetWorkRealCall_Time.newInstance().getRankListService()
                .requestTopList(2067)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    m_listWeeklyFocus = it.movies
                    title_weeklyfocus.text = it.topList.summary
                    initWeeklyFocusViews()
                }
    }

    fun initWeeklyFocusViews() {
        m_listWeeklyFocusCover.forEachIndexed { index, view ->
            view.mTimeDisplayImage(activity, m_listWeeklyFocus[index].posterUrl)
        }
        //
        viewpager_weeklyfocus.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view1: View?, view2: Any?) = view1 == view2

            override fun getCount(): Int = m_listWeeklyFocus.size

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(m_listWeeklyFocusCover[position])
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                container?.addView(m_listWeeklyFocusCover[position])
                return m_listWeeklyFocusCover[position]
            }
        }
        //
        viewpager_weeklyfocus.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Log.i("onPageSelected == ", position.toString())//正中间的是被选中的
            }
        })
        //
        viewpager_weeklyfocus.offscreenPageLimit = m_listWeeklyFocus.size
        viewpager_weeklyfocus.pageMargin = 40
        allviewpagercontainer.setOnTouchListener { _, motionEvent ->
            viewpager_weeklyfocus.dispatchTouchEvent(motionEvent)
        }
    }

    companion object {
        fun newInstance(): RankListFragment = RankListFragment()
    }
}
