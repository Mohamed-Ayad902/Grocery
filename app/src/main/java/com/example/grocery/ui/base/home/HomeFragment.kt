package com.example.grocery.ui.base.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.adapters.CategoriesAdapter
import com.example.grocery.adapters.OffersAdapter
import com.example.grocery.databinding.FragmentHomeBinding
import com.example.grocery.models.Category
import com.example.grocery.models.Offer
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.example.grocery.other.loadImage
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.UserViewModel
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment mohamed"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val userViewModel: UserViewModel by viewModels()
    private val storeViewModel: HomeViewModel by viewModels()

    private lateinit var offersAdapter: OffersAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setupOffersAdapter()
        setupCategoriesAdapter()
        return binding.root
    }

    private fun setupCategoriesAdapter() =
        binding.categoriesRecyclerView.apply {
            categoriesAdapter =
                CategoriesAdapter(object : CategoriesAdapter.OnCategoryClickListener {
                    override fun onClick(category: Category) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(
                                category.name
                            )
                        )
                    }
                })
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = categoriesAdapter
        }


    private fun setupOffersAdapter() =
        binding.offersSlider.apply {
            offersAdapter = OffersAdapter(object : OffersAdapter.OnOfferClickListener {
                override fun onClick(offer: Offer) {
                    storeViewModel.getProductByOffer(offer)
                }
            }, mutableListOf())
            isAutoCycle = true
            setSliderAdapter(offersAdapter)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.getOffers()
        storeViewModel.getCategories()

        binding.imageView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
        }

        observeListeners()

    }

    private fun observeListeners() {
        // check if user have data into firebase fireStore
        userViewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {        // so we have user and all ok
                    Log.d(TAG, "user observe success :${response.data} ")
                    response.data?.let { user ->
                        binding.apply {
                            tvName.text = user.name
                            imageView.loadImage(user.image)
                        }
                    }
                }
                is Resource.Error -> {      // user need to create account first
                    Log.e(
                        TAG,
                        "user observe error: ${response.message}  we need to complete profile"
                    )
                    showToast("Please complete your profile")
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
                }
                is Resource.Loading -> Log.d(TAG, "user observe: loading")
                is Resource.Idle -> {} // nothing to do
            }
        }
        storeViewModel.offers.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "offers observe loading:")
                }
                is Resource.Error -> {
                    Log.e(TAG, "offers observe error : ${response.message}")
                    showToast("error loading offers")
                }
                is Resource.Idle -> {}
                is Resource.Success -> {
                    Log.d(TAG, "offers observe success :")
                    response.data?.let {
                        offersAdapter.submitList(it)
                        binding.offersSlider.startAutoCycle()
                    }
                }
            }
        }
        storeViewModel.categories.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "categories observe loading:")
                }
                is Resource.Error -> {
                    Log.e(TAG, "categories observe error : ${response.message}")
                    showToast("error loading categories")
                }
                is Resource.Idle -> {}
                is Resource.Success -> {
                    Log.d(TAG, "categories observe success :")
                    response.data?.let {
                        categoriesAdapter.differ.submitList(it)
                    }
                }
            }
        }
        storeViewModel.laptop.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "laptop observe loading:")
                }
                is Resource.Error -> {
                    Log.e(TAG, "laptop observe error : ${response.message}")
                    showToast("error loading laptop")
                }
                is Resource.Idle -> {}
                is Resource.Success -> {
                    Log.d(TAG, "laptop observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                                it
                            )
                        )
                    }
                }
            }
        }

    }
}