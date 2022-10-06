package com.example.grocery.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentSplashBinding
import com.example.grocery.ui.auth.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val authViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimations()
        checkScreens()
    }

    private fun checkScreens() {
        lifecycleScope.launch {
            delay(5000)
            val isFirstTime = authViewModel.isFirstTime()
            if (!isFirstTime) {
                val isLoggedIn = authViewModel.checkIfUserLoggedIn()
                if (isLoggedIn) {   // opened app before -> if signed in we go to home else go to auth.
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_phoneAuthenticationFragment)
                }
            } else {  // first time to open the app
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            }
        }
    }

    private fun startAnimations() {
        val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.splashAnimation.apply {
            startAnimation(bounce)
            playAnimation()
        }

        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        lifecycleScope.launch {
            delay(1500)
            binding.tvTitle.startAnimation(fadeIn)
        }

    }

}