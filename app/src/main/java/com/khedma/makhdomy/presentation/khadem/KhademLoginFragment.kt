package com.khedma.makhdomy.presentation.khadem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentKhademLoginBinding
import com.khedma.makhdomy.databinding.FragmentKhademRegisterationBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.makeInVisible
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KhademLoginFragment : Fragment() {

    private val binding by lazy { FragmentKhademLoginBinding.inflate(layoutInflater) }
    val viewModel: KhademViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.login_label)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        observeOnLoading()
    }

    private fun setUpUi() {
        setUpLoginBtn()
        handleRegisterTextButton()
    }

    private fun handleRegisterTextButton() {
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_khademLoginFragment2_to_khademRegisterationFragment)
        }
    }


    private fun setUpLoginBtn() {
        binding.saveBtn.setOnClickListener {
            val email = binding.gmailField.editText!!.text.toString()
            val password = binding.passwordField.editText!!.text.toString()
            if (validateInputs(email, password))
                viewModel.signInWithEmailAndPassword(requireContext(), email, password)
        }
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
                        showToast(requireContext(), getString(R.string.wrong_email_or_password_err))
                    }
                }

            }
        }
    }

    private fun onSavingKhademSuccess() {
        showToast(requireContext(), getString(R.string.signed_in_successfully))
        findNavController().navigate(R.id.action_khademLoginFragment2_to_makhdommenListFragment)
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            binding.gmailField.helperText = getString(R.string.email_err_msg)
            isValid = false
        }

       else if (!email.contains("@gmail.com")) {
            binding.gmailField.helperText = getString(R.string.not_correct_email_msg)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordField.helperText = getString(R.string.password_err_msg)
            isValid = false
        }
        return isValid
    }

}