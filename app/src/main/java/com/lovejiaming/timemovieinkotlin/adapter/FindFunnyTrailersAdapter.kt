package com.lovejiaming.timemovieinkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.mTimeDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.TrailerItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.TrailersDataArray
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by xiaoxin on 2017/9/13.
 */

class FindFunnyTrailersAdapter(val ctx: Context) : RecyclerView.Adapter<FindFunnyTrailersAdapter.ViewHolder>() {
    //
    var m_listTrailers: MutableList<TrailerItem>? = null

    //
    fun insertAllTrailers(data: TrailersDataArray) {
        this.m_listTrailers = data.trailers.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        m_listTrailers?.let {
            with(holder!!) {
                funnytrailer_cover?.mTimeDisplayImage(ctx, m_listTrailers?.get(position)?.coverImg)
                funnytrailer_allinfo?.text = "<< ${m_listTrailers?.get(position)?.videoTitle} >>——${m_listTrailers?.get(position)?.summary} "
            }
        }
    }

    override fun getItemCount(): Int = m_listTrailers?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_findfunny_trailer, null)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val funnytrailer_cover = itemView?.findViewById<ImageView>(R.id.funnytrailer_cover)
        val funnytrailer_allinfo = itemView?.findViewById<TextView>(R.id.funnytrailer_allinfo)
    }

}