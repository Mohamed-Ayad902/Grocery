package com.example.grocery.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.models.Order
import com.example.grocery.models.PaymentMethod
import com.example.grocery.other.Resource
import com.example.grocery.repositories.LocalRepositoryImpl
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
    private val repository: StoreRepositoryImpl,
    private val localRepositoryImpl: LocalRepositoryImpl
) : ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Idle())
    val order: StateFlow<Resource<Order>> = _order

    fun deleteCartItems(){
        viewModelScope.launch {
            localRepositoryImpl.deleteCartItems()
        }
    }

    fun uploadOrder(orderLocation: String, totalPrice: Int, products: List<Cart>,paymentMethod: PaymentMethod) {
        _order.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _order.value = repository.uploadOrder(orderLocation, totalPrice, products,paymentMethod)
            delay(500)
            _order.value = Resource.Idle()
        }
    }

}