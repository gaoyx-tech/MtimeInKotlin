package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.networkbusiness.MovieSearchResultList
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.item_search_result_content.view.*
import java.lang.StringBuilder

/**
 * Created by choujiaming on 2017/8/29.
 */
class MovieSearchAdapter(val ctx: Context) : BaseAdapter() {
    //
    var m_listResult: List<MovieSearchResultList> = mutableListOf()

    //
    class ViewHolder {
        lateinit var iv_Cover: ImageView
        lateinit var tv_Name: TextView
        lateinit var tv_Director: TextView
        lateinit var tv_Type: TextView
        lateinit var tv_Score: TextView
    }

    //
    fun addSearchResultData(listResult: List<MovieSearchResultList>) {
        this.m_listResult = listResult
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var holder = ViewHolder()
        var convertView: View? = p1
        //
        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_search_result_content, null)
            with(holder) {
                iv_Cover = convertView?.search_cover as ImageView
                tv_Name = convertView?.search_name as TextView
                tv_Director = convertView?.search_director as TextView
                tv_Type = convertView?.search_type as TextView
                tv_Score = convertView?.search_score as TextView
            }
            convertView?.tag = holder
        }
        holder.apply {
            Glide.with(ctx).load(m_listResult[p0].images.large).skipMemoryCache(true).centerCrop().into(iv_Cover)
            tv_Name.text = "<< ${m_listResult[p0].title} >>"
            tv_Score.text = "${m_listResult[p0].rating.average}分  ${m_listResult[p0].year}年"
            //
            val strDire = m_listResult[p0].directors.map { it.name }.joinToString(",", "", "")
            tv_Director.text = "导演 $strDire"
            //
            val sbType = StringBuilder()
            m_listResult[p0].genres.forEach {
                sbType.append("$it ")
            }
            tv_Type.text = "类型 $sbType"
        }
        AutoUtils.autoSize(convertView)
        return convertView!!
    }

    override fun getItem(p0: Int): Any = m_listResult[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = m_listResult.size
}