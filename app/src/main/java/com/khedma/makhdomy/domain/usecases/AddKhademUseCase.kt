package com.khedma.makhdomy.domain.usecases

import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class AddKhademUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {

    suspend fun execute(khadem: Khadem): Result<String> = makhdomRepository.addKhadem(khadem = khadem)


}