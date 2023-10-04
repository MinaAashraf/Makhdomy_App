package com.khedma.makhdomy.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "makhdom_table")
data class Makhdom(
    // basic data
    var picture: Bitmap? = null,
    var name: String? = null,
    var birthDate: String? = null,
    var birthLocation: String? = null,
    // address data
    var address: Address? = null,
    // phone data
    var homePhoneNum: String? = null,
    var mobilePhones: MutableMap<String, String>? = null,
    // school data
    var schoolPhase: String? = null,
    var schoolName: String? = null,
    // family data
    var fatherJob: String? = null,
    var motherJob: String? = null,
    var brothers: List<Brother>? = null,

    var spiritualFatherName: String? = null,
    var churchName: String? = null,

    var hasComputer: Boolean? = null,
    var hasInternet: Boolean? = null,
    var computerDealing: String? = null,

    var hobbiesAndPrizes: String? = null,

    var healthProblems: String? = null,

    var childCharacterNotes: String? = null,

    )  {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
