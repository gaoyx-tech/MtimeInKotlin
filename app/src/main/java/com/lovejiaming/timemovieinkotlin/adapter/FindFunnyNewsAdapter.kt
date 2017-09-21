package com.lovejiaming.timemovieinkotlin.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.AdvertiseItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.NewsArray
import com.lovejiaming.timemovieinkotlin.networkbusiness.NewsItem
import com.zhy.autolayout.utils.AutoUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by xiaoxin on 2017/9/12.
 */
class FindFunnyNewsAdapter(val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val PICS_THREE = 1
    private val PICS_ONE = 2
    private val BANNER_ADVERTISE = 3
    //
    var m_listNewsData: List<NewsItem>? = null
    //
    private var mLastPosition = -1
    //
    var m_nPlayIndex = 0
    var m_listAdvertiseImages: MutableList<ImageView> = mutableListOf()
    var m_listViews: MutableList<View> = mutableListOf()

    fun insertAdvertiseData(data: MutableList<AdvertiseItem>) {
        //
        m_listAdvertiseImages.clear()
        m_listViews.clear()
        //
        (0 until data.size).forEachIndexed { index, _ ->
            //
            val iv_adv = ImageView(ctx)
            iv_adv.chAllDisplayImage(ctx, data[index].img)
            iv_adv.scaleType = ImageView.ScaleType.CENTER_CROP
            m_listAdvertiseImages.add(iv_adv)
            //
            val view = View(ctx)
            view.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            view.setBackgroundColor(Color.BLACK)
            m_listViews.add(view)
        }
        //初始化第一个为白
        m_listViews[0].setBackgroundColor(Color.WHITE)
    }

    fun insertNewsData(data: NewsArray) {
        this.m_listNewsData = data.newsList
        mLastPosition = -1
        notifyDataSetChanged()
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            PICS_ONE -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_findfunny_news_pic1, null)
                return PicOneViewHolder(view)
            }
            PICS_THREE -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_findfunny_news_pic3, null)
                return PicThreeViewHolder(view)
            }
            BANNER_ADVERTISE -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_findfunny_news_banner, null)
                return BannerViewHolder(view)
            }
        }
        return null!!
    }

    override fun getItemCount(): Int = m_listNewsData?.size ?: 0 + 1

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return BANNER_ADVERTISE
        else if (m_listNewsData?.get(position - 1)?.images?.size!! > 0)
            return PICS_THREE
        else
            return PICS_ONE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            PICS_ONE -> {
                with(holder!! as PicOneViewHolder) {
                    iv_pic1_newscover1?.chAllDisplayImage(ctx, m_listNewsData?.get(position - 1)?.image)
                    tv_pic1_newstitle?.text = m_listNewsData?.get(position - 1)?.title
                    tv_pic1_newstitle2?.text = m_listNewsData?.get(position - 1)?.title2
                    tv_pic1_newstime?.text = "${SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(m_listNewsData?.get(position)?.publishTime?.times(1000)!!))}"
                }
            }
            PICS_THREE -> {
                with(holder!! as PicThreeViewHolder) {
                    iv_pic3_newscover1?.chAllDisplayImage(ctx, m_listNewsData?.get(position - 1)?.images?.get(0)?.url1 ?: "")
                    iv_pic3_newscover2?.chAllDisplayImage(ctx, m_listNewsData?.get(position - 1)?.images?.get(1)?.url1 ?: "")
                    iv_pic3_newscover3?.chAllDisplayImage(ctx, m_listNewsData?.get(position - 1)?.images?.get(2)?.url1 ?: "")
                    tv_pic3_newstitle?.text = m_listNewsData?.get(position - 1)?.title
                    tv_pic3_newstitle2?.text = m_listNewsData?.get(position - 1)?.title2
                    tv_pic3_newstime?.text = "${SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(m_listNewsData?.get(position - 1)?.publishTime?.times(1000)!!))}"
                }
            }
            BANNER_ADVERTISE -> {
                with(holder!! as BannerViewHolder) {
                    layout_indcator?.removeAllViews()
                    m_listViews.forEachIndexed { _, view ->
                        if (view.parent == null)
                            layout_indcator?.addView(view)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }

        }
        //
        if (position > mLastPosition) {
            val tmpItem = holder?.itemView
            val ani = AnimationUtils.loadAnimation(ctx, R.anim.item_bottom_in)
            tmpItem?.startAnimation(ani)
            mLastPosition = position
        }
    }

    //
    class PicThreeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val iv_pic3_newscover1 = itemView?.findViewById<ImageView>(R.id.iv_pic3_newscover1)
        val iv_pic3_newscover2 = itemView?.findViewById<ImageView>(R.id.iv_pic3_newscover2)
        val iv_pic3_newscover3 = itemView?.findViewById<ImageView>(R.id.iv_pic3_newscover3)
        val tv_pic3_newstitle = itemView?.findViewById<TextView>(R.id.tv_pic3_newstitle)
        val tv_pic3_newstitle2 = itemView?.findViewById<TextView>(R.id.tv_pic3_newstitle2)
        val tv_pic3_newstime = itemView?.findViewById<TextView>(R.id.tv_pic3_newstime)
    }

    class PicOneViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val iv_pic1_newscover1 = itemView?.findViewById<ImageView>(R.id.iv_pic1_newscover1)
        val tv_pic1_newstitle = itemView?.findViewById<TextView>(R.id.tv_pic1_newstitle)
        val tv_pic1_newstitle2 = itemView?.findViewById<TextView>(R.id.tv_pic1_newstitle2)
        val tv_pic1_newstime = itemView?.findViewById<TextView>(R.id.tv_pic1_newstime)
    }

    inner class BannerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //
        val news_vp_advertise = itemView?.findViewById<ViewPager>(R.id.news_vp_advertise)
        //
        val layout_indcator = itemView?.findViewById<LinearLayout>(R.id.layout_indcator)

        //
        val mAdapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View?, view1: Any?): Boolean = view == view1

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(m_listAdvertiseImages[position])
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                if (m_listAdvertiseImages[position].parent == null) {
                    (container as? ViewPager)?.addView(m_listAdvertiseImages[position])
                } else
                    (container as? ViewPager)?.removeView(m_listAdvertiseImages[position])
                return m_listAdvertiseImages[position]
            }

            override fun getCount(): Int {
                return m_listAdvertiseImages.size
            }
        }

        init {
            AutoUtils.autoSize(itemView)
            news_vp_advertise?.adapter = mAdapter
            //只create一次
            Observable.interval(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                news_vp_advertise?.currentItem = m_nPlayIndex
                if (++m_nPlayIndex == m_listAdvertiseImages.size) {
                    m_nPlayIndex = 0
                }
            }
            //
            news_vp_advertise?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    m_listViews.forEachIndexed { index, view ->
                        if (index == position)
                            view.setBackgroundColor(Color.WHITE)
                        else
                            view.setBackgroundColor(Color.BLACK)
                    }
                }
            })
        }
    }
}