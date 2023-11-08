package com.khedma.makhdomy.domain.repository

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.model.Makhdom

interface MakhdomRepository  {

    suspend fun addMakhdom(makhdom: Makhdom)

    suspend fun addPublicMakhdom (makhdom: Makhdom, localMakhdomId : Int)

    suspend fun updatePublicMakhdom (makhdom: Makhdom)

    fun readAll(): LiveData<List<Makhdom>>

    fun readById(id: Int): LiveData<Makhdom>

    fun searchByKeyWord (keyWord:String) : LiveData<List<Makhdom>>
    fun searchByPhone (phone:String) : LiveData<List<Makhdom>>

    suspend fun updateMakhdom(makhdom: Makhdom)
    suspend fun updateMakhdomLocally(makhdom: Makhdom)
    suspend fun updateMakhdomRemotely(makhdom: Makhdom)

    suspend fun addKhadem(khadem: Khadem): Result<String>

    suspend fun getNotSynchMakhdommen () : List<Makhdom>

    suspend fun getDirtyMakhdommen () : List<Makhdom>


    suspend fun readKhadem (khademKey : String) : Result<Khadem>
}