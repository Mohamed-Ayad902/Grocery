package com.example.grocery.models

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "products_table")
data class Product(
    val image: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val price: Int = 0,
) : Serializable