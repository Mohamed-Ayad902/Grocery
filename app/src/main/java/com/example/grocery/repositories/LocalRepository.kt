package com.example.grocery.repositories

import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    // favorite
    suspend fun saveLaptop(laptop: Laptop)
    suspend fun saveProduct(product: Product)

    suspend fun deleteSavedLaptop(laptop: Laptop)
    suspend fun deleteSavedProduct(product: Product)

    suspend fun getLaptopIfSaved(id: String): Laptop?
    suspend fun getProductIfSaved(id: String): Product?

    fun getLaptopFlowIfSaved(id: String): Flow<Laptop?>
    fun getProductFlowIfSaved(id: String): Flow<Product?>

    fun getAllSavedLaptops(): Flow<List<Laptop>>
    fun getAllSavedProducts(): Flow<List<Product>>

    fun deleteAllSavedLaptops()
    fun deleteAllSavedProducts()

    // cart

    suspend fun addItem(cart: Cart)

    suspend fun deleteItem(cart: Cart)

    suspend fun updateItem(cart: Cart)

    fun getCartItems(): Flow<List<Cart>>

}