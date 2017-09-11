package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.networkbusiness.CinemaData
import com.lovejiaming.timemovieinkotlin.views.activity.ToCinemaMapActivity
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by choujiaming on 2017/8/25.
 */

class HotMovieCinemaAdapter(val ctx: Context) : RecyclerView.Adapter<HotMovieCinemaAdapter.InnerViewHolder>() {
    //
    var m_listCinemas: MutableList<CinemaData> = mutableListOf()

    //
    fun insertCinemaData(list: MutableList<CinemaData>) {
        this.m_listCinemas = list
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InnerViewHolder?, position: Int) {
        with(holder!!) {
            //
            itemView.setOnClickListener {
                val intent = Intent(ctx, ToCinemaMapActivity::class.java)
                intent.putExtra("latitude", m_listCinemas[position].baiduLatitude)
                intent.putExtra("longitude", m_listCinemas[position].baiduLongitude)
                intent.putExtra("feature", m_listCinemas[position].feature)
                intent.putExtra("name", m_listCinemas[position].cinameName)
                ctx.startActivity(intent)
            }
            cname.text = m_listCinemas[position].cinameName
            caddress.text = "${m_listCinemas[position].address} "
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_cinemas, null)
        return InnerViewHolder(view)
    }

    override fun getItemCount(): Int = m_listCinemas.size

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val cname = itemView.findViewById<TextView>(R.id.cinema_name)
        val caddress = itemView.findViewById<TextView>(R.id.cinema_address)
    }
}