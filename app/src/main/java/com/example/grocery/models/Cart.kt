package com.example.grocery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class Cart(
    val productName: String,
    val productImage: String,
    @PrimaryKey(autoGenerate = false)
    val productId: String,
    val productPrice: Int,
    val productQuantity: Int,
)