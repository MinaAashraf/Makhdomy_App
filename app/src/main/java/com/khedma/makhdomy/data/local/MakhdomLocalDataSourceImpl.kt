package com.khedma.makhdomy.data.local

import android.util.Log
import androidx.lifecycle.LiveData
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

    override fun searchByKeyWord(keyWord: String): LiveData<List<Makhdom>> =
        dao.searchByKeyWord("%$keyWord%")

    override suspend fun updateMakhdom(makhdom: Makhdom) = dao.updateMakhdom(makhdom)
    override suspend fun getUnSynchronizedMakhdommen() :List<Makhdom> = dao.getUnSynchronizedMakhdommen()

}