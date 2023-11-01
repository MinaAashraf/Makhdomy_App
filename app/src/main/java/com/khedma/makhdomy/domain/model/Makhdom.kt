package com.khedma.makhdomy.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khedma.makhdomy.data.remote.MakhdomData

@Entity(tableName = "makhdom_table")
data class Makhdom(
    // basic data
    var picture: Bitmap? = null,
    var name: String? = null,
    var birthDate: String? = null,
    var birthLocation: String? = null,
    var className: String? = null,
    var classId : Int? = null,
    // address data
    var address: Address? = null,
    // these splitted address fields for supporting searching by address
    var addressArea: String? = null,
    var streetName: String? = null,
    var motafare3From: String? = null,

    // phone data
    var homePhoneNum: String? = null,
    var mobilePhones: MutableMap<String, String>? = null,
    // school data
    var schoolPhase: String? = null,
    var schoolName: String? = null,
    // family data
    var fatherJob: String? = null,
    var motherJob: String? = null,
    var brothers: MutableList<Brother>? = null,

    var spiritualFatherName: String? = null,
    var churchName: String? = null,

    var hasComputer: Boolean? = null,
    var hasInternet: Boolean? = null,
    var computerDealing: String? = null,

    var hobbiesAndPrizes: String? = null,

    var healthProblems: String? = null,

    var childCharacterNotes: String? = null,

    var khademName: String? = null,

    var isSynchronized : Boolean = false,

    var makhdomKey : String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var couldKey : String? = null

    fun mapToMakhdomyData(): MakhdomData {
        return MakhdomData(
            null,
            name,
            birthDate,
            birthLocation,
            className,
            classId,
            address,
            addressArea,
            streetName,
            motafare3From,
            homePhoneNum,
            mobilePhones,
            schoolPhase,
            schoolName,
            fatherJob,
            motherJob,
            brothers,
            spiritualFatherName,
            churchName,
            hasComputer,
            hasInternet,
            computerDealing,
            hobbiesAndPrizes,
            healthProblems,
            childCharacterNotes,
            khademName
        )
    }


}
