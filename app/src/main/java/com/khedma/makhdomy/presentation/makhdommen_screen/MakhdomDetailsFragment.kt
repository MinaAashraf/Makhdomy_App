package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdomDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdomDetailsFragment : Fragment() {

    private val binding by lazy { FragmentMakhdomDetailsBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSaveBtn()
        setUpLastBtn()
    }

    private fun setUpSaveBtn() {

        binding.saveBtn.setOnClickListener {
            Log.d("makhdom details: ", viewModel.preparedMakhdom.toString())
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_makhdomDetailsFragment_to_healthAndCharacterMakhdomFragment)
        }
    }


}