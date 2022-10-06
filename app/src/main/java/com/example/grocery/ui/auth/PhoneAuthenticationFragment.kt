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
import com.example.grocery.other.Resource
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
        observeListener()

        binding.btnSend.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            if (phoneNumber.isEmpty()) {
                showToast("please enter your phone number")
            } else if (phoneNumber.length < 10) {
                showToast("invalid phone number")
                Log.e(TAG, "onViewCreated:length: ${phoneNumber.length}   $phoneNumber")
            } else {
                validPhoneNumber = "+20$phoneNumber"
                checkPhoneNumber()
            }
        }

    }

    private fun sendFirstSMSVerification(validPhoneNumber: String) {
        signInWithPhoneNumber(validPhoneNumber)
    }


    private fun observeListener() {
        authViewModel.auth.collectLatest(viewLifecycleOwner) {
            when (it) {
                /* some cases the phone number can be instantly verified without needing to send or enter a verification code
                    so here we will login with credential and we observe when login to open MainFragment if user has already an account
                    or open createUserFragment to add user info in app .
                 */
                is AuthResource.SuccessWithCredential -> {
                    authViewModel.signInWithPhoneAuthCredential(it.data)
                    authViewModel.setAuthState(AuthResource.Idle)
                    Log.d(TAG, "observeListener auth: SuccessWithCredential ${it.data}")
                }
                /*
                    Case two if user will verify with code has sent to him so here will open checkCodeAuth Fragment to check if
                    verification code has user added is correct .
                 */
                is AuthResource.SuccessWithCode -> {
                    verificationId = it.verificationId
                    verificationToken = it.verificationToken
                    navigateToVerifyFragment(it.verificationId, it.verificationToken)
                    authViewModel.setAuthState(AuthResource.Idle)
                    Log.d(TAG, "observeListener auth: SuccessWithCode $it")
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

        authViewModel.signInStatus.collectLatest(viewLifecycleOwner)
        {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is Resource.Error -> {
                    Log.e(TAG, "observeListener: signInStatus error ${it.message}")
                    showToast(it.message!!)
                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is Resource.Success -> {
                    Log.d(TAG, "observeListener: signInStatus success")
                    navigateToMainFragment()
                }
                is Resource.Loading -> Log.d(TAG, "observeListener: signInStatus loading")
                is Resource.Idle -> {} // nothing to do
            }
        }

    }

    private fun navigateToMainFragment() {
        findNavController().navigate(PhoneAuthenticationFragmentDirections.actionPhoneAuthenticationFragmentToHomeFragment())
    }


    private fun checkPhoneNumber() {
        /* Check this is the first verification sent from this mobile and not a second one by check if firebase timeout
           has finished . */
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

    private fun navigateToVerifyFragment(
        verificationId: String,
        verificationToken: PhoneAuthProvider.ForceResendingToken
    ) {
        val verificationModel =
            PhoneVerify(verificationId, verificationToken, validPhoneNumber)
        val action =
            PhoneAuthenticationFragmentDirections.actionPhoneAuthenticationFragmentToVerifyFragment(
                verificationModel
            )
        findNavController().navigate(action)
    }

    private fun signInWithPhoneNumber(validPhoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(validPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .build()
        verificationTimeOut = (System.currentTimeMillis() + 60000L)
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}