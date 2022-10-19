package com.example.grocery.models

data class PaymentKeyRequest(
    val auth_token: String,
    val amount_cents: String,
    val expiration: Int,
    val order_id: Int,
    val currency: String,
    val integration_id: String,
    val lock_order_when_paid: Boolean
)
