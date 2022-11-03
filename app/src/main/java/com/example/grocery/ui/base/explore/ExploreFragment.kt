package com.example.grocery.ui.base.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.adapters.SearchedLaptopsAdapter
import com.example.grocery.adapters.SearchedProductsAdapter
import com.example.grocery.databinding.FragmentExploreBinding
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ExploreFragment mohamed"

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var laptopsAdapter: SearchedLaptopsAdapter
    private lateinit var productsAdapter: SearchedProductsAdapter
    private val args: ExploreFragmentArgs by navArgs()
    private val passedQuery by lazy { args.query }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        setupLaptopRecyclerView()
        setupProductRecyclerView()
        return binding.root
    }

    private fun setupProductRecyclerView() {
        binding.productsRecyclerView.apply {
            productsAdapter =
                SearchedProductsAdapter(object : SearchedProductsAdapter.OnClickListener {
                    override fun onClick(product: Product) {
                        viewModel.getProduct(product.id)
                    }
                })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
        }
    }

    private fun setupLaptopRecyclerView() {
        binding.laptopsRecyclerView.apply {
            laptopsAdapter =
                SearchedLaptopsAdapter(object : SearchedLaptopsAdapter.OnClickListener {
                    override fun onClick(laptop: Laptop) {
                        viewModel.getLaptop(laptop.id)
                    }
                })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = laptopsAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passedQuery?.let {
            binding.etSearch.setText(it)
            viewModel.search(it)
        }
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(700)
                editable?.let { query ->
                    if (query.toString().trim().isNotEmpty())
                        viewModel.search(query.toString())
                }
            }
        }

        observeListeners()

    }

    private fun observeListeners() {
        viewModel.laptopsProducts.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "observe search : loading")
                    loadingSearch(true)
                }
                is Resource.Success -> {
                    response.data?.let {
                        loadingSearch(false)
                        Log.d(
                            TAG,
                            "observe search success laptops: ${it.laptop.size}  products: ${it.product.size}"
                        )
                        if (it.laptop.isNotEmpty()) {
                            laptopsAdapter.differ.submitList(it.laptop)
                        } else laptopsAdapter.differ.submitList(emptyList())
                        if (it.product.isNotEmpty()) {
                            productsAdapter.differ.submitList(it.product)
                        } else productsAdapter.differ.submitList(emptyList())

                        if (it.laptop.isEmpty() && it.product.isEmpty()) {
                            binding.apply {
                                tvNoResults.visibility = View.VISIBLE
                                lottieAnimation.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                tvNoResults.visibility = View.GONE
                                lottieAnimation.visibility = View.GONE
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    loadingSearch(false)
                    Log.e(TAG, "observe search error: ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

        viewModel.laptop.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "laptop observe loading:")
                }
                is Resource.Success -> {
                    Log.d(TAG, "laptop observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(laptop = it)
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

        viewModel.product.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "product observe loading:")
                }
                is Resource.Success -> {
                    Log.d(TAG, "product observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(product = it)
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

    private fun loadingSearch(loading: Boolean) {
        binding.apply {
            if (loading) {
                searchShimmer.visibility = View.VISIBLE
                productsRecyclerView.visibility = View.GONE
                laptopsRecyclerView.visibility = View.GONE
            } else {
                searchShimmer.visibility = View.GONE
                productsRecyclerView.visibility = View.VISIBLE
                laptopsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

}