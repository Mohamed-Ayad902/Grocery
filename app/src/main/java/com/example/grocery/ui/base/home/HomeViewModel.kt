package com.example.grocery.ui.base.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.*
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
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepositoryImpl,
    private val localRepository: LocalRepositoryImpl
) : ViewModel() {

    private val _addToCart = MutableStateFlow("")
    val addToCart: StateFlow<String> = _addToCart

    private val _offers = MutableStateFlow<Resource<List<Offer>>>(Resource.Idle())
    val offers: StateFlow<Resource<List<Offer>>> = _offers

    private val _categories = MutableStateFlow<Resource<List<Category>>>(Resource.Idle())
    val categories: StateFlow<Resource<List<Category>>> = _categories

    private val _hotAndNew = MutableStateFlow<Resource<List<HotAndNew>>>(Resource.Idle())
    val hotAndNew: StateFlow<Resource<List<HotAndNew>>> = _hotAndNew

    private val _laptop = MutableStateFlow<Resource<Laptop>>(Resource.Idle())
    val laptop: StateFlow<Resource<Laptop>> = _laptop

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Idle())
    val product: StateFlow<Resource<Product>> = _product


    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            _addToCart.value = localRepository.addItem(cart).toString()
            _addToCart.value = "added"
        }
    }

    fun getOffers() {
        viewModelScope.launch(Dispatchers.IO) {
            _offers.value = Resource.Loading()
            _offers.value = storeRepository.getOffers()
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _categories.value = Resource.Loading()
            _categories.value = storeRepository.getCategories()
        }
    }

    fun getHotAndNew() {
        viewModelScope.launch(Dispatchers.IO) {
            _hotAndNew.value = Resource.Loading()
            _hotAndNew.value = storeRepository.getHotAndNewItems()
        }
    }

    fun getProductByCategory(category: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (category == Constants.CATEGORY_LAPTOP)
                getLaptop(id)
            else
                getProduct(id)
        }
    }

    private fun getLaptop(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _laptop.value = Resource.Loading()
            _laptop.value = storeRepository.getLaptopById(id)
            delay(500)
            _laptop.value = Resource.Idle()
        }
    }

    private fun getProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.value = Resource.Loading()
            _product.value = storeRepository.getProductById(id)
            delay(500)
            _product.value = Resource.Idle()
        }
    }


}