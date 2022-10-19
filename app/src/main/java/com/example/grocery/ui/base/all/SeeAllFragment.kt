package com.example.grocery.ui.base.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocery.adapters.SeeAllLaptopsAdapter
import com.example.grocery.adapters.SeeAllProductsAdapter
import com.example.grocery.databinding.FragmentSeeAllBinding
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SeeAllFragment mohamed"

@AndroidEntryPoint
class SeeAllFragment : Fragment() {

    private lateinit var binding: FragmentSeeAllBinding
    private lateinit var laptopsAdapter: SeeAllLaptopsAdapter
    private lateinit var productsAdapter: SeeAllProductsAdapter
    private val args: SeeAllFragmentArgs by navArgs()

    private val viewModel: SeeAllViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        if (args.category == Constants.CATEGORY_LAPTOP)
            setupLaptopsAdapter()
        else
            setupProductsAdapter()

    }

    private fun setupProductsAdapter() {
        binding.recyclerView.apply {
            productsAdapter =
                SeeAllProductsAdapter(object : SeeAllProductsAdapter.OnProductClickListener {
                    override fun onClick(product: Product) {
                        viewModel.getProduct(product.id)
                    }

                    override fun onAddToCart(product: Product) {
                        viewModel.addToCart(
                            (Cart(
                                0,
                                product.name,
                                product.image,
                                product.id,
                                product.brand,
                                product.price,
                                1
                            ))
                        )
                    }
                })
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productsAdapter
        }
    }

    private fun setupLaptopsAdapter() {
        binding.recyclerView.apply {
            laptopsAdapter =
                SeeAllLaptopsAdapter(object : SeeAllLaptopsAdapter.OnLaptopClickListener {
                    override fun onClick(laptop: Laptop) {
                        viewModel.getLaptop(laptop.id)
                    }

                    override fun onAddToCart(laptop: Laptop) {
                        viewModel.addToCart(
                            (Cart(
                                0,
                                laptop.name,
                                laptop.image,
                                laptop.id,
                                laptop.brand,
                                laptop.price,
                                1
                            ))
                        )
                    }
                })
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = laptopsAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.seeAllProductsByCategory(args.category)

        observeListeners()
    }

    private fun observeListeners() {
        viewModel.laptops.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe laptops: loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "observe laptops: success ${response.data?.size}")
                    response.data?.let {
                        laptopsAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe laptops error : ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

        viewModel.products.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe products: loading")
                }
                is Resource.Success -> {
                    Log.d(
                        TAG,
                        "observe products: success ${response.data?.size}  ${response.data.toString()}"
                    )
                    response.data?.let {
                        productsAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe products error : ${response.message}")
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
                            SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(
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

        viewModel.product.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "product observe loading:")
                }
                is Resource.Success -> {
                    Log.d(TAG, "product observe success :")
                    response.data?.let {
                        findNavController().navigate(
                            SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(
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

        viewModel.addToCart.collectLatest(viewLifecycleOwner) {
            if (it == "added")
                showToast("added to cart")
        }

    }

}