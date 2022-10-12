package com.example.grocery.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.models.OnlinePayment
import com.example.grocery.models.Order
import com.example.grocery.other.Resource
import com.example.grocery.repositories.PaymentRepositoryImpl
import com.example.grocery.repositories.StoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl,
    private val repository: StoreRepositoryImpl
) : ViewModel() {

    private val _paymentIntent: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Idle())
    val paymentIntent: StateFlow<Resource<String>> = _paymentIntent

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Idle())
    val order: StateFlow<Resource<Order>> = _order

    fun createPaymentIntent(
        stripe: OnlinePayment
    ) {
        _paymentIntent.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _paymentIntent.value = paymentRepository.createPaymentIntent(stripe)
        }
    }

    fun uploadOrder(orderLocation: String, totalPrice: Int, products: List<Cart>) {
        _order.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _order.value = repository.uploadOrder(orderLocation, totalPrice, products)
            delay(500)
            _order.value = Resource.Idle()
        }
    }

}