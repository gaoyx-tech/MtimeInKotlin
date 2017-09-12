package com.lovejiaming.timemovieinkotlin.databasebusiness

import android.annotation.SuppressLint
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by xiaoxin on 2017/8/31.
 */
@Database(entities = arrayOf(WantSeeEntity::class, HaveSeenEntity::class), version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun WantSeeDao(): WantSeeDao
    abstract fun HaveSeenDao(): HaveSeenDao
}

@SuppressLint("StaticFieldLeak")
class MovieRoomOperate private constructor(ctx: Context) {
    //
    val appDataBase = Room.databaseBuilder(ctx, AppDataBase::class.java, "timemovieinkt").build()!!

    companion object {
        lateinit var mContext: Context
        val objectInstance: MovieRoomOperate by lazy {
            MovieRoomOperate(mContext)
        }

        //
        fun newInstance(ctx: Context): MovieRoomOperate {
            mContext = ctx.applicationContext
            return objectInstance
        }
    }

    //插入一条数据
    fun insertOneMovieWantSee(movieId: Int) =
            appDataBase.WantSeeDao().insertWantSee(WantSeeEntity(movieId = movieId))

    //查询所有数据
    fun queryAllMovieWantSee() = appDataBase.WantSeeDao().queryAllWantSeeData()

    //删除一条数据
    fun deleteOneMovieWantSee(movieId: Int) {
        val id = appDataBase.WantSeeDao().queryOneWantSeeData(movieId).id//这里一定要取得主键，才能删除
        appDataBase.WantSeeDao().deleteOneWantSee(WantSeeEntity(id = id))
    }

    fun insertOneHaveSeen(movieId: Int) = appDataBase.HaveSeenDao().insertHaveSeen(HaveSeenEntity(movieId = movieId))

    fun queryAllHaveSeen() = appDataBase.HaveSeenDao().queryAllHaveSeenData()

    fun deleteOneHaveSeen(movieId: Int) {
        val primaryId = appDataBase.HaveSeenDao().queryOneHaveSeenData(movieId).id
        appDataBase.HaveSeenDao().deleteOneHaveSeen(HaveSeenEntity(primaryId))
    }
}