package com.example.grocery.ui.base.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Laptop
import com.example.grocery.models.LaptopsProducts
import com.example.grocery.models.Product
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
class ExploreViewModel @Inject constructor(private val repository: StoreRepositoryImpl) :
    ViewModel() {

    private val _laptopsProducts: MutableStateFlow<Resource<LaptopsProducts>> =
        MutableStateFlow(Resource.Idle())
    val laptopsProducts: StateFlow<Resource<LaptopsProducts>> = _laptopsProducts


    private val _laptop = MutableStateFlow<Resource<Laptop>>(Resource.Idle())
    val laptop: StateFlow<Resource<Laptop>> = _laptop

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Idle())
    val product: StateFlow<Resource<Product>> = _product

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _laptopsProducts.value = Resource.Loading()
            _laptopsProducts.value = repository.search(query)
        }
    }

    fun getLaptop(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _laptop.value = Resource.Loading()
            _laptop.value = repository.getLaptopById(id)
            delay(500)
            _laptop.value = Resource.Idle()
        }
    }

    fun getProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.value = Resource.Loading()
            _product.value = repository.getProductById(id)
            delay(500)
            _product.value = Resource.Idle()
        }
    }

}