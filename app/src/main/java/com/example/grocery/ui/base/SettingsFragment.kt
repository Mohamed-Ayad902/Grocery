package com.example.grocery.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLocation.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_locationFragment)
            }
            btnMyOrders.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_ordersFragment)
            }
            btnMyProfile.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
            }
            btnLogout.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_phoneAuthenticationFragment)
            }
        }
    }

}