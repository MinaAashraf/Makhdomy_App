package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentSpiritualMakhdomBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpiritualMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentSpiritualMakhdomBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.spirit_toolbar_title)
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
    }


    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            saveSpiritualData()
            findNavController().navigate(R.id.action_spiritualMakhdomFragment_to_mediaAndHobbiesMakhdomFragment)
        }
    }

    private fun saveSpiritualData (){
        val spiritualFatherName = binding.spiritualFatherField.editText!!.text.toString()
        val churchName = binding.spiritualFatherChurchField.editText!!.text.toString()
        viewModel.preparedMakhdom.apply {
            this.spiritualFatherName = spiritualFatherName
            this.churchName = churchName
        }
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
            saveSpiritualData()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }


}