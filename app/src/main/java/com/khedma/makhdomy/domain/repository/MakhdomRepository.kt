package com.khedma.makhdomy.domain.repository

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.model.Makhdom

interface MakhdomRepository  {

    suspend fun addMakhdom(makhdom: Makhdom)

    fun readAll(): LiveData<List<Makhdom>>

    fun readById(id: Int): LiveData<Makhdom>

    fun searchByKeyWord (keyWord:String) : LiveData<List<Makhdom>>

    suspend fun updateMakhdom(makhdom: Makhdom)


    suspend fun addKhadem(khadem: Khadem): Result<String>


}