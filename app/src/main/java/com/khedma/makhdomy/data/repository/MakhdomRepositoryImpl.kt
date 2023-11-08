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
import kotlinx.coroutines.tasks.await
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
            try {
                val localMakhdomId = makhdomLocalDataSource.addMakhdom(makhdom)
                addPublicMakhdom(makhdom, localMakhdomId.toInt())
            } catch (e: Exception) {
                Log.d("imageInsertException: ", e.message.toString())
            }
        }

    }

    override suspend fun updateMakhdom(makhdom: Makhdom) {
        updateMakhdomLocally(makhdom)
        updateMakhdomRemotely(makhdom)
    }

    override suspend fun updateMakhdomLocally(makhdom: Makhdom) {
        makhdomLocalDataSource.updateMakhdom(makhdom)
    }

    override suspend fun updateMakhdomRemotely(makhdom: Makhdom) {
        // if makhdom object added remotely (synchronized) before : just update it remotely
        if (makhdom.isSynchronized && makhdom.isDirty)
            updatePublicMakhdom(makhdom)

        // if makhdom object is not added remotely (not synchronized) before: add it
        else if (!makhdom.isSynchronized)
            addPublicMakhdom(makhdom, makhdom.id!!)
    }

    override suspend fun addPublicMakhdom(makhdom: Makhdom, localMakhdomId: Int) {
        try {
            makhdomRemoteDataSource.addMakhdom(
                makhdom.picture?.convertToByteArr(),
                makhdom.mapToMakhdomyData()
            ).onSuccess { sharedParameters ->
                makhdom.apply {
                    makhdom.id = localMakhdomId
                    makhdom.isSynchronized = true
                    makhdom.makhdomKey = sharedParameters.key
                    makhdom.remotePictureUrl = sharedParameters.picUrl
                }
                updateMakhdom(makhdom)
                Log.d("firebase success: ", makhdom.toString())
                khademRemoteDataSource.addMakhdomIdToKhadem(makhdomKey = sharedParameters.key)
            }.onFailure {
                Log.d("firebase exception: ", it.message.toString())
            }
        } catch (e: Exception) {
            Log.d("firebase exception: ", e.message.toString())
        }
    }

    override suspend fun updatePublicMakhdom(makhdom: Makhdom) {
        try {
            makhdomRemoteDataSource.updateMakhdom(
                picture = if (makhdom.isPictureUpdated) makhdom.picture?.convertToByteArr() else null,
                makhdomData = makhdom.mapToMakhdomyData()
            ).onSuccess {
                makhdom.isDirty = false
                makhdom.isPictureUpdated = false
                makhdom.remotePictureUrl = it.picUrl
                updateMakhdomLocally(makhdom)
            }.onFailure {
                Log.d("updating remotely err:", it.message.toString())
            }
        } catch (e: Exception) {
            Log.d("updating remotely err:", e.message.toString())
        }
    }


    override fun readAll(): LiveData<List<Makhdom>> = makhdomLocalDataSource.readAll()

    override fun readById(id: Int): LiveData<Makhdom> = makhdomLocalDataSource.readById(id)

    override fun searchByKeyWord(keyWord: String): LiveData<List<Makhdom>> =
        makhdomLocalDataSource.searchByKeyWord(keyWord)

    override fun searchByPhone(phone: String): LiveData<List<Makhdom>> =
        makhdomLocalDataSource.searchByPhone(phone)


    override suspend fun addKhadem(khadem: Khadem): Result<String> =
        khademRemoteDataSource.addKhadem(khadem)

    override suspend fun getNotSynchMakhdommen(): List<Makhdom> =
        makhdomLocalDataSource.getUnSynchronizedMakhdommen()

    override suspend fun getDirtyMakhdommen(): List<Makhdom> =
        makhdomLocalDataSource.getDirtyMakhdommen()

    override suspend fun readKhadem(khademKey: String): Result<Khadem> {
        try {

        }
        catch (){

        }
        khademRemoteDataSource.readKhadem(khademKey).await()
    }
}