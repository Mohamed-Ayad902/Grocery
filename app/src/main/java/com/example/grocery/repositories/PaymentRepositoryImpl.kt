package com.example.grocery.repositories

import com.example.grocery.models.OnlinePayment
import com.example.grocery.network.ApiClient
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    PaymentRepository {

    override suspend fun createPaymentIntent(stripe: OnlinePayment) =
        apiClient.createPaymentIntent(stripe)

}