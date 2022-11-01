package com.example.grocery.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey val
    id: String= "",
    val image: String = "",
    val name: String = "",
    var searchKeywords: List<String> = emptyList(),
    val brand: String = "",
    val category: String = "",
    val description: String = "",
    val price: Int = 0,
) : Serializable