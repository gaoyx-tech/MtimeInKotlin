package com.lovejiaming.timemovieinkotlin.views.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.route.*
import com.baidu.mapapi.utils.DistanceUtil
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.networkbusiness.CinemaFeature
import com.lovejiaming.timemovieinkotlin.views.overlaymgr.WalkingRouteOverlay
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_to_cinema_map.*
import java.lang.StringBuilder

class ToCinemaMapActivity : AutoLayoutActivity() {
    //经纬度
    var m_dbCinemaLatitude: Double? = null
    var m_dbCinemaLongitude: Double? = null
    //
    lateinit var mLocaitonClient: LocationClient
    lateinit var mSearch: RoutePlanSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_cinema_map)
        //
        mapview.onCreate(this, savedInstanceState)
        initToolbar()
        setUpCinemaLocation()
        //开始给自己定位
        startLocateSelf()
        //过滤特性
        filterFeature()
    }

    fun filterFeature() {
        val feature = intent.getSerializableExtra("feature") as CinemaFeature
        //
        val sb = StringBuilder()
        if (feature.has3D == 1) sb.append("3D:yes  ")
        if (feature.hasFeature4D == 1) sb.append("4D:yes  ")
        if (feature.hasFeature4K == 1) sb.append("4K:yes  ")
        if (feature.hasFeatureDolby == 1) sb.append("Dolby:yes  ")
        if (feature.hasIMAX == 1) sb.append("IMAX:yes  ")
        if (feature.hasWifi == 1) sb.append("WIFI:yes  ")
        if (feature.hasVIP == 1) sb.append("VIP:yes  ")
        if (feature.hasPark == 1) sb.append("Park:yes  ")
        cinema_feature.text = sb.toString()
    }

    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapview.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    fun startLocateSelf() {
        mLocaitonClient = LocationClient(this)
        //
        val option = LocationClientOption()
        option.isOpenGps = true
        option.setCoorType("bd09ll")
        mLocaitonClient.locOption = option
        mLocaitonClient.registerLocationListener {
            //定位成功后
            val planNodeSelf = PlanNode.withLocation(LatLng(it.latitude, it.longitude))
            val planNodeCinema = PlanNode.withLocation(LatLng(m_dbCinemaLatitude!!, m_dbCinemaLongitude!!))
            //
            cinema_name.text = "${intent.getStringExtra("name")}   距离：${DistanceUtil.getDistance(LatLng(m_dbCinemaLatitude!!, m_dbCinemaLongitude!!)
                    , LatLng(it.latitude, it.longitude)).toInt()} 米"
            //开始路线图
            mSearch = RoutePlanSearch.newInstance()
            mSearch.setOnGetRoutePlanResultListener(mSearchListener)
            mSearch.walkingSearch(WalkingRoutePlanOption()
                    .from(planNodeSelf).to(planNodeCinema))
        }
        mLocaitonClient.start()
    }

    val mSearchListener = object : OnGetRoutePlanResultListener {
        override fun onGetIndoorRouteResult(p0: IndoorRouteResult?) {
        }

        override fun onGetTransitRouteResult(p0: TransitRouteResult?) {
        }

        override fun onGetDrivingRouteResult(p0: DrivingRouteResult?) {
        }

        override fun onGetWalkingRouteResult(p0: WalkingRouteResult?) {
            val overlay = WalkingRouteOverlay(mapview.map)
            overlay.setData(p0?.routeLines?.get(0))
            overlay.addToMap()
            overlay.zoomToSpan()
        }

        override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult?) {
        }

        override fun onGetBikingRouteResult(p0: BikingRouteResult?) {
        }
    }

    fun setUpCinemaLocation() {
        this.m_dbCinemaLatitude = intent.getDoubleExtra("latitude", 0.0)
        this.m_dbCinemaLongitude = intent.getDoubleExtra("longitude", 0.0)
    }

    fun initToolbar() {
        setSupportActionBar(cinema_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)// 给左上角图标的左边加上一个返回的图标
        supportActionBar?.setHomeButtonEnabled(true)//true 图标可以点击  false 不可以点击
        supportActionBar?.setDisplayShowHomeEnabled(false)//使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
        cinema_toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
