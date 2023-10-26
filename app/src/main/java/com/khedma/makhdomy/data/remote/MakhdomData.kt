package com.khedma.makhdomy.data.remote

import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Brother

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
    var brothers: List<Brother>? = null,

    var spiritualFatherName: String? = null,
    var churchName: String? = null,

    var hasComputer: Boolean? = null,
    var hasInternet: Boolean? = null,
    var computerDealing: String? = null,

    var hobbiesAndPrizes: String? = null,

    var healthProblems: String? = null,

    var childCharacterNotes: String? = null,

    var khademName : String? = null,

    var key: String? = null


)
