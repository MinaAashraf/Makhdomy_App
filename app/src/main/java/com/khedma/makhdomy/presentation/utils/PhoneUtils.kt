package com.khedma.makhdomy.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


fun makePhoneCall(activity: Activity, phone: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:${phone}")
    activity.startActivity(intent)
}

const val CALL_PHONE_DIALOG_TITLE = "الاتصال بالهاتف ؟"
const val DIALOG_POSITIVE_TEXT = "نعم"
const val DIALOG_NEGATIVE_TEXT = "لا"


fun getPhoneCallDialogMessage(name: String): String {
    return "هل تريد الاتصال ب $name"
}




fun checkPhoneCallPermission(context: Context) = ActivityCompat.checkSelfPermission(
    context,
    android.Manifest.permission.CALL_PHONE
) == PackageManager.PERMISSION_GRANTED

fun requestPermission(fragment: Fragment) {
    fragment.requestPermissions(
        arrayOf(android.Manifest.permission.CALL_PHONE),
        0
    )
}
