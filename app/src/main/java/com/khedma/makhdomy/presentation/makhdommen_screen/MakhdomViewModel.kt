package com.khedma.makhdomy.presentation.makhdommen_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.usecases.AddMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.ReadAllMakhdomeenUseCase
import com.khedma.makhdomy.domain.usecases.ReadMakhdomByIdUseCase
import com.khedma.makhdomy.domain.usecases.UpdateMakhdomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakhdomViewModel constructor(

) : ViewModel() {

    val preparedMakhdom: Makhdom = Makhdom()
/*

    val makhdommen: LiveData<List<Makhdom>> = readAllMakhdomeenUseCase.execute()

    fun readMakhdomById(id: Int) {
        readMakhdomByIdUseCase.execute(id)
    }

    fun addMakhdom(makhdom: Makhdom) {
        viewModelScope.launch {
            addMakhdomUseCase.execute(makhdom)
        }
    }

    fun updateMakhdom(makhdom: Makhdom) {
        viewModelScope.launch {
            updateMakhdomUseCase.execute(makhdom)
        }
    }
*/


}