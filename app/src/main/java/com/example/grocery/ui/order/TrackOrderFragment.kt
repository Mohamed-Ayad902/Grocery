package com.example.grocery.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocery.R
import com.example.grocery.databinding.FragmentTrackOrderBinding
import com.example.grocery.models.Status
import com.example.grocery.other.Resource
import com.example.grocery.ui.account.collectLatest
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "TrackOrderFragment mohamed"

@AndroidEntryPoint
class TrackOrderFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrderBinding
    private val args: TrackOrderFragmentArgs by navArgs()
    private val orderId by lazy { args.orderId }
    private val viewModel: TrackOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackOrderBinding.inflate(layoutInflater, container, false)
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnBackToHome.setOnClickListener { findNavController().navigate(R.id.action_trackOrderFragment_to_homeFragment) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrderById(orderId)
        binding.tvOrderId.text = orderId
        viewModel.order.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "observe track order: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(TAG, "observe track order: success")
                        binding.apply {
                            when (it.status) {
                                Status.PLACED -> {
                                    lottieAnimation.setAnimation(R.raw.order_placed_animation)
                                    tvOrderStatus.text = "Your order has been placed!"
                                }
                                Status.CONFIRMED -> {
                                    lottieAnimation.setAnimation(R.raw.order_confirmed_animation)
                                    tvOrderStatus.text = "We are preparing your order!"
                                }
                                Status.COMING -> {
                                    lottieAnimation.setAnimation(R.raw.delivery_animation)
                                    tvOrderStatus.text = "Your order is on it's way to you!"
                                }
                                Status.DELIVERED -> {
                                    lottieAnimation.setAnimation(R.raw.order_shipped_animation)
                                    tvOrderStatus.text = "You supposed to received the order!"
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe track order error: ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

    }

}