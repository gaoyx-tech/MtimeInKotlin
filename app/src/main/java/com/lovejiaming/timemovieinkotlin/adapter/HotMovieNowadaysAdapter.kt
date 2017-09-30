package com.lovejiaming.timemovieinkotlin.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllAsyncToMainThread
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.chAllstartActivity
import com.lovejiaming.timemovieinkotlin.databasebusiness.MovieRoomOperate
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieNowadaysItemData
import com.lovejiaming.timemovieinkotlin.views.activity.MovieDetailActivity
import com.zhy.autolayout.utils.AutoUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by xiaoxin on 2017/8/24.
 */

class HotMovieNowadaysAdapter(val ctx: Context, private val actionListener: (String) -> Unit/*HotMovie_NowadaysFragment.IClickHaveSeenBtn*/) : RecyclerView.Adapter<HotMovieNowadaysAdapter.InnerViewHolder>() {
    //数据集
    private var m_listHotNowadays: MutableList<HotMovieNowadaysItemData> = mutableListOf()
    //看过数据
    private var m_listHaveSeen: List<Int?> = mutableListOf()
    //点击了某一个看过
    private var m_nClickChangePos = -1
    private var m_nLastPosition = -1

    fun insertDatas(list: MutableList<HotMovieNowadaysItemData>) {
        m_listHotNowadays = list
        m_nClickChangePos = -1
        loadHaveSeenFromDataBase()
    }

    fun loadHaveSeenFromDataBase() {
        Observable.create(ObservableOnSubscribe<List<Int>> {
            e ->
            Log.i("HaveSeen db = ", MovieRoomOperate.newInstance(ctx).queryAllHaveSeen().toString())
            e.onNext(MovieRoomOperate.newInstance(ctx).queryAllHaveSeen().map { it.movieId })
            e.onComplete()
        }).chAllAsyncToMainThread()
                .subscribe {
                    m_listHaveSeen = it
                    if (m_nClickChangePos == -1)
                        notifyDataSetChanged()
                    else
                        notifyItemChanged(m_nClickChangePos)
                }
    }

    fun deleteHaveSeenRecord(movieId: Int) {
        Observable.create(ObservableOnSubscribe<String> {
            e ->
            MovieRoomOperate.newInstance(ctx).deleteOneHaveSeen(movieId)
            e.onNext("")
            e.onComplete()
        }).chAllAsyncToMainThread().subscribe {
            loadHaveSeenFromDataBase()
        }
    }

    fun insertHaveSeenRecord(movieId: Int, movieName: String, movieCoverUrl: String) {
        Observable.create(ObservableOnSubscribe<String> {
            e ->
            MovieRoomOperate.newInstance(ctx).insertOneHaveSeen(movieId, movieName, movieCoverUrl)
            e.onNext("")
            e.onComplete()
        }).chAllAsyncToMainThread().subscribe {
            loadHaveSeenFromDataBase()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InnerViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_nowdays, null)
        return InnerViewHolder(view)
    }

    override fun getItemCount(): Int = m_listHotNowadays.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InnerViewHolder?, position: Int) {
        with(holder!!) {
            iv_Cover.chAllDisplayImage(ctx, m_listHotNowadays[position].img)
            tv_Name.text = "片名：<< ${m_listHotNowadays[position].tCn} >>"
            tv_Actor.text = "演员：${m_listHotNowadays[position].actors} "
            tv_Score.text = "评分：${m_listHotNowadays[position].r} 分 "
            tv_WantCount.text = "${m_listHotNowadays[position].wantedCount} 人想看 "
            //
            val bIsHaveSeen: Boolean
            if (m_listHaveSeen.contains(m_listHotNowadays[position].id)) {
                bIsHaveSeen = true
                iv_HaveSeen.setImageResource(R.drawable.haveseen_select)
            } else {
                bIsHaveSeen = false
                iv_HaveSeen.setImageResource(R.drawable.haveseen_unselect)
            }
            iv_HaveSeen.setOnClickListener {
                m_nClickChangePos = position
                if (bIsHaveSeen) {
                    actionListener("影片：<< ${m_listHotNowadays[position].tCn} >> 已取消看过")
                    deleteHaveSeenRecord(m_listHotNowadays[position].id!!)
                } else {
                    actionListener("影片：<< ${m_listHotNowadays[position].tCn} >> 已加入看过")
                    insertHaveSeenRecord(m_listHotNowadays[position].id!!, m_listHotNowadays[position].tCn!!, m_listHotNowadays[position].img!!)
                }
            }
            itemView.setOnClickListener {
                ctx.chAllstartActivity<MovieDetailActivity>(mapOf("movieid" to m_listHotNowadays[position].id.toString(), "moviename" to m_listHotNowadays[position].tCn!!))
            }
            if (position > m_nLastPosition) {
                m_nLastPosition = position
                addAnimations(itemView)
            }
        }
    }

    fun addAnimations(view: View?) {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        animator.duration = 700
        animator.start()
    }

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val iv_Cover = itemView.findViewById<ImageView>(R.id.hotnowadays_cover)
        val tv_Name = itemView.findViewById<TextView>(R.id.hotnowadays_name)
        val tv_Actor = itemView.findViewById<TextView>(R.id.hotnowadays_actor)
        val tv_Score = itemView.findViewById<TextView>(R.id.hotnowadays_score)
        val tv_WantCount = itemView.findViewById<TextView>(R.id.hotnowadays_wantcount)
        val iv_HaveSeen = itemView.findViewById<ImageView>(R.id.hotnowadays_haveseen)
    }
}