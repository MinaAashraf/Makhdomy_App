package com.khedma.makhdomy.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.khedma.makhdomy.R
import com.khedma.makhdomy.data.utils.KHADEM_COLLECTION
import com.khedma.makhdomy.data.utils.MAKHDOMS_IDS_FIELD
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.presentation.utils.toJson
import com.khedma.makhdomy.presentation.utils.writePreferences
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KhademRemoteDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) :
    KhademRemoteDataSource {
    override suspend fun addKhadem(khadem: Khadem): Result<String> {
        return try {
            val reference = fireStore.collection(KHADEM_COLLECTION).document(khadem.key!!)
            reference.set(khadem).await()
            Result.Success(reference.id)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun addMakhdomIdToKhadem(makhdomKey: String) {
        val currentUser = firebaseAuth.currentUser
        val reference = fireStore.collection(KHADEM_COLLECTION).document(currentUser!!.uid)
        reference.update(MAKHDOMS_IDS_FIELD, FieldValue.arrayUnion(makhdomKey))

    }

    override suspend fun readKhadem(khademKey: String): Result<Khadem> {
       return try {
            val documentSnapShotTask =
                fireStore.collection(KHADEM_COLLECTION).document(khademKey).get().await()
            if (documentSnapShotTask != null && !documentSnapShotTask.data.isNullOrEmpty())
                Result.Success(documentSnapShotTask.toObject(Khadem::class.java)!!)
           else
                Result.Failure(Throwable("Data is not available!"))

        } catch (e : Exception) {
           Result.Failure(e)
        }
    }


}