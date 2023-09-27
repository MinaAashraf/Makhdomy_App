package com.khedma.makhdomy.domain.usecases

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class ReadMakhdomByIdUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {
    fun execute(id: Int): LiveData<Makhdom> = makhdomRepository.readById(id)
}