package com.example.grocery.network

import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentRequest
import com.example.grocery.models.PaymentResponse
import com.example.grocery.other.Constants.PAY_MOB_API_KEY
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApi {

    @Headers("Accept: application/json; charset=utf-8")
    @POST("auth/tokens")                    // returns token as a string
    suspend fun authenticationRequest(@Query("api_key") api_key: String = PAY_MOB_API_KEY): Response<String>

    @Headers("Accept: application/json; charset=utf-8")
    @POST("ecommerce/orders")
    suspend fun orderRegistration(
        paymentRequest: PaymentRequest
    ): Response<PaymentResponse>


    @Headers("Accept: application/json; charset=utf-8")
    @POST("acceptance/payment_keys")        // returns token as a string
    suspend fun paymentKeyRequest(
        paymentKeyRequest: PaymentKeyRequest
    ): Response<String>


}