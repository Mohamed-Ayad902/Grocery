package com.example.grocery.network

import com.example.grocery.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {


    @POST("auth/tokens")
    suspend fun authenticationRequest(@Body apiKey: ApiKey): Response<TokenResponse>

    @POST("ecommerce/orders")
    suspend fun orderRegistration(@Body paymentRequest: PaymentRequest): Response<PaymentResponse>

    @POST("acceptance/payment_keys")
    suspend fun paymentKeyRequest(@Body paymentKeyRequest: PaymentKeyRequest): Response<PaymentKeyResponse>


}