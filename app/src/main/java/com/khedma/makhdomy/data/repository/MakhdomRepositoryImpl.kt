package com.khedma.makhdomy.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.khedma.makhdomy.data.local.MakhdomLocalDataSource
import com.khedma.makhdomy.data.remote.KhademRemoteDataSource
import com.khedma.makhdomy.data.remote.MakhdomRemoteDataSource
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.onFailure
import com.khedma.makhdomy.domain.onSuccess
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import com.khedma.makhdomy.presentation.utils.convertToByteArr
import javax.inject.Inject

class MakhdomRepositoryImpl @Inject constructor(
    private val makhdomLocalDataSource: MakhdomLocalDataSource,
    private val makhdomRemoteDataSource: MakhdomRemoteDataSource,
    private val khademRemoteDataSource: KhademRemoteDataSource,
) :

    MakhdomRepository {
    override suspend fun addMakhdom(makhdom: Makhdom) {
        makhdom.id?.let {
            // already exist in local database but not uploaded to cloud yet
            addPublicMakhdom(makhdom, it)
        } ?: kotlin.run {
            // not exist in local database nor cloud
            val localMakhdomId = makhdomLocalDataSource.addMakhdom(makhdom)
            addPublicMakhdom(makhdom, localMakhdomId.toInt())
        }

    }

    override suspend fun addPublicMakhdom(makhdom: Makhdom, localMakhdomId: Int) {
        try {
            makhdomRemoteDataSource.addMakhdom(
                makhdom.picture?.convertToByteArr(),
                makhdom.mapToMakhdomyData()
            ).onSuccess { makhdomKey ->
                makhdom.apply {
                    makhdom.id = localMakhdomId
                    makhdom.isSynchronized = true
                    makhdom.makhdomKey = makhdomKey
                }
                updateMakhdom(makhdom)
                Log.d("firebase success: ", makhdom.toString())
                khademRemoteDataSource.addMakhdomIdToKhadem(makhdomKey = makhdomKey)
            }.onFailure {
                Log.d("firebase exception: ", it.message.toString())
            }
        } catch (e: Exception) {
            Log.d("firebase exception: ", e.message.toString())
        }
    }


    override fun readAll(): LiveData<List<Makhdom>> = makhdomLocalDataSource.readAll()

    override fun readById(id: Int): LiveData<Makhdom> = makhdomLocalDataSource.readById(id)

    override fun searchByKeyWord(keyWord: String): LiveData<List<Makhdom>> =
        makhdomLocalDataSource.searchByKeyWord(keyWord)


    override suspend fun updateMakhdom(makhdom: Makhdom) =
        makhdomLocalDataSource.updateMakhdom(makhdom)

    override suspend fun addKhadem(khadem: Khadem): Result<String> =
        khademRemoteDataSource.addKhadem(khadem)

    override suspend fun getNotSynchMakhdommen(): List<Makhdom> =
        makhdomLocalDataSource.getUnSynchronizedMakhdommen()


}