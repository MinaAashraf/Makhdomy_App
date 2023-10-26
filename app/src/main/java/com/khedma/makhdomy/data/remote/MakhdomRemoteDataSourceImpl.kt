package com.khedma.makhdomy.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.khedma.makhdomy.domain.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MakhdomRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MakhdomRemoteDataSource {

    override suspend fun addMakhdom(
        picture: ByteArray?,
        makhdomData: MakhdomData,
    ): Result<String> {

      return picture?.let {
            try {
                val storageTaskSnapshot =
                    storage.reference.child("servant_image/${makhdomData.name}").putBytes(picture)
                        .await()
                if (storageTaskSnapshot.task.isSuccessful) {
                    val uri = storageTaskSnapshot.storage.downloadUrl.await()
                    makhdomData.picture = uri?.toString()
                    val id = uploadMakhdomToDatabase(makhdomData)
                    Result.Success(id)
                } else {
                    Result.Failure(Exception("image not uploaded"))
                }
            } catch (e: Exception) {
                Result.Failure(Exception(e.message.toString()))
            }
        } ?: kotlin.run {
          return@run  try {
                val id = uploadMakhdomToDatabase(makhdomData)
                Result.Success(id)
            } catch (e: Exception) {
                Result.Failure(Exception(e.message.toString()))
            }

        }
    }

    private suspend fun uploadMakhdomToDatabase(makhdomData: MakhdomData): String {
        val reference = fireStore.collection("servant").document()
        reference.set(makhdomData).await()
        reference.update(mapOf("key" to reference.id))
        return reference.id
    }

}
