package com.khedma.makhdomy.presentation.makhdommen_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.khedma.makhdomy.R
import com.khedma.makhdomy.domain.model.Brother


class BrothersAdapter(
    context: Context,
    private val brothers: List<Brother>,
    private val onClickListener: OnClickListener
) :
    ArrayAdapter<Brother>(context, 0, brothers) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var myView = convertView

        if (myView == null) {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            myView =
                inflater.inflate(R.layout.phone_item, parent, false)
        }

        myView!!.findViewById<TextView>(R.id.phone_txt).text = brothers[position].name
        myView.findViewById<ImageView>(R.id.close_icon).setOnClickListener {
            onClickListener.onRemoveClick(position)
        }
        return myView
    }

    interface OnClickListener {
        fun onRemoveClick(position: Int)
    }

}