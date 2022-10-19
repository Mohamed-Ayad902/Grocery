package com.example.grocery.repositories

import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentRequest
import com.example.grocery.network.PaymentApiClient
import javax.inject.Inject

class PayMopRepositoryImpl @Inject constructor(private val paymentApiClient: PaymentApiClient) :
    PayMopRepository {

    override suspend fun authenticationRequest() = paymentApiClient.authenticationRequest()

    override suspend fun orderRegistration(paymentRequest: PaymentRequest) =
        paymentApiClient.orderRegistration(paymentRequest)

    override suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest) =
        paymentApiClient.paymentKeyRequest(paymentKeyRequest)

}