package com.example.grocery.models

import java.io.Serializable

data class Order(
    val id: String,
    val userId: String,
    val time: Long,
    val orderLocation: String,
    val status: Status,
    var totalPrice: Int,
    val products: List<Cart>
) : Serializable

enum class Status {
    PLACED,
    CONFIRMED,
    COMING,
    DELIVERED
}