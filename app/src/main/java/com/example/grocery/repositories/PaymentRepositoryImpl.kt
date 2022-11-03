package com.example.grocery.repositories

import com.example.grocery.models.payment.*
import com.example.grocery.network.ApiKey
import com.example.grocery.network.PaymentApi
import com.example.grocery.other.Resource
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val api: PaymentApi) :
    PaymentRepository {

    override suspend fun authenticationRequest(): Resource<TokenResponse> {
        val response = api.authenticationRequest(ApiKey())
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.code().toString() + " ${response.message()} $response")
    }

    override suspend fun orderRegistration(paymentRequest: PaymentRequest): Resource<PaymentResponse> {
        val response = api.orderRegistration(paymentRequest)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.code().toString() + " ${response.message()} $response")
    }

    override suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest): Resource<PaymentKeyResponse> {
        val response = api.paymentKeyRequest(paymentKeyRequest)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.code().toString() + " ${response.message()} $response")
    }
}