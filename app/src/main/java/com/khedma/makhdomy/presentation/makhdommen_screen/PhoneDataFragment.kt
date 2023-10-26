package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentPhoneDataBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhoneDataFragment : Fragment() {

    private val binding by lazy { FragmentPhoneDataBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSavingPhoneBtn()
        setUpNextBtn()
        setUpLastBtn()
    }

    private fun setUpSavingPhoneBtn() {
        binding.savePhoneBtn.setOnClickListener {
            val phoneOwner = binding.phoneOwnerField.editText!!.text.toString()
            val phoneNum = binding.phoneField.editText!!.text.toString()
            if (!validatePhoneNum(phoneNum))
                return@setOnClickListener

            val phones = viewModel.preparedMakhdom.mobilePhones
            phones?.let {
                it[if (phoneOwner.isEmpty()) "موبايل ${phones.size + 1}" else phoneOwner] = phoneNum
            }
                ?: kotlin.run {
                    viewModel.preparedMakhdom.mobilePhones =
                        mutableMapOf<String, String>().apply { this[phoneOwner] = phoneNum }
                }
            clearFields()
            Snackbar.make(
                binding.root,
                getString(R.string.phone_added_successfully_msg),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }


    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_phoneDataFragment_to_familyMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validatePhoneNum(phoneNum: String): Boolean {
        var isValid = true
        if (phoneNum.isEmpty()) {
            binding.phoneField.editText!!.error = getString(R.string.phone_empty_error_msg)
            isValid = false
        } else if (phoneNum.length != 11 || (phoneNum.substring(
                0, 3) != "010" && phoneNum.substring(0, 3) != "011"
                    && phoneNum.substring(0, 3) != "015")
        ) {
            binding.phoneField.editText!!.error = getString(R.string.phone_wrong_err_msg)
            isValid = false
        }
        return isValid
    }

    private fun clearFields() {
        binding.phoneField.editText?.setText("")
        binding.phoneOwnerField.editText?.setText("")
    }

}