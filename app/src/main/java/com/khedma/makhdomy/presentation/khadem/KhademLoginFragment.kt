package com.khedma.makhdomy.presentation.khadem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentKhademLoginBinding
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.presentation.utils.createPreferences
import com.khedma.makhdomy.presentation.utils.toJson
import com.khedma.makhdomy.presentation.utils.validatePhoneNum
import com.khedma.makhdomy.presentation.utils.writePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KhademLoginFragment : Fragment() {

    private val binding by lazy { FragmentKhademLoginBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }


    private fun setUpUi() {
        setUpClassNameDropDownList()
        handleSaveBtn()
    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener {
            clearFieldsErrors()
            if (!isInputsValid())
                return@setOnClickListener
            else {
                constructKhadem()
                findNavController().navigate(
                    KhademLoginFragmentDirections.actionKhademLoginFragmentToPhoneVerificationFragment()
                )
            }
        }
    }

    private fun clearFieldsErrors() {
        binding.apply {
            khademNameField.helperText = null
            phoneField.helperText = null
            classNameField.helperText = null
        }
    }

    private fun setUpClassNameDropDownList() {
        val dropDownAdapter = ArrayAdapter(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.classes_names)
        )
        binding.classNameAutoComplete.setAdapter(dropDownAdapter)
    }


    private fun constructKhadem() {
        val name = binding.khademNameField.editText!!.text.toString()
        val className = binding.classNameField.editText!!.text.toString()
        val classId = resources.getStringArray(R.array.classes_names).indexOf(className) + 1
        val phone = binding.phoneField.editText!!.text.toString()

        val currentKhadem = Khadem(
            name = name,
            className = className,
            classId = classId,
            makhdomeenCount = 0,
            phone = phone,
        )
        createPreferences(requireContext())
        writePreferences(getString(R.string.khadem_key), toJson(currentKhadem))
    }

    private fun isInputsValid(): Boolean {
        var isValid = true
        if (binding.khademNameField.editText!!.text.isEmpty()) {
            binding.khademNameField.helperText = "يجب ادخال اسم الخادم!"
            isValid = false
        }
        if (!validatePhoneNum(binding.phoneField, requireContext()))
            isValid = false

        if (binding.classNameField.editText!!.text.isEmpty()) {
            binding.classNameField.helperText = "يجب ادخال المرحلة!"
            isValid = false
        }
        return isValid
    }

}