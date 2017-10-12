package com.lovejiaming.timemovieinkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.chAllInflateView
import com.lovejiaming.timemovieinkotlin.networkbusiness.DetailCommentItem
import com.zhy.autolayout.utils.AutoUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by gaoyx on 2017/10/9.
 */
class MovieDetailAllCommentAdapter(val ctx: Context) : RecyclerView.Adapter<MovieDetailAllCommentAdapter.ViewHolder>() {

    var m_listComment: ArrayList<DetailCommentItem>? = null

    fun addCommentData(list: ArrayList<DetailCommentItem>) {
        m_listComment = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.apply {
            val result = m_listComment?.get(position)
            comment_head?.chAllDisplayImage(ctx, result?.caimg)
            comment_name_address?.text = "${result?.ca}  (${result?.cal})"
            comment_info?.text = result?.ce
            comment_score?.text = "打分 ${result?.cr}"
            comment_time?.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(result?.cd!!.times(1000)))
        }
    }

    override fun getItemCount(): Int = m_listComment?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = chAllInflateView(ctx, R.layout.item_detail_comment)
        AutoUtils.autoSize(view)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val comment_head by lazy {
            itemView?.findViewById<ImageView>(R.id.comment_head)
        }
        val comment_name_address by lazy {
            itemView?.findViewById<TextView>(R.id.comment_name_address)
        }
        val comment_info by lazy {
            itemView?.findViewById<TextView>(R.id.comment_info)
        }
        val comment_time by lazy {
            itemView?.findViewById<TextView>(R.id.comment_time)
        }
        val comment_score by lazy {
            itemView?.findViewById<TextView>(R.id.comment_score)
        }
    }
}