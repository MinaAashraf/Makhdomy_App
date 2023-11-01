package com.khedma.makhdomy.presentation.makhdommen_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.khedma.makhdomy.R


class PhonesAdapter(
    context: Context,
    private val phones: List<Pair<String, String>>,
    private val onClickListener: OnClickListener
) :
    ArrayAdapter<Pair<String, String>>(context, 0, phones) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var myView = convertView

        if (myView == null) {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            myView =
                inflater.inflate(R.layout.phone_item, parent, false)
        }

        myView!!.findViewById<TextView>(R.id.phone_txt).text = phones[position].second
        myView.findViewById<ImageView>(R.id.close_icon).setOnClickListener {
            onClickListener.onRemoveClick(phones[position].first,position)
        }
        return myView

    }

    interface OnClickListener {
        fun onRemoveClick(key:String,position: Int)
    }

}