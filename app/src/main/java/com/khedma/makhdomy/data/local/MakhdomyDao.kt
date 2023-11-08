package com.khedma.makhdomy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khedma.makhdomy.domain.model.Makhdom

@Dao
interface MakhdomyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMakhdom(makhdom: Makhdom): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMakhdommen(makhdommen: List<Makhdom>) : List<Long>

    @Query("select * from makhdom_table")
    fun readAll(): LiveData<List<Makhdom>>

    @Query("select * from makhdom_table where id = :id")
    fun readById(id: Int): LiveData<Makhdom>

    @Query(
        "Select * from makhdom_table" +
                " where name like :keyWord or className like :keyWord or addressArea like :keyWord" +
                " or streetName like :keyWord or motafare3From like :keyWord"
    )
    fun searchByKeyWord(keyWord: String): LiveData<List<Makhdom>>

    @Update
    suspend fun updateMakhdom(makhdom: Makhdom)


    @Query("select * from makhdom_table where isSynchronized=0")
    suspend fun getUnSynchronizedMakhdommen(): List<Makhdom>

    @Query("select * from makhdom_table where isSynchronized = 1 and isDirty=1")
    suspend fun getDirtyMakhdommen(): List<Makhdom>

    @Query("Delete from makhdom_table")
    suspend fun clear()


}