package com.khedma.makhdomy.domain.usecases

import androidx.lifecycle.LiveData
import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import javax.inject.Inject


class ReceiveMakhdommenFromRemote @Inject constructor(private val makhdomRepository: MakhdomRepository) {
    suspend fun execute(keys: List<String>): Result<String> =
        makhdomRepository.readMakhdomenFromRemote(keys)
}