package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMediaAndHobbiesMakhdomBinding
import com.khedma.makhdomy.databinding.FragmentSpiritualMakhdomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaAndHobbiesMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentMediaAndHobbiesMakhdomBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.makhdom = viewModel.preparedMakhdom
        setUpNextBtn()
        setUpLastBtn()
        handleComputerDealingFieldVisibility()
    }

    private fun setUpNextBtn() {

        binding.nextPageBtn.setOnClickListener {
            val hasComputerOrInternet = binding.positiveRadioBtn.isActivated
            val favouriteHobbiesAndPrizes = binding.hobbiesField.editText!!.text.toString()

            viewModel.preparedMakhdom.apply {
                this.hasComputer = hasComputerOrInternet
                this.hobbiesAndPrizes = favouriteHobbiesAndPrizes
            }

            findNavController().navigate(R.id.action_mediaAndHobbiesMakhdomFragment_to_healthAndCharacterMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaAndHobbiesMakhdomFragment_to_spiritualMakhdomFragment)
        }
    }

    private fun handleComputerDealingFieldVisibility() {
        binding.positiveRadioBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            binding.computerDealingField.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }


}