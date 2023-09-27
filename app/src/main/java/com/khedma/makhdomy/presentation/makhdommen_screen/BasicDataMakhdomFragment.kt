package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentBasicDataMakhdomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicDataMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentBasicDataMakhdomBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNextBtn()
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            val name = binding.nameTextField.editText!!.text.toString()
            val date = binding.birthDateField.editText!!.text.toString()
            val birthLocation = binding.birthLocationField.editText!!.text.toString()
            viewModel.preparedMakhdom.apply {
                this.name = name
                this.birthDate = date
                this.birthLocation = birthLocation
            }
            findNavController().navigate(R.id.action_basicDataMakhdomFragment_to_addressMakhdomFragment2)
        }
    }


}