package com.khedma.makhdomy.presentation.makhdommen_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.khedma.makhdomy.R

class PhoneSelectionAdapter(
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
                inflater.inflate(R.layout.phone_selection_item, parent, false)
        }

        myView!!.findViewById<TextView>(R.id.phone_key).text = phones[position].first
        myView.setOnClickListener { onClickListener.onPhoneItemSelect(phones[position]) }
        return myView

    }

    interface OnClickListener {
        fun onPhoneItemSelect(pair: Pair<String, String>)
    }

}