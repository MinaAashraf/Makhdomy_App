package com.khedma.makhdomy.data.repository

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.data.local.MakhdomLocalDataSource
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class MakhdomRepositoryImpl @Inject constructor(private val makhdomLocalDataSource: MakhdomLocalDataSource) :

    MakhdomRepository {
    override suspend fun addMakhdom(makhdom: Makhdom) =
        makhdomLocalDataSource.addMakhdom(makhdom)


    override fun readAll(): LiveData<List<Makhdom>> = makhdomLocalDataSource.readAll()

    override fun readById(id: Int): LiveData<Makhdom> = makhdomLocalDataSource.readById(id)

    override suspend fun updateMakhdom(makhdom: Makhdom) =
        makhdomLocalDataSource.updateMakhdom(makhdom)

}