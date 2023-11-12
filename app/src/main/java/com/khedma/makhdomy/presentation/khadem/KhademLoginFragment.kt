package com.khedma.makhdomy.presentation.khadem

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentKhademLoginBinding
import com.khedma.makhdomy.databinding.PasswordResetDialogBinding
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
        setUpForgetPasswordButton()
        setUpPasswordRestDialog()
    }

    private fun handleRegisterTextButton() {
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_khademLoginFragment2_to_khademRegisterationFragment)
        }
    }


    private fun setUpLoginBtn() {
        binding.saveBtn.setOnClickListener {
            binding.gmailField.helperText = ""
            binding.passwordField.helperText = ""
            val email = binding.gmailField.editText!!.text.toString()
            val password = binding.passwordField.editText!!.text.toString()
            if (validateInputs(email, password))
                viewModel.signInWithEmailAndPassword(requireContext(), email, password)
        }
    }

    private val passwordResetBinding: PasswordResetDialogBinding by lazy {
        PasswordResetDialogBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var passwordResetDialog: Dialog

    private fun setUpPasswordRestDialog (){
        passwordResetDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(passwordResetBinding.root)
        }
        passwordResetBinding.sendEmailBtn.setOnClickListener {
            setUpSendResetEmailDialogBtn()
        }
        passwordResetBinding.cancelBtn.setOnClickListener {
            passwordResetDialog.dismiss()
        }
    }
    private fun showResetPasswordDialog() {
       passwordResetDialog.show()
    }


    private fun setUpSendResetEmailDialogBtn() {
        val email = passwordResetBinding.emailField.editText!!.text.toString()
        if (email.isEmpty()) {
            passwordResetBinding.emailField.helperText = getString(R.string.email_err_msg)
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            passwordResetBinding.emailField.helperText = getString(R.string.not_correct_email_msg)
            return
        }
        viewModel.resetPassword(requireContext(), email)

    }

    private fun setUpForgetPasswordButton() {
        binding.forgetPasswordBtn.setOnClickListener {
            showResetPasswordDialog()
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
                        showToast(requireContext(), getString(R.string.wrong_email_or_password_err),Toast.LENGTH_LONG)
                    }
                }

            }
        }
        viewModel.passwordResetLoading.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    passwordResetBinding.sendEmailBtn.makeInVisible()
                    passwordResetBinding.progressBar.show()
                } else {
                    passwordResetBinding.progressBar.hide()
                    passwordResetBinding.sendEmailBtn.show()
                    showToast(requireContext(), viewModel.resetMailSendResultMessage!!)
                    passwordResetDialog.dismiss()
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
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.gmailField.editText!!.text ).matches())  {
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