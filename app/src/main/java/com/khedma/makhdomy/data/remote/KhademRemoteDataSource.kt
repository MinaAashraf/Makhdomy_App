package com.khedma.makhdomy.data.remote

import com.khedma.makhdomy.domain.Result
import com.khedma.makhdomy.domain.model.Khadem

interface KhademRemoteDataSource {

    suspend fun addKhadem(khadem: Khadem): Result<String>

    suspend fun addMakhdomIdToKhadem (makhdomKey:String)
}