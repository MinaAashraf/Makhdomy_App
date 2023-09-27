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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentFamilyMakhdomBinding.inflate(layoutInflater) }
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
            val fatherJob = binding.fatherJobField.editText!!.text.toString()
            val motherJob = binding.motherJobField.editText!!.text.toString()

            viewModel.preparedMakhdom.apply {
                this.fatherJob = fatherJob
                this.motherJob = motherJob
            }

            findNavController().navigate(R.id.action_familyMakhdomFragment_to_spiritualMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_familyMakhdomFragment_to_addressMakhdomFragment2)
        }
    }

}