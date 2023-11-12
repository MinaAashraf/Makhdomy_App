package com.khedma.makhdomy.presentation.bindingAdapters

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Brother
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show


@BindingAdapter("img_src")
fun bindImg(imgView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.setImageBitmap(it)
    } ?: run { imgView.setImageResource(R.drawable.profile_img) }
}

@BindingAdapter("img_src_dark")
fun bindDarkModeImg(imgView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.setImageBitmap(it)
    } ?: run { imgView.setImageResource(R.drawable.user) }
}

@BindingAdapter("address")
fun bindAddress(textView: TextView, address: Address?) {
    address?.let {
        textView.show()
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
    } ?: run { textView.hide() }
}

private fun concatenateDashIfRequired(addressString: String) =
    if (addressString.isNotEmpty()) " - " else ""

@BindingAdapter("has_computer")
fun bindComputerExistence(textView: TextView, hasComputer: Boolean = false) {
    textView.text = if (hasComputer) "نعم" else "لا"
}

@BindingAdapter("isEmpty")
fun bindTextVisibility(textView: TextView, text: String?) {
    textView.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("initial_visibility_depend_on_radio_btn")
fun bindComputerDealingInitialVisibilityDependingOnRadioBtn(
    inputLayout: TextInputLayout,
    hasComputer: Boolean = false
) {
     if (hasComputer)
        inputLayout.show()
     else
         inputLayout.hide()

}

@BindingAdapter("sync_icon")
fun bindSynchronizedIcon(imageView: ImageView, makhdom: Makhdom) {
    imageView.setImageResource(if (makhdom.isSynchronized && !makhdom.isDirty) R.drawable.sync_icon else R.drawable.notsync_icon)
}

@BindingAdapter("phones_map")
fun bindPhonesMap(textView: TextView, phones: MutableMap<String, String>?) {
    var phonesString = ""
    if (phones.isNullOrEmpty())
        textView.hide()
    else {
        phones.forEach {
            phonesString += "${it.key} : ${it.value}\n"
        }
        textView.text = phonesString
        textView.show()
    }
}

@BindingAdapter("brothers_list")
fun bindBrothersList(textView: TextView, brothers: List<Brother>?) {
    var brothersString = ""
    if (brothers.isNullOrEmpty())
        textView.hide()
    else {
        brothers.forEach { brothersString += "$it\n" }
        textView.text = brothersString
        textView.show()
    }
}

@BindingAdapter("class_id")
fun bindClassPhaseAutoCompleteField(
    textView: AutoCompleteTextView,
    classId: Int?,
    ) {
    classId?.let {
        if (it < 0)
            return
        val classIndex = classId - 1
        textView.setText(textView.context.resources.getStringArray(R.array.classes_names)[classIndex])
        textView.setAdapter(
            createClassNamesDropDownListAdapter(
                textView.context,
                textView.context.resources.getStringArray(R.array.classes_names)
            )
        )
    }

}

@BindingAdapter("life_level")
fun bindLifeLevelsAutoCompleteField(
    textView: AutoCompleteTextView,
    lifeLevel: String?,
) {
    lifeLevel?.let {
        if (it.isEmpty())
            return
        textView.setText(it)
        textView.setAdapter(
            createClassNamesDropDownListAdapter(
                textView.context,
                textView.context.resources.getStringArray(R.array.life_levels)
            )
        )
    }
}


private fun createClassNamesDropDownListAdapter(
    context: Context,
    list: Array<String>
): ArrayAdapter<String> {
    return ArrayAdapter(
        context,
        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
        list
    )
}






