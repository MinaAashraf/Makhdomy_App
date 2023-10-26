package com.khedma.makhdomy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Khadem(

    var name: String? = null,

    var className: String? = null,

    var classId: Int? = null,

    var phone: String? = null,

    var makhdomeenCount: Int = 0,

    var makhdomeenIds : List<String> = listOf(),

    var key: String? = null

) : Parcelable
