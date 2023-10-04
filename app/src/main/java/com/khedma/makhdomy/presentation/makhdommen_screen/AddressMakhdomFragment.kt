package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentAddressMakhdomBinding
import com.khedma.makhdomy.domain.model.Address
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentAddressMakhdomBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.address = viewModel.preparedMakhdom.address
        setUpNextBtn()
        setUpLastBtn()
    }

    private fun setUpNextBtn() {

        binding.nextPageBtn.setOnClickListener {
            val homeNum = binding.homeNumField.editText!!.text.toString()
            val floorNum = binding.floorNumField.editText!!.text.toString()
            val apartmentNum = binding.apartmentNumField.editText!!.text.toString()
            val areaName = binding.areaNameField.editText!!.text.toString()
            val streetName = binding.streetNameField.editText!!.text.toString()
            val motfare3From = binding.motfare3StreetField.editText!!.text.toString()
            val anotherAddressData = binding.otherAddressDetailsField.editText!!.text.toString()

            val address = Address(
                homeNum = homeNum,
                floorNum = floorNum,
                apartmentNum = apartmentNum,
                areaName = areaName,
                streetName = streetName,
                motafre3From = motfare3From,
                anotherAddressData = anotherAddressData
            )

            viewModel.preparedMakhdom.address = address
            findNavController().navigate(R.id.action_addressMakhdomFragment_to_familyMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addressMakhdomFragment_to_basicDataMakhdomFragment)
        }
    }


}