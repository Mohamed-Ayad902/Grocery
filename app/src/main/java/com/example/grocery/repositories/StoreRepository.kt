package com.example.grocery.repositories

import com.example.grocery.models.*
import com.example.grocery.other.Resource
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun getOffers(): Resource<List<Offer>>

    suspend fun getCategories(): Resource<List<Category>>

    suspend fun getLaptop(id: String): Resource<Laptop>

    suspend fun getProduct(id: String): Resource<Product>


    // local database
    // favorite
    suspend fun saveLaptop(laptop: Laptop)

    suspend fun deleteSavedLaptop(laptop: Laptop)

    fun getLaptopIfSaved(id: String): Laptop?

    fun getLaptopFlowIfSaved(id: String): Flow<Laptop?>

    fun getAllSavedLaptops(): Flow<List<Laptop>>

    fun deleteAllSavedLaptops()

    // cart

    suspend fun addItem(cart: Cart)

    suspend fun deleteItem(cart: Cart)

    suspend fun updateItem(cart: Cart)

    fun getCartItems(): Flow<List<Cart>>


}