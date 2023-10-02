package com.khedma.makhdomy.presentation.makhdommen_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khedma.makhdomy.databinding.MakhdomItemBinding
import com.khedma.makhdomy.domain.model.Makhdom

class MakhdommenAdapter(private val onItemClick: OnItemClick) :
    ListAdapter<Makhdom, MakhdommenAdapter.MyViewHolder>(DiffCallback()) {

    class MyViewHolder(private val binding: MakhdomItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(makhdom: Makhdom) {
            binding.makhdom = makhdom
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MakhdomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = MyViewHolder(binding)

        binding.root.setOnClickListener {
            val makhdom = getItem(holder.adapterPosition)
            onItemClick.onItemClick(makhdom.id!!)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    interface OnItemClick {
        fun onItemClick(id:Int)
    }
}