package com.example.grocery.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.adapters.OrdersAdapter
import com.example.grocery.databinding.FragmentOrdersBinding
import com.example.grocery.models.Order
import com.example.grocery.other.Resource
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "OrdersFragment mohamed"

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            ordersAdapter = OrdersAdapter(object : OrdersAdapter.OnOrderClickListener {
                override fun onClick(order: Order) {
                    findNavController().navigate(
                        OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(order)
                    )
                }
            })
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ordersAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.orders.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "observe user orders: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(TAG, "observe user orders: success")
                        ordersAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe user orders: error : ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

    }

}