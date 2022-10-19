package com.example.grocery.repositories

import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentRequest
import com.example.grocery.models.PaymentResponse
import com.example.grocery.other.Resource

interface PayMopRepository {

    suspend fun authenticationRequest(): Resource<String>

    suspend fun orderRegistration(paymentRequest: PaymentRequest): Resource<PaymentResponse>

    suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest): Resource<String>

}