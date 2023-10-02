package com.khedma.makhdomy.presentation.makhdommen_screen

import androidx.recyclerview.widget.DiffUtil
import com.khedma.makhdomy.domain.model.Makhdom


class DiffCallback : DiffUtil.ItemCallback<Makhdom>() {
    override fun areItemsTheSame(oldItem: Makhdom, newItem: Makhdom): Boolean =
        oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Makhdom, newItem: Makhdom): Boolean = oldItem == newItem
}