package com.khedma.makhdomy.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.khedma.makhdomy.R
import com.khedma.makhdomy.data.local.MakhdomLocalDataSource
import com.khedma.makhdomy.data.remote.KhademRemoteDataSource
import com.khedma.makhdomy.data.remote.MakhdomData
import com.khedma.makhdomy.data.remote.MakhdomRemoteDataSource
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.onFailure
import com.khedma.makhdomy.domain.onSuccess
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import com.khedma.makhdomy.presentation.utils.convertToByteArr
import com.khedma.makhdomy.presentation.utils.fromJson
import com.khedma.makhdomy.presentation.utils.readFromPreferences
import com.khedma.makhdomy.presentation.utils.toJson
import com.khedma.makhdomy.presentation.utils.writePreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MakhdomRepositoryImpl @Inject constructor(
    private val makhdomLocalDataSource: MakhdomLocalDataSource,
    private val makhdomRemoteDataSource: MakhdomRemoteDataSource,
    private val khademRemoteDataSource: KhademRemoteDataSource,
    @ApplicationContext private val context: Context
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

    // add makhdom in room after receive it from remote
    override suspend fun addLocalMakhdom(makhdom: MakhdomData) {
        makhdomLocalDataSource.addMakhdom(makhdom.mapToLocalMakhdom())
    }

    override suspend fun readMakhdomenFromRemote(makhdommenKeys: List<String>): Result<String> {
        makhdomRemoteDataSource.readMakhdmenByKeys(makhdommenKeys).onSuccess {
            val makhdommen: List<Makhdom> = it.map { it.mapToLocalMakhdom() }
            makhdomLocalDataSource.clear()
            val idsList = makhdomLocalDataSource.addMakhdommen(makhdommen)
            updateBitmapPictures(makhdommen,idsList)
            return Result.Success("تم استقبال البيانات بنجاح")
        }.onFailure {
            return Result.Failure(Throwable("لا يوجد بيانات"))
        }
        return Result.Failure(Throwable("لا يوجد بيانات"))
    }

    private suspend fun updateBitmapPictures(makhdommen: List<Makhdom>, idsList: List<Long>) {
        makhdommen.forEachIndexed() {index,makhdom->
            makhdom.id = idsList[index].toInt()
            makhdom.getBitmapFromUrl(context)
            makhdomLocalDataSource.updateMakhdom(makhdom)
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
                addMakhdomIdToKhadem(sharedParameters.key)
            }.onFailure {
                Log.d("firebase exception: ", it.message.toString())
            }
        } catch (e: Exception) {
            Log.d("firebase exception: ", e.message.toString())
        }
    }

    private suspend fun addMakhdomIdToKhadem(makhdomKey: String) {
        khademRemoteDataSource.addMakhdomIdToKhadem(makhdomKey = makhdomKey)
        val currentKhadem =
            fromJson(readFromPreferences(context, context.getString(R.string.khadem_key))!!)
        currentKhadem.makhdomeenIds.add(makhdomKey)
        writePreferences(context, context.getString(R.string.khadem_key), toJson(currentKhadem))
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

    override suspend fun readKhadem(khademKey: String): Result<Khadem> =
        khademRemoteDataSource.readKhadem(khademKey)
}