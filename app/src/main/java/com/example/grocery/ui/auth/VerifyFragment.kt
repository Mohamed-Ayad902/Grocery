package com.example.grocery.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentVerifyBinding

class VerifyFragment : Fragment() {

    private lateinit var binding: FragmentVerifyBinding

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
            findNavController().navigate(R.id.action_verifyFragment_to_profileFragment)
        }
    }

}