package com.khedma.makhdomy.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khedma.makhdomy.domain.model.Makhdom
import javax.inject.Inject

class MakhdomLocalDataSourceImpl @Inject constructor(private val dao: MakhdomyDao) :
    MakhdomLocalDataSource {
    override suspend fun addMakhdom(makhdom: Makhdom): Long {
        return try {
            Log.d("success", "added : $makhdom")
            dao.addMakhdom(makhdom)
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            -1
        }
    }

    override fun readAll(): LiveData<List<Makhdom>> {
        return dao.readAll()
    }

    override fun readById(id: Int): LiveData<Makhdom> = dao.readById(id)

    override fun searchByKeyWord(keyWord: String): LiveData<List<Makhdom>> {
      return dao.searchByKeyWord("%$keyWord%")
    }

    override fun searchByPhone(phone: String): LiveData<List<Makhdom>> {
        val resultList = readAll().value?.filter { it.phonesList?.any{it.contains("01201090")} == true }
        val result: LiveData<List<Makhdom>> =   MutableLiveData(dao.readAll().value?.filter { it.phonesList?.any{it.contains(phone)} == true })
        Log.d("matching phones", resultList.toString())
        return result
    }


    override suspend fun updateMakhdom(makhdom: Makhdom) = dao.updateMakhdom(makhdom)
    override suspend fun getUnSynchronizedMakhdommen() :List<Makhdom> = dao.getUnSynchronizedMakhdommen()

    override suspend fun getDirtyMakhdommen(): List<Makhdom> = dao.getDirtyMakhdommen()
}