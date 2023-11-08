package com.khedma.makhdomy.domain.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.khedma.makhdomy.data.remote.MakhdomData
import dagger.hilt.android.qualifiers.ApplicationContext

@Entity(tableName = "makhdom_table")
data class Makhdom(
    // basic data
    var picture: Bitmap? = null,
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

    var isSynchronized: Boolean = false,
    var isDirty: Boolean = false,
    var isPictureUpdated: Boolean = false,
    var makhdomKey: String? = null,
    var remotePictureUrl: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    /*  var mobilePhones: MutableMap<String, String>? = null
          set(value) {
              field = value
              phonesList = value?.values?.toMutableList()
          }
      var phonesList: MutableList<String>? = null*/

    fun mapToMakhdomyData(): MakhdomData {
        return MakhdomData(
            remotePictureUrl,
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
            makhdomKey
        )
    }

    suspend fun getBitmapFromUrl(context: Context) {
        remotePictureUrl?.let {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(it).build()
            val drawable = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (drawable as BitmapDrawable).bitmap
            this.picture = bitmap

        }
    }
}
