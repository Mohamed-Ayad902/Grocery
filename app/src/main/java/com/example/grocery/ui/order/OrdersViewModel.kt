package com.example.grocery.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Order
import com.example.grocery.other.Resource
import com.example.grocery.repositories.StoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repository: StoreRepositoryImpl) :
    ViewModel() {

    private val _orders: MutableStateFlow<Resource<List<Order>>> = MutableStateFlow(Resource.Idle())
    val orders: StateFlow<Resource<List<Order>>> = _orders

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch (Dispatchers.IO){
            _orders.value = Resource.Loading()
            _orders.value = repository.getUserOrders()
        }
    }

}