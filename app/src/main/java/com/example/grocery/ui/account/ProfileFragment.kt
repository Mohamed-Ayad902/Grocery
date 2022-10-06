package com.example.grocery.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.databinding.FragmentProfileBinding
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.auth.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ProfileFragment mohamed"

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private val userInfoViewModel by activityViewModels<UserViewModel>()

    private var image: Uri? = null
    private var hasSelectedLocation = false
    private val args by navArgs<ProfileFragmentArgs>()

    private lateinit var binding: FragmentProfileBinding

    override fun onResume() {
        super.onResume()
        image?.let {
            Glide.with(requireContext()).load(image).into(binding.imageView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        val user = args.user

        binding.apply {
            btnSubmit.setOnClickListener {
                etName.error = null

                if (etName.editText?.text.toString().trim().isEmpty())
                    etName.error = "Name is required"
                else if (!hasSelectedLocation)
                    showToast("Select your location")
                else if (image == null)
                    showToast("Select profile picture")
                else
                    submitData()
                Log.d(TAG, "onViewCreated: $user    ${image.toString()}")
            }

            imageView.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
            }

            etLocation.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_locationFragment)
            }

        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            image = result.data?.data
            image?.let {
                Glide.with(requireContext()).load(image).into(binding.imageView)
            }
        }

    private fun submitData() {
        val name = binding.etName.editText?.text.toString().trim()
        val location = binding.tvLocation.text.toString().trim()
        authViewModel.updateUserInfo(name, image, location)
    }

    private fun subscribeToObservers() {
        userInfoViewModel.userLocation.collectLatest(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    tvLocation.text = it
                    tvLocation.setTextColor(Constants.COLOR_GREEN)
                }
                hasSelectedLocation = true
            }
        }

        authViewModel.userInfo.collectLatest(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Log.e(TAG, "observing user: error ${it.message!!}")
                    showToast("error updating account")
                }
                is Resource.Idle -> {
                    Log.d(TAG, "observing user: Idle")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "observing user: loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "observing user: success ${it.data}")
                    showToast(it.data!!)
                    findNavController().popBackStack()
                }
            }
        }

    }
}

inline fun <T> Flow<T>.collectLatest(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        collectLatest {
            onCollect(it)
        }
    }
}