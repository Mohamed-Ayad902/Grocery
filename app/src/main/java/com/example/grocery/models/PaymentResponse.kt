package com.example.grocery.models

data class PaymentResponse(
    val amount_cents: Int,
    val collector: String,
    val created_at: String,
    val currency: String,
    val delivery_needed: Boolean,
    val id: Int,
    val is_payment_locked: String,
    val items: List<Cart>,
    val merchant: Merchant,
    val merchant_order_id: String,
    val paid_amount_cents: Int,
    val shipping_data: ShippingData,
    val wallet_notification: String
)