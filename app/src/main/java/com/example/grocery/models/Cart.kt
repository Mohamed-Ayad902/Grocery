package com.example.grocery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class Cart(
    val productName: String,
    val productImage: String,
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val productId: String,
    val productBrand: String,
    val productPrice: Int,
    var productQuantity: Int,
)