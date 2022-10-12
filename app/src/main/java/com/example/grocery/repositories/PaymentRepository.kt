package com.example.grocery.repositories

import com.example.grocery.models.OnlinePayment
import com.example.grocery.other.Resource

interface PaymentRepository {

    suspend fun createPaymentIntent(stripe: OnlinePayment): Resource<String>

}