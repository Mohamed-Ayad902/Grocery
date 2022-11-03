package com.example.grocery.models.payment

import com.google.gson.annotations.Expose

class PaymentKeyResponse(
    val token: String,
    @Expose
    val message: String? = null
)