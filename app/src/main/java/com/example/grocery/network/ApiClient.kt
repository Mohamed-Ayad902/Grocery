package com.example.grocery.network

import com.example.grocery.models.OnlinePayment
import com.example.grocery.other.Resource
import javax.inject.Inject

class ApiClient
@Inject
constructor(
    private val apiService: ApiService
) {

    suspend fun createPaymentIntent(
        stripe: OnlinePayment
    ): Resource<String> {
        val request = apiService.createPaymentIntent(stripe)
        return if (!request.isSuccessful) {
            Resource.Error(request.toString())
        } else {
            val responseData = request.body()
            val paymentIntentClientSecret: String? =
                responseData?.get("clientSecret")
            Resource.Success(paymentIntentClientSecret!!)
        }
    }
}
