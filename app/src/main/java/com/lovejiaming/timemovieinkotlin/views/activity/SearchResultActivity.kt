package com.lovejiaming.timemovieinkotlin.views.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.MovieSearchAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.MovieSearchResultList
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Douban
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_result.*
import java.util.*

class SearchResultActivity : AutoLayoutActivity() {
    //电影类型
    val mArrTypes: Array<String> by lazy {
        arrayOf("剧情", "爱情", "喜剧", "科幻", "动作", "悬疑", "犯罪", "恐怖", "青春", "励志", "战争", "文艺", "魔幻", "传记", "音乐", "家庭")
    }
    //year
    val mArrYears: MutableList<Int> by lazy {
        val intArray = Array(18, { index -> index + 2000 })
        intArray.asList().toMutableList()
    }
    val mAdapter: MovieSearchAdapter by lazy {
        MovieSearchAdapter(this)
    }
    //最后一次请求时by tag还是by name
    lateinit var mLastRequestType: String
    //最后一次请求的关键字
    lateinit var mLastRequestKeyWord: String
    //最后一次获取的所有数据缓存，作为排序
    lateinit var mLastResponseList: List<MovieSearchResultList>

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

    fun setListeners() {
        gridview_year.setOnItemClickListener { adapterView, view, i, _ ->
            (view as? TextView)?.setTextColor(Color.parseColor("#bf360c"))
            (0 until adapterView.count)
                    .filter { i != it }
                    .forEach { (adapterView.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE) }//智能转换
            (0 until gridview_type.count)
                    .forEach {
                        (gridview_type.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE)
                    }
            //
            netRequestByTag(mArrYears[i].toString())
        }
        //
        gridview_type.setOnItemClickListener { adapterView, view, i, l ->
            (view as? TextView)?.setTextColor(Color.parseColor("#bf360c"))
            (0 until adapterView.count)
                    .filter { it != i }
                    .forEach { (adapterView.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE) }
            (0 until gridview_year.count)
                    .forEach { (gridview_year.getChildAt(it) as? TextView)?.setTextColor(Color.WHITE) }
            //
            netRequestByTag(mArrTypes[i])
        }
    }

    fun initViews() {
        //再刷新随意从一个start开始，豆瓣接口
        swipe_search.setOnRefreshListener {
            val nStart = Random().nextInt(50)//random
            if (mLastRequestType == "bytag")
                netRequestByTag(mLastRequestKeyWord, start = nStart)
            else
                netRequestByName(mLastRequestKeyWord, start = nStart)
        }
        gridview_type.adapter = ArrayAdapter(this, R.layout.item_search_condition, mArrTypes)
        gridview_year.adapter = ArrayAdapter(this, R.layout.item_search_condition, mArrYears)
    }

    fun netRequestByTag(sTag: String, start: Int = 0) {
        this.mLastRequestKeyWord = sTag
        this.mLastRequestType = "bytag"
        swipe_search.isRefreshing = true
        NetWorkRealCall_Douban.newInstance()
                .getMovieSearchService()
                .requesSearchMovieFromTag(sTag, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    swipe_search.isRefreshing = false
                    gridview_result_content.smoothScrollToPosition(0)
                    mLastResponseList = it.subjects
                    mAdapter.addSearchResultData(it.subjects)
                }
    }

    fun netRequestByName(sName: String, start: Int = 0) {
        this.mLastRequestKeyWord = sName
        this.mLastRequestType = "byname"
        swipe_search.isRefreshing = true
        NetWorkRealCall_Douban.newInstance()
                .getMovieSearchService()
                .requestSearchMovieFromName(sName, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    swipe_search.isRefreshing = false
                    mLastResponseList = it.subjects
                    gridview_result_content.adapter = mAdapter
                    mAdapter.addSearchResultData(it.subjects)
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        toolbar.setOnMenuItemClickListener {
            item ->
            when (item.itemId) {
                R.id.searchitem_sortscore -> {
                    swipe_search.isRefreshing = true
                    mAdapter.addSearchResultData(mLastResponseList.sortedByDescending {
                        it.rating.average
                    })
                    swipe_search.isRefreshing = false
                }
                R.id.searchitem_sortyear -> {
                    swipe_search.isRefreshing = true
                    mAdapter.addSearchResultData(mLastResponseList.sortedByDescending {
                        it.year?.toInt()
                    })
                    swipe_search.isRefreshing = false
                }
            }
            return@setOnMenuItemClickListener true
        }
        return true
    }

}
