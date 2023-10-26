package com.khedma.makhdomy.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KhademRemoteDataSourceImpl @Inject constructor(private val fireStore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) :
    KhademRemoteDataSource {
    override suspend fun addKhadem(khadem: Khadem): Result<String> {
        return try {
            val reference = fireStore.collection("server").document(khadem.key!!)
            reference.set(khadem).await()
            Result.Success(reference.id)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun addMakhdomIdToKhadem(makhdomKey: String) {
        val currentUser = firebaseAuth.currentUser
        val reference = fireStore.collection("server").document(currentUser!!.uid)
        reference.update("makhdomeenIds",FieldValue.arrayUnion(makhdomKey))
    }
}