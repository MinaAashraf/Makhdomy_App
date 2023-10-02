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
import com.khedma.makhdomy.databinding.FragmentHealthAndCharacterMakhdomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthAndCharacterMakhdomFragment : Fragment() {
    private val binding by lazy { FragmentHealthAndCharacterMakhdomBinding.inflate(layoutInflater) }
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

            val healthProblems = binding.healthProblemsField.editText!!.text.toString()
            val childCharachterNotes = binding.childNotesField.editText!!.text.toString()

            viewModel.preparedMakhdom.apply {
                this.healthProblems = healthProblems
                this.childCharacterNotes = childCharachterNotes
            }

            findNavController().navigate(R.id.action_healthAndCharacterMakhdomFragment_to_makhdomDetailsFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
           findNavController().navigate(R.id.action_healthAndCharacterMakhdomFragment_to_mediaAndHobbiesMakhdomFragment)
        }
    }


}