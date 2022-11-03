package com.example.grocery.repositories

import com.example.grocery.models.payment.*
import com.example.grocery.other.Resource

interface PaymentRepository {

    suspend fun authenticationRequest(): Resource<TokenResponse>

    suspend fun orderRegistration(paymentRequest: PaymentRequest): Resource<PaymentResponse>

    suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest): Resource<PaymentKeyResponse>

}