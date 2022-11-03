package com.example.grocery.models

import java.io.Serializable

data class Order(
    val id: String = "",
    val userId: String = "",
    val time: Long = 0L,
    var latitude:Double = 0.0,
    var longitude:Double = 0.0,
    val status: Status = Status.PLACED,
    var totalPrice: Int = 0,
    val products: List<Cart> = emptyList(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH
) : Serializable

enum class Status {
    PLACED,
    CONFIRMED,
    COMING,
    DELIVERED
}

enum class PaymentMethod {
    CASH,
    ONLINE
}