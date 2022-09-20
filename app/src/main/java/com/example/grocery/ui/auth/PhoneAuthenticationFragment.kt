package com.example.grocery.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.grocery.databinding.FragmentPhoneAuthenticationBinding
import com.example.grocery.models.PhoneVerify
import com.example.grocery.other.AuthResource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "PhoneAuthenticationFrag mohamed"


@AndroidEntryPoint
class PhoneAuthenticationFragment : Fragment() {

    private var verificationTimeOut: Long = 0
    private var validPhoneNumber: String = ""
    private var verificationId: String? = null
    private var verificationToken: PhoneAuthProvider.ForceResendingToken? = null

    @Inject
    lateinit var auth: FirebaseAuth

    private val authViewModel by activityViewModels<AuthenticationViewModel>()

    private lateinit var binding: FragmentPhoneAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneAuthenticationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setOnClickListener {
            val phoneNumber = "+20" + binding.etPhone.text.toString()

            sendFirstSMSVerification(phoneNumber)

        }
    }

    private fun sendFirstSMSVerification(validPhoneNumber: String) {
        signInWithPhoneNumber(validPhoneNumber)
    }


    private fun observeListener() {
        authViewModel.phoneMainAuthLiveData.observe(viewLifecycleOwner) {

        }

        authViewModel.auth.collectLatest(viewLifecycleOwner) {
            when (it) {
                /* some cases the phone number can be instantly verified without needing to send or enter a verification code
                    so here we will login with credential and we observe when login to open MainFragment if user has already an account
                    or open createUserFragment to add user info in app .
                 */
                is AuthResource.SuccessWithCredential -> {
                    authViewModel.signInWithPhoneAuthCredential(it.data)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()
                }
                /*
                    Case two if user will verify with code has sent to him so here will open checkCodeAuth Fragment to check if
                    verification code has user added is correct .
                 */
                is AuthResource.SuccessWithCode -> {
                    verificationId = it.verificationId
                    verificationToken = it.verificationToken
                    navigateToCheckPhoneNumberAuthFragment(it.verificationId, it.verificationToken)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()

                }
                // If an error occurred will notify user with error message.
                is AuthResource.Error -> {
                    Log.e(TAG, "observeListener: error " + it.error)
                    when (it.error) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            showToast("Please check your connection")
                        }
                        else -> {
                            showToast("Error occurred ... please try again later")
                        }
                    }
                }
                // show loading dialog while send phone verification .
                is AuthResource.Loading -> {
                    Log.d(TAG, "observeListener: loading")
                }
                // hide loading dialog when app wait reaction from user.
                is AuthResource.Idle -> {
                    Log.d(TAG, "observeListener: idle")
                }
            }
        }
    }

    authViewModel.signInStatusLiveData.observe(viewLifecycleOwner)
    {
        when (it) {
            // When had an error with automatically login app will push an error message.
            is Resource.Error -> {
                loadingDialog.hide()
                showToast(it.msg!!)
            }
            /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
               or open createUserFragment to add user info in app .
            */
            is Resource.Success -> {
                loadingDialog.hide()
                navigateToMainFragment()
            }
            is Resource.Loading -> loadingDialog.show()
        }
    }

}


fun checkPhoneNumber() {
    val phoneNumber = binding.etPhone.text.toString().trim()
    when {
        phoneNumber.isEmpty() -> {
            showToast("Phone number is required")
        }
        phoneNumber.toInt() < 4 || phoneNumber.length == 10 || phoneNumber.length == 11 -> {
            showToast("Please enter a valid number")
        }
        else -> {
            /* Check this is the first verification sent from this mobile and not a second one by check if firebase timeout
               has finished . */
            validPhoneNumber = "+20$phoneNumber"
            Log.d(TAG, "checkPhoneNumber: $validPhoneNumber")
            if (verificationTimeOut == 0L || verificationTimeOut < System.currentTimeMillis()) {
                sendFirstSMSVerification(validPhoneNumber)
            } else {
                verificationId?.let {
                    verificationToken?.let {
                        findNavController().navigate(
                            PhoneAuthenticationFragmentDirections.actionPhoneAuthenticationFragmentToVerifyFragment(
                                PhoneVerify(
                                    verificationId!!,
                                    verificationToken!!,
                                    validPhoneNumber
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun signInWithPhoneNumber(validPhoneNumber: String) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(validPhoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(requireActivity())
        .setCallbacks()
        .build()
    verificationTimeOut = (System.currentTimeMillis() + 60000L)
    PhoneAuthProvider.verifyPhoneNumber(options)
}
}