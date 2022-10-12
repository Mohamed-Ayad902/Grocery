package com.example.grocery.network

import com.example.grocery.models.OnlinePayment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Accept: application/json; charset=utf-8")
    @POST("create-payment-intent")
    suspend fun createPaymentIntent(@Body body: OnlinePayment): Response<Map<String, String>>

}