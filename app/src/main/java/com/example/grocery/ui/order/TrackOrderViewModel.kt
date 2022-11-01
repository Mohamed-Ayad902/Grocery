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
class TrackOrderViewModel @Inject constructor(private val repository: StoreRepositoryImpl) : ViewModel() {

    private val _order: MutableStateFlow<Resource<Order>> = MutableStateFlow(Resource.Idle())
    val order: StateFlow<Resource<Order>> = _order

    fun getOrderById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _order.value = Resource.Loading()
            _order.value = repository.getOrderById(id)
        }
    }

}