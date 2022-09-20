package com.example.grocery.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.grocery.databinding.FragmentVerifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class VerifyFragment : Fragment() {

    private lateinit var binding: FragmentVerifyBinding
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnVerify.setOnClickListener {


            // get storedVerificationId from the intent
            val storedVerificationId = PhoneAuthenticationFragment.mVerificationId

            // fill otp and call the on click on button
            val otp =
                binding.code1.text.toString() + binding.code2.text.toString() + binding.code3.text.toString() + binding.code4.text.toString() + binding.code5.text.toString() + binding.code6.text.toString()

            val credential =
                PhoneAuthProvider.getCredential(storedVerificationId, otp)
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    Log.d("mohamed", "onViewCreated: all done a5ern")
                }.addOnFailureListener {
                    Log.e("mohamed", "onViewCreated: $it")
                }

        }

    }


}
