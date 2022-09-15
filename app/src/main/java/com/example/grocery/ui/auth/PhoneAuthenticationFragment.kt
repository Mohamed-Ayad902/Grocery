package com.example.grocery.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentPhoneAuthenticationBinding

class PhoneAuthenticationFragment : Fragment() {

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
            findNavController().navigate(R.id.action_phoneAuthenticationFragment_to_verifyFragment)
        }
    }

}