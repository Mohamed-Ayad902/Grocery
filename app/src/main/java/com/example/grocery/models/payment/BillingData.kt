package com.example.grocery.models.payment

data class BillingData(
    val apartment: String = "NA",
    val building: String = "NA",
    val city: String = "NA",
    val country: String = "NA",
    val email: String = "test@example.com",
    val first_name: String = "firstName",
    val floor: String = "NA",
    val last_name: String = "lastName",
    val phone_number: String = "0123456789",
    val postal_code: String = "NA",
    val shipping_method: String = "NA",
    val state: String = "NA",
    val street: String = "NA"
)