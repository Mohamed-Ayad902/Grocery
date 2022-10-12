package com.example.grocery.ui.base.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.repositories.LocalRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: LocalRepositoryImpl) :
    ViewModel() {

    private val _addedToCart = MutableStateFlow("")
    val addedToCart: StateFlow<String> = _addedToCart

    fun getSavedLaptops() = repository.getAllSavedLaptops()
    fun getSavedProducts() = repository.getAllSavedProducts()

    fun deleteLaptop(laptop: Laptop) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedLaptop(laptop)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedProduct(product)
        }
    }

    fun moveAllToCart() {
        viewModelScope.launch(Dispatchers.IO) {
            addAllLaptopsToCart()
            addAllProductsToCart()
            _addedToCart.value = "Added all to cart"
        }
    }

    private suspend fun addAllLaptopsToCart() {
        getSavedLaptops().collectLatest { list ->
            list.forEach {
                repository.addItem(Cart(-1, it.name, it.image, it.id, it.brand, it.price, 1))
            }
            repository.deleteAllSavedLaptops()
        }
    }

    private suspend fun addAllProductsToCart() {
        getSavedProducts().collectLatest { list ->
            list.forEach {
                repository.addItem(Cart(-1, it.name, it.image, it.id, it.brand, it.price, 1))
            }
            repository.deleteAllSavedProducts()
        }
    }

    fun addLaptop(laptop: Laptop) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLaptop(laptop)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProduct(product)
        }
    }
}