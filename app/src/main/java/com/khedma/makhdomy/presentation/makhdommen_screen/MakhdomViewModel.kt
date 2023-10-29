package com.khedma.makhdomy.presentation.makhdommen_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.khedma.makhdomy.domain.model.FilterType
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.usecases.AddMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.ReadAllMakhdomeenUseCase
import com.khedma.makhdomy.domain.usecases.ReadMakhdomByIdUseCase
import com.khedma.makhdomy.domain.usecases.SearchMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.UpdateMakhdomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakhdomViewModel @Inject constructor(
    private val readAllMakhdomeenUseCase: ReadAllMakhdomeenUseCase,
    private val readMakhdomByIdUseCase: ReadMakhdomByIdUseCase,
    private val addMakhdomUseCase: AddMakhdomUseCase,
    private val updateMakhdomUseCase: UpdateMakhdomUseCase,
    private val searchMakhdomUseCase: SearchMakhdomUseCase
) : ViewModel() {

    var preparedMakhdom: Makhdom = Makhdom()

    var updatingState: Boolean = false

    private val filterType: MutableLiveData<FilterType> = MutableLiveData(FilterType.ALL)

    private var searchKeyWord: String? = null

    val makhdommen: LiveData<List<Makhdom>> = filterType.switchMap {
        return@switchMap when (it) {
            FilterType.SEARCH -> searchMakhdomUseCase.execute(searchKeyWord!!)
            else -> readAllMakhdomeenUseCase.execute()
            //  notDataExists.value = books.value?.isEmpty() ?: true

        }
    }

    fun search(searchKeyWord: String) {
        if (searchKeyWord.isNotEmpty()) {
            this.searchKeyWord = searchKeyWord
            this.filterType.value = FilterType.SEARCH
        } else {
            this.filterType.value = FilterType.ALL
            this.searchKeyWord = null
        }
    }

    fun readMakhdomById(id: Int): LiveData<Makhdom> = readMakhdomByIdUseCase.execute(id)


    fun addMakhdom() {
        viewModelScope.launch(Dispatchers.IO) {
            addMakhdomUseCase.execute(preparedMakhdom)
            clearPreparedMakhdomData()
        }
    }

    fun updateMakhdom() {
        viewModelScope.launch(Dispatchers.IO) {
            updateMakhdomUseCase.execute(preparedMakhdom)
            updatingState = false
            clearPreparedMakhdomData()
            Log.d("updatingState:", "${updatingState}")

        }
    }

    private fun clearPreparedMakhdomData() {
        preparedMakhdom = Makhdom()
    }

    fun uploadMakhdommen() {

    }


}