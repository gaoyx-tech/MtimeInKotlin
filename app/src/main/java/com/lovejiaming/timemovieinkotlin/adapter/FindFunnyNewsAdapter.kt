package com.lovejiaming.timemovieinkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.mTimeDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.NewsArray
import com.lovejiaming.timemovieinkotlin.networkbusiness.NewsItem
import com.zhy.autolayout.utils.AutoUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xiaoxin on 2017/9/12.
 */
class FindFunnyNewsAdapter(val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val PICS_THREE = 1
    val PICS_ONE = 2
    //
    var m_listNewsData: List<NewsItem>? = null

    fun insertNewsData(data: NewsArray) {
        this.m_listNewsData = data.newsList
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
        }
        return null!!
    }

    override fun getItemCount(): Int = m_listNewsData?.size ?: 0

    override fun getItemViewType(position: Int): Int = if (m_listNewsData?.get(position)?.images!!.isNotEmpty()) PICS_THREE else PICS_ONE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            PICS_ONE -> {
                with(holder!! as PicOneViewHolder) {
                    iv_pic1_newscover1?.mTimeDisplayImage(ctx, m_listNewsData?.get(position)?.image)
                    tv_pic1_newstitle?.text = m_listNewsData?.get(position)?.title
                    tv_pic1_newstitle2?.text = m_listNewsData?.get(position)?.title2
                    tv_pic1_newstime?.text = "${ SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(m_listNewsData?.get(position)?.publishTime?.times(1000)!!)) }"
                }
            }
            PICS_THREE -> {
                with(holder!! as PicThreeViewHolder) {
                    iv_pic3_newscover1?.mTimeDisplayImage(ctx, m_listNewsData?.get(position)?.images?.get(0)?.url1 ?: "")
                    iv_pic3_newscover2?.mTimeDisplayImage(ctx, m_listNewsData?.get(position)?.images?.get(1)?.url1 ?: "")
                    iv_pic3_newscover3?.mTimeDisplayImage(ctx, m_listNewsData?.get(position)?.images?.get(2)?.url1 ?: "")
                    tv_pic3_newstitle?.text = m_listNewsData?.get(position)?.title
                    tv_pic3_newstitle2?.text = m_listNewsData?.get(position)?.title2
                    tv_pic3_newstime?.text = "${ SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(m_listNewsData?.get(position)?.publishTime?.times(1000)!!)) }"
                }
            }
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
}