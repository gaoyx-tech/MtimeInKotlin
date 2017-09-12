package com.lovejiaming.timemovieinkotlin.views.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.view.Menu
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.views.fragments.HotMovieFragment
import com.lovejiaming.timemovieinkotlin.views.fragments.FindFunnyFragment
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AutoLayoutActivity() {
    //
    lateinit var toggle: ActionBarDrawerToggle
    //
    val m_listFragments: ArrayList<Fragment> by lazy {
        arrayListOf(HotMovieFragment.newInstance(), FindFunnyFragment.newInstance())
    }
    var m_nCurrentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //toolbar和drawer产生联动
        setToolbarDrawer()
        //设置初始显示
        switchDisplayFragment(0)
        //
        navigation_main.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_menu_1 -> switchDisplayFragment(0)
                R.id.drawer_menu_2 -> switchDisplayFragment(1)
            }
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        navigation_main.setItemTextAppearance(R.style.SnackbarTextStyle)
    }

    fun switchDisplayFragment(nDisplayIndex: Int) {
        val mgr = supportFragmentManager.beginTransaction()
        //
        if (m_nCurrentIndex == nDisplayIndex) {
            if (!m_listFragments[nDisplayIndex].isAdded)
            //就是当前，但是还没添加进栈
                mgr.add(R.id.containerall, m_listFragments[nDisplayIndex]).show(m_listFragments[nDisplayIndex])
        } else {
            if (m_listFragments[nDisplayIndex].isAdded) {
                mgr.hide(m_listFragments[m_nCurrentIndex]).show(m_listFragments[nDisplayIndex])
            } else {
                mgr.hide(m_listFragments[m_nCurrentIndex]).add(R.id.containerall, m_listFragments[nDisplayIndex]).show(m_listFragments[nDisplayIndex])
            }
        }
        mgr.commit()
        m_nCurrentIndex = nDisplayIndex
    }

    fun setToolbarDrawer() {
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)
        drawer.addDrawerListener(toggle)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        //联动代码
        toolbar.setNavigationOnClickListener {
            if (!drawer.isDrawerVisible(GravityCompat.START))
                drawer.openDrawer(GravityCompat.START)
            else
                drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maintoolbar_find, menu)
        //
        val searchItem = menu?.findItem(R.id.main_action_find)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.queryHint = "输入电影名查找"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                startActivity(Intent(this@MainActivity, SearchResultActivity::class.java).putExtra("keyword", p0))
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        return true
    }
}
