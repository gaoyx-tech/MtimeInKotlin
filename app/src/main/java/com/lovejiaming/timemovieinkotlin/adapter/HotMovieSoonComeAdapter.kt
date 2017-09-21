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
import com.bumptech.glide.Glide
import com.lovejiaming.timemovieinkotlin.MTimeInKotlinApp
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllstartActivity
import com.lovejiaming.timemovieinkotlin.databasebusiness.MovieRoomOperate
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieSoonComeAllData
import com.lovejiaming.timemovieinkotlin.networkbusiness.HotMovieSoonComeItemData
import com.lovejiaming.timemovieinkotlin.views.activity.AllComeSoonMovieActivity
import com.lovejiaming.timemovieinkotlin.views.activity.MovieDetailActivity
import com.zhy.autolayout.utils.AutoUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by xiaoxin on 2017/8/25.
 * 数据库策略是，首先载入所有数据库数据，当点击桃心时，先insert和delete，然后立即从新load数据并填充缓存，
 * 并notify，这样，数据都是新的，桃心变色也是数据库数据，而不是先变色，再操作db
 */

@Suppress("IMPLICIT_CAST_TO_ANY")
class HotMovieSoonComeAdapter(val ctx: Context, val wannaListener: (String) -> Unit) : RecyclerView.Adapter<HotMovieSoonComeAdapter.InnerViewHolder>() {
    //总数据
    var mAllData: HotMovieSoonComeAllData? = null
    //attention
    var mAttentionData: List<HotMovieSoonComeItemData>? = null
    //sooncoming
    var mMovieComingData: List<HotMovieSoonComeItemData>? = null
    //月份集合
    val mArrMonthNum: ArrayList<Int> by lazy {
        arrayListOf(1)
    }
    //想看集合
    var mArrIWantSee: List<Int> = mutableListOf()

    fun addAllData(allData: HotMovieSoonComeAllData) {
        mAllData = allData
        mAttentionData = allData.attention
        mMovieComingData = allData.moviecomings
        filterMonthData()
        loadAllWantSeeDataFromDataBase()//from db
    }

    fun filterMonthData() {
        mArrMonthNum.clear()
        mMovieComingData?.forEach {
            if (it.rMonth !in mArrMonthNum)
                mArrMonthNum.add(it.rMonth!!)
        }
    }

