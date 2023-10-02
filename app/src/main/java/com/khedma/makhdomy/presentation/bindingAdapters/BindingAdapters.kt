package com.khedma.makhdomy.presentation.bindingAdapters

import android.graphics.Bitmap
import androidx.databinding.BindingAdapter
import com.khedma.makhdomy.R
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("img_src")
fun bindImg(imgView: CircleImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.setImageBitmap(it)
    } ?: run { imgView.setImageResource(R.drawable.profile_img) }
}
