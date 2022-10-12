package com.example.grocery.models

data class OnlinePayment(
    val amount: String,
    val currency: String = "usd",
    val paymentMethodType: String = "card"
)
