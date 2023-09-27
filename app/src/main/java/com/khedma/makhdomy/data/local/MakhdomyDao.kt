package com.khedma.makhdomy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.khedma.makhdomy.domain.model.Makhdom

@Dao
interface MakhdomyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMakhdom(makhdom: Makhdom)

    @Query("select * from makhdom_table")
    fun readAll(): LiveData<List<Makhdom>>

    @Query("select * from makhdom_table where id = :id")
    fun readById(id: Int): LiveData<Makhdom>

    @Update
    suspend fun updateMakhdom(makhdom: Makhdom)


}