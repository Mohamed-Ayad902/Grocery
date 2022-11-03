package com.example.grocery.models

import java.io.Serializable

data class User(
    var id: String = "",
    var name: String = "",
    val image: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phoneNumber: String = "",
    val isAdmin: Boolean = false
) : Serializable