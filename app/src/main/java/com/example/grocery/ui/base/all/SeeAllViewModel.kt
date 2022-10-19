package com.example.grocery.ui.base.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.Constants
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
class SeeAllViewModel @Inject constructor(
    private val storeRepository: StoreRepositoryImpl,
    private val localRepository: LocalRepositoryImpl
) : ViewModel() {

    private val _products: MutableStateFlow<Resource<List<Product>>> =
        MutableStateFlow(Resource.Idle())
    val products: StateFlow<Resource<List<Product>>> = _products

    private val _laptops: MutableStateFlow<Resource<List<Laptop>>> =
        MutableStateFlow(Resource.Idle())
    val laptops: StateFlow<Resource<List<Laptop>>> = _laptops


    private val _laptop = MutableStateFlow<Resource<Laptop>>(Resource.Idle())
    val laptop: StateFlow<Resource<Laptop>> = _laptop

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Idle())
    val product: StateFlow<Resource<Product>> = _product

    private val _addToCart = MutableStateFlow("")
    val addToCart: StateFlow<String> = _addToCart

    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            _addToCart.value = localRepository.addItem(cart).toString()
            _addToCart.value = "added"
            delay(500)
            _addToCart.value = ""
        }
    }

    fun seeAllProductsByCategory(category: String) {
        if (category == Constants.CATEGORY_LAPTOP) {
            getLaptops()
        } else {
            getProducts(category)
        }
    }

    private fun getLaptops() {
        viewModelScope.launch(Dispatchers.IO) {
            _laptops.value = Resource.Loading()
            _laptops.value = storeRepository.getAllLaptops()
        }
    }

    private fun getProducts(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = Resource.Loading()
            _products.value = storeRepository.getAllProductsByCategory(category)
        }
    }

    fun getLaptop(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _laptop.value = Resource.Loading()
            _laptop.value = storeRepository.getLaptopById(id)
            delay(500)
            _laptop.value = Resource.Idle()
        }
    }

    fun getProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.value = Resource.Loading()
            _product.value = storeRepository.getProductById(id)
            delay(500)
            _product.value = Resource.Idle()
        }
    }

}