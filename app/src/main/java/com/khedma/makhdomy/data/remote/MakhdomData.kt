package com.khedma.makhdomy.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Brother
import com.khedma.makhdomy.domain.model.Makhdom
import dagger.hilt.android.qualifiers.ApplicationContext

data class MakhdomData(
    // basic data
    var picture: String? = null,
    var name: String? = null,
    var birthDate: String? = null,
    var birthLocation: String? = null,
    var className: String? = null,
    var classId: Int? = null,
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
    var familyLifeLevel: String? = null,
    var familyLastVisitAttitude: String? = null,
    var familyChurchConnection: String? = null,
    var familyNotes: String? = null,
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

    var key: String? = null,

    ) {
    fun mapToLocalMakhdom(): Makhdom {
        return Makhdom(
            picture = null,
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
            familyLifeLevel,
            familyLastVisitAttitude,
            familyChurchConnection,
            familyNotes,
            brothers,
            spiritualFatherName,
            churchName,
            hasComputer,
            hasInternet,
            computerDealing,
            hobbiesAndPrizes,
            healthProblems,
            childCharacterNotes,
            khademName,
            true,
            false,
            false,
            key,
            remotePictureUrl = picture
        )
    }


}



