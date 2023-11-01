package com.khedma.makhdomy.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Khadem
import java.io.ByteArrayOutputStream


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}
fun View.makeInVisible() {
    this.visibility = View.INVISIBLE
}



fun Bitmap.convertToByteArr(): ByteArray {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    return bytes.toByteArray()
}

 fun validatePhoneNum(phoneField:TextInputLayout,context: Context): Boolean {
    var isValid = true
    phoneField.helperText = null
    val phoneNum = phoneField.editText!!.text.toString()
    if (phoneNum.isEmpty()) {
        phoneField.helperText = context.getString(R.string.phone_empty_error_msg)
        isValid = false
    } else if (phoneNum.length != 11) {
        phoneField.helperText = context.getString(R.string.phone_wrong_err_msg)
        isValid = false
    }
    return isValid
}


lateinit var preferences: SharedPreferences
fun createPreferences(context: Context) {
    preferences = context.getSharedPreferences(
        context.getString(R.string.preferences_file_key),
        Context.MODE_PRIVATE
    )
}


fun readFromPreferences(key: String) =
    preferences.getString(key, "")

fun writePreferences(key: String, value: String) =
    preferences.edit().putString(key, value).apply()

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG)
        .show()
}

fun fromJson(json: String): Khadem {
    return Gson().fromJson(json, object : TypeToken<Khadem>() {}.type)
}

fun toJson(khadem: Khadem): String = Gson().toJson(khadem)