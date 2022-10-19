package com.example.grocery.models

data class PaymentRequest(
    val amount_cents: String,
    val auth_token: String,
    val currency: String ,
    val delivery_needed: Boolean,
    val items: List<Cart>,
    val merchant_order_id: Int,
    val shipping_data: ShippingData
)