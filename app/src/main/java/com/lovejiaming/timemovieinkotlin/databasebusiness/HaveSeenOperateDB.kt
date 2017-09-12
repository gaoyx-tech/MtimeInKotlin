package com.lovejiaming.timemovieinkotlin.databasebusiness

import android.arch.persistence.room.*

/**
 * Created by xiaoxin on 2017/8/31.
 */
@Entity(tableName = "HaveSeen")
data class HaveSeenEntity(@PrimaryKey(autoGenerate = true) var id: Long? = null, var movieId: Int = 0) {
    constructor() : this(0)
}

@Dao
interface HaveSeenDao {
    @Insert
    fun insertHaveSeen(entity: HaveSeenEntity)

    @Query("SELECT * FROM HaveSeen")
    fun queryAllHaveSeenData(): List<HaveSeenEntity>

    @Query("SELECT * FROM HaveSeen where movieId = :arg0")
    fun queryOneHaveSeenData(movieId: Int): HaveSeenEntity

    @Delete
    fun deleteOneHaveSeen(entity: HaveSeenEntity)
}