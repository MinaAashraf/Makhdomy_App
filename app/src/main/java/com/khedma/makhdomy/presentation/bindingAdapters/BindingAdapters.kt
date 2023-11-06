package com.khedma.makhdomy.presentation.bindingAdapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Brother
import com.khedma.makhdomy.domain.model.Makhdom


@BindingAdapter("img_src")
fun bindImg(imgView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.setImageBitmap(it)
    } ?: run { imgView.setImageResource(R.drawable.profile_img) }
}

@BindingAdapter("address")
fun bindAddress(textView: TextView, address: Address?) {
    address?.let {

        var addressString = ""

        val streetName = it.streetName ?: ""
        addressString += streetName
        val motfare3From =
            it.motafre3From?.let { concatenateDashIfRequired(addressString) + "متفرع من " + it }
                ?: ""
        addressString += motfare3From
        val area = it.areaName?.let { concatenateDashIfRequired(addressString) + it } ?: ""
        addressString += area
        val homeNum =
            it.homeNum?.let { concatenateDashIfRequired(addressString) + "منزل " + it } ?: ""
        addressString += homeNum
        val apartmentNum =
            it.apartmentNum?.let { concatenateDashIfRequired(addressString) + "شقة " + it } ?: ""
        addressString += apartmentNum
        val floorNum =
            it.floorNum?.let { concatenateDashIfRequired(addressString) + "الدور " + it } ?: ""
        addressString += floorNum
        //  val addressString = "$streetName$motfare3From$area$homeNum$apartmentNum$floorNum"
        textView.text = addressString
    } ?: run { textView.text = "لا يوجد" }
}

private fun concatenateDashIfRequired(addressString: String) =
    if (addressString.isNotEmpty()) " - " else ""

@BindingAdapter("has_computer")
fun bindComputerExistence(textView: TextView, hasComputer: Boolean = false) {
    textView.text = if (hasComputer) "نعم" else "لا"
}

@BindingAdapter("initial_visibility_depend_on_radio_btn")
fun bindComputerDealingInitialVisibilityDependingOnRadioBtn(
    inputLayout: TextInputLayout,
    hasComputer: Boolean = false
) {
    inputLayout.visibility = if (hasComputer) View.VISIBLE else View.GONE
}

@BindingAdapter("sync_icon")
fun bindSynchronizedIcon(imageView: ImageView, makhdom: Makhdom) {
    imageView.setImageResource(if (makhdom.isSynchronized && !makhdom.isDirty) R.drawable.sync_icon else R.drawable.notsync_icon)
}

@BindingAdapter("phones_map")
fun bindPhonesMap(textView: TextView, phones: MutableMap<String, String>?) {
    var phonesString = ""
    phones?.forEach {
        phonesString += "${it.key} : ${it.value}\n"
    }
    textView.text = phonesString
}

@BindingAdapter("brothers_list")
fun bindBrothersList(textView: TextView, brothers: List<Brother>?) {
    var brothersString = ""
    brothers?.forEach { brothersString += "$it\n" }
    textView.text = brothersString
}


