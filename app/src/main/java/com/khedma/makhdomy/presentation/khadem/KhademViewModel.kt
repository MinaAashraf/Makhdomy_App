package com.khedma.makhdomy.presentation.khadem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.onFailure
import com.khedma.makhdomy.domain.onSuccess
import com.khedma.makhdomy.domain.usecases.AddKhademUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KhademViewModel @Inject constructor(private val addKhademUseCase: AddKhademUseCase) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading: LiveData<Boolean?> = _loading

    var successFlag: Boolean? = null
    var resultKhademId: String? = null
    fun addKhadem(khadem: Khadem) {
        viewModelScope.launch(Dispatchers.IO) {
            addKhademUseCase.execute(khadem).onSuccess {
                _loading.postValue(false)
                successFlag = true
                resultKhademId = it

            }.onFailure {
                _loading.postValue(false)
                successFlag = false
            }
        }
    }

    fun startLoading (){
        _loading.value = true
    }

    fun endLoading (){
        _loading.value = false
    }



}