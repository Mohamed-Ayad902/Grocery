package com.example.grocery.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocery.databinding.FragmentCheckOutOrderBinding
import com.example.grocery.models.OnlinePayment
import com.example.grocery.other.Resource
import com.example.grocery.ui.account.UserViewModel
import com.example.grocery.ui.account.collectLatest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CheckOutOrderFragment mohamed"

@AndroidEntryPoint
class CheckOutOrderFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCheckOutOrderBinding

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private val userViewModel by activityViewModels<UserViewModel>()

    private val args: CheckOutOrderFragmentArgs by navArgs()
    private val totalPrice by lazy { args.totalPrice }
    private val cartItems by lazy { args.cartItems.asList() }

    private var userLocation = ""

    private lateinit var paymentLauncher: PaymentLauncher
    private lateinit var paymentIntentClientSecret: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOutOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeListeners()
        initPaymentLauncher()
        startCheckout()

        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, p1 ->
                if (p1 == binding.btnVisa.id)
                    binding.cardInput.visibility = View.VISIBLE
                else
                    binding.cardInput.visibility = View.INVISIBLE
            }
            btnPlaceOrder.setOnClickListener { orderNow() }
        }
    }

    private fun observeListeners() {
        // gt user information from firebase to get user location and save in userLocation object to pass with user order .
        userViewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe user: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        userLocation = it.location
                    }
                    Log.d(TAG, "observe user done: ")
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe user error: ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

        // observe to get payment intent client secret to start payment process.
        checkoutViewModel.paymentIntent.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe payment intent :loading")
                }
                is Resource.Success -> {
                    paymentIntentClientSecret = response.data!!
                    Log.d(TAG, "observe payment intent :success")
                }
                is Resource.Error -> {
                    Log.e(TAG, ": observe payment intent :error ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }
        // observe if payment process successfully after order uploaded and the money sent successfully.
        checkoutViewModel.order.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe order: loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "observe order : success")
                    response.data?.let {
                        findNavController().navigate(
                            CheckOutOrderFragmentDirections.actionCheckOutOrderFragmentToOrderDetailsFragment(
                                it
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, "observe order error : ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }
    }


    private fun initPaymentLauncher() {
        val paymentConfiguration =
            PaymentConfiguration.getInstance(requireContext().applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this, paymentConfiguration.publishableKey, paymentConfiguration.stripeAccountId
        ) {
            val isOrderSubmitted = when (it) {
                is PaymentResult.Completed -> true
                else -> false
            }
            if (isOrderSubmitted)
                checkoutViewModel.uploadOrder(userLocation, totalPrice, cartItems)
        }
    }

    private fun startCheckout() {
        val amount = (totalPrice * 100).toString()
        OnlinePayment(amount).let {
            checkoutViewModel.createPaymentIntent(it)
        }
    }

    private fun orderNow() {
//        // check if payment card widget is hidden and not validate to show a message to user to complete payment process.
//        if (!cardWidget.isShown && !cardWidget.validateCardNumber()) {
//            showToast("please select payment method")
//            return
//        }
        // start payment process with the previous cost by the products selected on cart fragment.
        binding.cardInput.paymentMethodCreateParams?.let { params ->
            val confirmParams =
                ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                    params,
                    paymentIntentClientSecret
                )
            paymentLauncher.confirm(confirmParams)
            Log.d(TAG, "orderNow: loading")
        }
    }
}