package com.example.grocery.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.databinding.FragmentSettingsBinding
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.UserViewModel
import com.example.grocery.ui.account.collectLatest
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "SettingsFragment mohamed"

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: UserViewModel by activityViewModels()

    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    Log.e(TAG, "observe user error :${response.message} ")
                }
                is Resource.Idle -> {}
                is Resource.Loading -> {
                    Log.d(TAG, "observe user loading: ")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Glide.with(requireContext()).load(it.image).into(binding.imageView)
                        binding.tvName.text = it.name
                    }
                }
            }
        }

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
                fireBaseAuth.signOut()
                findNavController().navigate(R.id.action_settingsFragment_to_phoneAuthenticationFragment)
            }
            btnAboutUs.setOnClickListener {
                showToast("mohamed.ayad7474@gmail.com")
            }
        }
    }

}