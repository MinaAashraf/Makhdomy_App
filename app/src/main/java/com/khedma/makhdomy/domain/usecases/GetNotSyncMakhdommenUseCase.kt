package com.khedma.makhdomy.domain.usecases

import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class GetNotSyncMakhdommenUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {

    suspend fun execute() : List<Makhdom> = makhdomRepository.getNotSynchMakhdommen()

}