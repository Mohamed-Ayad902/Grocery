package com.example.grocery.ui.base.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.adapters.CartAdapter
import com.example.grocery.databinding.FragmentCartBinding
import com.example.grocery.models.Cart
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var totalPrice: Int = 0
    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            cartAdapter = CartAdapter(object : CartAdapter.OnCartClickListener {
                override fun onPlus(cart: Cart) {
                    viewModel.increase(cart)
                }

                override fun onMinus(cart: Cart) {
                    viewModel.decrease(cart)
                }

                override fun onDelete(cart: Cart) {
                    viewModel.delete(cart)
                }
            })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToCheckOutOrderFragment(
                    totalPrice,
                    cartAdapter.differ.currentList.toTypedArray()
                )
            )
        }

        viewModel.getCartItems().collectLatest(viewLifecycleOwner) {
            cartAdapter.differ.submitList(it)
            it.forEach { item ->
                totalPrice += item.productQuantity * item.productPrice
            }
        }
    }
}