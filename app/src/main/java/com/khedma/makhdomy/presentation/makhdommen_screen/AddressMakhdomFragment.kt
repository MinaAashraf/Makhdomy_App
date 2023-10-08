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
import com.khedma.makhdomy.presentation.hide
import com.khedma.makhdomy.presentation.show
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
        setUpUi()
    }

    private fun setUpUi() {
        setUpNextBtn()
        setUpLastBtn()
        handleSaveExitBtn()
    }

    private fun setUpNextBtn() {

        binding.nextPageBtn.setOnClickListener {
            saveAddressData()
            findNavController().navigate(R.id.action_addressMakhdomFragment_to_phoneDataFragment)
        }
    }

    private fun saveAddressData() {
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
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun handleSaveExitBtn() {
        if (viewModel.updatingState)
            binding.saveExitBtn.show()
        else
            binding.saveExitBtn.hide()

        binding.saveExitBtn.setOnClickListener {
            saveAddressData()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }

}
