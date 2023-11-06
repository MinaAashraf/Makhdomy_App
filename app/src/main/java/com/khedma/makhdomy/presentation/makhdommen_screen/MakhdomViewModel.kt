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
import com.khedma.makhdomy.domain.usecases.GetDirtyMakhdommenUseCase
import com.khedma.makhdomy.domain.usecases.GetNotSyncMakhdommenUseCase
import com.khedma.makhdomy.domain.usecases.ReadAllMakhdomeenUseCase
import com.khedma.makhdomy.domain.usecases.ReadMakhdomByIdUseCase
import com.khedma.makhdomy.domain.usecases.SearchMakhdomUseCase
import com.khedma.makhdomy.domain.usecases.SearchUsingPhoneUseCase
import com.khedma.makhdomy.domain.usecases.UpdateMakhdomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakhdomViewModel @Inject constructor(
    private val readAllMakhdomeenUseCase: ReadAllMakhdomeenUseCase,
    private val readMakhdomByIdUseCase: ReadMakhdomByIdUseCase,
    private val addMakhdomUseCase: AddMakhdomUseCase,
    private val updateMakhdomUseCase: UpdateMakhdomUseCase,
    private val searchMakhdomUseCase: SearchMakhdomUseCase,
    private val getNotSyncMakhdommenUseCase: GetNotSyncMakhdommenUseCase,
    private val getDirtyMakhdommenUseCase: GetDirtyMakhdommenUseCase,
    private val searchUsingPhoneUseCase: SearchUsingPhoneUseCase

) : ViewModel() {

    var preparedMakhdom: Makhdom = Makhdom()

    var updatingState: Boolean = false

    private val filterType: MutableLiveData<FilterType> = MutableLiveData(FilterType.ALL)

    private var searchKeyWord: String? = null

    val makhdommen: LiveData<List<Makhdom>> = filterType.switchMap {
        return@switchMap when (it) {
            FilterType.GENERAL_SEARCH -> searchMakhdomUseCase.execute(searchKeyWord!!)
            FilterType.PHONE_SEARCH -> searchUsingPhoneUseCase.execute(searchKeyWord!!)
            else -> readAllMakhdomeenUseCase.execute()
        }
    }

    init {
        uploadUnSynchMakhdommen()
        updateDirtyMakhdommenRemotely()
    }

    fun search(searchKeyWord: String) {
        if (searchKeyWord.isNotEmpty()) {
            this.searchKeyWord = searchKeyWord
            if (isNumeric(searchKeyWord)) {
                this.filterType.value = FilterType.PHONE_SEARCH
                Log.d("filterType: ", "phone")
            } else {
                this.filterType.value = FilterType.GENERAL_SEARCH
                Log.d("filterType: ", "general")
            }
        } else {
            this.filterType.value = FilterType.ALL
            this.searchKeyWord = null
            Log.d("filterType: ", "null")
        }
    }

    fun readMakhdomById(id: Int): LiveData<Makhdom> = readMakhdomByIdUseCase.execute(id)


    fun addMakhdom(makhdom: Makhdom = preparedMakhdom) {
        viewModelScope.launch {
            addMakhdomUseCase.execute(makhdom)
            clearPreparedMakhdomData()
        }
    }

    fun updateMakhdom() {
        viewModelScope.launch {
            if (preparedMakhdom.isSynchronized)
                preparedMakhdom.isDirty = true
            updateMakhdomUseCase.execute(preparedMakhdom)
            updatingState = false
            clearPreparedMakhdomData()
            Log.d("updatingState:", "${updatingState}")

        }
    }

    fun saveBrothersList (){
        if (preparedMakhdom.brothers == null)
             preparedMakhdom.brothers = mutableListOf()
        preparedMakhdom.brothers!!.clear()
        preparedMakhdom.brothers!!.addAll(brothers)
    }


    fun clearPreparedMakhdomData() {
        preparedMakhdom = Makhdom()
        phones = null
        phonesList.clear()
        brothers.clear()
    }

    private fun uploadUnSynchMakhdommen() {
        viewModelScope.launch {
            val unSynchMakhdommen = getNotSyncMakhdommenUseCase.execute()
            unSynchMakhdommen.forEach {
                addMakhdom(it)
            }
        }
    }

    private fun updateDirtyMakhdommenRemotely() {
        viewModelScope.launch {
            val dirtyMakhdommen = getDirtyMakhdommenUseCase.execute()
            dirtyMakhdommen.forEach {
                updateEachDirtyMakhdomRemotely(it)
            }
        }
    }

    private fun updateEachDirtyMakhdomRemotely(makhdom: Makhdom) {
        viewModelScope.launch {
            updateMakhdomUseCase.execute(makhdom)
        }
    }

    var phones: MutableMap<String, String>? = null

    val phonesList: MutableList<Pair<String, String>> = mutableListOf()

    var brothers: MutableList<Brother> = mutableListOf()

    private fun isNumeric(input: String): Boolean {
        return input.matches(Regex("\\d+"))
    }
}