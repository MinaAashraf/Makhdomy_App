package com.khedma.makhdomy.data.local

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.model.Makhdom

interface MakhdomLocalDataSource {

    suspend fun addMakhdom(makhdom: Makhdom)

    fun readAll(): LiveData<List<Makhdom>>

    fun readById(id: Int): LiveData<Makhdom>

    fun searchByKeyWord (keyWord : String) : LiveData<List<Makhdom>>

    suspend fun updateMakhdom(makhdom: Makhdom)

}