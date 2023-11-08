package com.khedma.makhdomy.presentation.khadem

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.onFailure
import com.khedma.makhdomy.domain.onSuccess
import com.khedma.makhdomy.domain.usecases.AddKhademUseCase
import com.khedma.makhdomy.domain.usecases.ReadKhademUseCase
import com.khedma.makhdomy.presentation.utils.toJson
import com.khedma.makhdomy.presentation.utils.writePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class KhademViewModel @Inject constructor(
    private val addKhademUseCase: AddKhademUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val readKhademUseCase: ReadKhademUseCase
) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading: LiveData<Boolean?> = _loading

    var successFlag: Boolean? = null


    fun startLoading() {
        _loading.value = true
    }

    fun endLoading() {
        _loading.value = false
    }

    fun isCurrentUserAlreadyLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }


    fun authenticateWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        khadem: Khadem
    ) {
        _loading.value = true
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                khadem.key = firebaseAuth.currentUser?.uid
                addKhadem(context, khadem)
            }.addOnFailureListener {
                _loading.postValue(false)
                successFlag = false
                Log.d("auth err: ", it.message.toString())
            }
        } catch (e: FirebaseAuthException) {
            Log.d("auth err: ", e.message.toString())
            _loading.postValue(false)
            successFlag = false
        }

    }

    fun signInWithEmailAndPassword(context: Context, email: String, password: String) {
        _loading.value = true
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                readKhadem(context)
            }.addOnFailureListener {
                _loading.postValue(false)
                successFlag = false
                Log.d("auth err: ", it.message.toString())
            }
        } catch (e: FirebaseAuthException) {
            _loading.postValue(false)
            successFlag = false
            Log.d("auth err: ", e.message.toString())
        }

    }

    fun addKhadem(context: Context, khadem: Khadem) {
        viewModelScope.launch(Dispatchers.IO) {
            addKhademUseCase.execute(khadem).onSuccess {
                writePreferences(context, context.getString(R.string.khadem_key), toJson(khadem))
                successFlag = true
                _loading.postValue(false)
            }.onFailure {
                _loading.postValue(false)
                successFlag = false
            }
        }
    }

    private fun readKhadem(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            readKhademUseCase.execute(getCurrentUserKey()).onSuccess {
                writePreferences(context, context.getString(R.string.khadem_key), toJson(it))
                successFlag = true
                _loading.postValue(false)
            }
                .onFailure {
                    _loading.postValue(false)
                    successFlag = false
                }
        }
    }


    private fun getCurrentUserKey(): String {
        return firebaseAuth.currentUser!!.uid
    }

}