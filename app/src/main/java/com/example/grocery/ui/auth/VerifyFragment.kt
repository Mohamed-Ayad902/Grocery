package com.example.grocery.ui.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocery.databinding.FragmentVerifyBinding
import com.example.grocery.other.Constants.COUNT_DOWN_DELAY
import com.example.grocery.other.Constants.COUNT_DOWN_INTERVAL
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "VerifyFragment mohamed"

@AndroidEntryPoint
class VerifyFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentVerifyBinding
    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private val args by navArgs<VerifyFragmentArgs>()
    private val phoneVerify by lazy { args.verify }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListeners()

        binding.btnResend.setOnClickListener {
            resendCode()
        }

        binding.btnVerify.setOnClickListener {
            val otp =
                binding.code1.text.toString() + binding.code2.text.toString() + binding.code3.text.toString() + binding.code4.text.toString() + binding.code5.text.toString() + binding.code6.text.toString()

            if (otp.length != 6) {
                showToast("Please enter verify code")
            } else {
                val credential =
                    PhoneAuthProvider.getCredential(phoneVerify.verificationId, otp)
                authViewModel.signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun startMinuteCountDown() {
        object : CountDownTimer(COUNT_DOWN_DELAY, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnResend.text =
                    "00:%1\$02d ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                changeResendTextViewsStyle(false)
            }
        }.start()
    }

    private fun changeResendTextViewsStyle(showTimer: Boolean) {
        binding.btnResend.isEnabled = !showTimer
    }

    private fun resendCode() {
        if (binding.btnResend.isEnabled) {
            changeResendTextViewsStyle(true)
            startMinuteCountDown()
        }
        resendVerificationCode()
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneVerify.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .setForceResendingToken(phoneVerify.verificationToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun observeListeners() {
        authViewModel.signInStatus.collectLatest(viewLifecycleOwner) {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is Resource.Error -> {
                    Log.e(TAG, "observeListener: signInStatus observe error ${it.message}")
                    showToast("Error occurred.. please try again later")
                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is Resource.Success -> {
                    Log.d(TAG, "observeListener: signInStatus observe success ${it.data}")
                    navigateToHomeFragment()
                }
                is Resource.Loading -> {
                    Log.d(TAG, "observeListener: signInStatus observe loading")
                }
                is Resource.Idle -> {}  // noting to do
            }
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(VerifyFragmentDirections.actionVerifyFragmentToHomeFragment())
    }


}
