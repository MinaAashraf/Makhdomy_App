package com.khedma.makhdomy.domain.usecases

import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class AddMakhdomUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {

    suspend fun execute(makhdom: Makhdom) = makhdomRepository.addMakhdom(makhdom)


}