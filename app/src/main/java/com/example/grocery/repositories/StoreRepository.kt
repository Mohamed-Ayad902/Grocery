package com.example.grocery.repositories

import com.example.grocery.models.*
import com.example.grocery.other.Resource

interface StoreRepository {

    suspend fun getOffers(): Resource<List<Offer>>

    suspend fun getCategories(): Resource<List<Category>>

    suspend fun getHotAndNewItems(): Resource<List<HotAndNew>>

    suspend fun getAllLaptops(): Resource<List<Laptop>>

    suspend fun getAllProductsByCategory(category: String): Resource<List<Product>>

    suspend fun getLaptopById(id: String): Resource<Laptop>

    suspend fun getProductById(id: String): Resource<Product>

    suspend fun uploadOrder(
        orderLocation: String,
        totalPrice: Int,
        products: List<Cart>,
        paymentMethod: PaymentMethod
    ): Resource<Order>

    suspend fun getOrderById(id: String): Resource<Order>

    suspend fun getUserOrders(): Resource<List<Order>>

//    suspend fun searchLaptops(query: String): Resource<List<Laptop>>
//
//    suspend fun searchProducts(query: String): Resource<List<Product>>

    suspend fun search(query: String): Resource<LaptopsProducts>

}