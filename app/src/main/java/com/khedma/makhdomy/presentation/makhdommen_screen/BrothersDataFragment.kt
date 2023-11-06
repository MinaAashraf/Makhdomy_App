package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentBrothersDataBinding
import com.khedma.makhdomy.domain.model.Brother
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast


class BrothersDataFragment : Fragment(), BrothersAdapter.OnClickListener {
    private val binding: FragmentBrothersDataBinding by lazy {
        FragmentBrothersDataBinding.inflate(
            layoutInflater
        )
    }


    private val viewModel: MakhdomViewModel by activityViewModels()


    private val brothersAdapter by lazy {
        BrothersAdapter(
            requireContext(),
            viewModel.brothers,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.brother_toolbar_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    fun setUpUi() {
        setUpSaveBrotherBtn()
        setUpGridView()
        setUpClassNameDropDownList()
        setUpNextBtn()
        setUpLastBtn()
        handleSaveExitBtn()
    }

    private fun setUpSaveBrotherBtn() {
        binding.saveBrotherBtn.setOnClickListener {
            binding.nameField.helperText = null
            val brotherName = binding.nameField.editText!!.text.toString()
            val brotherType = binding.typeField.editText!!.text.toString()
            val brotherAge = binding.ageField.editText!!.text.toString()
            val brotherStudy = binding.studyField.editText!!.text.toString()

            if (brotherName.isEmpty()) {
                binding.nameField.helperText = getString(R.string.brother_name_err_msg)
                return@setOnClickListener
            }
            val brother = Brother(brotherName, brotherType, brotherAge, brotherStudy)
            viewModel.brothers.add(brother)
            brothersAdapter.notifyDataSetChanged()
            clearFields()
            showToast(requireContext(), getString(R.string.brother_data_addess_succssfully_msg))
        }
    }

    private fun setUpGridView() {
        viewModel.preparedMakhdom.brothers?.let {
            if (viewModel.brothers.isEmpty())
                viewModel.brothers.addAll(it)
        }
        binding.brothersGrid.isExpanded = true
        binding.brothersGrid.adapter = brothersAdapter
    }

    private fun clearFields() {
        binding.nameField.editText?.setText("")
        binding.typeField.editText?.setText("")
        binding.ageField.editText?.setText("")
        binding.studyField.editText?.setText("")
    }

    override fun onRemoveClick(position: Int) {
        viewModel.brothers.removeAt(position)
        brothersAdapter.notifyDataSetChanged()
    }

    private fun setUpClassNameDropDownList() {
        val dropDownAdapter = ArrayAdapter(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.brother_types)
        )
        binding.typeAutoComplete.setAdapter(dropDownAdapter)
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            viewModel.saveBrothersList()
            findNavController().navigate(R.id.action_brothersDataFragment_to_spiritualMakhdomFragment)
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
            viewModel.saveBrothersList()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }

}