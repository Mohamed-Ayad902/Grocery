package com.example.grocery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "cart_table")
@Parcelize
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val productName: String,
    val productImage: String,
    val productId: String,
    val productBrand: String,
    val productPrice: Int,
    var productQuantity: Int,
) : Serializable, Parcelable