package com.khedma.makhdomy.presentation.makhdommen_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.khedma.makhdomy.domain.model.Brother
import com.khedma.makhdomy.domain.model.FilterType
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.usecases.AddMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.GetNotSyncMakhdommenUseCase
import com.khedma.makhdomy.domain.usecases.ReadAllMakhdomeenUseCase
import com.khedma.makhdomy.domain.usecases.ReadMakhdomByIdUseCase
import com.khedma.makhdomy.domain.usecases.SearchMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.UpdateMakhdomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakhdomViewModel @Inject constructor(
    private val readAllMakhdomeenUseCase: ReadAllMakhdomeenUseCase,
    private val readMakhdomByIdUseCase: ReadMakhdomByIdUseCase,
    private val addMakhdomUseCase: AddMakhdomUseCase,
    private val updateMakhdomUseCase: UpdateMakhdomUseCase,
    private val searchMakhdomUseCase: SearchMakhdomUseCase,
    private val getNotSyncMakhdommenUseCase: GetNotSyncMakhdommenUseCase


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

    init {
        uploadUnSynchMakhdommen()
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


    fun addMakhdom(makhdom: Makhdom = preparedMakhdom) {
        viewModelScope.launch {
            async { addMakhdomUseCase.execute(makhdom)}.await()
            clearPreparedMakhdomData()
        }
    }

    fun updateMakhdom() {
        viewModelScope.launch {
           async {updateMakhdomUseCase.execute(preparedMakhdom)}.await()
            updatingState = false
            clearPreparedMakhdomData()
            Log.d("updatingState:", "${updatingState}")

        }
    }

    private fun clearPreparedMakhdomData() {
        preparedMakhdom = Makhdom()
        phones = null
        phonesList.clear()
        brothers = null
    }

    private fun uploadUnSynchMakhdommen() {
        viewModelScope.launch {
            val unSynchMakhdommen = getNotSyncMakhdommenUseCase.execute()
            unSynchMakhdommen.forEach {
                addMakhdom(it)
            }
        }
    }


     var phones: MutableMap<String, String>? = null

     val phonesList: MutableList<Pair<String, String>> = mutableListOf()

     var brothers: MutableList<Brother>? = null

}