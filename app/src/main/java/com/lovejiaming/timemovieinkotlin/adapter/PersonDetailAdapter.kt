package com.lovejiaming.timemovieinkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.mTimeDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.AwardDetailItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonAllMovie
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonDetailResponse
import com.lovejiaming.timemovieinkotlin.views.activity.MovieDetailActivity
import com.lovejiaming.timemovieinkotlin.views.activity.SimpleItemDecorationHor
import com.lovejiaming.timemovieinkotlin.views.ownerdraw.CustomRatingView
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by xiaoxin on 2017/9/7.
 */
class PersonDetailAdapter(val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //内部类也要用的，但是不能inner OOM
    companion object {
        val HEAD_TYPE = 11
        val HOTMOVIE_TYPE = 12
        val ALLMOVIE_TYPE = 13
        val WINAWARD_TYPE = 14
        val NOMINATE_TYPE = 15
        val CONTENT_TYPE = 16
        //all winaward
        var g_listWinAwards: ArrayList<AwardDetailItem> = arrayListOf()
        //all nominate
        var g_listNominageAwards: ArrayList<AwardDetailItem> = arrayListOf()
        //all movies
        var g_listAllMovies: ArrayList<PersonAllMovie> = arrayListOf()
    }

    //
    var mData: PersonDetailResponse? = null

    //
    fun insertPersonDetailData(data: PersonDetailResponse) {
        this.mData = data
        //
        g_listNominageAwards.clear()
        g_listWinAwards.clear()
        mData?.awards?.forEach {
            g_listNominageAwards.addAll(it.nominateAwards)
            g_listWinAwards.addAll(it.winAwards)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
            when (position) {
                0 -> HEAD_TYPE
                1 -> WINAWARD_TYPE
                2 -> CONTENT_TYPE
                3 -> NOMINATE_TYPE
                4 -> HOTMOVIE_TYPE
                5 -> ALLMOVIE_TYPE
                else -> 100
            }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                HEAD_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_person_detail_head, null)
                    HeadViewHolder(view)
                }
                CONTENT_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_contentinfo, null)
                    ContentViewHolder(view)
                }
                HOTMOVIE_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_person_detail_hot_movie, null)
                    HotMovieViewHolder(view)
                }
                ALLMOVIE_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_recyclerperson, null)//复用
                    HorizontalViewHolder(ctx, ALLMOVIE_TYPE, view)
                }
                WINAWARD_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_recyclerperson, null)//复用
                    HorizontalViewHolder(ctx, WINAWARD_TYPE, view)
                }
                NOMINATE_TYPE -> {
                    val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_recyclerperson, null)//复用
                    HorizontalViewHolder(ctx, NOMINATE_TYPE, view)
                }
                else -> {
                    null!!
                }
            }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        mData?.let {
            when (getItemViewType(position)) {
                HEAD_TYPE -> {
                    with(holder as HeadViewHolder) {
                        head?.mTimeDisplayImage(ctx, mData?.image)
                        name?.text = mData?.nameCn ?: mData?.nameEn
                        birthday?.text = "${mData?.birthYear}-${mData?.birthMonth}-${mData?.birthDay} "
                        job?.text = mData?.profession
                        address?.text = mData?.address
                        ratingbar?.setRating(mData?.ratingFinal?.toFloat()?.times(10f)!!)
                    }
                }
                CONTENT_TYPE -> {
                    with(holder as ContentViewHolder) {
                        content?.text = "介绍：\r\n ${mData?.content}"
                    }
                }
                HOTMOVIE_TYPE -> {
                    with(holder as HotMovieViewHolder) {
                        person_hotmovie_name?.text = "${mData?.hotMovie?.movieTitleCn} "
                        person_hotmovie_rating?.text = "${mData?.hotMovie?.ratingFinal.toString()}分 "
                        person_hotmovie_type?.text = "${mData?.hotMovie?.type} "
                        person_hotmovie_cover?.mTimeDisplayImage(ctx, mData?.hotMovie?.movieCover ?: "")
                        //
                        itemView.setOnClickListener {
                            val intent = Intent(ctx, MovieDetailActivity::class.java)
                            intent.putExtra("moviename", mData?.hotMovie?.movieTitleCn)
                            intent.putExtra("movieid", mData?.hotMovie?.movieId)
                            ctx.startActivity(intent)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun getItemCount(): Int = 6

    //头部viewholder
    class HeadViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        //
        val head = itemView?.findViewById<ImageView>(R.id.head)
        val name = itemView?.findViewById<TextView>(R.id.name)
        val birthday = itemView?.findViewById<TextView>(R.id.birthday)
        val job = itemView?.findViewById<TextView>(R.id.job)
        val address = itemView?.findViewById<TextView>(R.id.address)
        val ratingbar = itemView?.findViewById<CustomRatingView>(R.id.ratingbar)
    }

    //介绍viewholder
    class ContentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val content = itemView?.findViewById<TextView>(R.id.content)
    }

    //热门viewholder
    class HotMovieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            AutoUtils.autoSize(itemView)
        }

        val person_hotmovie_cover = itemView?.findViewById<ImageView>(R.id.person_hotmovie_cover)
        val person_hotmovie_name = itemView?.findViewById<TextView>(R.id.person_hotmovie_name)
        val person_hotmovie_rating = itemView?.findViewById<TextView>(R.id.person_hotmovie_rating)
        val person_hotmovie_type = itemView?.findViewById<TextView>(R.id.person_hotmovie_type)
    }

    //所有movie，两个award (来自不同的adapter，也就是来自不同的数据集，可复用一个viewholder也就是一个布局)
    class HorizontalViewHolder(val ctx: Context, val type: Int, itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //
        var more_person: TextView? = null
        var recyclerview: RecyclerView? = null
        var mAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null//形变，子类

        init {
            more_person = itemView?.findViewById<TextView>(R.id.more_person)
            //
            recyclerview = itemView?.findViewById<RecyclerView>(R.id.detail_recyclerview_persons)
            recyclerview?.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            recyclerview?.addItemDecoration(SimpleItemDecorationHor())
            AutoUtils.autoSize(itemView)
            //
            when (type) {
                PersonDetailAdapter.WINAWARD_TYPE -> {
                    more_person?.text = "所有获奖 "
                    mAdapter = WinAwardAdapter(ctx)
                    recyclerview?.adapter = mAdapter
                    mAdapter?.notifyDataSetChanged()
                }
                PersonDetailAdapter.NOMINATE_TYPE -> {
                    more_person?.text = "所有提名 "
                    mAdapter = NominatAwardAdapter(ctx)
                    recyclerview?.adapter = mAdapter
                    mAdapter?.notifyDataSetChanged()
                }
                PersonDetailAdapter.ALLMOVIE_TYPE -> {
                    more_person?.text = "所有相关电影 "
                    mAdapter = AllMovieAdapter(ctx)
                    recyclerview?.adapter = mAdapter
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    //winaward adapter
    class WinAwardAdapter(val ctx: Context) : RecyclerView.Adapter<WinAwardAdapter.WinHolder>() {
        override fun onBindViewHolder(holder: WinHolder?, position: Int) {
            g_listWinAwards.let {
                with(holder!!) {
                    person_job?.text = g_listWinAwards[position].awardName ?: ""
                    person_name?.text = g_listWinAwards[position].movieTitle ?: ""
                    person_head?.mTimeDisplayImage(ctx, g_listWinAwards[position].image ?: "")
                    itemView?.setOnClickListener {
                        val intent = Intent(ctx, MovieDetailActivity::class.java)
                        intent.putExtra("movieid", g_listWinAwards[position].movieId)
                        intent.putExtra("moviename", g_listWinAwards[position].movieTitle)
                        ctx.startActivity(intent)
                    }
                }
            }
        }

        override fun getItemCount(): Int = g_listWinAwards.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WinHolder {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_person_detail, null)
            return WinHolder(view)
        }

        class WinHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            init {
                AutoUtils.autoSize(itemView)
            }

            val person_job = itemView?.findViewById<TextView>(R.id.person_job)
            val person_head = itemView?.findViewById<ImageView>(R.id.person_head)
            val person_name = itemView?.findViewById<TextView>(R.id.person_name)
        }
    }

    //nominate adapter
    class NominatAwardAdapter(val ctx: Context) : RecyclerView.Adapter<NominatAwardAdapter.NominatHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NominatAwardAdapter.NominatHolder {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_person_detail, null)
            return NominatHolder(view)
        }

        override fun getItemCount(): Int = g_listNominageAwards.size

        override fun onBindViewHolder(holder: NominatAwardAdapter.NominatHolder?, position: Int) {
            g_listNominageAwards[position].let {
                with(holder!!) {
                    person_name?.text = g_listNominageAwards[position].movieTitle ?: ""
                    person_job?.text = g_listNominageAwards[position].awardName ?: ""
                    person_head?.mTimeDisplayImage(ctx, g_listNominageAwards[position].image ?: "")
                    itemView?.setOnClickListener {
                        val intent = Intent(ctx, MovieDetailActivity::class.java)
                        intent.putExtra("movieid", g_listNominageAwards[position].movieId)
                        intent.putExtra("moviename", g_listNominageAwards[position].movieTitle)
                        ctx.startActivity(intent)
                    }
                }
            }
        }

        class NominatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            init {
                AutoUtils.autoSize(itemView)
            }

            val person_job = itemView?.findViewById<TextView>(R.id.person_job)
            val person_head = itemView?.findViewById<ImageView>(R.id.person_head)
            val person_name = itemView?.findViewById<TextView>(R.id.person_name)
        }
    }

    //all movies
    class AllMovieAdapter(val ctx: Context) : RecyclerView.Adapter<AllMovieAdapter.AllMovieViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AllMovieViewHolder {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_person_detail, null)
            return AllMovieViewHolder(view)
        }

        override fun getItemCount(): Int = g_listAllMovies.size

        override fun onBindViewHolder(holder: AllMovieViewHolder?, position: Int) {
            with(holder!!) {
                person_job?.text = g_listAllMovies[position].offices?.map { it.name }?.joinToString()
                person_name?.text = g_listAllMovies[position].name ?: ""
                person_head?.mTimeDisplayImage(ctx, g_listAllMovies[position].image ?: "")
                itemView?.setOnClickListener {
                    val intent = Intent(ctx, MovieDetailActivity::class.java)
                    intent.putExtra("movieid", g_listAllMovies[position].id)
                    intent.putExtra("moviename", g_listAllMovies[position].name)
                    ctx.startActivity(intent)
                }
            }
        }

        class AllMovieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            init {
                AutoUtils.autoSize(itemView)
            }

            val person_job = itemView?.findViewById<TextView>(R.id.person_job)
            val person_head = itemView?.findViewById<ImageView>(R.id.person_head)
            val person_name = itemView?.findViewById<TextView>(R.id.person_name)
        }
    }
}

