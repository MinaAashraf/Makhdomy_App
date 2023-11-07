package com.khedma.makhdomy.data.remote

import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.SharedParameters

interface MakhdomRemoteDataSource {

   suspend fun addMakhdom(picture: ByteArray?, makhdomData: MakhdomData): Result<SharedParameters>
   suspend fun updateMakhdom(picture: ByteArray?, makhdomData: MakhdomData): Result<SharedParameters>

}