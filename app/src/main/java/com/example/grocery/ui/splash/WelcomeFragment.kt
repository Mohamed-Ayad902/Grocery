package com.example.grocery.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentWelcomeBinding
import com.example.grocery.ui.auth.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    private val authViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFirstLogIn = authViewModel.checkIfFirstAppOpened()
        if (!isFirstLogIn) {
            val isLoggedIn = authViewModel.checkIfUserLoggedIn()
            if (isLoggedIn) {
                findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_welcomeFragment_to_phoneAuthenticationFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_phoneAuthenticationFragment)
        }
    }

}