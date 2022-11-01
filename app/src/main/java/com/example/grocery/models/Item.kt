package com.example.grocery.models

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("amount_cents")
    val amountCents: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: String
)