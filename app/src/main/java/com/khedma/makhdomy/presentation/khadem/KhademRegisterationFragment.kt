package com.khedma.makhdomy.presentation.khadem

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.MainActivity
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentKhademRegisterationBinding
import com.khedma.makhdomy.domain.model.Khadem
import com.khedma.makhdomy.presentation.utils.KHADEM_CODE
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.makeInVisible
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KhademRegisterationFragment : Fragment() {

    private val binding by lazy { FragmentKhademRegisterationBinding.inflate(layoutInflater) }
    val viewModel: KhademViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar?.show()
        requireActivity().title = getString(R.string.new_account_label)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        observeOnLoading()
    }


    private fun setUpUi() {
        setUpClassNameDropDownList()
        handleSaveBtn()
        handleLonginTextButton()
    }

    private fun handleLonginTextButton() {
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_khademRegisterationFragment_to_khademLoginFragment2)
        }
    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener {
            clearFieldsErrors()
            if (!isInputsValid())
                return@setOnClickListener
            else {
                val khadem = constructKhadem()
                val email = binding.gmailField.editText!!.text.toString()
                val password = binding.passwordField.editText!!.text.toString()
                viewModel.authenticateWithEmailAndPassword(
                    requireContext(),
                    email,
                    password,
                    khadem
                )
            }
        }
    }

    private fun clearFieldsErrors() {
        binding.apply {
            khademNameField.helperText = null
            gmailField.helperText = null
            passwordField.helperText = null
            confirmationPasswordField.helperText = null
            khademCodeField.helperText = null
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


    private fun constructKhadem(): Khadem {
        val name = binding.khademNameField.editText!!.text.toString()
        val className = binding.classNameField.editText!!.text.toString()
        val classId = resources.getStringArray(R.array.classes_names).indexOf(className) + 1

        return Khadem(
            name = name,
            className = className,
            classId = classId,
            makhdomeenCount = 0,
        )
    }

    private fun observeOnLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    binding.saveBtn.makeInVisible()
                    binding.progressBar.show()
                } else {
                    if (viewModel.successFlag != null && viewModel.successFlag == true)
                        onSavingKhademSuccess()
                    else {
                        binding.saveBtn.show()
                        binding.progressBar.hide()
                        showToast(requireContext(), getString(R.string.auth_err_msg))
                    }
                }

            }
        }
    }

    private fun isInputsValid(): Boolean {
        var isValid = true
        val password = binding.passwordField.editText!!.text.toString()
        val confirmationPassword = binding.confirmationPasswordField.editText!!.text.toString()

        if (binding.khademNameField.editText!!.text.isEmpty()) {
            binding.khademNameField.helperText = "يجب ادخال اسم الخادم!"
            isValid = false
        }

        if (binding.gmailField.editText!!.text.isEmpty()) {
            binding.gmailField.helperText = getString(R.string.email_err_msg)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.gmailField.editText!!.text ).matches()) {
            binding.gmailField.helperText = getString(R.string.not_correct_email_msg)
            isValid = false
        }



        if (password.isEmpty()) {
            binding.passwordField.helperText = getString(R.string.password_err_msg)
            isValid = false
        } else if (password.length < 8) {
            binding.passwordField.helperText = "لا يجب ان يقل عن 8 ارقام او حروف!"
            isValid = false
        }


        if (confirmationPassword.isEmpty()) {
            binding.confirmationPasswordField.helperText = "يجب تأكيد الرقم السري!"
            isValid = false
        }


        if (password != confirmationPassword) {
            binding.confirmationPasswordField.helperText = "يجب ان يكون مطابقاً للرقم السري!"
            isValid = false
        }

        val khademCode = binding.khademCodeField.editText!!.text.toString()
        if (khademCode.isEmpty()) {
            binding.khademCodeField.helperText = "يجب ادخال كود الخادم!"
            isValid = false
        } else if (khademCode != KHADEM_CODE) {
            binding.khademCodeField.helperText = "هذا الكود غير صحيح!"
            isValid = false
        }

        if (binding.classNameField.editText!!.text.isEmpty()) {
            binding.classNameField.helperText = "يجب ادخال المرحلة!"
            isValid = false
        }
        return isValid
    }

    private fun onSavingKhademSuccess() {
        showToast(requireContext(), getString(R.string.signed_in_successfully))
        findNavController().navigate(R.id.action_khademRegisterationFragment_to_makhdommenListFragment)
    }

}