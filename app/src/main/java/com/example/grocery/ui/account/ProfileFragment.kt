package com.example.grocery.ui.account

import android.content.Intent
import android.location.Address
import android.location.Geocoder
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
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ProfileFragment mohamed"

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private val userInfoViewModel by activityViewModels<UserViewModel>()

    private var image: Uri? = null
    private var hasSelectedLocation = false
    private val args by navArgs<ProfileFragmentArgs>()
    private val phoneNumber by lazy { args.phoneNumber!! }
    private var latLng: LatLng? = null

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

        user?.let {
            binding.apply {
                etName.editText?.setText(it.name)
                tvLocation.text = getCityNameFromLocation(LatLng(it.latitude, it.longitude))
                Glide.with(requireContext()).load(it.image).into(imageView)
            }
        }
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
        latLng?.let {
            val name = binding.etName.editText?.text.toString().trim()
            authViewModel.updateUserInfo(name, image, it.latitude, it.longitude, phoneNumber)
        }
    }

    private fun subscribeToObservers() {
        userInfoViewModel.latLng.collectLatest(viewLifecycleOwner) {
            it?.let {
                latLng = it
                binding.apply {
                    tvLocation.text = getCityNameFromLocation(it)
                    tvLocation.setTextColor(Constants.COLOR_GREEN)
                }
                hasSelectedLocation = true
            }
        }
        userInfoViewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "observe user data : loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(
                            TAG,
                            "observe user data : success location -- ${it.latitude}  ${it.longitude}"
                        )
                        binding.apply {
                            etName.editText?.setText(it.name)
                            tvLocation.text =
                                getCityNameFromLocation(LatLng(it.latitude, it.longitude))
                            Glide.with(requireContext()).load(it.image).into(imageView)
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe user data error :${response.message}")
                }
                is Resource.Idle -> {}
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

    private fun getCityNameFromLocation(locationLatLng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(locationLatLng.latitude, locationLatLng.longitude, 1)!!
        return addresses[0].getAddressLine(0)
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