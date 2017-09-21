package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.chAllstartActivity
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieSoonComeItemData
import com.lovejiaming.timemovieinkotlin.views.activity.MovieDetailActivity
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by xiaoxin on 2017/9/5.
 * 某个月的所有即将放映
 */
class HotMovieSoonComeOfAllAdapter(val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //
    lateinit var mAllMapData: MutableMap<Int?, List<HotMovieSoonComeItemData>>
    var mAllListData: MutableList<HotMovieSoonComeItemData> = mutableListOf()
    //
    val INFO_TYPE = 1
    val DATE_TYPE = 2
    //
    var m_nLoadCount = 0
    val m_arrListSumSize = arrayListOf<Int>()
    val m_arrListRD = arrayListOf<String>()

    fun addAllGroupData(data: Map<Int?, List<HotMovieSoonComeItemData>>) {
        mAllMapData = data.toMutableMap()
        //填充list
        for ((key, _) in mAllMapData) {
            mAllMapData[key]?.let { mAllListData.addAll(it) }
            m_arrListRD.add(mAllMapData[key]?.get(0)?.releaseDate!!)
        }
        //算出总数 moviecount+mapcount
        m_nLoadCount += mAllMapData.size
        mAllMapData.forEach {
            m_nLoadCount += it.value.size
        }
        //和数集合，集合中存储的是应该显示日期文字的索引
        var allCount = 0
        m_arrListSumSize.add(0)
        mAllMapData.forEach {
            Log.i("it.value.size == ", it.value.size.toString())
            allCount += (it.value.size + 1)
            m_arrListSumSize.add(allCount)
        }
        m_arrListSumSize.removeAt(m_arrListSumSize.size - 1)//去掉最后一个
        Log.i("arrlist == ", m_arrListSumSize.toString())
        Log.i("m_arrListRD == ", m_arrListRD.toString())
        //
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            INFO_TYPE -> {
                with(holder as ComeAllHolderInfo) {
                    val exclude = m_arrListSumSize.filter { position > it }//先计算出当前已经经过几个日期文字索引
                    comealldirector?.text = "导演：${mAllListData[position - exclude.size].director}  ${mAllListData[position - exclude.size].releaseDate} "
                    comeallname?.text = "<< ${mAllListData[position - exclude.size].title} >> "
                    comeallwannacount?.text = "${mAllListData[position - exclude.size].wantedCount}人想看 "
                    comealltype?.text = "${mAllListData[position - exclude.size].type} "
                    comeallcover?.chAllDisplayImage(ctx, mAllListData[position - exclude.size].image ?: "")
                    //
                    itemView.setOnClickListener {
                        ctx.chAllstartActivity(mapOf("movieid" to mAllListData[position - exclude.size].id.toString(), "moviename" to mAllListData[position - exclude.size].title!!),
                                MovieDetailActivity::class.java)
                    }
                }
            }
            DATE_TYPE -> {
                with(holder as ComeAllHolderDate) {
                    m_arrListSumSize.forEachIndexed {
                        index, value ->
                        if (value == position)
                            releasedate?.text = m_arrListRD[index]
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = m_nLoadCount

    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position in m_arrListSumSize) {
            return DATE_TYPE
        } else
            return INFO_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            INFO_TYPE -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_come_ofall, null)
                return ComeAllHolderInfo(view)
            }
            DATE_TYPE -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_come_ofalldate, null)
                return ComeAllHolderDate(view)
            }
        }
        return null!!
    }

    class ComeAllHolderInfo(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val comeallcover = itemView?.findViewById<ImageView>(R.id.comeall_cover)
        val comeallname = itemView?.findViewById<TextView>(R.id.comeall_name)
        val comealldirector = itemView?.findViewById<TextView>(R.id.comeall_director)
        val comeallwannacount = itemView?.findViewById<TextView>(R.id.comeall_wannacount)
        val comealltype = itemView?.findViewById<TextView>(R.id.comeall_type)
    }

    class ComeAllHolderDate(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val releasedate = itemView?.findViewById<TextView>(R.id.comeall_releasedate)
    }
}