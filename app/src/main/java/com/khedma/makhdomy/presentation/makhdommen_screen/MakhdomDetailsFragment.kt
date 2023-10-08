package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdomDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdomDetailsFragment : Fragment() {
    private val binding by lazy { FragmentMakhdomDetailsBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()
    private val args: MakhdomDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.updatingState = false
        viewModel.clearPreparedMakhdomData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentMakhdomData()
        setUpUpdateBtn()
    }

    private fun presentMakhdomData() {
        viewModel.readMakhdomById(args.makhdomId).observe(requireActivity()) {
            it?.let {
                binding.makhdom = it
                binding.address = it.address
            }
        }
    }

    private fun setUpUpdateBtn() {
        binding.updateBtn.setOnClickListener {
            findNavController().navigate(
                MakhdomDetailsFragmentDirections.actionMakhdomDetailsFragmentToBasicDataMakhdomFragment(
                    args.makhdomId
                )
            )

        }
    }


}