package com.example.grocery.ui.base.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import com.example.grocery.other.Constants
import com.example.grocery.repositories.LocalRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: LocalRepositoryImpl) :
    ViewModel() {

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity

    private val _addToCart = MutableStateFlow("")
    val addToCart: StateFlow<String> = _addToCart


    fun increase() {
        _quantity.value++
    }

    fun decrease() {
        if (_quantity.value > 1)
            _quantity.value--
    }

    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            _addToCart.value = repository.addItem(cart).toString()
            _addToCart.value = "added"
        }
    }

    fun favoriteClicked(laptop: Laptop? = null, product: Product? = null, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (category == Constants.CATEGORY_LAPTOP) {
                laptop?.let {
                    val isSaved = repository.getLaptopIfSaved(laptop.id)
                    if (isSaved == null)
                        repository.saveLaptop(laptop)
                    else
                        repository.deleteSavedLaptop(laptop)
                }
            } else {
                product?.let {
                    val isSaved = repository.getProductIfSaved(product.id)
                    if (isSaved == null)
                        repository.saveProduct(product)
                    else
                        repository.deleteSavedProduct(product)
                }
            }

        }
    }

    fun isLaptopSaved(id: String) =
        repository.getLaptopFlowIfSaved(id)

    fun isProductSaved(id: String) =
        repository.getProductFlowIfSaved(id)

}