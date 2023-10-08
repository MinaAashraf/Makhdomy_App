package com.khedma.makhdomy.presentation.bindingAdapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Address
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("img_src")
fun bindImg(imgView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.setImageBitmap(it)
    } ?: run { imgView.setImageResource(R.drawable.profile_img) }
}

@BindingAdapter("address")
fun bindAddress(textView: TextView, address: Address?) {
    address?.let {
        val streetName = it.streetName ?: ""
        val motfare3From = it.motafre3From?.let { " - $it" } ?: ""
        val area = it.areaName?.let { " - $it" } ?: ""
        val homeNum = it.homeNum?.let { " - " + "منزل " + it } ?: ""
        val apartmentNum = it.apartmentNum?.let { " - " + "شقة " + it } ?: ""
        val floorNum = it.homeNum?.let { " - " + "الدور رقم " + it } ?: ""
        val addressString = "$streetName$motfare3From$area$homeNum$apartmentNum$floorNum"
        textView.text = addressString
    } ?: run { textView.text = "لا يوجد" }
}

@BindingAdapter("has_computer")
fun bindComputerExistence(textView: TextView, hasComputer: Boolean = false) {
    textView.text = if (hasComputer) "نعم" else "لا"
}

@BindingAdapter ("initial_visibility_depend_on_radio_btn")
fun bindComputerDealingInitialVisibilityDependingOnRadioBtn (inputLayout: TextInputLayout , hasComputer: Boolean =false){
     inputLayout.visibility = if (hasComputer) View.VISIBLE else View.GONE
}



