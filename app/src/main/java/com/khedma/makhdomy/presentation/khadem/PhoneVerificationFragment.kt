package com.khedma.makhdomy.presentation.khadem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentPhoneVerificationBinding
import com.khedma.makhdomy.presentation.utils.createPreferences
import com.khedma.makhdomy.presentation.utils.fromJson
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.makeInVisible
import com.khedma.makhdomy.presentation.utils.readFromPreferences
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast
import com.khedma.makhdomy.presentation.utils.toJson
import com.khedma.makhdomy.presentation.utils.writePreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneVerificationFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth

    val binding by lazy { FragmentPhoneVerificationBinding.inflate(layoutInflater) }

    private val TAG = "PHONE VERIFICATION"

    private var storedVerificationId: String? = null

    private var resendToken: ForceResendingToken? = null

    private var credential: PhoneAuthCredential? = null

    private val currentKhadem by lazy { fromJson(readFromPreferences(getString(R.string.khadem_key))!!) }

    private val phoneNumber: String by lazy { "+2${currentKhadem.phone}" }

    val viewModel: KhademViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPreferences(requireContext())
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnLoading()
        setUpUi()
    }


    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                binding.otpView.setText(credential.smsCode)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                viewModel.endLoading()
                showToast(requireContext(), getString(R.string.try_again_msg))
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }


    private fun setUpUi() {
        setUpVerificationDescriptionTxt()
        setUpVerificationBtn()
        setUpResendCodeBtn()
    }

    private fun setUpVerificationBtn() {
        binding.veriicationBtn.setOnClickListener {
            binding.otpView.error = null
            if (binding.otpView.text!!.isEmpty()) {
                binding.otpView.error = getString(R.string.enter_code_msg)
                return@setOnClickListener
            }
            if (binding.otpView.text!!.length != 6) {
                binding.otpView.error = getString(R.string.enter_code_msg2)
                return@setOnClickListener
            }
            storedVerificationId?.let {
                viewModel.startLoading()
                credential = PhoneAuthProvider.getCredential(it, binding.otpView.text.toString())
                signInWithPhoneAuthCredential(credential!!)
            }
        }
    }

    private fun setUpVerificationDescriptionTxt() {
        binding.descriptionTxt.text =
            "${getString(R.string.enter_code_description)} ${currentKhadem.phone}"
    }

    private fun setUpResendCodeBtn() {
        binding.resendOtpBtn.setOnClickListener {
            resendVerificationCode()
        }
    }

    private fun resendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            requireActivity(),  // Activity (for callback binding)
            callbacks,  // OnVerificationStateChangedCallbacks
            resendToken
        ) // ForceResendingToken from callbacks
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                currentKhadem.key = it.result!!.user!!.uid
                viewModel.addKhadem(currentKhadem)
            } else {
                viewModel.endLoading()
                showToast(requireContext(), getString(R.string.otp_err_msg))
            }
        }
    }

    private fun observeOnLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    binding.veriicationBtn.makeInVisible()
                    binding.progressBar.show()
                } else {
                    if (viewModel.successFlag != null && viewModel.resultKhademId != null)
                        onSavingKhademSuccess()
                    else {
                        binding.veriicationBtn.show()
                        binding.progressBar.hide()
                        //  showToast(requireContext(), getString(R.string.internet_err_msg))
                    }
                }

            }
        }
    }

    private fun onSavingKhademSuccess() {
        currentKhadem.key = viewModel.resultKhademId
        writePreferences(getString(R.string.khadem_key), toJson(currentKhadem))
        showToast(requireContext(), getString(R.string.signed_in_successfully))
        findNavController().navigate(R.id.action_phoneVerificationFragment_to_makhdommenListFragment)
    }


}