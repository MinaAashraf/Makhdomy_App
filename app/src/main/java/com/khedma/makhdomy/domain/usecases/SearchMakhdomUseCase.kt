package com.khedma.makhdomy.domain.usecases

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject


class SearchMakhdomUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {
    fun execute(keyWord: String): LiveData<List<Makhdom>> =
        makhdomRepository.searchByKeyWord(keyWord)
}