package com.example.grocery.ui.base.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Category
import com.example.grocery.models.Laptop
import com.example.grocery.models.Offer
import com.example.grocery.models.Product
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.example.grocery.repositories.StoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storeRepository: StoreRepositoryImpl) :
    ViewModel() {

    private val _offers = MutableStateFlow<Resource<List<Offer>>>(Resource.Idle())
    val offers: StateFlow<Resource<List<Offer>>> = _offers

    private val _categories = MutableStateFlow<Resource<List<Category>>>(Resource.Idle())
    val categories: StateFlow<Resource<List<Category>>> = _categories

    private val _laptop = MutableStateFlow<Resource<Laptop>>(Resource.Idle())
    val laptop: StateFlow<Resource<Laptop>> = _laptop

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Idle())
    val product: StateFlow<Resource<Product>> = _product


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

    fun getProductByOffer(offer: Offer) {
        viewModelScope.launch(Dispatchers.IO) {
            if (offer.productCategory == Constants.CATEGORY_LAPTOP)
                getLaptop(offer.productId)
            else
                getProduct(offer.productId)
        }
    }

    fun getLaptop(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _laptop.value = Resource.Loading()
            _laptop.value = storeRepository.getLaptop(id)
            delay(500)
            _laptop.value = Resource.Idle()
        }
    }

    fun getProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.value = Resource.Loading()
            _product.value = storeRepository.getProduct(id)
        }
    }


}