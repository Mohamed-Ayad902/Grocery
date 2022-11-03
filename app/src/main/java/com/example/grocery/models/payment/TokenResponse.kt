package com.example.grocery.models.payment

data class TokenResponse(
    val profile: Profile,
    val token: String
)