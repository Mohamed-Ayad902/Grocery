package com.example.grocery.ui.base.home

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.adapters.CategoriesAdapter
import com.example.grocery.adapters.HotAndNewAdapter
import com.example.grocery.adapters.OffersAdapter
import com.example.grocery.databinding.FragmentHomeBinding
import com.example.grocery.models.Cart
import com.example.grocery.models.Category
import com.example.grocery.models.HotAndNew
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
    private lateinit var hotAndNewAdapter: HotAndNewAdapter

    private val args: HomeFragmentArgs by navArgs()
    private val phoneNumber by lazy { args.phoneNumber!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setupOffersAdapter()
        setupCategoriesAdapter()
        setupHotAndNewAdapter()
        return binding.root
    }

    private fun setupHotAndNewAdapter() {
        binding.hotAndNewRecyclerView.apply {
            hotAndNewAdapter = HotAndNewAdapter(object : HotAndNewAdapter.OnHotItemClickListener {
                override fun onClick(hotAndNew: HotAndNew) {
                    storeViewModel.getProductByCategory(
                        hotAndNew.productCategory,
                        hotAndNew.productId
                    )
                }

                override fun onAddToCart(hotAndNew: HotAndNew) {
                    storeViewModel.addToCart(
                        Cart(
                            0,
                            hotAndNew.name,
                            hotAndNew.image,
                            hotAndNew.productId,
                            hotAndNew.brand,
                            hotAndNew.price,
                            1
                        )
                    )
                }
            })
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = hotAndNewAdapter
        }
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
                    storeViewModel.getProductByCategory(offer.productCategory, offer.productId)
                }
            }, mutableListOf())
            isAutoCycle = true
            setSliderAdapter(offersAdapter)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.getOffers()
        storeViewModel.getCategories()
        storeViewModel.getHotAndNew()

        binding.apply {
            imageView.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
            btnSeeAllBestDeals.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(
                        Constants.HOT_AND_NEW_COLLECTION
                    )
                )
            }
            etSearch.setOnEditorActionListener(object : OnEditorActionListener {
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                        if (etSearch.text.toString().trim().isNotEmpty()) {
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeFragmentToExploreFragment(
                                    etSearch.text.toString().lowercase()
                                )
                            )
                            return true
                        }
                    }
                    return false
                }
            })
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
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToProfileFragment(
                            phoneNumber = phoneNumber
                        )
                    )
                }
                is Resource.Loading -> Log.d(TAG, "user observe: loading")
                is Resource.Idle -> {} // nothing to do
            }
        }

        storeViewModel.addToCart.collectLatest(viewLifecycleOwner) {
            if (it == "added")
                showToast("product added to cart")
        }

        storeViewModel.offers.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "offers observe loading:")
                    loadingOffers(true)
                }
                is Resource.Error -> {
                    Log.e(TAG, "offers observe error : ${response.message}")
                    showToast("error loading offers")
                    loadingOffers(false)
                }
                is Resource.Idle -> {}
                is Resource.Success -> {
                    Log.d(TAG, "offers observe success :")
                    response.data?.let {
                        loadingOffers(false)
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
                    loadingCategories(true)
                }
                is Resource.Error -> {
                    Log.e(TAG, "categories observe error : ${response.message}")
                    showToast("error loading categories")
                    loadingCategories(false)
                }
                is Resource.Idle -> {}
                is Resource.Success -> {
                    Log.d(TAG, "categories observe success :")
                    response.data?.let {
                        loadingCategories(false)
                        categoriesAdapter.differ.submitList(it)
                    }
                }
            }
        }

        storeViewModel.hotAndNew.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "hot and new observe loading: ")
                    loadingHot(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "hot and new observe success: ")
                    response.data?.let {
                        hotAndNewAdapter.differ.submitList(it)
                        loadingHot(false)
                    }
                }
                is Resource.Error -> {
                    loadingHot(false)
                    Log.e(TAG, "hot and new observe error: ${response.message}")
                    showToast("error loading hot and new items")
                }
                is Resource.Idle -> {}
            }
        }

        storeViewModel.laptop.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "laptop observe loading:")
                }
                is Resource.Success -> {
                    Log.d(TAG, "laptop observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                                laptop = it
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "laptop observe error : ${response.message}")
                    showToast("error loading laptop")
                }
                is Resource.Idle -> {}
            }
        }

        storeViewModel.product.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "product observe loading:")
                }
                is Resource.Success -> {
                    Log.d(TAG, "product observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                                product = it
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "product observe error : ${response.message}")
                    showToast("error loading product")
                }
                is Resource.Idle -> {}
            }
        }
    }

    private fun loadingOffers(loading: Boolean) {
        binding.apply {
            if (loading) {
                sliderShimmer.visibility = View.VISIBLE
                offersSlider.visibility = View.GONE
            } else {
                sliderShimmer.visibility = View.GONE
                offersSlider.visibility = View.VISIBLE
            }
        }
    }

    private fun loadingCategories(loading: Boolean) {
        binding.apply {
            if (loading) {
                categoryShimmer.visibility = View.VISIBLE
                categoriesRecyclerView.visibility = View.GONE
            } else {
                categoryShimmer.visibility = View.GONE
                categoriesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun loadingHot(loading: Boolean) {
        binding.apply {
            if (loading) {
                hotShimmer.visibility = View.VISIBLE
                hotAndNewRecyclerView.visibility = View.GONE
            } else {
                hotShimmer.visibility = View.GONE
                hotAndNewRecyclerView.visibility = View.VISIBLE
            }
        }
    }

}