package com.example.grocery.models

data class Offer(
    val productId: String = "",
    val productCategory: String = "",
    val image: String = "",
    val oldPrice: Int = 0,
    val newPrice: Int = 0,
    val discount: Double = 0.0,
)