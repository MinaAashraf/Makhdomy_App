package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMediaAndHobbiesMakhdomBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaAndHobbiesMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentMediaAndHobbiesMakhdomBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.media_toolbar_title)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.makhdom = viewModel.preparedMakhdom
        setUpUi()
    }

    private fun setUpUi() {
        setUpNextBtn()
        setUpLastBtn()
        handleSaveExitBtn()
        handleComputerDealingFieldVisibility()
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            saveMediaData()
            findNavController().navigate(R.id.action_mediaAndHobbiesMakhdomFragment_to_healthAndCharacterMakhdomFragment)
        }
    }


    private fun saveMediaData() {
        val hasComputerOrInternet = binding.positiveRadioBtn.isChecked
        val favouriteHobbiesAndPrizes = binding.hobbiesField.editText!!.text.toString()
        val computerDealing = binding.computerDealingField.editText!!.text.toString()
        viewModel.preparedMakhdom.apply {
            this.hasComputer = hasComputerOrInternet
            this.hobbiesAndPrizes = favouriteHobbiesAndPrizes
            this.computerDealing = computerDealing
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleComputerDealingFieldVisibility() {
        binding.positiveRadioBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            binding.computerDealingField.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun handleSaveExitBtn() {

        if (viewModel.updatingState)
            binding.saveExitBtn.show()
        else
            binding.saveExitBtn.hide()

        binding.saveExitBtn.setOnClickListener {
            saveMediaData()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }

}