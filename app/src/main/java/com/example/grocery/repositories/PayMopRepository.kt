package com.example.grocery.repositories

import com.example.grocery.models.*
import com.example.grocery.other.Resource

interface PayMopRepository {

    suspend fun authenticationRequest(): Resource<TokenResponse>

    suspend fun orderRegistration(paymentRequest: PaymentRequest): Resource<PaymentResponse>

    suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest): Resource<PaymentKeyResponse>

}