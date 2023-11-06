package com.khedma.makhdomy.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Brother
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    @TypeConverter
    fun fromBitmap(btm: Bitmap?): ByteArray? {
        return btm?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 70, outputStream)
            outputStream.toByteArray()
        }
    }

    @TypeConverter
    fun toHashMap(value: String): MutableMap<String, String>? =
        Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun fromHashMap(map: MutableMap<String, String>): String = Gson().toJson(map)

    @TypeConverter
    fun toListOfStrings(value: String): MutableList<String> =
        Gson().fromJson(value, object : TypeToken<MutableList<String>>() {})

    @TypeConverter
    fun fromListOfString(list: MutableList<String>): String = Gson().toJson(list)


    @TypeConverter
    fun toListOfBrothers(value: String): MutableList<Brother> =
        Gson().fromJson(value, object : TypeToken<MutableList<Brother>>() {})

    @TypeConverter
    fun fromListOfBrothers(list: MutableList<Brother>): String = Gson().toJson(list)

    @TypeConverter
    fun toAddressObj(value: String): Address =
        Gson().fromJson(value, object : TypeToken<Address>() {})

    @TypeConverter
    fun fromAddress(address: Address): String = Gson().toJson(address)


}