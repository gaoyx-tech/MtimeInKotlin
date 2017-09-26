package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.chAllstartActivity
import com.lovejiaming.timemovieinkotlin.networkbusiness.MovieSearchResultItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.TagMovieSearchItem
import com.lovejiaming.timemovieinkotlin.views.activity.MovieDetailActivity
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.item_search_result_content.view.*

/**
 * Created by xiaoxin on 2017/8/29.
 */
class MovieSearchAdapter(val ctx: Context) : BaseAdapter() {
    //
    var m_listResultOfName: MutableList<MovieSearchResultItem> = mutableListOf()
    var m_listResultOfTag: MutableList<TagMovieSearchItem> = mutableListOf()

    //
    class ViewHolder {
        lateinit var iv_Cover: ImageView
        lateinit var tv_Name: TextView
        lateinit var tv_Type: TextView
        lateinit var tv_Score: TextView
    }

    //
    fun addSearchResultData(listResult: List<MovieSearchResultItem>) {
        this.m_listResultOfName = listResult.toMutableList()
        this.m_listResultOfTag.clear()
        notifyDataSetChanged()
    }

    fun addSearchResultDataOfTag(listResult: List<TagMovieSearchItem>) {
        this.m_listResultOfTag = listResult.toMutableList()
        this.m_listResultOfName.clear()
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
                tv_Type = convertView?.search_type as TextView
                tv_Score = convertView?.search_score as TextView
            }
            convertView?.tag = holder
        }
        holder.apply {
            if (m_listResultOfName.size > 0) {
                iv_Cover.chAllDisplayImage(ctx, m_listResultOfName[p0].img)
                tv_Name.text = "<< ${m_listResultOfName[p0].name} >>"
                tv_Score.text = "${m_listResultOfName[p0].rating}分  ${m_listResultOfName[p0].rYear}年"
                tv_Type.text = m_listResultOfName[p0].movieType
                convertView?.setOnClickListener {
                    ctx.chAllstartActivity<MovieDetailActivity>(mapOf("moviename" to m_listResultOfName[p0].name!!, "movieid" to m_listResultOfName[p0].id.toString()))
                }
            } else {
                iv_Cover.chAllDisplayImage(ctx, m_listResultOfTag[p0].img)
                tv_Name.text = "<< ${m_listResultOfTag[p0].titleCn} >>"
                tv_Score.text = "${m_listResultOfTag[p0].ratingFinal}分  ${m_listResultOfTag[p0].rYear}年"
                tv_Type.text = m_listResultOfTag[p0].type
                convertView?.setOnClickListener {
                    ctx.chAllstartActivity<MovieDetailActivity>(mapOf("moviename" to m_listResultOfTag[p0].titleCn!!, "movieid" to m_listResultOfTag[p0].movieId.toString()))
                }
            }
        }
        AutoUtils.autoSize(convertView)
        return convertView!!
    }

    override fun getItem(p0: Int): Any = m_listResultOfName[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = if (m_listResultOfName.size > 0) m_listResultOfName.size else m_listResultOfTag.size
}