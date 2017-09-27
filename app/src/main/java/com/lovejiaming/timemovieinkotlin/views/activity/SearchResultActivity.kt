package com.lovejiaming.timemovieinkotlin.views.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieSearchAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.MovieSearchResultItem
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCallMtime
import com.lovejiaming.timemovieinkotlin.networkbusiness.TagMovieSearchItem
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : AutoLayoutActivity() {
    //电影类型
    private val mArrTypes: Array<String> by lazy {
        arrayOf("剧情 ", "爱情 ", "喜剧 ", "科幻 ", "动作 ", "悬疑 ", "犯罪 ", "惊悚 ", "动画 ", "恐怖 ", "战争 ", "历史 ", "奇幻 ", "传记 ", "音乐 ", "家庭 ", "纪录片 ")
    }
    //year
    private val mArrYears: MutableList<Int> by lazy {
        val intArray = Array(18, { index -> index + 2000 })
        intArray.asList().toMutableList()
    }
    val mAdapter: MovieSearchAdapter by lazy {
        MovieSearchAdapter(this)
    }
    //最后一次获取的所有数据缓存，作为排序
    private lateinit var mResponseListOfName: List<MovieSearchResultItem>
    private lateinit var mResponseListOfTag: List<TagMovieSearchItem>
    //
    private var mLastYears: String = "-1"//year
    private var mLastTypes: String = "-1"//types
    private var mRequestType = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        initViews()
        setListeners()
        netRequestByName(intent.getStringExtra("keyword"))
    }

    private fun setListeners() {
        gridview_year.setOnItemClickListener { adapterView, view, i, _ ->
            (view as TextView).setTextColor(Color.parseColor("#bf360c"))
            mLastYears = "${view.text}-${view.text}"
            (0 until adapterView.count)
                    .filter { i != it }
                    .forEach { (adapterView.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE) }//智能转换
            netRequestByTag()
        }
        //
        gridview_type.setOnItemClickListener { adapterView, view, i, _ ->
            (view as TextView).setTextColor(Color.parseColor("#bf360c"))
            mLastTypes = view.text.toString()
            (0 until adapterView.count)
                    .filter { it != i }
                    .forEach { (adapterView.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE) }
            netRequestByTag()
        }
    }

    private fun initViews() {
        gridview_type.adapter = ArrayAdapter(this, R.layout.item_search_condition, mArrTypes)
        gridview_year.adapter = ArrayAdapter(this, R.layout.item_search_condition, mArrYears)
    }

    private fun netRequestByTag() {
        swipe_search.isRefreshing = true
        NetWorkRealCallMtime.newInstance()
                .getSearchMovieService()
                .requesSearchMovieFromTag(years = mLastYears, genreTypes = mLastTypes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mRequestType = "tag"
                    swipe_search.isRefreshing = false
                    gridview_result_content.smoothScrollToPosition(0)
                    mResponseListOfTag = it.data.movieModelList
                    mAdapter.addSearchResultDataOfTag(mResponseListOfTag)
                }
    }

    private fun netRequestByName(sName: String) {
        swipe_search.isRefreshing = true
        NetWorkRealCallMtime.newInstance()
                .getSearchMovieService()
                .requestSearchMovieFromName(Keyword = sName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mRequestType = "name"
                    swipe_search.isRefreshing = false
                    mResponseListOfName = it.movies
                    gridview_result_content.adapter = mAdapter
                    mAdapter.addSearchResultData(it.movies)
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.searchitem_sortscore -> {
                    swipe_search.isRefreshing = true
                    if (mRequestType == "name")
                        mAdapter.addSearchResultData(mResponseListOfName.sortedByDescending { it.rating?.toDouble() })
                    else
                        mAdapter.addSearchResultDataOfTag(mResponseListOfTag.sortedByDescending { it.ratingFinal })
                    swipe_search.isRefreshing = false
                }
                R.id.searchitem_sortyear -> {
                    swipe_search.isRefreshing = true
                    if (mRequestType == "name")
                        mAdapter.addSearchResultData(mResponseListOfName.sortedByDescending { it.rYear })
                    else
                        mAdapter.addSearchResultDataOfTag(mResponseListOfTag.sortedByDescending { it.rYear })
                    swipe_search.isRefreshing = false
                }
            }
            return@setOnMenuItemClickListener true
        }
        return true
    }
}
