package com.example.grocery.ui.order


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocery.databinding.FragmentCheckOutOrderBinding
import com.example.grocery.models.BillingData
import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentMethod
import com.example.grocery.models.PaymentRequest
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.example.grocery.ui.account.UserViewModel
import com.example.grocery.ui.account.collectLatest
import com.example.grocery.ui.payment.PaymentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paymob.acceptsdk.*
import com.paymob.acceptsdk.IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "CheckOutOrderFragment mohamed"

@AndroidEntryPoint
class CheckOutOrderFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCheckOutOrderBinding

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val paymentViewModel: PaymentViewModel by viewModels()

    private val args: CheckOutOrderFragmentArgs by navArgs()
    private val totalPrice by lazy { args.totalPrice }
    private val cartItems by lazy { args.cartItems.asList() }

    private var userLocation = ""
    private lateinit var token: String
    private var orderId = 0
    private lateinit var paymentKey: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOutOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeListeners()

        binding.apply {
            btnPlaceOrder.setOnClickListener { orderNow() }
            tvTotalPrice.text = totalPrice.toString()
        }
    }

    private fun startPaymentProcess() {
        Log.e(TAG, "startPaymentProcess: ")
        paymentViewModel.authenticationRequest()

        paymentViewModel.auth.collectLatest(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    Log.w(TAG, "startPaymentProcess: observe auth :loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(TAG, "startPaymentProcess: observe auth: success ${it.token}")
                        token = it.token
                        paymentViewModel.orderRegistration(
                            PaymentRequest(
                                "${totalPrice * 100}",
                                token
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "startPaymentProcess: observe auth: error ->> ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

        paymentViewModel.orderRegistration.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "startPaymentProcess: observe order registration: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(
                            TAG,
                            "startPaymentProcess: observe order registration:success: ${response.data}"
                        )
                        orderId = it.id
                        paymentViewModel.paymentKeyRequest(
                            PaymentKeyRequest(
                                token,
                                "${totalPrice * 100}",
                                order_id = orderId.toString(),
                                billingData = BillingData()
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e(
                        TAG,
                        "startPaymentProcess:observe order registration:error : ${response.message}"
                    )
                }
                is Resource.Idle -> {}
            }
        }

        paymentViewModel.paymentKeyRequest.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "startPaymentProcess: observe payment key :loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(
                            TAG,
                            "startPaymentProcess: observe payment key: success ::: ${response.data}"
                        )
                        paymentKey = it.token
                        navigateToPayActivity(it.token)
                    }
                }
                is Resource.Error -> {
                    Log.e(
                        TAG,
                        "startPaymentProcess: observe payment key: error -- ${response.message}"
                    )
                }
                is Resource.Idle -> {}
            }
        }

    }

    private fun observeListeners() {
        userViewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe user: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        userLocation = it.location
                        binding.tvDelivery.text = it.location
                    }
                    Log.d(TAG, "observe user done: ")
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe user error: ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

        checkoutViewModel.order.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "observe order: loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "observe order : success")
                    response.data?.let {
                        showToast("Your order has been placed!")
                        findNavController().navigate(
                            CheckOutOrderFragmentDirections.actionCheckOutOrderFragmentToOrderDetailsFragment(
                                it
                            )
                        )
                        checkoutViewModel.deleteCartItems()
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, "observe order error : ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }
    }

    private fun navigateToPayActivity(token: String) {
        Log.e(TAG, "navigateToPayActivity: token ->> $token")
        Intent(requireContext(), PayActivity::class.java).apply {
            putExtra(PayActivityIntentKeys.PAYMENT_KEY, token)
            putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification")
            putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, false)
            putExtra("language", "en")
            activityResultLauncher.launch(this)
        }
    }

    private fun orderNow() {
        if (binding.btnVisa.isChecked) {
            startPaymentProcess()
        } else {
            checkoutViewModel.uploadOrder(userLocation, totalPrice, cartItems, PaymentMethod.CASH)
        }
    }


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "resuuuuuuuult: ${it.resultCode}")
            val extras = it.data?.extras
            when (it.resultCode) {

                IntentConstants.USER_CANCELED -> {
                    // User canceled and did no payment request was fired
                    ToastMaker.displayShortToast(requireActivity(), "User canceled!!")
                    Log.e(TAG, "payment response: user canceled")
                }
                IntentConstants.MISSING_ARGUMENT -> {
                    Log.e(TAG, "payment response: missing args")
                    // You forgot to pass an important key-value pair in the intent's extras
                    ToastMaker.displayShortToast(
                        requireActivity(),
                        "Missing Argument == " + extras!!.getString(IntentConstants.MISSING_ARGUMENT_VALUE)
                    )
                }
                IntentConstants.TRANSACTION_ERROR -> {
                    Log.e(TAG, "payment response: transaction error")
                    // An error occurred while handling an APIs response
                    ToastMaker.displayShortToast(
                        requireActivity(),
                        "Reason == " + extras!!.getString(IntentConstants.TRANSACTION_ERROR_REASON)
                    )
                }
                IntentConstants.TRANSACTION_REJECTED -> {
                    Log.e(TAG, "payment response: transaction rejected")
                    // User attempted to pay but their transaction was rejected
                    // Use the static keys declared in PayResponseKeys to extract the fields you want
                    ToastMaker.displayShortToast(
                        requireActivity(),
                        extras!!.getString(PayResponseKeys.DATA_MESSAGE)
                    )
                }
                IntentConstants.TRANSACTION_SUCCESSFUL, TRANSACTION_SUCCESSFUL_CARD_SAVED -> {
                    // User finished their payment successfully
                    // Use the static keys declared in PayResponseKeys to extract the fields you want
                    ToastMaker.displayShortToast(
                        requireActivity(),
                        extras!!.getString(PayResponseKeys.DATA_MESSAGE)
                    )
                    checkoutViewModel.uploadOrder(
                        userLocation,
                        totalPrice,
                        cartItems,
                        PaymentMethod.ONLINE
                    )
                }
            }
        }

}