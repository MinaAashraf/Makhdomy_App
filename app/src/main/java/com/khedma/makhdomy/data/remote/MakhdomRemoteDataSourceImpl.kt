package com.khedma.makhdomy.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.khedma.makhdomy.data.utils.MAKHDOM_COLLECTION
import com.khedma.makhdomy.data.utils.SERVANT_IMAGE_FOLDER
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.SharedParameters
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MakhdomRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MakhdomRemoteDataSource {

    override suspend fun addMakhdom(
        picture: ByteArray?,
        makhdomData: MakhdomData,
    ): Result<SharedParameters> {

        return picture?.let {
            try {
                val storageTaskSnapshot =
                    storage.reference.child("$SERVANT_IMAGE_FOLDER${makhdomData.name}").putBytes(picture)
                        .await()
                if (storageTaskSnapshot.task.isSuccessful) {
                    val uri = storageTaskSnapshot.storage.downloadUrl.await()
                    makhdomData.picture = uri?.toString()
                    val key = uploadMakhdomToDatabase(makhdomData)
                    Result.Success(SharedParameters(key, uri.toString()))
                } else {
                    Result.Failure(Exception("image not uploaded"))
                }
            } catch (e: Exception) {
                Result.Failure(Exception(e.message.toString()))
            }
        } ?: kotlin.run {
            return@run try {
                val key = uploadMakhdomToDatabase(makhdomData)
                Result.Success(SharedParameters(key))
            } catch (e: Exception) {
                Result.Failure(Exception(e.message.toString()))
            }

        }
    }

    private suspend fun uploadMakhdomToDatabase(makhdomData: MakhdomData): String {
        val reference = fireStore.collection(MAKHDOM_COLLECTION).document()
        reference.set(makhdomData).await()
        reference.update(mapOf("key" to reference.id))
        return reference.id
    }


    override suspend fun updateMakhdom(
        picture: ByteArray?,
        makhdomData: MakhdomData
    ): Result<SharedParameters> {

        return picture?.let {
            try {
                val storageTaskSnapshot =
                    storage.reference.child("$SERVANT_IMAGE_FOLDER${makhdomData.name}").putBytes(picture)
                        .await()

                if (storageTaskSnapshot.task.isSuccessful) {
                    val uri = storageTaskSnapshot.storage.downloadUrl.await()
                    makhdomData.picture = uri?.toString()

                    val reference = fireStore.collection(MAKHDOM_COLLECTION).document(makhdomData.key!!)
                    reference.set(makhdomData).await()
                    Result.Success(SharedParameters(makhdomData.key!!, uri.toString()))

                } else {
                    Result.Failure(Exception("image not uploaded"))
                }
            } catch (e: Exception) {
                Result.Failure(e)
            }
        } ?: kotlin.run {
            return@run try {
                val reference = fireStore.collection(MAKHDOM_COLLECTION).document(makhdomData.key!!)
                reference.set(makhdomData).await()
                Result.Success(SharedParameters(makhdomData.key!!))
            } catch (e: Exception) {
                Result.Failure(e)
            }

        }
    }
}