    fun loadAllWantSeeDataFromDataBase() {
        Observable.create(ObservableOnSubscribe<List<Int>> {
            e ->
            e.onNext(MovieRoomOperate.newInstance(ctx).queryAllMovieWantSee().map { it.movieId })//过滤id
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe {
            mArrIWantSee = it
            notifyDataSetChanged()
        }
    }

    fun insertIWantSeeEntity(movieId: Int) {
        Observable.create(ObservableOnSubscribe<String> {
            e ->
            MovieRoomOperate.newInstance(ctx).insertOneMovieWantSee(movieId)
            e.onNext("")
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe {
            loadAllWantSeeDataFromDataBase()
        }
    }

    fun deleteIWantSeeEntity(movieId: Int) {
        Observable.create(ObservableOnSubscribe<String> {
            e ->
            MovieRoomOperate.newInstance(ctx).deleteOneMovieWantSee(movieId)
            e.onNext("")
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe {
            loadAllWantSeeDataFromDataBase()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_sooncome, null)
        return InnerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InnerViewHolder?, position: Int) {
        mAllData?.apply {
            when (position) {
                0 -> {
                    with(holder!!) {
                        iv_title.text = "最受关注"
                        for (i in 0 until 6) {
                            //
                            listCover?.get(i)?.visibility = View.VISIBLE
                            listName?.get(i)?.visibility = View.VISIBLE
                            listWannaCount?.get(i)?.visibility = View.VISIBLE
                            listIwanna?.get(i)?.visibility = View.VISIBLE
                            //
                            Glide.with(ctx).load(mAttentionData?.get(i)?.image).centerCrop().skipMemoryCache(false).into(listCover?.get(i)).onDestroy()
                            listName?.get(i)?.text = "<< ${mAttentionData?.get(i)?.title} >>"
                            listWannaCount?.get(i)?.text = "${mAttentionData?.get(i)?.wantedCount} 人想看"
                            //想看
                            var bIsWant: Boolean
                            if (mArrIWantSee.contains(mAttentionData?.get(i)?.id)) {
                                listIwanna?.get(i)?.setImageResource(R.drawable.movie_wanna_selected)
                                bIsWant = true
                            } else {
                                listIwanna?.get(i)?.setImageResource(R.drawable.movie_wanna_unselected)
                                bIsWant = false
                            }
                            //
                            listIwanna?.get(i)?.setOnClickListener {
                                if (bIsWant) {
                                    deleteIWantSeeEntity(mAttentionData?.get(i)?.id!!)
                                    wannaListener("影片：<< ${mAttentionData?.get(i)?.title!!} >> 已取消想看")
                                } else {
                                    insertIWantSeeEntity(mAttentionData?.get(i)?.id!!)
                                    wannaListener("影片：<< ${mAttentionData?.get(i)?.title!!} >> 已加入想看")
                                }
                            }
                            //
                            listCover?.get(i)?.setOnClickListener {
                                ctx.chAllstartActivity(mapOf("movieid" to mAttentionData?.get(i)?.id!!.toString(), "moviename" to mAttentionData?.get(i)?.title!!), MovieDetailActivity::class.java)
                            }
                            tv_comemore.visibility = View.GONE//关注不做更多处理
                        }
                    }
                }
                else -> {
                    //首先筛选monthNo
                    holder!!.apply {
                        iv_title.text = "${mArrMonthNum[position - 1]}月大片"
                        //filter month film
                        val listAllMonth = mMovieComingData?.filter {
                            it.rMonth == mArrMonthNum[position - 1]
                        }
                        //HIDE ALL FIRST
                        (0 until 6).forEachIndexed { index, _ ->
                            listCover?.get(index)?.visibility = View.GONE
                            listName?.get(index)?.visibility = View.GONE
                            listWannaCount?.get(index)?.visibility = View.GONE
                            listIwanna?.get(index)?.visibility = View.GONE
                        }
                        (0 until minOf(6, listAllMonth?.size!!)).forEachIndexed { _, i ->
                            tv_comemore.visibility = View.VISIBLE
                            //
                            listCover?.get(i)?.visibility = View.VISIBLE
                            listName?.get(i)?.visibility = View.VISIBLE
                            listWannaCount?.get(i)?.visibility = View.VISIBLE
                            listIwanna?.get(i)?.visibility = View.VISIBLE
                            //
                            Glide.with(ctx).load(listAllMonth[i].image).centerCrop().skipMemoryCache(false).into(listCover?.get(i)).onDestroy()
                            listName?.get(i)?.text = "<< ${listAllMonth[i].title} >>"
                            listWannaCount?.get(i)?.text = "${listAllMonth[i].wantedCount} 人想看"
                            //想看按钮
                            listIwanna?.get(i)?.visibility = View.VISIBLE
                            //
                            val bIsWant: Boolean
                            if (listAllMonth[i].id in mArrIWantSee) {
                                bIsWant = true
                                listIwanna?.get(i)?.setImageResource(R.drawable.movie_wanna_selected)
                            } else {
                                bIsWant = false
                                listIwanna?.get(i)?.setImageResource(R.drawable.movie_wanna_unselected)
                            }
                            //
                            tv_comemore.setOnClickListener {
                                (ctx.applicationContext as MTimeInKotlinApp).mComeSoonMonData = listAllMonth//缓存到app中
                                ctx.chAllstartActivity(mapOf("month" to mArrMonthNum[position - 1].toString()), AllComeSoonMovieActivity::class.java)
                            }
                            listIwanna?.get(i)?.setOnClickListener {
                                if (bIsWant) {
                                    deleteIWantSeeEntity(listAllMonth[i].id!!)
                                    wannaListener("影片：<< ${listAllMonth[i].title!!} >> 已取消想看")
                                } else {
                                    insertIWantSeeEntity(listAllMonth[i].id!!)
                                    wannaListener("影片：<< ${listAllMonth[i].title!!} >> 已加入想看")
                                }
                            }
                            listCover?.get(i)?.setOnClickListener {
                                ctx.chAllstartActivity(mapOf("movieid" to listAllMonth[i].id.toString(), "moviename" to listAllMonth[i].title!!), MovieDetailActivity::class.java)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        Glide.get(ctx).clearMemory()
    }

    override fun getItemCount(): Int = mArrMonthNum.size + 1//所有月份+attention

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listCover: List<ImageView>? = null
        var listIwanna: List<ImageView>? = null
        var listName: List<TextView>? = null
        var listWannaCount: List<TextView>? = null
        //
        val iv_title: TextView by lazy { itemView.findViewById<TextView>(R.id.come_title) }
        //
        val iv_cover1: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover1) }
        val iv_cover2: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover2) }
        val iv_cover3: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover3) }
        val iv_cover4: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover4) }
        val iv_cover5: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover5) }
        val iv_cover6: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_cover6) }
        //
        val iv_iwannasee1: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee1) }
        val iv_iwannasee2: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee2) }
        val iv_iwannasee3: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee3) }
        val iv_iwannasee4: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee4) }
        val iv_iwannasee5: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee5) }
        val iv_iwannasee6: ImageView by lazy { itemView.findViewById<ImageView>(R.id.iv_iwannasee6) }
        //
        val tv_name1: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name1) }
        val tv_name2: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name2) }
        val tv_name3: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name3) }
        val tv_name4: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name4) }
        val tv_name5: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name5) }
        val tv_name6: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_name6) }
        //
        val tv_wanna1: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna1) }
        val tv_wanna2: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna2) }
        val tv_wanna3: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna3) }
        val tv_wanna4: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna4) }
        val tv_wanna5: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna5) }
        val tv_wanna6: TextView by lazy { itemView.findViewById<TextView>(R.id.tv_wanna6) }
        //
        val tv_comemore: TextView by lazy { itemView.findViewById<TextView>(R.id.come_more) }

        init {
            AutoUtils.autoSize(itemView)

            listCover = arrayListOf(iv_cover1, iv_cover2, iv_cover3, iv_cover4, iv_cover5, iv_cover6)
            listName = arrayListOf(tv_name1, tv_name2, tv_name3, tv_name4, tv_name5, tv_name6)
            listWannaCount = arrayListOf(tv_wanna1, tv_wanna2, tv_wanna3, tv_wanna4, tv_wanna5, tv_wanna6)
            listIwanna = arrayListOf(iv_iwannasee1, iv_iwannasee2, iv_iwannasee3, iv_iwannasee4, iv_iwannasee5, iv_iwannasee6)
        }
    }
}