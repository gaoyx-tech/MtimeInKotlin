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
import android.widget.RelativeLayout
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.mTimeDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.DetailCommentItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.MovieDetailInfo
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonDetailAll
import com.lovejiaming.timemovieinkotlin.views.activity.*
import com.lovejiaming.timemovieinkotlin.views.ownerdraw.CustomRatingView
import com.zhy.autolayout.utils.AutoUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xiaoxin on 2017/9/1.
 */
class MovieDetailAdapter(val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //detail数据
    var mMovieDetail: MovieDetailInfo? = null
    var m_nMovieId = -1
    //ITEMTYPE
    private val HEAD_TYPE = 1
    private val CONTENT_TYPE = 2
    private val IMAGE_TYPE = 3
    private val VIDEO_TYPE = 4
    private val PERSON_TYPE = 5
    private val COMMENT_TYPE = 6
    //
    fun insertDetailData(detail: MovieDetailInfo, movieId: Int) {
        this.mMovieDetail = detail
        m_nMovieId = movieId
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
            when (position) {
                0 -> HEAD_TYPE
                1 -> CONTENT_TYPE
                2 -> IMAGE_TYPE
                3 -> VIDEO_TYPE
                4 -> PERSON_TYPE
                5 -> COMMENT_TYPE
                else -> -1
            }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            HEAD_TYPE -> {
                with(holder as HeadInfoViewHolder) {
                    head_name?.text = "片名：${mMovieDetail?.titleCn} \r\n${mMovieDetail?.titleEn} "
                    head_director?.text = "导演：${mMovieDetail?.director?.directorName} "
                    head_runtime?.text = "时长：${mMovieDetail?.runTime} "
                    head_year?.text = "年代：${mMovieDetail?.year} "
                    head_cover?.mTimeDisplayImage(ctx, mMovieDetail?.image)
                    //
                    rating?.setRating(mMovieDetail?.rating?.toFloat()?.times(10f) ?: 0f)
                    //
                    head_actor?.text = "主演：${mMovieDetail?.actorList?.map { it.actor }?.joinToString(" ", "", "")} "//list直接变为string
                    head_type?.text = "类型：${mMovieDetail?.type?.map { it }?.joinToString(" ", "", "")} "
                }
            }
            CONTENT_TYPE -> {
                (holder as ContentViewHolder).content?.text = "简介： \r\n${mMovieDetail?.content} "
            }
            IMAGE_TYPE -> {
                mMovieDetail?.let {
                    val size = mMovieDetail?.images?.size ?: 0
                    (0 until minOf(size, 4)).forEach {
                        (holder as ImageInfoViewHolder).listImages[it]?.mTimeDisplayImage(ctx, mMovieDetail?.images?.get(it))
                    }
                }
            }
            VIDEO_TYPE -> {
                mMovieDetail?.let {
                    val size = mMovieDetail?.videos?.size ?: 0//null则为0
                    with(holder as VideoInfoViewHolder) {
                        (0 until minOf(size, 3)).forEach {
                            listVLayouts[it]?.visibility = View.VISIBLE
                            listVImages[it]?.mTimeDisplayImage(ctx, mMovieDetail?.videos?.get(it)?.image)
                            listVNames[it]?.text = mMovieDetail?.videos?.get(it)?.title
                            //跳转
                            val strPath = mMovieDetail?.videos?.get(it)?.url
                            val strName = mMovieDetail?.videos?.get(it)?.title
                            listVImages[it]?.setOnClickListener {
                                val intent = Intent(ctx, PlayVideoActivity::class.java)
                                intent.putExtra("path", strPath)
                                intent.putExtra("name", strName)
                                ctx.startActivity(intent)
                            }
                            moretrailer?.setOnClickListener {
                                val intent = Intent(ctx, MovieDetailOfTrailerActivity::class.java)
                                intent.putExtra("movieid", m_nMovieId)
                                ctx.startActivity(intent)
                            }
                        }
                    }
                }
            }
            PERSON_TYPE -> {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        when (viewType) {
            HEAD_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_headinfo, null)
                AutoUtils.autoSize(view)
                return HeadInfoViewHolder(view)
            }
            CONTENT_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_contentinfo, null)
                AutoUtils.autoSize(view)
                return ContentViewHolder(view)
            }
            IMAGE_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_imageinfo, null)
                AutoUtils.autoSize(view)
                return ImageInfoViewHolder(view)
            }
            VIDEO_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_videoinfo, null)
                AutoUtils.autoSize(view)
                return VideoInfoViewHolder(view)
            }
            PERSON_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_recyclerperson, null)
                AutoUtils.autoSize(view)
                return PersonRecyclerViewHolder(ctx, view)
            }
            COMMENT_TYPE -> {
                view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_recyclerperson, null)
                AutoUtils.autoSize(view)
                return CommentViewHolder(ctx, view)
            }
        }
        return null!!
    }

    override fun getItemCount(): Int = 6

    class HeadInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val head_name by lazy { itemView?.findViewById<TextView>(R.id.head_name) }
        val head_year by lazy { itemView?.findViewById<TextView>(R.id.head_year) }
        val head_runtime by lazy { itemView?.findViewById<TextView>(R.id.head_runtime) }
        val head_director by lazy { itemView?.findViewById<TextView>(R.id.head_director) }
        val head_actor by lazy { itemView?.findViewById<TextView>(R.id.head_actor) }
        val head_type by lazy { itemView?.findViewById<TextView>(R.id.head_type) }
        val head_cover by lazy { itemView?.findViewById<ImageView>(R.id.head_cover) }
        val rating by lazy { itemView?.findViewById<CustomRatingView>(R.id.rating) }
    }

    class VideoInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val v_image1 by lazy { itemView?.findViewById<ImageView>(R.id.v_image1) }
        val v_image2 by lazy { itemView?.findViewById<ImageView>(R.id.v_image2) }
        val v_image3 by lazy { itemView?.findViewById<ImageView>(R.id.v_image3) }
        //
        val v_name1 by lazy { itemView?.findViewById<TextView>(R.id.v_name1) }
        val v_name2 by lazy { itemView?.findViewById<TextView>(R.id.v_name2) }
        val v_name3 by lazy { itemView?.findViewById<TextView>(R.id.v_name3) }
        //
        val layout1 by lazy { itemView?.findViewById<RelativeLayout>(R.id.layout_image1) }
        val layout2 by lazy { itemView?.findViewById<RelativeLayout>(R.id.layout_image2) }
        val layout3 by lazy { itemView?.findViewById<RelativeLayout>(R.id.layout_image3) }
        //
        val moretrailer = itemView?.findViewById<TextView>(R.id.more_trailer)
        //
        val listVImages: MutableList<ImageView?>
        val listVNames: MutableList<TextView?>
        val listVLayouts: MutableList<RelativeLayout?>

        init {
            listVImages = mutableListOf(v_image1, v_image2, v_image3)
            listVNames = mutableListOf(v_name1, v_name2, v_name3)
            listVLayouts = mutableListOf(layout1, layout2, layout3)
        }
    }

    class ContentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val content = itemView?.findViewById<TextView>(R.id.content)
    }

    class ImageInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val image1 by lazy { itemView?.findViewById<ImageView>(R.id.image1) }
        val image2 by lazy { itemView?.findViewById<ImageView>(R.id.image2) }
        val image3 by lazy { itemView?.findViewById<ImageView>(R.id.image3) }
        val image4 by lazy { itemView?.findViewById<ImageView>(R.id.image4) }
        val listImages: MutableList<ImageView?>

        init {
            listImages = mutableListOf(image1, image2, image3, image4)
        }
    }

    class PersonRecyclerViewHolder(val ctx: Context, itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val recycler_persons = itemView?.findViewById<RecyclerView>(R.id.detail_recyclerview_persons)
        val more_person = itemView?.findViewById<TextView>(R.id.more_person)
        val mAdapter: PersonListAdapter by lazy { PersonListAdapter(ctx) }

        init {
            recycler_persons?.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            recycler_persons?.adapter = mAdapter
            recycler_persons?.addItemDecoration(SimpleItemDecorationHor())
            //
            more_person?.setOnClickListener {
                val intent = Intent(ctx, MovieDetailOfPersonActivity::class.java)
                intent.putExtra("allperson", PersonListAdapter.mPersonAll)
                ctx.startActivity(intent)
            }
        }
    }

    //person adapter
    class PersonListAdapter(val ctx: Context) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {
        companion object {
            var mPersonAll: PersonDetailAll? = null
            fun addPersonList(personDetailAll: PersonDetailAll) {
                this.mPersonAll = personDetailAll
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PersonListAdapter.ViewHolder {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_person_detail, null)
            AutoUtils.autoSize(view)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = minOf(mPersonAll?.types?.get(1)?.persons?.size?.plus(1) ?: 0, 8)

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: PersonListAdapter.ViewHolder?, position: Int) {
            with(holder!!) {
                when (position) {
                    0 -> {
                        head?.mTimeDisplayImage(ctx, mPersonAll?.types?.get(0)?.persons?.get(0)?.image)
                        job?.text = "${mPersonAll?.types?.get(0)?.typeName}: "
                        name?.text = "${mPersonAll?.types?.get(0)?.persons?.get(0)?.name} "
                        itemView.setOnClickListener {
                            val intent = Intent(ctx, PersonDetailActivity::class.java)
                            intent.putExtra("personid", mPersonAll?.types?.get(0)?.persons?.get(0)?.id)
                            intent.putExtra("personname", mPersonAll?.types?.get(0)?.persons?.get(0)?.name)
                            ctx.startActivity(intent)
                        }
                    }
                    else -> {
                        head?.mTimeDisplayImage(ctx, mPersonAll?.types?.get(1)?.persons?.get(position - 1)?.image)
                        job?.text = "${mPersonAll?.types?.get(1)?.typeName}: "
                        name?.text = "${mPersonAll?.types?.get(1)?.persons?.get(position - 1)?.name} "
                        itemView.setOnClickListener {
                            val intent = Intent(ctx, PersonDetailActivity::class.java)
                            intent.putExtra("personid", mPersonAll?.types?.get(1)?.persons?.get(position - 1)?.id)
                            intent.putExtra("personname", mPersonAll?.types?.get(1)?.persons?.get(position - 1)?.name)
                            ctx.startActivity(intent)
                        }
                    }
                }
            }
        }

        //
        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            val job = itemView?.findViewById<TextView>(R.id.person_job)
            val head = itemView?.findViewById<ImageView>(R.id.person_head)
            val name = itemView?.findViewById<TextView>(R.id.person_name)
        }
    }

    //comment list
    class CommentViewHolder(val ctx: Context, itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val recyclerView by lazy { itemView?.findViewById<RecyclerView>(R.id.detail_recyclerview_persons) }
        val more_person by lazy { itemView?.findViewById<TextView>(R.id.more_person) }
        val mAdapter: MovieDetailCommentAdapter by lazy {
            MovieDetailCommentAdapter(ctx)
        }

        init {
            more_person?.text = "热门短评 "
            recyclerView?.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
            recyclerView?.adapter = mAdapter
        }
    }

    class MovieDetailCommentAdapter(val ctx: Context) : RecyclerView.Adapter<MovieDetailCommentAdapter.ViewHolder>() {
        companion object {
            var m_listComment: ArrayList<DetailCommentItem>? = null
            fun insertAllComment(cts: ArrayList<DetailCommentItem>) {
                m_listComment = cts
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: MovieDetailCommentAdapter.ViewHolder?, position: Int) {
            holder?.apply {
                comment_head?.mTimeDisplayImage(ctx, m_listComment?.get(position)?.caimg)
                comment_name_address?.text = "${m_listComment?.get(position)?.ca}    (${m_listComment?.get(position)?.cal})"
                comment_info?.text = m_listComment?.get(position)?.ce
                comment_score?.text = "打分 ${m_listComment?.get(position)?.cr}"
                //
                val strTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(m_listComment?.get(position)?.cd!!.times(1000)))//乘以1000，from70y
                comment_time?.text = strTime
            }
        }

        override fun getItemCount(): Int = m_listComment?.size ?: 0

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_detail_comment, null)
            AutoUtils.autoSize(view)
            return ViewHolder(view)
        }

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            val comment_head by lazy { itemView?.findViewById<ImageView>(R.id.comment_head) }
            val comment_name_address by lazy { itemView?.findViewById<TextView>(R.id.comment_name_address) }
            val comment_info by lazy { itemView?.findViewById<TextView>(R.id.comment_info) }
            val comment_time by lazy { itemView?.findViewById<TextView>(R.id.comment_time) }
            val comment_score by lazy { itemView?.findViewById<TextView>(R.id.comment_score) }
        }
    }
}