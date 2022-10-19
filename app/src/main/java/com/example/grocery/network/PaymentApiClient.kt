package com.example.grocery.network

import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentRequest
import com.example.grocery.models.PaymentResponse
import com.example.grocery.other.Resource
import javax.inject.Inject

class PaymentApiClient @Inject constructor(private val paymentApi: PaymentApi) {

    suspend fun authenticationRequest(): Resource<String> {
        val response = paymentApi.authenticationRequest()
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun orderRegistration(paymentRequest: PaymentRequest): Resource<PaymentResponse> {
        val response = paymentApi.orderRegistration(paymentRequest)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest): Resource<String> {
        val response = paymentApi.paymentKeyRequest(paymentKeyRequest)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}
