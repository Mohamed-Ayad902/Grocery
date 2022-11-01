package com.example.grocery.network

import com.example.grocery.other.Constants

data class ApiKey(val api_key: String = Constants.PAY_MOB_API_KEY) :
    java.io.Serializable