package com.example.grocery.models.payment

data class PaymentKeyRequest(
    val auth_token: String,
    val amount_cents: String,
    val expiration: Int = 3600,
    val order_id: String,
    val billingData: BillingData,
    val currency: String = "EGP",
    val integration_id: Int = 2959914,
    val lock_order_when_paid: String = "false"
)