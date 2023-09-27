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
import com.khedma.makhdomy.databinding.FragmentFamilyMakhdomBinding
import com.khedma.makhdomy.databinding.FragmentSpiritualMakhdomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpiritualMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentSpiritualMakhdomBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNextBtn()
        setUpLastBtn()
    }

    private fun setUpNextBtn() {

        binding.nextPageBtn.setOnClickListener {
            val spiritualFatherName = binding.spiritualFatherField.editText!!.text.toString()
            val churchName = binding.spiritualFatherChurchField.editText!!.text.toString()

            viewModel.preparedMakhdom.apply {
                this.spiritualFatherName = spiritualFatherName
                this.churchName = churchName
            }

            findNavController().navigate(R.id.action_spiritualMakhdomFragment_to_mediaAndHobbiesMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_spiritualMakhdomFragment_to_familyMakhdomFragment)
        }
    }


}