package com.example.grocery.ui.base.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.repositories.StoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: StoreRepositoryImpl) :
    ViewModel() {

    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity

    private val _addToCart = MutableStateFlow("")
    val addToCart: StateFlow<String> = _addToCart

    fun increase() {
        _quantity.value += 1
    }

    fun decrease() {
        if (_quantity.value > 0)
            _quantity.value -= 1
    }

    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            _addToCart.value = repository.addItem(cart).toString()
            _addToCart.value = "added"
        }
    }

}