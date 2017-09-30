package com.lovejiaming.timemovieinkotlin.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.chAllDisplayImage
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonDetail
import com.lovejiaming.timemovieinkotlin.networkbusiness.PersonDetailAll
import com.lovejiaming.timemovieinkotlin.views.activity.PersonDetailActivity
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by xiaoxin on 2017/9/7.
 */
class MovieDetailAllPersonAdapter(val ctx: Context, person: PersonDetailAll) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //应显示项目个数，职责名称+具体人数
    var m_nShouldDisplayCount: Int = 0
    //typename数组
    var m_arrTypeNames: ArrayList<String> = arrayListOf()
    //typename索引数组，recyclerview中显示位置
    var m_arrTypeNameIndex: ArrayList<Int> = arrayListOf()
    //显示类型
    val JOB_TEXT_TYPE = 1
    val PERSON_TYPE = 2
    //真正的存储数据
    var m_arrRealPersonInfo: ArrayList<PersonDetail> = arrayListOf()

    init {
        this.m_nShouldDisplayCount += person.types.size
        //第0个位置放一种类型文字
        this.m_arrTypeNameIndex.add(0)
        var textCount = 0
        person.types.forEach {
            //应显示项数
            this.m_nShouldDisplayCount += it.persons.size
            //
            this.m_arrTypeNames.add(it.typeName)
            //合并所有进list
            this.m_arrRealPersonInfo.addAll(it.persons)
            textCount += it.persons.size + 1
            this.m_arrTypeNameIndex.add(textCount)
        }
        this.m_arrTypeNameIndex.removeAt(m_arrTypeNameIndex.size - 1)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = if (position in m_arrTypeNameIndex) JOB_TEXT_TYPE else PERSON_TYPE

    override fun getItemCount(): Int = m_nShouldDisplayCount

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == JOB_TEXT_TYPE) {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_come_ofalldate, null)
                PersonJobTextViewHolder(view)
            } else {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_hot_movie_come_ofall, null)
                PersonViewHolder(view)
            }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            JOB_TEXT_TYPE -> {
                m_arrTypeNameIndex.forEachIndexed { index, value ->
                    if (value == position) {
                        (holder as PersonJobTextViewHolder).apply { textJob?.text = m_arrTypeNames[index] }
                    }
                }
            }
            PERSON_TYPE -> {
                with(holder as PersonViewHolder) {
                    val forsize = m_arrTypeNameIndex.filter { position > it }
                    name?.text = m_arrRealPersonInfo[position - forsize.size].name
                    nameEn?.text = m_arrRealPersonInfo[position - forsize.size].nameEn
                    head?.chAllDisplayImage(ctx, m_arrRealPersonInfo[position - forsize.size].image)
                    //
                    itemView.setOnClickListener {
                        val intent = Intent(ctx, PersonDetailActivity::class.java)
                        intent.putExtra("personid", m_arrRealPersonInfo[position - forsize.size].id)
                        intent.putExtra("personname", m_arrRealPersonInfo[position - forsize.size].name)
                        ctx.startActivity(intent)
                    }
                }
            }
        }
    }

    //复用
    class PersonViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val name = itemView?.findViewById<TextView>(R.id.comeall_name)
        val nameEn = itemView?.findViewById<TextView>(R.id.comeall_director)
        val head = itemView?.findViewById<ImageView>(R.id.comeall_cover)

        init {
            AutoUtils.autoSize(itemView)

        }
    }

    class PersonJobTextViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textJob = itemView?.findViewById<TextView>(R.id.comeall_releasedate)

        init {
            AutoUtils.autoSize(itemView)

        }
    }
}