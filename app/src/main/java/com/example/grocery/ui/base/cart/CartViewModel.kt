package com.example.grocery.ui.base.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.repositories.StoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: StoreRepositoryImpl) : ViewModel() {


    fun getCartItems() = repository.getCartItems()

    fun increase(cart: Cart) {
        viewModelScope.launch {
            cart.productQuantity++
            repository.updateItem(cart)
        }
    }

    fun decrease(cart: Cart) {
        if (cart.productQuantity > 0)
            viewModelScope.launch {
                cart.productQuantity--
                repository.updateItem(cart)
            }
    }

    fun delete(cart: Cart) {
        viewModelScope.launch {
            repository.deleteItem(cart)
        }
    }

}