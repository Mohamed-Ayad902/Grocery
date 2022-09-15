package com.example.grocery.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocery.other.Constants.PRICE_TYPE_PIECE
import java.io.Serializable

@Entity(tableName = "favorite_table")
data class Product(
    @PrimaryKey var id: String = "",
    val image: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = PRICE_TYPE_PIECE,
    val priceType: String = "",
    val price: Double = 0.0,
    val fat: Double = 0.0,
    val protein: Double = 0.0,
    val carb: Double = 0.0,
    val cal: Double = 0.0,
    val isOffer: Boolean = false
) : Serializable