package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.TrailersData
import com.lovejiaming.timemovieinkotlin.networkbusiness.VideoList
import com.lovejiaming.timemovieinkotlin.views.activity.PlayVideoActivity
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by xiaoxin on 2017/9/6.
 */
class MovieDetailAllTrailerAdapter(val ctx: Context) : RecyclerView.Adapter<MovieDetailAllTrailerAdapter.AllTrailerViewHolder>() {

    var m_arrAllTrailers: MutableList<VideoList> = mutableListOf()

    //
    fun addAllTrailersData(data: TrailersData, action: String) {
        if (action == "refresh")
            m_arrAllTrailers = data.videoList.toMutableList()
        else
            m_arrAllTrailers.addAll(data.videoList.toMutableList())
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AllTrailerViewHolder?, position: Int) {
        m_arrAllTrailers.let {
            holder?.apply {
                alltrailer_name.text = "${m_arrAllTrailers[position].title} "
                alltrailer_time.text = "${m_arrAllTrailers[position].length}秒 "
                alltrailer_cover?.chAllDisplayImage(ctx, m_arrAllTrailers[position].image)
                itemView.setOnClickListener {
                    val intent = Intent(ctx, PlayVideoActivity::class.java)
                    intent.putExtra("path", m_arrAllTrailers[position].hightUrl)
                    intent.putExtra("name", m_arrAllTrailers[position].title)
                    ctx.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AllTrailerViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_all_trailers, null)
        return AllTrailerViewHolder(view)
    }

    override fun getItemCount(): Int = m_arrAllTrailers.size

    class AllTrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alltrailer_cover = itemView.findViewById<ImageView>(R.id.alltrailer_cover)
        val alltrailer_name = itemView.findViewById<TextView>(R.id.alltrailer_name)
        val alltrailer_time = itemView.findViewById<TextView>(R.id.alltrailer_time)

        init {
            AutoUtils.autoSize(itemView)

        }
    }
}