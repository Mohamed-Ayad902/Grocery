package com.example.grocery.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.PaymentKeyRequest
import com.example.grocery.models.PaymentRequest
import com.example.grocery.models.PaymentResponse
import com.example.grocery.other.Resource
import com.example.grocery.repositories.PayMopRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val payMobRepository: PayMopRepositoryImpl
) : ViewModel() {

    private val _auth: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Idle())
    val auth: StateFlow<Resource<String>> = _auth

    private val _orderRegistration: MutableStateFlow<Resource<PaymentResponse>> =
        MutableStateFlow(Resource.Idle())
    val orderRegistration: StateFlow<Resource<PaymentResponse>> = _orderRegistration

    private val _paymentKeyRequest: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Idle())
    val paymentKeyRequest: StateFlow<Resource<String>> = _paymentKeyRequest


    fun authenticationRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            _auth.value = Resource.Loading()
            _auth.value = payMobRepository.authenticationRequest()
        }
    }

    fun orderRegistration(paymentRequest: PaymentRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _orderRegistration.value = Resource.Loading()
            _orderRegistration.value = payMobRepository.orderRegistration(paymentRequest)
        }
    }

    fun paymentKeyRequest(paymentKeyRequest: PaymentKeyRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _paymentKeyRequest.value = Resource.Loading()
            _paymentKeyRequest.value = payMobRepository.paymentKeyRequest(paymentKeyRequest)
        }
    }


}