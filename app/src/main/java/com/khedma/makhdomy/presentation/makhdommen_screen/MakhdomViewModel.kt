package com.khedma.makhdomy.presentation.makhdommen_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import com.khedma.makhdomy.domain.usecases.AddMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.ReadAllMakhdomeenUseCase
import com.khedma.makhdomy.domain.usecases.ReadMakhdomByIdUseCase
import com.khedma.makhdomy.domain.usecases.UpdateMakhdomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakhdomViewModel @Inject constructor(
    private val readAllMakhdomeenUseCase: ReadAllMakhdomeenUseCase,
    private val readMakhdomByIdUseCase: ReadMakhdomByIdUseCase,
    private val addMakhdomUseCase: AddMakhdomUseCase,
    private val updateMakhdomUseCase: UpdateMakhdomUseCase
) : ViewModel() {

    var preparedMakhdom: Makhdom = Makhdom()


    val makhdommen: LiveData<List<Makhdom>> = readAllMakhdomeenUseCase.execute()

    fun readMakhdomById(id: Int) : LiveData<Makhdom> = readMakhdomByIdUseCase.execute(id)


    fun addMakhdom() {
        viewModelScope.launch {
            addMakhdomUseCase.execute(preparedMakhdom)
        }
    }

    fun updateMakhdom(makhdom: Makhdom) {
        viewModelScope.launch {
            updateMakhdomUseCase.execute(makhdom)
        }
    }

    fun clearPreparedMakhdomData (){
        preparedMakhdom = Makhdom()
    }


}