package com.example.grocery.ui.base.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.adapters.FavoriteLaptopsAdapter
import com.example.grocery.adapters.FavoriteProductAdapter
import com.example.grocery.databinding.FragmentFavoriteBinding
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.collectLatest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteLaptopsAdapter: FavoriteLaptopsAdapter
    private lateinit var favoriteProductsAdapter: FavoriteProductAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        setupLaptopsRecyclerView()
        setupProductsRecyclerView()
        return binding.root
    }

    private fun setupProductsRecyclerView() {
        binding.productsRecyclerView.apply {
            favoriteProductsAdapter = FavoriteProductAdapter(object :
                FavoriteProductAdapter.OnFavoriteProductClickListener {
                override fun onSavedClick(product: Product) {
                    viewModel.deleteProduct(product)
                    Snackbar.make(view!!, "UnSaved", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.addProduct(product)
                        }
                        show()
                    }
                }

                override fun onItemClick(product: Product) {
                    findNavController().navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                            product = product
                        )
                    )
                }
            })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteProductsAdapter
        }
    }

    private fun setupLaptopsRecyclerView() {
        binding.laptopRecyclerView.apply {
            favoriteLaptopsAdapter = FavoriteLaptopsAdapter(object :
                FavoriteLaptopsAdapter.OnFavoriteLaptopClickListener {
                override fun onSavedClick(laptop: Laptop) {
                    viewModel.deleteLaptop(laptop)
                    Snackbar.make(view!!, "UnSaved", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.addLaptop(laptop)
                        }
                        show()
                    }
                }

                override fun onItemClick(laptop: Laptop) {
                    findNavController().navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                            laptop = laptop
                        )
                    )
                }
            })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteLaptopsAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMoveAllToCart.setOnClickListener {
            viewModel.moveAllToCart()
        }

        observeListeners()


    }

    private fun observeListeners() {
        viewModel.getSavedLaptops().collectLatest(viewLifecycleOwner) {
            favoriteLaptopsAdapter.differ.submitList(it)
        }

        viewModel.getSavedProducts().collectLatest(viewLifecycleOwner) {
            favoriteProductsAdapter.differ.submitList(it)
        }

        viewModel.addedToCart.collectLatest(viewLifecycleOwner) {
            if (it.isNotEmpty())
                showToast(it)
        }
    }


}