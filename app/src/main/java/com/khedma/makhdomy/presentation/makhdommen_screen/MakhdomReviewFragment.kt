package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdomReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdomReviewFragment : Fragment() {

    private val binding by lazy { FragmentMakhdomReviewBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.confirmation_toolbar_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSaveBtn()
        setUpLastBtn()
    }

    private fun setUpSaveBtn() {
        binding.saveBtn.setOnClickListener {
            if (viewModel.updatingState && viewModel.preparedMakhdom.isSynchronized)
                viewModel.updateMakhdom()
            else
                viewModel.addMakhdom()
            findNavController().popBackStack(R.id.makhdommenListFragment, false)
            Log.d("makhdom : ", viewModel.preparedMakhdom.toString())
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}