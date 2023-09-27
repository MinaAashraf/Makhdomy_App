package com.khedma.makhdomy.domain.usecases

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class ReadAllMakhdomeenUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {

    fun execute(): LiveData<List<Makhdom>> = makhdomRepository.readAll()


}