package com.khedma.makhdomy.data.remote

import com.khedma.makhdomy.domain.Result

interface MakhdomRemoteDataSource {

   suspend fun addMakhdom(picture: ByteArray?, makhdomData: MakhdomData): Result<String>

}