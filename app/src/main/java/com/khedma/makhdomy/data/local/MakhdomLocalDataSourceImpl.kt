package com.khedma.makhdomy.data.local

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.model.Makhdom
import javax.inject.Inject

class MakhdomLocalDataSourceImpl @Inject constructor(private val dao: MakhdomyDao) :
    MakhdomLocalDataSource {
    override suspend fun addMakhdom(makhdom: Makhdom) {
        dao.addMakhdom(makhdom)
    }

    override fun readAll(): LiveData<List<Makhdom>> = dao.readAll()

    override fun readById(id: Int): LiveData<Makhdom> = dao.readById(id)

    override suspend fun updateMakhdom(makhdom: Makhdom) = dao.updateMakhdom(makhdom)

}