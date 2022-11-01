package com.example.grocery.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocery.adapters.OrderAdapter
import com.example.grocery.databinding.FragmentOrderDetailsBinding
import com.example.grocery.models.Order
import com.example.grocery.models.PaymentMethod
import com.example.grocery.other.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*

private const val TAG = "OrderDetailsFragment mohamed"

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val args: OrderDetailsFragmentArgs by navArgs()
    private val order: Order by lazy { args.order }
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            orderAdapter = OrderAdapter()
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = orderAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnTrackOrder.setOnClickListener {
                findNavController().navigate(
                    OrderDetailsFragmentDirections.actionOrderDetailsFragmentToTrackOrderFragment(
                        order.id
                    )
                )
            }

            val formated = DateFormat.getDateTimeInstance().format(Date(order.time))
            orderAdapter.differ.submitList(order.products)
            showToast(formated.toString())
            tvOrderId.text = order.id
            tvTotalPrice.text = order.totalPrice.toString()
            if (order.paymentMethod == PaymentMethod.ONLINE)
                tvPaymentMethod.text = "ONLINE"
            else
                tvPaymentMethod.text = "CASH"
            tvOrderTime.text = formated

        }

    }

}