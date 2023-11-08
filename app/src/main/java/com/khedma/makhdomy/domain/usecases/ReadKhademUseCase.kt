package com.khedma.makhdomy.domain.usecases

import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject

class ReadKhademUseCase @Inject constructor(private val makhdomRepository: MakhdomRepository) {
   suspend fun execute(khademKey:String): Result<Khadem> = makhdomRepository.readKhadem(khademKey)
}